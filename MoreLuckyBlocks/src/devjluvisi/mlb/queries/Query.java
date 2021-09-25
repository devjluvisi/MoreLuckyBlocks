package devjluvisi.mlb.queries;

import java.util.UUID;

import org.bukkit.Bukkit;

import devjluvisi.mlb.MoreLuckyBlocks;

/**
 * A class for all different types of queries in MoreLuckyBlocks.
 * <br />
 * <b>A query</b> represents a task or command the server wants to perform at a later date.
 * Instead of performing such action suddenly, the server should wait until another method call
 * gets processed in which the queries in the main class will be looped through.
 * 
 * Each query is pre-defined with the code to execute and is mapped to a player UUID.
 * <br />
 * Such an example of a query could be a confirm menu.
 * We send the user to a new menu but we do not want to "forget" the task they are wanting
 * to confirm for. So we add a query before we send the user to the confirm menu, if the user confirms,
 * then the query is executed.
 * 
 * @author jacob
 *
 */
public abstract class Query {
	private UUID playerUUID;
	private MoreLuckyBlocks plugin;

	public Query(UUID u, MoreLuckyBlocks plugin) {
		this.plugin = plugin;
		this.playerUUID = u;
	}
	
	/**
	 * Adds a query to the plugin.
	 * No parameters are needed.
	 */
	public void add() {
		plugin.getRequests().add(this);
	}
	

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public MoreLuckyBlocks getPlugin() {
		return plugin;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Query)) {
			return false;
		}
		return ((Query)obj).playerUUID == this.playerUUID;
	}
	
	@Override
	public int hashCode() {
		return (int)playerUUID.getMostSignificantBits();
	}
	
	/**
	 * Executes the query starting from the parent {@see Query} class.
	 * Automatically removes the user from the set.
	 */
	public void execute() {
		plugin.getRequests().remove(this);
		runProcess();
	}
	
	public static Query queryContains(MoreLuckyBlocks plugin, UUID playerUUID) {
		for(Query q: plugin.getRequests()) {
			if(q.playerUUID == playerUUID) {
				return q;
			}
		}
		return null;
	}
	

	protected abstract void runProcess();

}
