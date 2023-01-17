package me.imlukas.withdrawer.constant;

public enum ItemType {
    BANKNOTE("banknote"),
    EXPBOTTLE("expbottle"),
    HEALTH("health");


    public final String lowercase;

    ItemType(String lowercase) {
        this.lowercase = lowercase;
    }
}
