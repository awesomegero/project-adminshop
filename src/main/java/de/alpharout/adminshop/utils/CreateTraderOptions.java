package de.alpharout.adminshop.utils;

public class CreateTraderOptions {
    private String internalName;
    private String displayName;
    private String skinUrl;

    public CreateTraderOptions() {

    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setSkinUrl(String skinUrl) {
        this.skinUrl = skinUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSkinUrl() {
        return skinUrl;
    }

    public String getInternalName() {
        return internalName;
    }
}
