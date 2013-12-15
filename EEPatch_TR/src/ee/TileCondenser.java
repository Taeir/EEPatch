package ee;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.HumanEntity;

import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockContainer;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.TileEntityChest;
import net.minecraft.server.mod_EE;
import buildcraft.api.ISpecialInventory;
import buildcraft.api.Orientations;
import ee.core.GuiIds;
import ee.item.ItemLootBall;
import forge.ISidedInventory;

public class TileCondenser extends TileEE implements ISpecialInventory, ISidedInventory, IEEPowerNet {
	private ItemStack items[];
	public int scaledEnergy;
	public float lidAngle;
	public float prevLidAngle;
	public int numUsingPlayers;
	private int ticksSinceSync;
	private int eternalDensity;
	private boolean condenseOn;
	private boolean initialized;
	public int displayEnergy;
	public int currentItemProgress;
	private boolean attractionOn;

	public TileCondenser() {
		items = new ItemStack[92];
		scaledEnergy = 0;
		displayEnergy = 0;
		currentItemProgress = 0;
	}

	private boolean isChest(TileEntity var1) {
		return var1 instanceof TileEntityChest || var1 instanceof TileAlchChest;
	}

	@SuppressWarnings("null")
	public void onBlockRemoval() {
		for (HumanEntity h : this.getViewers()) h.closeInventory();
		//close all inventories upon removal.
		for (int var1 = 0; var1 < getSize(); var1++) {
			ItemStack var2 = getItem(var1);
			if (var2 != null) {
				float var3 = world.random.nextFloat() * 0.8F + 0.1F;
				float var4 = world.random.nextFloat() * 0.8F + 0.1F;
				float var5 = world.random.nextFloat() * 0.8F + 0.1F;
				while (var2.count > 0) {
					int var6 = world.random.nextInt(21) + 10;
					if (var6 > var2.count) var6 = var2.count;
					var2.count -= var6;
					EntityItem var7 = new EntityItem(world, x + var3, y + var4, z + var5, new ItemStack(var2.id, var6, var2.getData()));
					if (var7 != null) {
						float var8 = 0.05F;
						var7.motX = (float) world.random.nextGaussian() * var8;
						var7.motY = (float) world.random.nextGaussian() * var8 + 0.2F;
						var7.motZ = (float) world.random.nextGaussian() * var8;
						if (var7.itemStack.getItem() instanceof ItemKleinStar)
							((ItemKleinStar) var7.itemStack.getItem()).setKleinPoints(var7.itemStack, ((ItemKleinStar) var2.getItem()).getKleinPoints(var2));
						world.addEntity(var7);
					}
				}
			}
		}

	}

	public static boolean putInChest(TileEntity var0, ItemStack var1) {
		if (var1 != null && var1.id != 0) {
			if (var0 == null) return false;
			if (var0 instanceof TileEntityChest) {
				for (int var2 = 0; var2 < ((TileEntityChest) var0).getSize(); var2++) {
					ItemStack var3 = ((TileEntityChest) var0).getItem(var2);
					if (var3 != null && var3.doMaterialsMatch(var1) && var3.count + var1.count <= var3.getMaxStackSize()) {
						var3.count += var1.count;
						return true;
					}
				}

				for (int var2 = 0; var2 < ((TileEntityChest) var0).getSize(); var2++)
					if (((TileEntityChest) var0).getItem(var2) == null) {
						((TileEntityChest) var0).setItem(var2, var1);
						return true;
					}

			} else if (var0 instanceof TileAlchChest) {
				for (int var2 = 0; var2 < ((TileAlchChest) var0).getSize(); var2++) {
					ItemStack var3 = ((TileAlchChest) var0).getItem(var2);
					if (var3 != null && var3.doMaterialsMatch(var1) && var3.count + var1.count <= var3.getMaxStackSize() && var3.getData() == var1.getData()) {
						var3.count += var1.count;
						return true;
					}
				}

				for (int var2 = 0; var2 < ((TileAlchChest) var0).getSize(); var2++)
					if (((TileAlchChest) var0).getItem(var2) == null) {
						((TileAlchChest) var0).setItem(var2, var1);
						return true;
					}

			}
			return false;
		}

		return true;
	}

