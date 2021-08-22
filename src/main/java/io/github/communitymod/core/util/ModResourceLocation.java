package io.github.communitymod.core.util;

import io.github.communitymod.CommunityMod;
import net.minecraft.resources.ResourceLocation;

public class ModResourceLocation extends ResourceLocation {
    public ModResourceLocation(final String string) {
        super(CommunityMod.MODID, string);
    }
}
