package com.github.meebisui.inventory.item;

public enum FunctionalItemType {

    ACTIONABLE, // FunctionalItems that have a direct player action and cannot be taken out of the inventory
    TAKEABLE, // FunctionalItems that can be taken out of the inventory without any action
    CANCELLED, // FunctionalItems that cannot be taken out of the inventory and have no action

}
