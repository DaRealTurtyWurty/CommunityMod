package io.github.communitymod.common.items;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.core.util.SpawnEggData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class ModSpawnEggItem extends SpawnEggItem {

    protected static final Set<ModSpawnEggItem> UNADDED_EGGS = new HashSet<>();

    private final SpawnEggData eggData;

    private final Supplier<? extends EntityType<?>> entityTypeSupplier;

    public ModSpawnEggItem(@Nonnull final Supplier<? extends EntityType<?>> entity,
            final SpawnEggData eggData) {
        super(null, eggData.primaryColor, eggData.secondaryColor,
                new Properties().tab(CommunityMod.TAB).stacksTo(16));
        this.entityTypeSupplier = entity;
        this.eggData = eggData;
        UNADDED_EGGS.add(this);
    }

    public static void initSpawnEggs() {
        final Map<EntityType<?>, SpawnEggItem> EGGS = ObfuscationReflectionHelper
                .getPrivateValue(SpawnEggItem.class, null, "f_43201_");

        for (final ModSpawnEggItem spawnEgg : UNADDED_EGGS) {
            EGGS.put(spawnEgg.getType(null), spawnEgg);
            DispenserBlock.registerBehavior(spawnEgg, spawnEgg.eggData.getDispenseBehaviour());
        }
        UNADDED_EGGS.clear();
    }

    @Override
    public EntityType<?> getType(final CompoundTag nbt) {
        return this.entityTypeSupplier.get();
    }
}
