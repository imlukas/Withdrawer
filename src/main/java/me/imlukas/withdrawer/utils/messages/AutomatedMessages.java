package me.imlukas.withdrawer.utils.messages;

import me.imlukas.withdrawer.utils.text.Placeholder;
import org.bukkit.entity.Player;

import java.util.List;

public class AutomatedMessages {
    private final MessagesFile messages;

    public AutomatedMessages(MessagesFile messages) {
        this.messages = messages;
    }

    public void sendRedeemMessage(Player player, String itemType, int totalAmount) {
        sendRedeemMessage(player, itemType, totalAmount, "");
    }


    public void sendRedeemMessage(Player player, String itemType, int totalAmount, String currencySign) {
        List<Placeholder<Player>> placeholders = List.of(
                new Placeholder<>("amount", String.valueOf(totalAmount)),
                new Placeholder<>("currency", currencySign));

        messages.sendMessage(player, itemType + ".redeem", placeholders);
    }

    public void sendItemWithdrawMessage(Player player, String itemType, int totalAmount) {
        sendRedeemMessage(player, itemType, totalAmount, "");
    }

    public void sendItemWithdrawMessage(Player player, String itemType, int totalAmount, String currencySign) {
        List<Placeholder<Player>> placeholders = List.of(
                new Placeholder<>("amount", String.valueOf(totalAmount)),
                new Placeholder<>("currency", currencySign));

        messages.sendMessage(player, itemType + ".withdraw", placeholders);
    }
}
