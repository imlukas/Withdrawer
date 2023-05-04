package me.imlukas.withdrawer.utils.interactions.messages;

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

        if (messages.isLessIntrusive) {
            messages.sendMessage(player, itemType + ".redeem-less-intrusive", placeholders);
            return;
        }

        messages.sendMessage(player, itemType + ".redeem", placeholders);
    }

    public void sendWithdrawMessage(Player player, String itemType, int totalAmount) {
        sendRedeemMessage(player, itemType, totalAmount, "");
    }

    public void sendWithdrawMessage(Player player, String itemType, int totalAmount, String currencySign) {
        List<Placeholder<Player>> placeholders = List.of(
                new Placeholder<>("amount", String.valueOf(totalAmount)),
                new Placeholder<>("currency", currencySign));

        if (messages.isLessIntrusive) {
            messages.sendMessage(player, itemType + ".withdraw-less-intrusive", placeholders);
            return;
        }

        messages.sendMessage(player, itemType + ".withdraw", placeholders);
    }

    public void sendGiftedMessage(Player target, Player gifter, String itemType, int totalAmount) {
        sendGiftedMessage(target, gifter, itemType, totalAmount, "");
    }

    public void sendGiftedMessage(Player target, Player gifter, String itemType, int totalAmount, String currencySign) {
        List<Placeholder<Player>> placeholders = List.of(
                new Placeholder<>("amount", String.valueOf(totalAmount)),
                new Placeholder<>("currency", currencySign),
                new Placeholder<>("gifter", gifter.getDisplayName()));

        messages.sendMessage(target, itemType + ".gifted", placeholders);
    }
}
