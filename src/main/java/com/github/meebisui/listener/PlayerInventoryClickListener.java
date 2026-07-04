package com.github.meebisui.listener;

import com.github.meebisui.MeebisUI;
import com.github.meebisui.inventory.MeebisInventory;
import com.github.meebisui.inventory.item.FunctionalItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PlayerInventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            MeebisInventory meebisInventory = MeebisUI.INSTANCE.meebisInventoryTrack().get(player);

            if (meebisInventory != null && meebisInventory.isMeebisInventory(event.getView().title())) {
                FunctionalItem functionalItem = meebisInventory.functionalItemAt(event.getSlot());
                if (functionalItem == null) return;

                functionalItem.clickAction().accept(player);
            }
        }
    }
}
