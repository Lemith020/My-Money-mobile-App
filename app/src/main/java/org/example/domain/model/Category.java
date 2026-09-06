package org.example.domain.model;

public class Category {
    private long id;
    private String name;
    private String icon;
    private String color;
    private TransactionType type;

    public Category(long id, String name, String icon, String color, TransactionType type) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.color = color;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getColor() {
        return color;
    }

    public TransactionType getType() {
        return type;
    }
}
