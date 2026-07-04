package com.github.meebisui.test;

import com.github.meebisui.inventory.item.FunctionalItem;
import com.github.meebisui.inventory.MeebisInventory;
import com.github.meebisui.inventory.pagination.Pagination;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class ShopInventory extends MeebisInventory {

    public ShopInventory() {
        super(Component.text("Demo Title"), 3);

        // Filling the inventory with black glass panes
        fillInventory(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));


        // Set a tnt item with a direct player action
        withItem(FunctionalItem.of(new ItemStack(Material.TNT), player -> {
            player.sendMessage("You've clicked on tnt");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 1);
        }), 2, 5);


        // Set a sponge with no player action, this item can be taken out of the inventory.
        withItem(FunctionalItem.ofEmpty(new ItemStack(Material.SPONGE)), 2, 5);

        // Create a functionalItem list for pagination that contains the item for pagination
        List<FunctionalItem> shopCategories = new ArrayList<>();
        shopCategories.add(FunctionalItem.ofEmpty(new ItemStack(Material.PURPLE_STAINED_GLASS)));
        shopCategories.add(FunctionalItem.ofEmpty(new ItemStack(Material.PAPER)));
        shopCategories.add(FunctionalItem.ofEmpty(new ItemStack(Material.DIRT)));
        shopCategories.add(FunctionalItem.ofEmpty(new ItemStack(Material.APPLE)));

        // Create the pagination itself with a startSlot, ignoredSlots and the items
        Pagination pagination = new Pagination();
        pagination.startSlot(0);
        pagination.pageSize(0);
        pagination.ignoredSlots(new int[]{7, 8, 9});
        pagination.functionalItems(shopCategories);

        // Display the pagination in the inventory
        withPagination(pagination);


    }
}
