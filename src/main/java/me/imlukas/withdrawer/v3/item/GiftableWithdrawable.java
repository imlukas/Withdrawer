package me.imlukas.withdrawer.v3.item;

public interface GiftableWithdrawable extends Withdrawable {

    void setGifted(boolean isGifted);

    boolean isGifted();
}
