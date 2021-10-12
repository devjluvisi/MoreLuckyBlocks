package devjluvisi.mlb.events.luckyblocks;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.util.structs.RelativeObject;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Objects;

public record BreakEvent(MoreLuckyBlocks plugin) implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void breakLuckyBlockEvent(BlockBreakEvent e) {
        if (!this.plugin.getAudit().isLuckyBlock(e.getBlock().getLocation())) {
            return;
        }
        final LuckyBlock lb = this.plugin.getAudit().find(e.getBlock().getLocation());

        if (Objects.isNull(lb)) {
            return;
        }
        if (lb.getDroppableItems().isEmpty()) {
            e.getPlayer().sendMessage(ChatColor.RED
                    + "This was a lucky block but it did not drop anything because no drops were available.");
            e.setCancelled(true);
            return;
        }
        e.getPlayer().sendMessage(ChatColor.YELLOW + "You broke a lucky block!");
        final LuckyBlockDrop drop = lb
                .generateDrop(this.plugin.getPlayerManager().getPlayer(e.getPlayer().getName()).getLuck());
        drop.applyTo(e.getBlock().getLocation(), e.getPlayer());

        if (drop.hasStructure()) {
            for (final String s : this.plugin.getStructuresYaml().getConfig()
                    .getStringList("structures." + drop.getStructure().toString())) {
                final RelativeObject r = new RelativeObject().deserialize(s);
                final Location loc = e.getBlock().getLocation();
                // todo; get direction of player and adjust.
                r.setOffset(loc);
                r.place(loc.getWorld());
            }
        }
        this.plugin.getAudit().remove(e.getBlock().getLocation());
    }

}
