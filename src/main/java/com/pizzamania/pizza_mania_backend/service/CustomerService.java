package com.pizzamania.pizza_mania_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.pizzamania.pizza_mania_backend.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class CustomerService {

    private static final String COLLECTION_NAME = "customers";

    // Save a customer
    public String saveCustomer(Customer customer) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();

        // Check if phone number already exists
        ApiFuture<QuerySnapshot> query = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("phone", customer.getPhone())
                .get();

        List<QueryDocumentSnapshot> existingCustomers = query.get().getDocuments();
        if (!existingCustomers.isEmpty()) {
            System.out.println("Phone number already exists: " + customer.getPhone());
            return "Error: Phone number already exists!";
        }

        // Generate customerId in format CSTM001, CSTM002, etc.
        if (customer.getCustomerId() == null || customer.getCustomerId().isEmpty()) {
            ApiFuture<QuerySnapshot> allCustomersQuery = firestore.collection(COLLECTION_NAME)
                    .orderBy("customerId", Query.Direction.DESCENDING) // get the last inserted
                    .limit(1)
                    .get();

            List<QueryDocumentSnapshot> docs = allCustomersQuery.get().getDocuments();
            String newId;

            if (docs.isEmpty()) {
                newId = "CSTM001";
            } else {
                String lastId = docs.get(0).getString("customerId");
                if (lastId == null || lastId.isEmpty()) {
                    newId = "CSTM001";
                } else {
                    int lastNumber = Integer.parseInt(lastId.replace("CSTM", ""));
                    int nextNumber = lastNumber + 1;
                    newId = String.format("CSTM%03d", nextNumber); // always 3 digits
                }
            }
            customer.setCustomerId(newId);
        }

        // ✅ Generate random 6-digit OTP
        int otp = 100000 + new Random().nextInt(900000);
        customer.setOtpCode(otp);

        // ✅ Set OTP generated time
        customer.setOtpGeneratedTime(new Date());

        // Save the customer document with newId as the document ID
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME)
                .document(customer.getCustomerId())
                .set(customer);

        System.out.println("Customer saved with ID: " + customer.getCustomerId()
                + ", OTP: " + otp
                + " at " + future.get().getUpdateTime());

        return "Customer saved successfully with ID: " + customer.getCustomerId();
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
