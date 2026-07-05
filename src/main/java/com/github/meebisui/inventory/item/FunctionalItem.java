package com.github.meebisui.inventory.item;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@Getter
@Accessors(fluent = true)
public class FunctionalItem {

    private final ItemStack itemStack;
    private final Consumer<Player> clickAction;
    private final FunctionalItemType type;

    private FunctionalItem(ItemStack itemStack, Consumer<Player> clickAction, FunctionalItemType type) {
        this.itemStack = itemStack;
        this.clickAction = clickAction;
        this.type = type;
    }

    /**
     * FunctionalItem with a itemStack and a direct player action
     * This item cannot be taken out of the inventory
     * because it executes a player direct action
     *
     * @param itemStack   the itemStack, cannot be null
     * @param clickAction the player direct action, cannot be null else use this#ofEmpty
     * @return a FunctionalItem instance with given clickAction
     */
    public static FunctionalItem of(@NotNull ItemStack itemStack, @NotNull Consumer<Player> clickAction) {
        return new FunctionalItem(itemStack, clickAction, FunctionalItemType.ACTIONABLE);
    }

    /**
     * FunctionalItem with a itemStack and NO player action.
     * This item can later be taken out of the inventory
     *
     * @param itemStack the itemStack, cannot be null
     * @return a FunctionalItem instance with empty clickAction
     */
    public static FunctionalItem ofEmpty(@NotNull ItemStack itemStack) {
        return new FunctionalItem(itemStack, null, FunctionalItemType.TAKEABLE);
    }

    /**
     * FunctionalItem with a itemStack and NO player action.
     * This item cannot be taken out of the inventory
     *
     * @param itemStack the itemStack, cannot be null
     * @return a FunctionalItem instance with empty clickAction
     */
    public static FunctionalItem ofCancelled(@NotNull ItemStack itemStack) {
        return new FunctionalItem(itemStack, null, FunctionalItemType.CANCELLED);
    }
}