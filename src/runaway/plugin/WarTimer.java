package runaway.plugin;

import org.bukkit.scheduler.BukkitRunnable;

public class WarTimer extends BukkitRunnable{
	private final RunAway plg;
	private int count;

	public WarTimer(RunAway plg_, int count_) {
		plg = plg_;
		count = count_;
	}

	@Override
	public void run() {
		if (!StartGame.BeginFlag) {
			return;
		} else if (StartGame.BeginFlag) {
			plg.getServer().dispatchCommand(plg.getServer().getConsoleSender(),
					"title @a title {\"text\":\"" + count + "\", \"color\":\"red\", \"bold\":true}");
			if (count == 0) {
				plg.getServer().dispatchCommand(plg.getServer().getConsoleSender(),
						"title @a title {\"text\":\"ゲームスタート\", \"color\":\"red\", \"bold\":true}");
			}
			count--;
		}
		if (count>=0 ) {
			new WarTimer(plg, count).runTaskLater(plg, 20);
		}
	}
}
