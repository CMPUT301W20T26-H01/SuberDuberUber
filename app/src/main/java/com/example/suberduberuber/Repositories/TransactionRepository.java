package com.example.suberduberuber.Repositories;

import com.example.suberduberuber.Models.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class TransactionRepository {
    FirebaseAuth myAuth = FirebaseAuth.getInstance();
    FirebaseFirestore myDb = FirebaseFirestore.getInstance();

    public void saveTransaction(Transaction transaction) {
        myDb.collection("transactions")
                .add(transaction);
    }

    public void processTransaction(String paidUserUID, double newPayingBalance, double newPaidBalance) {
        myDb.collection("users")
                .document(myAuth.getUid())
                .update("balance", newPayingBalance);
        myDb.collection("users")
                .document(paidUserUID)
                .update("balance", newPaidBalance);

    }
}
