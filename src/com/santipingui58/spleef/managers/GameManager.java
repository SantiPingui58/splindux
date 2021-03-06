package com.santipingui58.spleef.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.santipingui58.spleef.Main;
import com.santipingui58.spleef.game.BowSpleefGame;
import com.santipingui58.spleef.game.BuildSpleefPvPGame;
import com.santipingui58.spleef.game.FFASpleefGame;
import com.santipingui58.spleef.game.Game;
import com.santipingui58.spleef.game.RankedSpleefGame;
import com.santipingui58.spleef.game.Spleef2v2Game;
import com.santipingui58.spleef.game.SpleefGame;





public class GameManager {
	
	
	
	private static GameManager am;

    
    private final List<Game> arenas = new ArrayList<Game>();
    private final List<Game> arenasingame = new ArrayList<Game>();
    private final List<Game> rankedgames = new ArrayList<Game>();
    public static HashMap<Player,String> gamemenu = new HashMap<Player,String>();
    
    private GameManager() {}
    
    
    public static GameManager getManager() {
        if (am == null)
            am = new GameManager();

        return am;
    }
    
    public Game getArena(String s){
        for (Game a : this.arenas) {
            if (a.getId() == s) {
                return a;
            }
        }

        return null; // Not found
    }
    
    
    public List<Game> getRankedGames() {
    	return this.rankedgames;
    }
    
    public boolean isRanked(Game g) {
    	if (this.rankedgames.contains(g)) {
    		return true;
    	}
    	return false;
    }
    
    public boolean is2vs2Spleef (Game g) {
    	if (g.getPlayer1().size() == 2 || g.getPlayer2().size() == 2) {
    		return true;
    	}
    	return false;
    }
    
