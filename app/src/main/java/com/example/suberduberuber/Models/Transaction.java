package com.example.suberduberuber.Models;

import com.google.firebase.firestore.DocumentId;

import java.util.Date;

public class Transaction {
    @DocumentId
    private String transactionID; // Stored as a string as @DocumentID (Firestore) requires String type
    private Rider userPaying;
    private Driver userPaid;
    private double paymentAmount;
    private Date time;

    /**
     * Transaction object constructor
     * @param userPaying
     *      Rider that is paying amount
     * @param userPaid
     *      Driver that is getting paid amount
     * @param paymentAmount
     *      Amount being transferred
     */
    public Transaction(Rider userPaying, Driver userPaid, double paymentAmount, Date time) {
        this.userPaying = userPaying;
        this.userPaid = userPaid;
        this.paymentAmount = paymentAmount;
        this.time = time;
    }

    /**
     * Empty public constructor needed by Cloud Firestore for serializability
    */
    public Transaction() {}

    public String getTransactionID() {
        return transactionID;
    }

    public Rider getUserPaying() {
        return userPaying;
    }

    public void setUserPaying(Rider userPaying) {
        this.userPaying = userPaying;
    }

    public Driver getUserPaid() {
        return userPaid;
    }

    public void setUserPaid(Driver userPaid) {
        this.userPaid = userPaid;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
