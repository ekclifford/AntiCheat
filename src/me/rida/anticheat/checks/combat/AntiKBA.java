package me.rida.anticheat.checks.combat;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;
import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.checks.Check;
import me.rida.anticheat.checks.CheckType;
import me.rida.anticheat.other.Ping;
import me.rida.anticheat.utils.Color;
import me.rida.anticheat.utils.ServerUtil;

public class AntiKBA extends Check {
    public static Map<Player, Long> lastVelocity = new HashMap<Player, Long>();
    public static Map<Player, Integer> awaitingVelocity = new HashMap<Player, Integer>();
    public static Map<Player, Double> totalMoved = new HashMap<Player, Double>();

    public AntiKBA(AntiCheat AntiCheat) {
        super("AntiKBA", "AntiKB",  CheckType.Combat, AntiCheat);
		setEnabled(true);
		setMaxViolations(30);
		setBannable(false);
		setViolationsToNotify(3);
		setViolationResetTime(250000);
    }

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	private void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (AntiKBA.lastVelocity.containsKey((Object)p)) {
            AntiKBA.lastVelocity.remove((Object)p);
        }
        if (AntiKBA.awaitingVelocity.containsKey((Object)p)) {
            AntiKBA.awaitingVelocity.remove((Object)p);
        }
        if (AntiKBA.totalMoved.containsKey((Object)p)) {
            AntiKBA.totalMoved.remove((Object)p);
        }
    }

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	private void Move(PlayerMoveEvent e) {
        double d;
        Player p = e.getPlayer();
        if (ServerUtil.isOnBlock(p, 0, new Material[]{Material.WEB}) 
        		|| ServerUtil.isOnBlock(p, 1, new Material[]{Material.WEB}) 
        		|| (ServerUtil.isHoveringOverWater(p, 1) 
        		|| ServerUtil.isHoveringOverWater(p, 0)) 
        		|| (p.getAllowFlight()) 
        		|| (p.isDead()) 
        		|| (Ping.getPing(p) > 400)
                || p.getGameMode().equals(GameMode.CREATIVE)
        		|| getAntiCheat().getLag().getTPS() < getAntiCheat().getTPSCancel()
                || getAntiCheat().getLag().getPing(p) > getAntiCheat().getPingCancel()) {
            return;
        }
        int n = 0;
        if (AntiKBA.awaitingVelocity.containsKey((Object)p)) {
            n = AntiKBA.awaitingVelocity.get((Object)p);
        }
        long l = 0;
        if (AntiKBA.lastVelocity.containsKey((Object)p)) {
            l = AntiKBA.lastVelocity.get((Object)p);
        }
        if (p.getLastDamageCause() == null || p.getLastDamageCause().getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && p.getLastDamageCause().getCause() != EntityDamageEvent.DamageCause.PROJECTILE) {
            n = 0;
        }
        if (System.currentTimeMillis() - l > 2000 && n > 0) {
            --n;
        }
        double d2 = 0.0;
        if (AntiKBA.totalMoved.containsKey((Object)p)) {
            d2 = AntiKBA.totalMoved.get((Object)p);
        }
        if ((d = e.getTo().getY() - e.getFrom().getY()) > 0.0) {
            d2 += d;
        }
        int n2 = 0;
        int n3 = 1;
        if (n > 0) {
            if (d2 < 0.3) {
                n2 += 9;
            } else {
                n2 = 0;
                d2 = 0.0;
                --n;
            }
            if (ServerUtil.isOnGround(p, -1) || ServerUtil.isOnGround(p, -2) || ServerUtil.isOnGround(p, -3)) {
                n2 -= 9;
            }
        }
        if (n2 > n3) {
            if (d2 == 0.0) {
                if (Ping.getPing(p) > 500) {
                    return;
                
                }
            	getAntiCheat().logCheat(this, p, Color.Red + "Experemental", "(Type: A)");
            	} else {
                if (Ping.getPing(p) > 220) {
                    return;
                }
            	getAntiCheat().logCheat(this, p, Color.Red + "Experemental", "(Type: A)");            }
            n2 = 0;
            d2 = 0.0;
            --n;
        }
        AntiKBA.awaitingVelocity.put(p, n);
        AntiKBA.totalMoved.put(p, d2);
    }

	@SuppressWarnings( "unused" )
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	private void Velocity(PlayerVelocityEvent e) {
        double d;
        long l;
        Player p = e.getPlayer();
        if (ServerUtil.isOnBlock(p, 0, new Material[]{Material.WEB}) || ServerUtil.isOnBlock(p, 1, new Material[]{Material.WEB})) {
            return;
        }
        if (ServerUtil.isHoveringOverWater(p, 1) || ServerUtil.isHoveringOverWater(p, 0)) {
            return;
        }
        if (ServerUtil.isOnGround(p, -1) || ServerUtil.isOnGround(p, -2) || ServerUtil.isOnGround(p, -3)) {
            return;
        }
        if (p.getAllowFlight()) {
            return;
        }
        if (AntiKBA.lastVelocity.containsKey((Object)p) && (l = System.currentTimeMillis() - AntiKBA.lastVelocity.get((Object)p)) < 500) {
            return;
        }
        Vector vector = e.getVelocity();
        double d2 = Math.abs(vector.getY());
        if (d2 > 0.0 && (d = (double)((int)(Math.pow(d2 + 2.0, 2.0) * 5.0))) > 20.0) {
            int n = 0;
            if (AntiKBA.awaitingVelocity.containsKey((Object)p)) {
                n = AntiKBA.awaitingVelocity.get((Object)p);
            }
            AntiKBA.awaitingVelocity.put(p, ++n);
            AntiKBA.lastVelocity.put(p, System.currentTimeMillis());
        }
    }
}

