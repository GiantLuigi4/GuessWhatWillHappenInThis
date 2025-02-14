package fr.anatom3000.gwwhit.mixin;

import fr.anatom3000.gwwhit.CustomItemGroups;
import fr.anatom3000.gwwhit.config.ConfigManager;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin {
    private static final FoodComponent DEFAULT_COMPONENT = new FoodComponent.Builder()
            .alwaysEdible()
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100), 0.75f)
            .saturationModifier(1)
            .hunger(-5)
            .build();
    
    @Mutable @Final @Shadow private int maxCount;
    @Mutable @Final @Shadow private FoodComponent foodComponent;
    @Mutable @Final @Shadow protected ItemGroup group;
    
    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(Item.Settings settings, CallbackInfo ci) {
        maxCount = Math.min(ConfigManager.getLoadedConfig().gameplay.items.maxStackSize, maxCount);
        if (foodComponent == null && ConfigManager.getLoadedConfig().gameplay.items.everythingIsEdible)
            foodComponent = DEFAULT_COMPONENT;
        if (group == null && ConfigManager.getLoadedConfig().gameplay.items.hiddenItemsTab)
            group = CustomItemGroups.CURSED_GROUP;
    }
}
