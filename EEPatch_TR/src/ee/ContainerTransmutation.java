package ee;

import net.minecraft.server.Container;
import net.minecraft.server.EEProxy;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ICrafting;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.PlayerInventory;
import net.minecraft.server.Slot;

public class ContainerTransmutation extends Container {
	private EntityHuman player;
	private TransTabletData transGrid;
	private int latentEnergy;
	private int currentEnergy;
	private int learned;
	private int lock;
	private boolean initialized;

	public ContainerTransmutation(PlayerInventory var1, EntityHuman human, TransTabletData transData) {
		player = human;
		setPlayer(human);
		transGrid = transData;
		learned = transData.learned;
		lock = transData.isMatterLocked() ? 1 : transData.isFuelLocked() ? 2 : 0;
		latentEnergy = transData.getLatentEnergy();
		currentEnergy = transData.getCurrentEnergy();
		a(new SlotTransmuteInput(transData, 0, 43, 29));
		a(new SlotTransmuteInput(transData, 1, 34, 47));
		a(new SlotTransmuteInput(transData, 2, 52, 47));
		a(new SlotTransmuteInput(transData, 3, 16, 56));
		a(new SlotTransmuteInput(transData, 4, 70, 56));
		a(new SlotTransmuteInput(transData, 5, 34, 65));
		a(new SlotTransmuteInput(transData, 6, 52, 65));
		a(new SlotTransmuteInput(transData, 7, 43, 83));
		a(new SlotTransmuteInput(transData, 8, 158, 56));
		a(new SlotConsume(transData, 9, 107, 103));
		a(new SlotTransmute(transData, 10, 158, 15));
		a(new SlotTransmute(transData, 11, 140, 19));
		a(new SlotTransmute(transData, 12, 176, 19));
		a(new SlotTransmute(transData, 13, 123, 36));
		a(new SlotTransmute(transData, 14, 158, 37));
		a(new SlotTransmute(transData, 15, 193, 36));
		a(new SlotTransmute(transData, 16, 116, 56));
		a(new SlotTransmute(transData, 17, 139, 56));
		a(new SlotTransmute(transData, 18, 177, 56));
		a(new SlotTransmute(transData, 19, 199, 56));
		a(new SlotTransmute(transData, 20, 123, 76));
		a(new SlotTransmute(transData, 21, 158, 75));
		a(new SlotTransmute(transData, 22, 193, 76));
		a(new SlotTransmute(transData, 23, 140, 93));
		a(new SlotTransmute(transData, 24, 176, 93));
		a(new SlotTransmute(transData, 25, 158, 97));

		for (int var4 = 0; var4 < 3; var4++) {
			for (int var5 = 0; var5 < 9; var5++) {
				//0+0*9+9= 9
				//0+1*9+9= 18
				//0+2*9+9= 27
				//0+8*9+9=81
				//1+0*9+9=10
				a(new Slot(player.inventory, var5 + var4 * 9 + 9, 35 + var5 * 18, 123 + var4 * 18));
			}
		}

		for (int var4 = 0; var4 < 9; var4++) {
			a(new Slot(player.inventory, var4, 35 + var4 * 18, 181));
		}

		a(transGrid);
		EEBase.watchTransGrid(player);
	}

	public IInventory getInventory() {
		return transGrid;
	}

	public void setItem(int var1, ItemStack var2) {
		super.setItem(var1, var2);

		if (var1 < 26) {
			if (var2 == null) {
				transGrid.items[var1] = null;
			} else {
				transGrid.items[var1] = var2.cloneItemStack();
			}
		}

		a(transGrid);
	}

	public void a(IInventory var1) {
		a();
		if (!EEProxy.isClient(EEProxy.theWorld)) {
			transGrid.update();
			transGrid.displayResults(transGrid.latentEnergy + transGrid.currentEnergy);
		}
	}