    public Game getArenabyPlayer (Player p) {
    	for (Game a : this.arenas) {
    		if (a.getType().equalsIgnoreCase("spleef") || a.getType().equalsIgnoreCase("BuildSpleefPvP")
    				|| a.getType().equalsIgnoreCase("bowspleef")) {
    		if (a.getPlayer1().contains(p) || a.getPlayer2().contains(p) || a.getSpectators().contains(p)) {
    			return a;
    		}
    		} else if (a.getType().equalsIgnoreCase("spleef2v2")) {
        		if (a.getPlayer1().contains(p) || a.getPlayer2().contains(p) || a.getSpectators().contains(p) || a.getInGameSpect().contains(p)) {
        			return a;
        		}
        		} else if (a.getType().equalsIgnoreCase("ffaspleef")) {
    			if (a.getPlayers().contains(p) || a.getSpectators().contains(p)) {
    				return a;
    			}
    		}
    	}
    	return null;
    }
    
   
    public boolean isInTemp(Player p) {
    	for (Game g : this.arenas) {
    		if (g.getTempPlayer1_2v2().contains(p)) {
    			return true;
    		} else if (g.getTempPlayer2_2v2().contains(p)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    
    public Game getFFAQueue (Player p) {
    	for (Game a : this.arenas) {
    		if (a.getType().equalsIgnoreCase("ffaspleef")) {
    		 if (a.getQueue().contains(p)) {
    			return a;
    		 }
    		 }
    	}
    	return null;
    }
    
    
 
    public Game loadArena(Location spawn1, Location spawn2,Location arena1, Location arena2,Location death1,Location death2, Location spect, String id, String tipo) {       
    	 Game a;
    	if (tipo.equalsIgnoreCase("BuildSpleefPvP")) {
    		a= new Game(spawn1, spawn2, arena1, arena2,death1,death2,spect, id, tipo);
    	} else {
    		  a= new Game(spawn1, spawn2, arena1, arena2,null,null,spect, id, tipo);
    	}
      
        this.arenas.add(a);
        

        return a;
    }
    
    
    
    public void loadArenas() { 
    	int arenasint = 0;
    	Set<String> arenas = Main.arena.getConfig().getConfigurationSection("arenas").getKeys(false);
    		for (String b : arenas) {		
    			Location spawn1 = Main.getLoc(Main.arena.getConfig().getString("arenas." + b + ".spawn1"), true);
				Location spawn2 = Main.getLoc(Main.arena.getConfig().getString("arenas." + b + ".spawn2"), true);
				Location arena1 = Main.getLoc(Main.arena.getConfig().getString("arenas." + b + ".arena1"), true);
				Location arena2 = Main.getLoc(Main.arena.getConfig().getString("arenas." + b + ".arena2"), true);
				
				
				Location spect = Main.getLoc(Main.arena.getConfig().getString("arenas." +b + ".spect"), true);
				String tipo = Main.arena.getConfig().getString("arenas." +b + ".type");
				if (tipo.equalsIgnoreCase("BuildSpleefPvP")) {
					Location death1 = Main.getLoc(Main.arena.getConfig().getString("arenas." + b + ".death1"), true);
					Location death2 = Main.getLoc(Main.arena.getConfig().getString("arenas." + b + ".death2"), true);
					GameManager.getManager().loadArena(spawn1, spawn2, arena1, arena2,death1,death2, spect, b, tipo);
				} else {
					GameManager.getManager().loadArena(spawn1, spawn2, arena1, arena2,null,null, spect, b, tipo);
				}
				arenasint++;
				
    		}
    		
    		Main.get().getLogger().info(arenasint+ " arenas cargadas!");
    }
    
    
    
    public void Countdown(final Game g) {
    		g.falseCanPlay();
    		reinicio(g);
    		if (g.getType().equalsIgnoreCase("spleef") || g.getType().equalsIgnoreCase("BuildSpleefPvP")
    				|| g.getType().equalsIgnoreCase("bowspleef")) {
    			if (g.getType().equalsIgnoreCase("bowspleef")) {
    				for (Player p1 : g.getPlayer1()) {
    					p1.getInventory().removeItem(new ItemStack(Material.ARROW));
    				}
    				for (Player p2 : g.getPlayer2()) {
    					p2.getInventory().removeItem(new ItemStack(Material.ARROW));
    				}
    			}
    			
    			CapsuleManager.generateCapsula(g.getPlayer1().get(0), g.getSpawn1());
    			CapsuleManager.generateCapsula(g.getPlayer2().get(0), g.getSpawn2());
    		} else if (g.getType().equalsIgnoreCase("spleef2v2")){
    			try {
    			CapsuleManager.generateCapsula(g.getPlayer1().get(0), g.getSpawn1A());
    			} catch (Exception ex) {}
    			try {
    			CapsuleManager.generateCapsula(g.getPlayer1().get(1), g.getSpawn1B());
    			} catch (Exception ex) {}
    			try {
    			CapsuleManager.generateCapsula(g.getPlayer2().get(0), g.getSpawn2A());
    			} catch (Exception ex) {}
    			try {
    			CapsuleManager.generateCapsula(g.getPlayer2().get(1), g.getSpawn2B());
    			} catch (Exception ex) {}
    			
    			
    			}
    		}
    		      

    	
   
    
    
    public void reinicio(Game g) {
    	
    	 Location a = g.getArena1();
		  Location b =g.getArena2();
		  int ax = a.getBlockX();
		  int az = a.getBlockZ();
		  
		
		  
		  int bx = b.getBlockX();
		  int bz = b.getBlockZ();
    	
    		if (g.getType().equalsIgnoreCase("BuildSpleefPvP")) {
    			g.cleanPlacedBlocks();
    			List<Location> list = new ArrayList<Location>();
    			  Set<String> set = Main.arena.getConfig().getConfigurationSection(g.getId()+".arenablocks").getKeys(false);   
    			  for (String f : set) {
    				  list.add(Main.getLoc(f));
    			  }
    			  
    			int ay = a.getBlockY();
    			int by = b.getBlockY();

    					if (ay==by) {
    						 for (int x = ax; x < bx; x++) {
    		        			  for (int z = az; z < bz; z++) {		 
    		        				  Location aire = new Location (a.getWorld(), x, a.getBlockY(), z);
    		        				  for (Location l :list ) {
    		        					  if (l==aire) {
    		        						  aire.getBlock().setType(Material.SNOW_BLOCK);
    		        					  }
    		        				  }
    		        				  }
    		        		  }	
    					} else {
    			 for (int x = ax; x < bx; x++) {
    				 for (int y = ay; y < by; y++) {
        			  for (int z = az; z < bz; z++) {		 
        				  Location aire = new Location (a.getWorld(), x, y, z);
        				  for (Location l : list) {
        					  if (l==aire) {
        						  aire.getBlock().setType(Material.SNOW_BLOCK);
        					  }
        				  }
        			  }
        				  }
        		  }	
    			 }
    			
    			
    		} else {
    			  int y = a.getBlockY();	  
            	   for (int x = ax; x < bx; x++) {
         			  for (int z = az; z < bz; z++) {
         				  Location aire = new Location (a.getWorld(), x, y, z);
         				  if (aire.getBlock().getType().equals(Material.AIR)) {
         					  if (g.getType().equalsIgnoreCase("spleef") || g.getType().equalsIgnoreCase("spleef2v2") ||
         							 g.getType().equalsIgnoreCase("ffaspleef")) {
         				  aire.getBlock().setType(Material.SNOW_BLOCK); 
         				  } else if (g.getType().equalsIgnoreCase("bowspleef")) {
         					  aire.getBlock().setType(Material.TNT); 
         				  }
         				  }
         			  }
         		  }
               
           
    		} 
	  }
    
    
    
    public void cristalquitar (Location l1, Location l2) {
		  int x1 =  l1.getBlockX();
			 int y1 = l1.getBlockY();
			 int z1 = l1.getBlockZ();
			 
			 int x2 =  l2.getBlockX();
			 int y2 = l2.getBlockY();
			 int z2 = l2.getBlockZ();
			 
			 int x1_1 = x1 +1;
			 int x1_2 = x1 -1;
			 
			 int x2_1 = x2 +1;
			 int x2_2 = x2 -1;
			 
			 int z1_1 = z1+1;
			 int z1_2 = z1-1;
			 
			 int z2_1 = z2+1;
			 int z2_2 = z2-1;
			 
			 Location vidrio1_1 = new Location (l1.getWorld(), x1_1, y1,z1);
			 Location vidrio1_2 = new Location (l1.getWorld(), x1_1, y1 + 1,z1);
			 
			 Location vidrio1_3 = new Location (l1.getWorld(), x1_2, y1,z1);
			 Location vidrio1_4 = new Location (l1.getWorld(), x1_2, y1+1,z1);
			 
			 Location vidrio1_5 = new Location (l1.getWorld(), x1, y1, z1_1);
			 Location vidrio1_6 = new Location (l1.getWorld(), x1, y1+1, z1_1);
			 
			 
			 Location vidrio1_7 = new Location (l2.getWorld(), x1, y1, z1_2);
			 Location vidrio1_8 = new Location (l2.getWorld(), x1, y1+1, z1_2);
			 Location vidrio1_9 = new Location (l2.getWorld(), x1,y1+2,z1);
			 
			 
			 
			 Location vidrio2_1 = new Location (l2.getWorld(), x2_1, y2,z2);
			 Location vidrio2_2 = new Location (l2.getWorld(), x2_1, y2 + 1,z2);
			 Location vidrio2_3 = new Location (l2.getWorld(), x2_2, y2,z2);
			 Location vidrio2_4 = new Location (l2.getWorld(), x2_2, y2+1,z2);
			 Location vidrio2_5 = new Location (l2.getWorld(), x2, y2, z2_1);
			 Location vidrio2_6 = new Location (l2.getWorld(), x2, y2+1, z2_1);
			 Location vidrio2_7 = new Location (l2.getWorld(), x2, y2, z2_2);
			 Location vidrio2_8 = new Location (l2.getWorld(), x2, y2+1, z2_2);
			 Location vidrio2_9 = new Location (l2.getWorld(), x2,y2+2,z2);
			 
			 
			 vidrio1_1.getBlock().setType(Material.AIR);
			 vidrio1_2.getBlock().setType(Material.AIR);
			 vidrio1_3.getBlock().setType(Material.AIR);
			 vidrio1_4.getBlock().setType(Material.AIR);
			 vidrio1_5.getBlock().setType(Material.AIR);
			 vidrio1_6.getBlock().setType(Material.AIR);
			 vidrio1_7.getBlock().setType(Material.AIR);
			 vidrio1_8.getBlock().setType(Material.AIR);
			 vidrio1_9.getBlock().setType(Material.AIR);
			 
			 vidrio2_1.getBlock().setType(Material.AIR);
			 vidrio2_2.getBlock().setType(Material.AIR);
			 vidrio2_3.getBlock().setType(Material.AIR);
			 vidrio2_4.getBlock().setType(Material.AIR);
			 vidrio2_5.getBlock().setType(Material.AIR);
			 vidrio2_6.getBlock().setType(Material.AIR);
			 vidrio2_7.getBlock().setType(Material.AIR);
			 vidrio2_8.getBlock().setType(Material.AIR);
			 vidrio2_9.getBlock().setType(Material.AIR);
			 
			 
		  }
    
    
    public  void cristalcolocar(Location l1, Location l2) {
		 int x1 =  l1.getBlockX();
		 int y1 = l1.getBlockY();
		 int z1 = l1.getBlockZ();
		 
		 int x2 =  l2.getBlockX();
		 int y2 = l2.getBlockY();
		 int z2 = l2.getBlockZ();
		 
		 int x1_1 = x1 +1;
		 int x1_2 = x1 -1;
		 
		 int x2_1 = x2 +1;
		 int x2_2 = x2 -1;
		 
		 int z1_1 = z1+1;
		 int z1_2 = z1-1;
		 
		 int z2_1 = z2+1;
		 int z2_2 = z2-1;
		 
		 Location vidrio1_1 = new Location (l1.getWorld(), x1_1, y1,z1);
		 Location vidrio1_2 = new Location (l1.getWorld(), x1_1, y1 + 1,z1);	 
		 Location vidrio1_3 = new Location (l1.getWorld(), x1_2, y1,z1);
		 Location vidrio1_4 = new Location (l1.getWorld(), x1_2, y1+1,z1);	 
		 Location vidrio1_5 = new Location (l1.getWorld(), x1, y1, z1_1);
		 Location vidrio1_6 = new Location (l1.getWorld(), x1, y1+1, z1_1);	 
		 Location vidrio1_7 = new Location (l2.getWorld(), x1, y1, z1_2);
		 Location vidrio1_8 = new Location (l2.getWorld(), x1, y1+1, z1_2);
		 Location vidrio1_9 = new Location (l2.getWorld(), x1,y1+2,z1);
		 
		 
		 Location vidrio2_1 = new Location (l2.getWorld(), x2_1, y2,z2);
		 Location vidrio2_2 = new Location (l2.getWorld(), x2_1, y2 + 1,z2);
		 Location vidrio2_3 = new Location (l2.getWorld(), x2_2, y2,z2);
		 Location vidrio2_4 = new Location (l2.getWorld(), x2_2, y2+1,z2);
		 Location vidrio2_5 = new Location (l2.getWorld(), x2, y2, z2_1);
		 Location vidrio2_6 = new Location (l2.getWorld(), x2, y2+1, z2_1);
		 Location vidrio2_7 = new Location (l2.getWorld(), x2, y2, z2_2);
		 Location vidrio2_8 = new Location (l2.getWorld(), x2, y2+1, z2_2);
		 Location vidrio2_9 = new Location (l2.getWorld(), x2,y2+2,z2);
		 
		 
		 
		 
		 vidrio1_1.getBlock().setType(Material.GLASS);
		 vidrio1_2.getBlock().setType(Material.GLASS);
		 vidrio1_3.getBlock().setType(Material.GLASS);
		 vidrio1_4.getBlock().setType(Material.GLASS);
		 vidrio1_5.getBlock().setType(Material.GLASS);
		 vidrio1_6.getBlock().setType(Material.GLASS);
		 vidrio1_7.getBlock().setType(Material.GLASS);
		 vidrio1_8.getBlock().setType(Material.GLASS);
		 vidrio1_9.getBlock().setType(Material.GLASS);
		 
		 vidrio2_1.getBlock().setType(Material.GLASS);
		 vidrio2_2.getBlock().setType(Material.GLASS);
		 vidrio2_3.getBlock().setType(Material.GLASS);
		 vidrio2_4.getBlock().setType(Material.GLASS);
		 vidrio2_5.getBlock().setType(Material.GLASS);
		 vidrio2_6.getBlock().setType(Material.GLASS);
		 vidrio2_7.getBlock().setType(Material.GLASS);
		 vidrio2_8.getBlock().setType(Material.GLASS);
		 vidrio2_9.getBlock().setType(Material.GLASS);
		 
		 
	  }
	  
    public void cristalquitar2v2 (Location l1, Location l2, Location l3, Location l4) {
    		int x1 =  l1.getBlockX() + 2;
			 int y1 = l1.getBlockY();
			 int z1 = l1.getBlockZ();
			 
			 int x2 =  l2.getBlockX() - 2;
			 int y2 = l2.getBlockY();
			 int z2 = l2.getBlockZ();
			 
			 
			 int x3 =  l3.getBlockX() + 2;
			 int y3 = l3.getBlockY();
			 int z3 = l3.getBlockZ();
			 
			 int x4 =  l4.getBlockX() - 2;
			 int y4 = l4.getBlockY();
			 int z4 = l4.getBlockZ();
			 
			 int x1_1 = x1 +1;
			 int x1_2 = x1 -1;
			 
			 int x2_1 = x2 +1;
			 int x2_2 = x2 -1;
			 
			 int z1_1 = z1+1;
			 int z1_2 = z1-1;
			 
			 int z2_1 = z2+1;
			 int z2_2 = z2-1;
			 
			 int x3_1 = x1 +1;
			 int x3_2 = x1 -1;
			 
			 int x4_1 = x2 +1;
			 int x4_2 = x2 -1;
			 
			 int z3_1 = z1+1;
			 int z3_2 = z1-1;
			 
			 int z4_1 = z2+1;
			 int z4_2 = z2-1;
			 
			 
			 
			 Location vidrio1_1 = new Location (l1.getWorld(), x1_1, y1,z1);
			 Location vidrio1_2 = new Location (l1.getWorld(), x1_1, y1 + 1,z1);		 
			 Location vidrio1_3 = new Location (l1.getWorld(), x1_2, y1,z1);
			 Location vidrio1_4 = new Location (l1.getWorld(), x1_2, y1+1,z1);		 
			 Location vidrio1_5 = new Location (l1.getWorld(), x1, y1, z1_1);
			 Location vidrio1_6 = new Location (l1.getWorld(), x1, y1+1, z1_1);
			 Location vidrio1_7 = new Location (l2.getWorld(), x1, y1, z1_2);
			 Location vidrio1_8 = new Location (l2.getWorld(), x1, y1+1, z1_2);
			 Location vidrio1_9 = new Location (l2.getWorld(), x1,y1+2,z1);

			 Location vidrio2_1 = new Location (l2.getWorld(), x2_1, y2,z2);
			 Location vidrio2_2 = new Location (l2.getWorld(), x2_1, y2 + 1,z2);
			 Location vidrio2_3 = new Location (l2.getWorld(), x2_2, y2,z2);
			 Location vidrio2_4 = new Location (l2.getWorld(), x2_2, y2+1,z2);
			 Location vidrio2_5 = new Location (l2.getWorld(), x2, y2, z2_1);
			 Location vidrio2_6 = new Location (l2.getWorld(), x2, y2+1, z2_1);
			 Location vidrio2_7 = new Location (l2.getWorld(), x2, y2, z2_2);
			 Location vidrio2_8 = new Location (l2.getWorld(), x2, y2+1, z2_2);
			 Location vidrio2_9 = new Location (l2.getWorld(), x2,y2+2,z2);
			 
			 Location vidrio3_1 = new Location (l3.getWorld(), x3_1, y3,z3);
			 Location vidrio3_2 = new Location (l3.getWorld(), x3_1, y3 + 1,z3);
			 Location vidrio3_3 = new Location (l3.getWorld(), x3_2, y3,z3);
			 Location vidrio3_4 = new Location (l3.getWorld(), x3_2, y3+1,z3);
			 Location vidrio3_5 = new Location (l3.getWorld(), x3, y3, z3_1);
			 Location vidrio3_6 = new Location (l3.getWorld(), x3, y3+1, z3_1);
			 Location vidrio3_7 = new Location (l3.getWorld(), x3, y3, z3_2);
			 Location vidrio3_8 = new Location (l3.getWorld(), x3, y3+1, z3_2);
			 Location vidrio3_9 = new Location (l3.getWorld(), x3,y3+2,z3);
			 
			 Location vidrio4_1 = new Location (l4.getWorld(), x4_1, y4,z4);
			 Location vidrio4_2 = new Location (l4.getWorld(), x4_1, y4 + 1,z4);
			 Location vidrio4_3 = new Location (l4.getWorld(), x4_2, y4,z4);
			 Location vidrio4_4 = new Location (l4.getWorld(), x4_2, y4+1,z4);
			 Location vidrio4_5 = new Location (l4.getWorld(), x4, y4, z4_1);
			 Location vidrio4_6 = new Location (l4.getWorld(), x4, y4+1, z4_1);
			 Location vidrio4_7 = new Location (l4.getWorld(), x4, y4, z4_2);
			 Location vidrio4_8 = new Location (l4.getWorld(), x4, y4+1, z4_2);
			 Location vidrio4_9 = new Location (l4.getWorld(), x4,y4+2,z4);
			 
			 
			 
			 vidrio1_1.getBlock().setType(Material.AIR);
			 vidrio1_2.getBlock().setType(Material.AIR);
			 vidrio1_3.getBlock().setType(Material.AIR);
			 vidrio1_4.getBlock().setType(Material.AIR);
			 vidrio1_5.getBlock().setType(Material.AIR);
			 vidrio1_6.getBlock().setType(Material.AIR);
			 vidrio1_7.getBlock().setType(Material.AIR);
			 vidrio1_8.getBlock().setType(Material.AIR);
			 vidrio1_9.getBlock().setType(Material.AIR);
			 
			 vidrio2_1.getBlock().setType(Material.AIR);
			 vidrio2_2.getBlock().setType(Material.AIR);
			 vidrio2_3.getBlock().setType(Material.AIR);
			 vidrio2_4.getBlock().setType(Material.AIR);
			 vidrio2_5.getBlock().setType(Material.AIR);
			 vidrio2_6.getBlock().setType(Material.AIR);
			 vidrio2_7.getBlock().setType(Material.AIR);
			 vidrio2_8.getBlock().setType(Material.AIR);
			 vidrio2_9.getBlock().setType(Material.AIR);
			 
			 
			 vidrio3_1.getBlock().setType(Material.AIR);
			 vidrio3_2.getBlock().setType(Material.AIR);
			 vidrio3_3.getBlock().setType(Material.AIR);
			 vidrio3_4.getBlock().setType(Material.AIR);
			 vidrio3_5.getBlock().setType(Material.AIR);
			 vidrio3_6.getBlock().setType(Material.AIR);
			 vidrio3_7.getBlock().setType(Material.AIR);
			 vidrio3_8.getBlock().setType(Material.AIR);
			 vidrio3_9.getBlock().setType(Material.AIR);
			 
			 vidrio4_1.getBlock().setType(Material.AIR);
			 vidrio4_2.getBlock().setType(Material.AIR);
			 vidrio4_3.getBlock().setType(Material.AIR);
			 vidrio4_4.getBlock().setType(Material.AIR);
			 vidrio4_5.getBlock().setType(Material.AIR);
			 vidrio4_6.getBlock().setType(Material.AIR);
			 vidrio4_7.getBlock().setType(Material.AIR);
			 vidrio4_8.getBlock().setType(Material.AIR);
			 vidrio4_9.getBlock().setType(Material.AIR);
			 
			 
		  }
  
    public void cristalcolocar2v2 (Location l1, Location l2, Location l3, Location l4) {
		int x1 =  l1.getBlockX() + 2;
		 int y1 = l1.getBlockY();
		 int z1 = l1.getBlockZ();
		 
		 int x2 =  l2.getBlockX() - 2;
		 int y2 = l2.getBlockY();
		 int z2 = l2.getBlockZ();
		 
		 
		 int x3 =  l3.getBlockX() + 2;
		 int y3 = l3.getBlockY();
		 int z3 = l3.getBlockZ();
		 
		 int x4 =  l4.getBlockX() - 2;
		 int y4 = l4.getBlockY();
		 int z4 = l4.getBlockZ();
		 
		 int x1_1 = x1 +1;
		 int x1_2 = x1 -1;
		 
		 int x2_1 = x2 +1;
		 int x2_2 = x2 -1;
		 
		 int z1_1 = z1+1;
		 int z1_2 = z1-1;
		 
		 int z2_1 = z2+1;
		 int z2_2 = z2-1;
		 
		 int x3_1 = x1 +1;
		 int x3_2 = x1 -1;
		 
		 int x4_1 = x2 +1;
		 int x4_2 = x2 -1;
		 
		 int z3_1 = z1+1;
		 int z3_2 = z1-1;
		 
		 int z4_1 = z2+1;
		 int z4_2 = z2-1;
		 
		 
		 
		 Location vidrio1_1 = new Location (l1.getWorld(), x1_1, y1,z1);
		 Location vidrio1_2 = new Location (l1.getWorld(), x1_1, y1 + 1,z1);		 
		 Location vidrio1_3 = new Location (l1.getWorld(), x1_2, y1,z1);
		 Location vidrio1_4 = new Location (l1.getWorld(), x1_2, y1+1,z1);		 
		 Location vidrio1_5 = new Location (l1.getWorld(), x1, y1, z1_1);
		 Location vidrio1_6 = new Location (l1.getWorld(), x1, y1+1, z1_1);
		 Location vidrio1_7 = new Location (l2.getWorld(), x1, y1, z1_2);
		 Location vidrio1_8 = new Location (l2.getWorld(), x1, y1+1, z1_2);
		 Location vidrio1_9 = new Location (l2.getWorld(), x1,y1+2,z1);

		 Location vidrio2_1 = new Location (l2.getWorld(), x2_1, y2,z2);
		 Location vidrio2_2 = new Location (l2.getWorld(), x2_1, y2 + 1,z2);
		 Location vidrio2_3 = new Location (l2.getWorld(), x2_2, y2,z2);
		 Location vidrio2_4 = new Location (l2.getWorld(), x2_2, y2+1,z2);
		 Location vidrio2_5 = new Location (l2.getWorld(), x2, y2, z2_1);
		 Location vidrio2_6 = new Location (l2.getWorld(), x2, y2+1, z2_1);
		 Location vidrio2_7 = new Location (l2.getWorld(), x2, y2, z2_2);
		 Location vidrio2_8 = new Location (l2.getWorld(), x2, y2+1, z2_2);
		 Location vidrio2_9 = new Location (l2.getWorld(), x2,y2+2,z2);
		 
		 Location vidrio3_1 = new Location (l3.getWorld(), x3_1, y3,z3);
		 Location vidrio3_2 = new Location (l3.getWorld(), x3_1, y3 + 1,z3);
		 Location vidrio3_3 = new Location (l3.getWorld(), x3_2, y3,z3);
		 Location vidrio3_4 = new Location (l3.getWorld(), x3_2, y3+1,z3);
		 Location vidrio3_5 = new Location (l3.getWorld(), x3, y3, z3_1);
		 Location vidrio3_6 = new Location (l3.getWorld(), x3, y3+1, z3_1);
		 Location vidrio3_7 = new Location (l3.getWorld(), x3, y3, z3_2);
		 Location vidrio3_8 = new Location (l3.getWorld(), x3, y3+1, z3_2);
		 Location vidrio3_9 = new Location (l3.getWorld(), x3,y3+2,z3);
		 
		 Location vidrio4_1 = new Location (l4.getWorld(), x4_1, y4,z4);
		 Location vidrio4_2 = new Location (l4.getWorld(), x4_1, y4 + 1,z4);
		 Location vidrio4_3 = new Location (l4.getWorld(), x4_2, y4,z4);
		 Location vidrio4_4 = new Location (l4.getWorld(), x4_2, y4+1,z4);
		 Location vidrio4_5 = new Location (l4.getWorld(), x4, y4, z4_1);
		 Location vidrio4_6 = new Location (l4.getWorld(), x4, y4+1, z4_1);
		 Location vidrio4_7 = new Location (l4.getWorld(), x4, y4, z4_2);
		 Location vidrio4_8 = new Location (l4.getWorld(), x4, y4+1, z4_2);
		 Location vidrio4_9 = new Location (l4.getWorld(), x4,y4+2,z4);
		 
		 
		 
		 vidrio1_1.getBlock().setType(Material.GLASS);
		 vidrio1_2.getBlock().setType(Material.GLASS);
		 vidrio1_3.getBlock().setType(Material.GLASS);
		 vidrio1_4.getBlock().setType(Material.GLASS);
		 vidrio1_5.getBlock().setType(Material.GLASS);
		 vidrio1_6.getBlock().setType(Material.GLASS);
		 vidrio1_7.getBlock().setType(Material.GLASS);
		 vidrio1_8.getBlock().setType(Material.GLASS);
		 vidrio1_9.getBlock().setType(Material.GLASS);
		 
		 vidrio2_1.getBlock().setType(Material.GLASS);
		 vidrio2_2.getBlock().setType(Material.GLASS);
		 vidrio2_3.getBlock().setType(Material.GLASS);
		 vidrio2_4.getBlock().setType(Material.GLASS);
		 vidrio2_5.getBlock().setType(Material.GLASS);
		 vidrio2_6.getBlock().setType(Material.GLASS);
		 vidrio2_7.getBlock().setType(Material.GLASS);
		 vidrio2_8.getBlock().setType(Material.GLASS);
		 vidrio2_9.getBlock().setType(Material.GLASS);
		 
		 
		 vidrio3_1.getBlock().setType(Material.GLASS);
		 vidrio3_2.getBlock().setType(Material.GLASS);
		 vidrio3_3.getBlock().setType(Material.GLASS);
		 vidrio3_4.getBlock().setType(Material.GLASS);
		 vidrio3_5.getBlock().setType(Material.GLASS);
		 vidrio3_6.getBlock().setType(Material.GLASS);
		 vidrio3_7.getBlock().setType(Material.GLASS);
		 vidrio3_8.getBlock().setType(Material.GLASS);
		 vidrio3_9.getBlock().setType(Material.GLASS);
		 
		 vidrio4_1.getBlock().setType(Material.GLASS);
		 vidrio4_2.getBlock().setType(Material.GLASS);
		 vidrio4_3.getBlock().setType(Material.GLASS);
		 vidrio4_4.getBlock().setType(Material.GLASS);
		 vidrio4_5.getBlock().setType(Material.GLASS);
		 vidrio4_6.getBlock().setType(Material.GLASS);
		 vidrio4_7.getBlock().setType(Material.GLASS);
		 vidrio4_8.getBlock().setType(Material.GLASS);
		 vidrio4_9.getBlock().setType(Material.GLASS);
		 
		 
	  }
	  
  
    
    public void crumble (Game g, int por) {
    	
    	  Location a = g.getArena1();
		  Location b =g.getArena2();
		  int ax = a.getBlockX();
		  int az = a.getBlockZ();
		  
		  int y = a.getBlockY();
		  
		  int bx = b.getBlockX();
		  int bz = b.getBlockZ();
		  

		  Player p1 = g.getPlayer1().get(0);
		  Player p2 = g.getPlayer2().get(0);
		  
		  Location p1block = new Location (p1.getWorld(), p1.getLocation().getBlockX(), p1.getLocation().getBlockY()-1,
				  p1.getLocation().getBlockZ());
		  
		  Location p2block = new Location (p2.getWorld(), p2.getLocation().getBlockX(), p2.getLocation().getBlockY()-1,
				  p2.getLocation().getBlockZ());
		 
		  Location spawn1 = new Location (g.getSpawn1().getWorld(),g.getSpawn1().getBlockX(),
				  g.getSpawn1().getBlockY()-1, g.getSpawn1().getBlockZ());
		  
		  Location spawn2 = new Location (g.getSpawn2().getWorld(),g.getSpawn2().getBlockX(),
				  g.getSpawn2().getBlockY()-1, g.getSpawn2().getBlockZ());
		  
		  for (int x = ax; x < bx; x++) {
			  for (int z = az; z < bz; z++) {
				  Location aire = new Location (a.getWorld(), x, y, z); 
				  
				  if ((p1block.getBlockX() == aire.getBlockX() && p1block.getBlockY() == aire.getBlockY() 
						  && p1block.getBlockZ() == aire.getBlockZ()) || (p2block.getBlockX() == aire.getBlockX() && p2block.getBlockY() == aire.getBlockY() 
						  && p2block.getBlockZ() == aire.getBlockZ()) || (spawn1.getBlockX() == aire.getBlockX() && spawn1.getBlockY() == aire.getBlockY() 
						  && spawn1.getBlockZ() == aire.getBlockZ()) ||(spawn2.getBlockX() == aire.getBlockX() && spawn2.getBlockY() == aire.getBlockY() 
						  && spawn2.getBlockZ() == aire.getBlockZ())) {
				  } else {
					  if (aire.getBlock().getType().equals(Material.AIR)) {
						  aire.getBlock().setType(Material.SNOW_BLOCK);
					  }   else {
						  int randomNum = ThreadLocalRandom.current().nextInt(1, 100 + 1);
						  if (randomNum<por) {
							  if (aire.getBlock().getType().equals(Material.SNOW_BLOCK)) {
								  aire.getBlock().setType(Material.AIR);
							  }
						  }
						  }
				  }
			  }
		  }
		  
		  
    }
    

    
    public boolean isStarted(Game g) {
    	if (g.getType().equalsIgnoreCase("ffaspleef")) {
    		if (g.getPlayers().isEmpty()) {
    			return false;
    		}
    		return true;
    	} else {
    	if ((g.getPlayer1().isEmpty()) && (g.getPlayer2().isEmpty())) {
    		return false;
    	} 
    	return true;
    }
    }
    
    
    
    public boolean isInGame(Player p) {
        for (Game a : this.arenas) {
        	if (a.getType().equalsIgnoreCase("spleef") || a.getType().equalsIgnoreCase("spleef2v2") 
        			|| a.getType().equalsIgnoreCase("BuildSpleefPvP") || a.getType().equalsIgnoreCase("bowspleef")) {
        		 if (a.getPlayer1().contains(p) || a.getPlayer2().contains(p))
                     return true;
        	} else if (a.getType().equalsIgnoreCase("ffaspleef")) {
        		if (a.getPlayers().contains(p))
        				  return true;
        	}
        	
           
        }
        return false;
    }
    
    
    public boolean isInFFAQueue(Player p) {
        for (Game a : this.arenas) {
        		if (a.getType().equalsIgnoreCase("ffaspleef")) {
        		 if (a.getQueue().contains(p))
                     return true;
        		}
        }
        return false;
    }
    
    
    public boolean isInQueue(Player p) {
        for (Game a : this.arenas) {
        		 if (a.getQueue().contains(p))
                     return true;
        		
        }
        return false;
    }
    
    
    
    public boolean isSpectating(Player p) {
        for (Game a : this.arenas) {
            if (a.getSpectators().contains(p))
                return true;
        }
        return false;
    }
    
    
    
    public int getQueueMapListRanked (String name) {
    	int queue = 0;
    	for (Game ga : this.arenas) {
    		if (ga.getType().equalsIgnoreCase("spleef")) {
    		if (GameManager.getManager().rankedgames.contains(ga)) {
    		if (ga.getId().contains(name)) {
    			queue = queue + ga.getQueue().size();
    		}
    	}
    	}
    	}
    	
		return queue;
    	
    }
    
    public int getQueueMapListUnranked (String name) {
    	int queue = 0;
    	for (Game ga : this.arenas) {
    		if (ga.getType().equalsIgnoreCase("spleef")) {
    		if (!GameManager.getManager().rankedgames.contains(ga)) {
    		if (ga.getId().contains(name)) {
    			queue = queue + ga.getQueue().size();
    		}
    	}
    		}
    	}
    	
		return queue;
    	
    }
    
    
    public List<Game> getArenasList() {
		return this.arenas;
    	
    }
    
    
    public List<Game> getInGameArenas() {
    	return this.arenasingame;
    }
    
   
  
    
    
    
    public void leaveQueue(Player p,boolean msg,boolean tp) {
    	for (Game g : getArenasList()) {
    		if (g.getQueue().contains(p)) {
    			g.getQueue().remove(p);
    			if (g.getType().equalsIgnoreCase("ffaspleef")) {
					if (!GameManager.getManager().isStarted(g)) {
						if (g.getQueue().size() <= 3) {
							g.falseFFAStarting();
						for (Player pa : g.getQueue()) {
							if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
								pa.sendMessage("�3"+getGamePrefix(p)+" �cNo hay suficientes jugadores, partida cancelada.");
							} else if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
								pa.sendMessage("�3"+getGamePrefix(p)+" �cThere are not enough players, game cancelled.");
							}
						}
						}
					}
				}
    		}
    	}
    	if (tp) p.teleport(Main.getLoc(Main.arena.getConfig().getString("lobby")));
    	if (msg) {   
    	if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    		p.sendMessage("�3"+getGamePrefix(p)+" �6Has salido de la cola");
    	} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    		p.sendMessage("�3"+getGamePrefix(p)+" �6You have been removed from the queue");
    	}
    	}
    	
    	
    	p.getInventory().clear();
    	Main.giveItems(p);
    }
    
 
    public void leave(Player p) {
    	for (Game g : getArenasList()) {
    		try {
    			if (g.getType().equalsIgnoreCase("spleef")) {
    			if (g.getPlayer1().contains(p)) {
    				if (isRanked(g)) {
    					
    				RankedSpleefGame.gameOver(g.getPlayer2().get(0), p, g.getId());
    				} else {
    					SpleefGame.gameOver(g.getPlayer2().get(0), p, g.getId());
    				}
    				for (Player pa : g.getPlayer1()) {
						if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
							pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
						} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
							pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
						}
					}
					
					for (Player pa : g.getPlayer2()) {
						if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
							pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
						} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
							pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
						}
					}
					
					for (Player pa : g.getSpectators()) {
						if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
							pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
						} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
							pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
						}
					}
    			}
    			if (g.getPlayer2().contains(p)) {
    				if (isRanked(g)) {
    					RankedSpleefGame.gameOver(g.getPlayer1().get(0), p, g.getId());
    				} else {
    					SpleefGame.gameOver(g.getPlayer1().get(0), p, g.getId());
    				}
    				for (Player pa : g.getPlayer1()) {
						if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
							pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
						} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
							pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
						}
					}
					
					for (Player pa : g.getPlayer2()) {
						if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
							pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
						} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
							pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
						}
					}
					
					for (Player pa : g.getSpectators()) {
						if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
							pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
						} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
							pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
						}
					}
    			}
    			} else if (g.getType().equalsIgnoreCase("bowspleef")) {
        			if (g.getPlayer1().contains(p)) {
        				if (isRanked(g)) {
        					
        				RankedSpleefGame.gameOver(g.getPlayer2().get(0), p, g.getId());
        				} else {
        					SpleefGame.gameOver(g.getPlayer2().get(0), p, g.getId());
        				}
        				for (Player pa : g.getPlayer1()) {
    						if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
    						} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
    						}
    					}
    					
    					for (Player pa : g.getPlayer2()) {
    						if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
    						} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
    						}
    					}
    					
    					for (Player pa : g.getSpectators()) {
    						if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
    						} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
    						}
    					}
        			}
        			if (g.getPlayer2().contains(p)) {
        				if (isRanked(g)) {
        					RankedSpleefGame.gameOver(g.getPlayer1().get(0), p, g.getId());
        				} else {
        					SpleefGame.gameOver(g.getPlayer1().get(0), p, g.getId());
        				}
        				for (Player pa : g.getPlayer1()) {
    						if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
    						} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
    						}
    					}
    					
    					for (Player pa : g.getPlayer2()) {
    						if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
    						} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
    						}
    					}
    					
    					for (Player pa : g.getSpectators()) {
    						if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
    						} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
    						}
    					}
        			}
        			} else if (g.getType().equalsIgnoreCase("BuildSpleefPvP")) {
    				if (g.getPlayer1().contains(p)) {
        				if (isRanked(g)) {
        					
        				RankedSpleefGame.gameOver(g.getPlayer2().get(0), p, g.getId());
        				} else {
        					SpleefGame.gameOver(g.getPlayer2().get(0), p, g.getId());
        				}
        				for (Player pa : g.getPlayer1()) {
    						if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
    						} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
    						}
    					}
    					
    					for (Player pa : g.getPlayer2()) {
    						if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
    						} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
    						}
    					}
    					
    					for (Player pa : g.getSpectators()) {
    						if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
    						} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
    						}
    					}
        			}
        			if (g.getPlayer2().contains(p)) {
        				if (isRanked(g)) {
        					RankedSpleefGame.gameOver(g.getPlayer1().get(0), p, g.getId());
        				} else {
        					SpleefGame.gameOver(g.getPlayer1().get(0), p, g.getId());
        				}
        				for (Player pa : g.getPlayer1()) {
    						if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
    						} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
    						}
    					}
    					
    					for (Player pa : g.getPlayer2()) {
    						if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
    						} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
    						}
    					}
    					
    					for (Player pa : g.getSpectators()) {
    						if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
    						} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
    							pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
    						}
    					}
        			}
    			} else if (g.getType().equalsIgnoreCase("spleef2v2")) {
					if (g.getPlayer1().contains(p)) {
						if (g.getPlayer1().size() >= 2) {
								g.getPlayer1().remove(p);							
						} else {
							Spleef2v2Game.gameOver(g.getPlayer2(), g.getPlayer1(), g.getId());
						}
						for (Player pa : g.getPlayer1()) {
							if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
								pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
							} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
								pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
							}
						}
						
						for (Player pa : g.getPlayer2()) {
							if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
								pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
							} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
								pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
							}
						}
						
						for (Player pa : g.getSpectators()) {
							if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
								pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
							} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
								pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
							}
						}
					} else if (g.getPlayer2().contains(p)) {
						if (g.getPlayer2().size() >= 2) {
							g.getPlayer2().remove(p);							
					} else {
						Spleef2v2Game.gameOver(g.getPlayer1(), g.getPlayer2(), g.getId());
					}
						for (Player pa : g.getPlayer1()) {
							if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
								pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
							} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
								pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
							}
						}
						
						for (Player pa : g.getPlayer2()) {
							if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
								pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
							} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
								pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
							}
						}
						
						for (Player pa : g.getSpectators()) {
							if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
								pa.sendMessage("�3"+getGamePrefix(p)+" �cThe player �b" + p.getName() + " �chas surrendered!");
							} else if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
								pa.sendMessage("�3"+getGamePrefix(p)+" �cEl jugador �b" + p.getName() + " �cse ha rendido!");
							}
						}
				}
    			}
    			
    			if (g.getPlayers().contains(p)) {
					g.getPlayers().remove(p);
					if (g.getType().equalsIgnoreCase("ffaspleef")) {
						if (!GameManager.getManager().isStarted(g)) {
							if (g.getQueue().size() <= 3) {
							for (Player pa : g.getQueue()) {
								if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
									pa.sendMessage("�3"+getGamePrefix(p)+" �cNo hay suficientes jugadores, partida cancelada.");
								} else if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) {
									pa.sendMessage("�3"+getGamePrefix(p)+" �cThere are not enough players, game cancelled.");
								}
							}
							}
						}
					}
				} 
    			
    	} catch (Exception e) {}
    	leaveQueue(p,false,false);
    	if (g.getInGameSpect().contains(p)) {
			g.getInGameSpect().remove(p);
		}
		if (g.getSpectators().contains(p)) {
			g.getSpectators().remove(p);
		}
		
    }
    	
    	
    	
    	
    }
    
    
    
    
    
    public void addSpleefFFAQueue (Player p) {
    	if (p.hasPermission("splindux.disguise")) Bukkit.dispatchCommand(Main.get().getServer().getConsoleSender(), "ud " + p.getName());
    	for (final Game g : this.arenas) {
    			if (g.getType().equalsIgnoreCase("ffaspleef")) {
    				leave(p);
    				Main.givequeueItems(p);
    				g.getQueue().add(p);
    				p.getPassengers().clear();
    				p.teleport(g.getSpect());
    				if (GameManager.getManager().isStarted(g)) {
    					if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    		    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola para �aFFASpleef");
    		    			p.sendMessage("�aPodr�s jugar una vez que la actual partida termine.");
    		    		} else 	if (DataManager.getLang(p).equalsIgnoreCase("ESP")) { 
    		    			p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the queue for �aFFASpleef");
    		    			p.sendMessage("�aYou can play when the current game is over.");
    		    		}
    					
    				} else {
    					for (Player pa: g.getQueue()) {
    						if (pa != p) {
    							if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
    	    		    			pa.sendMessage("�3"+getGamePrefix(p)+" �b" + p.getName() + " �aha entrado a la Queue. �7(�e" + g.getQueue().size() + "�7)");
    	    		    		} else if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) { 
    	    		    			pa.sendMessage("�3"+getGamePrefix(p)+" �b" + p.getName() + " �ahas joined the Queue. �7(�e" + g.getQueue().size() + "�7)");
    	    		    		}
    						}
    					}
    					if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    		    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola para �aFFASpleef");
    		    		} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) { 
    		    			p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the queue for �aFFASpleef");
    		    		}
    					if (g.getQueue().size() == 3) {
    						for (Player pa : g.getQueue()) {
    							if (DataManager.getLang(pa).equalsIgnoreCase("ESP")) {
    	    		    			pa.sendMessage("�3"+getGamePrefix(p)+" �6La partida comenzar� en 5 segundos.");
    	    		    		} else if (DataManager.getLang(pa).equalsIgnoreCase("ENG")) { 
    	    		    			pa.sendMessage("�3"+getGamePrefix(p)+" �6The game starts in 5 seconds.");
    	    		    		}
    						}
    						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.get(), new Runnable()
    					    {

    							public void run()
    					        {  if (g.getQueue().size() >= 3) {
    					        	FFASpleefGame.startCountdown(g.getId());
    					          }
    								
    					        }
    					      }
    					      , 100L);
    					
    				}
    				}	
    				return;
    	}
    			
    	}
    	
    	
    	if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
			 p.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
		 } else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
			 p.sendMessage("�cThere are not available arenas, please try again later.");
		 }
		p.closeInventory();
		
   	return;
    	
    }
    
 public void addBowSpleefUnrankedQueue(Player p, String id) {	
    	
    	if (p.hasPermission("splindux.disguise")) Bukkit.dispatchCommand(Main.get().getServer().getConsoleSender(), "ud " + p.getName());
    	leave(p);
    	
    	if (id == null) { 	
    		for (Game g : this.arenas) {
    			if (g.getType().equalsIgnoreCase("BowSpleef")) {
    			if (g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
    		if (g.getQueue().size() >= 1) {
    			if (!GameManager.getManager().rankedgames.contains(g)) {
    			if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bUnranked �6para �aBuild Spleef PvP 1vs1�6, en el mapa �b" + g.getId());
    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bUnranked �6queue for �aBuild Spleef PvP 1vs1�6, in the arena �b" + g.getId());
    			}
    			g.getQueue().add(p);
    			checkQueue(g, false);
    			Main.givequeueItems(p);
    			return;
    			}
    		}
    			}  
    		}
    			}
    		
    			List<Game> av = new ArrayList<Game>();
    			for (Game g : this.arenas) {
    				if (g.getType().equalsIgnoreCase("BowSpleef")) {
    				if (g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
    				if (g.getQueue().isEmpty()) {
    					if (!GameManager.getManager().rankedgames.contains(g)) {
    					av.add(g);
    					}
    				}
    				}
    			}
    	}
    			int min = 0;
				int max = av.size()-1;
				Random randomNo = new Random();
				for (Game ga : av) {
					int no = 0;
					try {
				 no = randomNo.nextInt((max-min) + 1) + min;
					} catch (Exception e) {
						no = 0;
					}
				ga = av.get(no);
    					if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    		    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bUnranked �6para �aBuild Spleef PvP 1vs1�6, en el mapa �b" + ga.getId());
    		    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    		    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bUnranked �6queue for �aBuild Spleef PvP 1vs1�6, in the arena �b" + ga.getId());
    		    			}
    				ga.getQueue().add(p);
    				Main.givequeueItems(p);
    				return;
				
    			
    	
    	}
    		if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
				 p.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
			 } else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
				 p.sendMessage("�cThere are not available arenas, please try again later.");
			 }
    		return;
    		
    		} else {
    				for (Game g : this.arenas) {
    					if (g.getType().equalsIgnoreCase("BowSpleef")) {
    					if (g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
    					if (Main.containsIgnoreCase(g.getId(), id)) {
    		    		if (g.getQueue().size() >= 1) {
    		    			if (!GameManager.getManager().rankedgames.contains(g)) {
    		    				if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    		    	    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bUnranked �6para �aBuild Spleef PvP 1vs1�6, en el mapa �b" + g.getId());
    		    	    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    		    	    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bUnranked �6queue for �aBuild Spleef PvP 1vs1�6, in the arena �b" + g.getId());
    		    	    			}
    		    			g.getQueue().add(p);
    		    			checkQueue(g, false);
    		    			Main.givequeueItems(p);
    		    			return;
    		    			}
    		    		}
    		    		}
    		    			}  
    				}
    		}
    				
    					List<Game> av = new ArrayList<Game>();
    		    			for (Game g : this.arenas) {
    		    				if (g.getType().equalsIgnoreCase("BowSpleef")) {
    		    				if (g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
    		    				if (Main.containsIgnoreCase(g.getId(), id)) {
    		    					if (g.getQueue().isEmpty()) {
    		    						av.add(g);
    		    					}
    		    				}
    		    					int min = 0;
		    						int max = av.size()-1;
		    						Random randomNo = new Random();
		    						int no = 0;
		    						try {
		    						 no = randomNo.nextInt((max-min) + 1) + min;
		    						} catch (Exception e) {
		    							no = 0;
		    						}
    		    					for (Game ga : av) {
    		    						
    		    						ga = av.get(no);
    		    						
    		    						if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    		    			    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bUnranked �6para �aBuild Spleef PvP 1vs1�6, en el mapa �b" + ga.getId());
    		    			    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    		    			    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bUnranked �6queue for �aBuild Spleef PvP 1vs1�6, in the arena �b" + ga.getId());
    		    			    			}
    		    				ga.getQueue().add(p);
    		    				Main.givequeueItems(p);
    		    				return;
    		    				}
    		    			
    		    				}
    		    			}
    		    			}
    				
    				 if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    					 p.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
    				 } else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    					 p.sendMessage("�cThere are not available arenas, please try again later.");
    				 }			
    				 return;
    		} 
    	}
    
    
 public void addBSPUnrankedQueue(Player p, String id) {	
    	
    	if (p.hasPermission("splindux.disguise")) Bukkit.dispatchCommand(Main.get().getServer().getConsoleSender(), "ud " + p.getName());
    	leave(p);
    	
    	if (id == null) { 	
    		for (Game g : this.arenas) {
    			if (g.getType().equalsIgnoreCase("BuildSpleefPvP")) {
    			if (g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
    		if (g.getQueue().size() >= 1) {
    			if (!GameManager.getManager().rankedgames.contains(g)) {
    			if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bUnranked �6para �aBuild Spleef PvP 1vs1�6, en el mapa �b" + g.getId());
    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bUnranked �6queue for �aBuild Spleef PvP 1vs1�6, in the arena �b" + g.getId());
    			}
    			g.getQueue().add(p);
    			checkQueue(g, false);
    			Main.givequeueItems(p);
    			return;
    			}
    		}
    			}  
    		}
    			}
    		
    			List<Game> av = new ArrayList<Game>();
    			for (Game g : this.arenas) {
    				if (g.getType().equalsIgnoreCase("BuildSpleefPvP")) {
    				if (g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
    				if (g.getQueue().isEmpty()) {
    					if (!GameManager.getManager().rankedgames.contains(g)) {
    					av.add(g);
    					}
    				}
    				}
    			}
    	}
    			int min = 0;
				int max = av.size()-1;
				Random randomNo = new Random();
				for (Game ga : av) {
					int no = 0;
					try {
				 no = randomNo.nextInt((max-min) + 1) + min;
					} catch (Exception e) {
						no = 0;
					}
				ga = av.get(no);
    					if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    		    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bUnranked �6para �aBuild Spleef PvP 1vs1�6, en el mapa �b" + ga.getId());
    		    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    		    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bUnranked �6queue for �aBuild Spleef PvP 1vs1�6, in the arena �b" + ga.getId());
    		    			}
    				ga.getQueue().add(p);
    				Main.givequeueItems(p);
    				return;
				
    			
    	
    	}
    		if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
				 p.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
			 } else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
				 p.sendMessage("�cThere are not available arenas, please try again later.");
			 }
    		return;
    		
    		} else {
    				for (Game g : this.arenas) {
    					if (g.getType().equalsIgnoreCase("BuildSpleefPvP")) {
    					if (g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
    					if (Main.containsIgnoreCase(g.getId(), id)) {
    		    		if (g.getQueue().size() >= 1) {
    		    			if (!GameManager.getManager().rankedgames.contains(g)) {
    		    				if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    		    	    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bUnranked �6para �aBuild Spleef PvP 1vs1�6, en el mapa �b" + g.getId());
    		    	    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    		    	    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bUnranked �6queue for �aBuild Spleef PvP 1vs1�6, in the arena �b" + g.getId());
    		    	    			}
    		    			g.getQueue().add(p);
    		    			checkQueue(g, false);
    		    			Main.givequeueItems(p);
    		    			return;
    		    			}
    		    		}
    		    		}
    		    			}  
    				}
    		}
    				
    					List<Game> av = new ArrayList<Game>();
    		    			for (Game g : this.arenas) {
    		    				if (g.getType().equalsIgnoreCase("BuildSpleefPvP")) {
    		    				if (g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
    		    				if (Main.containsIgnoreCase(g.getId(), id)) {
    		    					if (g.getQueue().isEmpty()) {
    		    						av.add(g);
    		    					}
    		    				}
    		    					int min = 0;
		    						int max = av.size()-1;
		    						Random randomNo = new Random();
		    						int no = 0;
		    						try {
		    						 no = randomNo.nextInt((max-min) + 1) + min;
		    						} catch (Exception e) {
		    							no = 0;
		    						}
    		    					for (Game ga : av) {
    		    						
    		    						ga = av.get(no);
    		    						
    		    						if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    		    			    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bUnranked �6para �aBuild Spleef PvP 1vs1�6, en el mapa �b" + ga.getId());
    		    			    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    		    			    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bUnranked �6queue for �aBuild Spleef PvP 1vs1�6, in the arena �b" + ga.getId());
    		    			    			}
    		    				ga.getQueue().add(p);
    		    				Main.givequeueItems(p);
    		    				return;
    		    				}
    		    			
    		    				}
    		    			}
    		    			}
    				
    				 if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    					 p.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
    				 } else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    					 p.sendMessage("�cThere are not available arenas, please try again later.");
    				 }			
    				 return;
    		} 
    	}
    
    
    
    
    
    
    
    
    
    
    
    public void addSpleef2v2Queue (Player p, String id) {
    	if (p.hasPermission("splindux.disguise")) Bukkit.dispatchCommand(Main.get().getServer().getConsoleSender(), "ud " + p.getName());
    	leave(p);
    	if (id == null) {
    	for (final Game g : this.arenas) {
    		if (g.getType().equalsIgnoreCase("spleef2v2")) {
    			
    			if (!this.arenasingame.contains(g)) {
    				if (g.getQueue().size() >= 1) {
    					
    					if (!GameManager.getManager().rankedgames.contains(g)) {
    		    			if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    		    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bUnranked �6para �aSnow Spleef 2vs2�6, en el mapa �b" + g.getId());
    		    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    		    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bUnranked �6queue for �aSnow Spleef 2vs2�6, in the arena �b" + g.getId());
    		    			}
    		    			g.getQueue().add(p);
    		    			checkQueue(g, false);
    		    			Main.givequeueItems(p);
    		    			return;
    		    			}
    				}
    			}
    		
    		}
    	}
    	List<Game> av = new ArrayList<Game>();
		for (Game g : this.arenas) {
			if (g.getType().equalsIgnoreCase("spleef2v2")) {
			if (!this.arenasingame.contains(p)) {
			if (g.getQueue().isEmpty()) {
				if (!GameManager.getManager().rankedgames.contains(g)) {
				av.add(g);
				}
			}
			}
		}
}
		int min = 0;
		int max = av.size()-1;
		Random randomNo = new Random();
		for (Game ga : av) {
			int no = 0;
			try {
				no = randomNo.nextInt((max-min) + 1) + min;
			} catch (Exception e) {
				no = 0;
			}
		ga = av.get(no);
				if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
	    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bUnranked �6para �aSnow Spleef 2vs2�6, en el mapa �b" + ga.getId());
	    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
	    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bUnranked �6queue for �aSnow Spleef 2vs2�6, in the arena �b" + ga.getId());
	    			}
			ga.getQueue().add(p);
			Main.givequeueItems(p);
			return;
    	
    	
    	
    	}
    }else {
		for (Game g : this.arenas) {
			if (g.getType().equalsIgnoreCase("spleef2v2")) {
			if (g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
			if (Main.containsIgnoreCase(g.getId(), id)) {
    		if (g.getQueue().size() >= 1) {
    			if (!GameManager.getManager().rankedgames.contains(g)) {
    				if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    	    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bUnranked �6para �aSnow Spleef 2vs2�6, en el mapa �b" + g.getId());
    	    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    	    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bUnranked �6queue for �aSnow Spleef 2vs2�6, in the arena �b" + g.getId());
    	    			}
    			g.getQueue().add(p);
    			checkQueue(g, false);
    			Main.givequeueItems(p);
    			return;
    			}
    		}
    		}
    			}  
		}
}
		
			List<Game> av = new ArrayList<Game>();
    			for (Game g : this.arenas) {
    				if (g.getType().equalsIgnoreCase("spleef2v2")) {
    				if (g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
    				if (Main.containsIgnoreCase(g.getId(), id)) {
    					if (g.getQueue().isEmpty()) {
    						if (!GameManager.getManager().rankedgames.contains(g)) {
    						av.add(g);
    						}
    					}
    				}
    					int min = 0;
						int max = av.size()-1;
						Random randomNo = new Random();
						int no = 0;
						try {
						 no = randomNo.nextInt((max-min) + 1) + min;
						} catch (Exception e) {
							no = 0;
						}
    					for (Game ga : av) {
    						
    						ga = av.get(no);
    						
    						if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    			    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bUnranked �6para �aSnow Spleef 1vs1�6, en el mapa �b" + ga.getId());
    			    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    			    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bUnranked �6queue for �aSnow Spleef 1vs1�6, in the arena �b" + ga.getId());
    			    			}
    				ga.getQueue().add(p);
    				Main.givequeueItems(p);
    				return;
    				}
    			
    				}
    			}
    			}
		
		 if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
			 p.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
		 } else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
			 p.sendMessage("�cThere are not available arenas, please try again later.");
		 }			
		 return;
} 
}
  
    
    public void addUnrankedQueue(Player p, String id) {	
    	
    	if (p.hasPermission("splindux.disguise")) Bukkit.dispatchCommand(Main.get().getServer().getConsoleSender(), "ud " + p.getName());
    	leave(p);
    	
    	if (id == null) { 	
    		for (Game g : this.arenas) {
    			if (g.getType().equalsIgnoreCase("spleef")) {
    			if (g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
    		if (g.getQueue().size() >= 1) {
    			if (!GameManager.getManager().rankedgames.contains(g)) {
    			if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bUnranked �6para �aSnow Spleef 1vs1�6, en el mapa �b" + g.getId());
    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bUnranked �6queue for �aSnow Spleef 1vs1�6, in the arena �b" + g.getId());
    			}
    			g.getQueue().add(p);
    			checkQueue(g, false);
    			Main.givequeueItems(p);
    			return;
    			}
    		}
    			}  
    		}
    			}
    		
    			List<Game> av = new ArrayList<Game>();
    			for (Game g : this.arenas) {
    				if (g.getType().equalsIgnoreCase("spleef")) {
    				if (g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
    				if (g.getQueue().isEmpty()) {
    					if (!GameManager.getManager().rankedgames.contains(g)) {
    					av.add(g);
    					}
    				}
    				}
    			}
    	}
    			int min = 0;
				int max = av.size()-1;
				Random randomNo = new Random();
				for (Game ga : av) {
					int no = 0;
					try {
				 no = randomNo.nextInt((max-min) + 1) + min;
					} catch (Exception e) {
						no = 0;
					}
				ga = av.get(no);
    					if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    		    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bUnranked �6para �aSnow Spleef 1vs1�6, en el mapa �b" + ga.getId());
    		    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    		    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bUnranked �6queue for �aSnow Spleef 1vs1�6, in the arena �b" + ga.getId());
    		    			}
    				ga.getQueue().add(p);
    				Main.givequeueItems(p);
    				return;
				
    			
    	
    	}
    		if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
				 p.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
			 } else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
				 p.sendMessage("�cThere are not available arenas, please try again later.");
			 }
    		return;
    		
    		} else {
    				for (Game g : this.arenas) {
    					if (g.getType().equalsIgnoreCase("spleef")) {
    					if (g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
    					if (Main.containsIgnoreCase(g.getId(), id)) {
    		    		if (g.getQueue().size() >= 1) {
    		    			if (!GameManager.getManager().rankedgames.contains(g)) {
    		    				if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    		    	    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bUnranked �6para �aSnow Spleef 1vs1�6, en el mapa �b" + g.getId());
    		    	    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    		    	    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bUnranked �6queue for �aSnow Spleef 1vs1�6, in the arena �b" + g.getId());
    		    	    			}
    		    			g.getQueue().add(p);
    		    			checkQueue(g, false);
    		    			Main.givequeueItems(p);
    		    			return;
    		    			}
    		    		}
    		    		}
    		    			}  
    				}
    		}
    				
    					List<Game> av = new ArrayList<Game>();
    		    			for (Game g : this.arenas) {
    		    				if (g.getType().equalsIgnoreCase("spleef")) {
    		    				if (g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
    		    				if (Main.containsIgnoreCase(g.getId(), id)) {
    		    					if (g.getQueue().isEmpty()) {
    		    						av.add(g);
    		    					}
    		    				}
    		    					int min = 0;
		    						int max = av.size()-1;
		    						Random randomNo = new Random();
		    						int no = 0;
		    						try {
		    						 no = randomNo.nextInt((max-min) + 1) + min;
		    						} catch (Exception e) {
		    							no = 0;
		    						}
    		    					for (Game ga : av) {
    		    						
    		    						ga = av.get(no);
    		    						
    		    						if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    		    			    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bUnranked �6para �aSnow Spleef 1vs1�6, en el mapa �b" + ga.getId());
    		    			    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    		    			    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bUnranked �6queue for �aSnow Spleef 1vs1�6, in the arena �b" + ga.getId());
    		    			    			}
    		    				ga.getQueue().add(p);
    		    				Main.givequeueItems(p);
    		    				return;
    		    				}
    		    			
    		    				}
    		    			}
    		    			}
    				
    				 if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    					 p.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
    				 } else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    					 p.sendMessage("�cThere are not available arenas, please try again later.");
    				 }			
    				 return;
    		} 
    	}

    
    
    public void addRankedQueue(Player p, String id) {	
    	if (p.hasPermission("splindux.disguise"))Bukkit.dispatchCommand(Main.get().getServer().getConsoleSender(), "ud " + p.getName());
    	leave(p);
    	
    	if (id == null) { 	
    		for (Game g : this.arenas) {
    			if (g.getType().equalsIgnoreCase("spleef")) {
    			if (g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
    		if (g.getQueue().size() >= 1) {
    			if (GameManager.getManager().rankedgames.contains(g)) {
    				if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    	    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bRanked �6para �aSnow Spleef 1vs1�6, en el mapa �b" + g.getId());
    	    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    	    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bRanked �6queue for �aSnow Spleef 1vs1�6, in the arena �b" + g.getId());
    	    			}
    			g.getQueue().add(p);
    			checkQueue(g, true);
    			Main.givequeueItems(p);
    			return;
    			}
    		}
    		}  
    			}
    	}
    			List<Game> av = new ArrayList<Game>();
    			for (Game g : this.arenas) {
    				if (g.getType().equalsIgnoreCase("spleef")) {
    				if (g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
    				if (g.getQueue().isEmpty()) {
    				av.add(g);
    				}
    			}
    				}
    			}
    			
    			int min = 0;
				int max = av.size()-1;
				Random randomNo = new Random();
				int no = 0;
				try {
				 no = randomNo.nextInt((max-min) + 1) + min;		
				} catch (Exception e) {
					no = 0;
				}
    			for (Game ga : av) {
    				ga = av.get(no);
    					if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
        	    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bRanked �6para �aSnow Spleef 1vs1�6, en el mapa �b" + ga.getId());
        	    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
        	    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bRanked �6queue for �aSnow Spleef 1vs1�6, in the arena �b" + ga.getId());
        	    			}
    				GameManager.getManager().rankedgames.add(ga);
    				ga.getQueue().add(p);
    				Main.givequeueItems(p);
    				return;
    				
    			
    				}
    		
    		if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
				 p.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
			 } else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
				 p.sendMessage("�cThere are not available arenas, please try again later.");
			 }
    		
    		} else {
    				for (Game g : this.arenas) {
    					if (g.getType().equalsIgnoreCase("spleef")) {
    					if (g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
    					if (Main.containsIgnoreCase(g.getId(), id)) {
    		    		if (g.getQueue().size() >= 1) {
    		    			if (GameManager.getManager().rankedgames.contains(g)) {
    		    				if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    		    	    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bRanked �6para �aSnow Spleef 1vs1�6, en el mapa �b" + g.getId());
    		    	    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    		    	    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bRanked �6queue for �aSnow Spleef 1vs1�6, in the arena �b" + g.getId());
    		    	    			}
    		    			g.getQueue().add(p);
    		    			checkQueue(g, true);
    		    			Main.givequeueItems(p);
    		    			return;
    		    			}
    		    		}
    		    			}  
    				}
    					}
    		}
    				
    				List<Game> av = new ArrayList<Game>();
    		    			for (Game g : this.arenas) {
    		    				if (g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
    		    				if (Main.containsIgnoreCase(g.getId(), id)) {
    		    					if (g.getQueue().isEmpty()) {
    		    						av.add(g);
    		    					}
    		    				}
    		    					}
    		    				}
    		    			
    		    			int min = 0;
    						int max = av.size()-1;
    						Random randomNo = new Random();
    						int no = 0;
    						try {
    						no = randomNo.nextInt((max-min) + 1) + min;	
    						} catch (Exception e) {
    							no = 0;
    						}
    		    				for (Game ga : av) {
    		    					ga = av.get(no);
    		    						if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    		    	    	    			p.sendMessage("�3"+getGamePrefix(p)+" �6A�adido a la cola �bRanked �6para �aSnow Spleef 1vs1�6, en el mapa �b" + ga.getId());
    		    	    	    			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    		    	    	    				p.sendMessage("�3"+getGamePrefix(p)+" �6Added to the �bRanked �6queue for �aSnow Spleef 1vs1�6, in the arena �b" + ga.getId());
    		    	    	    			}
    		    				GameManager.getManager().rankedgames.add(ga);
    		    				ga.getQueue().add(p);
    		    				Main.givequeueItems(p);
    		    				return;
    		    				
    		    				}
    		    		
    		    			}
    				
    				 if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
    					 p.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
    				 } else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
    					 p.sendMessage("�cThere are not available arenas, please try again later.");
    				 }		
    				 return;
    		} 
    	
    	
    
