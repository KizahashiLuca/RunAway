package runaway.plugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import net.md_5.bungee.api.ChatColor;

public class StartGame implements CommandExecutor {

	private final RunAway plg;
	private Player player;
	private Scoreboard s;
	private Objective obj;
	private List<String> members;
	public static boolean BeginFlag;
	public static boolean isPaused;

	// ゲームスタート時にスポーンする場所のリスト
	int[][] SpawnPlace = new int[][] {
			{204, 68, 302},
			{201, 80, 378}
	};

	protected enum gamerulelist {
		doDaylightCycle,
		doMobSpawning,
		doWeatherCycle
	};


	public StartGame(RunAway plg_) {
		plg=plg_;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String lavel, String[] args) {
		// senderの検証 コンソールからの実行禁止
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "ゲーム内から実行してください.");
			return false;
		}
		player = (Player) sender;

		// 実行権限
		if (!(player.hasPermission("some.pointless.permisssion"))) {
			player.sendMessage(ChatColor.RED + "あなたにこのコマンドの実行権限はありません.");
			return false;
		}

		// パラメタ長の検証
		if (args.length != 0) {
			player.sendMessage(ChatColor.RED + "コマンドは単体で実行します. ");
			return false;
		}

		// BeginFlagがtrueの場合(ゲーム中の場合)機能しない
		if (BeginFlag) {
			player.sendMessage(ChatColor.RED + "ゲームは既に行われています.  ");
			return true;
		}
		BeginFlag = true;

		// 環境整備
		// 時間/天候/モブキルの変更
		plg.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"time set 1000");
		plg.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"weather clear");
		plg.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"kill @e[type=Zombie,type=Skeleton,type=Creeper,type=Witch,type=Enderman,type=Spider,type=Slime,type=Zombie_villager,type=Cave_Spider]");

		// Gameruleの変更
		for (gamerulelist g : gamerulelist.values()) {
			plg.getServer().dispatchCommand(Bukkit.getConsoleSender(),
					"gamerule " + g + " false");
		}

		// プレイヤー列挙
		members = new ArrayList<String>();
		for (Player p : Bukkit.getOnlinePlayers()) {
			members.add(p.getName());
		}

		// 時間設定
		int oneday = 60 * 30;  // 60(s) * 20(m) = 3600(s)
		int countdown = 10;  // ゲーム開始カウントダウン 10(s)
		int term = oneday + countdown;

		new WarTimer(plg, countdown).runTaskLater(plg, 0);

		// timerの生成
		timerObjectives();

		// Sidebar timer
		new WarTimer2(plg, term).runTaskLater(plg, countdown * 20);

		return true;
	}

	private void timerObjectives() {
		s = Bukkit.getScoreboardManager().getMainScoreboard();
		if (s.getObjective("Timer") != null) {
			s.getObjective("Timer").unregister();
		}
		obj = s.registerNewObjective("Timer", "dummy");
		String sidebarTitle = "残り時間";
		obj.setDisplayName(sidebarTitle);
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
}
