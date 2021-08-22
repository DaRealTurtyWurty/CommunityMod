package io.github.communitymod.core.init;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.core.util.ModResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class SoundsInit {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister
            .create(ForgeRegistries.SOUND_EVENTS, CommunityMod.MODID);

    public static final RegistryObject<SoundEvent> MUSIC_DISC_BEANAL = SOUNDS.register("music.record.beanal",
            () -> new SoundEvent(new ModResourceLocation("music.record.beanal")));

    public static final RegistryObject<SoundEvent> MUSIC_DISC_CHEESE = SOUNDS.register("music.record.cheese",
            () -> new SoundEvent(new ModResourceLocation("music.record.cheese")));

    public static final RegistryObject<SoundEvent> MUSIC_DISC_SOVIET = SOUNDS.register("music.record.ussr",
            () -> new SoundEvent(new ModResourceLocation("music.record.ussr")));

    public static final RegistryObject<SoundEvent> MUSIC_DISC_DICE = SOUNDS.register("music.record.dice",
            () -> new SoundEvent(new ModResourceLocation("music.record.dice")));

    public static final RegistryObject<SoundEvent> WHAT = SOUNDS.register("items.misc.what",
            () -> new SoundEvent(new ModResourceLocation("items.misc.what")));

    public static final RegistryObject<SoundEvent> BONK = SOUNDS.register("items.misc.bonk",
            () -> new SoundEvent(new ModResourceLocation("items.misc.bonk")));
}