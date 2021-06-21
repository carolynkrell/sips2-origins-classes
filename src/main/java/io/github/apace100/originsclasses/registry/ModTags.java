package io.github.apace100.originsclasses.registry;

import io.github.apace100.originsclasses.OriginsClasses;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class ModTags {

    public static final Tag<Block> SEA_BLOCKS = TagRegistry.block(new Identifier(OriginsClasses.MODID, "sea_blocks"));
    public static final Tag<Item> COOKED_FISH = TagRegistry.item(new Identifier(OriginsClasses.MODID, "cooked_fish"));
    public static final Tag<Item> DYES = TagRegistry.item(new Identifier(OriginsClasses.MODID, "dyes"));

    public static void register() {

    }
}
