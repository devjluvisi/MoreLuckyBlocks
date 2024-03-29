package devjluvisi.mlb.api.gui.pages;

import devjluvisi.mlb.api.gui.utils.Coords2D;
import org.apache.commons.lang.Validate;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

/**
 * Type of a page, related to various containers in Minecraft.
 */
public enum PageType {

    CHEST(3, 9, InventoryType.CHEST), CHEST_PLUS(4, 9, InventoryType.CHEST), CHEST_PLUS_PLUS(5, 9, InventoryType.CHEST),
    DOUBLE_CHEST(6, 9, InventoryType.CHEST), DISPENSER(3, 3, InventoryType.DISPENSER),
    HOPPER(1, 9, InventoryType.HOPPER);

    private final int row;
    private final int column;
    private final InventoryType inventoryType;

    PageType(int row, int column, InventoryType inventoryType) {
        this.row = row;
        this.column = column;
        this.inventoryType = inventoryType;
    }

    /**
     * Gets the 2D coordinates of the item with a given index.
     *
     * @param index the index of the item in the content array
     * @return the coordinates of the item
     */
    public Coords2D getCoords2DFromIndex(int index) {
        return new Coords2D(index / this.getRow(), (index % this.getRow()));
    }

    /**
     * @return the number of rows
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Gets the index the item with coordinates.
     *
     * @param coords2D the coordinates of the item
     * @return the index in the content of the item
     */
    public int getIndexFrom2D(Coords2D coords2D) {
        return (this.getColumn() * coords2D.getY()) + coords2D.getX();
    }

    /**
     * @return the number of columns
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * Reshape a flat array to match shape of the page.
     *
     * @param content the original array
     * @return the reshaped 2-dimensional array
     * @throws IllegalArgumentException if the array does not match the shape of the
     *                                  page
     */
    public ItemStack[][] reshapeIn2D(ItemStack[] content) {
        Validate.isTrue(content.length == this.getSize());

        final ItemStack[][] rtn = this.getBlank2DArray();

        int y = 0;
        int x = 0;

        for (final ItemStack item : content) {
            rtn[y][x] = item;
            x++;
            if (x == 9) {
                x = 0;
                y++;
            }
        }

        return rtn;
    }

    /**
     * @return the number of slots
     */
    public int getSize() {
        return this.row * this.column;
    }

    /**
     * Gets a blank array (filled with null) with the shape of the page. It gives a
     * 2-dimensional array (an array of rows).
     *
     * @return an array
     */
    public ItemStack[][] getBlank2DArray() {
        return new ItemStack[this.getRow()][this.getColumn()];
    }

    /**
     * Flatten the 2-dimensional array (which must match the shape of the page).
     *
     * @param content the 2-dimensional array
     * @return the flatten array * @throws IllegalArgumentException if the array
     * does not match the shape of the page
     */
    public ItemStack[] flatten(ItemStack[][] content) {
        Validate.isTrue(content.length == this.getRow(), "The number of rows does not match the shape of the page, "
                + content.length + " instead of " + this.getRow());

        final ItemStack[] rtn = this.getBlankArray();

        int y = 0;

        for (final ItemStack[] column : content) {

            Validate.isTrue(column.length == this.getColumn(),
                    "The number of columns does not match the shape of the page, " + column.length + " instead of "
                            + this.getColumn());

            int x = 0;

            for (final ItemStack item : column) {
                rtn[(y * this.getColumn()) + x] = item;
                x++;
            }

            y++;
        }

        return rtn;
    }

    /**
     * Gets a blank array (filled with null) with the size of the page.
     *
     * @return an array
     */
    public ItemStack[] getBlankArray() {
        return new ItemStack[this.getSize()];
    }

    /**
     * @return the inventory type of the page
     */
    public InventoryType getInventoryType() {
        return this.inventoryType;
    }
}
