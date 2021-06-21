package io.github.apace100.originsclasses.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleType;

public class ModifyScalePower extends Power {
    private static final ScaleType[] MODIFY_SIZE_TYPES = { ScaleType.WIDTH, ScaleType.HEIGHT, ScaleType.DROPS };

    public final float scale;

    public ModifyScalePower(PowerType<?> type, LivingEntity entity, float scale) {
        super(type, entity);
        this.scale = scale;
    }

    @Override
    public void onAdded() {
        super.onAdded();
        for(ScaleType type : MODIFY_SIZE_TYPES) {
            setScale(scale, type);
        }
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
        for(ScaleType type : MODIFY_SIZE_TYPES) {
            setScale(1.0f, type);
        }
    }

    private void setScale(float scale, ScaleType type) {
        ScaleData scaleData = type.getScaleData(this.entity);
        scaleData.setScale(scale);
    }

}