public void DuelGame2v2 (Player p1, Player p2, Player p3,Player p4, String id) {
	
	if (p1.hasPermission("splindux.disguise"))Bukkit.dispatchCommand(Main.get().getServer().getConsoleSender(), "ud " + p1.getName());
	if (p2.hasPermission("splindux.disguise"))Bukkit.dispatchCommand(Main.get().getServer().getConsoleSender(), "ud " + p2.getName());
	if (p3.hasPermission("splindux.disguise"))Bukkit.dispatchCommand(Main.get().getServer().getConsoleSender(), "ud " + p3.getName());
	if (p4.hasPermission("splindux.disguise"))Bukkit.dispatchCommand(Main.get().getServer().getConsoleSender(), "ud " + p4.getName());
	
	leave(p1);
	leave(p2);
	leave(p3);
	leave(p4);
	
	if (id == null) {
		List<Game> av = new ArrayList<Game>();
		 for (Game g : getArenasList()) {
			 if (g.getType().equalsIgnoreCase("spleef2v2")) {
			 if (g.getQueue().size() == 0 && g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
				 if (!this.rankedgames.contains(g)) {
				 av.add(g);
				 }
			 }
			 }
		 }
		 	int min = 0;
			int max = av.size()-1;
			Random randomNo = new Random();
			int no = randomNo.nextInt((max-min) + 1) + min;		
			
		 	for (Game ga : av) {
		 		ga = av.get(no);
		 			ga.getQueue().add(p1);
					ga.getQueue().add(p2);
					ga.getQueue().add(p3);
					ga.getQueue().add(p4);
					checkQueue(ga, false);
					return;
			 }
		 	
		 
		 if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
			 p1.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
		 } else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
			 p1.sendMessage("�cThere are not available arenas, please try again later.");
		 }
		if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
		 p2.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
		} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
			p2.sendMessage("�cThere are not available arenas, please try again later.");
		}
		 return;
	} else {
		for (Game g : getArenasList()) {
			if (g.getType().equalsIgnoreCase("spleef2v2")) {
			if (Main.containsIgnoreCase(g.getId(), id)) {
				if (g.getQueue().size() == 0 && g.getPlayer1().isEmpty() && g.getPlayer2().isEmpty()) {
					if (!this.rankedgames.contains(g)) {
				g.getQueue().add(p1);
				g.getQueue().add(p2);
				g.getQueue().add(p3);
				g.getQueue().add(p4);
				checkQueue(g,false);
				return;
					}
				}
			} 
			}
		}
		if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
			 p1.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
		 } else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
			 p1.sendMessage("�cThere are not available arenas, please try again later.");
		 }
		if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
		 p2.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
		} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
			p2.sendMessage("�cThere are not available arenas, please try again later.");
		}
		return;
	}
	
	
}

