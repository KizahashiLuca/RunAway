package runaway.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public class RunAway extends JavaPlugin {

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onEnable() {
		super.onEnable();
		getCommand("StartGame").setExecutor(new StartGame(this));
	}

}
