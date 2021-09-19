package com.sales.market.controller;

import com.sales.market.dto.ItemDto;
import com.sales.market.dto.ItemInventoryDto;
import com.sales.market.model.Item;
import com.sales.market.model.ItemInventory;
import com.sales.market.service.GenericService;
import com.sales.market.service.ItemInstanceService;
import com.sales.market.service.ItemInventoryService;
import com.sales.market.service.ItemService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/iteminventory")
public class ItemInventoryController extends GenericController<ItemInventory, ItemInventoryDto>{
    private ItemInventoryService service;

    public ItemInventoryController(ItemInventoryService service) {
        this.service = service;
    }

    @Override
    protected ItemInventoryService getService() {
        return service;
    }
}
