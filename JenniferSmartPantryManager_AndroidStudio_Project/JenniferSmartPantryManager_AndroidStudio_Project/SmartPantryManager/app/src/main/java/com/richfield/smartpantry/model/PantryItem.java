package com.richfield.smartpantry.model;

/**
 * Plain data model for a single ingredient the user has at home.
 * Mirrors one document in the Firestore {@code pantry_items} collection.
 */
public class PantryItem {

    private String id;
    private String name;
    private double quantity;
    private String unit;
    /** Expiry date stored as an ISO string (yyyy-MM-dd), or null if not set. */
    private String expiryDate;

    public PantryItem() {
    }

    public PantryItem(String id, String name, double quantity, String unit, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    public PantryItem(long id, String name, double quantity, String unit, String expiryDate) {
        this(String.valueOf(id), name, quantity, unit, expiryDate);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
