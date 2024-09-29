package org.ladysnake.impaled.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;
import org.ladysnake.impaled.common.init.ImpaledEntityTypes;
import org.ladysnake.impaled.common.init.ImpaledItems;
import org.ladysnake.impaled.common.item.ImpaledItemGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Impaled implements ModInitializer {
    public static final String MODID = "impaled";
    public static final Logger LOGGER = LogManager.getLogger("Impaled");

    private static final Identifier BASTION_TREASURE_CHEST_LOOT_TABLE_ID = new Identifier("minecraft", "chests/bastion_treasure");

    public static Identifier id(String path) {
        return new Identifier(MODID, path);
    }

    @Override
    public void onInitialize() {
        ImpaledEntityTypes.init();
        ImpaledItems.init();
        ImpaledItemGroup.init();

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (BASTION_TREASURE_CHEST_LOOT_TABLE_ID.equals(id)) {
                tableBuilder.pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1))  // Number of rolls (1 item)
                                .with(ItemEntry.builder(ImpaledItems.ANCIENT_TRIDENT))
                                .conditionally(RandomChanceLootCondition.builder(0.6f))  // 60% chance
                                .build()
                );
            }
        });
    }
}
