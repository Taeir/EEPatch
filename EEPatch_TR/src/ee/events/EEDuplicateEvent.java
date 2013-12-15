package ee.events;

import net.minecraft.server.ItemStack;
import ee.events.EEEnums.DuplicateType;

public class EEDuplicateEvent extends EEEvent {
	protected DuplicateType type;
	protected ItemStack item;
	public EEDuplicateEvent(ItemStack item, DuplicateType type){
		this.type = type;
		this.item = item;
	}
	
	public ItemStack getItemStack(){
		return item;
	}
	
	public DuplicateType getDuplicateType(){
		return type;
	}
}
