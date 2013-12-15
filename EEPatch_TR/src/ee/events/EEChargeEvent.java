package ee.events;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;

public class EEChargeEvent extends EEEvent {
	private ItemStack item;
	private EntityHuman human;
	private int oldlevel, newlevel;
	private int maxlevel;
	public EEChargeEvent (ItemStack item, EntityHuman human, int oldlevel, int newlevel, int maxlevel){
		super();
		this.item = item;
		this.human = human;
		this.oldlevel = oldlevel;
		this.newlevel = newlevel;
		this.maxlevel = maxlevel;
	}
	
	public int getOldChargeLevel(){
		return oldlevel;
	}
	public int getNewChargeLevel(){
		return newlevel;
	}
	public int getMaxChargeLevel(){
		return maxlevel;
	}
	public EntityHuman getHuman(){
		return human;
	}
	public ItemStack getItem(){
		return item;
	}
	
}
