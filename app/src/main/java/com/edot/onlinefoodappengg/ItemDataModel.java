package com.edot.onlinefoodappengg;

import java.io.Serializable;
import java.util.Objects;

public final class ItemDataModel implements Serializable {

    private final String name;
    private int quantity = 0;
    private final int cost;

    public ItemDataModel(String name, int cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDataModel that = (ItemDataModel) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public int incrementQuantity(int count)
    {
        quantity = quantity + count;
        return quantity;
    }

    public int decrementQuantity(int count)
    {
        quantity = (count < quantity)? quantity-count : 0;
        return quantity;
    }

}
