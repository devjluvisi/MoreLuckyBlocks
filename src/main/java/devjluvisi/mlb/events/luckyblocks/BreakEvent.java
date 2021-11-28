package devjluvisi.mlb.events.luckyblocks;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.blocks.LuckyBlockDrop;
import devjluvisi.mlb.events.custom.LogDataEvent;
import devjluvisi.mlb.util.config.files.messages.Message;
import devjluvisi.mlb.util.structs.RelativeObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Objects;

public record BreakEvent(MoreLuckyBlocks plugin) implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void breakLuckyBlockEvent(BlockBreakEvent e) {
        if (!this.plugin.getAudit().isLuckyBlock(e.getBlock().getLocation())) {
            return;
        }
        final LuckyBlock lb = this.plugin.getAudit().find(e.getBlock().getLocation());
        final ItemStack item = e.getPlayer().getItemInUse();
        if (Objects.isNull(lb)) {
            return;
        }
        if (lb.getDroppableItems().isEmpty()) {
            e.getPlayer().sendMessage(Message.NO_DROPS.get());
            e.setCancelled(true);
            return;
        }

        // Cancel event if silk touch pickaxe.
        if (item != null && item.hasItemMeta()) {
            if (Objects.requireNonNull(item.getItemMeta()).hasEnchants()) {
                if (item.getItemMeta().getEnchants().containsKey(Enchantment.SILK_TOUCH)) {
                    e.getPlayer().sendMessage(Message.M46.get());
                    e.setCancelled(true);
                    return;
                }
            }
        }

        Location l = e.getBlock().getLocation();
        e.getPlayer().sendMessage(Message.BREAK_LUCKY.format(lb.getInternalName(), Objects.requireNonNull(l.getWorld()).getName(), l.getBlock(), l.getBlockY(), l.getBlockZ(), lb.getBlockLuck()));
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
        Bukkit.broadcastMessage("Broken");
        this.plugin.getAudit().remove(e.getBlock().getLocation());
        plugin.getPlayerManager().incrementBlockBreak(e.getPlayer().getUniqueId());
        plugin.getPlayerManager().getPlayerLuckMap().putIfAbsent(e.getPlayer().getUniqueId(), new ArrayList<>());
        plugin.getPlayerManager().getPlayerLuckMap().get(e.getPlayer().getUniqueId()).add(drop.getRarity());
        plugin.getServer().getPluginManager().callEvent(new LogDataEvent(e.getPlayer().getName() + " broke a lucky block [" + lb.getInternalName() + "," + lb.getBlockLuck() + "]"));
    }

}