	public boolean tryDropInChest(ItemStack var1) {
		TileEntity var2 = null;
		if (isChest(world.getTileEntity(x, y + 1, z))) {
			var2 = world.getTileEntity(x, y + 1, z);
			return putInChest(var2, var1);
		}
		if (isChest(world.getTileEntity(x, y - 1, z))) {
			var2 = world.getTileEntity(x, y - 1, z);
			return putInChest(var2, var1);
		}
		if (isChest(world.getTileEntity(x + 1, y, z))) {
			var2 = world.getTileEntity(x + 1, y, z);
			return putInChest(var2, var1);
		}
		if (isChest(world.getTileEntity(x - 1, y, z))) {
			var2 = world.getTileEntity(x - 1, y, z);
			return putInChest(var2, var1);
		}
		if (isChest(world.getTileEntity(x, y, z + 1))) {
			var2 = world.getTileEntity(x, y, z + 1);
			return putInChest(var2, var1);
		}
		if (isChest(world.getTileEntity(x, y, z - 1))) {
			var2 = world.getTileEntity(x, y, z - 1);
			return putInChest(var2, var1);
		}

		return false;
	}

	int itt = 0;

	public void doCondense(ItemStack var1) {
		itt++;
		if (itt == 3) {
			itt = 0;
			return;
		}

		if (eternalDensity != -1) {
			int var2 = 0;
			for (int var3 = 1; var3 < items.length; var3++){
				if (items[var3] == null) continue;
				if (!isValidMaterial(items[var3])) continue;
				int emc = EEMaps.getEMC(items[var3]);
				if (emc > var2) var2 = emc;
			}

			for (int var3 = 1; var3 < items.length; var3++){
				if (items[var3] == null) continue;
				if (!isValidMaterial(items[var3])) continue;
				int emc = EEMaps.getEMC(items[var3]);
				if (emc < var2) var2 = emc;
			}
			int emc;
			if (var2 < (emc = EEMaps.getEMC(EEItem.redMatter.id)) && !AnalyzeTier(items[eternalDensity], emc)
			 && var2 < (emc = EEMaps.getEMC(EEItem.darkMatter.id)) && !AnalyzeTier(items[eternalDensity], emc)
			 && var2 < (emc = EEMaps.getEMC(Item.DIAMOND.id)) && !AnalyzeTier(items[eternalDensity], emc)
			 && var2 < (emc = EEMaps.getEMC(Item.GOLD_INGOT.id)) && !AnalyzeTier(items[eternalDensity], emc)
			 && var2 < (emc = EEMaps.getEMC(Item.IRON_INGOT.id)))
				if (!AnalyzeTier(items[eternalDensity], emc)) ;
		}
	}

	private boolean AnalyzeTier(ItemStack var1, int var2) {
		if (var1 == null) return false;
		int var3 = 0;
		for (int var4 = 0; var4 < items.length; var4++) {
			if (items[var4] == null || !isValidMaterial(items[var4])) continue;
			int emc = EEMaps.getEMC(items[var4]);
			if (emc < var2)
				var3 += emc * items[var4].count;
		}

		if (var3 + emc(var1) < var2) return false;
		for (int var4 = 0; var3 + emc(var1) >= var2 && var4 < 10; ConsumeMaterialBelowTier(var1, var2))
			var4++;

		if (emc(var1) >= var2 && roomFor(getProduct(var2))) {
			PushStack(getProduct(var2));
			takeEMC(var1, var2);
		}
		return true;
	}

	private boolean roomFor(ItemStack var1) {
		if (var1 == null) return false;
		for (int var2 = 1; var2 < items.length; var2++) {
			if (items[var2] == null) return true;
			if (items[var2].doMaterialsMatch(var1) && items[var2].count <= var1.getMaxStackSize() - var1.count) return true;
		}

		return false;
	}

