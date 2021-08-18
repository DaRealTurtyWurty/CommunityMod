package io.github.communitymod.core.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.communitymod.CommunityMod;

import io.github.communitymod.common.items.ModSpawnEggItem;
import io.github.communitymod.common.items.SpecialItem;


import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.HashSet;
import java.util.Set;

public class ModDataGeneration {
    private static final Logger DATAGEN_LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();



    /*
    * ----------------------SERVER SIDE DATAGEN----------------------
     */

    public static final class RecipeGen extends RecipeProvider {
        public RecipeGen(DataGenerator gen) {
            super(gen);
        }

        @Override
        protected void buildCraftingRecipes(@Nonnull Consumer<FinishedRecipe> consumer) {
            // bean block from beans
            ShapedRecipeBuilder.shaped(BlockInit.BEAN_BLOCK.get()).pattern("###").pattern("###").pattern("###").define('#', ItemInit.BEANS.get()).unlockedBy("has_beans", has(ItemInit.BEANS.get())).save(consumer, new ModResourceLocation("bean_block_from_beans"));
            // beans from block
            ShapelessRecipeBuilder.shapeless(ItemInit.BEANS.get(), 9).requires(BlockInit.BEAN_BLOCK.get()).unlockedBy("has_bean_block", has(BlockInit.BEAN_BLOCK.get())).save(consumer, new ModResourceLocation("beans_from_block"));
            // bean soup
            ShapelessRecipeBuilder.shapeless(ItemInit.BEAN_SOUP.get()).requires(Items.BOWL).unlockedBy("has_bowl", has(Items.BOWL)).requires(ItemInit.BEANS.get()).requires(ItemInit.BEANS.get()).requires(ItemInit.BEANS.get()).requires(ItemInit.BEANS.get()).requires(ItemInit.BEANS.get()).requires(ItemInit.BEANS.get()).requires(ItemInit.BEANS.get()).requires(ItemInit.BEANS.get()).unlockedBy("has_beans", has(ItemInit.BEANS.get())).save(consumer);
            // orb of insanity
            ShapedRecipeBuilder.shaped(ItemInit.ORB_OF_INSANITY.get()).pattern("RRR").pattern("RPR").pattern("RRR").define('R', Items.ROTTEN_FLESH).unlockedBy("has_rotten_flesh", has(Items.ROTTEN_FLESH)).define('P', Items.POISONOUS_POTATO).unlockedBy("has_poisonous_potato", has(Items.POISONOUS_POTATO)).save(consumer);
            // special item
            ShapedRecipeBuilder.shaped(ItemInit.SPECIAL_ITEM.get()).pattern("GGG").pattern(" GG").pattern(" G ").define('G', Items.GRAY_DYE).unlockedBy("has_gray_dye", has(Items.GRAY_DYE)).save(consumer);
            // bean hat
            ShapedRecipeBuilder.shaped(ItemInit.BEAN_HAT.get()).pattern("#B#").pattern("# #").define('#', Items.LEATHER).unlockedBy("has_leather", has(Items.LEATHER)).define('B', ItemInit.BEANS.get()).unlockedBy("has_beans", has(ItemInit.BEANS.get())).save(consumer);
            // bean belt
            ShapedRecipeBuilder.shaped(ItemInit.BEAN_BELT.get()).pattern(" # ").pattern("#L#").pattern(" B ").define('#', Items.LEATHER).unlockedBy("has_leather", has(Items.LEATHER)).define('L', Items.LEAD).unlockedBy("has_lead", has(Items.LEAD)).define('B', ItemInit.BEANS.get()).unlockedBy("has_beans", has(ItemInit.BEANS.get())).save(consumer);

            // TODO: Missing Da Dog Hand
        }
    }

    public static final class BlockTagsGen extends BlockTagsProvider {
        public BlockTagsGen(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
            super(generatorIn, CommunityMod.MODID, existingFileHelper);
        }
    }

    public static final class ItemTagsGen extends ItemTagsProvider {
        public ItemTagsGen(DataGenerator gen, BlockTagsProvider provider,
                           ExistingFileHelper existingFileHelper) {
            super(gen, provider, CommunityMod.MODID, existingFileHelper);
        }

        @Override
        protected void addTags() {
        }
    }


