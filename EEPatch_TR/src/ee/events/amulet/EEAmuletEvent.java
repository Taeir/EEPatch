package ee.events.amulet;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import ee.events.EEEvent;
import ee.events.EEEnums.*;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class EEAmuletEvent extends EEEvent {
	protected ItemStack amulet;
	protected EEAction action;
	protected EntityHuman human;
	protected EEAmuletAction extra;
	protected double x, y, z;
	protected World world;

	public EEAmuletEvent(ItemStack amulet, EEAction action, EntityHuman human, EEAmuletAction extra){
		this.amulet = amulet;
		this.action = action;
		this.human = human;
		this.extra = extra;
		this.world = human.world;
		this.x = human.locX;
		this.y = human.locY;
		this.z = human.locZ;
	}

	public EEAmuletEvent(ItemStack amulet, EEAction action, EntityHuman human, int x, int y, int z, EEAmuletAction extra) {
		this.action = action;
		this.amulet = amulet;
		this.human = human;
		this.extra = extra;
		this.world = human.world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public EEAction getAction() {
		return action;
	}

	public ItemStack getAmulet() {
		return amulet;
	}

	public EntityHuman getHuman(){
		return human;
	}
	
	public Player getPlayer(){
		if (human == null) return null;
		return (Player) human.getBukkitEntity();
	}
	
	public EEAmuletAction getExtraInfo(){
		return extra;
	}
	
	public void setExtraInfo(EEAmuletAction extra){
		this.extra = extra;
	}
	
	public Location getLocation(){
		return new Location(world.getWorld(), x, y, z);
	}
}
