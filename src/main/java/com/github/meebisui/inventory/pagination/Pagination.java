package com.github.meebisui.inventory.pagination;

import com.github.meebisui.inventory.item.FunctionalItem;
import com.github.meebisui.inventory.slot.Slot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
@Accessors(fluent = true)
public class Pagination {

    /**
     * The start slot of where the pagination should start
     */
    private Slot startSlot;
    /**
     * The size of how many items should be display per page
     */
    private int pageSize;
    /**
     * The currentPage that is used currently
     */
    private int currentPage;
    /**
     * The items list of what should be displayed
     */
    private List<FunctionalItem> functionalItems;
    /**
     * The slots what items are ignored when being displayed
     */
    private Slot[] ignoredSlots = new Slot[0];

    /**
     * This item can be defined if you want to set a default fallback / background
     * item when a pagination page is cleared.
     * <p>
     * For example:
     * When having 7 items per page and your pagination item list has 11 elements
     * it displays 7 on the first and 4 on the second page, but that pagination page
     * expects 7 items on that page so the other 3 slots are going to be empty (AIR).
     * <p>
     * When defining this item, the other 3 slots are being replaced by this item.
     * <p>
     * This is good, if you fill your inventory with glass panes, and you want
     * the other 3 slots to be glass panes too instead of empty (AIR).
     * <p>
     * If not defined, the other 3 slots are going to be empty (AIR)!
     */
    private ItemStack backgroundItemSlot;

    public int totalPages() {
        return (int) Math.ceil((double) functionalItems.size() / pageSize);
    }
}
