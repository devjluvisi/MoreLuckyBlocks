package devjluvisi.fb;

import org.bukkit.plugin.java.JavaPlugin;


/**
 * FortuneBlocks - 1.17
 * -> LuckyBlocks plugin for Minecraft Spigot 1.17
 * <br />
 * GitHub: https://github.com/devjluvisi/FortuneBlocks
 * 
 * @version UNREL
 * @author devjluvisi (Jacob Luvisi)
 * IGN: Interryne
 *
 */
public class FortuneBlocks extends JavaPlugin {
	
	@Override
	public void onEnable() {
		getLogger().info("LuckyBlocks 1.17 has started!");
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		getLogger().info("LuckyBlocks 1.17 has stopped!");
		super.onDisable();
	}
	
	public static void main(String[] args) {
		System.out.println("Hello");
	}
	
	

}
