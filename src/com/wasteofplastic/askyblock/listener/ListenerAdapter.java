package com.wasteofplastic.askyblock.listener;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
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
import org.bukkit.inventory.Inventory;

import com.wasteofplastic.askyblock.zcore.ZUtils;

@SuppressWarnings("deprecation")
public abstract class ListenerAdapter extends ZUtils {

	protected void onConnect(PlayerJoinEvent event, Player player) {
	}

	protected void onQuit(PlayerQuitEvent event, Player player) {
	}

	protected void onMove(PlayerMoveEvent event, Player player) {
	}

	protected void onInventoryClick(InventoryClickEvent event, Player player) {
	}

	protected void onInventoryClose(InventoryCloseEvent event, Player player) {
	}

	protected void onInventoryDrag(InventoryDragEvent event, Player player) {
	}

	protected void onBlockBreak(BlockBreakEvent event, Player player) {
	}

	protected void onBlockPlace(BlockPlaceEvent event, Player player) {
	}

	protected void onEntityDeath(EntityDeathEvent event, LivingEntity entity) {
	}

	protected void onInteract(PlayerInteractEvent event, Player player) {
	}

	protected void onPlayerTalk(AsyncPlayerChatEvent event, String message) {
	}

	protected void onCraftItem(CraftItemEvent event) {
	}

	protected void onCommand(PlayerCommandPreprocessEvent event, Player player, String message) {
	}

	protected void onGamemodeChange(PlayerGameModeChangeEvent event, Player player) {
	}

	public void onDrop(PlayerDropItemEvent event, Player player) {
	}

	public void onPickUp(PlayerPickupItemEvent event, Player player) {
	}

	public void onMobSpawn(CreatureSpawnEvent event) {
	}

	public void onForm(BlockFormEvent event, Block block, BlockState newState) {
	}

	public void onFormTo(BlockFromToEvent event, Block block) {
	}

	public void onPostForm(BlockFormEvent event, Block block) {
	}

	public void onItemMove(InventoryMoveItemEvent event, Inventory destination, Inventory source) {
	}

	public void onPlayerDeath(PlayerDeathEvent event, Player entity) {
	}

	public void onInteractEntity(PlayerInteractEntityEvent event, Player player, Entity rightClicked) {
	}

	public void onPlayerDamageByEntity(EntityDamageByEntityEvent event, Player entity, Entity damager) {
	}

	public void onSign(SignChangeEvent event, Player player) {
	}

	public void onBlockForm(BlockFormEvent event, BlockState newState) {
	}
}
