package devjluvisi.mlb.blocks;

import org.bukkit.potion.PotionEffectType;

public class LuckyBlockPotionEffect {
	
	private PotionEffectType type;
	private int duration;
	private int amplifier;
	
	public LuckyBlockPotionEffect(PotionEffectType type, int duration, int amplifier) {
		super();
		this.type = type;
		this.duration = duration;
		this.amplifier = amplifier;
	}
	
	public PotionEffectType getType() {
		return type;
	}
	public void setType(PotionEffectType type) {
		this.type = type;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getAmplifier() {
		return amplifier;
	}
	public void setAmplifier(int amplifier) {
		this.amplifier = amplifier;
	}
	
	/**
	 * Parses a new instance of this object from a parseable string.
	 * Strings from potion effects are represented as follows: [TYPE, AMPLIFIER, DURATION].
	 * 
	 * Example:  [STRENGTH, 2, 30] -> Strength II for 30s
	 * 
	 * @param raw String to parse from.
	 */
	public static LuckyBlockPotionEffect parseFromFile(String raw) {
		raw = raw.replace("[", "").replace("]", "");

		String[] cut = raw.split(",");
		PotionEffectType potionType = PotionEffectType.getByName(cut[0].toUpperCase().trim());
		int amplifier = Integer.parseInt(cut[1].trim());
		int duration = Integer.parseInt(cut[2].trim());
		return new LuckyBlockPotionEffect(potionType, duration, amplifier);
	}
	
	@Override
	public String toString() {
		return "LuckyBlockPotionEffect [type=" + type + ", duration=" + duration + ", amplifier=" + amplifier + "]";
	}
	
	
	

}
