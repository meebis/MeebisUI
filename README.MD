<h1>MeebisUI</h1>
<p>a basic inventory framework by meebis</p>

<h2>This basic spigot inventory framework provides:</h2>
- Simple Inventory creation class based, everything is done in one Inventory Class, no listener etc.
- It has its own FunctionalItem with a ItemStack and Click callback
- Pagination including update to update single items on click or the whole pagination

<h2>InventoryFillType</h2> 
- A Type Enum to easily fill a complete inventory, corners or left / right side of a inventory with a specific FuntionalItem.

<h2>Pagination</h2>
- A basic pagination to have multiple items in a inventory that automatically delivers multiple pages depending on the actual paginations item size and page size.

<br>
<br>
<h3>Many more to come when i need something my own when using :p</h3>

<h2>Usage</h2>
- To use MeebisUI you simple need the jitpack repository and the meebisUI with the latest commit hash, for Example:
- The important 

``maven { url = uri("https://jitpack.io") }``

``implementation("com.github.meebis:MeebisUI:68f30df")``
