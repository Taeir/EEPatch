package ee.events.other;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;
import ee.events.EEEnums.EEAction;
import ee.events.EEEnums.EEWatchAction;
import ee.events.EEEvent;

public class EEWOFTEvent extends EEEvent {
	protected ItemStack tool;
	protected EEAction action;
	protected EntityHuman human;
	protected EEWatchAction extra;
	protected double x, y, z;
	protected World world;
	
	public EEWOFTEvent(ItemStack tool, EEAction action, EntityHuman human, EEWatchAction extra) {
		this.action = action;
		this.tool = tool;
		this.human = human;
		this.extra = extra;
		this.world = human.world;
		this.x = human.locX;
		this.y = human.locY;
		this.z = human.locZ;
	}
	
	public EEWOFTEvent(EEAction action, EntityHuman human, EEWatchAction extra) {
		this.action = action;
		this.tool = null;
		this.human = human;
		this.extra = extra;
		this.world = human.world;
		this.x = human.locX;
		this.y = human.locY;
		this.z = human.locZ;
	}
	
	public EEWOFTEvent(ItemStack tool, EEAction action, EntityHuman human, int x, int y, int z, EEWatchAction extra) {
		this.action = action;
		this.tool = tool;
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

	/**
	 * @return The watch, null if not available
	 */
	public ItemStack getTool() {
		return tool;
	}

	public EntityHuman getHuman(){
		return human;
	}
	
	public Player getPlayer(){
		if (human == null) return null;
		return (Player) human.getBukkitEntity();
	}
	
	public EEWatchAction getExtraInfo(){
		return extra;
	}
	
	public void setExtraInfo(EEWatchAction extra){
		this.extra = extra;
	}
	
	public Location getLocation(){
		return new Location(world.getWorld(), x, y, z);
	}
}
