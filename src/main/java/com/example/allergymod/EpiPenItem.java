package com.example.allergymod;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EpiPenItem extends Item {

    public EpiPenItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemInteractionResult useOn(net.minecraft.world.item.context.UseOnContext context) {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public net.minecraft.world.InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            if (player.hasEffect(MobEffects.POISON)) {
                player.removeEffect(MobEffects.POISON);
                player.hurt(player.damageSources().generic(), 4.0f);
                AllergyTickHandler.resetTimer(player.getUUID());
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                return net.minecraft.world.InteractionResult.CONSUME;
            }
        }

        return net.minecraft.world.InteractionResult.PASS;
    }
}
