package devjluvisi.mlb.cmds.admin;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.blocks.LuckyBlock;
import devjluvisi.mlb.cmds.SubCommand;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.util.Range;
import devjluvisi.mlb.util.luckyblocks.MapLocation3D;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Transform all blocks of a certain type into a luckyblock within a radius
 */
public class TransformCommand implements SubCommand {

    private MoreLuckyBlocks plugin;
    public TransformCommand(MoreLuckyBlocks plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "transform";
    }

    @Override
    public String getDescription() {
        return "Transforms all blocks of a lucky block material into a genuine lucky block which has that material.";
    }

    @Override
    public String getSyntax() {
        return "/mlb transform <lb-name> <radius>\n/mlb transform <lb-name> <world> <x> <y> <z> <radius>";
    }

    @Override
    public String getPermission() {
        return "mlb.admin.transform";
    }

    @Override
    public boolean isAllowConsole() {
        return true;
    }

    @Override
    public Range getArgumentRange() {
        return new Range(2, 7);
    }

    @Override
    public ExecutionResult perform(CommandSender sender, String[] args) {
        LuckyBlock lb = null;
        Location orgin = new Location(null, 0, 0, 0);
        int radius = 0;

        if(args.length == 7) {
            try {
                lb = plugin.getLuckyBlocks().get(args[1]);
                orgin.setWorld(plugin.getServer().getWorld(args[2]));
                orgin.setX(Util.toNumber(args[3]));
                orgin.setY(Util.toNumber(args[4]));
                orgin.setZ(Util.toNumber(args[5]));
                radius = Integer.parseInt(args[6]);
                Validate.notNull(lb);
                Validate.notNull(orgin.getWorld());
                Validate.notNull(orgin);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "Failed." + ChatColor.RED + " There are or more errors in your command. Make sure all numbers are valid and the specified world exists.");
                return ExecutionResult.PASSED;
            }
        }else if(args.length == 3) {
            if(!(sender instanceof Player)) {
                sender.sendMessage("You cannot execute this form of the command as a non-player. You must use the /mlb transform <world> <x> <y> <z> <radius> version.");
                return ExecutionResult.PASSED;
            }

            if (!Util.isNumber(args[2])) {
                return ExecutionResult.BAD_ARGUMENT_TYPE;
            }
            if(!plugin.getLuckyBlocks().contains(args[1])) {
                sender.sendMessage(ChatColor.RED + "Could not find lucky block " + args[1]);
                return ExecutionResult.PASSED;
            }
            radius = Integer.parseInt(args[2]);
            lb = plugin.getLuckyBlocks().get(args[1]);
            orgin = ((Player)sender).getLocation();
        }else{
            return ExecutionResult.BAD_USAGE;
        }
        final BukkitScheduler scheduler = plugin.getServer().getScheduler();
        sender.sendMessage(ChatColor.BLUE + "Replacing regular blocks with lucky block: " + ChatColor.GOLD + lb.getInternalName());

        int fromX = orgin.getBlockX() - radius;
        int fromY = orgin.getBlockY() - radius;
        int fromZ = orgin.getBlockZ() - radius;
        int toX = orgin.getBlockX() + radius;
        int toY = orgin.getBlockY() + radius;
        int toZ = orgin.getBlockZ() + radius;
        fromY = fromY < 0 ? 0 : Math.min(fromY, 255);
        toY = toY < 0 ? 0 : Math.min(toY, 255);
        sender.sendMessage(ChatColor.GRAY + "X -> " + fromX + " to " + toX);
        sender.sendMessage(ChatColor.GRAY + "Y -> " + fromY + " to " + toY);
        sender.sendMessage(ChatColor.GRAY + "Z -> " + fromZ + " to " + toZ);

        final long searchBlocks = (long) Math.pow((radius*2), 3);
        sender.sendMessage(ChatColor.DARK_GRAY + "Request to search " + searchBlocks + " blocks.");
        if(searchBlocks > 50000) {
            sender.sendMessage(ChatColor.RED + "[WARNING] Blocks requested to search exceeds 50,000. It may take time to replace the blocks requested.");
        }

        final long time = System.currentTimeMillis();

        int finalFromY = fromY;
        int finalToY = toY;
        Location finalOrgin = orgin;
        LuckyBlock finalLb = lb;
        final int[] blocksReplaced = {0};

