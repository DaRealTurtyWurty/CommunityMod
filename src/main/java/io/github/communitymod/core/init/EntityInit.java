package io.github.communitymod.core.init;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.common.entities.BeanEntity;
import io.github.communitymod.common.items.ModSpawnEggItem;
import io.github.communitymod.core.util.SpawnEggData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class EntityInit {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES,
            CommunityMod.MODID);

    public static final RegistryObject<EntityType<BeanEntity>> BEAN_ENTITY = ENTITIES.register("bean",
            () -> EntityType.Builder.<BeanEntity>of(BeanEntity::new, MobCategory.MISC).sized(0.8f, 1.75f)
                    .build(new ResourceLocation(CommunityMod.MODID, "bean").toString()));

    public static void registerSpawnEggs() {
        ItemInit.ITEMS.register("bean_spawn_egg",
                () -> new ModSpawnEggItem(BEAN_ENTITY, new SpawnEggData(0xFFCC88, 0x38FA71)));
    }
}
