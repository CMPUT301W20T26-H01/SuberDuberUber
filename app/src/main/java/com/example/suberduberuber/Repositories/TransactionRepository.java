package com.example.suberduberuber.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.suberduberuber.Models.Transaction;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class TransactionRepository {
    private static final String TAG = "TRANSACTION_REPOSITORY";

    FirebaseAuth myAuth = FirebaseAuth.getInstance();
    FirebaseFirestore myDb = FirebaseFirestore.getInstance();

    public void saveTransaction(Transaction transaction) {
        myDb.collection("transactions")
                .add(transaction)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    public void processTransaction(String paidUserUID, double newPayingBalance, double newPaidBalance) {
        myDb.collection("users")
                .document(myAuth.getUid())
                .update("balance", newPayingBalance)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
        myDb.collection("users")
                .document(paidUserUID)
                .update("balance", newPaidBalance)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });;
    }
}
