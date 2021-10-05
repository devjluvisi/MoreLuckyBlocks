package devjluvisi.mlb.util.structs;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;

public class DropStructure implements Listener {
	
	
	private static int BUILD_LIMIT = 128;
	private static byte WORLD_BORDER_SIZE = 50;
	
	private static String defaultName = "mlb-world";
	private MoreLuckyBlocks plugin;
	
	private UUID editingPlayerUUID;
	private World structWorld;
	
	private LuckyBlock lb;
	private LuckyBlockDrop drop;
	
	public DropStructure(MoreLuckyBlocks plugin) {
		this.plugin = plugin;
		if(plugin.getServer().getWorld(defaultName) != null) {
			structWorld = plugin.getServer().getWorld(defaultName);
			
			for(Player p : structWorld.getPlayers()) {
				
				p.teleport(plugin.getServer().getWorlds().get(0).getSpawnLocation());
				p.kickPlayer(ChatColor.RED + "You were in a structure world on a server reload/restart.");
				
			}
			return;
		}
		
		structWorld = plugin.getServer().createWorld(new WorldCreator(defaultName).type(WorldType.FLAT).generateStructures(false));
		structWorld.setSpawnLocation(new Location(structWorld, 0, structWorld.getHighestBlockYAt(0, 0)+1, 0));
	
		structWorld.setDifficulty(Difficulty.PEACEFUL);
		structWorld.setPVP(false);
		structWorld.setAutoSave(false);
		structWorld.setSpawnFlags(false, false);
		structWorld.setKeepSpawnInMemory(false);
		structWorld.setTime(10000);
		structWorld.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
		structWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		structWorld.setGameRule(GameRule.DO_FIRE_TICK, false);
		structWorld.setGameRule(GameRule.DO_ENTITY_DROPS, false);
		structWorld.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
		structWorld.setGameRule(GameRule.DO_MOB_LOOT, false);
		structWorld.setGameRule(GameRule.SPAWN_RADIUS, 0);
		structWorld.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
		structWorld.setGameRule(GameRule.MOB_GRIEFING, false);
		structWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
		structWorld.setGameRule(GameRule.KEEP_INVENTORY, true);
		structWorld.setGameRule(GameRule.FALL_DAMAGE, false);
		structWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
		structWorld.setGameRule(GameRule.FIRE_DAMAGE, false);
		structWorld.setGameRule(GameRule.DROWNING_DAMAGE, false);
		
		WorldBorder border = structWorld.getWorldBorder();
		border.setSize(WORLD_BORDER_SIZE);
		border.setCenter(0.0, 0.0);
	}
	
	public void update(LuckyBlock lb, LuckyBlockDrop editingDrop, Player p) {
		this.lb = lb;
		this.drop = editingDrop;
		this.editingPlayerUUID = p.getUniqueId();
		for(int x = -(WORLD_BORDER_SIZE/2); x < (WORLD_BORDER_SIZE/2); x++) {
			for(int z = -(WORLD_BORDER_SIZE/2); z < (WORLD_BORDER_SIZE/2); z++) {
				for(int y = 0; y < BUILD_LIMIT; y++) {
					if(y < BUILD_LIMIT/2) {
						structWorld.getBlockAt(x,y,z).setType(Material.STONE);
						continue;
					}
					structWorld.getBlockAt(x,y,z).setType(Material.AIR);
				}
			}
		}
		
		structWorld.getEntities().clear();
		structWorld.getBlockAt(0, structWorld.getHighestBlockYAt(0, 0)+1, 0).setType(lb.getBlockMaterial());
		if(editingDrop.hasStructure()) {
			LinkedList<String> blockList = new LinkedList<String>(plugin.getStructuresYaml().getConfig().getStringList("structures." + editingDrop.getStructure().toString()));
			for(String s : blockList) {
				RelativeBlock r = new RelativeBlock().deserialize(s);
				r.place(structWorld);
				Bukkit.getLogger().info(r.toString());
			}
		}
	}
	
	private void reset() {
		this.lb = null;
		this.drop = null;
		this.editingPlayerUUID = null;
	}

	public final UUID getEditingPlayerUUID() {
		return editingPlayerUUID;
	}
	
	public void teleport(Player p) {
		
		if(!(structWorld.getPlayers().size() > 0)) {
			p.teleport(new Location(structWorld, 0.0D, structWorld.getHighestBlockYAt(0, 0)+1, 0.0D));
			p.sendMessage("");
			p.sendMessage(ChatColor.DARK_GRAY + "[!] " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "You have entered structure editing mode.");
			p.sendMessage("");
			p.sendMessage(ChatColor.GRAY + "Place blocks and spawn mobs to edit this structure.\nWhen finished, type /mlb save to save the structure to the block.\nType /mlb exit to exit this creator world.");
			p.sendMessage("");
			if(drop.hasStructure()) {
				p.sendMessage(ChatColor.DARK_GREEN.toString() + ChatColor.ITALIC.toString() + "Loaded your previously saved structure.");
			}
		}
	}
	