	private ItemStack getProduct(int var1) {
		if (var1 != EEMaps.getEMC(Item.IRON_INGOT.id)){
			if (var1 != EEMaps.getEMC(Item.GOLD_INGOT.id)){
				if (var1 != EEMaps.getEMC(Item.DIAMOND.id)){
					if (var1 != EEMaps.getEMC(EEItem.darkMatter.id)){
						if (var1 != EEMaps.getEMC(EEItem.redMatter.id)){
							return null;
						} else {
							return new ItemStack(EEItem.redMatter, 1);
						}
					} else {
						return new ItemStack(EEItem.darkMatter, 1);
					}
				} else {
					return new ItemStack(Item.DIAMOND, 1);
				}
			} else {
				return new ItemStack(Item.GOLD_INGOT, 1);
			}
		} else {
			return new ItemStack(Item.IRON_INGOT, 1);
		}
	}

	private void ConsumeMaterialBelowTier(ItemStack var1, int var2) {
		for (int var3 = 1; var3 < items.length; var3++){
			if (items[var3] == null || !isValidMaterial(items[var3])) continue;
			int emc = EEMaps.getEMC(items[var3]);
			if (emc < var2) {
				addEMC(var1, emc);
				//items[var3].count--;
				//if (items[var3].count == 0) items[var3] = null;
				
				if (items[var3].count == 1)
					items[var3] = null;
				else
					items[var3].count--;
				return;
			}
		}
	}

	private boolean isValidMaterial(ItemStack var1) {
		if (var1 == null) return false;
		if (EEMaps.getEMC(var1) == 0) return false;
		if (var1.getItem() instanceof ItemKleinStar) {
			return false;
		}
		int var2 = var1.id;
		return var2 != EEItem.redMatter.id ? var2 >= Block.byId.length || !(Block.byId[var2] instanceof BlockContainer)
				|| !Block.byId[var2].hasTileEntity(var1.getData()) : false;
	}

	private int emc(ItemStack var1) {
		return var1.getItem() instanceof ItemEternalDensity ? ((ItemEternalDensity) var1.getItem()).getInteger(var1, "emc")
				: var1.getItem() instanceof ItemVoidRing ? ((ItemVoidRing) var1.getItem()).getInteger(var1, "emc") : 0;
	}

	private void takeEMC(ItemStack var1, int var2) {
		if (var1.getItem() instanceof ItemEternalDensity || var1.getItem() instanceof ItemVoidRing)
			if (var1.getItem() instanceof ItemEternalDensity) ((ItemEternalDensity) var1.getItem()).setInteger(var1, "emc", emc(var1) - var2);
			else ((ItemVoidRing) var1.getItem()).setInteger(var1, "emc", emc(var1) - var2);
	}

	private void addEMC(ItemStack var1, int var2) {
		if (var1.getItem() instanceof ItemEternalDensity || var1.getItem() instanceof ItemVoidRing)
			if (var1.getItem() instanceof ItemEternalDensity) ((ItemEternalDensity) var1.getItem()).setInteger(var1, "emc", emc(var1) + var2);
			else ((ItemVoidRing) var1.getItem()).setInteger(var1, "emc", emc(var1) + var2);
	}

	public ItemStack target() {
		return items[0];
	}

	public int getTargetValue(ItemStack var1) {
		return var1 != null ? EEMaps.getEMC(var1.id, var1.getData()) != 0 ? EEMaps.getEMC(var1.id, var1.getData()) : var1.d() ? EEMaps.getEMC(var1.id)
				* (int) (((float) var1.i() - (float) var1.getData()) / var1.i()) : EEMaps.getEMC(var1.id) : 0;
	}

	public boolean canCondense() {
		return target() != null ? getTargetValue(target()) != 0 ? !isInventoryFull() || roomFor(target()) : false : false;
	}

	public boolean isInventoryFull() {
		for (int var1 = 0; var1 < items.length; var1++)
			if (items[var1] == null) return false;

		return true;
	}

	public boolean receiveEnergy(int var1, byte var2, boolean var3) {
		if (canCondense() && scaledEnergy + var1 <= 800000000) {
			if (var3) scaledEnergy += var1;
			return true;
		}

		return false;
	}

	public boolean sendEnergy(int var1, byte var2, boolean var3) {
		return false;
	}

	public boolean passEnergy(int var1, byte var2, boolean var3) {
		return false;
	}

	public void sendAllPackets(int i) {}

	public int relayBonus() {
		return 0;
	}

	public int getSize() {
		return items.length;
	}

	public int getMaxStackSize() {
		return 64;
	}

