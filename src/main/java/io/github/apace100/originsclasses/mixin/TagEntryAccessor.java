package io.github.apace100.originsclasses.mixin;

import net.minecraft.item.Item;
import net.minecraft.loot.entry.TagEntry;
import net.minecraft.tag.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TagEntry.class)
public interface TagEntryAccessor {

    @Accessor
    Tag<Item> getName();
}