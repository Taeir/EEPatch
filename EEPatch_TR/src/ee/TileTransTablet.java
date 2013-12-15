package ee;

import ee.core.GuiIds;
import net.minecraft.server.*;

public class TileTransTablet extends TileEE {

	public TileTransTablet() {}

	public int getTextureForSide(int var1) {
		return var1 != 0 ? var1 != 1 ? EEBase.transTabletSide : EEBase.transTabletTop : EEBase.transTabletBottom;
	}

	public int getInventoryTexture(int var1) {
		return var1 != 1 ? EEBase.transTabletSide : EEBase.transTabletTop;
	}

	public boolean onBlockActivated(EntityHuman var1) {
		if (!world.isStatic) var1.openGui(mod_EE.getInstance(), GuiIds.TRANS_TABLE, world, x, y, z);
		return true;
	}
}
