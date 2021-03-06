package cz.dubcat.xpboost.api;

import java.util.UUID;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI.Condition;
import cz.dubcat.xpboost.api.MainAPI.Debug;
import cz.dubcat.xpboost.constructors.GlobalBoost;
import cz.dubcat.xpboost.constructors.XPBoost;

public class xpbAPI {
	
	
	public static boolean hasBoost(UUID id){		
		if(Main.allplayers.containsKey(id))return true;
					
		return false;	
	}
	
	public static XPBoost getBoost(UUID id){
		if(Main.allplayers.containsKey(id)){
			return Main.allplayers.get(id);
		}else{
			return null;
		}
	}
	
	
	@Deprecated
	public static double getPlayerBoost(UUID id){
		XPBoost xpb = getBoost(id);
		
		if(xpb != null){
			return xpb.getBoost();
		}else{
			return 1;
		}
	}
	
	@Deprecated
	public static void clearBoost(UUID id){
		XPBoost xpb = getBoost(id);
		if(xpb != null){
			xpb.clear();
			MainAPI.debug("Clearing boost for UUID " + id , Debug.NORMAL);
		}
	}
	
	@Deprecated
	public static double getTimeRemaining(UUID id){
		XPBoost xpb = getBoost(id);
		
		if(xpb != null){
			return xpb.getTimeRemaining();
		}else{
			return 0;
		}
	}
	
	@Deprecated
	public static void addPlayerBoost(UUID id, double boost, int time){
		XPBoost xpb = getBoost(id);
		if(xpb != null){
			xpb.addBoost(boost, time*1000);
			MainAPI.debug("Adding boost of "+boost +"x for UUID " + id +" endTime: " + (time*1000) , Debug.NORMAL);
		}else{
			XPBoost newxpb = new XPBoost(id, boost, (System.currentTimeMillis() + time*1000));
			Main.allplayers.put(id, newxpb);
			MainAPI.debug("Creating new boost "+boost +"x for UUID " + id , Debug.NORMAL);
		}
	}
	
	public static XPBoost setPlayerBoost(UUID id, double boost, int time){
		XPBoost xpb = getBoost(id);
		if(xpb != null){
			if(xpb.getBoost() == boost){
				xpb.addBoost(boost, time*1000);
				MainAPI.debug("Adding boost of "+boost +"x for UUID " + id +" endTime: " + (time*1000) , Debug.NORMAL);
				MainAPI.debug("Player's boost time has been extended UUID " + xpb.getUUID(), Debug.NORMAL);
				return xpb;
			}
			
			xpb.setBoost(boost, System.currentTimeMillis() + time*1000);
			MainAPI.debug("Setting boost of "+boost +"x for UUID " + id +" endTime: " + (System.currentTimeMillis() + time*1000) , Debug.NORMAL);
		}else{
			xpb = new XPBoost(id, boost, (System.currentTimeMillis() + time*1000));
			Main.allplayers.put(id, xpb);
			MainAPI.debug("Creating new boost "+boost +"x for UUID " + id , Debug.NORMAL);
		}
		
		return xpb;
	}
	
	public static GlobalBoost getGlobalBoost(){
		return Main.GLOBAL_BOOST;
	}
	
	@Deprecated
	public static void setBoostCondition(UUID id, Condition cond, boolean bol){
		XPBoost xpb = getBoost(id);
		if(xpb != null){
			if(!xpb.getConditions().contains(cond)){
				xpb.putCondition(cond, bol);
				
				MainAPI.debug("Adding " + cond + " condition to UUID " + id , Debug.NORMAL);
			}
		}
	}
	
	//dont forget to save after changes.
	public static XPBoost getOfflineBoost(UUID id){
		MainAPI.setPlayerFile(id);
		
		double boost = 0;
		long endtime = 0;
		
		if(MainAPI.players.contains("xp.boost")){
			boost = (double) MainAPI.getPlayerVariable("xp.boost");
		}
		
		if(MainAPI.players.contains("xp.endtime")){
			endtime = (long) MainAPI.getPlayerVariable("xp.endtime");
		}
		
		if(boost != 0 && endtime != 0)
			return new XPBoost(id, boost, endtime);
		else
			return null;
	}
	

}