public void DuelGameBowSpleef (Player p1, Player p2, String id) {
	
	if (p1.hasPermission("splindux.disguise"))Bukkit.dispatchCommand(Main.get().getServer().getConsoleSender(), "ud " + p1.getName());
	if (p2.hasPermission("splindux.disguise"))Bukkit.dispatchCommand(Main.get().getServer().getConsoleSender(), "ud " + p2.getName());
	leave(p1);
	leave(p2);
	
	if (id == null) {
		List<Game> av = new ArrayList<Game>();
		 for (Game g : getArenasList()) {
			 if (g.getType().equalsIgnoreCase("bowspleef")) {
			 if (g.getQueue().size() == 0) {
				 if (!this.rankedgames.contains(g) && !this.arenasingame.contains(g)) {
				 av.add(g);
				 }
			 }
			 }
		 }
		 	int min = 0;
			int max = av.size()-1;
			Random randomNo = new Random();
			int no = randomNo.nextInt((max-min) + 1) + min;		
			
		 	for (Game ga : av) {
		 		ga = av.get(no);
				 ga.getQueue().add(p1);
					ga.getQueue().add(p2);
					checkQueue(ga, false);
					return;
			 }
		 	
		 
		 if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
			 p1.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
		 } else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
			 p1.sendMessage("�cThere are not available arenas, please try again later.");
		 }
		if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
		 p2.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
		} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
			p2.sendMessage("�cThere are not available arenas, please try again later.");
		}
		 return;
	} else {
	for (Game g : getArenasList()) {
		if (g.getType().equalsIgnoreCase("bowspleef")) {
		if (Main.containsIgnoreCase(g.getId(), id)) {
			if (g.getQueue().size() == 0) {
				if (!this.rankedgames.contains(g) && !this.arenasingame.contains(g)) {
			g.getQueue().add(p1);
			g.getQueue().add(p2);
			checkQueue(g,false);
			return;
				}
			}
		} 
		}
	}
	if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
		 p1.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
	 } else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
		 p1.sendMessage("�cThere are not available arenas, please try again later.");
	 }
	if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
	 p2.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
	} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
		p2.sendMessage("�cThere are not available arenas, please try again later.");
	}
	return;
	}
}

