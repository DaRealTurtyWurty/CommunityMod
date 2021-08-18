package io.github.communitymod.core.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.communitymod.CommunityMod;
import io.github.communitymod.core.init.BlockInit;
import io.github.communitymod.core.init.EntityInit;
import io.github.communitymod.core.init.ItemInit;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class ModDataGeneration {
    private static final Logger DATAGEN_LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static class LanguageGen extends LanguageProvider {

        public LanguageGen(DataGenerator gen, String locale) {
            super(gen, CommunityMod.MODID, locale);
        }

        @Override
        protected void addTranslations() {
            String locale = this.getName().replace("Languages: ", "");
            switch (locale) {
                case "en_us":
                    add(BlockInit.BEAN_BLOCK.get(), "Block of Beans");
                    add(EntityInit.BEAN_ENTITY.get(), "Bean");
                    add(ItemInit.BEANS.get(), "Beans");
                    add(ItemInit.BEANS_SANDWICH.get(), "Beans Sandwitch");
                    add(ItemInit.BEAN_SOUP.get(), "Bean Soup");
                    add(ItemInit.MIGUEL_OF_FORTUNE.get(), "Miguel of Fortune");
                    add(CommunityMod.TAB.getDisplayName().getString(), "Community Mod");

                    addSpawnEgg("bean_spawn_egg", "Spawn Bean");
                    break;
                default:
                    DATAGEN_LOGGER.error("Unsupported language {}!", locale);
                    break;
            }
        }

        public void addSpawnEgg(String name, String translation) {
            add("item." + CommunityMod.MODID + "." + name, translation);
        }
    }

    public static class ItemModelGen extends ItemModelProvider {
        private final Set<Item> blacklist = new HashSet<>();

        public ItemModelGen(DataGenerator gen, ExistingFileHelper existingFileHelper) {
            super(gen, CommunityMod.MODID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
            for (ResourceLocation id : ForgeRegistries.ITEMS.getKeys()) {
                Item item = ForgeRegistries.ITEMS.getValue(id);
                if (item != null && CommunityMod.MODID.equals(id.getNamespace()) && !this.blacklist.contains(item)) {
                    if (item instanceof BlockItem) {
                        return;
                    } else {
                        this.defaultItem(id, item);
                    }
                }
            }
        }

        private void defaultItem(ResourceLocation id, Item item) {
            this.withExistingParent(id.getPath(), "item/generated").texture("layer0",
                    new ResourceLocation(id.getNamespace(), "item/" + id.getPath()));
            DATAGEN_LOGGER.debug("Generated item model for: " + item.getRegistryName());
        }
    }

    public static class BlockStateGen extends BlockStateProvider {
        public BlockStateGen(DataGenerator gen, ExistingFileHelper exFileHelper) {
            super(gen, CommunityMod.MODID, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
            simpleBlock(BlockInit.BEAN_BLOCK.get());
        }
    }
}
