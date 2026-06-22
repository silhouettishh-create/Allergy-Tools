package com.example.allergymod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class AllergyMod implements ModInitializer {

    public static final String MOD_ID = "allergymod";

    @Override
    public void onInitialize() {
        AllergyItems.initialize();
        ServerTickEvents.END_SERVER_TICK.register(AllergyTickHandler::onEndTick);
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                AllergyTickHandler.removePlayer(handler.getPlayer().getUUID()));
    }
}
