package de.alpharout.adminshop.utils;

import de.alpharout.adminshop.api.ProductType;

public class AddProductOptions {
    private String internalName;
    private String displayName;
    private int productAmount;
    private double productPrice;
    private int productLimit;
    private ProductType productType;

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductLimit(int productLimit) {
        this.productLimit = productLimit;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getInternalName() {
        return internalName;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public int getProductLimit() {
        return productLimit;
    }

    public ProductType getProductType() {
        return productType;
    }
}
