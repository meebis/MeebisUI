package com.github.meebisui.inventory;

import com.github.meebisui.MeebisUI;
import com.github.meebisui.inventory.item.FunctionalItem;
import com.github.meebisui.inventory.pagination.Pagination;
import com.github.meebisui.inventory.slot.Slot;
import com.github.meebisui.utility.Pair;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Accessors(fluent = true)
public abstract class MeebisInventory {

    private final Component title;
    private final int rows;
    private final Inventory bukkitInventory;
    //private final List<FunctionalItem> functionalItemList = new ArrayList<>();
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
    }

    public void fillInventory(@NotNull FunctionalItem functionalItem) {
        for (int i = 0; i < rows * 9; i++) {
            this.bukkitInventory.setItem(i, functionalItem.itemStack());
            this.functionalItemsBySlot.put(i, functionalItem);
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

        int startSlot = this.pagination.startSlot().slot();
        int pageSize = this.pagination.pageSize();

        int startItem = this.pagination.currentPage() * pageSize;
        int endItem = Math.min(startItem + pageSize, this.pagination.functionalItems().size());

        Set<Integer> ignored = new HashSet<>();
        for (Slot s : this.pagination.ignoredSlots()) {
            ignored.add(s.slot());
        }

        int cleared = 0;
        int slot = startSlot;

        while (cleared < pageSize) {
            if (!ignored.contains(slot)) {
                FunctionalItem backgroundItem = this.pagination.backgroundItem();
                this.bukkitInventory.setItem(slot, backgroundItem != null ? backgroundItem.itemStack() : null);
                // So if the backgroundItem is not null, we need to store its functionalItem in our map
                // in order for our PlayerInventoryClickListener to work and listen for certain actions on that slot.
                if (backgroundItem != null) {
                    this.functionalItemsBySlot.put(slot, backgroundItem);
                } else {
                    // If the backgroundItem is null, which it freely can be, then it just doesn't have any action
                    // and we also dont need any action, because if its null, then the slot is just Empty (AIR)
                    // and we remove it from the map because else the next page in the pagination recognizes that slot
                    this.functionalItemsBySlot.remove(slot);
                }
                cleared++;
            }
            slot++;
        }

        slot = startSlot;

        for (int itemIndex = startItem; itemIndex < endItem; itemIndex++) {

            while (ignored.contains(slot)) {
                slot++;
            }

            FunctionalItem item = this.pagination.functionalItems().get(itemIndex);

            this.bukkitInventory.setItem(slot, item.itemStack());
            this.functionalItemsBySlot.put(slot, item);

            slot++;
        }
    }

    /**
     * The page indicators can be used with a pagination
     * so players can use two items (previous and next) to switch
     * to the next pagination page.
     */
    public void withPageIndicator(Pair<Slot, ItemStack> previous, Pair<Slot, ItemStack> next) {
        if (this.pagination == null) {
            throw new RuntimeException("Cannot set page indicators when no pagination is set");
        }

        FunctionalItem previousItem = FunctionalItem.of(previous.second(), event -> {
            if (pagination.currentPage() <= 0) {
                return;
            }
            pagination.currentPage(pagination.currentPage() - 1);
            this.refreshPagination();
        });


        FunctionalItem nextItem = FunctionalItem.of(next.second(), event -> {
            if (pagination.currentPage() + 1 >= pagination.totalPages()) {
                return;
            }
            pagination.currentPage(pagination.currentPage() + 1);
            this.refreshPagination();
        });

        this.bukkitInventory.setItem(previous.first().slot(), previousItem.itemStack());
        this.functionalItemsBySlot.put(previous.first().slot(), previousItem);

        this.bukkitInventory.setItem(next.first().slot(), nextItem.itemStack());
        this.functionalItemsBySlot.put(next.first().slot(), nextItem);
    }

    @ApiStatus.Experimental
    public void updateItemAt(int slot) {
        FunctionalItem functionalItem = this.functionalItemAt(slot);
        if (functionalItem == null) return;

        this.bukkitInventory.setItem(slot, functionalItem.itemStack());
    }

    public void updateItemAt(Slot slot, ItemStack itemStack) {
        FunctionalItem functionalItem = this.functionalItemAt(slot.slot());
        if (functionalItem == null) return;

        this.bukkitInventory.setItem(slot.slot(), itemStack);
        this.functionalItemsBySlot.put(slot.slot(), FunctionalItem.of(itemStack, functionalItem.clickAction()));
    }

    @ApiStatus.Experimental
    @SuppressWarnings("ALL")
    public void updateItemLore(Slot slot, List<Component> lore) {
        FunctionalItem functionalItem = this.functionalItemAt(slot.slot());
        if (functionalItem == null) return;

        ItemStack itemStack = functionalItem.itemStack();
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(lore));

        this.bukkitInventory.setItem(slot.slot(), itemStack);
    }

    @ApiStatus.Experimental
    @SuppressWarnings("ALL")
    public void updateItemLore(FunctionalItem functionalItem, Slot slot, List<Component> lore) {
        if (functionalItem == null) return;

        ItemStack itemStack = functionalItem.itemStack();
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(lore));

        this.bukkitInventory.setItem(slot.slot(), itemStack);
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