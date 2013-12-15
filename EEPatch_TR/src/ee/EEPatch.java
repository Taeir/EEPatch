package ee;

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
	public static final double version = 1.6;
	
	public static EEProps InitProps(EEProps props)
	{
		if (props.getInt("EEPatch_ApplyCondenserSidePatch", 1) == 1){
			TileCondenser.applySidePatch = true;
		}
		return props;
	}
	
	public static boolean attemptBreak(EntityHuman player, int x, int y, int z)
	{
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

	public static boolean attemptPlace(EntityHuman player, int x, int y, int z)
	{
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
