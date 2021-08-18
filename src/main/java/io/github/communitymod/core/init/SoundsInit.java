package io.github.communitymod.core.init;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.core.util.ModResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public final class SoundsInit {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
            CommunityMod.MODID);

    public static final RegistryObject<SoundEvent> MUSIC_DISC_BEANAL = SOUNDS.register("music.record.beanal",
            () -> new SoundEvent(new ModResourceLocation("music.record.beanal")));

    static final Set<RegistryObject<Block>> BLOCK_ITEM_BLACKLIST = new HashSet<>();

    public static void registerSounds() {

    }

}
