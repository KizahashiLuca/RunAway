package runaway.plugin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class WarTimer2 extends BukkitRunnable{
	private final RunAway plg;
	private Scoreboard s;
	private Objective obj;
	private int count;

	public WarTimer2(RunAway plg_, int count_) {
		plg = plg_;
		count = count_;
	}

	@Override
	public void run() {
		if (!StartGame.BeginFlag) {
			return;
		} else if (StartGame.BeginFlag) {
			s = Bukkit.getScoreboardManager().getMainScoreboard();
			obj = s.getObjective("Timer");
			obj.getScore("残り").setScore(count);

			if (count == 600 || count == 300 || count == 180 || count == 60) {
				plg.getServer().dispatchCommand(plg.getServer().getConsoleSender(),
						"title @a title {\"text\":\"ゲーム終了まで\", \"color\":\"red\", \"bold\":true}");
				plg.getServer().dispatchCommand(plg.getServer().getConsoleSender(),
						"title @a subtitle {\"text\":\"あと" + count/60 + "分\", \"color\":\"red\", \"bold\":true}");
			} else if (count == 30 || count == 10) {
				plg.getServer().dispatchCommand(plg.getServer().getConsoleSender(),
						"title @a title {\"text\":\"ゲーム終了まで\", \"color\":\"red\", \"bold\":true}");
				plg.getServer().dispatchCommand(plg.getServer().getConsoleSender(),
						"title @a subtitle {\"text\":\"あと" + count + "秒\", \"color\":\"red\", \"bold\":true}");
			} else if (count <= 5) {
				plg.getServer().dispatchCommand(plg.getServer().getConsoleSender(),
						"title @a title {\"text\":\"" + count + "\", \"color\":\"red\", \"bold\":true}");
			} else if (count == 0) {
				plg.getServer().dispatchCommand(plg.getServer().getConsoleSender(),
						"title @a title {\"text\":\"ゲーム終了\", \"color\":\"red\", \"bold\":true}");
				EndGame();
				return;
			}
		}

		if (count>0 && !StartGame.isPaused) {
			count--;
			new WarTimer2(plg, count).runTaskLater(plg, 20);
		} else if (count>0 && StartGame.isPaused) {
			count+=0;
			new WarTimer2(plg, count).runTaskLater(plg, 20);
		}
	}

	public void EndGame() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.setGameMode(GameMode.SPECTATOR);
		}
		StartGame.BeginFlag = false;
		StartGame.isPaused = false;
		obj.unregister();
	}

}
