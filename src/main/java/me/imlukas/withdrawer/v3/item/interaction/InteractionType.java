package me.imlukas.withdrawer.v3.item.interaction;

public enum InteractionType {

    REDEEM("redeem"),
    WITHDRAW("withdraw"),
    GIVE("give"),
    GIFT("gift");

    private final String configName;

    InteractionType(String configName) {
        this.configName = configName;
    }

    public String getConfigName() {
        return configName;
    }
}
