package me.goonhaven.SimpleSilk.Commands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class CommandSimpleSilk implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player;
		String cmd = command.getName();
		if (cmd.equalsIgnoreCase("simplesilk")) {
			if (sender instanceof Player) {
				player = (Player) sender;
				if (args.length >= 1) {
					if (player.hasPermission("ss.cmd")) {
						int argOne = 1;
						if (args.length == 2) {
							try {
								argOne = Integer.parseInt(args[1]);
								if (argOne < 0) {
									player.sendMessage(ChatColor.GREEN + "Positive numbers only!");
									return false;
								}
							} catch (NumberFormatException nfe) {
								player.sendMessage(ChatColor.GREEN + "Use only numbers for the quantity!");
								argOne = 1;
							}
						}
						ItemStack spawner = new ItemStack(Material.SPAWNER, argOne);
						ItemMeta meta = spawner.getItemMeta();
						ArrayList<String> lore = new ArrayList<String>();
						if (isValidMob(args[0])) {
							lore.add(args[0].toUpperCase());
							meta.setLore(lore);
							meta.setDisplayName(ChatColor.AQUA + formaliseText(args[0]) + " Spawner");
							spawner.setItemMeta(meta);
							if (player.getInventory().firstEmpty() > -1) {
								player.getInventory().addItem(spawner);
							}
							else
								player.sendMessage(ChatColor.GREEN + "Inventory full!");
						} else
							player.sendMessage(
									ChatColor.DARK_GREEN + args[0] + ChatColor.GREEN + " is an invalid mob type!");
					}
					else
						player.sendMessage(ChatColor.DARK_PURPLE + "You do not have permission to use the " + ChatColor.LIGHT_PURPLE + "/simplesilk" + ChatColor.DARK_PURPLE + " command!");
				} else
					player.sendMessage(ChatColor.GREEN + "Missing mob type!\n" + ChatColor.DARK_PURPLE
							+ "Usage: /simplesilk <mob name> [quantity]");
			} else
				sender.sendMessage(ChatColor.DARK_RED + "Command " + ChatColor.GREEN + ((label.isEmpty()) ? cmd : label)
						+ ChatColor.DARK_RED + " cannot execute from the console!");
		}
		return true;

	}

	public static boolean isValidMob(String mob) {
		String uCMob = mob.toUpperCase();
		if (uCMob.equals("BAT")) 
			return true;
		if (uCMob.equals("BLAZE"))
			return true;
		if (uCMob.equals("CAVE_SPIDER"))
			return true;
		if (uCMob.equals("CHICKEN"))
			return true;
		if (uCMob.equals("COD"))
			return true;
		if (uCMob.equals("COW"))
			return true;
		if (uCMob.equals("CREEPER"))
			return true;
		if (uCMob.equals("DOLPHIN"))
			return true;
		if (uCMob.equals("DONKEY"))
			return true;
		if (uCMob.equals("DROWNED"))
			return true;
		if (uCMob.equals("ELDER_GUARDIAN"))
			return true;
		if (uCMob.equals("ENDER_DRAGON"))
			return true;
		if (uCMob.equals("ENDERMAN"))
			return true;
		if (uCMob.equals("ENDERMITE"))
			return true;
		if (uCMob.equals("EVOKER"))
			return true;
		if (uCMob.equals("GHAST"))
			return true;
		if (uCMob.equals("GIANT"))
			return true;
		if (uCMob.equals("GUARDIAN"))
			return true;
		if (uCMob.equals("HORSE"))
			return true;
		if (uCMob.equals("HUSK"))
			return true;
		if (uCMob.equals("ILLUSIONER"))
			return true;
		if (uCMob.equals("IRON_GOLEM"))
			return true;
		if (uCMob.equals("LLAMA"))
			return true;
		if (uCMob.equals("MAGMA_CUBE"))
			return true;
		if (uCMob.equals("MULE"))
			return true;
		if (uCMob.equals("OCELOT"))
			return true;
		if (uCMob.equals("PHANTOM"))
			return true;
		if (uCMob.equals("PIG"))
			return true;
		if (uCMob.equals("PIG_ZOMBIE"))
			return true;
		if (uCMob.equals("POLAR_BEAR"))
			return true;
		if (uCMob.equals("PUFFER_FISH"))
			return true;
		if (uCMob.equals("RABBIT"))
			return true;
		if (uCMob.equals("SALMON"))
			return true;
		if (uCMob.equals("SHEEP"))
			return true;
		if (uCMob.equals("SHULKER"))
			return true;
		if (uCMob.equals("SILVERFISH"))
			return true;
		if (uCMob.equals("SKELETON"))
			return true;
		if (uCMob.equals("SKELETON_HORSE"))
			return true;
		if (uCMob.equals("SLIME"))
			return true;
		if (uCMob.equals("SNOWMAN"))
			return true;
		if (uCMob.equals("SPIDER"))
			return true;
		if (uCMob.equals("SQUID"))
			return true;
		if (uCMob.equals("STRAY"))
			return true;
		if (uCMob.equals("TROPICAL_FISH"))
			return true;
		if (uCMob.equals("TURTLE"))
			return true;
		if (uCMob.equals("VEX"))
			return true;
		if (uCMob.equals("VILLAGER"))
			return true;
		if (uCMob.equals("VINDICATOR"))
			return true;
		if (uCMob.equals("WITCH"))
			return true;
		if (uCMob.equals("WITHER"))
			return true;
		if (uCMob.equals("WITHER_SKELETON"))
			return true;
		if (uCMob.equals("WOLF"))
			return true;
		if (uCMob.equals("ZOMBIE"))
			return true;
		if (uCMob.equals("ZOMBIE_HORSE"))
			return true;
		if (uCMob.equals("ZOMBIE_VILLAGER"))
			return true;
		return false;
	}

	public static String formaliseText(String text) {
		return text.substring(0, 1).toUpperCase() + text.substring(1, text.length()).toLowerCase();
	}
}
