package org.example.domain.model;

public class Transaction {
    private long id;
    private TransactionType type;
    private double amount;
    private String category;
    private String note;
    private long date; // Changed from String to long

    public Transaction() {}

    public Transaction(long id, TransactionType type, double amount, String category, String note, long date) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.note = note;
        this.date = date;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
}