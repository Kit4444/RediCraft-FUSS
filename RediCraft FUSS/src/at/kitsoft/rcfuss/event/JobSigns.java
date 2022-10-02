package at.kitsoft.rcfuss.event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import at.kitsoft.rcfuss.api.APIs;
import at.kitsoft.rcfuss.api.PrefixType;
import at.kitsoft.rcfuss.main.Main;
import at.kitsoft.rcfuss.mysql.lb.MySQL;

public class JobSigns implements Listener, CommandExecutor{
	
static HashMap<UUID, Boolean> delSign = new HashMap<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(Main.consolesend);
		}else {
			APIs api = new APIs();
			Player p = (Player) sender;
			if(p.hasPermission("mlps.deleteSign")) {
				delSign.put(p.getUniqueId(), true);
				p.sendMessage("Delete the sign you want to delete.");
			}else {
				api.noPerm(p);
			}
		}
		return false;
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Location blockLoc = e.getBlock().getLocation();
		if(isJobSign(blockLoc)) {
			APIs api = new APIs();
			if(delSign.containsKey(p.getUniqueId())) {
				if(delSign.get(p.getUniqueId())) {
					e.setCancelled(false);
					delSign.put(p.getUniqueId(), false);
					deleteJobSignFromDB(blockLoc);
					api.sendMSGReady(p, "event.jobSign.deletedSuccessfully");
				}else {
					e.setCancelled(true);
					api.sendMSGReady(p, "event.jobSign.notDeleteable");
				}
				e.setCancelled(true);
				api.sendMSGReady(p, "event.jobSign.notDeleteable");
			}
			e.setCancelled(true);
			api.sendMSGReady(p, "event.jobSign.notDeleteable");
		}
	}
	
	@EventHandler
	public void onJobSigns(SignChangeEvent e) {
		Player p = e.getPlayer();
		APIs api = new APIs();
		if(p.hasPermission("mlps.setJobSigns")) {
			if(e.getLine(0).equalsIgnoreCase("rcjob")) {
				Location loc = e.getBlock().getLocation();
				if(e.getLine(1).equalsIgnoreCase("toplist")) {
					if(e.getLine(2).equalsIgnoreCase("woodcutter") || e.getLine(2).equalsIgnoreCase("1")) {
						insertJobSignInDB(p, loc, "toplist", "woodcutter");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("miner") || e.getLine(2).equalsIgnoreCase("2")) {
						insertJobSignInDB(p, loc, "toplist", "miner");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("builder") || e.getLine(2).equalsIgnoreCase("3")) {
						insertJobSignInDB(p, loc, "toplist", "builder");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("digger") || e.getLine(2).equalsIgnoreCase("4")) {
						insertJobSignInDB(p, loc, "toplist", "digger");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("farmer") || e.getLine(2).equalsIgnoreCase("5")) {
						insertJobSignInDB(p, loc, "toplist", "farmer");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("hunter") || e.getLine(2).equalsIgnoreCase("6")) {
						insertJobSignInDB(p, loc, "toplist", "hunter");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("explorer") || e.getLine(2).equalsIgnoreCase("7")) {
						insertJobSignInDB(p, loc, "toplist", "explorer");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("crafter") || e.getLine(2).equalsIgnoreCase("8")) {
						insertJobSignInDB(p, loc, "toplist", "crafter");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("fisherman") || e.getLine(2).equalsIgnoreCase("9")) {
						insertJobSignInDB(p, loc, "toplist", "fisherman");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("weaponsmith") || e.getLine(2).equalsIgnoreCase("10")) {
						insertJobSignInDB(p, loc, "toplist", "weaponsmith");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("brewer") || e.getLine(2).equalsIgnoreCase("11")) {
						insertJobSignInDB(p, loc, "toplist", "brewer");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("enchanter") || e.getLine(2).equalsIgnoreCase("12")) {
						insertJobSignInDB(p, loc, "toplist", "enchanter");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}
				}else if(e.getLine(1).equalsIgnoreCase("top")) {
					if(e.getLine(2).equalsIgnoreCase("woodcutter") || e.getLine(2).equalsIgnoreCase("1")) {
						insertJobSignInDB(p, loc, "top", "woodcutter");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("miner") || e.getLine(2).equalsIgnoreCase("2")) {
						insertJobSignInDB(p, loc, "top", "miner");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("builder") || e.getLine(2).equalsIgnoreCase("3")) {
						insertJobSignInDB(p, loc, "top", "builder");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("digger") || e.getLine(2).equalsIgnoreCase("4")) {
						insertJobSignInDB(p, loc, "top", "digger");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("farmer") || e.getLine(2).equalsIgnoreCase("5")) {
						insertJobSignInDB(p, loc, "top", "farmer");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("hunter") || e.getLine(2).equalsIgnoreCase("6")) {
						insertJobSignInDB(p, loc, "top", "hunter");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("explorer") || e.getLine(2).equalsIgnoreCase("7")) {
						insertJobSignInDB(p, loc, "top", "explorer");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("crafter") || e.getLine(2).equalsIgnoreCase("8")) {
						insertJobSignInDB(p, loc, "top", "crafter");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("fisherman") || e.getLine(2).equalsIgnoreCase("9")) {
						insertJobSignInDB(p, loc, "top", "fisherman");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("weaponsmith") || e.getLine(2).equalsIgnoreCase("10")) {
						insertJobSignInDB(p, loc, "top", "weaponsmith");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("brewer") || e.getLine(2).equalsIgnoreCase("11")) {
						insertJobSignInDB(p, loc, "top", "brewer");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}else if(e.getLine(2).equalsIgnoreCase("enchanter") || e.getLine(2).equalsIgnoreCase("12")) {
						insertJobSignInDB(p, loc, "top", "enchanter");
						e.setLine(0, "");
						e.setLine(1, "JobSign");
						e.setLine(2, "has been set");
					}
				}else if(e.getLine(1).equalsIgnoreCase("join")) {
					if(e.getLine(2).equalsIgnoreCase("woodcutter") || e.getLine(2).equalsIgnoreCase("1")) {
						insertJobSignInDB(p, loc, "join", "woodcutter");
						e.setLine(0, api.getPrefix(PrefixType.MAIN));
						e.setLine(1, "§6Join");
						e.setLine(2, "§aWoodcutter");
					}else if(e.getLine(2).equalsIgnoreCase("miner") || e.getLine(2).equalsIgnoreCase("2")) {
						insertJobSignInDB(p, loc, "join", "miner");
						e.setLine(0, api.getPrefix(PrefixType.MAIN));
						e.setLine(1, "§6Join");
						e.setLine(2, "§aMiner");
					}else if(e.getLine(2).equalsIgnoreCase("builder") || e.getLine(2).equalsIgnoreCase("3")) {
						insertJobSignInDB(p, loc, "join", "builder");
						e.setLine(0, api.getPrefix(PrefixType.MAIN));
						e.setLine(1, "§6Join");
						e.setLine(2, "§aBuilder");
					}else if(e.getLine(2).equalsIgnoreCase("digger") || e.getLine(2).equalsIgnoreCase("4")) {
						insertJobSignInDB(p, loc, "join", "digger");
						e.setLine(0, api.getPrefix(PrefixType.MAIN));
						e.setLine(1, "§6Join");
						e.setLine(2, "§aDigger");
					}else if(e.getLine(2).equalsIgnoreCase("farmer") || e.getLine(2).equalsIgnoreCase("5")) {
						insertJobSignInDB(p, loc, "join", "farmer");
						e.setLine(0, api.getPrefix(PrefixType.MAIN));
						e.setLine(1, "§6Join");
						e.setLine(2, "§aFarmer");
					}else if(e.getLine(2).equalsIgnoreCase("hunter") || e.getLine(2).equalsIgnoreCase("6")) {
						insertJobSignInDB(p, loc, "join", "hunter");
						e.setLine(0, api.getPrefix(PrefixType.MAIN));
						e.setLine(1, "§6Join");
						e.setLine(2, "§aHunter");
					}else if(e.getLine(2).equalsIgnoreCase("explorer") || e.getLine(2).equalsIgnoreCase("7")) {
						insertJobSignInDB(p, loc, "join", "explorer");
						e.setLine(0, api.getPrefix(PrefixType.MAIN));
						e.setLine(1, "§6Join");
						e.setLine(2, "§aExplorer");
					}else if(e.getLine(2).equalsIgnoreCase("crafter") || e.getLine(2).equalsIgnoreCase("8")) {
						insertJobSignInDB(p, loc, "join", "crafter");
						e.setLine(0, api.getPrefix(PrefixType.MAIN));
						e.setLine(1, "§6Join");
						e.setLine(2, "§aCrafter");
					}else if(e.getLine(2).equalsIgnoreCase("fisherman") || e.getLine(2).equalsIgnoreCase("9")) {
						insertJobSignInDB(p, loc, "join", "fisherman");
						e.setLine(0, api.getPrefix(PrefixType.MAIN));
						e.setLine(1, "§6Join");
						e.setLine(2, "§aFisherman");
					}else if(e.getLine(2).equalsIgnoreCase("weaponsmith") || e.getLine(2).equalsIgnoreCase("10")) {
						insertJobSignInDB(p, loc, "join", "weaponsmith");
						e.setLine(0, api.getPrefix(PrefixType.MAIN));
						e.setLine(1, "§6Join");
						e.setLine(2, "§aWeaponsmith");
					}else if(e.getLine(2).equalsIgnoreCase("brewer") || e.getLine(2).equalsIgnoreCase("11")) {
						insertJobSignInDB(p, loc, "join", "brewer");
						e.setLine(0, api.getPrefix(PrefixType.MAIN));
						e.setLine(1, "§6Join");
						e.setLine(2, "§aBrewer");
					}else if(e.getLine(2).equalsIgnoreCase("enchanter") || e.getLine(2).equalsIgnoreCase("12")) {
						insertJobSignInDB(p, loc, "join", "enchanter");
						e.setLine(0, api.getPrefix(PrefixType.MAIN));
						e.setLine(1, "§6Join");
						e.setLine(2, "§aEnchanter");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Action a = e.getAction();
		if(e.getClickedBlock() == null) return;
		if(e.getClickedBlock().getType() == Material.SIGN_POST || e.getClickedBlock().getType() == Material.WALL_SIGN) {
			Player p = e.getPlayer();
			Sign s = (Sign) e.getClickedBlock().getState();
			String key = s.getLine(1);
			String job = s.getLine(2).substring(2);
			if(a == Action.LEFT_CLICK_BLOCK) {
				//join
				if(key.equalsIgnoreCase("§6Join")) {
					String todo = "join";
					switch(job) {
					case "Builder": p.performCommand("jobs " + todo + " " + job); break;
					case "Farmer": p.performCommand("jobs " + todo + " " + job); break;
					case "Hunter": p.performCommand("jobs " + todo + " " + job); break;
					case "Fisherman": p.performCommand("jobs " + todo + " " + job); break;
					case "Weaponsmith": p.performCommand("jobs " + todo + " " + job); break;
					case "Miner": p.performCommand("jobs " + todo + " " + job); break;
					case "Digger": p.performCommand("jobs " + todo + " " + job); break;
					case "Woodcutter": p.performCommand("jobs " + todo + " " + job); break;
					case "Crafter": p.performCommand("jobs " + todo + " " + job); break;
					case "Enchanter": p.performCommand("jobs " + todo + " " + job); break;
					case "Brewer": p.performCommand("jobs " + todo + " " + job); break;
					case "Explorer": p.performCommand("jobs " + todo + " " + job); break;
					default: Bukkit.getConsoleSender().sendMessage("Default hit! Key: " + key + ", Job: " + job); break;
					}
				}
			}else if(a == Action.RIGHT_CLICK_BLOCK) {
				//leave
				String todo = "leave";
				switch(job) {
				case "Builder": p.performCommand("jobs " + todo + " " + job); break;
				case "Farmer": p.performCommand("jobs " + todo + " " + job); break;
				case "Hunter": p.performCommand("jobs " + todo + " " + job); break;
				case "Fisherman": p.performCommand("jobs " + todo + " " + job); break;
				case "Weaponsmith": p.performCommand("jobs " + todo + " " + job); break;
				case "Miner": p.performCommand("jobs " + todo + " " + job); break;
				case "Digger": p.performCommand("jobs " + todo + " " + job); break;
				case "Woodcutter": p.performCommand("jobs " + todo + " " + job); break;
				case "Crafter": p.performCommand("jobs " + todo + " " + job); break;
				case "Enchanter": p.performCommand("jobs " + todo + " " + job); break;
				case "Brewer": p.performCommand("jobs " + todo + " " + job); break;
				case "Explorer": p.performCommand("jobs " + todo + " " + job); break;
				default: Bukkit.getConsoleSender().sendMessage("Default hit! Key: " + key + ", Job: " + job); break;
				}
			}
		}
	}
	
	/*
	 *  TOPLIST SIGN:
	 *  §a%name §6%level
	 *  for all 4 lines
	 *  
	 *  TOP SIGN
	 *  %prefix
	 *  %name
	 *  %job
	 *  %level
	 */

	public void onUpdateSigns() {
		APIs api = new APIs();
		if(Bukkit.getOnlinePlayers().size() != 0) {
			try {
				PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM redicore_jobsigns WHERE server = ?");
				ps.setString(1, api.getServerName());
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					String cmdKey = rs.getString("cmd_key");
					String jobKey = rs.getString("job");
					String world = rs.getString("world");
					int x = rs.getInt("x");
					int y = rs.getInt("y");
					int z = rs.getInt("z");
					Block bSign = Bukkit.getWorld(world).getBlockAt(x, y, z);
					if(bSign.getType() == Material.SIGN_POST || bSign.getType() == Material.WALL_SIGN) {
						Sign s = (Sign) bSign.getState();
						s.setLine(0, "");
						s.setLine(1, "");
						s.setLine(2, "");
						s.setLine(3, "");
						s.update();
						if(cmdKey.equalsIgnoreCase("toplist")) {
							int jobId = translateJobToInt(jobKey);
							HashMap<Integer, Integer> topJobs = getTopsFromJob(jobId);
							int contains = topJobs.size();
							if(contains <= 0) {
								s.setLine(0, "§a" + translateJobCorrect(jobKey));
								s.setLine(1, "§chas not any");
								s.setLine(2, "§cparticipants");
							}else if(contains >= 1) {
								s.setLine(0, "§a" + translateJobCorrect(jobKey));
								int i = 1;
								for(Map.Entry<Integer, Integer> entries : topJobs.entrySet()) {
									s.setLine(i, "§a" + getUserFromID(entries.getKey()) + "§7: §6" + entries.getValue());
									i++;
									if(i >= 4) {
										break;
									}
								}
							}
							s.update();
						}else if(cmdKey.equalsIgnoreCase("top")){
							int jobId = translateJobToInt(jobKey);
							HashMap<Integer, Integer> topJob = getTopFromJob(jobId);
							if(topJob != null) {
								s.setLine(0, api.getPrefix(PrefixType.SCOREBOARD));
								for(Map.Entry<Integer, Integer> entries : topJob.entrySet()) {
									s.setLine(1, "§6" + getUserFromID(entries.getKey()));
									s.setLine(3, "§7Level: §a" + entries.getValue());
								}
								s.setLine(2, "§a" + translateJobCorrect(jobKey));
							}else {
								s.setLine(0, "§a" + translateJobCorrect(jobKey));
								s.setLine(1, "§chas not any");
								s.setLine(2, "§cparticipants");
							}
							s.update();
						}else if(cmdKey.equalsIgnoreCase("join")) {
							int jobId = translateJobToInt(jobKey);
							s.setLine(0, api.getPrefix(PrefixType.SCOREBOARD));
							s.setLine(1, "§6Join");
							s.setLine(2, "§a" + translateJobCorrect(jobKey));
							s.setLine(3, "§7Members: §a" + getParticipantsPerJob(jobId));
							s.update();
						}
					}
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	private String getUserFromID(int id) {
		String pName = "Steve";
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT username FROM jobs_users WHERE id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				pName = rs.getString("username");
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pName;
	}
	
	//returns a HashMap (Integer, UserID - Integer, Level
	private HashMap<Integer, Integer> getTopsFromJob(int jobId){
		HashMap<Integer, Integer> mainHM = new HashMap<>();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT level,userid FROM jobs_jobs WHERE jobid = ? ORDER BY experience DESC");
			ps.setInt(1, jobId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				mainHM.put(rs.getInt("userid"), rs.getInt("level"));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mainHM;
	}
	
	//returns a HashMap (Integer, UserID - Integer, Level
	private HashMap<Integer, Integer> getTopFromJob(int jobId){
		HashMap<Integer, Integer> mainHM = new HashMap<>();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT level,userid FROM jobs_jobs WHERE jobid = ? ORDER BY experience DESC");
			ps.setInt(1, jobId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				mainHM.put(rs.getInt("userid"), rs.getInt("level"));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mainHM;
	}
	
	private int getParticipantsPerJob(int jobId) {
		int participants = 0;
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM jobs_jobs WHERE jobid = ?");
			ps.setInt(1, jobId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				participants++;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return participants;
	}
	
	private int translateJobToInt(String job) {
		int jobId = 0;
		switch(job.toLowerCase()) {
		case "woodcutter": jobId = 1; break;
		case "miner": jobId = 2; break;
		case "builder": jobId = 3; break;
		case "digger": jobId = 4; break;
		case "farmer": jobId = 5; break;
		case "hunter": jobId = 6; break;
		case "explorer": jobId = 7; break;
		case "crafter": jobId = 8; break;
		case "fisherman": jobId = 9; break;
		case "weaponsmith": jobId = 10; break;
		case "brewer": jobId = 11; break;
		case "enchanter": jobId = 12; break;
		}
		return jobId;
	}
	
	@SuppressWarnings("unused")
	private String translateJobToString(int job) {
		String jobname = "";
		switch(job) {
		case 1: jobname = "Woodcutter"; break;
		case 2: jobname = "Miner"; break;
		case 3: jobname = "Builder"; break;
		case 4: jobname = "Digger"; break;
		case 5: jobname = "Farmer"; break;
		case 6: jobname = "Hunter"; break;
		case 7: jobname = "Explorer"; break;
		case 8: jobname = "Crafter"; break;
		case 9: jobname = "Fisherman"; break;
		case 10: jobname = "Weaponsmith"; break;
		case 11: jobname = "Brewer"; break;
		case 12: jobname = "Enchanter"; break;
		}
		return jobname;
	}
	
	private String translateJobCorrect(String job) {
		String jobname = "";
		switch(job) {
		case "woodcutter": jobname = "Woodcutter"; break;
		case "miner": jobname = "Miner"; break;
		case "builder": jobname = "Builder"; break;
		case "digger": jobname = "Digger"; break;
		case "farmer": jobname = "Farmer"; break;
		case "hunter": jobname = "Hunter"; break;
		case "explorer": jobname = "Explorer"; break;
		case "crafter": jobname = "Crafter"; break;
		case "fisherman": jobname = "Fisherman"; break;
		case "weaponsmith": jobname = "Weaponsmith"; break;
		case "brewer": jobname = "Brewer"; break;
		case "enchanter": jobname = "Enchanter"; break;
		}
		return jobname;
	}
	
	//needs an instance of the Player (Signsetter), Location (Signloc), String cmdkey (toplist, top1) and job
	private void insertJobSignInDB(Player p, Location loc, String cmdkey, String job) {
		SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
		Date currentDate = new Date();
		APIs api = new APIs();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("INSERT INTO redicore_jobsigns(server, world, x, y, z, cmd_key, job, date, time, p_uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, api.getServerName());
			ps.setString(2, loc.getWorld().getName());
			ps.setInt(3, loc.getBlockX());
			ps.setInt(4, loc.getBlockY());
			ps.setInt(5, loc.getBlockZ());
			ps.setString(6, cmdkey);
			ps.setString(7, job);
			ps.setString(8, date.format(currentDate));
			ps.setString(9, time.format(currentDate));
			ps.setString(10, p.getUniqueId().toString());
			ps.executeUpdate();
			ps.close();
			p.sendMessage(api.getPrefix(PrefixType.MAIN) + api.returnStringReady(p, "event.jobSign.createdSign").replace("%key", cmdkey).replace("%job", translateJobCorrect(job)));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//needs the Location of the sign.
	private boolean isJobSign(Location loc) {
		boolean isJobSign = false;
		APIs api = new APIs();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("SELECT * FROM redicore_jobsigns WHERE server = ? AND world = ? AND x = ? AND y = ? AND z = ?");
			ps.setString(1, api.getServerName());
			ps.setString(2, loc.getWorld().getName());
			ps.setInt(3, loc.getBlockX());
			ps.setInt(4, loc.getBlockY());
			ps.setInt(5, loc.getBlockZ());
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				isJobSign = true;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isJobSign;
	}
	
	private void deleteJobSignFromDB(Location loc) {
		APIs api = new APIs();
		try {
			PreparedStatement ps = MySQL.getConnection().prepareStatement("DELETE FROM redicore_jobsigns WHERE server = ? AND world = ? AND x = ? AND y = ? AND z = ?");
			ps.setString(1, api.getServerName());
			ps.setString(2, loc.getWorld().getName());
			ps.setInt(3, loc.getBlockX());
			ps.setInt(4, loc.getBlockY());
			ps.setInt(5, loc.getBlockZ());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}