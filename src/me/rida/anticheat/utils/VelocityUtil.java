package me.rida.anticheat.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.data.DataPlayer;

public class VelocityUtil implements Listener {
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        DataPlayer data = AntiCheat.getInstance().getDataManager().getData(p);
        if (data != null) {
            if (data.isDidTakeVelocity()) {
                if (TimerUtils.elapsed(data.getLastVelMS(),2000L)) {
                    data.setDidTakeVelocity(false);
                }
            }
        }
    }
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onVelEvent(PlayerVelocityEvent e) {
        Player p = e.getPlayer();
         DataPlayer data = AntiCheat.getInstance().getDataManager().getData(p);
        if (data != null) {
            data.setDidTakeVelocity(true);
            data.setLastVelMS(TimerUtils.nowlong());
        }
    }
    public static boolean didTakeVelocity(Player p) {
        boolean out = false;
     DataPlayer data = AntiCheat.getInstance().getDataManager().getData(p);
        if (data != null && data.isDidTakeVelocity()) {
            out = true;
        }
        return out;
    }
}