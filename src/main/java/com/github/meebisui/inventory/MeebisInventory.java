package com.github.meebisui.inventory;

import com.github.meebisui.MeebisUI;
import com.github.meebisui.inventory.item.FunctionalItem;
import com.github.meebisui.inventory.pagination.Pagination;
import com.github.meebisui.inventory.slot.Slot;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Accessors(fluent = true)
public abstract class MeebisInventory {

    private final Component title;
    private final int rows;
    private final Inventory bukkitInventory;
    private final List<FunctionalItem> functionalItemList = new ArrayList<>();
    private final Map<Integer, FunctionalItem> functionalItemsBySlot = new HashMap<>();
    private Pagination pagination;

    public MeebisInventory(@NotNull Component title, int rows) {
        this.title = title;
        this.rows = rows;
        this.bukkitInventory = Bukkit.createInventory(null, rows * 9, title);
    }

    public void openInventory(@NotNull Player player) {
        if (MeebisUI.INSTANCE().meebisInventoryTrack().containsKey(player)) {
            MeebisUI.INSTANCE().meebisInventoryTrack().remove(player);
        }

        renderPagination();

        player.openInventory(this.bukkitInventory);
        MeebisUI.INSTANCE().meebisInventoryTrack().put(player, this);
    }

    public void closeInventory(@NotNull Player player) {
        player.closeInventory();
        MeebisUI.INSTANCE().meebisInventoryTrack().remove(player);
    }

    public void withItem(@NotNull FunctionalItem functionalItem, int row, int column) {
        int slot = row * 9 + column;
        if (row < 0 || column < 0) {
            throw new RuntimeException("The row or column of a MeebisInventory cannot be less than 0.");
        }
        if (slot >= rows * 9) {
            throw new RuntimeException("The slot cannot be bigger than the actual MeebisInventory size.");
        }
        this.bukkitInventory.setItem(slot, functionalItem.itemStack());
        this.functionalItemsBySlot.put(slot, functionalItem);
        this.functionalItemList.add(functionalItem);
    }

    public void fillInventory(@NotNull FunctionalItem functionalItem) {
        for (int i = 0; i < rows * 9; i++) {
            this.bukkitInventory.setItem(i, functionalItem.itemStack());
            this.functionalItemsBySlot.put(i, functionalItem);
            this.functionalItemList.add(functionalItem);
        }
    }

    public void withPagination(@NotNull Pagination pagination) {
        this.pagination = pagination;

        if (this.pagination.pageSize() < 1) {
            throw new RuntimeException("A pagination's pageSize cannot be smaller than 1");
        }

        this.renderPagination();
    }

    public void refreshPagination() {
        renderPagination();
    }

    private void renderPagination() {
        if (this.pagination == null) return;

        int slot = this.pagination.startSlot().slot();
        int startItem = this.pagination.currentPage() * this.pagination.pageSize();

        int endItem = Math.min(startItem + this.pagination.pageSize(), this.pagination.functionalItems().size());

        Set<Integer> ignored = new HashSet<>();
        for (Slot s : this.pagination.ignoredSlots()) {
            ignored.add(s.slot());
        }

        for (int itemIndex = startItem; itemIndex < endItem; itemIndex++) {

            while (ignored.contains(slot)) {
                slot++;
            }

            this.bukkitInventory.setItem(slot, new ItemStack(Material.AIR));

            FunctionalItem item = this.pagination.functionalItems().get(itemIndex);

            this.bukkitInventory.setItem(slot, item.itemStack());
            this.functionalItemsBySlot.put(slot, item);

            slot++;
        }
    }

    public FunctionalItem functionalItemAt(int slot) {
        return this.functionalItemsBySlot.get(slot);
    }

    public boolean isMeebisInventory(Component inventoryTitle) {
        return this.title.equals(inventoryTitle);
    }

    public boolean isFunctionalItem(ItemStack itemStack) {
        return this.functionalItemsBySlot.values().stream().anyMatch(functionalItem -> functionalItem.itemStack().equals(itemStack));
    }
}