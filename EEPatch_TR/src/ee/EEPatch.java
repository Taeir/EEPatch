package ee;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import net.minecraft.server.EntityHuman;

public class EEPatch {
	public static final double version = 1.81;//1.8
	public static int alcBagAmount = 15;
	public static boolean allowAlcBags = true;
	public static boolean separateAlcBags = false;
	public static boolean applySidePatch = false;
	public static ArrayList<String> sharedWorlds = new ArrayList<String>();
	public static String mainSharedWorld = "";
	public static int transTableInterval;
	
	public static EEProps InitProps(EEProps props) {
		int alcbag = props.getInt("EEPatch_AllowAlchemyBags", 1);
		if (alcbag == 0){
			allowAlcBags = false;
			alcBagAmount = 0;
		} else {
			alcBagAmount = EEBase.props.getInt("EEPatch_AlchemyBagAmount", 16)-1;
		}
		separateAlcBags = props.getInt("EEPatch_SeparateAlcBagsPerWorld", 0) == 1;
		
		String shared = props.func_26599_getString("EEPatch_AlcBag_Shared_Worlds", "");
		if (!shared.isEmpty()){
			String[] w = shared.split(",");
			mainSharedWorld = w[0].trim();
			for (String s : w){
				sharedWorlds.add(s.trim().toLowerCase());
			}
		}
		applySidePatch = props.getInt("EEPatch_ApplyCondenserSidePatch", 1) == 1;
		transTableInterval = props.getInt("EEPatch_TransmutionTableInterval", 10);

		return props;
	}
	
	public static boolean attemptBreak(EntityHuman player, int x, int y, int z){
		if (player == null) return false;
		
		CraftWorld craftWorld = player.world.getWorld();
		CraftServer craftServer = player.world.getServer();
		Block block = craftWorld.getBlockAt(x, y, z);
		if(block == null) return false;
		Player ply = (Player) player.getBukkitEntity();
		//Player ply = craftServer.getPlayer((EntityPlayer)player);
		if(ply != null)
		{
			BlockBreakEvent event = new BlockBreakEvent(block, ply);
			craftServer.getPluginManager().callEvent(event);
			return !event.isCancelled();
		}
		return false;
	}

	public static boolean attemptPlace(EntityHuman player, int x, int y, int z) {
		if(player == null) return false;
		CraftWorld craftWorld = player.world.getWorld();
		CraftServer craftServer = player.world.getServer();
		Player ply = (Player) player.getBukkitEntity();

		BlockState state = CraftBlockState.getBlockState(player.world, x, y, z);
		Block placedagainst = craftWorld.getBlockAt(x, y - 1, z);
		
		if(ply != null)
		{
			BlockPlaceEvent event = new BlockPlaceEvent(state.getBlock(), state, placedagainst, ply.getItemInHand(), ply, true);
			craftServer.getPluginManager().callEvent(event);
			return !event.isCancelled();
		}
		return false;
	}
	
}