	public boolean addItem(ItemStack var1, boolean var2, Orientations var3) {
		if (var1 != null) {
			for (int var4 = 1; var4 < items.length; var4++) {
				if (items[var4] == null) {
					if (var2) {
						items[var4] = var1.cloneItemStack();
						for (; var1.count > 0; var1.count--);
					}
					return true;
				}
				if (!items[var4].doMaterialsMatch(var1) || items[var4].count >= items[var4].getMaxStackSize()) continue;
				if (var2) {
					for (; items[var4].count < items[var4].getMaxStackSize() && var1.count > 0; var1.count--)
						items[var4].count++;

					if (var1.count != 0) continue;
				} else {
					return true;
				}
			}

		}
		return false;
		/*
		switch (var3) {
			case Unknown:
			case XNeg:
			case XPos:
			case YNeg:
			case YPos:
			case ZNeg:
			case ZPos:
				if (var1 != null) {
					for (int var4 = 1; var4 < items.length; var4++) {
						if (items[var4] == null) {
							if (var2) {
								items[var4] = var1.cloneItemStack();
								for (; var1.count > 0; var1.count--);
							}
							return true;
						}
						if (!items[var4].doMaterialsMatch(var1) || items[var4].count >= items[var4].getMaxStackSize()) continue;
						if (var2) {
							for (; items[var4].count < items[var4].getMaxStackSize() && var1.count > 0; var1.count--)
								items[var4].count++;

							if (var1.count != 0) continue;
						} else {
							return true;
						}
					}

				}
				break;
		}
		return false;
		*/
	}

	public ItemStack extractItem(boolean var1, Orientations var2) {
		for (int var3 = 1; var3 < items.length; var3++){
			if (items[var3] != null && (target() == null || items[var3].doMaterialsMatch(target()))) {
				ItemStack var4 = items[var3].cloneItemStack();
				var4.count = 1;
				if (var1) {
					items[var3].count--;
					if (items[var3].count < 1) items[var3] = null;
				}
				return var4;
			}
		}
		return null;
		/*
		switch (var2) {
			case Unknown:
			case XNeg:
			case XPos:
			case YNeg:
			case YPos:
			case ZNeg:
			case ZPos:
				for (int var3 = 1; var3 < items.length; var3++)
					if (items[var3] != null && (target() == null || items[var3].doMaterialsMatch(target()))) {
						ItemStack var4 = items[var3].cloneItemStack();
						var4.count = 1;
						if (var1) {
							items[var3].count--;
							if (items[var3].count < 1) items[var3] = null;
						}
						return var4;
					}

				break;
		}
		return null;
		*/
	}

	public String getName() {
		return "Condenser";
	}

	public void a(NBTTagCompound var1) {
		super.a(var1);
		NBTTagList var2 = var1.getList("Items");
		items = new ItemStack[getSize()];
		for (int var3 = 0; var3 < var2.size(); var3++) {
			NBTTagCompound var4 = (NBTTagCompound) var2.get(var3);
			byte var5 = var4.getByte("Slot");
			if (var5 >= 0 && var5 < items.length) items[var5] = ItemStack.a(var4);
		}

		scaledEnergy = var1.getInt("scaledEnergy");
		eternalDensity = var1.getShort("eternalDensity");
	}

	public void b(NBTTagCompound var1) {
		super.b(var1);
		var1.setInt("scaledEnergy", scaledEnergy);
		var1.setShort("eternalDensity", (short) eternalDensity);
		NBTTagList var2 = new NBTTagList();
		for (int var3 = 0; var3 < items.length; var3++)
			if (items[var3] != null) {
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				items[var3].save(var4);
				var2.add(var4);
			}

		var1.set("Items", var2);
	}

	public ItemStack getItem(int var1) {
		return items[var1];
	}

	public ItemStack splitStack(int var1, int var2) {
		if (items[var1] != null) {
			ItemStack var3;
			if (items[var1].count <= var2) {
				var3 = items[var1];
				items[var1] = null;
				return var3;
			}
			var3 = items[var1].a(var2);
			if (items[var1].count == 0) items[var1] = null;
			return var3;
		}

		return null;
	}

