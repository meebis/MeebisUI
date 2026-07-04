package com.github.meebisui.inventory.pagination;

import com.github.meebisui.inventory.item.FunctionalItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
public class Pagination {

    /**
     * The start slot of where the pagination should start
     */
    private int startSlot;
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
    private int[] ignoredSlots;
}
