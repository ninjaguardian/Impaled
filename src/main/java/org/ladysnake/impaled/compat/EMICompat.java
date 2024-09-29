package org.ladysnake.impaled.compat;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.*;
import dev.emi.emi.recipe.EmiSmithingRecipe;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.ladysnake.impaled.common.init.ImpaledItems;
import org.ladysnake.sincereloyalty.LoyalTrident;
import org.ladysnake.sincereloyalty.LoyaltyBindingRecipe;
import org.ladysnake.sincereloyalty.SincereLoyalty;

@EmiEntrypoint
public class EMICompat implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        for (SmithingRecipe recipe : getRecipes(registry, RecipeType.SMITHING)) {
            //addRecipeSafe(registry, () -> new EmiSmithingRecipe(recipe), recipe);
            if (recipe instanceof LoyaltyBindingRecipe loyaltyRecipe) {
                if (!EnchancementCompat.enabled) {
                    addRecipeSafe(registry, () -> new EmiSmithingRecipe(
                            EmiIngredient.of(loyaltyRecipe.getTemplate()),
                            EmiIngredient.of(Ingredient.ofStacks(fixBaseItem(loyaltyRecipe.getBase().getMatchingStacks()[0]))),
                            EmiIngredient.of(loyaltyRecipe.getAddition()),
                            EmiStack.of(getOutputFromInput(loyaltyRecipe.getBase(),true)),
                            loyaltyRecipe.getId()
                    ), recipe);
                } else {
                    boolean loyaltyAllowed = EnchancementUtil.isEnchantmentAllowed(Enchantments.LOYALTY);
                    boolean singleLevel = ModConfig.singleLevelMode;
                    boolean allTridentsHaveLoyalty = ModConfig.allTridentsHaveLoyalty;
                    if (loyaltyAllowed&&!singleLevel) {
                        // Modify input and output
                        addRecipeSafe(registry, () -> new EmiSmithingRecipe(
                                EmiIngredient.of(loyaltyRecipe.getTemplate()),
                                EmiIngredient.of(Ingredient.ofStacks(fixBaseItem(loyaltyRecipe.getBase().getMatchingStacks()[0]))),
                                EmiIngredient.of(loyaltyRecipe.getAddition()),
                                EmiStack.of(getOutputFromInput(loyaltyRecipe.getBase(),loyaltyAllowed)),
                                loyaltyRecipe.getId()
                        ), recipe);
                    } else if (allTridentsHaveLoyalty&&!loyaltyAllowed) {
                        // Modify input
                        addRecipeSafe(registry, () -> new EmiSmithingRecipe(
                                EmiIngredient.of(loyaltyRecipe.getTemplate()),
                                EmiIngredient.of(loyaltyRecipe.getBase()),
                                EmiIngredient.of(loyaltyRecipe.getAddition()),
                                EmiStack.of(getOutputFromInput(loyaltyRecipe.getBase(),loyaltyAllowed)),
                                loyaltyRecipe.getId()
                        ), recipe);
                    }
                }
            }
        }


        Identifier soulforkLanternId = new Identifier("impaled","/trident_conversion/soulfork/lantern");
        addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
                .id(soulforkLanternId)
                .leftInput(EmiIngredient.of(Ingredient.ofStacks(new ItemStack(ImpaledItems.HELLFORK))))
                .rightInput(EmiStack.of(new ItemStack(Blocks.SOUL_LANTERN)).setRemainder(EmiStack.of(Blocks.LANTERN)), false)
                .output(EmiStack.of(ImpaledItems.SOULFORK))
                .build(), soulforkLanternId);

        Identifier soulforkCampfireId = new Identifier("impaled","/trident_conversion/soulfork/campfire");
        addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
                .id(soulforkCampfireId)
                .leftInput(EmiIngredient.of(Ingredient.ofStacks(new ItemStack(ImpaledItems.HELLFORK))))
                .rightInput(EmiStack.of(new ItemStack(Blocks.SOUL_CAMPFIRE)).setRemainder(EmiStack.of(Blocks.CAMPFIRE)), false)
                .output(EmiStack.of(ImpaledItems.SOULFORK))
                .build(), soulforkCampfireId);

        Identifier soulforkTorchId = new Identifier("impaled","/trident_conversion/soulfork/torch");
        addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
                .id(soulforkTorchId)
                .leftInput(EmiIngredient.of(Ingredient.ofStacks(new ItemStack(ImpaledItems.HELLFORK))))
                .rightInput(EmiStack.of(new ItemStack(Blocks.SOUL_TORCH)).setRemainder(EmiStack.of(Blocks.TORCH)), false)
                .output(EmiStack.of(ImpaledItems.SOULFORK))
                .build(), soulforkTorchId);

        Identifier hellforkLanternId = new Identifier("impaled","/trident_conversion/hellfork/soul_lantern");
        addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
                .id(hellforkLanternId)
                .leftInput(EmiIngredient.of(Ingredient.ofStacks(new ItemStack(ImpaledItems.SOULFORK))))
                .rightInput(EmiStack.of(new ItemStack(Blocks.LANTERN)).setRemainder(EmiStack.of(Blocks.SOUL_LANTERN)), false)
                .output(EmiStack.of(ImpaledItems.HELLFORK))
                .build(), hellforkLanternId);

        Identifier hellforkCampfireId = new Identifier("impaled","/trident_conversion/hellfork/soul_campfire");
        addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
                .id(hellforkCampfireId)
                .leftInput(EmiIngredient.of(Ingredient.ofStacks(new ItemStack(ImpaledItems.SOULFORK))))
                .rightInput(EmiStack.of(new ItemStack(Blocks.CAMPFIRE)).setRemainder(EmiStack.of(Blocks.SOUL_CAMPFIRE)), false)
                .output(EmiStack.of(ImpaledItems.HELLFORK))
                .build(), hellforkCampfireId);

        Identifier hellforkTorchId = new Identifier("impaled","/trident_conversion/hellfork/soul_torch");
        addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
                .id(hellforkTorchId)
                .leftInput(EmiIngredient.of(Ingredient.ofStacks(new ItemStack(ImpaledItems.SOULFORK))))
                .rightInput(EmiStack.of(new ItemStack(Blocks.TORCH)).setRemainder(EmiStack.of(Blocks.SOUL_TORCH)), false)
                .output(EmiStack.of(ImpaledItems.HELLFORK))
                .build(), hellforkTorchId);

        Identifier elderGuardianEyeId = new Identifier("impaled","/info/elder_guardian_eye");
        addRecipeSafe(registry, () -> new EmiInfoRecipe(
                List.of(EmiStack.of(new ItemStack(ImpaledItems.ELDER_GUARDIAN_EYE))),
                List.of(Text.of("Obtained when killing an Elder Guardian with any trident.")),
                elderGuardianEyeId
        ), elderGuardianEyeId);
    }

    private static <C extends Inventory, T extends Recipe<C>> Iterable<T> getRecipes(EmiRegistry registry, RecipeType<T> type) {
        return registry.getRecipeManager().listAllOfType(type).stream()::iterator;
    }

    private static String getFormattedClientUsername() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player != null) {
            return "§k"+player.getName().getString();
        }

        return "§kusername";
    }

    private static void addRecipeSafe(EmiRegistry registry, Supplier<EmiRecipe> supplier, Recipe<?> recipe) {
        try {
            registry.addRecipe(supplier.get());
        } catch (Throwable e) {
            SincereLoyalty.LOGGER.warn("Exception thrown when parsing recipe {}", recipe.getId());
            SincereLoyalty.LOGGER.error(e);
        }
    }

    private static void addRecipeSafe(EmiRegistry registry, Supplier<EmiRecipe> supplier, Identifier id) {
        try {
            registry.addRecipe(supplier.get());
        } catch (Throwable e) {
            SincereLoyalty.LOGGER.warn("Exception thrown when parsing recipe {}", id);
            SincereLoyalty.LOGGER.error(e);
        }
    }

    private static ItemStack getOutputFromInput(ItemStack item, boolean addEnchantment) {
        ItemStack trident = item.copy();

        if (addEnchantment) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(trident);

            // Set the Loyalty enchantment to the maximum level + 1
            enchantments.put(Enchantments.LOYALTY, Enchantments.LOYALTY.getMaxLevel() + 1);
            // Apply the modified enchantments back to the trident
            EnchantmentHelper.set(enchantments, trident);
        }

        NbtCompound loyaltyData = trident.getOrCreateSubNbt(LoyalTrident.MOD_NBT_KEY);
        loyaltyData.putString(LoyalTrident.OWNER_NAME_NBT_KEY, getFormattedClientUsername());

        return trident;
    }

    private static ItemStack fixBaseItem(ItemStack input) {
        ItemStack result = input.copy();
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(result);
        enchantments.put(Enchantments.LOYALTY, Enchantments.LOYALTY.getMaxLevel());
        EnchantmentHelper.set(enchantments, result);
        return result;
    }

    private static ItemStack getOutputFromInput(Ingredient base, boolean loyaltyAllowed) {
        return getOutputFromInput(base, loyaltyAllowed, 0);
    }

    private static ItemStack getOutputFromInput(Ingredient base, boolean loyaltyAllowed, Integer item_idx) {
        return getOutputFromInput(base.getMatchingStacks()[item_idx], loyaltyAllowed);
    }

    private static ItemStack getOutputFromInput(boolean loyaltyAllowed) {
        return getOutputFromInput(new ItemStack(Items.TRIDENT), loyaltyAllowed);
    }
}
