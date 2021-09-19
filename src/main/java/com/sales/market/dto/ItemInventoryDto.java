package com.sales.market.dto;

import com.sales.market.model.Item;
import com.sales.market.model.ItemInventory;

import java.math.BigDecimal;

public class ItemInventoryDto extends DtoBase<ItemInventory> {
    private Item item;
    private BigDecimal stockQuantity;
    private BigDecimal totalPrice;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public BigDecimal getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(BigDecimal stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
