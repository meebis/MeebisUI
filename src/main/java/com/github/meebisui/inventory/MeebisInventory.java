package com.github.meebisui.inventory;

import com.github.meebisui.MeebisUI;
import com.github.meebisui.inventory.item.FunctionalItem;
import com.github.meebisui.inventory.pagination.Pagination;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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
        this.bukkitInventory = Bukkit.createInventory(null, 3 * rows, title);
    }

    public void openInventory(@NotNull Player player) {
        if (MeebisUI.INSTANCE.meebisInventoryTrack().containsKey(player)) {
            MeebisUI.INSTANCE.meebisInventoryTrack().remove(player);
        }

        if (this.pagination != null) {
            int start = this.pagination.startSlot();
            int end = start + this.pagination.pageSize();
            int page = this.pagination.currentPage();

            for (int slot = start; slot < end; slot++) {
                final int slotIndex = slot;

                boolean ignored = Arrays.stream(this.pagination.ignoredSlots())
                        .anyMatch(s -> s == slotIndex);
                if (ignored) continue;

                this.bukkitInventory.clear(slotIndex);
                this.functionalItemsBySlot.remove(slotIndex);

                int itemIndex = (page * this.pagination.pageSize()) + (slotIndex - start);
                List<FunctionalItem> items = this.pagination.functionalItems();

                if (itemIndex < items.size()) {
                    FunctionalItem functionalItem = items.get(itemIndex);
                    this.bukkitInventory.setItem(slotIndex, functionalItem.itemStack());
                    this.functionalItemsBySlot.put(slotIndex, functionalItem);
                    this.functionalItemList.add(functionalItem);
                }
            }
        }

        player.openInventory(this.bukkitInventory);
        MeebisUI.INSTANCE.meebisInventoryTrack().put(player, this);
    }

    public void closeInventory(@NotNull Player player) {
        player.closeInventory();
        MeebisUI.INSTANCE.meebisInventoryTrack().remove(player);
    }

    public void withItem(@NotNull FunctionalItem functionalItem, int row, int column) {
        if (row < 0 || column < 0) {
            throw new RuntimeException("The row or column of a MeebisInventory cannot be less than 0.");
        }

        int slot = row * 9 + column;
        this.bukkitInventory.setItem(slot, functionalItem.itemStack());
        this.functionalItemsBySlot.put(slot, functionalItem);
        this.functionalItemList.add(functionalItem);
    }

    public void fillInventory(@NotNull ItemStack itemStack) {
        for (int i = 0; i < rows * 9; i++) {
            this.bukkitInventory.setItem(i, itemStack);
        }
    }

    public void withPagination(@NotNull Pagination pagination) {
        this.pagination = pagination;

        if (this.pagination.pageSize() < 1) {
            throw new RuntimeException("A pagination's pageSize cannot be smaller than 1");
        }
    }

    public FunctionalItem functionalItemAt(int slot) {
        return this.functionalItemsBySlot.get(slot);
    }

    public boolean isMeebisInventory(Component inventoryTitle) {
        return this.title.equals(inventoryTitle);
    }

    public boolean isFunctionalItem(ItemStack itemStack) {
        return this.functionalItemsBySlot.values().stream()
                .anyMatch(functionalItem -> functionalItem.itemStack().equals(itemStack));
    }
}