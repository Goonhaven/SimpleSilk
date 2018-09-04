package me.goonhaven.SimpleSilk.Events;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import me.goonhaven.SimpleSilk.SimpleSilk;
import me.goonhaven.SimpleSilk.Commands.CommandSimpleSilk;
import net.md_5.bungee.api.ChatColor;

public class EventsClass implements Listener {
	/*
	 * SimpleSilk plugin
	 */
	private SimpleSilk plugin = SimpleSilk.getPlugin(SimpleSilk.class);

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Location bLoc = event.getBlock().getLocation();
		FileConfiguration config = plugin.getConfig();
		String world = player.getWorld().getName();
		String location = String.valueOf(bLoc.getBlockX()) + String.valueOf(bLoc.getBlockY())
				+ String.valueOf(bLoc.getBlockZ());
		String path = "Spawners_Placed.World." + world + ".Location";
		ArrayList<String> list = new ArrayList<String>();
		Material blockType = event.getBlock().getType();
		if (blockType.equals(Material.SPAWNER)) {
			// Has to be assigned in first condition block so that spawner can be removed
			// from config even if the conditions below are false.
			list = (ArrayList<String>) config.getStringList(path);
			if (player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)
					&& player.hasPermission("ss.spawner.silkbreak")
					&& !player.getGameMode().equals(GameMode.CREATIVE)) {
				// If spawner is unnatural or player has natural silk-break permission give it
				// to player, otherwise break naturally and drop nothing.
				if (list.contains(location) || player.hasPermission("ss.spawner.silkbreak.natural")) {
					unnaturalDrop(event);
				} else
					player.sendMessage(ChatColor.DARK_PURPLE
							+ "No spawner dropped as you do not have permission to silk-mine natural spawners!");
			}
		}
		if (!list.isEmpty()) {
			// Returns true if location was found and deleted.
			list.remove(location);
		}
		config.set(path, list);
		plugin.saveConfig();
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Location bLoc = event.getBlockPlaced().getLocation();
		if (event.getBlockPlaced().getType().equals(Material.SPAWNER)) {
			if (player.hasPermission("ss.spawner.place")) {
				CreatureSpawner creatureSpawner = (CreatureSpawner) event.getBlockPlaced().getState();
				String lore = "";
				if (!event.getItemInHand().getItemMeta().hasLore()) {
					event.getPlayer().sendMessage(
							ChatColor.DARK_GREEN + "Detected natural spawner. Setting to Snowman Spawner!");
					creatureSpawner.setSpawnedType(EntityType.SNOWMAN);
				} else {
					lore = event.getItemInHand().getItemMeta().getLore().get(0);
					if (CommandSimpleSilk.isValidMob(lore))
						creatureSpawner.setSpawnedType(EntityType.valueOf(lore));
					else {
						player.sendMessage(ChatColor.DARK_GREEN
								+ "Someone has tampered with this spawner's lore! Setting to Snowman Spawner.");
						creatureSpawner.setSpawnedType(EntityType.SNOWMAN);
					}
				}
				creatureSpawner.update();
				String world = player.getWorld().getName();
				String location = String.valueOf(bLoc.getBlockX()) + String.valueOf(bLoc.getBlockY())
						+ String.valueOf(bLoc.getBlockZ());
				String path = "Spawners_Placed.World." + world + ".Location";
				ArrayList<String> list = (ArrayList<String>) plugin.getConfig().getStringList(path);
				if (!list.contains(location))
					list.add(location);
				plugin.getConfig().set(path, list);
				plugin.saveConfig();
			} else {
				player.sendMessage(ChatColor.DARK_PURPLE + "You do not have permission to place spawners!");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onExplosion(EntityExplodeEvent eee) {
		// System.out.println("An explosion occured");
		FileConfiguration config = plugin.getConfig();
		String world = eee.getLocation().getWorld().getName();
		Location bLoc;
		String location;
		String path = "Spawners_Placed.World." + world + ".Location";
		ArrayList<String> list = (ArrayList<String>) config.getStringList(path);
		double dropChance;
		double rand;
		for (Block block : eee.blockList()) {
			if (block.getType().equals(Material.SPAWNER)) {
				// Fail safe is in the main method. Will not return anything but a double.
				dropChance = plugin.getConfig().getDouble("TNT-Drop-Chance") / 100.0;
				rand = Math.random();
				bLoc = block.getLocation();
				location = String.valueOf(bLoc.getBlockX()) + String.valueOf(bLoc.getBlockY())
						+ String.valueOf(bLoc.getBlockZ());
				if (list.contains(location)) {
					if (dropChance >= rand) {
						explosionDrop(block);
						// System.out.println("A spawner should've broken");
					}
					list.remove(location);
					config.set(path, list);
					plugin.saveConfig();
				}
			}
		}
	}

	public static void unnaturalDrop(BlockBreakEvent bbe) {
		Location bLoc = bbe.getBlock().getLocation();
		CreatureSpawner creatureSpawner = (CreatureSpawner) bbe.getBlock().getState();
		bbe.setExpToDrop(0);

		ItemStack spawnerItem = new ItemStack(bbe.getBlock().getType(), 1);
		ItemMeta spawnerMeta = spawnerItem.getItemMeta();
		spawnerMeta.setDisplayName(ChatColor.AQUA
				+ CommandSimpleSilk.formaliseText(creatureSpawner.getSpawnedType().toString()) + " Spawner");

		ArrayList<String> lore = new ArrayList<String>();
		lore.add(creatureSpawner.getSpawnedType().toString());
		spawnerMeta.setLore(lore);
		spawnerItem.setItemMeta(spawnerMeta);
		bLoc.getWorld().dropItemNaturally(bLoc, spawnerItem);
	}

	public static void explosionDrop(Block blockBroken) {
		Location bLoc = blockBroken.getLocation();
		CreatureSpawner creatureSpawner = (CreatureSpawner) blockBroken.getState();

		ItemStack spawnerItem = new ItemStack(blockBroken.getType(), 1);
		ItemMeta spawnerMeta = spawnerItem.getItemMeta();
		spawnerMeta.setDisplayName(ChatColor.AQUA
				+ CommandSimpleSilk.formaliseText(creatureSpawner.getSpawnedType().toString()) + " Spawner");

		ArrayList<String> lore = new ArrayList<String>();
		lore.add(creatureSpawner.getSpawnedType().toString());
		spawnerMeta.setLore(lore);
		spawnerItem.setItemMeta(spawnerMeta);
		bLoc.getWorld().dropItemNaturally(bLoc, spawnerItem);
	}
}