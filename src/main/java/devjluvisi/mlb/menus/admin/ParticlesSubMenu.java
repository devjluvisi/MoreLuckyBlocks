package devjluvisi.mlb.menus.admin;

import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.util.MenuItem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ParticlesSubMenu extends MenuBuilder {

    /**
     * Current particle selection.
     * 0 = LOW
     * 1 = MEDIUM
     * 2 = HIGH
     */
    private final Set<Map.Entry<Particle, Integer>> selectedParticles;
    private int particleSelect;
    private boolean init;

    private static final List<Map.Entry<Particle, Material>> particles = List.of(
            Map.entry(Particle.FLAME, Material.BLAZE_POWDER),
            Map.entry(Particle.EXPLOSION_NORMAL, Material.TNT),
            Map.entry(Particle.GLOW, Material.GLOWSTONE_DUST),
            Map.entry(Particle.HEART, Material.APPLE),
            Map.entry(Particle.NOTE, Material.NOTE_BLOCK),
            Map.entry(Particle.CRIT_MAGIC, Material.END_CRYSTAL),
            Map.entry(Particle.SNOWBALL, Material.SNOWBALL),
            Map.entry(Particle.WATER_BUBBLE, Material.WATER_BUCKET),
            Map.entry(Particle.DRAGON_BREATH, Material.DRAGON_HEAD),
            Map.entry(Particle.VILLAGER_HAPPY, Material.EMERALD),
            Map.entry(Particle.FIREWORKS_SPARK, Material.FIREWORK_ROCKET),
            Map.entry(Particle.SMOKE_NORMAL, Material.FIRE_CHARGE),
            Map.entry(Particle.SMOKE_LARGE, Material.CAMPFIRE),
            Map.entry(Particle.PORTAL, Material.MAGENTA_DYE),
            Map.entry(Particle.VILLAGER_ANGRY, Material.FERMENTED_SPIDER_EYE)
    );

    public ParticlesSubMenu(MenuManager manager) {
        super(manager, "Edit Particles", PageType.DOUBLE_CHEST);
        this.particleSelect = 1;
        this.selectedParticles = new HashSet<>();
        this.init = false;
    }

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        for(int i = 0; i < getPageType().getRow(); i++) {
            Arrays.fill(content[i], new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE));
        }

        content[4][1]=content[4][2] = content[4][3] = content[4][4] = content[4][5] = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        content[1][6] = content[2][6] = content[3][6] = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        content[4][6] = new MenuItem(MenuItem.SpecialItem.EXIT_BUTTON).asItem();

        // If there are no particles selected.
        if(!init) {
            // Add particles to the selected particles from the lucky block.
            manager.getMenuData().getLuckyBlock().getParticleMap().forEach((key, value) -> selectedParticles.add(Map.entry(key, value)));
            init = true;
        }


            MenuItem item = new MenuItem().with(Material.EMERALD).with("&aSave").addLine("&7Click to Save.")
                    .addLine("\n");
        if(selectedParticles.isEmpty()) {
            item.addLine("&fNone");
            content[4][7] = item.asItem();
        }else{
            selectedParticles.forEach(k -> item.addLine(ChatColor.YELLOW + transform(k.getKey().name()) + ChatColor.GRAY + " " + (stars(k.getValue()))));
            content[4][7] = item.asItem();
        }



        content[1][7] = new MenuItem().with(Material.GOLD_INGOT).with("&6Max Particles").addLine("&7Click to select a high amount").addLine("&7of particles to spawn.").asItem();
        content[2][7] = new MenuItem().with(Material.IRON_INGOT).with("&6Some Particles").addLine("&7Click to select a regular amount").addLine("&7of particles to spawn.").asItem();
        content[3][7] = new MenuItem().with(Material.BRICK).with("&6Little Particles").addLine("&7Click to select a low amount").addLine("&7of particles to spawn.").asItem();
        ListIterator<Map.Entry<Particle, Material>> iterator = particles.listIterator();
        for(int i = 1; i < 4; i++) {
            for(int j = 1; j < 6; j++) {
                Map.Entry<Particle, Material> entry = iterator.next();
                if(selectedParticles.stream().anyMatch(e -> e.getKey().equals(entry.getKey()))) {
                    content[i][j] = new MenuItem().with(entry.getValue()).with("&3"+ transform(entry.getKey().name()))
                            .addLine("&7Click to unselect.")
                            .addLine("\n")
                            .addLine("&a&lSELECTED ✔")
                            .asItem();
                    continue;
                }
                content[i][j] = new MenuItem().with(entry.getValue()).with("&3"+ transform(entry.getKey().name()))
                        .addLine("&7Click to select this particle.")
                        .asItem();
            }
        }

        if(particleSelect == 0) {
            content[3][7].addUnsafeEnchantment(Enchantment.THORNS, 1);
            ItemMeta meta = content[3][7].getItemMeta();
            assert meta != null;
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            content[3][7].setItemMeta(meta);
        }else if(particleSelect == 1) {
            content[2][7].addUnsafeEnchantment(Enchantment.THORNS, 1);
            ItemMeta meta = content[2][7].getItemMeta();
            assert meta != null;
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            content[2][7].setItemMeta(meta);
        }else if(particleSelect == 2) {
            content[1][7].addUnsafeEnchantment(Enchantment.THORNS, 1);
            ItemMeta meta = content[1][7].getItemMeta();
            assert meta != null;
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            content[1][7].setItemMeta(meta);
        }
        return content;
    }

    private static final byte MAX_PARTICLES = 16;
    private static final byte MED_PARTICLES = 25;
    private static final byte LOW_PARTICLES = 34;

    private static final byte EXIT_MENU = 42;
    private static final byte SAVE_BUTTON = 43;

    public static String transform(String s) {
        return WordUtils.capitalize(StringUtils.lowerCase(StringUtils.replace(s.toUpperCase(), "_", " ")));
    }

    public static String stars(int level) {
        return level == 0 ? "★☆☆" : level == 1 ? "★★☆" : "★★★";
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        switch(slot) {
            case MAX_PARTICLES -> {
                this.particleSelect = 2;
                view.reopen();
            }
            case MED_PARTICLES -> {
                this.particleSelect = 1;
                view.reopen();
            }
            case LOW_PARTICLES -> {
                this.particleSelect = 0;
                view.reopen();
            }
            case SAVE_BUTTON -> {
                manager.getPlayer().sendMessage(ChatColor.DARK_GREEN + "Updated the particles of " + ChatColor.GOLD + manager.getMenuData().getLuckyBlock().getInternalName());
                if(!selectedParticles.isEmpty()) {
                    manager.getPlayer().sendMessage(ChatColor.GRAY + "New Particles " + ChatColor.WHITE + "→ ");
                }else{
                    manager.getPlayer().sendMessage(ChatColor.GRAY + "New Particles " + ChatColor.WHITE + "→ " + ChatColor.LIGHT_PURPLE + "None");
                }

                StringBuilder builder = new StringBuilder();
                EnumMap<Particle, Integer> particleIntegerEnumMap = manager.getMenuData().getLuckyBlock().getParticleMap();
                particleIntegerEnumMap.clear();
                selectedParticles.forEach(e -> {
                    particleIntegerEnumMap.put(e.getKey(), e.getValue());
                    builder.append("  ").append(ChatColor.LIGHT_PURPLE).append(transform(e.getKey().name())).append(ChatColor.DARK_GRAY).append(" (").append(ChatColor.YELLOW).append(stars(e.getValue())).append(ChatColor.DARK_GRAY).append(")").append("\n");
                });
                manager.getPlayer().sendMessage(builder.toString());
                view.close();
                return;
            }
            case EXIT_MENU -> {
                manager.regress(view);
            }
            default -> {

            }
        }
        if(itemStack.hasItemMeta() && !Objects.requireNonNull(itemStack.getItemMeta()).getLore().isEmpty()) {
            for(Map.Entry<Particle, Material> entry : particles) {
                if(entry.getValue() == itemStack.getType()) {
                    // Check if the particle clicked on is currently selected.
                    if(!selectedParticles.removeIf(e -> e.getKey().equals(entry.getKey()))) {
                        selectedParticles.add(Map.entry(entry.getKey(), particleSelect));
                    }
                    view.reopen();
                    return;
                }
            }
        }

    }

    @Override
    public MenuType type() {
        return MenuType.EDIT_PARTICLES_SUB;
    }
}