    public static final class AdvancementGen extends AdvancementProvider {
        private final DataGenerator generator;

        public AdvancementGen(DataGenerator generatorIn) {
            super(generatorIn);
            this.generator = generatorIn;
        }

        private void registerAdvancements(Consumer<Advancement> consumer) {
        }

        @Override
        public void run(@Nullable HashCache cache) {
            Path outputFolder = this.generator.getOutputFolder();
            Consumer<Advancement> consumer = (advancement) -> {

                Path path = outputFolder.resolve("data/" + advancement.getId().getNamespace() + "/advancements/"
                        + advancement.getId().getPath() + ".json");
                try {
                    DataProvider.save(GSON, cache, advancement.deconstruct().serializeToJson(), path);
                    DATAGEN_LOGGER.debug("Creating advancement {}", advancement.getId());
                } catch (IOException e) {
                    DATAGEN_LOGGER.error("Couldn't create advancement {}", path, e);
                }
            };
            registerAdvancements(consumer);
        }
    }

    public static final class LootTableGen extends LootTableProvider {

        public LootTableGen(DataGenerator dataGeneratorIn) {
            super(dataGeneratorIn);
        }

       @Override
        protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
            return ImmutableList.of(
                    Pair.of(Blocks::new, LootContextParamSets.BLOCK),
                    Pair.of(Entities::new, LootContextParamSets.ENTITY)
            );
        }

        @Override
        protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationTracker) {
            map.forEach((resourceLocation, lootTable) -> LootTables.validate(validationTracker, resourceLocation, lootTable));
        }

        public static class Blocks extends BlockLoot {
            @Override
            protected void addTables() {
                this.add(BlockInit.BEAN_BLOCK.get(), createBeanDrop(ItemInit.BEANS.get()));
            }

            protected static LootTable.Builder createBeanDrop(ItemLike pItem) {
                return LootTable.lootTable().withPool(applyExplosionCondition(pItem, LootPool.lootPool().setRolls(ConstantValue.exactly(9.0F)).add(LootItem.lootTableItem(pItem))));
            }

            @Override
            protected Iterable<Block> getKnownBlocks() {
                return BlockInit.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList());
            }
        }

        public static class Entities extends EntityLoot {
            @Override
            protected void addTables() {
                this.add(EntityInit.BEAN_ENTITY.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(3.0F)).add(LootItem.lootTableItem(ItemInit.BEANS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
            }

            @Override
            protected Iterable<EntityType<?>> getKnownEntities() {
                return EntityInit.ENTITIES.getEntries().stream().map(Supplier::get).collect(Collectors.toList());
            }
        }

    }



    /*
    * ----------------------CLIENT SIDE DATAGEN----------------------
     */

    public static class LanguageGen extends LanguageProvider {

        public LanguageGen(DataGenerator gen, String locale) {
            super(gen, CommunityMod.MODID, locale);
        }

        @Override
        protected void addTranslations() {
            String locale = this.getName().replace("Languages: ", "");
            switch (locale) {
                case "en_us" -> { 
                    add(BlockInit.BEAN_BLOCK.get(), "Block of Beans");
                    add(EntityInit.BEAN_ENTITY.get(), "Bean");

                    //items
                    add(ItemInit.SPECIAL_ITEM.get(), "Test Item");
                    ItemInit.ITEMS.getEntries().stream().filter(item -> !(item.get() instanceof SpecialItem)).forEach(item -> {
                        String name = WordUtils.capitalize(item.getId().getPath().replace("_", " "));
                        addItem(item, name);
                    });

                    //tooltips
                    addToolTip(ItemInit.BEAN_BELT.get(), "Equip and press %3$s%2$sSHIFT%1$s to go %4$sboom%1$s!");

                    // music disc desc
                    addMusicDisc(ItemInit.MUSIC_DISC_BEANAL.get(), "LudoCrypt - beanal");

                    //item group
                    add(CommunityMod.TAB.getDisplayName().getString(), "Community Mod");
                }
                default -> throw new IllegalStateException("Unsupported language " + locale + "!");
            }
        }

        private void addMusicDisc(Item item, String musicDescTranslation) {
            add(item.getDescriptionId() + ".desc", musicDescTranslation);
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