	public World getWorld() {
		return structWorld;
	}
	
	public boolean hasEditingPlayer() {
		return structWorld.getPlayers().size() > 0;
	}
	
	
	@EventHandler
	public void preventPlace(BlockPlaceEvent e) {
		if(!(e.getBlock().getLocation().getWorld().getUID().equals(structWorld.getUID()))) return; 
		if(e.getBlock().getLocation().getY() > BUILD_LIMIT) {
			e.getPlayer().sendMessage(ChatColor.RED + "Build limit reached >> " + BUILD_LIMIT);
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void blockFlow(BlockSpreadEvent e) {
		if(!(e.getBlock().getLocation().getWorld().getUID().equals(structWorld.getUID()))) return; 
		e.setCancelled(true);
	}
	
	@EventHandler
	public void blockChange(EntityChangeBlockEvent e) {
		if(!(e.getBlock().getLocation().getWorld().getUID().equals(structWorld.getUID()))) return; 
		e.setCancelled(true);
	}
	
	@EventHandler
	public void blockPhysics(BlockPhysicsEvent e) {
		if(!(e.getBlock().getLocation().getWorld().getUID().equals(structWorld.getUID()))) return; 
		e.setCancelled(true);
	}
	
	@EventHandler
	public void blockToBlock(BlockFromToEvent e) {
		if(!(e.getBlock().getLocation().getWorld().getUID().equals(structWorld.getUID()))) return; 
		e.setCancelled(true);
	}
	
	@EventHandler
	public void blockPiston(BlockPistonExtendEvent e) {
		if(!(e.getBlock().getLocation().getWorld().getUID().equals(structWorld.getUID()))) return; 
		e.setCancelled(true);
	}
	
	@EventHandler
	public void blockRedstone(BlockRedstoneEvent e) {
		if(!(e.getBlock().getLocation().getWorld().getUID().equals(structWorld.getUID()))) return; 
		e.setNewCurrent(0);
	}
	
	@EventHandler
	public void preventExplode(EntityExplodeEvent e) {
		if(!(e.getLocation().getWorld().getUID().equals(structWorld.getUID()))) return; 
		e.setCancelled(true);
	}
	
	@EventHandler
	public void preventBreak(BlockBreakEvent e) {
		if(!(e.getBlock().getLocation().getWorld().getUID().equals(structWorld.getUID()))) return; 
		if(e.getBlock().getLocation().equals(new Location(structWorld, 0, BUILD_LIMIT/2, 0))) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "You cannot break this block.");
			e.getPlayer().sendMessage(ChatColor.GRAY + "This block is used to serve as a reference for what structure to create when a player gets this drop.");
			e.getPlayer().sendMessage(ChatColor.GRAY + "You should build your structure around this block.");
		}
	}
	
	@EventHandler
	public void playerLeave(PlayerQuitEvent e) {
		if(!(e.getPlayer().getLocation().getWorld().getUID().equals(structWorld.getUID()))) return; 
		reset();
	}
	
	public void save() {
		if(drop.getStructure() == null) {
			drop.setStructure(UUID.randomUUID());
		}
		
		LinkedList<String> itemList = new LinkedList<String>();
		
		for(int x = -(WORLD_BORDER_SIZE/2); x < (WORLD_BORDER_SIZE/2); x++) {
			for(int z = -(WORLD_BORDER_SIZE/2); z < (WORLD_BORDER_SIZE/2); z++) {
				for(int y = 0; y < BUILD_LIMIT; y++) {
					if(x==0&&z==0&&y==BUILD_LIMIT/2) continue;
					Material m = structWorld.getBlockAt(x, y, z).getType();
					if(!(m.isAir() && y < BUILD_LIMIT/2) && (structWorld.getBlockAt(x, y, z).isEmpty() || m==Material.STONE)) continue;
					RelativeBlock block = new RelativeBlock(m, x, y, z);
					itemList.add(block.serialize());
				}
			}
		}
		
		plugin.getStructuresYaml().getConfig().set("structures." + drop.getStructure().toString(), itemList);
		plugin.getStructuresYaml().save();
		plugin.getStructuresYaml().reload();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DropStructure [structUUID=");
		builder.append(defaultName);
		builder.append(", editingPlayerUUID=");
		builder.append(editingPlayerUUID);
		builder.append("]");
		return builder.toString();
	}
	
	

}
