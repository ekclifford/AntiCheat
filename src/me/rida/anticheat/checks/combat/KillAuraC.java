package me.rida.anticheat.checks.combat;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.rida.anticheat.checks.Check;
import me.rida.anticheat.checks.CheckType;
import me.rida.anticheat.other.Ping;
import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.utils.Color;
import me.rida.anticheat.utils.MathUtil;

public class KillAuraC extends Check {
    public KillAuraC(AntiCheat AntiCheat) {
        super("KillAuraC", "KillAura",  CheckType.Combat, AntiCheat);
		setEnabled(true);
		setMaxViolations(20);
		setBannable(false);
		setViolationsToNotify(3);
    }

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	private void onAngleHit(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player) 
        		||!(e.getDamager() instanceof Player)) {
            return;
        }
        Player p = (Player)e.getDamager();
        Player p2 = (Player)e.getEntity();
        double d = Ping.getPing(p);
        double d2 = Ping.getPing(p2);
        double d3 = MathUtil.getOffsets2(p, (LivingEntity)p2)[0];
        if (d2 > 450.0) {
            return;
        }
        if (d >= 100.0 && d < 200.0) {
            d3 -= 50.0;
        } else if (d >= 200.0 && d < 250.0) {
            d3 -= 75.0;
        } else if (d >= 250.0 && d < 300.0) {
            d3 -= 150.0;
        } else if (d >= 300.0 && d < 350.0) {
            d3 -= 300.0;
        } else if (d >= 350.0 && d < 400.0) {
            d3 -= 350.0;
        } else if (d > 400.0) {
            return;
        }
        if (d3 >= 300.0) {
        	if (getAntiCheat().getLag().getTPS() < getAntiCheat().getTPSCancel()
                || getAntiCheat().getLag().getPing(p) > getAntiCheat().getPingCancel()) {
        		return;
        	}
        	getAntiCheat().logCheat(this, p, Color.Red + "Experemental", "(Type: C)");
        }
    }
}

