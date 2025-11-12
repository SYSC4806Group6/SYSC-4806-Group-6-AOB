package org.example.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue
    private long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.ALL)
    private List<ShoppingCartItem> items;

    /**
     * Default constructor
     */
    public ShoppingCart() {
        this.items = new ArrayList<ShoppingCartItem>();
    }

    /**
     * Initialize a shoppingcart with a user attached to it
     * @param user User associated with the shopping cart
     */
    public ShoppingCart(User user) {
        this.user = user;
        this.user.setShoppingCart(this);
        this.items = new ArrayList<ShoppingCartItem>();
    }

    /**
     * adds a ShoppingCartItem to the ShoppingCart
     * If book already exists in the ShoppingCart, increment the quantity
     * @param item The item to be added
     * @return True if added/incremented, False otherwise
     */
    public boolean addShoppingCartItem(ShoppingCartItem item) {
        if (items.contains(item)) {
            ShoppingCartItem currItem = items.get(items.indexOf(item));
            currItem.setQuantity(currItem.getQuantity() + 1);
            return true;
        }
        return this.items.add(item);
    }

    /**
     * removes a ShoppingCartItem from the ShoppingCart
     * If book already exists in the ShoppingCart, decrement the quantity
     * @param item The item to be removed
     * @return True if removed/decremented, False otherwise
     */
    public boolean removeShoppingCartItem(ShoppingCartItem item) {
        if (items.contains(item)) {
            int currentItemQuantity = items.get(items.indexOf(item)).getQuantity();
            if (currentItemQuantity > 1) {
                items.get(items.indexOf(item)).setQuantity(currentItemQuantity - 1);
                return true;
            }
        }
        return this.items.remove(item);
    }

    /**
     * Calculate the total cart cost
     * @return double - Total price of the cart
     */
    public double getAndCalculateTotalCartPrice() {
        double totalCartCost = 0.00;
        for (ShoppingCartItem item : this.items) {
            totalCartCost += item.getAndCalculateTotalItemPrice();
        }
        return totalCartCost;
    }

    public int getTotalNumBooks() {
        int totalNumBooks = 0;
        for (ShoppingCartItem item : this.items) {
            totalNumBooks += item.getQuantity();
        }
        return totalNumBooks;
    }

    /* Getters and Setters */
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public User getUser() {
        return this.user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public List<ShoppingCartItem> getItems() {
        return this.items;
    }
    public void setItems(List<ShoppingCartItem> items) {
        this.items = items;
    }

    /**
     * Consider equal if all attributes equal
     * @param o   the reference object with which to compare.
     * @return True if equal, False otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShoppingCart sc = (ShoppingCart) o;
        return (this.id == sc.id);
    }
}
