/**
 * @author: Edson A. Terceros T.
 */

package com.sales.market.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Entity
public class Item extends ModelBase {
    private String name;
    private String code;
    private Byte[] image;
    private BigDecimal unitPrice;
    @OneToOne(targetEntity = SubCategory.class)
    private SubCategory subCategory;

    @OneToOne(targetEntity = SubCategory.class)
    private SubCategory subCategorySt2;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Byte[] getImage() {
        return image;
    }

    public void setImage(Byte[] image) {
        this.image = image;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