	public void setItem(int var1, ItemStack var2) {
		items[var1] = var2;
		if (var2 != null && var2.count > getMaxStackSize()) var2.count = getMaxStackSize();
	}

	public void update() {
		super.update();
		boolean var1 = false;
		boolean var2 = false;
		for (int var3 = 0; var3 < getSize(); var3++)
			if (items[var3] != null) {
				if (items[var3].getItem() instanceof ItemVoidRing) {
					eternalDensity = var3;
					if ((items[var3].getData() & 1) == 0) {
						items[var3].setData(items[var3].getData() + 1);
						((ItemEECharged) items[var3].getItem()).setBoolean(items[var3], "active", true);
					}
					var1 = true;
					var2 = true;
				}
				if (items[var3].getItem().id == EEItem.eternalDensity.id) {
					eternalDensity = var3;
					if ((items[var3].getData() & 1) == 0) {
						items[var3].setData(items[var3].getData() + 1);
						((ItemEECharged) items[var3].getItem()).setBoolean(items[var3], "active", true);
					}
					var1 = true;
				}
				if (items[var3].getItem() instanceof ItemAttractionRing) {
					if ((items[var3].getData() & 1) == 0) {
						items[var3].setData(items[var3].getData() + 1);
						((ItemEECharged) items[var3].getItem()).setBoolean(items[var3], "active", true);
					}
					var2 = true;
				}
			}

		if (var1 != condenseOn) condenseOn = var1;
		if (var2 != attractionOn) attractionOn = var2;
	}

	public int getCondenserProgressScaled(int var1) {
		return getTargetValue(target()) != 0 ? scaledEnergy / 80 <= getTargetValue(target()) ? scaledEnergy / 80 * var1 / getTargetValue(target()) : var1 : 0;
	}

	public boolean isValidTarget() {
		return EEMaps.getEMC(items[0].id, items[0].getData()) == 0 ? EEMaps.getEMC(items[0].id) == 0 : true;
	}

