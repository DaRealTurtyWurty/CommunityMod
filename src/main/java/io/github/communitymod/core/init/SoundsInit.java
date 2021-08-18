package io.github.communitymod.core.init;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.core.util.ModResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class SoundsInit {

	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
			CommunityMod.MODID);

	public static final RegistryObject<SoundEvent> MUSIC_DISC_BEANAL = SOUNDS.register("music.record.beanal",
			() -> new SoundEvent(new ModResourceLocation("music.record.beanal")));
}
