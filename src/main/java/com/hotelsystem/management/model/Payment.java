package com.hotelsystem.management.model;

public class Payment {
    private int id;
    private double amount;
    private String payment_method;

    public Payment(int id, double amount, String payment_method) {
        this.id = id;
        this.amount = amount;
        this.payment_method = payment_method;
    }
    public Payment(double amount, String payment_method) {
        this.id = id;
        this.amount = amount;
        this.payment_method = payment_method;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return payment_method;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.payment_method = payment_method;
    }
}
