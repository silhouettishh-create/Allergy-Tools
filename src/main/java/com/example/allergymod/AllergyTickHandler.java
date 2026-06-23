package com.example.allergymod;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;

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
        // TieredItem covers all pickaxes, axes, shovels, hoes, swords
        if (item instanceof TieredItem) return true;

        String name = item.getClass().getSimpleName().toLowerCase();
        return name.contains("fishing") || name.contains("shears")
                || name.contains("flintandsteel") || name.contains("bow")
                || name.contains("crossbow") || name.contains("trident")
                || name.contains("shield") || name.contains("brush");
    }
}
