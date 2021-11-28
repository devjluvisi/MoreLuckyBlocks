package devjluvisi.mlb.util.config.files.messages;

import devjluvisi.mlb.MoreLuckyBlocks;
import devjluvisi.mlb.helper.Util;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.EnumSet;
import java.util.Objects;

public enum Message {

    NO_PERMISSION("&cYou do not have permission to perform this command."),
    MUST_BE_PLAYER("&cYou must be a player to execute this command."),
    BAD_COMMAND_USAGE("&cIncorrect Usage.\n{0}"),
    GENERAL_COMMAND_ERROR("&cThere was a problem executing your command."),
    UNKNOWN_PLAYER("&cCould not find specified player \"{0}\"."),
    BAD_ARGUMENT_1("&cInvalid argument specified."),
    BAD_ARGUMENT_2("&cInvalid Argument Type {0}."),
    BAD_REQUEST("&cYou cannot execute this command right now."),
    UNKNOWN_LUCKY_BLOCK("&cCould not find lucky block \"{0}\"."),
    BAD_MATERIAL("&cInvalid material specified: \"{0}\"."),
    UNKNOWN_COMMAND("&cUnknown Command. Type /mlb help for a list of commands."),
    BREAK_LUCKY("&eYou broke a lucky block!\nType: {0}\nLocation: {1}, {2}, {3}, {4}\nLuck: {5}"),
    NO_DROPS("&eYou broke a lucky block but there were no drops available."),
    CANT_PLACE("&cThis lucky block cannot be placed because there are no drops set."),
    PLACED_BLOCK("&eYou placed a lucky block.\nType:{0}\nLocation: {1},{2},{3},{4}\nLuck:{5}"),
    CANCELLED("&eYou cancelled this action."),
    COMPLETE("&aFinished."),
    TYPE_EXIT("&7Type \"exit\" to exit."),
    VIEWING_GUI_TITLE("Viewing: {0}"),
    VIEWING_LOOT_GUI_TITLE("Viewing Loot for Drop #{0}"),
    EMPTY("&7Empty"),
    PREVIOUS_PAGE_TITLE("&ePrevious Page &8(&6{0}&e/&6{1}&8)"),
    PREVIOUS_PAGE_LORE("&7Go back to the previous page."),
    NEXT_PAGE_TITLE("&eNext Page &8(&6{0}&e/&6{1}&8)"),
    NEXT_PAGE_LORE("&7Go to the next page."),
    DROP_TITLE("&aDrop: "),
    STRUCTURES_TITLE("&e&lStructures"),
    LB_TOTAL_LOOT("&7&lTotal Drops: &d{0}"),
    LB_DROP_RARITY("&7&lRarity: &d{0}"),
    LB_VIEW_CONTENTS("&7&oClick to view drop contents."),
    LABEL_RARITY("&8- &7Rarity: &d{0}"),
    LABEL_TOTAL_LOOT("&8- &7Total Loot: &d{0}"),
    LABEL_ITEMS("&8- &7Items: &d{0}"),
    LABEL_POTIONS("&8- &7Potions: &d{0}"),
    LABEL_COMMANDS("&8- &7Commands: &d{0}"),
    LABEL_STRUCTURE("&8- &7Has Structure: &d{0}"),
    DISABLED_COMMAND("&cThis command has been disabled by server administrators."),