        new BukkitRunnable() {
            long bCounter =0;
            boolean hasShown = false;
            final long thresholdMessage = searchBlocks/16;
            final MapLocation3D l = new MapLocation3D();

            @Override
            public void run() {
                for(int x = fromX; x < toX; x++) {
                    for(int y = finalFromY; y < finalToY; y++) {
                        for(int z = fromZ; z < toZ; z++) {
                            bCounter++;
                            if(bCounter - 5000D > 0 && !hasShown) {
                                    // Calculate estimated time based on the time it took to place "X" blocks.
                                    int seconds = (int)(((double)(System.currentTimeMillis()-time)/1000D)*((double)searchBlocks/5000D));
                                    sender.sendMessage(ChatColor.BLUE + "Transform: " + ChatColor.AQUA + "Estimated to take " + seconds + " seconds.");
                                    hasShown = true;
                            }

                            l.setWorld(Objects.requireNonNull(finalOrgin.getWorld()).getUID());
                            l.setX(x);
                            l.setY(y);
                            l.setZ(z);
                            if(l.getBlock().getType()== finalLb.getBlockMaterial()) {
                                plugin.getAudit().put(l, finalLb);
                                blocksReplaced[0]++;
                            }
                            if(hasShown && bCounter % thresholdMessage == 0 && bCounter != 0) {
                                sender.sendMessage(ChatColor.GREEN + "Searched over " + ChatColor.GOLD + bCounter + ChatColor.GREEN + " blocks. (" + ChatColor.GOLD + blocksReplaced[0] + ChatColor.GREEN + " replaced)");
                            }
                        }
                    }
                }
                sender.sendMessage("");
                sender.sendMessage(ChatColor.DARK_GREEN.toString() + ChatColor.UNDERLINE + ChatColor.BOLD + "Completed.");
                sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "Task completed in: " + (System.currentTimeMillis()-time)/1000 + " seconds.");
                sender.sendMessage("");
                sender.sendMessage(ChatColor.GREEN + "Edited " + ChatColor.YELLOW + blocksReplaced[0] + ChatColor.GREEN + " blocks and turned them into lucky blocks!");
                this.cancel();
            }
        }.runTaskAsynchronously(plugin);



        /*

        Async Block Searcher similar to the above method
        but utilizes large "areas".
        WARNING: Takes A LOT of ram for large radiuses.

        final int maxAreaSize = 15000; // Blocks per replace

        ArrayList<ArrayList<MapLocation3D>> replaceBlockArea = new ArrayList<>();
        int counter = 0;
        int chunkReplacements = 0;

        ArrayList<MapLocation3D> mapLocation3DArrayList = new ArrayList<>();
        for(int x = fromX; x < toX; x++) {
            for(int y = fromY; y < toY; y++) {
                for(int z = fromZ; z < toZ; z++) {
                    mapLocation3DArrayList.add(new MapLocation3D(new Location(orgin.getWorld(), x, y, z)));
                    if(counter != 0 && counter % maxAreaSize == 0) {
                        replaceBlockArea.add(new ArrayList<>(mapLocation3DArrayList));
                        mapLocation3DArrayList.clear();
                        chunkReplacements++;
                    }
                    counter++;
                }
            }
        }

        // Add remaning
        if(mapLocation3DArrayList.size() != 0) {
            replaceBlockArea.add(new ArrayList<>(mapLocation3DArrayList));
            chunkReplacements++;
        }
        sender.sendMessage(ChatColor.GREEN + "Searching " + ChatColor.GRAY.toString()+ChatColor.BOLD.toString() + counter + ChatColor.GREEN + " blocks in " + ChatColor.GRAY.toString()+ChatColor.BOLD.toString() +  "x" + chunkReplacements + ChatColor.GREEN + " areas.");
        sender.sendMessage(ChatColor.LIGHT_PURPLE  + "Estimated time: " + ChatColor.DARK_PURPLE + (chunkReplacements) +  ChatColor.LIGHT_PURPLE + " seconds.");
        sender.sendMessage(ChatColor.ITALIC.toString() + "Starting in 5 seconds.");

        AtomicInteger replaceAreaCount = new AtomicInteger();
        int finalChunkReplacements = chunkReplacements;
        final int[] actualBlocksReplaced = {0};
        LuckyBlock finalLb = lb;
        new BukkitRunnable() {
            @Override
            public void run() {

                replaceAreaCount.getAndIncrement();
                sender.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC+  "Replacing area: " + replaceAreaCount.get());
                ArrayList<MapLocation3D> list = replaceBlockArea.get(replaceAreaCount.get()-1);
                list.stream().parallel().forEach(e -> putIf(e, finalLb, actualBlocksReplaced));



                if(replaceAreaCount.get() == finalChunkReplacements) {
                    sender.sendMessage("");
                    sender.sendMessage(ChatColor.DARK_GREEN.toString() + ChatColor.UNDERLINE + ChatColor.BOLD + "Completed.");
                    sender.sendMessage("");
                    sender.sendMessage(ChatColor.GREEN + "Edited " + ChatColor.YELLOW + actualBlocksReplaced[0] + ChatColor.GREEN + " blocks and turned them into lucky blocks!");
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin, 20L * 5L, 10L);
*/


        return ExecutionResult.PASSED;
    }

    public void putIf(MapLocation3D l, LuckyBlock lb, int[] amt) {
        if(l.getBlock().getType()== lb.getBlockMaterial()) {
            plugin.getAudit().put(l, lb);
            amt[0]++;
        }
    }
}