	public void a() {
		super.a();

		for (int var1 = 0; var1 < listeners.size(); var1++) {
			ICrafting var2 = (ICrafting) listeners.get(var1);

			if (latentEnergy != transGrid.latentEnergy || !initialized) {
				var2.setContainerData(this, 0, transGrid.latentEnergy & 0xFFFF);
			}

			if (latentEnergy != transGrid.latentEnergy || !initialized) {
				var2.setContainerData(this, 1, transGrid.latentEnergy >>> 16);
			}

			if (currentEnergy != transGrid.currentEnergy || !initialized) {
				var2.setContainerData(this, 2, transGrid.currentEnergy & 0xFFFF);
			}

			if (currentEnergy != transGrid.currentEnergy || !initialized) {
				var2.setContainerData(this, 3, transGrid.currentEnergy >>> 16);
			}

			if (learned != transGrid.learned || !initialized) {
				var2.setContainerData(this, 4, transGrid.learned);
			}

			if (lock != (transGrid.isMatterLocked() ? 1 : transGrid.isFuelLocked() ? 2 : 0) || !initialized) {
				var2.setContainerData(this, 5, transGrid.isMatterLocked() ? 1 : transGrid.isFuelLocked() ? 2 : 0);
			}
		}

		learned = transGrid.learned;
		lock = transGrid.isMatterLocked() ? 1 : transGrid.isFuelLocked() ? 2 : 0;
		latentEnergy = transGrid.latentEnergy;
		currentEnergy = transGrid.currentEnergy;
		initialized = true;
	}

	public void updateProgressBar(int var1, int var2) {
		if (var1 == 0) transGrid.latentEnergy = transGrid.latentEnergy & 0xFFFF0000 | var2;
		else if (var1 == 1) transGrid.latentEnergy = transGrid.latentEnergy & 0xFFFF | var2 << 16;
		else if (var1 == 2) transGrid.currentEnergy = transGrid.currentEnergy & 0xFFFF0000 | var2;
		else if (var1 == 3) transGrid.currentEnergy = transGrid.currentEnergy & 0xFFFF | var2 << 16;
		else if (var1 == 4) transGrid.learned = var2;
		else if (var1 == 5) {
			if (var2 == 0) {
				transGrid.unlock();
			} else if (var2 == 1) {
				transGrid.fuelUnlock();
				transGrid.matterLock();
			} else if (var2 == 2) {
				transGrid.matterUnlock();
				transGrid.fuelLock();
			}
		}
	}

	public boolean b(EntityHuman var1) {
		return true;
	}

	/**
	 * Called when the player closes his inventory.
	 * @see net.minecraft.server.Container#a(net.minecraft.server.EntityHuman)
	 */
	public void a(EntityHuman var1) {
		super.a(var1);
		EEBase.closeTransGrid(player);
		
		if (player.world.isStatic) return;
		for (int var2 = 0; var2 < 25; var2++) {
			ItemStack var3 = transGrid.splitWithoutUpdate(var2);

			if (var3 != null) {
				player.drop(var3);
			}
		}
		
	}

	public ItemStack a(int slotNr) {
		Slot slot = (Slot) e.get(slotNr);
		if (slot == null) return null;
		
		ItemStack var2 = null;
		ItemStack var4 = null;

		if (slotNr > 9 && slotNr < 26 && slot.c()) {//output
			var4 = slot.getItem().cloneItemStack();
		}

		if (slot.c()) {
			ItemStack item = slot.getItem();
			var2 = item.cloneItemStack();

			if (slotNr <= 8) {//Input
				if (!a(item, 26, 62, true)) {
					slot.set(null);
				}
			} else if (slotNr > 9 && slotNr < 26) {
				if (!grabResult(item, slot, 26, 62, false)) {
					slot.set(null);
				}
			} else if (slotNr >= 26 && slotNr < 62) {
				if ((EEMaps.getEMC(item) > 0 || EEBase.isKleinStar(item.id)) && !a(item, 0, 8, false)) {
					if (item.count == 0) {
						slot.set(null);
					}

					return null;
				}
			} else if (!a(item, 26, 62, false)) {
				if (item.count == 0) {
					slot.set(null);
				}

				return null;
			}

			if (item.count == 0) {
				if (slotNr > 9 && slotNr < 26) {
					item.count = 1;
				} else {
					slot.set(null);
				}
			} else {
				slot.d();
			}

			if (item.count == var2.count) {
				if (slotNr > 9 && slotNr < 26 && var4 != null) {
					return var4;
				}

				return null;
			}

			if (slotNr > 9 && slotNr < 26 && transGrid.latentEnergy + transGrid.currentEnergy < EEMaps.getEMC(item)) {
				return null;
			}

			slot.c(item);
		}

		if (var4 != null && slotNr > 9 && slotNr < 26) {
			slot.set(var4);
		}

		return var2;
	}

