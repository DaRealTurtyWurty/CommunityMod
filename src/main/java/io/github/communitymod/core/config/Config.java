package io.github.communitymod.core.config;

import io.github.communitymod.core.init.ItemInit;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config {

    public static final Client CLIENT;

    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder()
                .configure(Common::new);
        COMMON_SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();

        final Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder()
                .configure(Client::new);
        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
    }

    public static class Client {

        public final ForgeConfigSpec.ConfigValue<String> tabIcon;

        public Client(final ForgeConfigSpec.Builder builder) {
            this.tabIcon = builder
                    .comment("Resource location of the item to apply to the mod's creative tab icon")
                    .define("Creative Tab Icon", ItemInit.BEANS.getId().toString());
        }
    }

    public static class Common {

        public final ForgeConfigSpec.ConfigValue<String> example;

        public Common(final ForgeConfigSpec.Builder builder) {
            builder.comment("Example category. You can remove this if you add a new one.").push("example");
            this.example = builder.comment("Example config field. You can remove this if you add a new one.")
                    .define("Example", "Beans");
            builder.pop();
        }
    }
}
