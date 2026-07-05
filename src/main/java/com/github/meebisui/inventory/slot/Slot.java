package com.github.meebisui.inventory.slot;

public record Slot(int slot) {

    /**
     * Returns a Slot with direct bukkit inventory slot parameter
     *
     * @param slot 0-54 in the biggest inventory
     */
    public static Slot slot(int slot) {
        return new Slot(slot);
    }

    /**
     * Returns a slot with the row and column specified parameter
     */
    public static Slot slot(int row, int column) {
        return new Slot(row * 9 + column);
    }
}