	protected boolean grabResult(ItemStack var1, Slot slot, int var3, int var4, boolean var5) {
		if (transGrid.latentEnergy + transGrid.currentEnergy < EEMaps.getEMC(var1)) return false;

		slot.c(var1);
		boolean var6 = false;
		int var7 = var3;

		if (var5) {
			var7 = var4 - 1;
		}

		if (var1.isStackable()) {
			while (var1.count > 0 && ((!var5 && var7 < var4) || (var5 && var7 >= var3))) {
				Slot var8 = (Slot) e.get(var7);
				ItemStack var9 = var8.getItem();

				if (var9 != null && var9.id == var1.id && (!var1.usesData() || var1.getData() == var9.getData()) && ItemStack.equals(var1, var9)) {
					int var10 = var9.count + var1.count;

					if (var10 <= var1.getMaxStackSize()) {
						var1.count = 0;
						var9.count = var10;
						var8.d();
						var6 = true;
					} else if (var9.count < var1.getMaxStackSize()) {
						var1.count -= var1.getMaxStackSize() - var9.count;
						var9.count = var1.getMaxStackSize();
						var8.d();
						var6 = true;
					}
				}

				if (var5) {
					var7--;
				} else {
					var7++;
				}
			}
		}

		if (var1.count > 0) {
			int var11;
			if (var5) {
				var11 = var4 - 1;
			} else {
				var11 = var3;
			}

			while ((!var5 && var11 < var4) || (var5 && var11 >= var3)) {
				Slot var12 = (Slot) e.get(var11);
				ItemStack var13 = var12.getItem();

				if (var13 == null) {
					var12.set(var1.cloneItemStack());
					var12.d();
					var1.count = 0;
					var6 = true;
					break;
				}

				if (var5) {
					var11--;
				} else {
					var11++;
				}
			}
		}

		var1.count = 1;
		return var6;
	}

	public ItemStack clickItem(int var1, int var2, boolean var3, EntityHuman human) {
		ItemStack var5 = null;

		if (var2 > 1) {
			return null;
		}

		if (var2 == 0 || var2 == 1) {
			PlayerInventory var6 = human.inventory;

			if (var1 == -999) {
				if (var6.getCarried() != null && var1 == -999) {
					if (var2 == 0) {
						human.drop(var6.getCarried());
						var6.setCarried(null);
					}

					if (var2 == 1) {
						human.drop(var6.getCarried().a(1));

						if (var6.getCarried().count == 0) {
							var6.setCarried(null);
						}
					}
				}
			} else if (var3) {
				ItemStack var7 = a(var1);

				if (var7 != null) {
					int var8 = var7.id;
					var5 = var7.cloneItemStack();
					Slot slot = (Slot) e.get(var1);

					if (slot != null && slot.getItem() != null && slot.getItem().id == var8 && slot.getItem().isStackable()) {
						retrySlotClick(var1, var2, 1, slot.getItem().getMaxStackSize(), var3, human);
					}
				}
			} else {
				if (var1 < 0) {
					return null;
				}

				Slot slot = (Slot) e.get(var1);

				if (slot != null) {
					slot.d();
					ItemStack var13 = slot.getItem();
					ItemStack var14 = var6.getCarried();

					if (var13 != null) {
						var5 = var13.cloneItemStack();
					}

					if (var13 == null) {
						if (var14 != null && slot.isAllowed(var14)) {
							int var10 = var2 != 0 ? 1 : var14.count;

							if (var10 > slot.a()) {
								var10 = slot.a();
							}

							slot.set(var14.a(var10));

							if (var14.count == 0) {
								var6.setCarried(null);
							}
						}
					} else if (var14 == null) {
						int var10 = var2 != 0 ? (var13.count + 1) / 2 : var13.count;
						ItemStack var11 = slot.a(var10);
						var6.setCarried(var11);

						if (var1 >= 10 && var1 <= 25) {
							slot.set(new ItemStack(var11.id, 1, var11.getData()));
						} else if (var13.count == 0) {
							slot.set(null);
						}

						slot.c(var6.getCarried());
					} else if (slot.isAllowed(var14)) {
						if (var13.id == var14.id && (!var13.usesData() || var13.getData() == var14.getData()) && ItemStack.equals(var13, var14)) {
							int var10 = var2 != 0 ? 1 : var14.count;

							if (var10 > slot.a() - var13.count) {
								var10 = slot.a() - var13.count;
							}

							if (var10 > var14.getMaxStackSize() - var13.count) {
								var10 = var14.getMaxStackSize() - var13.count;
							}

							var14.a(var10);

							if (var14.count == 0) {
								var6.setCarried(null);
							}

							var13.count += var10;
						} else if (var14.count <= slot.a()) {
							slot.set(var14);
							var6.setCarried(var13);
						}
					} else if (var13.id == var14.id && var14.getMaxStackSize() > 1 && (!var13.usesData() || var13.getData() == var14.getData())
							&& ItemStack.equals(var13, var14)) {
						int var10 = var13.count;

						if (var10 > 0 && var10 + var14.count <= var14.getMaxStackSize()) {
							var14.count += var10;

							if (var1 < 10 || var1 > 25) {
								var13.a(var10);

								if (var13.count == 0) {
									slot.set(null);
								}
							}

							slot.c(var6.getCarried());
						}
					}
				}
			}
		}

		return var5;
	}

