package me.imlukas.withdrawer.utils.interactions;

import me.imlukas.withdrawer.utils.storage.YMLBase;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SoundManager extends YMLBase {

    private final FileConfiguration configuration;

    public SoundManager(JavaPlugin plugin) {
        super(plugin, "sounds.yml");
        this.configuration = getConfiguration();
    }

    public void playSound(Player player, String key) {
        int volume = configuration.getInt(key + ".volume") == 0 ? 1 : configuration.getInt(key + ".volume");
        int pitch = configuration.getInt(key + ".pitch") == 0 ? 1 : configuration.getInt(key + ".pitch");

        playSound(player, key, volume, pitch);
    }

    public void playSound(Player player, String key, int volume, int pitch) {
        playSound(player, player.getLocation(), key, volume, pitch);
    }

    public void playSound(Player player, Location location, String key, int volume, int pitch) {
        Sound sound = Sound.valueOf(configuration.getString(key + ".sound"));
        player.playSound(location, sound, volume, pitch);
    }

    public void playSoundWorld(Player player, Location location, String key, int volume, int pitch) {
        World world = player.getWorld();
        Sound sound = Sound.valueOf(configuration.getString(key + ".sound"));
        world.playSound(location, sound, volume, pitch);
    }
}
