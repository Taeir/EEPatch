package ee.events;

public class EEEnums {
	public enum EEAction {
		PASSIVE, ACTIVE, HELD, RELEASE, ALTERNATE, LEFTCLICK, RIGHTCLICK,
		
		CHARGE,
		@Deprecated
		UNCHARGE,
		TOGGLE, TOGGLE2, BREAKBLOCK
	}
	
	public enum EEAction2 {
		/** Break blocks in a radius. */
		BreakRadius(0),
		/** Till blocks in a radius. */
		TillRadius(1),
		/** Attack players and mobs in a radius. */
		AttackRadius(2), 
		/** Break 3 blocks using the tall mode available on some tools. */
		TallBreak(3),
		/** Break 3 blocks using the wide mode available on some tools. */
		WideBreak(4), 
		/** Break 3 blocks using the long mode available on some tools. */
		LongBreak(5),
		/** Break a big square of blocks using the mega break mode on hammers. */
		MegaBreak(6), 
		///** Toggle the tool mode between tall, wide and long. */
		//UpdateToolMode,
		///** Toggle the hammer mode between normal and mega. */
		//UpdateHammerMode,
		///** Toggle the sword mode between only hostile and all mobs. */
		//UpdateSwordMode, 
		/** The ability to use the divining rod */
		DiviningRod(7), 
		/** Shear 1 or multiple mobs with an EE tool. */
		Shear(8), 
		/** Create 1 or more water blocks (used by evertide amulet) */
		CreateWater(9),
		/** The action has not been specified or does not fall under any of the possible categories. */
		Unknown(10);
		private int nr;
		EEAction2(int nr){
			this.nr = nr;
		}
		
		public int getNr() {
			return nr;
		}
	}
	
	public enum EERingAction {
		/** Negate Falldamage (SWRG and Ring of Arcana) (0)*/
		NegateFallDamage("negatefalldamage",0),
		/** Fly (SWRG and Ring of Arcana) (1)*/
		Fly("fly with",1),
		/** Interdict (SWRG and Ring of Arcana) (2)*/
		Interdict("interdict mobs with",2),
		
		/** Freeze (Zero Ring and Ring of Arcana) (3)*/
		Freeze("freeze an area",3),
		/** Burn (Ring of Ignition and Ring of Arcana) (4)*/
		Burn("burn an area with",4),
		/** Extinguish (Ring of Ignition) (5)*/
		Extinguish("extinguish fires around you with",5),
		
		/** Plant plants in a radius (Harvest Goddess Band) (6)*/
		PlantRadius("automatically plant plants in a radius",6),
		/** Fertilize (Harvest Goddess Band and Ring of Arcana) (7)*/
		Fertilize("make plants grow faster with",7), 
		/** Harvest (Harvest Goddess Band and Ring of Arcana) (8)*/
		Harvest("harvest plants in a radius with",8),
		
		/** Attract Items (BHB and Void Ring) (9)*/
		AttractItems("attract items with",9),
		/** Vaporize liquid (BHB and Void Ring) (10)*/
		DeleteLiquid("delete liquids with",10),
		
		/** Teleport (Void Ring) (11)*/
		Teleport("teleport with",11),
		/** Condense (Gem of Eternal Density and Void Ring) (12)*/
		Condense("condense items with",12),
		
		/** Gust (SWWRG and Ring of Arcana) (13)*/
		Gust("create Gusts with",13),
		/** Strike Lightning (Ring of Arcana) (14)*/
		StrikeLightning("cause lightning to strike with",14),
		
		/** Throw a snowball (Zero Ring and Ring of Arcana) (15)*/
		ThrowSnowball("throw snowballs with",15),
		/** Throw a pyrokinesis entity (Ring of Ignition and Ring of Arcana) (16)*/
		ThrowPyrokinesis("throw fireballs with",16),
		/** Throw a water essence (Evertide Amulet) (17)*/
		ThrowWater("throw water essences with",17),
		/** Throw a lava essence (Vulcanite Amulet) (18)*/
		ThrowLava("throw lava essences with",18),
		
