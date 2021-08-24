package io.github.communitymod.common.items;

import io.github.communitymod.core.util.ColorConstants;
import io.github.communitymod.core.util.OtherUtils;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * An item that does a random thing.
 *
 * !!!Feel free to add other things this item does!!!
 */
public class OrbOfInsanity extends Item {
    public OrbOfInsanity(final Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level level, final Player player,
            final InteractionHand hand) {
        if (level.isClientSide())
            return InteractionResultHolder.success(player.getItemInHand(hand));
        final Random rand = ThreadLocalRandom.current();
        var message = "If you receive this message, then AAAAAAAAAAAAAAAA";
        final var randomNumb = rand.nextInt(33);
        player.sendMessage(new TextComponent("The orb glows and leaves you with the number " + randomNumb),
                player.getUUID());
        switch (randomNumb) {
        case 0:
            player.kill();
            message = "You are dead.";
            break;
        case 1:
            player.addEffect(new MobEffectInstance(MobEffects.WITHER, 999999, 255, true, true));
            message = "The orb curses you with the wither.";
            break;
        case 2:
            player.addEffect(new MobEffectInstance(MobEffects.POISON, 999999, 255, true, true));
            message = "The orb poisons you.";
            break;
        case 3:
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 999999, 255, true, true));
            message = "The orb makes it practically impossible to walk.";
            break;
        case 4:
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 999999, 255, true, true));
            message = "The orb makes you blind.";
            break;
        case 5:
            player.setSecondsOnFire(255);
            message = "The orb sets you ablaze.";
            break;
        case 6:
            player.fallDistance += 10;
            message = "The orb simulates a fall.";
            break;
        case 7:
            player.experienceLevel = 0;
            message = "The orb saps your experience away.";
            break;
        case 8:
            if (rand.nextBoolean()) {
                message = "The orb enjoys the number 8, and spares you a terrible fate.";
            } else {
                message = "The orb enjoys the number 8, but not enough to spare your life this time.";
                player.kill();
            }
            break;
        case 9:
            message = "The orb deploys its explosive friends on you. Good luck!";
            for (var i = 0; i < 50; i++) {
                final var creeper = new Creeper(EntityType.CREEPER, level);
                creeper.setPos(player.getX(), player.getY(), player.getZ());
                level.addFreshEntity(creeper);
            }
            break;
        case 10:
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20 * 120, 1));
            player.setSecondsOnFire(120);
            message = "The orb blocks your screen with fire!";
            break;
        case 11:
            final var giant = new Giant(EntityType.GIANT, level);
            giant.setPos(player.getX(), player.getY(), player.getZ());
            level.addFreshEntity(giant);
            message = "The orb summons a giant in your mist!";
            break;
        case 12:
            player.getEnderChestInventory().clearContent();
            message = "The orb chuckles, as it consumes the items in your Ender Chest.";
            break;
        case 13:
            player.getInventory().dropAll();
            message = "The orb chuckles a little as it drops all of your items to the ground.";
            break;
        case 14:
            player.getItemInHand(hand).use(level, player, hand);
            message = "The orb is unsure of what to do, and looks to a new number for guidance.";
            break;
        case 15:
            player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 999999, 255, true, true));
            message = "The orb lifts you into the sky, to live in for 13.888875 hours.";
            break;
        case 16:
            player.getInventory().clearContent();
            message = "The orb clears your inventory, bringing itself with it...";
            break;
        case 17:
            player.setHealth(player.getMaxHealth());
            message = "The orb sees to it that your health is restored in-full.";
            break;
        case 18:
            player.experienceLevel *= 3;
            message = "The orb makes your experience rival that of the greats.";
            break;
        case 19:
            player.setHealth(player.getMaxHealth() / 2);
            message = "The orb sets your health to be half of its maximum!";
            break;
        case 20:
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 20 * 10, 255));
            message = "The orb grants you the health of a god, even if only for a short amount of time.";
            break;
        case 21:
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 8, 9));
            message = "Star music intensifies.";
            break;
        case 22:
            player.spawnAtLocation(Items.DIAMOND);
            message = "The orb grants you a single diamond.";
            break;
        case 23:
            player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 20 * 300, 10));
            message = "The orb grants you a significant boost to your maximum health.";
            break;
        case 24:
            final var elytra = new ItemStack(Items.ELYTRA);
            elytra.enchant(Enchantments.MENDING, 1);
            elytra.enchant(Enchantments.UNBREAKING, 10);
            elytra.setRepairCost(0);
            player.spawnAtLocation(elytra);
            message = "The orb grants you a very useful set of wings. Use them wisely!";
            break;
        case 25:
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 999999, 4));
            message = "The orb grants you insane healing potential. Don't let it go to waste.";
            break;
        case 26:
            player.spawnAtLocation(Items.DIRT);
            message = "The orb has granted you a piece of dirt.";
            break;
            case 27:
                player.spawnAtLocation(new ItemStack(Items.DIRT, 2));
                message = "The orb has granted you two pieces of dirt. Fascinating!";
                break;
            case 28:
                final var stand = new ArmorStand(EntityType.ARMOR_STAND, level);
                stand.setPos(player.getX(), player.getY(), player.getZ());
                level.addFreshEntity(stand);
                message = "The orb gives you an armor stand.";
                break;
            case 29:
                player.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1));
                message = "The orb poisons you and raises your ability to heal.";
                break;
            case 30:
                player.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 1, 1));
                message = "The orb has given you a bad omen, but only for a fraction of a second.";
                break;
            case 31:
                List<Player> players = new ArrayList<>();
                Player[] allPlayers = (Player[]) player.level.players().toArray();
                for (Player player1 : allPlayers) {
                    if (OtherUtils.distanceOf(player, player1) <= 5) {
                        players.add(player1);
                    }
                }
                if (players.size() > 1) {
                    message = ColorConstants.RED + "The orb does not like how you and " + ColorConstants.DARK_RED + (players.size() - 1) + ColorConstants.RED + (players.size() - 1 > 1 ? " other people" : " other person") + " are so close together. Wear masks!";
                    players.forEach(player2 -> player2.hurt(DamageSource.GENERIC, player2.getMaxHealth() / 2f));
                }
                break;
            case 32:
                final var builder = new StringBuilder();
                builder.append("This is the orb's message. Nothing else, just this message. ");
                var numsAdded = 0; // Max 100.
                while (numsAdded < 100 && rand.nextInt(100) != 19) {
                    builder.append("This is not the orb's message: ").append(rand.nextInt(999999999))
                            .append("! ");
                    numsAdded++;
                }
                message = builder.toString();
                break;
            default:
                break;
        }
        player.sendMessage(new TextComponent(message), player.getUUID());
        player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), 20);
        if (!player.isCreative()) {
            player.getItemInHand(hand).shrink(1);
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
