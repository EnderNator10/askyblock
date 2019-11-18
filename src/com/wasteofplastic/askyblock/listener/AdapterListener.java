package com.wasteofplastic.askyblock.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.wasteofplastic.askyblock.ASkyBlock;

@SuppressWarnings("deprecation")
public class AdapterListener implements Listener {

	private final ASkyBlock template;

	public AdapterListener(ASkyBlock template) {
		this.template = template;
	}

	@EventHandler
	public void onConnect(PlayerJoinEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onConnect(event, event.getPlayer()));
	}

	@EventHandler
	public void onQuite(PlayerQuitEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onQuit(event, event.getPlayer()));
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onMove(event, event.getPlayer()));
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		template.getListenerAdapters()
				.forEach(adapter -> adapter.onInventoryClick(event, (Player) event.getWhoClicked()));
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onBlockBreak(event, event.getPlayer()));
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onBlockPlace(event, event.getPlayer()));
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onEntityDeath(event, event.getEntity()));
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onInteract(event, event.getPlayer()));
	}

	@EventHandler
	public void onPlayerTalk(AsyncPlayerChatEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onPlayerTalk(event, event.getMessage()));
	}

	@EventHandler
	public void onCraftItem(CraftItemEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onCraftItem(event));
	}

	@EventHandler
	public void onDrag(InventoryDragEvent event) {
		template.getListenerAdapters()
				.forEach(adapter -> adapter.onInventoryDrag(event, (Player) event.getWhoClicked()));
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onInventoryClose(event, (Player) event.getPlayer()));
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		template.getListenerAdapters()
				.forEach(adapter -> adapter.onCommand(event, event.getPlayer(), event.getMessage()));
	}

	@EventHandler
	public void onGamemodeChange(PlayerGameModeChangeEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onGamemodeChange(event, event.getPlayer()));
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onDrop(event, event.getPlayer()));
	}

	@EventHandler
	public void onPick(PlayerPickupItemEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onPickUp(event, event.getPlayer()));
	}

	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onMobSpawn(event));
	}

	@EventHandler
	public void onForm(BlockFormEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onForm(event, event.getBlock(), event.getNewState()));
		template.getListenerAdapters().forEach(adapter -> adapter.onPostForm(event, event.getNewState().getBlock()));
	}

	@EventHandler
	public void onMoveItem(InventoryMoveItemEvent event) {
		template.getListenerAdapters()
				.forEach(adapter -> adapter.onItemMove(event, event.getDestination(), event.getSource()));
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onPlayerDeath(event, event.getEntity()));
	}

	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent event) {
		template.getListenerAdapters()
				.forEach(adapter -> adapter.onInteractEntity(event, event.getPlayer(), event.getRightClicked()));
	}

	@EventHandler
	public void onSign(SignChangeEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onSign(event, event.getPlayer()));
	}

	@EventHandler
	public void weather(WeatherChangeEvent e) {
		e.setCancelled(e.toWeatherState());
	}

	@EventHandler
	public void onPlayerDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player)
			template.getListenerAdapters().forEach(
					adapter -> adapter.onPlayerDamageByEntity(event, (Player) event.getEntity(), event.getDamager()));
	}
	
	@EventHandler
	public void onBlockForm(BlockFormEvent event) {
		template.getListenerAdapters().forEach(
				adapter -> adapter.onBlockForm(event, event.getNewState()));
	}
	
	@EventHandler
	public void onChunk(ChunkLoadEvent event) {
		template.getListenerAdapters().forEach(
				adapter -> adapter.onChunkLoad(event, event.getChunk(), event.getWorld()));
	}
	
	@EventHandler
	public void onChunk(ChunkUnloadEvent event) {
		template.getListenerAdapters().forEach(
				adapter -> adapter.onChunkUnLoad(event, event.getChunk(), event.getWorld()));
	}
}
