/**
 * Project 2: Warehouse Inventory Management System
 *
 * Manages the grid of shelves, adding, removing, 
 * finding, counting, and moving pallets.
 * @author Anathi Mudunuri, CS 18000 
 * @version October 18, 2026
 *
 */

public class Warehouse {
    private Shelf[][] shelves;

    public Warehouse(int rows, int columns) {
        shelves = new Shelf[rows][columns];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                shelves[r][c] = new Shelf(Policies.getShelfCapacity(), 
                        Policies.getShelfWeightLimit());
            }
        }
    }

    public Shelf[][] getShelves() {
        return shelves;
    }

    private int unitsByWeight(Shelf shelf, Item item, int wanted) {
        int units = 0;
        double weight = shelf.getCurrentWeight();
        while (units < wanted && weight + item.getWeight() 
                <= shelf.getWeightLimit() + Policies.getDelta()) {
            weight += item.getWeight();
            units++;
        }
        return units;
    }

    public int addItems(Item item, int quantity) {
        if (item == null || quantity <= 0) {
            return quantity;
        }
        int remaining = quantity;
        for (Shelf[] row : shelves) {
            for (Shelf shelf : row) {
                for (Pallet pallet : shelf.getPallets()) {
                    if (remaining > 0 && pallet != null 
                            && item.equals(pallet.getItem())) {
                        int canAdd = Math.min(remaining, pallet.getRemainingCapacity());
                        canAdd = unitsByWeight(shelf, item, canAdd);
                        remaining -= canAdd - pallet.addItems(item, canAdd); 
                    }
                }
            }
        }
        int capacity = Policies.getPalletCapacity(item.getSize());
        for (Shelf[] row : shelves) {
            for (Shelf shelf : row) {
                int amount = unitsByWeight(shelf, item, 
                        Math.min(remaining, capacity));
                while (amount > 0 && shelf.hasOpenPositions()) {
                    shelf.addPallet(new Pallet(item, amount));
                    remaining -= amount;
                    amount = unitsByWeight(shelf, item, 
                            Math.min(remaining, capacity));
                }
            }
        }
        return remaining;
    }

    public int removeItems(Item item, int quantity) {
        if (item == null || quantity  <= 0) {
            return 0;
        }
        int removed = 0;
        for (Shelf[] row : shelves) {
            for (Shelf shelf : row) {
                Pallet[] pallets = shelf.getPallets();
                for (int i = 0; i < pallets.length; i++) {
                    if (removed < quantity && pallets[i] != null) {
                        removed += pallets[i].removeItems(item, quantity - removed);
                        if (pallets[i].isEmpty()) {
                            shelf.removePallet(i);
                        }
                    }
                }
            }
        }
        return removed;
    }

    public int getItemCount(Item item) {
        int total = 0;
        for (Shelf[] row : shelves) {
            for (Shelf shelf : row) {
                for (Pallet pallet : shelf.getPallets()) {
                    if (pallet != null && pallet.getItem() 
                            != null && pallet.getItem().equals(item)) {
                        total += pallet.getCount();
                    }
                }
            }
        }
        return total;
    }

    public int[] findItem(Item item) {
        int[] location = {-1, -1};
        for (int r = 0; r < shelves.length; r++) {
            for (int c = 0; c < shelves[r].length; c++) {
                if (location[0] == -1 && shelves[r][c].findItem(item) != -1) {
                    location[0] = r;
                    location[1] = c;
                }
            }
        }
        return location;
    }

    public boolean movePallet(int sourceRow, int sourceColumn, 
            int palletIndex, int destinationRow, int destinationColumn) {
        if (sourceRow < 0 || sourceRow >= shelves.length || sourceColumn < 0 
                || sourceColumn >= shelves[sourceRow].length || destinationRow < 0 
                || destinationRow >= shelves.length 
                || destinationColumn < 0 
                || destinationColumn >= shelves[destinationRow].length) {
            return false;
        }
        if (sourceRow == destinationRow && sourceColumn == destinationColumn) {
            return false;
        }
        Shelf source = shelves[sourceRow][sourceColumn];
        Shelf destination = shelves[destinationRow][destinationColumn];
        Pallet pallet = source.removePallet(palletIndex);
        if (pallet == null) {
            return false;
        }
        if (destination.addPallet(pallet)) {
            return true;
        }
        source.getPallets()[palletIndex] = pallet;
        return false;
    }


}
