package nl.taico.fixpack.ee;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TRUtil {
	public static class world {
		public static void setAir(int x, int y, int z, World world){
			world.setTypeId(x, y, z, 0);
		}
		
		public static void setTypeIdAndData(World world, int x, int y, int z, int id, int data){
			world.setTypeIdAndData(x, y, z, id, data);
		}
	}
	
	public static class entityHuman {
		public static ItemStack getItemInHand(EntityHuman human){
			return human.U();
		}
	}
	
	private static Player getPlayer(EntityHuman human){
		if (human == null) return null;
		Player player = (Player) human.getBukkitEntity();
		return player;
	}
	
	@SuppressWarnings("unused")
	private static boolean hasPerm(EntityHuman human, String perm){
		if (human == null) return false;
		Player player = (Player) human.getBukkitEntity();
		return player.hasPermission(perm);
	}
	
	private static boolean hasMeta(Object plr, String key){
		if (plr == null) return false;
		Player player;
		
		if (plr instanceof EntityHuman)
			player = (Player) ((EntityHuman) plr).getBukkitEntity();
		else if (plr instanceof Player)
			player = (Player) plr;
		else
			return false;
		
		return player.hasMetadata(key);
	}

	@Deprecated
	public static boolean isAllowed(Object player, String key){
		return !hasMeta(player, key);
	}
	
	public static boolean isAllowed(Object plr, String key, String perm){
		if (plr == null) return false;
		Player player;
		if (plr instanceof EntityHuman)
			player = getPlayer((EntityHuman) plr);
		else if (plr instanceof Player)
			player = (Player) plr;
		else
			return false;
		
		if (hasMeta(player, "TekkitRestrict_"+key) || player.hasPermission("tekkitrestrict."+perm))
			return false;
		return true;
	}
	
	/**
	 * @return If this returns true, the method that called it should return.
	 */
	public static boolean checkAndBlock(EntityHuman human, String key, String perm){
		Player player = getPlayer(human);
		if (!isAllowed(player, key, perm)){
			player.sendMessage(ChatColor.RED + "You are not allowed to use this feature.");
			return true;
		}
		return false;
	}

	public static void setDataAndUpdate(EntityHuman human, int x, int y, int z, int id, int data){
		human.world.setData(x, y, z, data);
		Player player = getPlayer(human);
		player.sendBlockChange(new Location(player.getWorld(), x, y, z), id, (byte) data);
	}
}
