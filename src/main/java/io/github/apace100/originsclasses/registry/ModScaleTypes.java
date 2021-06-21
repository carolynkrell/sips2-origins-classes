package io.github.apace100.originsclasses.registry;

import io.github.apace100.origins.Origins;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.TypedScaleModifier;

import java.util.Map;

public class ModScaleTypes {
    private static final ScaleType[] MODIFY_SIZE_TYPES = {ScaleType.WIDTH, ScaleType.HEIGHT, ScaleType.DROPS};

    public static final ScaleModifier MODIFY_SIZE_MODIFIER = register(ScaleRegistries.SCALE_MODIFIERS, new TypedScaleModifier(() -> ModScaleTypes.MODIFY_SIZE_TYPE));

    public static final ScaleType MODIFY_SIZE_TYPE = register(ScaleRegistries.SCALE_TYPES, ScaleType.Builder.create().addDependentModifier(MODIFY_SIZE_MODIFIER).affectsDimensions().build());

    private static <T> T register(Map<Identifier, T> registry, T entry) {
        return ScaleRegistries.register(registry, new Identifier(Origins.MODID, "modify_size"), entry);
    }

    public static void init() {
        for (ScaleType type : MODIFY_SIZE_TYPES) {
            type.getDefaultBaseValueModifiers().add(MODIFY_SIZE_MODIFIER);
        }
    }
}
