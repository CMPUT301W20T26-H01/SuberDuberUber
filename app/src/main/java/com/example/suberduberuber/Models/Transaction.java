package com.example.suberduberuber.Models;

/*
Copyright [2020] [SuberDuberUber]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

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

    /**
     * Returns a transaction ID number
     * @return
     *      Transaction ID number
     */
    public String getTransactionID() {
        return transactionID;
    }

    /**
     * Returns the rider user that is paying
     * @return
     *      Rider that is paying
     */
    public Rider getUserPaying() {
        return userPaying;
    }

    /**
     * Sets the rider that is paying
     * @param userPaying
     *      New rider that is paying
     */
    public void setUserPaying(Rider userPaying) {
        this.userPaying = userPaying;
    }

    /**
     * Returns the driver user that is getting paid
     * @return
     *      Driver that is getting paid
     */
    public Driver getUserPaid() {
        return userPaid;
    }

    /**
     * Sets the driver that is getting paid
     * @param userPaid
     *      New driver that is getting paid
     */
    public void setUserPaid(Driver userPaid) {
        this.userPaid = userPaid;
    }

    /**
     * Returns the amount being transferred
     * @return
     *      Amount getting transferred
     */
    public double getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * Sets the amount being transferred
     * @param paymentAmount
     *      New amount being transferred
     */
    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    /**
     * Gets the time of the transaction
     * @return
     *      Time of the transaction
     */
    public Date getTime() {
        return time;
    }

    /**
     * Sets time of the transaction
     * @param time
     *      New time of the transaction
     */
    public void setTime(Date time) {
        this.time = time;
    }
}
