package at.kitsoft.rcfuss.event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.JobProgression;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.MoneyAPI;
import at.kitsoft.rcfuss.api.PerformanceMonitor;
import at.kitsoft.rcfuss.api.PrefixType;
import at.kitsoft.rcfuss.api.Serverupdater;
import at.kitsoft.rcfuss.main.Main;
import at.kitsoft.rcfuss.mysql.lb.MySQL;
import net.md_5.bungee.api.ChatColor;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class SBHandler implements Listener{
	
	static int sbmain = 0;
	private static HashMap<String, String> roleHM = new HashMap<>();
	private static HashMap<String, String> tabHM = new HashMap<>();
	private static HashMap<String, String> chatHM = new HashMap<>();
	
	public void setScoreboard(Player p) {
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective o = sb.registerNewObjective("aaa", "bbb");
		
		PermissionUser pu = PermissionsEx.getUser(p);
		APIs api = new APIs();
		int pusergen = api.getPlayers("BungeeCord", "currPlayers");
		int pusermax = api.getPlayers("BungeeCord", "maxPlayers");
		int pusercurr = Bukkit.getOnlinePlayers().size();
		int userperc = (pusergen * 100 / pusermax);
		double moneyTotal = MoneyAPI.getMoneyTotal(p.getUniqueId());
		String moneyFormat = new DecimalFormat("#.##").format(moneyTotal);
		
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
		o.setDisplayName(api.getPrefix(PrefixType.SCOREBOARD));
		if(getSB(p) == 0) {
			//Sideboard has been disabled
		}else if(getSB(p) == 1) {
			if (sbmain >= 0 && sbmain <= 5) {
				o.getScore("§7Player:").setScore(7);
				if (userperc <= 20) {
					o.getScore("  §7(§9" + pusercurr + "§7) §a" + pusergen + " §7/§c " + pusermax).setScore(6);
				} else if (userperc >= 21 && userperc <= 40) {
					o.getScore("  §7(§9" + pusercurr + "§7) §2" + pusergen + " §7/§c " + pusermax).setScore(6);
				} else if (userperc >= 41 && userperc <= 60) {
					o.getScore("  §7(§9" + pusercurr + "§7) §e" + pusergen + " §7/§c " + pusermax).setScore(6);
				} else if (userperc >= 61 && userperc <= 80) {
					o.getScore("  §7(§9" + pusercurr + "§7) §6" + pusergen + " §7/§c " + pusermax).setScore(6);
				} else if (userperc >= 81 && userperc <= 90) {
					o.getScore("  §7(§9" + pusercurr + "§7) §c" + pusergen + " §7/§c " + pusermax).setScore(6);
				} else if (userperc >= 91) {
					o.getScore("  §7(§9" + pusercurr + "§7) §4" + pusergen + " §7/§c " + pusermax).setScore(6);
				}
				o.getScore("  §a").setScore(5);
				o.getScore("§7Ping:").setScore(4);
				o.getScore("  §a" + api.getPlayerPing(p) + "§7ms").setScore(3);
				o.getScore("  §b").setScore(2);
				o.getScore(api.returnStringReady(p, "scoreboard.sideboard.rank")).setScore(1);
				o.getScore(retGroup(pu)).setScore(0);

			} else if (sbmain >= 6 && sbmain <= 10) {
				o.getScore("§7Server:").setScore(8);
				o.getScore("  §a" + api.getServerName() + " §7/§a " + api.getServerId()).setScore(7);
				o.getScore("  §9").setScore(6);
				o.getScore(api.returnStringReady(p, "scoreboard.sideboard.money")).setScore(5);
				o.getScore("  §a" + moneyFormat + "§7 Coins").setScore(4);
				o.getScore("  §8").setScore(2);
				o.getScore(api.returnStringReady(p, "scoreboard.sideboard.playerid")).setScore(1);
				o.getScore("  §7" + igpre(p) + " §9" + igid(p)).setScore(0);
			}
			p.setScoreboard(sb);
		}else if(getSB(p) == 2) {
			if (Main.serverlist.contains(api.getServerName())) {
				List<JobProgression> jobs = Jobs.getPlayerManager().getJobsPlayer(p).getJobProgression();
				int i = 0;
				int j = jobs.size();
				DecimalFormat df = new DecimalFormat("#.##");
				o.getScore("§9Jobs").setScore(15);
				for (JobProgression job : jobs) {
					o.getScore("§7Level: §a" + job.getLevel()).setScore(i);
					i++;
					o.getScore(
							"§7Exp: §a" + df.format(job.getExperience()) + " §7/§c" + df.format(job.getMaxExperience()))
							.setScore(i);
					i++;
					o.getScore("§7Job: §6" + job.getJob().getName()).setScore(i);
					i++;
					o.getScore("§9  Job " + j).setScore(i);
					j--;
					i++;
				}
			} else {
				o.getScore("§e" + api.getServerName() + " §cdoes").setScore(6);
				o.getScore("§cnot support").setScore(5);
				o.getScore("§cthe Jobview!").setScore(4);
				o.getScore("§b").setScore(3);
				o.getScore("§cSetting back").setScore(2);
				o.getScore("§cto default").setScore(1);
				o.getScore("§cview!").setScore(0);
				setSB(p, 1);
			}
			p.setScoreboard(sb);
		}else if(getSB(p) == 3) {
			if (sbmain >= 0 && sbmain <= 5) {
				o.getScore("§7Newest Report").setScore(9);
				o.getScore("§a§b§c").setScore(8);
				o.getScore("§7Reporter:").setScore(7);
				o.getScore("  §1§a" + retLatestReport("reporter")).setScore(6);
				o.getScore("§7Perpetrator:").setScore(5);
				o.getScore("  §2§c" + retLatestReport("perpetrator")).setScore(4);
				o.getScore("§7Server:").setScore(3);
				o.getScore("  §3§a" + retLatestReport("server")).setScore(2);
				o.getScore("§7Reason:").setScore(1);
				o.getScore("  §4§a" + retLatestReport("reason")).setScore(0);
			} else if (sbmain >= 6 && sbmain <= 10) {
				o.getScore("§7Reports total " + api.getServerName()).setScore(7);
				o.getScore("§1  §a" + retRepsTotCuSe() + " §7Reports").setScore(6);
				o.getScore("§7Reports today " + api.getServerName()).setScore(5);
				o.getScore("§2  §a" + retRepsTodCuSe() + " §7Reports").setScore(4);
				o.getScore("§7Reports total Network").setScore(3);
				o.getScore("§3  §a" + retRepstotal() + " §7Reports").setScore(2);
				o.getScore("§7Reports today Network").setScore(1);
				o.getScore("§4  §a" + retRepsToday() + " §7Reports").setScore(0);
			}
			p.setScoreboard(sb);
		}else if(getSB(p) == 4) {
			Runtime runtime = Runtime.getRuntime();
			int cpucores = runtime.availableProcessors();
			double cpuload = PerformanceMonitor.getProcessCpuLoad();
			System.out.println("CPULOAD DEBUG " + cpuload);
			long ramusage = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
			long ramtotal = runtime.totalMemory() / 1048576L;
			float rampercentage = (ramusage * 100 / ramtotal);
			float networkuserperc = (pusergen * 100 / pusermax);
			float curruserperc = (pusercurr * 100 / pusermax);

			if (sbmain >= 0 && sbmain <= 5) {
				o.getScore("§7CPU load: (§a" + cpucores + " §7Cores)").setScore(7);
				o.getScore(visualizeDouble(cpuload, "§1")).setScore(6);
				o.getScore("§7Ramload:").setScore(5);
				o.getScore(visualizeDouble(rampercentage, "§2")).setScore(4);
				o.getScore("§7Playerload Network:").setScore(3);
				o.getScore(visualizeDouble(networkuserperc, "§3")).setScore(2);
				o.getScore("§7Playerload " + api.getServerName()).setScore(1);
				o.getScore(visualizeDouble(curruserperc, "§4")).setScore(0);
			} else if (sbmain >= 6 && sbmain <= 10) {
				DecimalFormat df = new DecimalFormat("#.##");
				o.getScore("§7CPU load: (§a" + cpucores + " §7Cores)").setScore(7);
				o.getScore("  §1§a" + df.format(cpuload) + "§7% usage").setScore(6);
				o.getScore("§7Ramload: §a").setScore(5);
				o.getScore("  §2§a" + ramusage + "§7MB /§c " + ramtotal + "§7MB").setScore(4);
				o.getScore("§7Playerload Network:").setScore(3);
				o.getScore("  §3§a" + pusergen + " §7Players").setScore(2);
				o.getScore("§7Playerload " + api.getServerName()).setScore(1);
				o.getScore("  §4§a" + Bukkit.getOnlinePlayers().size() + " §7Players").setScore(0);
			}
			p.setScoreboard(sb);
		}else if(getSB(p) == 5) {
			String pl = getRadio("playlist");
			String art = getRadio("artist");
			String tra = getRadio("track");
			String alb = getRadio("album");
			String currListen = getRadio("current_listener");
			o.getScore("§aRedi§6FM").setScore(11);
			o.getScore("§a§b").setScore(10);
			o.getScore("§7Current Playlist:").setScore(9);
			if (pl.length() <= 24) {
				o.getScore("  §9§a" + pl).setScore(8);
			} else {
				o.getScore("  §9§a" + pl.substring(0, 24)).setScore(8);
			}
			o.getScore("§7Current Listeners:").setScore(7);
			o.getScore("  §6§a" + currListen).setScore(6);
			o.getScore("§7Current Track:").setScore(5);
			if (tra.length() <= 24) {
				o.getScore("  §8§a" + tra).setScore(4);
			} else {
				o.getScore("  §8§a" + tra.substring(0, 24)).setScore(4);
			}
			o.getScore("§7Current Artist:").setScore(3);
			if (art.length() <= 24) {
				o.getScore("  §7§a" + art).setScore(2);
			} else {
				o.getScore("  §7§a" + art.substring(0, 24)).setScore(2);
			}
			if (!alb.equalsIgnoreCase("null") || !alb.equalsIgnoreCase("none")) {
				o.getScore("§7Current Album:").setScore(1);
				if (alb.length() <= 24) {
					o.getScore("  §6§a" + alb).setScore(0);
				} else {
					o.getScore("  §6§a" + alb.substring(0, 24)).setScore(0);
				}
			}
			p.setScoreboard(sb);
		}else if(getSB(p) == 6) {
			if (p.hasPermission("mlps.isTeam")) {
				o.getScore("§7Servers/Players:").setScore(8);
				o.getScore("  §a§lNetwork§7: §a" + getPlayers("BungeeCord")).setScore(7);
				o.getScore("  §bStaffserver§7: §a" + getPlayers("Staffserver")).setScore(6);
			} else {
				o.getScore("§7Servers/Players:").setScore(7);
				o.getScore("  §aNetwork§7: §a" + getPlayers("BungeeCord")).setScore(6);
			}
			o.getScore("  §6Lobby§7: §a" + getPlayers("Lobby")).setScore(5);
			o.getScore("  §eCreative§7: §a" + getPlayers("Creative")).setScore(4);
			o.getScore("  §cSurvival§7: §a" + getPlayers("Survival")).setScore(3);
			o.getScore("  §fSky§2Block§7: §a" + getPlayers("SkyBlock")).setScore(2);
			o.getScore("  §6Towny§7: §a" + getPlayers("Towny")).setScore(1);
			o.getScore("  §5Farmserver§7: §a" + getPlayers("Farmserver")).setScore(0);
			p.setScoreboard(sb);
		}else if(getSB(p) == 7) {
			Location loc = p.getLocation();
			DecimalFormat df = new DecimalFormat("#.##");
			o.getScore("§7Position").setScore(6);
			o.getScore("§0").setScore(5);
			o.getScore("§7Server:§a " + api.getServerName()).setScore(4);
			o.getScore("§7World:§a " + loc.getWorld().getName()).setScore(3);
			o.getScore("§7X:§a " + df.format(loc.getX())).setScore(2);
			o.getScore("§7Y:§a " + df.format(loc.getY())).setScore(1);
			o.getScore("§7Z:§a " + df.format(loc.getZ())).setScore(0);
			p.setScoreboard(sb);
		}
		
		Team pman = getTeam(sb, "pman");
		Team apman = getTeam(sb, "apman");
		Team sman = getTeam(sb, "sman");
		Team gmmman = getTeam(sb, "gmmman");
		Team dev = getTeam(sb, "dev");
		Team gman = getTeam(sb, "gman");
		Team adprman = getTeam(sb, "adprman");
		Team sda = getTeam(sb, "sda");
		Team cm = getTeam(sb, "cm");
		Team ct = getTeam(sb, "ct");
		Team st = getTeam(sb, "st");
		Team bd = getTeam(sb, "bd");
		Team gm = getTeam(sb, "gm");
		Team aot = getTeam(sb, "aot");
		Team train = getTeam(sb, "train");
		Team rltm = getTeam(sb, "rltm");
		Team rtm = getTeam(sb, "rtm");
		Team part = getTeam(sb, "part");
		Team fs = getTeam(sb, "fs");
		Team nb = getTeam(sb, "nb");
		Team bt = getTeam(sb, "bt");
		Team friend = getTeam(sb, "friend");
		Team vip = getTeam(sb, "vip");
		Team player = getTeam(sb, "default");
		Team afk = getTeam(sb, "safk");
		for(Player all : Bukkit.getOnlinePlayers()) {
			PermissionUser pp = PermissionsEx.getUser(all);
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT nickname,loggedin,userprefix FROM redicore_userstats WHERE uuid = ?");
				ps.setString(1, all.getUniqueId().toString().replace("-", ""));
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					if (rs.getString("nickname").equalsIgnoreCase("none")) {
						all.setCustomName(all.getName());
					} else {
						all.setCustomName(rs.getString("nickname"));
					}
					String prefix = "";
					if (!rs.getString("userprefix").equalsIgnoreCase("RESET")) {
						prefix = rs.getString("userprefix");
					}
					if (isAFK(all)) {
						afk.addEntry(all.getName());
						all.setPlayerListName(retPrefix("safk", "prefix_tab") + all.getCustomName() + " §7| ID§7: §a"
								+ igid(all) + " §f" + prefix);
					} else {
						if (!rs.getBoolean("loggedin")) {
							player.addEntry(all.getName());
							all.setDisplayName(retPrefix("default", "prefix_chat") + all.getCustomName());
							all.setPlayerListName(retPrefix("default", "prefix_tab") + all.getCustomName() + " §7| ID: §a"
									+ igid(all) + " §f" + prefix);
						} else {
							if (pp.inGroup("pman")) {
								pman.addEntry(all.getName());
								all.setDisplayName(retPrefix("pman", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("pman", "prefix_tab") + all.getName() + " §7| ID: §a"
										+ igid(all) + " §f" + prefix);
							} else if (pp.inGroup("apman")) {
								apman.addEntry(all.getName());
								all.setDisplayName(retPrefix("apman", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("aman", "prefix_tab") + all.getName() + " §7| ID: §a"
										+ igid(all) + " §f" + prefix);
							}else if (pp.inGroup("sman")) {
								sman.addEntry(all.getName());
								all.setDisplayName(retPrefix("sman", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("sman", "prefix_tab") + all.getName() + " §7| ID: §a"
										+ igid(all) + " §f" + prefix);
							}else if (pp.inGroup("adprman")) {
								adprman.addEntry(all.getName());
								all.setDisplayName(retPrefix("adprman", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("adprman", "prefix_tab") + all.getName() + " §7| ID: §a"
										+ igid(all) + " §f" + prefix);
							} else if (pp.inGroup("dev")) {
								dev.addEntry(all.getName());
								all.setDisplayName(retPrefix("dev", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("dev", "prefix_tab") + all.getName() + " §7| ID§7: §a"
										+ igid(all) + " §f" + prefix);
							} else if (pp.inGroup("gman")) {
								gman.addEntry(all.getName());
								all.setDisplayName(retPrefix("gman", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("gman", "prefix_tab") + all.getName() + " §7| ID: §a"
										+ igid(all) + " §f" + prefix);
							} else if (pp.inGroup("sda")) {
								sda.addEntry(all.getName());
								all.setDisplayName(retPrefix("sda", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("sda", "prefix_tab") + all.getName() + " §7| ID: §a" + igid(all)
										+ " §f" + prefix);
							} else if (pp.inGroup("gmmman")) {
								gmmman.addEntry(all.getName());
								all.setDisplayName(retPrefix("gmmman", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("gmmman", "prefix_tab") + all.getName() + " §7| ID: §a"
										+ igid(all) + " §f" + prefix);
							} else if (pp.inGroup("gm")) {
								gm.addEntry(all.getName());
								all.setDisplayName(retPrefix("gm", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("gm", "prefix_tab") + all.getName() + " §7| ID: §a" + igid(all)
										+ " §f" + prefix);
							} else if (pp.inGroup("cm")) {
								cm.addEntry(all.getName());
								all.setDisplayName(retPrefix("cm", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("cm", "prefix_tab") + all.getName() + " §7| ID: §a" + igid(all)
										+ " §f" + prefix);
							} else if (pp.inGroup("ct")) {
								ct.addEntry(all.getName());
								all.setDisplayName(retPrefix("ct", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("ct", "prefix_tab") + all.getName() + " §7| ID: §a" + igid(all)
										+ " §f" + prefix);
							} else if (pp.inGroup("st")) {
								st.addEntry(all.getName());
								all.setDisplayName(retPrefix("st", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("st", "prefix_tab") + all.getName() + " §7| ID: §a" + igid(all)
										+ " §f" + prefix);
							} else if (pp.inGroup("bd")) {
								bd.addEntry(all.getName());
								all.setDisplayName(retPrefix("bd", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("bd", "prefix_tab") + all.getName() + " §7| ID: §a" + igid(all)
										+ " §f" + prefix);
							} else if (pp.inGroup("aot")) {
								aot.addEntry(all.getName());
								all.setDisplayName(retPrefix("aot", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("aot", "prefix_tab") + all.getName() + " §7| ID: §a" + igid(all)
										+ " §f" + prefix);
							} else if (pp.inGroup("train")) {
								train.addEntry(all.getName());
								all.setDisplayName(retPrefix("train", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("train", "prefix_tab") + all.getName() + " §7| ID: §a"
										+ igid(all) + " §f" + prefix);
							} else if (pp.inGroup("rltm")) {
								rltm.addEntry(all.getName());
								all.setDisplayName(retPrefix("rltm", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("rltm", "prefix_tab") + all.getName() + " §7| ID: §a"
										+ igid(all) + " §f" + prefix);
							} else if (pp.inGroup("rtm")) {
								rtm.addEntry(all.getName());
								all.setDisplayName(retPrefix("rtm", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("rtm", "prefix_tab") + all.getName() + " §7| ID: §a" + igid(all)
										+ " §f" + prefix);
							} else if (pp.inGroup("part")) {
								part.addEntry(all.getName());
								all.setDisplayName(retPrefix("part", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("part", "prefix_tab") + all.getName() + " §7| ID: §a"
										+ igid(all) + " §f" + prefix);
							} else if (pp.inGroup("bt")) {
								bt.addEntry(all.getName());
								all.setDisplayName(retPrefix("bt", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("bt", "prefix_tab") + all.getName() + " §7| ID: §a" + igid(all)
										+ " §f" + prefix);
							} else if (pp.inGroup("fs")) {
								fs.addEntry(all.getName());
								all.setDisplayName(retPrefix("fs", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("fs", "prefix_tab") + all.getName() + " §7| ID: §a" + igid(all)
										+ " §f" + prefix);
							} else if (pp.inGroup("nb")) {
								nb.addEntry(all.getName());
								all.setDisplayName(retPrefix("nb", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("nb", "prefix_tab") + all.getName() + " §7| ID: §a" + igid(all)
										+ " §f" + prefix);
							} else if (pp.inGroup("friend")) {
								friend.addEntry(all.getName());
								all.setDisplayName(retPrefix("friend", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("friend", "prefix_tab") + all.getName() + " §7| ID: §a"
										+ igid(all) + " §f" + prefix);
							} else if (pp.inGroup("vip")) {
								vip.addEntry(all.getName());
								all.setDisplayName(retPrefix("vip", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("vip", "prefix_tab") + all.getName() + " §7| ID: §a" + igid(all)
										+ " §f" + prefix);
							} else if (pp.inGroup("default")) {
								player.addEntry(all.getName());
								all.setDisplayName(retPrefix("default", "prefix_chat") + all.getName());
								all.setPlayerListName(retPrefix("default", "prefix_tab") + all.getName() + " §7| ID: §a"
										+ igid(all) + " §f" + prefix);
							} else {
								player.addEntry(all.getName());
								all.setDisplayName("§cunknown Role " + all.getName());
								all.setPlayerListName(
										"§cunknown Role " + all.getName() + " §7| ID: §a" + igid(all) + " §f" + prefix);
							}
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String server_pf = "";
		if (p.hasPermission("mlps.colorChat")) {
			String msg = ChatColor.translateAlternateColorCodes('&', e.getMessage().replace("%", "%%"));
			e.setFormat(p.getDisplayName() + "§7 (" + server_pf + "§9" + igid(p) + "§7): " + msg);
		} else {
			e.setFormat(p.getDisplayName() + "§7 (" + server_pf + "§9" + igid(p) + "§7): " + e.getMessage().replace("%", "%%"));
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		setScoreboard(e.getPlayer());
		APIs api = new APIs();
		api.sendHotbarMessage(e.getPlayer(), api.returnStringReady(e.getPlayer(), "event.join.welcomemessage").replace("%server", api.getServerName()).replace("%displayer", e.getPlayer().getDisplayName()));
	}
	
	private String igid(Player p) {
		String s = "";
		try {
			PreparedStatement ps = MySQL.getConnection()
					.prepareStatement("SELECT * FROM redicore_userstats WHERE uuid = ?");
			ps.setString(1, p.getUniqueId().toString().replaceAll("-", ""));
			ResultSet rs = ps.executeQuery();
			rs.next();
			s = rs.getString("userid");
			rs.close();
			ps.close();
		} catch (SQLException e) {
		}
		return s;
	}

	private String retGroup(PermissionUser po) {
		String group = "";
		if (po.inGroup("pman")) {
			group = "§7» §aProject Manager";
		}else if(po.inGroup("apman")) {
			group = "§7» §aAssistant Project Manager";
		}else if(po.inGroup("adprman")) {
			group = "§7» §aAd & PR Manager";
		} else if (po.inGroup("sman")) {
			group = "§7» §aStaff Manager";
		} else if (po.inGroup("gmmman")) {
			group = "§7» §aGame Mod. Manager";
		} else if (po.inGroup("dev")) {
			group = "§7» §aDeveloper";
		} else if (po.inGroup("gman")) {
			group = "§7» §aGeneral Manager";
		} else if (po.inGroup("sda")) {
			group = "§7» §aService & Data Analyst";
		} else if (po.inGroup("cm")) {
			group = "§7» §aCommunity Moderator";
		} else if (po.inGroup("ct")) {
			group = "§7» §aContent Team";
		} else if (po.inGroup("st")) {
			group = "§7» §aSupport Team";
		} else if (po.inGroup("bd")) {
			group = "§7» §aBuilder";
		} else if (po.inGroup("gm")) {
			group = "§7» §aGame Moderator";
		} else if (po.inGroup("aot")) {
			group = "§7» §aAdd-On Team";
		} else if (po.inGroup("train")) {
			group = "§7» §aTrainee";
		} else if (po.inGroup("rltm")) {
			group = "§7» §aRetired Legend";
		} else if (po.inGroup("rtm")) {
			group = "§7» §aRetired Team Member";
		} else if (po.inGroup("part")) {
			group = "§7» §aPartner";
		} else if (po.inGroup("fs")) {
			group = "§7» §aForum Supporter";
		} else if (po.inGroup("nb")) {
			group = "§7» §aNitro Booster";
		} else if (po.inGroup("bt")) {
			group = "§7» §aBeta Tester";
		} else if (po.inGroup("friend")) {
			group = "§7» §aFriend";
		} else if (po.inGroup("vip")) {
			group = "§7» §aVIP";
		} else if (po.inGroup("default")) {
			group = "§7» §aPlayer";
		} else {
			group = "§7» §cunknown Role";
		}
		return group;
	}

	private String visualizeDouble(float value, String color) {
		String retVal = "";
		if (value <= 10) {
			retVal = color + "§e⬛§a⬜⬜⬜⬜⬜⬜⬜⬜⬜";
		} else if (value >= 11 && value <= 20) {
			retVal = color + "§c⬛§e⬛§a⬜⬜⬜⬜⬜⬜⬜⬜";
		} else if (value >= 21 && value <= 30) {
			retVal = color + "§c⬛⬛§e⬛§a⬜⬜⬜⬜⬜⬜⬜";
		} else if (value >= 31 && value <= 40) {
			retVal = color + "§c⬛⬛⬛§e⬛§a⬜⬜⬜⬜⬜⬜";
		} else if (value >= 41 && value <= 50) {
			retVal = color + "§c⬛⬛⬛⬛§e⬛§a⬜⬜⬜⬜⬜";
		} else if (value >= 51 && value <= 60) {
			retVal = color + "§c⬛⬛⬛⬛⬛§e⬛§a⬜⬜⬜⬜";
		} else if (value >= 61 && value <= 70) {
			retVal = color + "§c⬛⬛⬛⬛⬛⬛§e⬛§a⬜⬜⬜";
		} else if (value >= 71 && value <= 80) {
			retVal = color + "§c⬛⬛⬛⬛⬛⬛⬛§e⬛§a⬜⬜";
		} else if (value >= 81 && value <= 90) {
			retVal = color + "§c⬛⬛⬛⬛⬛⬛⬛⬛§e⬛§a⬜";
		} else if (value >= 91 && value <= 100) {
			retVal = color + "§c⬛⬛⬛⬛⬛⬛⬛⬛⬛§e⬛";
		} else {
			retVal = color + "§c⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛";
		}
		return retVal;
	}

	private String visualizeDouble(double value, String color) {
		String retVal = "";
		if (value <= 10) {
			retVal = color + "§e⬛§a⬜⬜⬜⬜⬜⬜⬜⬜⬜";
		} else if (value >= 11 && value <= 20) {
			retVal = color + "§c⬛§e⬛§a⬜⬜⬜⬜⬜⬜⬜⬜";
		} else if (value >= 21 && value <= 30) {
			retVal = color + "§c⬛⬛§e⬛§a⬜⬜⬜⬜⬜⬜⬜";
		} else if (value >= 31 && value <= 40) {
			retVal = color + "§c⬛⬛⬛§e⬛§a⬜⬜⬜⬜⬜⬜";
		} else if (value >= 41 && value <= 50) {
			retVal = color + "§c⬛⬛⬛⬛§e⬛§a⬜⬜⬜⬜⬜";
		} else if (value >= 51 && value <= 60) {
			retVal = color + "§c⬛⬛⬛⬛⬛§e⬛§a⬜⬜⬜⬜";
		} else if (value >= 61 && value <= 70) {
			retVal = color + "§c⬛⬛⬛⬛⬛⬛§e⬛§a⬜⬜⬜";
		} else if (value >= 71 && value <= 80) {
			retVal = color + "§c⬛⬛⬛⬛⬛⬛⬛§e⬛§a⬜⬜";
		} else if (value >= 81 && value <= 90) {
			retVal = color + "§c⬛⬛⬛⬛⬛⬛⬛⬛§e⬛§a⬜";
		} else if (value >= 91 && value <= 100) {
			retVal = color + "§c⬛⬛⬛⬛⬛⬛⬛⬛⬛§e⬛";
		} else {
			retVal = color + "§c⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛";
		}
		return retVal;
	}

	private static String igpre(Player p) {
		String prefix = "";
		try {
			PreparedStatement ps = MySQL.getConnection()
					.prepareStatement("SELECT * FROM redicore_userstats WHERE uuid = ?");
			ps.setString(1, p.getUniqueId().toString().replaceAll("-", ""));
			ResultSet rs = ps.executeQuery();
			rs.next();
			String pre = rs.getString("userprefix");
			if (pre.equalsIgnoreCase("RESET")) {
				prefix = "";
			} else {
				prefix = pre;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prefix;
	}

	private static boolean isAFK(Player p) {
		boolean boo = false;
		try {
			PreparedStatement ps = MySQL.getConnection()
					.prepareStatement("SELECT * FROM redicore_userstats WHERE uuid = ?");
			ps.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet rs = ps.executeQuery();
			rs.next();
			boo = rs.getBoolean("afk");
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return boo;
	}

	public Team getTeam(Scoreboard sb, String role) {
		Team team = sb.registerNewTeam(retPrefix(role, "team"));
		team.setPrefix(retPrefix(role, "prefix_tab"));
		return team;
	}

	private int getPlayers(String server) {
		int i = 0;
		PreparedStatement ps;
		try {
			ps = MySQL.getConnection().prepareStatement("SELECT * FROM redicore_serverstats WHERE servername = ?");
			ps.setString(1, server);
			ResultSet rs = ps.executeQuery();
			rs.next();
			i = rs.getInt("currPlayers");
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i;
	}

	private int getSB(Player p) {
		int i = 0;
		try {
			PreparedStatement ps = MySQL.getConnection()
					.prepareStatement("SELECT * FROM redicore_userstats WHERE uuid = ?");
			ps.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet rs = ps.executeQuery();
			rs.next();
			i = rs.getInt("scoreboard");
			ps.close();
			rs.close();
		} catch (SQLException e) {
		}
		return i;
	}

	private void setSB(Player p, int state) {
		try {
			PreparedStatement ps = MySQL.getConnection()
					.prepareStatement("UPDATE redicore_userstats SET scoreboard = ? WHERE uuid = ?");
			ps.setInt(1, state);
			ps.setString(2, p.getUniqueId().toString().replace("-", ""));
			ps.executeUpdate();
			ps.close();
			p.sendMessage(new APIs().getPrefix(PrefixType.MAIN)
					+ "Your Scoreboard-View has been changed back to the default view.\n§7Reason is, that §a%server §7hasn't Jobs available.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String retDate(String format) {
		SimpleDateFormat time = new SimpleDateFormat(format);
		return time.format(new Date());
	}

	private int retRepsToday() {
		int reports = 0;
		try {
			String date = retDate("yyyy.MM.dd");
			PreparedStatement ps = MySQL.getConnection()
					.prepareStatement("SELECT * FROM redicore_reports WHERE time_string = ?");
			ps.setString(1, date);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				reports++;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			reports = 9999;
		}
		return reports;
	}

	private int retRepstotal() {
		int reports = 0;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM redicore_reports");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				reports++;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			reports = 9999;
		}
		return reports;
	}

	private int retRepsTotCuSe() {
		int reports = 0;
		APIs api = new APIs();
		try {
			PreparedStatement ps = MySQL.getConnection()
					.prepareStatement("SELECT * FROM redicore_reports WHERE server = ?");
			ps.setString(1, api.getServerName());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				reports++;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			reports = 9999;
		}
		return reports;
	}

	private int retRepsTodCuSe() {
		int reports = 0;
		APIs api = new APIs();
		try {
			String csdate = retDate("yyyy.MM.dd");
			PreparedStatement ps = MySQL.getConnection()
					.prepareStatement("SELECT * FROM redicore_reports WHERE time_string = ? AND server = ?");
			ps.setString(1, csdate);
			ps.setString(2, api.getServerName());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				reports++;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			reports = 9999;
		}
		return reports;
	}

	private String getRadio(String type) {
		String s = "";
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM redifm_current WHERE id = ?");
			ps.setInt(1, 1);
			ResultSet rs = ps.executeQuery();
			rs.next();
			s = rs.getString(type);
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}

	private String retLatestReport(String type) {
		String report = "";
		String csdate = retDate("yyyy.MM.dd");
		try {
			PreparedStatement ps = MySQL.getConnection()
					.prepareStatement("SELECT * FROM redicore_reports ORDER BY id DESC");
			ResultSet rs = ps.executeQuery();
			rs.next();
			if (type.equalsIgnoreCase("reporter")) {
				if (rs.getString("time_string").equalsIgnoreCase(csdate)) {
					report = rs.getString("user_reporter");
				} else {
					report = "n/a";
				}
			} else if (type.equalsIgnoreCase("perpetrator")) {
				if (rs.getString("time_string").equalsIgnoreCase(csdate)) {
					report = rs.getString("user_target");
				} else {
					report = "n/a";
				}
			} else if (type.equalsIgnoreCase("reason")) {
				if (rs.getString("time_string").equalsIgnoreCase(csdate)) {
					if (rs.getString("reason").length() <= 15) {
						report = rs.getString("reason");
					} else {
						report = rs.getString("reason").substring(0, 16);
					}
				} else {
					report = "n/a";
				}
			} else if (type.equalsIgnoreCase("server")) {
				if (rs.getString("time_string").equalsIgnoreCase(csdate)) {
					report = rs.getString("server");
				} else {
					report = "n/a";
				}
			} else {
				report = "ERRORDEF1a";
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			report = "ERRORDEF2a";
			e.printStackTrace();
		}
		return report;
	}

	private String retPrefix(String rank, String type) {
		String prefix = "";
		if (type.equalsIgnoreCase("prefix_chat")) {
			prefix = chatHM.get(rank);
		} else if (type.equalsIgnoreCase("prefix_tab")) {
			prefix = tabHM.get(rank);
		} else if (type.equalsIgnoreCase("team")) {
			prefix = roleHM.get(rank);
		}
		String ret = net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', prefix);
		return ret;
	}

	public void downloadStrings() {
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM redicore_ranks");
			ResultSet rs = ps.executeQuery();
			tabHM.clear();
			chatHM.clear();
			roleHM.clear();
			while (rs.next()) {
				tabHM.put(rs.getString("rank"), rs.getString("alternativeColor") + rs.getString("prefix_tab"));
				chatHM.put(rs.getString("rank"), rs.getString("alternativeColor") + rs.getString("prefix_chat"));
				roleHM.put(rs.getString("rank"), rs.getString("team"));
			}
		} catch (SQLException e) {
		}
	}

	private void updateAFK(Player p, boolean boo) {
		try {
			PreparedStatement ps = MySQL.getConnection()
					.prepareStatement("UPDATE redicore_userstats SET afk = ? WHERE uuid = ?");
			ps.setBoolean(1, boo);
			ps.setString(2, p.getUniqueId().toString().replace("-", ""));
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private int translateBoolean(boolean bool) {
		if (bool) {
			return 1;
		} else {
			return 0;
		}
	}
	
	int cacheTimer = 21600;
	int cacheTimer_afk = 2; // 3600s equals to 60 minutes (60*60)
	int jobSignTimer = 5; // initial 2 secs, then all 60 secs to refresh to avoid overloading DB.
	public static HashMap<Player, Long> afk_timer = new HashMap<>();
	public static HashMap<Player, Boolean> staffKick = new HashMap<>();
	public static HashMap<Player, Boolean> autoAFK = new HashMap<>();
	public static HashMap<String, Integer> serverAFK = new HashMap<>();
	
	public void sbSched(int delay, int periodsb, int periodot) {
		Serverupdater su = new Serverupdater();
		new BukkitRunnable() {
			@Override
			public void run() {
				su.updateServer();
				sbmain++;
				if (sbmain == 10) {
					sbmain = 0;
				}
				for (Player all : Bukkit.getOnlinePlayers()) {
					try {
						setScoreboard(all);
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			}
		}.runTaskTimer(Main.instance, delay, periodsb);

		new BukkitRunnable() {

			@Override
			public void run() {
				su.clearlag();
			}
		}.runTaskTimer(Main.instance, delay, periodot);

		new BukkitRunnable() {
			@Override
			public void run() {
				APIs api = new APIs();
				su.Serverrestarter();
				cacheTimer--;
				if (cacheTimer == 21599) {
					api.loadConfig();
				} else if (cacheTimer == 0) {
					cacheTimer = 21600;
				}
				SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
				String stime = time.format(new Date());
				cacheTimer_afk--;
				if (cacheTimer_afk == 0) {
					cacheTimer_afk = 3600;
					try {
						PreparedStatement ps = MySQL.getConnection().prepareStatement(
								"SELECT autoafk_kick,autoafk_set,autoafk_autoset,autoafk_time FROM redicore_serverstats WHERE servername = ?");
						ps.setString(1, api.getServerName());
						ResultSet rs = ps.executeQuery();
						rs.next();
						serverAFK.put("auto", translateBoolean(rs.getBoolean("autoafk_autoset"))); // boolean (1 =
																									// autoset, 0 = no
																									// autoset)
						serverAFK.put("kick", translateBoolean(rs.getBoolean("autoafk_kick"))); // boolean (1 = kick, 0
																								// = no kick)
						serverAFK.put("set", rs.getInt("autoafk_set")); // time until the player will be marked as AFK
																		// (dependent on "auto")
						serverAFK.put("time", rs.getInt("autoafk_time")); // total time until the player will be kicked
																			// from the server (dependent on "kick")
						rs.close();
						ps.close();
						Bukkit.getConsoleSender()
								.sendMessage("Re-Cached the AutoAFK Settings for " + api.getServerName());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				jobSignTimer--;
				if (jobSignTimer == 0) {
					jobSignTimer = 30;
					JobSigns js = new JobSigns();
					js.onUpdateSigns();
				}
				for (Player all : Bukkit.getOnlinePlayers()) {
					APIs.sendTablist(all, api.returnStringReady(all, "scoreboard.playerlist.top").replace("|", "\n"), api.returnStringReady(all, "scoreboard.playerlist.bottom").replace("|", "\n").replace("%time", stime).replace("%servername", api.getServerName()).replace("%serverid", api.getServerId()));

					if (afk_timer.containsKey(all)) {
						long timeinsec = (System.currentTimeMillis() / 1000);
						long max_time;
						long set_time;
						boolean autoset = true;
						boolean autokick = true;
						if (serverAFK.containsKey("time")) {
							max_time = serverAFK.get("time");
						} else {
							max_time = 900;
						}
						if (serverAFK.containsKey("set")) {
							set_time = serverAFK.get("set");
						} else {
							set_time = 120;
						}
						if (serverAFK.containsKey("kick")) {
							int auto = serverAFK.get("kick");
							if (auto == 0) {
								autokick = false;
							} else if (auto == 1) {
								autokick = true;
							}
						}
						if (serverAFK.containsKey("auto")) {
							int auto = serverAFK.get("auto");
							if (auto == 0) {
								autoset = false;
							} else if (auto == 1) {
								autoset = true;
							}
						}
						long notif_time = (max_time - 60);
						long diff_time = (timeinsec - afk_timer.get(all));

						if (diff_time == notif_time) {
							if (!all.hasPermission("mlps.canBan")) {
								api.sendMSGReady(all, "event.autokick.info");
							} else {
								if (staffKick.containsKey(all)) {
									if (staffKick.get(all)) {
										api.sendMSGReady(all, "event.autokick.info");
									}
								}
							}
						}
						if (autoset) {
							if (diff_time == set_time) {
								api.sendMSGReady(all, "event.autokick.autoafk");
								updateAFK(all, true);
							}
						}
						if (autokick) {
							if (diff_time == max_time) {
								if (!all.hasPermission("mlps.isStaff") || !all.hasPermission("mlps.isTeam")) {
									all.kickPlayer("§aRedi§cCraft\n \n§7You've got kicked from our Server.\n§7Reason: "
											+ api.returnStringReady(all, "event.autokick.kick")
											+ "\n§7Issuer: §aServer");
								} else {
									if (staffKick.containsKey(all)) {
										if (staffKick.get(all)) {
											all.kickPlayer(
													"§aRedi§cCraft\n \n§7You've got kicked from our Server.\n§7Reason: "
															+ api.returnStringReady(all, "event.autokick.kick")
															+ "\n§7Issuer: §aServer");
										}
									}
								}
							}
						}
					}
				}
			}
		}.runTaskTimer(Main.instance, delay, periodot);
	}
}