public void DuelGameBSP (Player p1, Player p2, String id) {
	
	if (p1.hasPermission("splindux.disguise"))Bukkit.dispatchCommand(Main.get().getServer().getConsoleSender(), "ud " + p1.getName());
	if (p2.hasPermission("splindux.disguise"))Bukkit.dispatchCommand(Main.get().getServer().getConsoleSender(), "ud " + p2.getName());
	leave(p1);
	leave(p2);
	
	if (id == null) {
		List<Game> av = new ArrayList<Game>();
		 for (Game g : getArenasList()) {
			 if (g.getType().equalsIgnoreCase("BuildSpleefPvP")) {
			 if (g.getQueue().size() == 0) {
				 if (!this.rankedgames.contains(g) && !this.arenasingame.contains(g)) {
				 av.add(g);
				 }
			 }
			 }
		 }
		 	int min = 0;
			int max = av.size()-1;
			Random randomNo = new Random();
			int no = randomNo.nextInt((max-min) + 1) + min;		
			
		 	for (Game ga : av) {
		 		ga = av.get(no);
				 ga.getQueue().add(p1);
					ga.getQueue().add(p2);
					checkQueue(ga, false);
					return;
			 }
		 	
		 
		 if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
			 p1.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
		 } else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
			 p1.sendMessage("�cThere are not available arenas, please try again later.");
		 }
		if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
		 p2.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
		} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
			p2.sendMessage("�cThere are not available arenas, please try again later.");
		}
		 return;
	} else {
	for (Game g : getArenasList()) {
		if (g.getType().equalsIgnoreCase("BuildSpleefPvP")) {
		if (Main.containsIgnoreCase(g.getId(), id)) {
			if (g.getQueue().size() == 0) {
				if (!this.rankedgames.contains(g) && !this.arenasingame.contains(g)) {
			g.getQueue().add(p1);
			g.getQueue().add(p2);
			checkQueue(g,false);
			return;
				}
			}
		} 
		}
	}
	if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
		 p1.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
	 } else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
		 p1.sendMessage("�cThere are not available arenas, please try again later.");
	 }
	if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
	 p2.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
	} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
		p2.sendMessage("�cThere are not available arenas, please try again later.");
	}
	return;
	}
}

