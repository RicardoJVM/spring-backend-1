package com.sales.market.controller;

import com.sales.market.dto.ItemInventoryDto;
import com.sales.market.dto.ItemInventoryEntryDto;
import com.sales.market.model.ItemInventory;
import com.sales.market.model.ItemInventoryEntry;
import com.sales.market.service.ItemInventoryEntryService;
import com.sales.market.service.ItemInventoryService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/iteminventoryentry")
public class ItemInventoryEntryController extends GenericController<ItemInventoryEntry, ItemInventoryEntryDto>{
    private ItemInventoryEntryService service;

    public ItemInventoryEntryController(ItemInventoryEntryService service) {
        this.service = service;
    }

    @Override
    protected ItemInventoryEntryService getService() {
        return service;
    }

    @Override
    protected ItemInventoryEntryDto save(@RequestBody ItemInventoryEntryDto element, Boolean bunchSave) {
        // Para probar comprar 10 unidades del item 1
        ItemInventoryEntry itemInventoryEntry = service.bunchSave(toModel(element));
        return toDto(itemInventoryEntry);
    }
/*
    {
        "itemInventory": {
        "item":{
            "name":"B-MALTIN",
                    "code":"MALTIN",
                    "unitPrice":10
        },
        "stockQuantity":100,
                "lowerBoundThreshold":95,
                "upperBoundThreshold":105,
                "totalPrice":1000
    },
        "movementType": "SALE",
            "quantity": 10,
            "itemInstanceSkus":"1-10"
    }
 */
}
