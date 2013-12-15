package ee.events.armor;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.World;
import ee.events.EEEvent;
import ee.events.EEEnums.EEAction;
import ee.events.EEEnums.EEArmorAction;

public class EEArmorEvent extends EEEvent {
	protected EntityHuman human;
	protected double x, y, z;
	protected World world;
	protected EEAction action;
	protected EEArmorAction extra;
	
	public EEArmorEvent(EntityHuman human, EEAction action, EEArmorAction extra){
		this.human = human;
		this.action = action;
		this.extra = extra;
		this.world = human.world;
		this.x = human.locX;
		this.y = human.locY;
		this.z = human.locZ;
	}
	
	public EEAction getAction() {
		return action;
	}
	
	public EntityHuman getHuman(){
		return human;
	}
	
	public Player getPlayer(){
		if (human == null) return null;
		return (Player) human.getBukkitEntity();
	}
	
	public Location getLocation(){
		return new Location(world.getWorld(), x, y, z);
	}
	
	public EEArmorAction getExtraInfo(){
		return extra;
	}
}
