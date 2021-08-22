package io.github.communitymod.core.init;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.common.entities.BeanEntity;
import io.github.communitymod.common.entities.GooseEntity;
import io.github.communitymod.common.entities.ThrownStickEntity;
import io.github.communitymod.common.items.ModSpawnEggItem;
import io.github.communitymod.core.util.SpawnEggData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class EntityInit {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister
            .create(ForgeRegistries.ENTITIES, CommunityMod.MODID);

    public static final RegistryObject<EntityType<BeanEntity>> BEAN_ENTITY = ENTITIES.register("bean",
            () -> EntityType.Builder.<BeanEntity>of(BeanEntity::new, MobCategory.MISC).sized(0.8f, 1.75f)
                    .build(new ResourceLocation(CommunityMod.MODID, "bean").toString()));

    public static final RegistryObject<EntityType<GooseEntity>> GOOSE_ENTITY = ENTITIES.register("goose",
            () -> EntityType.Builder.<GooseEntity>of(GooseEntity::new, MobCategory.MISC).sized(0.5f, 1.3f)
                    .build(new ResourceLocation(CommunityMod.MODID, "goose").toString()));

    public static final RegistryObject<EntityType<ThrownStickEntity>> THROWN_STICK = ENTITIES.register("stick",
            () -> EntityType.Builder.<ThrownStickEntity>of(ThrownStickEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(new ResourceLocation(CommunityMod.MODID, "stick").toString()));

    public static void registerSpawnEggs() {
        ItemInit.ITEMS.register("bean_spawn_egg",
                () -> new ModSpawnEggItem(BEAN_ENTITY, new SpawnEggData(0xFFCC88, 0x38FA71)));
        ItemInit.ITEMS.register("goose_spawn_egg",
                () -> new ModSpawnEggItem(GOOSE_ENTITY, new SpawnEggData(0xA3B9CA, 0xB1AA96)));
    }
}
