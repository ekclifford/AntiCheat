package me.rida.anticheat.other;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import me.rida.anticheat.AntiCheat;

public class LagCore implements Listener {
	private AntiCheat AntiCheat;
	private double tps;

	public LagCore(AntiCheat AntiCheat) {
		this.AntiCheat = AntiCheat;

		new BukkitRunnable() {
			long sec;
			long currentSec;
			int ticks;

			public void run() {
				this.sec = (System.currentTimeMillis() / 1000L);
				if (this.currentSec == this.sec) {
					this.ticks += 1;
				} else {
					this.currentSec = this.sec;
					LagCore.this.tps = (LagCore.this.tps == 0.0D ? this.ticks : (LagCore.this.tps + this.ticks) / 2.0D);
					this.ticks = 0;
				}
			}
		}.runTaskTimerAsynchronously(AntiCheat, 1L, 1L);

		this.AntiCheat.RegisterListener(this);
	}

	public double getTPS() {
		return this.tps + 1.0D > 20.0D ? 20.0D : this.tps + 1.0D;
	}

	public double getLag() {
		return Math.round((1.0D - tps / 20.0D) * 100.0D);
	}

	public double getFreeRam() {
		return Math.round(Runtime.getRuntime().freeMemory() / 1000000);
	}

	public double getMaxRam() {
		return Math.round(Runtime.getRuntime().maxMemory() / 1000000);
	}

	public static Object getNmsPlayer(Player p) throws Exception {
		Method getHandle = p.getClass().getMethod("getHandle");
		return getHandle.invoke(p);
	}

	public static Object getFieldValue(Object instance, String fieldName) throws Exception {
		Field field = instance.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(instance);
	}

	public int getPing(Player p) {
		try {
			String bukkitversion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
			Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + bukkitversion + ".entity.CraftPlayer");
			Object handle = craftPlayer.getMethod("getHandle").invoke(p);
			Integer ping = (Integer) handle.getClass().getDeclaredField("ping").get(handle);
			return ping.intValue();
		} catch (Exception e) {
			return 404;
		}
	}
}