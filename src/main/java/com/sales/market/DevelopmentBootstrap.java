/**
 * @author: Edson A. Terceros T.
 */

package com.sales.market;

import com.sales.market.model.*;
import com.sales.market.repository.BuyRepository;
import com.sales.market.service.*;
import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@Component
public class DevelopmentBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private final BuyRepository buyRepository;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final ItemService itemService;
    private final ItemInstanceService itemInstanceService;
    private final ItemInventoryService itemInventoryService;
    private final ItemInventoryEntryService itemInventoryEntryService;

    SubCategory beverageSubCat = null;
    Item itemExtra1 = null;
    Item itemExtra2 = null;
    Item itemExtra3 = null;
    ItemInventory itemInventory1 = null;
    ItemInventory itemInventory2 = null;
    ItemInventory itemInventory3 = null;
    // injeccion evita hacer instancia   = new Clase();
    // bean pueden tener muchos campos y otros beans asociados

    public DevelopmentBootstrap(BuyRepository buyRepository, CategoryService categoryService,
                                SubCategoryService subCategoryService, ItemService itemService, ItemInstanceService itemInstanceService, ItemInventoryService itemInventoryService, ItemInventoryEntryService itemInventoryEntryService) {
        this.buyRepository = buyRepository;
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
        this.itemService = itemService;
        this.itemInstanceService = itemInstanceService;
        this.itemInventoryService = itemInventoryService;
        this.itemInventoryEntryService = itemInventoryEntryService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("evento de spring");
        /*   duplicacion de codigo
        Buy buy = new Buy();
        buy.setValue(BigDecimal.TEN);
        buyRespository.save(buy);
        Buy buy2 = new Buy();
        buy2.setValue(BigDecimal.ONE);
        buyRespository.save(buy);*/

        persistBuy(BigDecimal.TEN);
        persistBuy(BigDecimal.ONE);
        persistCategoriesAndSubCategories();
        Item maltinItem = persistItems(beverageSubCat);
        persistItemInstances(maltinItem);
        persistItems2(beverageSubCat);
        persistItemInventories();
        persistItemInventoryEntries();
    }

    private void persistItemInstances(Item maltinItem) {
        ItemInstance maltinItem1 = createItem(maltinItem, "SKU-77721106006158", 5D);
        ItemInstance maltinItem2 = createItem(maltinItem, "SKU-77721106006159", 5D);
        ItemInstance maltinItem3 = createItem(maltinItem, "SKU-77721106006160", 5D);
        ItemInstance maltinItem4 = createItem(maltinItem, "SKU-77721106006161", 5D);
        itemInstanceService.save(maltinItem1);
        itemInstanceService.save(maltinItem2);
        itemInstanceService.save(maltinItem3);
        itemInstanceService.save(maltinItem4);
    }

    private ItemInstance createItem(Item maltinItem, String sku, double price) {
        ItemInstance itemInstance = new ItemInstance();
        itemInstance.setItem(maltinItem);
        itemInstance.setFeatured(true);
        itemInstance.setPrice(price);
        itemInstance.setIdentifier(sku);
        return itemInstance;
    }

    private Item persistItems(SubCategory subCategory) {
        Item item = new Item();
        item.setCode("B-MALTIN");
        item.setName("MALTIN");
        item.setSubCategory(subCategory);
        item.setUnitPrice(new BigDecimal(5));
        /*try {
            item.setImage(ImageUtils.inputStreamToByteArray(getResourceAsStream("/images/maltin.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        this.itemExtra1 = item;
        return itemService.save(item);
    }

    private String getResourceAsString(String resourceName) {
        try (InputStream inputStream = this.getClass().getResourceAsStream(resourceName)) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private InputStream getResourceAsStream(String resourceName) {
        try (InputStream inputStream = this.getClass().getResourceAsStream(resourceName)) {
            return inputStream;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void persistCategoriesAndSubCategories() {
        Category category = persistCategory();
        persistSubCategory("SUBCAT1-NAME", "SUBCAT1-CODE", category);
        beverageSubCat = persistSubCategory("BEVERAGE", "BEVERAGE-CODE", category);
    }

    private Category persistCategory() {
        Category category = new Category();
        category.setName("CAT1-NAME");
        category.setCode("CAT1-CODE");
        return categoryService.save(category);
    }

    private SubCategory persistSubCategory(String name, String code, Category category) {
        SubCategory subCategory = new SubCategory();
        subCategory.setName(name);
        subCategory.setCode(code);
        subCategory.setCategory(category);
        return subCategoryService.save(subCategory);
    }

    private void persistBuy(BigDecimal value) {
        Buy buy = new Buy();
        buy.setValue(value);
        buyRepository.save(buy);
    }

    private void persistItems2(SubCategory subCategory){
        Item item = new Item();
        item.setCode("A-POWERADE");
        item.setName("POWERADE");
        item.setSubCategory(subCategory);
        this.itemExtra2 = item;
        itemService.save(item);

        Item item2 = new Item();
        item2.setCode("A-MONSTER");
        item2.setName("MONSTER");
        item2.setSubCategory(subCategory);
        this.itemExtra3 = item2;
        itemService.save(item2);
    }

    private void persistItemInventories(){
        ItemInventory itemInventory = new ItemInventory();
        itemInventory.setItem(itemExtra1);
        itemInventory.setStockQuantity(new BigDecimal(100));
        itemInventory.setUpperBoundThreshold(new BigDecimal(105));
        itemInventory.setLowerBoundThreshold(new BigDecimal(95));
        itemInventory.setTotalPrice(new BigDecimal(1000));
        this.itemInventory1 = itemInventory;
        itemInventoryService.save(itemInventory);

        ItemInventory itemInventory2 = new ItemInventory();
        itemInventory2.setItem(itemExtra2);
        itemInventory2.setStockQuantity(new BigDecimal(100));
        itemInventory2.setUpperBoundThreshold(new BigDecimal(110));
        itemInventory2.setLowerBoundThreshold(new BigDecimal(90));
        itemInventory2.setTotalPrice(new BigDecimal(2000));
        this.itemInventory2 = itemInventory2;
        itemInventoryService.save(itemInventory2);

        ItemInventory itemInventory3 = new ItemInventory();
        itemInventory3.setItem(itemExtra3);
        itemInventory3.setStockQuantity(new BigDecimal(100));
        itemInventory3.setUpperBoundThreshold(new BigDecimal(150));
        itemInventory3.setLowerBoundThreshold(new BigDecimal(50));
        itemInventory3.setTotalPrice(new BigDecimal(3000));
        this.itemInventory3 = itemInventory3;
        itemInventoryService.save(itemInventory3);
    }

    private void persistItemInventoryEntries(){
        ItemInventoryEntry itemInventoryEntry = new ItemInventoryEntry();
        itemInventoryEntry.setItemInventory(itemInventory1);
        itemInventoryEntry.setMovementType(MovementType.SALE);
        itemInventoryEntry.setQuantity(new BigDecimal(6));
        itemInventoryEntry.setItemInstanceSkus("1-6");
        itemInventoryEntryService.save(itemInventoryEntry);
    }
}
