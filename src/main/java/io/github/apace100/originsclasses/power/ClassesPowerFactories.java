package io.github.apace100.originsclasses.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.VariableIntPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.originsclasses.OriginsClasses;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.util.*;

public class ClassesPowerFactories {

    @SuppressWarnings("unchecked")
    public static void register() {
        register(new PowerFactory<>(new Identifier(OriginsClasses.MODID, "craft_amount"),
                new SerializableData()
                        .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                        .add("modifier", SerializableDataTypes.ATTRIBUTE_MODIFIER, null)
                        .add("modifiers", SerializableDataTypes.ATTRIBUTE_MODIFIERS, null),
                data ->
                        (type, player) -> {
                            CraftAmountPower power = new CraftAmountPower(type, player, data.isPresent("item_condition") ?
                                    (ConditionFactory<ItemStack>.Instance)data.get("item_condition") : (stack -> true));
                            if(data.isPresent("modifier")) {
                                power.addModifier(data.getModifier("modifier"));
                            }
                            if(data.isPresent("modifiers")) {
                                ((List<EntityAttributeModifier>)data.get("modifiers"))
                                        .forEach(power::addModifier);
                            }
                            return power;
                        }));
        register(new PowerFactory<>(new Identifier(OriginsClasses.MODID,"starting_equipment"),
                new SerializableData()
                        .add("stack", ApoliDataTypes.POSITIONED_ITEM_STACK, null)
                        .add("stacks", ApoliDataTypes.POSITIONED_ITEM_STACKS, null),
                data ->
                        (type, player) -> {
                            io.github.apace100.originsclasses.power.StartingEquipmentPower power = new io.github.apace100.originsclasses.power.StartingEquipmentPower(type, player);
                            if(data.isPresent("stack")) {
                                Pair<Integer, ItemStack> stack = (Pair<Integer, ItemStack>)data.get("stack");
                                int slot = stack.getLeft();
                                if(slot > Integer.MIN_VALUE) {
                                    power.addStack(stack.getLeft(), stack.getRight());
                                } else {
                                    power.addStack(stack.getRight());
                                }
                            }
                            if(data.isPresent("stacks")) {
                                ((List<Pair<Integer, ItemStack>>)data.get("stacks"))
                                        .forEach(integerItemStackPair -> {
                                            int slot = integerItemStackPair.getLeft();
                                            if(slot > Integer.MIN_VALUE) {
                                                power.addStack(integerItemStackPair.getLeft(), integerItemStackPair.getRight());
                                            } else {
                                                power.addStack(integerItemStackPair.getRight());
                                            }
                                        });
                            }
                            return power;
                        }));
        register(new PowerFactory<>(new Identifier(OriginsClasses.MODID, "modify_scale"),
                new SerializableData().add("scale_amount", SerializableDataTypes.FLOAT, 1.0F),
                (data) -> (type, player) -> {
                    return new ModifyScalePower(type, player, data.getFloat("scale_amount"));
                }).allowCondition());
        register(new PowerFactory<>(new Identifier(OriginsClasses.MODID, "lumberjack"),
                new SerializableData(),
                data ->
                        (type, entity) -> new MultiMinePower(type, entity, (pl, bs, bp) -> {
                            Set<BlockPos> affected = new HashSet<>();
                            Queue<BlockPos> queue = new LinkedList<>();
                            queue.add(bp);
                            boolean foundOneWithLeaves = false;
                            BlockPos.Mutable pos = bp.mutableCopy();
                            BlockPos.Mutable newPos = bp.mutableCopy();
                            while(!queue.isEmpty()) {
                                pos.set(queue.remove());
                                for(int dx = -1; dx <= 1; dx++) {
                                    for(int dy = 0; dy <= 1; dy++) {
                                        for(int dz = -1; dz <= 1; dz++) {
                                            if(dx == 0 & dy == 0 && dz == 0) {
                                                continue;
                                            }
                                            newPos.set(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
                                            BlockState state = pl.world.getBlockState(newPos);
                                            if(state.isOf(bs.getBlock()) && !affected.contains(newPos)) {
                                                BlockPos savedNewPos = newPos.toImmutable();
                                                affected.add(savedNewPos);
                                                queue.add(savedNewPos);
                                                if(affected.size() > 255) {
                                                    if(!foundOneWithLeaves) {
                                                        return new ArrayList<>();
                                                    }
                                                    return new ArrayList<>(affected);
                                                }
                                            } else
                                            if((BlockTags.LEAVES.contains(state.getBlock()) || state.getBlock() instanceof LeavesBlock) && !state.get(LeavesBlock.PERSISTENT)) {
                                                foundOneWithLeaves = true;
                                            }
                                        }
                                    }
                                }
                            }
                            if(!foundOneWithLeaves) {
                                affected.clear();
                            }
                            return new ArrayList<>(affected);
                        }, state -> BlockTags.LOGS.contains(state.getBlock())).addCondition(p -> p.getMainHandStack().getItem() instanceof AxeItem)
        ));
        register(new PowerFactory<>(new Identifier(OriginsClasses.MODID, "variable_int"),
                new SerializableData()
                        .add("start_value", SerializableDataTypes.INT, null)
                        .add("min", SerializableDataTypes.INT, Integer.MIN_VALUE)
                        .add("max", SerializableDataTypes.INT, Integer.MAX_VALUE),
                data ->
                        (type, entity) -> new VariableIntPower(type, entity, data.getInt("start_value"), data.getInt("min"), data.getInt("max"))));
    }

    private static void register(PowerFactory<?> factory) {
        Registry.register(ApoliRegistries.POWER_FACTORY, factory.getSerializerId(), factory);
    }
}