		/** Activate (19)*/
		Activate("Activate",19),
		/** Activate Interdict (SWRG and Ring of Arcana) (20)*/
		ActivateInterdict("Activate Interdiction",20),
		/** Deactivate (21) 
		 * @deprecated */
		Deactivate("deactivate",21),

		/** Unknown (22) */
		Unknown("unknown",22),
		/** Shoot arrows (Archangels Smite) (23) */
		ShootArrows("shoot arrows with",23);
		
		private int nr;
		private String name;
		EERingAction(String name, int nr){
			this.name = name;
			this.nr = nr;
		}
		
		public int getNr(){
			return nr;
		}
		
		public String getName(){
			return name;
		}
	}
	
	public enum EEAmuletAction {
		/** Create 1 or more water blocks (used by evertide amulet) */
		CreateWater(0),
		/** Create a water essence which creates water where it hits, and turns lava to obsidian */
		CreateWaterBall(1),
		/** Stop the player from drowning (Passive of Evertide Amulet) (Drowntime of 15s)*/
		StopDrowning(2),
		/** Create a lava essence which creates lava where it hits. */
		CreateLavaBall(3),
		/** Create 1 or more lava blocks (used by volcanite amulet) */
		CreateLava(4),
		/** Vaporize water in a range (used by volcanite amulet) */
		Vaporize(5),
		/** Stop the player from taking fire damage (Passive of Volcanite Amulet) (30s) */
		FireImmune(6);
		
		private int nr;
		EEAmuletAction(int nr){
			this.nr = nr;
		}
		
		public int getNr(){
			return nr;
		}
	}
	
	public enum EETransmuteAction {
		Transmute(0),
		ChangeMob(1),
		PortableCrafting(2), PortableTable(3);
		
		private int nr;
		EETransmuteAction(int nr){
			this.nr = nr;
		}
		
		public int getNr(){
			return nr;
		}
	}
	
	public enum EEPedestalAction {
		/** Push mobs away (EE Torch) */
		Interdict(0),
		/** Keep the weather Stormy (Evertide Amulet) */
		Storm(1),
		/** Shoot arrows at players (Archangels Smite) */
		ShootArrow(2),
		/** Harvest plants (Harvest Godess band) */
		Harvest(3),
		/** Zero Ring (Does nothing) */
		None(4),
		/** Ignite mobs that come close (Ring of Ignition) */
		Ignition(5),
		/** Repair tools of players nearby (Repair Talisman) */
		Repair(6),
		/** Heal nearby mobs and players (Soul stone) */
		Heal(7),
		/** Strike nearby monsters with Lightning (SWRG) */
		StrikeLightning(8),
		/** Stop all storms (Volcanite Amulet) */
		StopStorm(9),
		/** Slow Entities and speed up machines. (Watch Of Flowing Time) */
		Time(10),
		/** Attract Items (Black Hole Band) */
		Attract(11),
		/** Activate the pedestal */
		Activate(12);
		
		private int nr;
		EEPedestalAction(int nr){
			this.nr = nr;
		}
		
		public int getNr(){
			return nr;
		}
	}
	
	public enum DuplicateType {
		RMFurnace(0);
		private int nr;
		DuplicateType(int nr){
			this.nr = nr;
		}
		public int getNr(){
			return nr;
		}
	}

	public enum EEArmorAction {
		/** When a player activates offensive powers. */
		OffensiveActivate(true, 0), OffensiveExplode(true, 1), OffensiveStrike(true, 2), MovementActivate(false, 3);
		private boolean offensive = false;
		
		private int nr;
		EEArmorAction(boolean offensive, int nr){
			this.offensive = offensive;
			this.nr = nr;
		}
		
		public boolean isOffensive(){
			return offensive;
		}
		
		public int getNr(){
			return nr;
		}
	}
	
	public enum EEWatchAction {
		TimeForward, TimeBackward
	}
}
