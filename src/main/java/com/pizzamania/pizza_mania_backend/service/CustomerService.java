package com.pizzamania.pizza_mania_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.pizzamania.pizza_mania_backend.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CustomerService {

    private static final String COLLECTION_NAME = "customers";

    // Save a customer
    public String saveCustomer(Customer customer) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(); // auto-generated ID
        customer.setCustomerId(docRef.getId());
        ApiFuture<WriteResult> future = docRef.set(customer);
        future.get(); // wait for completion
        return docRef.getId();
    }

    // Get all customers
    public List<Customer> getAllCustomers() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Customer> customers = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            Customer customer = doc.toObject(Customer.class);
            customers.add(customer);
        }
        return customers;
    }

    // Get a customer by ID
    public Customer getCustomerById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            return document.toObject(Customer.class);
        }
        return null;
    }

    // Update a customer
    public void updateCustomer(Customer customer) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(customer.getCustomerId());
        ApiFuture<WriteResult> future = docRef.set(customer);
        future.get();
    }

    // Delete a customer
    public void deleteCustomer(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);
        ApiFuture<WriteResult> future = docRef.delete();
        future.get();
    }
}
