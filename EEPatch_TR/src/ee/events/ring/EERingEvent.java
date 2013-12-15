package ee.events.ring;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

import ee.events.EEEvent;
import ee.events.EEEnums.*;

public class EERingEvent extends EEEvent {
	protected ItemStack ring;
	protected EEAction action;
	protected EntityHuman human;
	protected EERingAction extra;
	protected double x, y, z;
	protected World world;
	
	public EERingEvent(ItemStack ring, EEAction action, EntityHuman human, EERingAction extra) {
		this.action = action;
		this.ring = ring;
		this.human = human;
		this.extra = extra;
		this.world = human.world;
		this.x = human.locX;
		this.y = human.locY;
		this.z = human.locZ;
	}
	
	public EERingEvent(ItemStack ring, EEAction action, EntityHuman human, int x, int y, int z, EERingAction extra) {
		this.action = action;
		this.ring = ring;
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

	public ItemStack getRing() {
		return ring;
	}

	public EntityHuman getHuman(){
		return human;
	}
	
	public Player getPlayer(){
		if (human == null) return null;
		return (Player) human.getBukkitEntity();
	}
	
	public EERingAction getExtraInfo(){
		return extra;
	}
	
	public void setExtraInfo(EERingAction extra){
		this.extra = extra;
	}
	
	public Location getLocation(){
		return new Location(world.getWorld(), x, y, z);
	}
}
