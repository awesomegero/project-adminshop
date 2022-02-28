package de.alpharout.adminshop.utils;

public class EditProductOptions {
    private int buyLimit;
    private double price;
    private int amount;
    private String productName;

    public int getBuyLimit() {
        return buyLimit;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setBuyLimit(int buyLimit) {
        this.buyLimit = buyLimit;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
