/*
 * Silk spawner plugin (at the moment - was RTP plugin with cooldown in the works).
 */

package me.goonhaven.SimpleSilk;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.goonhaven.SimpleSilk.Commands.CommandSimpleSilk;
import me.goonhaven.SimpleSilk.Events.EventsClass;
import net.md_5.bungee.api.ChatColor;

public class SimpleSilk extends JavaPlugin {
	public void onEnable() {
		getCommand("simplesilk").setExecutor(new CommandSimpleSilk());
		getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Loaded SimpleSilk");
		getServer().getPluginManager().registerEvents(new EventsClass(), this);
		loadConfig();
	}
	public void onDisable()	{
		getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "SimpleSilk shut down");
	}
	public void loadConfig() {
		FileConfiguration config = getConfig();
		config.options().copyDefaults(true);
		String path = "TNT-Drop-Chance";
		if (!config.contains(path))
			config.set(path, 25.0);
		else {
			try {
				Double.parseDouble(config.getString(path));
			} catch (NumberFormatException nfe) {
				config.set(path, 25.0);
			}
		}
		saveConfig();
	}
}