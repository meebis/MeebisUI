# MeebisUI

A lightweight inventory framework for Spigot plugins.

Create interactive inventories with a clean class-based system. No listeners required, everything is handled directly inside your inventory classes.

## Features

- Simple class-based inventory creation
- `FunctionalItem` system with click callbacks
- Built-in pagination support
- Easy inventory filling with `InventoryFillType`
- Update single items or complete pages

# Installation

## Repository

Add the JitPack repository:

```kotlin
repositories {
    maven {
        url = uri("https://jitpack.io")
    }
}
```

## Dependency

Add MeebisUI with the latest commit hash:

```kotlin
dependencies {
    implementation("com.github.meebis:MeebisUI:<commit-hash>")
}
```

Example:

```kotlin[ShopInventory.java](../TestPlugin/src/main/java/com/github/meebis/test/ShopInventory.java)
dependencies {
    implementation("com.github.meebis:MeebisUI:68f30df")
}
```

---

# Setup

MeebisUI is a standalone library and needs to be shaded into your Spigot plugin.

Before using any inventories, initialize MeebisUI with your `JavaPlugin` instance.

```java
MeebisUI.INSTANCE().loadPlugin(this);
```

Example:

```java
public class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        MeebisUI.INSTANCE().loadPlugin(this);
    }
}
```

---

# Usage

After initialization, you can create inventories using MeebisUI.

Example Inventory (A bit messy but still easy to understand if you know java):

And fairly good code comments that explain what stuff does.

If you need help with something or have any other issue, create a pull request.
```java
public class TestInventory extends MeebisInventory {

    public TestInventory() {
        super(Component.text("Test Inventory"), 5);

        // Filling the inventory with black glass panes
        ItemStack backgroundItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        fillInventory(FunctionalItem.ofCancelled(backgroundItem));


        ItemBuilder tntItemBuilder = ItemBuilder.of(Material.TNT)
                .name(Component.text("Tnt Dummy"))
                .lore(Component.text("§c" + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis())));


        FunctionalItem tntItem = FunctionalItem.of(tntItemBuilder.build(), event -> {
            if(event.getWhoClicked() instanceof Player player) {
                if(event.getClick().equals(ClickType.LEFT)) {
                    player.sendMessage("You left clicked on tnt");
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 1);
                }
                if(event.getClick().equals(ClickType.RIGHT)) {
                    player.sendMessage("You right-clicked on tnt");
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100, 1);
                }

                updateItemAt(Slot.of(1,3), tntItemBuilder.lore(Component.text("§c" + new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()))).build());
                player.updateInventory();
            }
        });
        // Set a tnt item with a direct player action
        withItem(tntItem, 1, 3);


        // Set a sponge with no player action, this item can be taken out of the inventory.
        withItem(FunctionalItem.ofEmpty(new ItemStack(Material.SPONGE)), 1, 5);

        // Create a functionalItem list for pagination that contains the item for pagination
        List<FunctionalItem> shopCategories = new ArrayList<>();
        shopCategories.add(FunctionalItem.ofCancelled(new ItemStack(Material.PURPLE_STAINED_GLASS)));
        shopCategories.add(FunctionalItem.ofCancelled(new ItemStack(Material.PAPER)));
        shopCategories.add(FunctionalItem.ofCancelled(new ItemStack(Material.DIRT)));
        shopCategories.add(FunctionalItem.ofCancelled(new ItemStack(Material.GRASS_BLOCK)));
        shopCategories.add(FunctionalItem.ofCancelled(new ItemStack(Material.HAY_BLOCK)));
        shopCategories.add(FunctionalItem.of(new ItemStack(Material.WHEAT_SEEDS), event -> {
            if(event.getWhoClicked() instanceof Player player) {
                player.sendMessage("You clicked on the paginated wheat seeds!");
                player.getInventory().addItem(new ItemStack(Material.WHEAT_SEEDS));
            }
        }));
        shopCategories.add(FunctionalItem.ofCancelled(new ItemStack(Material.PUMPKIN)));
        shopCategories.add(FunctionalItem.ofCancelled(new ItemStack(Material.EMERALD)));
        shopCategories.add(FunctionalItem.ofCancelled(new ItemStack(Material.ACACIA_LEAVES)));
        shopCategories.add(FunctionalItem.ofCancelled(new ItemStack(Material.ANDESITE_SLAB)));
        shopCategories.add(FunctionalItem.of(new ItemStack(Material.APPLE), event -> {
            if(event.getWhoClicked() instanceof Player player) {
                player.sendMessage("You clicked on the paginated apple!");
                player.getInventory().addItem(new ItemStack(Material.APPLE));
            }
        }));

        // Create the pagination itself with a startSlot, ignoredSlots and the items
        Pagination pagination = new Pagination();
        pagination.startSlot(Slot.of(3,0)); // The slot where it starts displaying
        pagination.pageSize(7); // How many items per page should be displayed
        pagination.ignoredSlots(new Slot[]{Slot.of(3,0), Slot.of(3,8)});
        pagination.backgroundItem(FunctionalItem.ofCancelled(backgroundItem));
        pagination.functionalItems(shopCategories);

        // Display the pagination in the inventory
        withPagination(pagination);

        ItemBuilder nextPage = ItemBuilder.of(Material.ARROW)
                .name(Component.text("§aNext page"))
                .lore(Component.text("§7Page: §a" + (pagination.currentPage()+1) + "/" + pagination.totalPages()));
        ItemBuilder previousPage = ItemBuilder.of(Material.ARROW)
                .name(Component.text("§cPrevious Page"))
                .lore(Component.text("§7Page: §a" + (pagination.currentPage() == 0 ? 0 : pagination.currentPage()-1) + "/" + pagination.totalPages()));

        withPageIndicator(
                new Pair<>(Slot.of(0, 0), previousPage.build()),
                new Pair<>(Slot.of(0,8), nextPage.build())
        );

    }
}
```

---

# Future Plans

MeebisUI will continue to grow with features that are useful when building Spigot inventories.

More features and improvements will be added when i need that stuff too and when i have time ;)