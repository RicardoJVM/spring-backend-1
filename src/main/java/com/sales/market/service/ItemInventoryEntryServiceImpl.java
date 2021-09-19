package com.sales.market.service;

import com.sales.market.model.ItemInventory;
import com.sales.market.model.ItemInventoryEntry;
import com.sales.market.repository.GenericRepository;
import com.sales.market.repository.ItemInventoryEntryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@Service
public class ItemInventoryEntryServiceImpl extends GenericServiceImpl<ItemInventoryEntry> implements ItemInventoryEntryService{
    private final ItemInventoryEntryRepository repository;
    private final ItemInventoryService itemInventoryService;
    @Value("${spring.mail.username}")
    private String sender;
    private final JavaMailSender javaMailSender;

    public ItemInventoryEntryServiceImpl(ItemInventoryEntryRepository repository, ItemInventoryService itemInventoryService, JavaMailSender javaMailSender) {
        this.repository = repository;
        this.itemInventoryService = itemInventoryService;
        this.javaMailSender = javaMailSender;
    }

    @Override
    protected GenericRepository<ItemInventoryEntry> getRepository() {
        return repository;
    }

    @Override
    public ItemInventoryEntry bunchSave(ItemInventoryEntry itemInventoryEntry) {
        // Inventario a cambiar
        ItemInventory inventoryToChange = itemInventoryService.findById(itemInventoryEntry.getItemInventory().getId());
        // Cantidad de items que se acaban de mover
        BigDecimal amountBought = itemInventoryEntry.getQuantity();
        // Cantidad de dinero que se acaba de mover (cantidad vendida * precio unitario)
        BigDecimal moneyMovement = amountBought.multiply(itemInventoryEntry.getItemInventory().getItem().getUnitPrice());

        BigDecimal stockQuantityResult = new BigDecimal(0);
        BigDecimal stockMoneyResult = new BigDecimal(0);

        switch (itemInventoryEntry.getMovementType()) {
            case BUY:
                //Si fue compra sumar al inventario la cantidad de items comprados
                stockQuantityResult = inventoryToChange.getStockQuantity().add(amountBought);
                //Y restar del monto de dinero del inventario
                stockMoneyResult = inventoryToChange.getTotalPrice().subtract(moneyMovement);
                break;
            case SALE:
                //Si fue venta restar del inventario la cantidad de items vendidos
                stockQuantityResult = inventoryToChange.getStockQuantity().subtract(amountBought);
                //Y sumar al monto de dinero del inventario
                stockMoneyResult = inventoryToChange.getTotalPrice().add(moneyMovement);
                break;
            case REMOVED:
                //Si fue removido restar del inventario la cantidad de items deshechados
                stockQuantityResult = inventoryToChange.getStockQuantity().subtract(amountBought);
                //Pero no considerar movimiento de dinero
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + itemInventoryEntry.getMovementType());
        }
        // Aplica las modificaciones a ItemInventory y lo guarda
        inventoryToChange.setStockQuantity(stockQuantityResult);
        inventoryToChange.setTotalPrice(stockMoneyResult);
        itemInventoryService.save(inventoryToChange);
        // Guarda el movimiento de ItemInventoryEntry tambien
        ItemInventoryEntry itemInventoryEntrySaved = super.bunchSave(itemInventoryEntry);

        // Envio de email - Validar el umbral con el inventario ya modificado
        validateThreshold(inventoryToChange);

        return itemInventoryEntrySaved;
    }

    public void validateThreshold(ItemInventory itemInventory){
        try {
            verifyLimitOfStock(itemInventory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void verifyLimitOfStock(ItemInventory itemInventory) throws IOException {
        BigDecimal maxQuantity = itemInventory.getUpperBoundThreshold();
        BigDecimal minQuantity = itemInventory.getLowerBoundThreshold();

        String inventoryStatus = "";
        if (itemInventory.getStockQuantity().compareTo(maxQuantity) > 0) {
            inventoryStatus = "over";
        } else if (itemInventory.getStockQuantity().compareTo(minQuantity) < 0){
            inventoryStatus = "below";
        }

        if (inventoryStatus != "") {
            sendAlertEmail(itemInventory.getItem().getName(),inventoryStatus);
        }
    }

    public void sendAlertEmail(String itemName,String inventoryStatus) throws IOException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo("ricardini162@gmail.com");
        message.setSubject("Alerta en el estado del inventario del item:"+itemName);
        message.setText(readTxt(inventoryStatus));
        javaMailSender.send(message);
    }

    public String readTxt(String caseOfLimit) throws IOException {
        String inputFileName = "";
        InputStream is;
        String content= "";

        switch (caseOfLimit){
            case "below":
                inputFileName = "src/main/resources/BelowTheLimit.txt";
                is = new FileInputStream(inputFileName);
                content = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
                break;
            case "over":
                inputFileName = "src/main/resources/OverTheLimit.txt";
                is = new FileInputStream(inputFileName);
                content = StreamUtils.copyToString(is, StandardCharsets.UTF_8);
                break;
        }
        return content;
    }
}
