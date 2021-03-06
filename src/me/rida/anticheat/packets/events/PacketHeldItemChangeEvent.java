package me.rida.anticheat.packets.events;

import org.bukkit.event.HandlerList;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class PacketHeldItemChangeEvent extends Event {
	private Player Player;
	private PacketEvent Event;
	private static final HandlerList handlers;

	static {
		handlers = new HandlerList();
	}

	public PacketHeldItemChangeEvent(final PacketEvent Event, final Player Player) {
		super();
		this.Player = Player;
		this.Event = Event;
	}

	public PacketEvent getPacketEvent() {
		return this.Event;
	}

	public Player getPlayer() {
		return this.Player;
	}

	public HandlerList getHandlers() {
		return PacketHeldItemChangeEvent.handlers;
	}

	public static HandlerList getHandlerList() {
		return PacketHeldItemChangeEvent.handlers;
	}
}