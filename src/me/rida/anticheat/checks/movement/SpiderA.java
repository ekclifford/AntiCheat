package me.rida.anticheat.checks.movement;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.checks.Check;
import me.rida.anticheat.checks.CheckType;
import me.rida.anticheat.utils.BlockUtil;
import me.rida.anticheat.utils.Color;
import me.rida.anticheat.utils.MathUtil;
import me.rida.anticheat.utils.PlayerUtil;
import me.rida.anticheat.utils.CheatUtil;
import me.rida.anticheat.utils.VelocityUtil;

public class SpiderA extends Check {

	public SpiderA(AntiCheat AntiCheat) {
		super("SpiderA", "Spider", CheckType.Movement, AntiCheat);

		this.setEnabled(true);
		this.setBannable(false);
		setViolationResetTime(1000);
		setViolationsToNotify(2);
		setMaxViolations(5);
	}

    @SuppressWarnings("unused")
    private void onMove(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();
        Player p = e.getPlayer();
    	double OffSet = e.getFrom().getY() - e.getTo().getY();
		if (OffSet <= 0.0 || OffSet > 0.16) {
			
		}
    }
	public static Map<UUID, Map.Entry<Long, Double>> AscensionTicks = new HashMap<UUID, Map.Entry<Long, Double>>();

	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	private void CheckSpider(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		UUID u = p.getUniqueId();
		
        if (p.getGameMode().equals(GameMode.CREATIVE)
                || p.getAllowFlight()
                || p.getVehicle() != null
                || p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SPONGE
                || p.getLocation().getBlock().getRelative(BlockFace.DOWN).getTypeId() == 165
                || PlayerUtil.isOnClimbable(p, 0)
                || PlayerUtil.isOnClimbable(p, 1) 
				|| getAntiCheat().getLag().getTPS() < getAntiCheat().getTPSCancel()
		        || getAntiCheat().getLag().getPing(p) > getAntiCheat().getPingCancel()
				|| !getAntiCheat().isEnabled()
				|| PlayerUtil.isNotSpider(p)
				|| PlayerUtil.isOnFence(p.getLocation())
				|| PlayerUtil.isOnPressure(p.getLocation())
				|| BlockUtil.isNearFence(p)
				|| BlockUtil.isNearPressure(p)
                || VelocityUtil.didTakeVelocity(p)) {
        	return;
        }
        if (BlockUtil.isNearLiquid(p) && BlockUtil.isNearHalfBlock(p)) {
        	return;
        }
		

		long Time = System.currentTimeMillis();
		double TotalBlocks = 0.0D;
		if (SpiderA.AscensionTicks.containsKey(u)) {
			Time = AscensionTicks.get(u).getKey().longValue();
			TotalBlocks = AscensionTicks.get(u).getValue().doubleValue();
		}
		long MS = System.currentTimeMillis() - Time;
        double OffsetY = MathUtil.offset(MathUtil.getVerticalVector(e.getFrom().toVector()), MathUtil.getVerticalVector(e.getTo().toVector()));

		boolean ya = false;
		List<Material> Types = new ArrayList<Material>();
		Types.add(p.getLocation().getBlock().getRelative(BlockFace.SOUTH).getType());
		Types.add(p.getLocation().getBlock().getRelative(BlockFace.NORTH).getType());
		Types.add(p.getLocation().getBlock().getRelative(BlockFace.WEST).getType());
		Types.add(p.getLocation().getBlock().getRelative(BlockFace.EAST).getType());
		for (Material Type : Types) {
			if ((Type.isSolid()) && (Type != Material.LADDER) && (Type != Material.VINE) && (Type != Material.AIR)) {
				ya = true;
				break;
			}
		}
		if (OffsetY > 0.0D) {
			TotalBlocks += OffsetY;
		} else if ((!ya) || (!CheatUtil.blocksNear(p))) {
			TotalBlocks = 0.0D;
		} else if (((e.getFrom().getY() > e.getTo().getY()) || (PlayerUtil.isInGround(p)))) {
			TotalBlocks = 0.0D;
		}
		double Limit = 0.5D;
		if (p.hasPotionEffect(PotionEffectType.JUMP)) {
			for (PotionEffect effect : p.getActivePotionEffects()) {
				if (effect.getType().equals(PotionEffectType.JUMP)) {
					int level = effect.getAmplifier() + 1;
					Limit += Math.pow(level + 4.2D, 2.0D) / 16.0D;
					break;
				}
			}
		}
		if ((ya) && (TotalBlocks > Limit)) {
			if (MS > 500L) {
				getAntiCheat().logCheat(this, p, Color.Red + "(WallClimb) False flag if the player is falling next to a wall!", "(Type: A)");
				Time = System.currentTimeMillis();
			}
		} else {
			Time = System.currentTimeMillis();
		}
		SpiderA.AscensionTicks.put(u, new AbstractMap.SimpleEntry<>(Time, TotalBlocks));
	}

}