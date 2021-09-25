package devjluvisi.mlb.queries;

import java.util.UUID;

import org.bukkit.entity.Player;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.menus.ViewDropsMenu;
import net.md_5.bungee.api.ChatColor;

/**
 * A request to remove a lucky block from the server.
 * @author jacob
 *
 */
public class RemoveDropRequest extends Query {
	
	private int blockIndex;
	private LuckyBlockDrop blockDrop;
	
	public RemoveDropRequest(MoreLuckyBlocks plugin, UUID u, LuckyBlockDrop blockDrop, int blockIndex) {
		super(u, plugin);
		this.blockIndex = blockIndex;
		this.blockDrop = blockDrop;
	}
//TODO: Fix
	@Override
	protected void runProcess() {
		LuckyBlock block = super.getPlugin().getLuckyBlocks().get(blockIndex);
		Player p = super.getPlugin().getServer().getPlayer(super.getPlayerUUID());
		block.removeDrop(blockDrop);
		block.saveConfig(super.getPlugin().getBlocksYaml());
		new ViewDropsMenu(super.getPlugin(), super.getPlugin().getLuckyBlocks().get(blockIndex)).open(p);
	}
	
	

}