    /*
     MISC_MESSAGES
     The following messages are two obscure to give proper enum names so they are just generalized
     using letters and numbers.
     */
    A1("&cCommand must start with a \"/\"."),
    A2("&aAdded command {0} to the lucky block \"{1}\"."),
    A3("&eYou have stopped adding a potion effect."),
    A4("&4Unknown potion effect type \"{0}\". Try again."),
    A5("&7Potion Effect Type: &e{0}\n&7Amplifier: &e{1}\n&7Duration: &e{2}"),
    A6("&6(2/3) > &ePlease enter the amplifier of the potion. (0-255)"),
    A7("&6(3/3) > &ePlease enter the duration of the potion. (0-32768)"),
    A8("&cYour number must be between 0 and 255."),
    A9("&cYour number must be between 0 and 32768."),
    A10("&aYou successfully added a potion effect to this drop!"),
    M1("&aEditing block {0} drop #{1}."),
    M2("&7You have left the structure modification world."),
    M3("&cYou were in a structure world on a server reload/restart."),
    M4("\n&d&l[!] &fYou have entered structure editing mode.&7\nPlace blocks and spawn mobs to edit this structure.\nWhen finished, type &6/mlb save&7 to save your changes.\nType &6/mlb exit&7 to edit this editor.\n"),
    M5("&a&oLoaded your previously saved structure."),
    M6("&dYou have placed an entity \"{0}\" to spawn at ({1},{2},{3})."),
    M7("&cBuild Limit Reached >> {0}"),
    M8("&eYou broke the reference lucky block. Particles will spawn to indicate where the reference block is."),
    M9("&7&o[MoreLuckyBlocks: &8Changes Saved&7&o]"),
    M10("&7You are currently viewing drop &8#&e{0}."),
    M11("&7Structures represent the blocks that are changed whenever a lucky block is broken. Some drops may not have structures (ie. only drop items)."),
    M12("Lucky Block List"),
    M13("\n&8(&7{0}&8)\n&3Possible Drops&7: {1}\n&9/mlb info {0}\n&7for more information.\n"),
    M14("&a%check% You can break this lucky block!"),
    M15("&c%cross% You cannot break this lucky block."),
    M16("&0Redeem List"),
    M17("&0Redeem Menu"),
    M18("&0Redeem for: {0}"),
    M19("&c-1 Quantity"),
    M20("&a+1 Quantity"),
    M21("&7Decrease the amount of requested lucky blocks to: &6{0}&7."),
    M22("&7Increase the amount of requested lucky blocks to: &6{0}&7."),
    M23("&2&lConfirm"),
    M24("&7Confirm your transaction to buy {0} of {1}."),
    M25("&c&lDecline"),
    M26("&7Decline the transaction."),
    M27("&c&lCould not complete transaction."),
    M28("&7You are missing an item: &fx{0} {1}&7.\n&7Hover over to view information."),
    M29("&cPlease have at least one open slot in your inventory to exchange!"),
    M30("&2Success!\n&fYou purchased {0}x {1} &flucky blocks!"),
    M31("&cCancelled transaction."),
    M32("&0Viewing Item: {0}"),
    M33("&cThis lucky block does not have a required tool to break it."),
    M34("&cThere are no lucky blocks to view right now."),
    M35("&cYou must hold a valid lucky block in your hand.\nOr use: /mlb drops <name>"),
    M36("&cOut of range. Lucky block {0} only has {1} drops. Including drop zero."),
    M37("""
            &7Info for &9{0}
             &f%arrow% &7Item Name &8%small_arrow% &a{1}
             &f%arrow% &7Block Type &8%small_arrow% &a{2}
             &f%arrow% &7Default Luck &8%small_arrow% &a{3}
             &f%arrow% &7# of Drops &8%small_arrow% &a{4}
             &f%arrow% &7Break Cooldown &8%small_arrow% &a{5}
             &f%arrow% &7Place Cooldown &8%small_arrow% &a{6}
            """),
    M38("""
            &7Info for &9{0}
             &f%arrow% &7Item Name &8%small_arrow% &a{1}
             &f%arrow% &7Block Type &8%small_arrow% &a{2}
             &f%arrow% &7Default Luck &8%small_arrow% &a{3}
             &f%arrow% &7# of Drops &8%small_arrow% &a{4}
             &f%arrow% &7Break Cooldown &8%small_arrow% &a{5}
             &f%arrow% &7Place Cooldown &8%small_arrow% &a{6}
             &c%arrow% &7Enchanted &8%small_arrow% &a{7}
             &c%arrow% &7Break Permission &8%small_arrow% &a{8}
             &c%arrow% &7Lore Lines &8%small_arrow% &a{9}
            """),
    M39("  %pick% Requires Special Tool &8(&e&l?&8)"),
    M40("  %check% Has Particles"),
    M41("  %check% Plays Sound when Broken"),
    M42("&2You have permission to break this lucky block."),
    M43("&cYou do not have permission to break this lucky block."),
    M44("&o-- Drops --"),
    M45("&9[{0}]"),
    M46("&cYou cannot break a lucky block with a silk touch pickaxe!")

    //TODO FOR LATER: START ON USAGE COMMAND AND GO UP.


    ;


    // Singleton Accessprivate static MoreLuckyBlocks plugin;

    private static MoreLuckyBlocks plugin;

    static {
        int index = 1;
        for (Message m : EnumSet.allOf(Message.class)) {
            m.id = index;
            index++;
        }
    }

    private final String defaultValue;
    private int id;

    Message(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public static String get(int id) {
        return plugin.getMessagesManager().getPrefix() + Util.toColor((String) Objects.requireNonNull(plugin.getMessagesManager().getConfig().get(String.valueOf(id))));
    }

    public static void loadDefaults(YamlConfiguration cfg) {
        EnumSet<Message> messages = EnumSet.allOf(Message.class);
        for (Message m : messages) {
            if (cfg.get(String.valueOf(m.id)) != null) {
                continue;
            }
            cfg.set(String.valueOf(m.id), m.defaultValue);
        }
    }

    public static void register(MoreLuckyBlocks pl) {
        if (plugin != null) {
            return;
        }
        plugin = pl;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String format(Object... args) {
        String raw = get();
        int index = 0;

        String[] argList = new String[args.length];

        for (int i = 0; i < args.length; i++) {
            argList[i] = args[i].toString();
        }

        for (String s : argList) {
            raw = StringUtils.replace(raw, "{" + index + "}", s);
            index++;
        }
        return raw;
    }

    public String get() {
        if (plugin.getMessagesManager().getConfig().get(String.valueOf(this.id)) == null) {
            plugin.getServer().getLogger().severe("Displayed unknown message of id: " + this.id);
            return plugin.getMessagesManager().getPrefix() + Util.toColor(defaultValue);
        }
        return (plugin.getMessagesManager().getPrefix() + Util.toColor((String) Objects.requireNonNull(plugin.getMessagesManager().getConfig().get(String.valueOf(this.id)))))
                .replaceAll("%check%", "✔")
                .replaceAll("%cross%", "✖")
                .replaceAll("%arrow%", "▶")
                .replaceAll("%pick%", "⛏")
                .replaceAll("%small_arrow%", "→");
    }

    public int getId() {
        return this.id;
    }

    public void setId(int num) {
        this.id = num;
    }

    public String asDebug() {
        return "Message" + " [" + this.name() + "] -> {" + "defaultValue='" + defaultValue + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public String toString() {
        return get();
    }
}