public void DuelGame (Player p1, Player p2, String id) {
	
	if (p1.hasPermission("splindux.disguise"))Bukkit.dispatchCommand(Main.get().getServer().getConsoleSender(), "ud " + p1.getName());
	if (p2.hasPermission("splindux.disguise"))Bukkit.dispatchCommand(Main.get().getServer().getConsoleSender(), "ud " + p2.getName());
	leave(p1);
	leave(p2);
	
	if (id == null) {
		List<Game> av = new ArrayList<Game>();
		 for (Game g : getArenasList()) {
			 if (g.getType().equalsIgnoreCase("spleef")) {
			 if (g.getQueue().size() == 0) {
				 if (!this.rankedgames.contains(g) && !this.arenasingame.contains(g)) {
				 av.add(g);
				 }
			 }
			 }
		 }
		 	int min = 0;
			int max = av.size()-1;
			Random randomNo = new Random();
			int no = randomNo.nextInt((max-min) + 1) + min;		
			
		 	for (Game ga : av) {
		 		ga = av.get(no);
				 ga.getQueue().add(p1);
					ga.getQueue().add(p2);
					checkQueue(ga, false);
					return;
			 }
		 	
		 
		 if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
			 p1.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
		 } else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
			 p1.sendMessage("�cThere are not available arenas, please try again later.");
		 }
		if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
		 p2.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
		} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
			p2.sendMessage("�cThere are not available arenas, please try again later.");
		}
		 return;
	} else {
	for (Game g : getArenasList()) {
		if (g.getType().equalsIgnoreCase("spleef")) {
		if (Main.containsIgnoreCase(g.getId(), id)) {
			if (g.getQueue().size() == 0) {
				if (!this.rankedgames.contains(g) && !this.arenasingame.contains(g)) {
			g.getQueue().add(p1);
			g.getQueue().add(p2);
			checkQueue(g,false);
			return;
				}
			}
		} 
		}
	}
	if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
		 p1.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
	 } else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
		 p1.sendMessage("�cThere are not available arenas, please try again later.");
	 }
	if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
	 p2.sendMessage("�cNo hay arenas disponibles en este momento, por favor intenta m�s tarde.");
	} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
		p2.sendMessage("�cThere are not available arenas, please try again later.");
	}
	return;
	}
}

