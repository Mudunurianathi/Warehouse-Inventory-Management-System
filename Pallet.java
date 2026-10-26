/**
 * Project 2: Warehouse Inventory Management System
 *
 * A stack of one kind of item.
 * @author Anathi Mudunuri, CS 18000 
 * @version October 18, 2026
 *
 */

public class Pallet {
    private Item item;
    private int count;


    public Pallet(Item item, int count) {
        this.item = item;
        this.count = count;
        if (item == null || count < 0 || count > 
                getCapacity()) {
            this.item = null;
            this.count = 0;
        }
    }

    public Item getItem() {
        return item;
    }

    public int getCount() {
        return count;
    }

    public int getCapacity() {
        if (item == null) {
            return -1;
        }
        return Policies.getPalletCapacity(item.getSize());
    }

    public int getRemainingCapacity() {
        if (item == null) {
            return -1;
        }
        return getCapacity() - count;
    }

    public boolean isFull() {
        return item != null && count >= getCapacity();
    }

    public boolean isEmpty() {
        return item == null || count == 0;
    }

    public double getWeight() {
        if (item == null) {
            return 0.0;
        }
        return item.getWeight() * count;
    }

    public int addItems(Item item, int quantity) {
        if (item == null || quantity <= 0 
                || (this.item != null && !this.item.equals(item))) {
            return quantity;
        }
        if (this.item == null) {
            this.item = item;
        }
        int toAdd = Math.min(quantity, getRemainingCapacity());
        count += toAdd;
        if (count == 0) {
            this.item = null;
        }
        return quantity - toAdd;
    }

    public int removeItems(Item item, int quantity) {
        if (this.item == null || item == null || quantity <= 0 
                || !this.item.equals(item)) {
            return 0;
        }
        int removed = Math.min(quantity, count);
        count -= removed;
        if (count == 0) {
            this.item = null;
        }
        return removed;
    }

    @Override
    public String toString() {
        return item + " x " + count;
    }

}
