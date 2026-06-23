package com.example.allergymod;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AllergyTickHandler {

    private static final Map<UUID, Integer> TOOL_TICKS = new HashMap<>();
    private static final int MAX_TICKS = 200;

    public static void onEndTick(MinecraftServer server) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            UUID id = player.getUUID();
            ItemStack held = player.getMainHandItem();

            if (isTool(held.getItem())) {
                int ticks = TOOL_TICKS.getOrDefault(id, 0) + 1;
                TOOL_TICKS.put(id, Math.min(ticks, MAX_TICKS));

                if (ticks >= MAX_TICKS && !player.hasEffect(MobEffects.POISON)) {
                    player.addEffect(new MobEffectInstance(
                            MobEffects.POISON, Integer.MAX_VALUE, 0));
                }
            } else {
                // Reset timer when not holding a tool
                TOOL_TICKS.put(id, TOOL_TICKS.getOrDefault(id, 0));
            }
        }
    }

    public static void resetTimer(UUID id) {
        TOOL_TICKS.put(id, 0);
    }

    public static void removePlayer(UUID id) {
        TOOL_TICKS.remove(id);
    }

    private static boolean isTool(Item item) {
        return item instanceof net.minecraft.world.item.DiggerItem
                || item instanceof net.minecraft.world.item.SwordItem
                || item instanceof net.minecraft.world.item.FishingRodItem
                || item instanceof net.minecraft.world.item.ShearsItem
                || item instanceof net.minecraft.world.item.FlintAndSteelItem
                || item instanceof net.minecraft.world.item.BowItem
                || item instanceof net.minecraft.world.item.CrossbowItem
                || item instanceof net.minecraft.world.item.TridentItem
                || item instanceof net.minecraft.world.item.ShieldItem
                || item instanceof net.minecraft.world.item.BrushItem;
    }
}