public void checkQueue(Game g, boolean isranked) {
		if (g.getType().equalsIgnoreCase("spleef2v2")) {
			if (g.getQueue().size() >=4) {
				Player p1_A = g.getQueue().get(0);
				Player p1_B = g.getQueue().get(1);
				Player p2_A = g.getQueue().get(2);
				Player p2_B = g.getQueue().get(3);
				
				
				g.getQueue().remove(p1_A);
				g.getQueue().remove(p1_B);
				g.getQueue().remove(p2_A);
				g.getQueue().remove(p2_B);
				
				g.getPlayer1().add(p1_A);
				g.getPlayer1().add(p1_B);
				g.getPlayer2().add(p2_A);
				g.getPlayer2().add(p2_B);
				
				if (isranked == true) { 
					if (DataManager.getLang(p1_A).equalsIgnoreCase("ESP")) {
					p1_A.sendMessage("�3"+getGamePrefix(p1_A)+" �6Rivales encontrados: �a" + p2_A.getName()  + "-" + p2_B.getName() + "�7(�e" + DataManager.getElo(p2_A) + "�7)");
					p1_A.sendMessage("�aTu team es: �b" + p1_B.getName());
					} else if (DataManager.getLang(p1_A).equalsIgnoreCase("ENG")) {
						p1_A.sendMessage("�3"+getGamePrefix(p1_A)+" �6Found opponents: �a" + p2_A.getName()  + "-" + p2_B.getName() + "�7(�e" + DataManager.getElo(p2_A) + "�7)");
						p1_A.sendMessage("�aYour teammate: �b" + p1_B.getName());
						}
					
					if (DataManager.getLang(p1_B).equalsIgnoreCase("ESP")) {
						p1_B.sendMessage("�3"+getGamePrefix(p1_B)+" �6Rivales encontrados: �a" + p2_A.getName()  + "-" + p2_B.getName() + "�7(�e" + DataManager.getElo(p2_A) + "�7)");
						p1_B.sendMessage("�aTu team es: �b" + p1_A.getName());
						} else if (DataManager.getLang(p1_A).equalsIgnoreCase("ENG")) {
							p1_B.sendMessage("�3"+getGamePrefix(p1_B)+" �6Found opponents: �a" + p2_A.getName()  + "-" + p2_B.getName()+ "�7(�e" + DataManager.getElo(p2_A) + "�7)");
							p1_B.sendMessage("�aYour teammate: �b" + p1_A.getName());
							}
					
					
					if (DataManager.getLang(p2_A).equalsIgnoreCase("ESP")) {
						p2_A.sendMessage("�3"+getGamePrefix(p2_A)+" �6Rivales encontrados: �a" + p1_A.getName()  + "-" + p1_B.getName()+ "�7(�e" + DataManager.getElo(p1_A) + "�7)");
						p2_A.sendMessage("�aTu team es: �b" + p2_B.getName());
						} else if (DataManager.getLang(p2_A).equalsIgnoreCase("ENG")) {
							p2_A.sendMessage("�3"+getGamePrefix(p2_A)+" �6Found opponents: �a" + p1_A.getName()  + "-" + p1_B.getName()+ "�7(�e" + DataManager.getElo(p1_A) + "�7)");
							p2_A.sendMessage("�aYour teammate: �b" + p2_B.getName());
							}
					
					if (DataManager.getLang(p2_B).equalsIgnoreCase("ESP")) {
						p2_B.sendMessage("�3"+getGamePrefix(p2_B)+" �6Rivales encontrados: �a" + p1_A.getName()  + "-" + p1_B.getName()+ "�7(�e" + DataManager.getElo(p1_A) + "�7)");
						p2_B.sendMessage("�aTu team es: �b" + p2_A.getName());
						} else if (DataManager.getLang(p1_A).equalsIgnoreCase("ENG")) {
							p2_B.sendMessage("�3"+getGamePrefix(p2_B)+" �6Found opponents: �a" + p1_A.getName()  + "-" + p1_B.getName()+ "�7(�e" + DataManager.getElo(p1_A) + "�7)");
							p2_B.sendMessage("�aYour teammate: �b" + p2_A.getName());
							}
					
					
		          g.resetWin();
		          g.resetArenaStarting();
		   	      g.resetPoints();
		   	      g.resetRounds();
		   	      g.resetTime();
		   	      this.arenasingame.add(g);
		   	      p1_A.getInventory().clear();
		   	      p2_A.getInventory().clear(); 	  
		   	      p1_B.getInventory().clear();
		   	      p2_B.getInventory().clear(); 	
		   	      
		   	      p1_A.getPassengers().clear();
		   	      p1_B.getPassengers().clear();
		   	      p2_A.getPassengers().clear();
		   	      p2_B.getPassengers().clear();
		   	
		   	      DataManager.playedRanked(p1_A);
		   	      DataManager.playedRanked(p2_A);
		   	      DataManager.playedRanked(p1_B);
		   	      DataManager.playedRanked(p2_B);
		   	      
		     //   RankedSpleefGame.startCountdown(g.getId());
		   	      
		   	      
		        } else  { 
		        	if (DataManager.getLang(p1_A).equalsIgnoreCase("ESP")) {
						p1_A.sendMessage("�3"+getGamePrefix(p1_A)+" �6Rivales encontrados: �a" + p2_A.getName()  + "-" + p2_B.getName());
						p1_A.sendMessage("�aTu team es: �b" + p1_B.getName());
						} else if (DataManager.getLang(p1_A).equalsIgnoreCase("ENG")) {
							p1_A.sendMessage("�3"+getGamePrefix(p1_A)+" �6Found opponents: �a" + p2_A.getName()  + "-" + p2_B.getName());
							p1_A.sendMessage("�aYour teammate: �b" + p1_B.getName());
							}
						
						if (DataManager.getLang(p1_B).equalsIgnoreCase("ESP")) {
							p1_B.sendMessage("�3"+getGamePrefix(p1_B)+" �6Rivales encontrados: �a" + p2_A.getName()  + "-" + p2_B.getName());
							p1_B.sendMessage("�aTu team es: �b" + p1_A.getName());
							} else if (DataManager.getLang(p1_A).equalsIgnoreCase("ENG")) {
								p1_B.sendMessage("�3"+getGamePrefix(p1_B)+" �6Found opponents: �a" + p2_A.getName()  + "-" + p2_B.getName());
								p1_B.sendMessage("�aYour teammate: �b" + p1_A.getName());
								}
						
						
						if (DataManager.getLang(p2_A).equalsIgnoreCase("ESP")) {
							p2_A.sendMessage("�3"+getGamePrefix(p2_A)+" �6Rivales encontrados: �a" + p1_A.getName()  + "-" + p1_B.getName());
							p2_A.sendMessage("�aTu team es: �b" + p2_B.getName());
							} else if (DataManager.getLang(p2_A).equalsIgnoreCase("ENG")) {
								p2_A.sendMessage("�3"+getGamePrefix(p2_A)+" �6Found opponents: �a" + p1_A.getName()  + "-" + p1_B.getName());
								p2_A.sendMessage("�aYour teammate: �b" + p2_B.getName());
								}
						
						if (DataManager.getLang(p2_B).equalsIgnoreCase("ESP")) {
							p2_B.sendMessage("�3"+getGamePrefix(p2_B)+" �6Rivales encontrados: �a" + p1_A.getName()  + "-" + p1_B.getName());
							p2_B.sendMessage("�aTu team es: �b" + p2_A.getName());
							} else if (DataManager.getLang(p2_B).equalsIgnoreCase("ENG")) {
								p2_B.sendMessage("�3"+getGamePrefix(p2_B)+" �6Found opponents: �a" + p1_A.getName()  + "-" + p1_B.getName());
								p2_B.sendMessage("�aYour teammate: �b" + p2_A.getName());
								}
						
		        	
		        	
				g.resetWin();
				g.resetArenaStarting();
		   	      g.resetPoints();
		   	      g.resetRounds();
		   	      g.resetTime();
		   	      this.arenasingame.add(g);
		   	      p1_A.getInventory().clear();
		   	      p2_A.getInventory().clear(); 	  
		   	      p1_B.getInventory().clear();
		   	      p2_B.getInventory().clear(); 
		   	      
		   	      p1_A.getPassengers().clear();
		   	      p1_B.getPassengers().clear();
		   	      p2_A.getPassengers().clear();
		   	      p2_B.getPassengers().clear();
		        	Spleef2v2Game.startCountdown(g.getId());
		        }
				
				
			}
		} else if (g.getType().equalsIgnoreCase("spleef")) {
		if (g.getQueue().size() >= 2) {
			Player p1 = g.getQueue().get(0);
			Player p2 = g.getQueue().get(1);
			
			g.getQueue().remove(p1);
			g.getQueue().remove(p2);
			
			g.getPlayer1().add(p1);
			g.getPlayer2().add(p2);
			
			
			
			if (isranked == true) { 
				if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
				p1.sendMessage("�3"+getGamePrefix(p1)+" �6Rival encontrado: �a" + p2.getName() + "�7(�e" + DataManager.getElo(p2) + "�7)");
				} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
					p1.sendMessage("�3"+getGamePrefix(p1)+" �6Found opponent : �a" + p2.getName() + "�7(�e" + DataManager.getElo(p2) + "�7)");
				}
				
				if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
					p2.sendMessage("�3"+getGamePrefix(p2)+" �6Rival encontrado: �a" + p1.getName() + "�7(�e" + DataManager.getElo(p1) + "�7)");
					} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
						p2.sendMessage("�3"+getGamePrefix(p2)+" �6Found opponent: �a" + p1.getName() + "�7(�e" + DataManager.getElo(p1) + "�7)");
					}
	        	g.resetWin();
	        	g.resetArenaStarting();
	   	      g.resetPoints();
	   	      g.resetRounds();
	   	      g.resetTime();
	   	 	 this.arenasingame.add(g);
	   	 	 this.rankedgames.add(g);
	   	      p1.getInventory().clear(); 
	   	      p1.getPassengers().clear();
	   	      p2.getInventory().clear(); 	
	   	      p2.getPassengers().clear();
	   	      DataManager.playedRanked(p1);
	   	      DataManager.playedRanked(p2);
	   	      
	        RankedSpleefGame.startCountdown(g.getId());
	        
	        } else  { 
	        	g.resetWin();
	        	 g.resetArenaStarting();
	   	      g.resetPoints();
	   	      g.resetRounds();
	   	      g.resetTime();
	   	      this.arenasingame.add(g);
	   	      p1.getInventory().clear();
	   	      p1.getPassengers().clear();
	   	      p2.getPassengers().clear();
	        	SpleefGame.startCountdown(g.getId());
	        }
			
		}
		}  else if (g.getType().equalsIgnoreCase("BuildSpleefPvP")) {
			if (g.getQueue().size() >= 2) {
				Player p1 = g.getQueue().get(0);
				Player p2 = g.getQueue().get(1);
				
				g.getQueue().remove(p1);
				g.getQueue().remove(p2);
				
				g.getPlayer1().add(p1);
				g.getPlayer2().add(p2);
				
				
				
				if (isranked == true) { 
					if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
					p1.sendMessage("�3"+getGamePrefix(p1)+" �6Rival encontrado: �a" + p2.getName() + "�7(�e" + DataManager.getElo(p2) + "�7)");
					} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
						p1.sendMessage("�3"+getGamePrefix(p1)+" �6Found opponent : �a" + p2.getName() + "�7(�e" + DataManager.getElo(p2) + "�7)");
					}
					
					if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
						p2.sendMessage("�3"+getGamePrefix(p2)+" �6Rival encontrado: �a" + p1.getName() + "�7(�e" + DataManager.getElo(p1) + "�7)");
						} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
							p2.sendMessage("�3"+getGamePrefix(p2)+" �6Found opponent: �a" + p1.getName() + "�7(�e" + DataManager.getElo(p1) + "�7)");
						}
					
		        	g.resetWin();
		        	g.resetArenaStarting();
		        	g.resetPoints();
		   	      	g.resetRounds();
		   	      	g.resetTime();
		   	      	this.arenasingame.add(g);
		   	      	this.rankedgames.add(g);
		   	      p1.getInventory().clear(); 
		   	      p1.getPassengers().clear();
		   	      p2.getInventory().clear(); 	
		   	      p2.getPassengers().clear();
		   	      DataManager.playedRanked(p1);
		   	      DataManager.playedRanked(p2);
		        RankedSpleefGame.startCountdown(g.getId());
		        
		        } else  { 
		        	g.resetWin();
		        	 g.resetArenaStarting();
		   	      g.resetPoints();
		   	      g.resetRounds();
		   	      g.resetTime();
		   	      this.arenasingame.add(g);
		   	      p1.getInventory().clear();
		   	      p1.getPassengers().clear();
		   	      p2.getPassengers().clear();
		        	BuildSpleefPvPGame.startCountdown(g.getId());
		        }
				
			}
			}else if (g.getType().equalsIgnoreCase("bowspleef")) {
				if (g.getQueue().size() >= 2) {
					Player p1 = g.getQueue().get(0);
					Player p2 = g.getQueue().get(1);
					
					g.getQueue().remove(p1);
					g.getQueue().remove(p2);
					
					g.getPlayer1().add(p1);
					g.getPlayer2().add(p2);
					
					
					
					if (isranked == true) { 
						if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
						p1.sendMessage("�3"+getGamePrefix(p1)+" �6Rival encontrado: �a" + p2.getName() + "�7(�e" + DataManager.getElo(p2) + "�7)");
						} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
							p1.sendMessage("�3"+getGamePrefix(p1)+" �6Found opponent : �a" + p2.getName() + "�7(�e" + DataManager.getElo(p2) + "�7)");
						}
						
						if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
							p2.sendMessage("�3"+getGamePrefix(p2)+" �6Rival encontrado: �a" + p1.getName() + "�7(�e" + DataManager.getElo(p1) + "�7)");
							} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
								p2.sendMessage("�3"+getGamePrefix(p2)+" �6Found opponent: �a" + p1.getName() + "�7(�e" + DataManager.getElo(p1) + "�7)");
							}
						
			        	g.resetWin();
			        	g.resetArenaStarting();
			        	g.resetPoints();
			   	      	g.resetRounds();
			   	      	g.resetTime();
			   	      	this.arenasingame.add(g);
			   	      	this.rankedgames.add(g);
			   	      p1.getInventory().clear(); 
			   	      p1.getPassengers().clear();
			   	      p2.getInventory().clear(); 	
			   	      p2.getPassengers().clear();
			   	      DataManager.playedRanked(p1);
			   	      DataManager.playedRanked(p2);
			   	      
				        RankedSpleefGame.startCountdown(g.getId());
			        
			        } else  { 
			        	g.resetWin();
			        	 g.resetArenaStarting();
			   	      g.resetPoints();
			   	      g.resetRounds();
			   	      g.resetTime();
			   	      this.arenasingame.add(g);
			   	      p1.getInventory().clear();
			   	      p1.getPassengers().clear();
			   	      p2.getPassengers().clear();
			        	BowSpleefGame.startCountdown(g.getId());
			        }
					
				}
				}
}
 
public void resetRequest(Player p) {
	Game g = GameManager.getManager().getArenabyPlayer(p);
	
	if (!g.getReset().contains(p)) {
	g.getReset().add(p);
	}
	
	if ((g.getPlayer1().size() + g.getPlayer2().size()) <= g.getReset().size()) {
		reinicio(g);
		
		for (Player p1 : g.getPlayer1()) {
			if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
			p1.sendMessage("�3"+getGamePrefix(p)+"�6 Arena reiniciada");
			p1.sendMessage("�3"+getGamePrefix(p)+" �aLa ronda �c" + g.getRounds() + " �aha terminado en empate");
			
			} else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
				p1.sendMessage("�3"+getGamePrefix(p)+"�6 Arena restarted");
				p1.sendMessage("�3"+getGamePrefix(p)+" �aThe round �c" + g.getRounds() + " �ahas ended in a draw");
			}
			
			if (g.getType().equalsIgnoreCase("spleef2v2")) {
				  Spleef2v2Game.teamTeleport(g.getPlayer1(), g.getSpawn1());
			} else {
			p1.teleport(g.getSpawn1());
			}
		}
		
		for (Player p2 : g.getPlayer2()) {
			if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
			p2.sendMessage("�3"+getGamePrefix(p)+"�6 Arena reiniciada");
			p2.sendMessage("�3"+getGamePrefix(p)+" �aLa ronda �c" + g.getRounds() + " �a ha terminado en empate");
			} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
				p2.sendMessage("�3"+getGamePrefix(p)+"�6 Arena restarted");
				p2.sendMessage("�3"+getGamePrefix(p)+" �aThe round �c" + g.getRounds() + " �a has ended in a draw");
			}
			
			if (g.getType().equalsIgnoreCase("spleef2v2")) {
				  Spleef2v2Game.teamTeleport(g.getPlayer2(), g.getSpawn2());
			} else {
			p2.teleport(g.getSpawn2());
			}
		}
		
		for (Player sp : g.getSpectators()) {		
			if (DataManager.getLang(sp).equalsIgnoreCase("ESP")) {
			sp.sendMessage("�3"+getGamePrefix(p)+" �aLa ronda �c" + g.getRounds() + " ha terminado en empate");
			} else if (DataManager.getLang(sp).equalsIgnoreCase("ENG")) {
				sp.sendMessage("�3"+getGamePrefix(p)+" �aThe round �c" + g.getRounds() + " has ended in a draw");
			}
		}
		
		g.addRounds();
		g.getReset().clear();
		
	} else {
			if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
			p.sendMessage("�3"+getGamePrefix(p)+"�6 Has solicitado reiniciar la arena");
			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
				p.sendMessage("�3"+getGamePrefix(p)+"�6 You have requested to reset the arena");
			}
			if (g.getPlayer1().contains(p)) {
				for (Player p2 :GameManager.getManager().getArenabyPlayer(p).getPlayer2()) {
				if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
				p2.sendMessage("�3"+getGamePrefix(p)+"�aTu oponente ha solicitado reiniciar la arena, coloca �b/reset �apara reiniciarla.");
				} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
					p2.sendMessage("�3"+getGamePrefix(p)+"�a Your oponent has requested to reset the arena, type �b/reset �a to reset it.");
				}
				}
			} else if (g.getPlayer2().contains(p)) {
				for (Player p1 :GameManager.getManager().getArenabyPlayer(p).getPlayer1()) {
				if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
				p1.sendMessage("�3"+getGamePrefix(p)+"�aTu oponente ha solicitado reiniciar la arena, coloca �b/reset �apara reiniciarla.");
				} else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
					p1.sendMessage("�3"+getGamePrefix(p)+"�a Your oponent has requested to reset the arena, type �b/reset �a to reset it.");
				}
				}
			}
				
		
	}
	
}

