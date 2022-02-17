package de.alpharout.adminshop.api;

public class SkinInformation {
    private final String skinName;
    private final String textureSignature;
    private final String textureValue;

    /*
    These three values are necesary to load a skin from mineskin.org.
    Citizens supports them natively.
     */
    public SkinInformation(String skinName, String textureSignature, String textureValue) {
        this.skinName = skinName;
        this.textureSignature = textureSignature;
        this.textureValue = textureValue;
    }

    public String getSkinName() {
        return skinName;
    }

    public String getTextureSignature() {
        return textureSignature;
    }

    public String getTextureValue() {
        return textureValue;
    }
}
