package me.imlukas.withdrawer.item.interactions;

import me.imlukas.withdrawer.Withdrawer;
import me.imlukas.withdrawer.utils.interactions.SoundManager;
import me.imlukas.withdrawer.utils.interactions.messages.MessagesFile;
import org.bukkit.entity.Player;

public class Interactions {

    private final MessagesFile messages;
    private final SoundManager sounds;
    private final String configName;

    public Interactions(Withdrawer withdrawer, String configName) {
        this.messages = withdrawer.getMessages();
        this.sounds = withdrawer.getSounds();
        this.configName = configName;
    }

    // Interactions (Messages and Sounds)
    public void sendRedeemInteractions(Player player, int totalAmount) {
        sendRedeemInteractions(player, totalAmount, "");
    }

    public void sendRedeemInteractions(Player player, int totalAmount, String currencySign) {
        messages.getAutomatedMessages().sendRedeemMessage(player, configName, totalAmount, currencySign);
        sounds.playSound(player, configName + ".redeem");
    }

    public void sendWithdrawInteractions(Player player, int totalAmount) {
        sendWithdrawInteractions(player, totalAmount, "");
    }

    public void sendWithdrawInteractions(Player player, int totalAmount, String currencySign) {
        messages.getAutomatedMessages().sendWithdrawMessage(player, configName, totalAmount, currencySign);
        sounds.playSound(player, configName + ".withdraw");
    }

    public void sendGiftedInteractions(Player player, Player gifter, int totalAmount) {
        sendGiftedInteractions(player, gifter, totalAmount, "");
    }

    public void sendGiftedInteractions(Player player, Player gifter, int totalAmount, String currencySign) {
        messages.getAutomatedMessages().sendGiftedMessage(player, gifter, configName, totalAmount, currencySign);
        sounds.playSound(player, configName + ".gifted");
    }

    public void sendGiveInteractions(Player target, int totalAmount) {
        sendGiveInteractions(target, totalAmount, "");
    }

    public void sendGiveInteractions(Player target, int totalAmount, String currencySign) {
        messages.getAutomatedMessages().sendGiveMessage(target, configName, totalAmount, currencySign);
        sounds.playSound(target, configName + ".given");
    }
}