public void crumbleRequest(Player p, Integer por) {
	Game g = GameManager.getManager().getArenabyPlayer(p);
	if (!g.getCrumble().containsKey(p)) {
	g.getCrumble().put(p, por);
	}
	
	if ((g.getPlayer1().size() + g.getPlayer2().size()) <= g.getCrumble().size()) {
		
		for (Integer i : g.getCrumble().values()) {
			if (!(por == i)) {
				break;
			}
			
			crumble(g, por);
			g.getCrumble().clear();
			
			for (Player p1 : g.getPlayer1()) {
				if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
				p1.sendMessage("�3"+getGamePrefix(p)+"�6 Ultimo punto seteado a: �a" + por);
				} else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
					p1.sendMessage("�3"+getGamePrefix(p)+"�6 Last point set to : �a" + por);
				}
			}
			for (Player p2 : g.getPlayer2()) {
				if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
				p2.sendMessage("�3"+getGamePrefix(p)+"�6 Ultimo punto seteado a: �a" + por);
				} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
					p2.sendMessage("�3"+getGamePrefix(p)+"�6 Last point set to : �a" + por);
				}
			}
			
			return;
			
		}
		
			if (g.getPlayer1().contains(p)) {
				
				for (Player p2 : g.getPlayer2()) {
					if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
						p2.sendMessage("�3"+getGamePrefix(p)+"�aTu oponente ha solicitado crumblear la arena �7(�e"+por + "%�7)�a, coloca �b/crumble " 
								+ por + "�a para hacerlo.");
						} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
							p2.sendMessage("�3"+getGamePrefix(p)+"�a Your oponent has requested to crumble the arena�7(�e"+por + "%�7)�a, type �b/crumble " 
								+ por + "�a to do it.");
					}
				}
				
				for (Player p1 : g.getPlayer1()) {
					
					if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
						p1.sendMessage("�3"+getGamePrefix(p)+"�6 Has solicitado crumblear la arena");
						} else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
							p1.sendMessage("�3"+getGamePrefix(p)+"�6 You have requested to crumble the arena");
						}
				}
				
				
				
			} else if (g.getPlayer2().contains(p)) {
				
				for (Player p1 : g.getPlayer1()) {
					if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
						p1.sendMessage("�3"+getGamePrefix(p)+"�aTu oponente ha solicitado crumblear la arena �7(�e"+por + "%�7)�a, coloca �b/crumble " 
								+ por + "�a para hacerlo.");
						} else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
							p1.sendMessage("�3"+getGamePrefix(p)+"�a Your oponent has requested to crumble the arena�7(�e"+por + "%�7)�a, type �b/crumble " 
								+ por + "�a to do it.");
					}
					}
					
					for (Player p2 : g.getPlayer2()) {
						
						if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
							p2.sendMessage("�3"+getGamePrefix(p)+"�6 Has solicitado crumblear la arena");
							} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
								p2.sendMessage("�3"+getGamePrefix(p)+"�6 You have requested to crumble the arena");
							}
					}
			}
		
			g.getCrumble().put(p, por);
		
			
		
		
	} else {
		if (g.getPlayer1().contains(p)) {
			
			for (Player p2 : g.getPlayer2()) {
				if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
					p2.sendMessage("�3"+getGamePrefix(p)+"�aTu oponente ha solicitado crumblear la arena �7(�e"+por + "%�7)�a, coloca �b/crumble " 
							+ por + "�a para hacerlo.");
					} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
						p2.sendMessage("�3"+getGamePrefix(p)+"�a Your oponent has requested to crumble the arena�7(�e"+por + "%�7)�a, type �b/crumble " 
							+ por + "�a to do it.");
					}
			}
			
			for (Player p1 : g.getPlayer1()) {
				
				if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
					p1.sendMessage("�3"+getGamePrefix(p)+"�6 Has solicitado jugar hasta �e" + por);
					} else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
						p1.sendMessage("�3"+getGamePrefix(p)+"�6 You have requested to play to �e" + por);
					}
			}
			
			
			
		} else if (g.getPlayer2().contains(p)) {
			
			for (Player p1 : g.getPlayer1()) {
				if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
					p1.sendMessage("�3"+getGamePrefix(p)+"�aTu oponente ha solicitado crumblear la arena �7(�e"+por + "%�7)�a, coloca �b/crumble " 
							+ por + "�a para hacerlo.");
					} else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
						p1.sendMessage("�3"+getGamePrefix(p)+"�a Your oponent has requested to crumble the arena�7(�e"+por + "%�7)�a, type �b/crumble " 
							+ por + "�a to do it.");
					}
				}
				
				for (Player p2 : g.getPlayer2()) {
					
					if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
						p2.sendMessage("�3"+getGamePrefix(p)+"�6 Has solicitado jugar hasta �e" + por);
						} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
							p2.sendMessage("�3"+getGamePrefix(p)+"�6 You have requested to play to �e" + por);
						}
				}
		}
		
		
			
	}
	
}

public void endgameRequest(Player p) {
	Game g = GameManager.getManager().getArenabyPlayer(p);
	if (!g.getEndGame().contains(p)) {
	g.getEndGame().add(p);
	}
	if (g.getPlayer1().size() + g.getPlayer2().size() <= g.getEndGame().size()) {
		SpleefGame.gameOver(null, null, g.getId());
		for (Player p1 : g.getPlayer1()) {
			if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
			p1.sendMessage("�3"+getGamePrefix(p)+"�6 La partida ha finalizado en empate");
			} else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
				p1.sendMessage("�3"+getGamePrefix(p)+"�6 The match has finished in a draw");
			}
		}
		for (Player p2 : g.getPlayer2()) {
			if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
			p2.sendMessage("�3"+getGamePrefix(p)+"�6 La partida ha finalizado en empate");
			} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
				p2.sendMessage("�3"+getGamePrefix(p)+"�6 The match has finished in a draw");
			}
		}
		g.getEndGame().clear();
		
	} else {
			if (DataManager.getLang(p).equalsIgnoreCase("ESP")) {
			p.sendMessage("�3"+getGamePrefix(p)+"�6 Has solicitado finalizar la partida");
			} else if (DataManager.getLang(p).equalsIgnoreCase("ENG")) {
				p.sendMessage("�3"+getGamePrefix(p)+"�6 You have requested to end the match");
			}
			if (g.getPlayer1().contains(p)) {
				Player p2 = GameManager.getManager().getArenabyPlayer(p).getPlayer2().get(0);
				if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
				p2.sendMessage("�3"+getGamePrefix(p)+" �aTu oponente ha solicitado finalizar la partida, coloca �b/endgame �apara terminarala.");
				} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
					p2.sendMessage("�3"+getGamePrefix(p)+"�a Your oponent has requested to end the match, type �b/endgame �a to finish it.");
				}
			} else if (g.getPlayer2().contains(p)) {
				Player p1 = GameManager.getManager().getArenabyPlayer(p).getPlayer1().get(0);
				if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
				p1.sendMessage("�3"+getGamePrefix(p)+" �aTu oponente ha solicitado finalizar la partida, coloca �b/endgame �apara terminarala.");
				} else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
					p1.sendMessage("�3"+getGamePrefix(p)+"�a Your oponent has requested to end the match, type �b/endgame �a to finish it.");
				}
			}
				
		
	}
	
}
public void playToRequest(Player p, Integer por) {
	Game g = GameManager.getManager().getArenabyPlayer(p);
	if (!g.getPlayTo().containsKey(p)) {
	g.getPlayTo().put(p, por);
	}
	
	if ((g.getPlayer1().size() + g.getPlayer2().size()) <= g.getPlayTo().size()) {
		
		for (Integer i : g.getPlayTo().values()) {
			if (!(por == i)) {
				break;
			}
			
			g.setWin(por);
			g.getPlayTo().clear();
			
			for (Player p1 : g.getPlayer1()) {
				if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
				p1.sendMessage("�3"+getGamePrefix(p)+"�6 Ultimo punto seteado a: �a" + por);
				} else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
					p1.sendMessage("�3"+getGamePrefix(p)+"�6 Last point set to : �a" + por);
				}
			}
			for (Player p2 : g.getPlayer2()) {
				if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
				p2.sendMessage("�3"+getGamePrefix(p)+"�6 Ultimo punto seteado a: �a" + por);
				} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
					p2.sendMessage("�3"+getGamePrefix(p)+"�6 Last point set to : �a" + por);
				}
			}
			
			return;
			
		}
		
			if (g.getPlayer1().contains(p)) {
				
				for (Player p2 : g.getPlayer2()) {
				if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
				p2.sendMessage("�3"+getGamePrefix(p)+"�aTu oponente ha solicitado jugar hasta �b" + por + "�a, coloca /playto " + por 
						+ " para aceptarlo."); 
				} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
					p2.sendMessage("�3"+getGamePrefix(p)+"�a Your oponent has requested to play to �b" +  por + "�a, type /playto " + por 
						+ " to accept it."); 
				}
				}
				
				for (Player p1 : g.getPlayer1()) {
					
					if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
						p1.sendMessage("�3"+getGamePrefix(p)+"�6 Has solicitado jugar hasta �e" + por);
						} else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
							p1.sendMessage("�3"+getGamePrefix(p)+"�6 You have requested to play to �e" + por);
						}
				}
				
				
				
			} else if (g.getPlayer2().contains(p)) {
				
				for (Player p1 : g.getPlayer1()) {
					if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
					p1.sendMessage("�3"+getGamePrefix(p)+"�aTu oponente ha solicitado jugar hasta �b" + por + "�a, coloca /playto " + por 
							+ "para aceptarlo."); 
					} else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
						p1.sendMessage("�3"+getGamePrefix(p)+"�a Your oponent has requested to play to �b" +  por + "�a, type /playto " + por 
							+ "to accept it."); 
					}
					}
					
					for (Player p2 : g.getPlayer2()) {
						
						if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
							p2.sendMessage("�3"+getGamePrefix(p)+"�6 Has solicitado jugar hasta �e" + por);
							} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
								p2.sendMessage("�3"+getGamePrefix(p)+"�6 You have requested to play to �e" + por);
							}
					}
			}
		
			g.getPlayTo().put(p, por);
		
			
		
		
	} else {
		if (g.getPlayer1().contains(p)) {
			
			for (Player p2 : g.getPlayer2()) {
			if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
			p2.sendMessage("�3"+getGamePrefix(p)+"�aTu oponente ha solicitado jugar hasta �b" + por + "�a, coloca /playto " + por 
					+ "para aceptarlo."); 
			} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
				p2.sendMessage("�3"+getGamePrefix(p)+"�a Your oponent has requested to play to �b" +  por + "�a, type /playto " + por 
					+ "to accept it."); 
			}
			}
			
			for (Player p1 : g.getPlayer1()) {
				
				if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
					p1.sendMessage("�3"+getGamePrefix(p)+"�6 Has solicitado jugar hasta �e" + por);
					} else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
						p1.sendMessage("�3"+getGamePrefix(p)+"�6 You have requested to play to �e" + por);
					}
			}
			
			
			
		} else if (g.getPlayer2().contains(p)) {
			
			for (Player p1 : g.getPlayer1()) {
				if (DataManager.getLang(p1).equalsIgnoreCase("ESP")) {
				p1.sendMessage("�3"+getGamePrefix(p)+"�aTu oponente ha solicitado jugar hasta �b" + por + "�a, coloca /playto " + por 
						+ "para aceptarlo."); 
				} else if (DataManager.getLang(p1).equalsIgnoreCase("ENG")) {
					p1.sendMessage("�3"+getGamePrefix(p)+"�a Your oponent has requested to play to �b" +  por + "�a, type /playto " + por 
						+ "to accept it."); 
				}
				}
				
				for (Player p2 : g.getPlayer2()) {
					
					if (DataManager.getLang(p2).equalsIgnoreCase("ESP")) {
						p2.sendMessage("�3"+getGamePrefix(p)+"�6 Has solicitado jugar hasta �e" + por);
						} else if (DataManager.getLang(p2).equalsIgnoreCase("ENG")) {
							p2.sendMessage("�3"+getGamePrefix(p)+"�6 You have requested to play to �e" + por);
						}
				}
		}
		
		
			
	}
	
}


public List<Location> saveArenaBlocks(Location l1, Location l2) {
	  List<Location> arenablocks = new ArrayList<Location>();
	  for (int x = l1.getBlockX(); x <= l2.getBlockX(); x++) {
        for (int y = l1.getBlockY(); y <= l2.getBlockY(); y++) {
            for (int z = l1.getBlockZ(); z <= l2.getBlockZ(); z++) {
               Location block = new Location(l1.getWorld(),x,y,z);
                if (block.getBlock().getType().equals(Material.SNOW_BLOCK)) {
                	arenablocks.add(block);        	
                } 
            }
        }
    }
    return arenablocks;
}




public String getGamePrefix(Player p) {
	if (GameManager.getManager().isInGame(p)) {
		Game g = GameManager.getManager().getArenabyPlayer(p);
		if (g.getType().equalsIgnoreCase("spleef") || g.getType().equalsIgnoreCase("spleef2v2")) {
			return "[Spleef]";
		} else if (g.getType().equalsIgnoreCase("bowspleef")) {
			return "[BowSpleef]";
		} else if (g.getType().equalsIgnoreCase("BuildSpleefPvP")) {
			return "[BuildSpleefPvP]";
		} else if (g.getType().equalsIgnoreCase("ffaspleef")) {
			return "[FFASpleef]";
		}
	}
	return "[Spleef]";
	
}



    
}
