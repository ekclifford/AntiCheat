package me.rida.anticheat.checks.combat;

import me.rida.anticheat.AntiCheat;
import me.rida.anticheat.checks.Check;
import me.rida.anticheat.checks.CheckType;
import me.rida.anticheat.data.DataPlayer;
import me.rida.anticheat.utils.ExtraUtil;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Collections;
import java.util.Optional;

public class AimAssistA extends Check {
    public AimAssistA(AntiCheat AntiCheat) {
        super("AimAssistA", "AimAssist", CheckType.Combat, AntiCheat);
		setEnabled(true);
		setMaxViolations(10);
		setBannable(false);

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(AntiCheat.getInstance(), PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Optional<Entity> entityOp = event.getPlayer().getWorld().getEntities().stream().filter(entity -> entity.getEntityId() == event.getPacket().getIntegers().read(0)).findFirst();

                if(entityOp.isPresent()) {
                    Entity entity = entityOp.get();

                    EnumWrappers.EntityUseAction action = event.getPacket().getEntityUseActions().read(0);

                    if(action.equals(EnumWrappers.EntityUseAction.ATTACK) && entity instanceof LivingEntity) {
                        DataPlayer data = AntiCheat.getInstance().getDataManager().getDataPlayer(event.getPlayer());

                        if(data != null) {
                            data.lastAttack = System.currentTimeMillis();
                            data.lastHitEntity = (LivingEntity) entity;
                        }
                    }
                }
            }
        });
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        DataPlayer data = AntiCheat.getInstance().getDataManager().getDataPlayer(event.getPlayer());

        if(data == null
                || data.lastHitEntity == null
                || (System.currentTimeMillis() - data.lastAttack) > 350L) return;

        float offset = ExtraUtil.yawTo180F((float) ExtraUtil.getOffsetFromEntity(event.getPlayer(), data.lastHitEntity)[0]);

        if(data.patterns.size() >= 10) {
            //TODO Check

            Collections.sort(data.patterns);

            float range = Math.abs(data.patterns.get(data.patterns.size() - 1) -  data.patterns.get(0));

            if(Math.abs(range - data.lastRange) < 4) {

            	getAntiCheat().logCheat(this, event.getPlayer(), null, "(Type: A)");
            	}
            event.getPlayer().sendMessage("Range: " + range);

            data.lastRange = range;
            data.patterns.clear();
        } else {
            data.patterns.add(offset);
        }


    }


}