	public void q_() {
		currentItemProgress = getCondenserProgressScaled(102);
		displayEnergy = latentEnergy();
		if (!initialized) {
			initialized = true;
			update();
		}
		if (++ticksSinceSync % 20 * 4 == 0) world.playNote(x, y, z, 1, numUsingPlayers);
		prevLidAngle = lidAngle;
		float var1 = 0.1F;
		if (numUsingPlayers > 0 && lidAngle == 0.0F) {
			double var4 = x + 0.5D;
			double var2 = z + 0.5D;
			world.makeSound(var4, y + 0.5D, var2, "random.chestopen", 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
		}
		if (numUsingPlayers == 0 && lidAngle > 0.0F || numUsingPlayers > 0 && lidAngle < 1.0F) {
			float var8 = lidAngle;
			if (numUsingPlayers > 0) lidAngle += var1;
			else lidAngle -= var1;
			if (lidAngle > 1.0F) lidAngle = 1.0F;
			float var5 = 0.5F;
			if (lidAngle < var5 && var8 >= var5) {
				double var2 = x + 0.5D;
				double var6 = z + 0.5D;
				world.makeSound(var2, y + 0.5D, var6, "random.chestclosed", 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
			}
			if (lidAngle < 0.0F) lidAngle = 0.0F;
		}
		if (canCondense()) {
			for (; scaledEnergy >= getTargetValue(target()) * 80 && roomFor(new ItemStack(target().id, 1, target().getData())); PushStack(new ItemStack(
					target().id, 1, target().getData())))
				scaledEnergy -= getTargetValue(target()) * 80;

			for (int var9 = 1; var9 < items.length; var9++) {
				if (items[var9] == null || EEMaps.getEMC(items[var9]) == 0 || items[var9].doMaterialsMatch(target())
						|| items[var9].getItem() instanceof ItemKleinStar || scaledEnergy + EEMaps.getEMC(items[var9]) * 80 > 800000000) continue;
				scaledEnergy += EEMaps.getEMC(items[var9]) * 80;
				items[var9].count--;
				if (items[var9].count == 0) items[var9] = null;
				break;
			}

		}
		if (condenseOn && eternalDensity >= 0) doCondense(items[eternalDensity]);
		if (attractionOn) doAttraction();
	}

	private void doAttraction() {
		List<EntityLootBall> var1 = world.a(ee.EntityLootBall.class, AxisAlignedBB.b(x - 10, y - 10, z - 10, x + 10, y + 10, z + 10));
		Entity var2;
		for (Iterator<EntityLootBall> var3 = var1.iterator(); var3.hasNext(); PullItems(var2))
			var2 = var3.next();

		List<EntityLootBall> var12 = world.a(ee.EntityLootBall.class, AxisAlignedBB.b(x - 10, y - 10, z - 10, x + 10, y + 10, z + 10));
		Entity var4;
		for (Iterator<EntityLootBall> var5 = var12.iterator(); var5.hasNext(); PullItems(var4))
			var4 = var5.next();

		List<EntityItem> var13 = world.a(net.minecraft.server.EntityItem.class, AxisAlignedBB.b(x - 10, y - 10, z - 10, x + 10, y + 10, z + 10));
		Entity var6;
		for (Iterator<EntityItem> var7 = var13.iterator(); var7.hasNext(); PullItems(var6))
			var6 = var7.next();

		List<Entity> var14 = world.a(ee.EntityLootBall.class, AxisAlignedBB.b(x - 0.5D, y - 0.5D, z - 0.5D, x + 1.25D, y + 1.25D, z + 1.25D));
		Iterator<Entity> var9 = var14.iterator();

		while (var9.hasNext()) {
			Entity var8 = var9.next();
			if (var8 == null) continue;
			GrabItems(var8);
		}

		List<Entity> var15 = world.a(net.minecraft.server.EntityItem.class, AxisAlignedBB.b(x - 0.5D, y - 0.5D, z - 0.5D, x + 1.25D, y + 1.25D, z + 1.25D));
		Iterator<Entity> var11 = var15.iterator();

		while (var11.hasNext()) {
			Entity var10 = var11.next();
			if (var10 == null) continue;
			GrabItems(var10);
		}

	}

	public boolean PushStack(EntityItem var1) {
		if (var1 == null) return false;
		if (var1.itemStack == null) {
			var1.die();
			return false;
		}
		if (var1.itemStack.count < 1) {
			var1.die();
			return false;
		}
		for (int var2 = 1; var2 < items.length; var2++) {
			if (items[var2] == null) {
				items[var2] = var1.itemStack.cloneItemStack();
				for (items[var2].count = 0; var1.itemStack.count > 0 && items[var2].count < items[var2].getMaxStackSize(); var1.itemStack.count--)
					items[var2].count++;

				var1.die();
				return true;
			}
			if (items[var2].doMaterialsMatch(var1.itemStack) && items[var2].count <= var1.itemStack.getMaxStackSize() - var1.itemStack.count) {
				for (; var1.itemStack.count > 0 && items[var2].count < items[var2].getMaxStackSize(); var1.itemStack.count--)
					items[var2].count++;

				var1.die();
				return true;
			}
		}

		return false;
	}

	public boolean PushStack(ItemStack var1) {
		if (var1 == null) return false;
		for (int var2 = 1; var2 < items.length; var2++) {
			if (items[var2] == null) {
				items[var2] = var1.cloneItemStack();
				var1 = null;
				return true;
			}
			if (items[var2].doMaterialsMatch(var1) && items[var2].count <= var1.getMaxStackSize() - var1.count) {
				items[var2].count += var1.count;
				var1 = null;
				return true;
			}
		}

		return false;
	}

	private ItemStack[] PushDenseStacks(ItemStack[] var1) {
		for (int var2 = 1; var2 < var1.length; var2++) {
			if (var1[var2] != null && PushStack(var1[var2])){
				var1[var2] = null;
			}
		}
		return var1;
	}

	//Not the problem below here
	private void GrabItems(Entity var1) {
		if (var1 == null || var1.dead) return;
		
		if (var1 instanceof EntityItem) {
			if (((EntityItem) var1).itemStack == null) {
				var1.die();
				return;
			}
			ItemStack var9 = ((EntityItem) var1).itemStack.cloneItemStack();
			((EntityItem) var1).itemStack = null;
			
			if (var9.getItem() instanceof ItemLootBall) {
				ItemLootBall var3 = (ItemLootBall) var9.getItem();
				ItemStack var4[] = var3.getDroplist(var9);
				ItemStack var5[] = var4;
				int var6 = var4.length;
				
				for (int var7 = 0; var7 < var6; var7++) {
					ItemStack var8 = var5[var7];
					PushStack(var8);
				}

				var1.die();
			} else {
				if (PushStack(var9)) var1.die();
				else {
					((EntityItem) var1).itemStack = var9;
					var1.dead = false;
				}
			}
		} else if (var1 instanceof EntityLootBall) {
			if (((EntityLootBall) var1).items == null){
				var1.die();
			}
			
			ItemStack var2[] = ((EntityLootBall)var1).items;
			((EntityLootBall) var1).items = PushDenseStacks(var2);
			
			if (((EntityLootBall) var1).isEmpty()) {
				var1.die();
			}
		}
	}

	private void PullItems(Entity var1) {
		if (var1 instanceof EntityItem || var1 instanceof EntityLootBall) {
			if (var1 instanceof EntityLootBall) ((EntityLootBall) var1).setBeingPulled(true);
			double var3 = x + 0.5D - var1.locX;
			double var5 = y + 0.5D - var1.locY;
			double var7 = z + 0.5D - var1.locZ;
			double var9 = var3 * var3 + var5 * var5 + var7 * var7;
			var9 *= var9;
			if (var9 <= Math.pow(6D, 4D)) {
				double var11 = var3 * 0.019999999552965164D / var9 * Math.pow(6D, 3D);
				double var13 = var5 * 0.019999999552965164D / var9 * Math.pow(6D, 3D);
				double var15 = var7 * 0.019999999552965164D / var9 * Math.pow(6D, 3D);
				if (var11 > 0.10000000000000001D) var11 = 0.10000000000000001D;
				else if (var11 < -0.10000000000000001D) var11 = -0.10000000000000001D;
				if (var13 > 0.10000000000000001D) var13 = 0.10000000000000001D;
				else if (var13 < -0.10000000000000001D) var13 = -0.10000000000000001D;
				if (var15 > 0.10000000000000001D) var15 = 0.10000000000000001D;
				else if (var15 < -0.10000000000000001D) var15 = -0.10000000000000001D;
				var1.motX += var11 * 1.2D;
				var1.motY += var13 * 1.2D;
				var1.motZ += var15 * 1.2D;
			}
		}
	}

	//All good below here
	public int latentEnergy() {
		return scaledEnergy / 80;
	}

	public void b(int var1, int var2) {
		if (var1 == 1) numUsingPlayers = var2;
	}

	public void f() {
		numUsingPlayers++;
		world.playNote(x, y, z, 1, numUsingPlayers);
	}

	public void g() {
		numUsingPlayers--;
		world.playNote(x, y, z, 1, numUsingPlayers);
	}

	public boolean a(EntityHuman var1) {
		return world.getTileEntity(x, y, z) == this ? var1.e(x + 0.5D, y + 0.5D, z + 0.5D) <= 64D : false;
	}

	public int getStartInventorySide(int var1) {
		return 1;
	}

	public int getSizeInventorySide(int var1) {
		return items.length - 1;
	}

	public boolean onBlockActivated(EntityHuman var1) {
		if (world.isStatic) return true;
		
		var1.openGui(mod_EE.getInstance(), GuiIds.CONDENSER, world, x, y, z);
		return true;
	}

	public int getTextureForSide(int var1) {
		if (var1 != 1 && var1 != 0) {
			byte var2 = direction;
			return var1 == var2 ? EEBase.condenserFront : EEBase.condenserSide;
		} else {
			return EEBase.condenserTop;
		}
	}

	public int getInventoryTexture(int var1) {
		return var1 == 1 || var1 == 0 ? EEBase.condenserTop : var1 != 3 ? EEBase.condenserSide : EEBase.condenserFront;
	}

	public int getLightValue() {
		return 10;
	}

	public void onNeighborBlockChange(int i) {}

	public void randomDisplayTick(Random random) {}

	public ItemStack splitWithoutUpdate(int var1) {
		return null;
	}

	public ItemStack[] getContents() {
		return items;
	}

	public void setMaxStackSize(int i) {}
}