package io.github.apace100.originsclasses.condition;

import io.github.apace100.origins.power.factory.condition.ConditionFactory;
import io.github.apace100.origins.registry.ModRegistries;
import io.github.apace100.origins.util.SerializableData;
import io.github.apace100.originsclasses.OriginsClasses;
import io.github.apace100.originsclasses.data.ClassesDataTypes;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ClassesBlockConditions {

    public static void register() {
        register(new ConditionFactory<>(new Identifier(OriginsClasses.MODID, "material"), new SerializableData()
            .add("material", ClassesDataTypes.MATERIAL),
            (data, block) -> block.getBlockState().getMaterial() == data.get("material")));
    }

    private static void register(ConditionFactory<CachedBlockPosition> conditionFactory) {
        Registry.register(ModRegistries.BLOCK_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
