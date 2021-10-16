package devjluvisi.mlb.menus.admin;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.api.gui.MenuView;
import devjluvisi.mlb.api.gui.pages.PageType;
import devjluvisi.mlb.api.gui.utils.Coords2D;
import devjluvisi.mlb.helper.Util;
import devjluvisi.mlb.menus.MenuBuilder;
import devjluvisi.mlb.menus.MenuManager;
import devjluvisi.mlb.menus.MenuType;
import devjluvisi.mlb.menus.util.MenuItem;
import devjluvisi.mlb.util.config.files.SettingType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ConfigSettingsMenu extends MenuBuilder {

    private static final byte MAX_SETTINGS_PER_PAGE = 6;

    private final ArrayList<ArrayList<Map.Entry<Slot, SettingType>>> settings;

    private final int maxPages;

    private MoreLuckyBlocks plugin;
    private int currentPage;

    public ConfigSettingsMenu(MenuManager manager) {
        super(manager, "Settings Menu", PageType.DOUBLE_CHEST);
        this.settings = new ArrayList<>();

        this.currentPage = 1;
        this.plugin = manager.getPlugin();
        addSetting(Slot.SETTINGS_1, SettingType.AUTO_SAVE_ENABLED);
        addSetting(Slot.SETTINGS_2, SettingType.GET_BLOCK_DATA_INTERVAL);
        addSetting(Slot.SETTINGS_3, SettingType.AUTO_SAVE_ENABLED);
        addSetting(Slot.SETTINGS_4, SettingType.AUTO_SAVE_ENABLED);
        addSetting(Slot.SETTINGS_5, SettingType.AUTO_SAVE_ENABLED);
        addSetting(Slot.SETTINGS_6, SettingType.AUTO_SAVE_ENABLED);


        addSetting(Slot.SETTINGS_1, SettingType.AUTO_SAVE_ENABLED);
        addSetting(Slot.SETTINGS_2, SettingType.AUTO_SAVE_ENABLED);
        addSetting(Slot.SETTINGS_3, SettingType.AUTO_SAVE_ENABLED);
        addSetting(Slot.SETTINGS_4, SettingType.AUTO_SAVE_ENABLED);
        this.maxPages = settings.size();
    }

    private void addSetting(Slot s, SettingType type) {
        Map.Entry<Slot, SettingType> entry = Map.entry(s, type);
        if(!settings.isEmpty() && settings.get(settings.size()-1).size() != MAX_SETTINGS_PER_PAGE) {
            settings.get(settings.size()-1).add(entry);
            return;
        }
        settings.add(new ArrayList<>(List.of(entry)));
    }

    private ItemStack getItemForValue(SettingType type) {
        ItemStack item = new ItemStack(type.getMaterial(), 1);
        ItemMeta meta = item.getItemMeta();
        Object settingValue = plugin.getSettingsManager().getGenericSetting(type);

        String description = ChatColor.BLUE + type.getDescription();
        String name = ChatColor.YELLOW + type.getName();

        if(type.getReturnType() == SettingType.ReturnType.BOOLEAN) {
            boolean b = (boolean)settingValue;
            if(b) {
                item.setType(Material.GREEN_DYE);
            }else{
                item.setType(Material.RED_DYE);
            }
            name = StringUtils.replace(name, SettingType.CURRENT_VALUE_PLACEHOLDER, b ? "ON" : "OFF");
            description = StringUtils.replace(description, SettingType.CURRENT_VALUE_PLACEHOLDER, b ? ChatColor.GREEN + "✔ Enabled" : ChatColor.RED + "✖ Disabled");
        } else if(type.getReturnType() == SettingType.ReturnType.INT || type.getReturnType() == SettingType.ReturnType.DECIMAL) {
            double val = Double.parseDouble(String.valueOf(settingValue));
            if(type.getReturnType() == SettingType.ReturnType.INT) {
                val = (int)val;
            }
            name = StringUtils.replace(name, SettingType.CURRENT_VALUE_PLACEHOLDER, String.valueOf(val));
            description = StringUtils.replace(description, SettingType.CURRENT_VALUE_PLACEHOLDER, String.valueOf(val));
        }
        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(Util.descriptionToLore(description));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public ItemStack[][] getContent(ItemStack[][] content) {
        setMenuName("Settings Menu (" + currentPage + "/" + maxPages + ")");
        Arrays.stream(content).forEach(e -> Arrays.fill(e, MenuItem.blackPlaceholder().asItem()));
        for(int i = 1; i < 4; i++) {
            for(int j = 1; j < 8; j++) {
                content[i][j] = MenuItem.whitePlaceholder().asItem();
            }
        }

        // Display the settings.
        for(int i = 0; i < MAX_SETTINGS_PER_PAGE; i++) {
            if(i >= settings.get(currentPage-1).size()) {
                break;
            }
            content
                    [rawSlotToCoords(settings.get(currentPage-1).get(i).getKey().get())[0]]
                    [rawSlotToCoords(settings.get(currentPage-1).get(i).getKey().get())[1]]
                    = getItemForValue(settings.get(currentPage-1).get(i).getValue());
        }


        if(currentPage == 1) {
            content[4][6] = new MenuItem().with(Material.FEATHER).with("&ePrevious Page").addLine("&7You are on the first page.").asItem();
        }else{
            content[4][6] = new MenuItem().with(Material.FEATHER).with("&ePrevious Page").addLine("&7Click to go to page " + (currentPage-1) + ".").asItem();
        }
        if(currentPage == maxPages) {
            content[4][7] = new MenuItem().with(Material.ARROW).with("&eNext Page").addLine("&7You are on the last page.").asItem();
        }else{
            content[4][7] = new MenuItem().with(Material.ARROW).with("&eNext Page").addLine("&7Click to go to page " + (currentPage+1) + ".").asItem();
        }
         content[5][4] = new MenuItem(MenuItem.SpecialItem.EXIT_BUTTON).asItem();
        return content;
    }

    enum Slot {
        PREVIOUS_PAGE((byte)42), NEXT_PAGE((byte)43), EXIT_MENU((byte)49),
        SETTINGS_1((byte)10),
        SETTINGS_2((byte)13),
        SETTINGS_3((byte)16),
        SETTINGS_4((byte)28),
        SETTINGS_5((byte)31),
        SETTINGS_6((byte)34),
        EMPTY((byte)0);

        final byte val;
        Slot(byte val) {
            this.val = val;
        }
        public byte get() {
            return this.val;
        }
        public static Slot ofValue(byte val) {
            return EnumSet.copyOf(Arrays.stream(Slot.values()).toList()).stream().filter(e -> e.get() == val).findFirst().orElse(EMPTY);
        }
    }

    private static int[] rawSlotToCoords(int slot) {
        return new int[] {slot/9, slot%9};
    }

    @Override
    public void onClick(MenuView view, ClickType clickType, int slot, ItemStack itemStack) {
        if(itemStack == null || isPlayerSlot(slot)) {
            return;
        }
        // Slot 49 = Exit Button
        // Slot 43 = Next Page
        // Slot 42 = Previous Page
        // Slot 10 = Settings Option #1
        // Slot 28 = Settings Option #2
        // Slot 13 = Settings Option #3
        // Slot 31 = Settings Option #4
        // Slot 16 = Settings Option #5
        // Slot 34 = Settings Option #6
        switch (Slot.ofValue((byte) slot)) {
            case PREVIOUS_PAGE -> {
                if (currentPage == 1) {
                    return;
                }
                this.currentPage--;
                view.reopen();
            }
            case NEXT_PAGE -> {
                if(currentPage == maxPages) {
                    return;
                }
                this.currentPage++;
                view.reopen();
            }
            case EXIT_MENU -> {
                view.close();
            }
            case SETTINGS_1, SETTINGS_2, SETTINGS_3, SETTINGS_4, SETTINGS_5, SETTINGS_6 -> {
                Map.Entry<Slot, SettingType> entry = settings.get(currentPage-1).stream().filter(e -> e.getKey().get() == slot).findFirst().orElse(null);
                if(entry==null) {
                    return;
                }
                if(entry.getValue().getReturnType() == SettingType.ReturnType.BOOLEAN) {
                    plugin.getSettingsManager().setValue(entry.getValue().getNode(), !(boolean)plugin.getSettingsManager().getGenericSetting(entry.getValue()));
                    manager.getPlayer().sendMessage("Updated the value of " + entry.getValue().name() + " to " + (boolean)plugin.getSettingsManager().getGenericSetting(entry.getValue()));
                    view.reopen();
                }else{

                }
            }
            default -> {
                return;
            }
        }
    }

    @Override
    public MenuType type() {
        return MenuType.ADJUST_SETTINGS;
    }
}