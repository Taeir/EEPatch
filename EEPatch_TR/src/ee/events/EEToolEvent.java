package ee.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

import ee.events.EEEnums.*;

public class EEToolEvent extends EEEvent {
	protected ItemStack tool;
	protected EEAction action;
	protected EntityHuman human;
	protected EEAction2 extra;
	protected double x, y, z;
	protected World world;
	
	public EEToolEvent(ItemStack tool, EEAction action, EntityHuman human, EEAction2 extra) {
		this.action = action;
		this.tool = tool;
		this.human = human;
		this.extra = extra;
		this.world = human.world;
		this.x = human.locX;
		this.y = human.locY;
		this.z = human.locZ;
	}
	
	public EEToolEvent(ItemStack tool, EEAction action, EntityHuman human, int x, int y, int z, EEAction2 extra) {
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
	
	/**
	 * Common extra info:<br>
	 * BreakRadius, TillRadius<br>
	 * TallBreak, WideBreak, LongBreak, MegaBreak (hammer)<br>
	 * UpdateToolMode, UpdateHammerMode<br>
	 * Shear
	 * @return
	 */
	public EEAction2 getExtraInfo(){
		return extra;
	}
	
	public void setExtraInfo(EEAction2 extra){
		this.extra = extra;
	}
	
	public Location getLocation(){
		return new Location(world.getWorld(), x, y, z);
	}
}
