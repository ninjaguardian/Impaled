package org.ladysnake.impaled.common.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.ladysnake.impaled.common.Impaled;
import org.ladysnake.impaled.common.init.ImpaledItems;
import org.ladysnake.sincereloyalty.SincereLoyalty;

public class ImpaledItemGroup {
    public static final ItemGroup IMPALED_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(Impaled.MODID, "impaled"),
            FabricItemGroup.builder().displayName(Text.translatable("itemGroup.impaled"))
                    .icon(() -> new ItemStack(ImpaledItems.HELLFORK)).entries((displayContext, entries) -> {
                        entries.add(ImpaledItems.ATLAN);
                        entries.add(ImpaledItems.ANCIENT_TRIDENT);
                        entries.add(ImpaledItems.HELLFORK);
                        entries.add(ImpaledItems.SOULFORK);
                        entries.add(ImpaledItems.ELDER_TRIDENT);
                        entries.add(ImpaledItems.ELDER_GUARDIAN_EYE);
                        entries.add(ImpaledItems.PITCHFORK);
                        entries.add(ImpaledItems.MAELSTROM);
                        entries.add(ImpaledItems.TRIDENT_UPGRADE_SMITHING_TEMPLATE);
                        entries.add(SincereLoyalty.LOYALTY_UPGRADE_SMITHING_TEMPLATE);
                    }).build());

    public static void init() {}
}
