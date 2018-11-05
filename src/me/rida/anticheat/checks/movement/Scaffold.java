package me.rida.anticheat.checks.movement;

import org.bukkit.event.inventory.*;
import org.bukkit.enchantments.*;
import org.bukkit.event.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import java.util.*;
import org.bukkit.command.*;
import org.bukkit.potion.*;
import org.bukkit.*;
import org.bukkit.event.block.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.util.*;

import me.rida.anticheat.utils.Color;
import me.rida.anticheat.utils.UtilVelocity;
import me.rida.anticheat.utils.needscleanup.ExtraUtils;
import me.rida.anticheat.utils.needscleanup.UtilsB;

import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.checks.Check;

public class Scaffold extends Check {
    public Scaffold(AntiCheat AntiCheat) {
        super("Scaffold", "Scaffold", AntiCheat);
		setEnabled(true);
		setMaxViolations(10);
		setViolationResetTime(1000);
		setBannable(false);
		setViolationsToNotify(2);
    }
    @EventHandler
    public void onPlaceBlock(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final Block target = player.getTargetBlock((Set)null, 5);
        if (player.getGameMode().equals(GameMode.CREATIVE)
                || player.getAllowFlight()
                || event.getPlayer().getVehicle() != null
				|| !getAntiCheat().isEnabled()
				|| getAntiCheat().isSotwMode()
                || UtilVelocity.didTakeVelocity(player)) return;
        if (event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation().subtract(0.0, 1.0, 0.0)).getType() == Material.AIR) {
            if (!event.getBlock().getLocation().equals((Object)target.getLocation()) && !event.isCancelled() && target.getType().isSolid() && !target.getType().name().toLowerCase().contains("sign") && !target.getType().toString().toLowerCase().contains("fence") && player.getLocation().getY() > event.getBlock().getLocation().getY()) {
            	getAntiCheat().logCheat(this, player, Color.Red + "Experemental", null);
            }
        }
    }
}
    


