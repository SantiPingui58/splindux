package com.santipingui58.spleef.managers;

import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.santipingui58.spleef.Main;

public class DataManager {

	
	  public static void updateName(Player p) {
		  if (Main.data.getConfig().contains("data." + p.getUniqueId())) {
			  
			  if (!Main.data.getConfig().getString("data." + p.getUniqueId() + ".name").equals(p.getName())) {
				  String oldName = Main.data.getConfig().getString("data." + p.getUniqueId() + ".name");
			  Main.data.getConfig().set("data." + p.getUniqueId() + ".name", p.getName());
			  
			  Set<String> data = Main.data.getConfig().getConfigurationSection("data").getKeys(false);
			  
			  for (String s : data) {
				  Set<String> friends = Main.data.getConfig().getConfigurationSection("data." + s  + ".friends").getKeys(false);
				  for (String f : friends) {
					  if (f.equalsIgnoreCase(oldName)) {
						  Main.data.getConfig().set("data." + s + ".friends." + f, null);
						  Main.data.getConfig().set("data." + s + ".friends", p.getName());
						  
					  }
				  }
			  }
			  
			  }
		  }
		  
	  }
	  

		public static String getLastConnection (OfflinePlayer p) {
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
			String time = Main.data.getConfig().getString("data."+p.getUniqueId()+".lastconnect");
			Date date;
			try {
				date = format.parse(time);
			} catch (ParseException e) {
			return "???";
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			
			Date ahora = new Date();
			Calendar now = Calendar.getInstance();
			now.setTime(ahora);
			
			int year = now.get(Calendar.YEAR)-calendar.get(Calendar.YEAR);
			int month = now.get(Calendar.MONTH)-calendar.get(Calendar.MONTH);
			int day = now.get(Calendar.DAY_OF_MONTH)-calendar.get(Calendar.DAY_OF_MONTH);
			int hours = now.get(Calendar.HOUR_OF_DAY)-calendar.get(Calendar.HOUR_OF_DAY);
			int minutes = now.get(Calendar.MINUTE)-calendar.get(Calendar.MINUTE);
			
			if (year==0) {
				if (month==0) {
					if (day==0) {
						if (hours==0) {
							if (minutes==0) {
								return "hace unos segundos.";
							} else {
								return "hace " + minutes + " minutos.";
							}
						} else {
							return "hace " + hours + " horas.";
						}
					} else {
						return "hace " + day + " dias.";
					}
				} else {
					return "hace " + month + " meses.";
				}
			} else {
				return "hace " + year + " a�os.";
			}
		  }
	  
	  
	public static void updateLastConnection(Player p) {
		  if (Main.data.getConfig().contains("data." + p.getUniqueId())) {
			  SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			    Date now = new Date();
			    String strDate = sdfDate.format(now);
			  Main.data.getConfig().set("data." + p.getUniqueId() + ".lastconnect", strDate);
			   Main.data.save();
			   
			   InetAddress ip = p.getAddress().getAddress();
			   Main.data.getConfig().set("data." + p.getUniqueId() + ".IP", ip.toString());
			   Main.data.save();
		  }
	  }
	  
	  
	  
	public static void onCreateData(Player p) {
		
		  if (!Main.data.getConfig().contains("data." + p.getUniqueId())) {
		
			  Main.data.getConfig().set("data." + p.getUniqueId() + ".name", p.getName());
			  Main.data.save();
			 updateLastConnection(p);
			   InetAddress ip = p.getAddress().getAddress();  
			  Main.data.getConfig().set("data." + p.getUniqueId() + ".IP",  ip.toString());
			  Main.data.save();
			  Main.data.getConfig().set("data." + p.getUniqueId()+ ".Ganadas", 0);
			  Main.data.save();
			  Main.data.getConfig().set("data." + p.getUniqueId()+ ".Perdidas", 0);
			  Main.data.save();
			  Main.data.getConfig().set("data." + p.getUniqueId()+ ".Jugadas", 0);
			  Main.data.save();
			  Main.data.getConfig().set("data." + p.getUniqueId()+ ".ELO", 1000);
			  Main.data.save();
			  Main.data.getConfig().set("data." + p.getUniqueId()+ ".rankeds", 10);
			  Main.data.save();
			  Main.data.getConfig().set("data." + p.getUniqueId()+ ".G/J", 0);
			  Main.data.save();
			  Main.data.getConfig().set("data." + p.getUniqueId()+ ".FFASpleefStats.Ganadas", 0);
			  Main.data.save();
			  Main.data.getConfig().set("data." + p.getUniqueId()+ ".FFASpleefStats.Jugadas", 0);
			  Main.data.save();
			  Main.data.getConfig().set("data." + p.getUniqueId()+ ".FFASpleefStats.G/J", 0);
			  Main.data.save();
			  Main.data.getConfig().set("data." + p.getUniqueId() + ".lang", "ESP");	  		
			  Main.data.save();
			  Main.data.getConfig().set("data." + p.getUniqueId() + ".nick", null);	  		
			  Main.data.save();
			  Main.data.getConfig().set("data." + p.getUniqueId() + ".level", 0);	  		
			  Main.data.save();
			  Main.data.getConfig().set("data." + p.getUniqueId() + ".spleefrank", "PrincipianteI");	  		
			  Main.data.save();
			  Main.data.getConfig().set("data." + p.getUniqueId() + ".friends", "");	  		
			  Main.data.save();
			  Main.data.getConfig().set("data." + p.getUniqueId() + ".nightvision", false);	  		  
			  
			  Main.data.save();
		  }
	  }
	  
	  
	  public static boolean hasTranslate(Player p) {
		  if (Main.data.getConfig().contains("data." + p.getUniqueId() + ".translate")) {
			  return true;
		  }
		  return false;
	  }
	  
	  public static void removeTranslate(Player p) {
		  if (Main.data.getConfig().contains("data." + p.getUniqueId() + ".translate")) {
			  Main.data.getConfig().set("data."+p.getUniqueId() + ".translate", null);
		  }
	  }
	  
	  public static String getTranslate(Player p) {
		  if (Main.data.getConfig().contains("data." + p.getUniqueId() + ".translate")) {
			  return Main.data.getConfig().getString("data." + p.getUniqueId() + ".translate");
		  }
		  return "ESP->ENG";
	  }
	  
	  public static void setTranslate(Player p,String lang) {
		  if (Main.data.getConfig().contains("data." + p.getUniqueId() + ".translate")) {
			  Main.data.getConfig().set("data."+p.getUniqueId() + ".translate", lang);
		  }
	  }
	  
	  	  
	  public static boolean getNightVision (Player p) {
		  if (Main.data.getConfig().contains("data." + p.getUniqueId() + ".nightvision")) {
			  return (Boolean) Main.data.getConfig().get("data." + p.getUniqueId() + ".nightvision");
		  }
		  return false;
	  }
	  
	  
	  public static void trueNightVision (Player p) {
		  if (Main.data.getConfig().contains("data." + p.getUniqueId())) {
			 Main.data.getConfig().set("data." + p.getUniqueId() + ".nightvision", true);
		  }
	  }
	  
	  public static void falseNightVision (Player p) {
		  if (Main.data.getConfig().contains("data." + p.getUniqueId())) {
			 Main.data.getConfig().set("data." + p.getUniqueId() + ".nightvision", false);
		  }
	  }
	  
	  
	  public static int getLevel(Player p) {
		  if (Main.data.getConfig().contains("data." + p.getUniqueId())) {
			  return Main.data.getConfig().getInt("data." + p.getUniqueId() + ".level");
		  }
		  return 0;
	  }
	  
	  
	  public static String getLang (Player p) {
		  if (Main.data.getConfig().contains("data."+ p.getUniqueId())) {
				 return  Main.data.getConfig().getString("data." + p.getUniqueId() + ".lang");		  
			  }
			  return "ESP";  
	  }
	  
	  public static String getNick (Player p) {
		  if (Main.data.getConfig().contains("data."+ p.getUniqueId())) {
				 return Main.data.getConfig().getString("data." + p.getUniqueId() + ".nick");		  
			  }
			  return null;  
	  }
	  
	  public static void deleteNick (OfflinePlayer p) {
		  if (Main.data.getConfig().contains("data."+ p.getUniqueId())) {
				  Main.data.getConfig().set("data." + p.getUniqueId() + ".nick", null);	
				  Main.data.save();
			  }
	  }
	  
	  public static void setNick (Player p, String nick) {
		  if (Main.data.getConfig().contains("data."+ p.getUniqueId())) {
				  Main.data.getConfig().set("data." + p.getUniqueId() + ".nick", nick);	
				  Main.data.save();
			  }
	  }
	  
	  public static int getFFASpleefWins(Player p) {
		  if (Main.data.getConfig().contains("data."+ p.getUniqueId())) {
			 return (Integer) Main.data.getConfig().get("data." + p.getUniqueId() + ".FFASpleefStats.Ganadas");		  
		  }
		  return 0;
	  }
	  
	  public static int getFFASpleefKD(Player p) {
		  if (Main.data.getConfig().contains("data."+ p.getUniqueId())) {
			 return (Integer) Main.data.getConfig().get("data." + p.getUniqueId() + ".FFASpleefStats.Ganadas");		  
		  }
		  return 0;
	  }
	  
	  public static void addFFASpleefWins(Player p) {
		  addMonthlyFFASpleefWins(p);
		  if (Main.data.getConfig().contains("data."+ p.getUniqueId())) {
			  int wins = getFFASpleefWins(p);
			  wins++;
			  Main.data.getConfig().set("data." + p.getUniqueId() + ".FFASpleefStats.Ganadas", wins);	
			  Main.data.save();
				  
			  }
	  }
	  
	  public static int getFFASpleefGames(Player p) {
		  if (Main.data.getConfig().contains("data."+ p.getUniqueId())) {
			 return (Integer) Main.data.getConfig().get("data." + p.getUniqueId() + ".FFASpleefStats.Jugadas");		  
		  }
		  return 0;
	  }
	  
	  
	  public static void addFFASpleefGames(Player p) {
		  if (Main.data.getConfig().contains("data."+ p.getUniqueId())) {
			  int games = getFFASpleefGames(p);
			  
			  games++;
			  Main.data.save();
			  			  
				  Main.data.getConfig().set("data." + p.getUniqueId() + ".FFASpleefStats.Jugadas", games);	
				  Main.data.save();
			  }
	  }
	  
	  
	  
	  public static int getElo(Player p) {
		  if (Main.data.getConfig().contains("data."+ p.getUniqueId())) {
			 return (Integer) Main.data.getConfig().get("data." + p.getUniqueId() + ".ELO");		  
		  }
		  return 1000;
	  }
	  
	  
	  public static int getGames(Player p) {
		  if (Main.data.getConfig().contains("data."+ p.getUniqueId())) {
			 return (Integer) Main.data.getConfig().get("data." + p.getUniqueId() + ".Jugadas");		  
		  }
		  return 0;
	  }
	  
	  public static int getLoses(Player p) {
		  if (Main.data.getConfig().contains("data."+ p.getUniqueId())) {
			 return (Integer) Main.data.getConfig().get("data." + p.getUniqueId() + ".Perdidas");		  
		  }
		  return 0;
	  }
	  
	  public static int getWins(Player p) {
		  if (Main.data.getConfig().contains("data."+ p.getUniqueId())) {
			 return (Integer) Main.data.getConfig().get("data." + p.getUniqueId() + ".Ganadas");		  
		  }
		  return 0;
	  }
	  
	  public static int getKD(Player p) {
		  if (Main.data.getConfig().contains("data."+ p.getUniqueId())) {
			 return (Integer) Main.data.getConfig().get("data." + p.getUniqueId() + ".G/J");		  
		  }
		  return 0;
	  }

	  public static int getRankeds(Player p) {
		  if (Main.data.getConfig().contains("data."+ p.getUniqueId())) {
			 return (Integer) Main.data.getConfig().get("data." + p.getUniqueId() + ".rankeds");		  
		  }
		  return 10;
	  }
	  
	  public static void playedRanked (Player p) {
		  if (Main.data.getConfig().contains("data." + p.getUniqueId())) {
			  int rankeds = DataManager.getRankeds(p);
			  if (rankeds>=1) {
			  rankeds--;
			  }
			  Main.data.getConfig().set("data." + p.getUniqueId() + ".rankeds", rankeds);
			  Main.data.save();
		  }
	  }
	  
	  public static void setRankeds(String p, int r) {
			  Main.data.getConfig().set(p, r);
			  Main.data.save();
		  
	  }
	  
	  public static void setRankeds(Player p, int r) {
		  	if (Main.data.getConfig().contains("data."+p.getUniqueId())) {
				  Main.data.getConfig().set("data." + p.getUniqueId() + ".rankeds", r);
				  Main.data.save();
			  }
	  }

	  public static double newElo(Player ganador, int difpuntos, Player perdedor) {
		  
		  int OldEloA = getElo(ganador); 
		  int OldEloB = getElo(perdedor); 
		  int C = 0;
		   
		double p1 = ((double) OldEloA/ (double) 600); //2
		double p1_ = Math.pow(10, p1); //100
		
		double p2 = ((double)OldEloB/(double)600); 
		double p2_ = Math.pow(10, p2);
		 
		double d = p1_ + p2_;
		
		double ex1 = (double)p1_/(double)d;
		double ex2 = (double)p2_/(double)d;
		
		
	  
		  if (difpuntos <= 1) {
			  C = 20;
		  } else if (difpuntos == 2) {
			  C = 30;
		  } else if (difpuntos == 3) {
			  C = 40;
		  } else if (difpuntos == 4) {
			  C = 50;
		  } else if (difpuntos >= 5) {
			  C = 60;
		  }
		    
		  double a1 = ((double)C*(1-(double)ex1));
		  double a2 = ((double)C*(0-(double)ex2));
		  
		  int newEloA = (int) (OldEloA + a1);
		  int newEloB = (int) (OldEloB + a2);
		
	  
		  Main.data.getConfig().set("data." + ganador.getUniqueId() + ".ELO", newEloA);
		  Main.data.getConfig().set("data." + perdedor.getUniqueId() + ".ELO", newEloB);
		  int ganA = (Integer) Main.data.getConfig().get("data." + ganador.getUniqueId() + ".Ganadas");
		  int jugA = (Integer) Main.data.getConfig().get("data." + ganador.getUniqueId() + ".Jugadas");
		  
		  int jugB = (Integer) Main.data.getConfig().get("data." + perdedor.getUniqueId() + ".Jugadas");
		  int perB = (Integer) Main.data.getConfig().get("data." + perdedor.getUniqueId() + ".Perdidas");
		  int ganB = (Integer) Main.data.getConfig().get("data." + perdedor.getUniqueId() + ".Ganadas");
		  
		  ganA++;
		  jugA++;
		  perB++;
		  jugB++;	
		  
		  double JAG = ganA;
		  double JAJ = jugA;
		  double JBG = ganB;
		  double JBJ = jugB;
		  
		  int GJA = (int) ((JAG/JAJ) * 100);
		  int GJB = (int) ((JBG/JBJ) * 100);

		  Main.data.getConfig().set("data." + ganador.getUniqueId() + ".Ganadas", ganA);
		  Main.data.getConfig().set("data." + ganador.getUniqueId() + ".Jugadas", jugA);
		  Main.data.getConfig().set("data." + perdedor.getUniqueId() + ".Jugadas", jugB);
		  Main.data.getConfig().set("data." + perdedor.getUniqueId() + ".Perdidas", perB);
		  
		  Main.data.getConfig().set("data." + ganador.getUniqueId() + ".G/J", GJA);
		  Main.data.getConfig().set("data." + perdedor.getUniqueId() + ".G/J", GJB);
		  
		  Main.data.save();
		  return (C*(1-ex1));
	  }
	  
	  
	  
	  
	  
	  
	
	
	public static String getName(String uuid) {
		if (Main.data.getConfig().contains("data."+uuid)) {
			return Main.data.getConfig().getString("data."+uuid+".name");
		}
		return "null";
	}
	
	
	
	public static void addMonthlyFFASpleefWins (Player p) {
		if (Main.data.getConfig().contains("ffa_monthly."+p.getUniqueId())) {
			int wins = Main.data.getConfig().getInt("ffa_monthly."+p.getUniqueId());
			wins++;
			Main.data.getConfig().set("ffa_monthly."+p.getUniqueId(), wins);
		
		} else {
			Main.data.getConfig().set("ffa_monthly."+p.getUniqueId(), 1);
		}
		Main.data.save();
	}
	
	
	public static int getMonthlyFFASpleefWins(Player p) {
		if (Main.data.getConfig().contains("ffa_monthly."+p.getUniqueId())) {
			return Main.data.getConfig().getInt("ffa_monthly."+p.getUniqueId());
		}
		
		return 0;
	}
	
	
	public static void resetMonthlyFFAWins() {
		Main.data.getConfig().set("ffa_monthly", null);
	}
	
	
	
	
	public static void addSC(String UUID,int coins) {
		Set<String> data = Main.data.getConfig().getConfigurationSection("data").getKeys(false);   
		
		for (String u : data) {
			if (Main.data.getConfig().getString(u).equalsIgnoreCase(UUID)) {
				int sc = 0;
				if (Main.data.getConfig().contains(UUID+".splinduxcoins")) {
					 sc = Main.data.getConfig().getInt(UUID+".splinduxcoins");
				}
				sc = sc + coins;
				Main.data.getConfig().set(UUID+".splinduxcoins", sc);
				Main.data.save();
			}
 		}
	} 
	
	public static void removeSC(String UUID,int coins) {
		Set<String> data = Main.data.getConfig().getConfigurationSection("data").getKeys(false);   
		
		for (String u : data) {
			if (Main.data.getConfig().getString(u).equalsIgnoreCase(UUID)) {
				int sc = 0;
				if (Main.data.getConfig().contains(UUID+".splinduxcoins")) {
					 sc = Main.data.getConfig().getInt(UUID+".splinduxcoins");
				}
				sc = sc - coins;
				Main.data.getConfig().set(UUID+".splinduxcoins", sc);
				Main.data.save();
			}
 		}
	} 
	
	
	
	
	
	
	
	
	
}
