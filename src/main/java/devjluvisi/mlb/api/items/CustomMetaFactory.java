package devjluvisi.mlb.api.items;

import devjluvisi.mlb.api.items.itemdata.util.ServerVersion;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.TreeMap;
import java.util.function.BiFunction;

// TODO Remove?
public class CustomMetaFactory {

    private static final TreeMap<ServerVersion, BiFunction<CustomMetaFactory, ItemMeta, CustomItemMeta>> functionMap = new TreeMap<>(
            ServerVersion::compareTo);

    static {
        functionMap.put(ServerVersion.v1_8, (metaFactory, meta) -> new LegacyCustomItemMeta(meta));
        functionMap.put(ServerVersion.v1_14,
                (metaFactory, meta) -> new NewCustomItemMeta(metaFactory.getJavaPlugin(), meta));
    }

    private final BiFunction<CustomMetaFactory, ItemMeta, CustomItemMeta> biFunction;
    private JavaPlugin javaPlugin;

    public CustomMetaFactory(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
        this.biFunction = functionMap.floorEntry(ServerVersion.fromServer(javaPlugin.getServer())).getValue();
    }

    public JavaPlugin getJavaPlugin() {
        return this.javaPlugin;
    }

    public void setJavaPlugin(JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
    }

    public CustomItemMeta createCustomMeta(ItemMeta meta) {
        return this.biFunction.apply(this, meta);
    }

}
