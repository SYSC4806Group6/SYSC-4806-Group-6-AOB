package org.example.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PurchaseReceipt {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "purchaseReceipt", cascade = CascadeType.ALL)
    private List<PurchaseReceiptItem> items;
    private double totalCost;
    private LocalDateTime orderDateTime;

    private String shippingName;
    private String shippingAddress;
    private String email;

    /**
     * Default constructor
     */
    public PurchaseReceipt() {
        this.items = new ArrayList<PurchaseReceiptItem>();
        this.totalCost = -1;
        this.orderDateTime = LocalDateTime.now();
    }

    /**
     * Initialize a purchasereceipt with a user attached to it
     * @param user User associated with the purchase receipt
     */
    public PurchaseReceipt(User user) {
        this.user = user;
        this.user.addPurchaseReceipt(this);
        this.items = new ArrayList<PurchaseReceiptItem>();
        this.totalCost = -1;
        this.orderDateTime = LocalDateTime.now();
    }

    /**
     * adds a PurchaseReceiptItem to the ShoppingCart
     * @param item The item to be added
     * @return True if added, False otherwise
     */
    public boolean addPurchaseReceiptItem(PurchaseReceiptItem item) {
        item.setPurchaseReceipt(this);
        return this.items.add(item);
    }

    /**
     * removes a PurchaseReceiptItem from the PurchaseReceipt
     * @param item The item to be removed
     * @return True if removed, False otherwise
     */
    public boolean removePurchaseReceiptItem(PurchaseReceiptItem item) {
        item.setPurchaseReceipt(null);
        return this.items.remove(item);
    }

    public void calculateAndSetTotalCost() {
        totalCost = 0.00;
        for (PurchaseReceiptItem item : items) {
            totalCost += item.getPricePerBook() * item.getQuantity();
        }
    }

    /* Getters and Setters */
    public long getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public User getUser() {
        return this.user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public List<PurchaseReceiptItem> getItems() {
        return this.items;
    }
    public void setItems(List<PurchaseReceiptItem> items) {
        this.items = items;
    }
    public double getTotalCost() {
        return this.totalCost;
    }
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    public LocalDateTime getOrderDateTime() {
        return this.orderDateTime;
    }
    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }
    public String getShippingName() {return this.shippingName;}
    public void setShippingName(String shippingName) {this.shippingName = shippingName;}
    public String getShippingAddress() {return this.shippingAddress;}
    public void setShippingAddress(String shippingAddress) {this.shippingAddress = shippingAddress;}
    public String getEmail() {return this.email;}
    public void setEmail(String email) {this.email = email;}

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
        PurchaseReceipt pr = (PurchaseReceipt) o;
        return (this.id == pr.id);
    }
}
