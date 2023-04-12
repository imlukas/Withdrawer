package me.imlukas.withdrawer.constant;

public enum ItemType {

    BANKNOTE("banknote"),
    EXPBOTTLE("expbottle"),
    HEALTH("health");

    private final String lowercase;

    ItemType(String lowercase) {
        this.lowercase = lowercase;
    }

    public String getLowercase() {
        return lowercase;
    }
}
