/**
 * Project 2: Warehouse Inventory Management System
 *
 * Represents a type of product, with a name, 
 * item number, weight, and size.
 * @author Anathi Mudunuri, CS 18000 
 * @version October 18, 2026
 *
 */

public class Item {
    private String name;
    private int itemNumber;
    private double weight;
    private char size;


    public Item(String name, int itemNumber, double weight, char size) {
        this.name = name;
        this.itemNumber = itemNumber;
        this.weight = weight;
        this.size = size;
    }


    public String getName() {
        return name;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public double getWeight() {
        return weight;
    }

    public char getSize() {
        return size;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setSize(char size) {
        this.size = size;
    }


    @Override
    public String toString() {
        return name + " (#" + itemNumber + ") - " 
            + String.format("%.2f", weight) 
                + " lbs - Size " + size;

    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item other = (Item) o;
        return name.equals(other.name)
                && itemNumber == other.itemNumber
                && Math.abs(weight - other.weight) < Policies.getDelta()
                && size == other.size;
    }
}