	protected void retrySlotClick(int var1, int var2, int var3, int var4, boolean var5, EntityHuman var6) {
		if (var3 < var4) {
			var3++;
			slotClick(var1, var2, var3, var4, var5, var6);
		}
	}

	public ItemStack slotClick(int slotNr, int var2, int var3, int var4, boolean var5, EntityHuman human) {
		ItemStack var7 = null;

		if (var2 > 1) return null;
		

		if (var2 == 0 || var2 == 1) {
			PlayerInventory inv = human.inventory;

			if (slotNr == -999) {
				if (inv.getCarried() != null && slotNr == -999) {
					if (var2 == 0) {
						human.drop(inv.getCarried());
						inv.setCarried(null);
					}

					if (var2 == 1) {
						human.drop(inv.getCarried().a(1));

						if (inv.getCarried().count == 0) {
							inv.setCarried(null);
						}
					}
				}
			} else if (var5) {
				ItemStack var9 = a(slotNr);

				if (var9 != null) {
					int var10 = var9.id;
					var7 = var9.cloneItemStack();
					Slot var11 = (Slot) e.get(slotNr);

					if (var11 != null && var11.getItem() != null && var11.getItem().id == var10) {
						retrySlotClick(slotNr, var2, var3, var4, var5, human);
					}
				}
			} else {
				if (slotNr < 0) {
					return null;
				}

				Slot slot = (Slot) e.get(slotNr);

				if (slot != null) {
					slot.d();
					ItemStack sItem = slot.getItem();
					ItemStack mItem = inv.getCarried();

					if (sItem != null) {
						var7 = sItem.cloneItemStack();
					}

					if (sItem == null) {
						if (mItem != null && slot.isAllowed(mItem)) {
							int var12 = var2 != 0 ? 1 : mItem.count;

							if (var12 > slot.a()) {
								var12 = slot.a();
							}

							slot.set(mItem.a(var12));

							if (mItem.count == 0) {
								inv.setCarried(null);
							}
						}
					} else if (mItem == null) {
						int var12 = var2 != 0 ? (sItem.count + 1) / 2 : sItem.count;
						ItemStack var13 = slot.a(var12);
						inv.setCarried(var13);

						if (slotNr >= 10 && slotNr <= 25) {
							slot.set(new ItemStack(var13.id, 1, var13.getData()));
						} else if (sItem.count == 0) {
							slot.set(null);
						}

						slot.c(inv.getCarried());
					} else if (slot.isAllowed(mItem)) {
						if (sItem.id == mItem.id && (!sItem.usesData() || sItem.getData() == mItem.getData()) && ItemStack.equals(sItem, mItem)) {
							int var12 = var2 != 0 ? 1 : mItem.count;

							if (var12 > slot.a() - sItem.count) {
								var12 = slot.a() - sItem.count;
							}

							if (var12 > mItem.getMaxStackSize() - sItem.count) {
								var12 = mItem.getMaxStackSize() - sItem.count;
							}

							mItem.a(var12);

							if (mItem.count == 0) {
								inv.setCarried(null);
							}

							sItem.count += var12;
						} else if (mItem.count <= slot.a()) {
							slot.set(mItem);
							inv.setCarried(sItem);
						}
					} else if (sItem.id == mItem.id && mItem.getMaxStackSize() > 1 && (!sItem.usesData() || sItem.getData() == mItem.getData())
							&& ItemStack.equals(sItem, mItem)) {
						int var12 = sItem.count;

						if (var12 > 0 && var12 + mItem.count <= mItem.getMaxStackSize()) {
							mItem.count += var12;

							if (slotNr < 10 || slotNr > 25) {
								sItem.a(var12);

								if (sItem.count == 0) {
									slot.set(null);
								}
							}

							slot.c(inv.getCarried());
						}
					}
				}
			}
		}

		return var7;
	}
}