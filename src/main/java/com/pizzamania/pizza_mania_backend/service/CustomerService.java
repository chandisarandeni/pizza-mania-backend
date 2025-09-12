package com.pizzamania.pizza_mania_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.pizzamania.pizza_mania_backend.entity.Customer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class CustomerService {

    private static final String COLLECTION_NAME = "customers";

    // -------------------- Basic CRUD --------------------

    // Save a customer
    public String saveCustomer(Customer customer) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();

        // ✅ Check if email already exists
        ApiFuture<QuerySnapshot> emailQuery = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("email", customer.getEmail())
                .get();
        List<QueryDocumentSnapshot> existingByEmail = emailQuery.get().getDocuments();
        if (!existingByEmail.isEmpty()) {
            System.out.println("Email already exists: " + customer.getEmail());
            return "Error: Email already exists!";
        }

        // ✅ Generate customerId in format CSTM001, CSTM002, etc.
        if (customer.getCustomerId() == null || customer.getCustomerId().isEmpty()) {
            ApiFuture<QuerySnapshot> allCustomersQuery = firestore.collection(COLLECTION_NAME)
                    .orderBy("customerId", Query.Direction.DESCENDING)
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
                    newId = String.format("CSTM%03d", lastNumber + 1); // always 3 digits
                }
            }
            customer.setCustomerId(newId);
        }

        // ✅ Generate random 4-digit OTP
        int otp = 1000 + new Random().nextInt(9000); // 1000-9999
        customer.setOtpCode(otp);

        // ✅ Set OTP generated time
        customer.setOtpGeneratedTime(new Date());

        // ✅ Save the customer document with newId as the document ID
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

    // -------------------- Advanced features --------------------

    // Search customer by email address
    public List<Customer> searchCustomersByEmail(String email) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = db.collection(COLLECTION_NAME)
                .whereEqualTo("email", email)
                .get();

        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        List<Customer> customers = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            Customer customer = doc.toObject(Customer.class);
            customers.add(customer);
        }
        return customers;
    }

    // Send OTP to customer email
    public String sendOtp(String email) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        // 1️⃣ Search the user by email
        ApiFuture<QuerySnapshot> query = db.collection(COLLECTION_NAME)
                .whereEqualTo("email", email)
                .get();
        List<QueryDocumentSnapshot> docs = query.get().getDocuments();

        if (docs.isEmpty()) {
            return "Error: Customer with email " + email + " not found!";
        }

        // Get the first customer (assuming email is unique)
        DocumentReference customerDocRef = docs.get(0).getReference();
        Customer customer = docs.get(0).toObject(Customer.class);

        // 2️⃣ Generate new 4-digit OTP
        int newOtp = 1000 + new Random().nextInt(9000);
        customer.setOtpCode(newOtp);
        customer.setOtpGeneratedTime(new Date());

        // 3️⃣ Update customer in Firestore
        customerDocRef.set(customer).get();

        // 4️⃣ Send request to notification service
        RestTemplate restTemplate = new RestTemplate();
        String notificationUrl = "http://localhost:8080/api/v1/notifications/send-otp";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("otp", newOtp);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForObject(notificationUrl, entity, String.class);
            System.out.println("OTP sent to " + email + ": " + newOtp);
        } catch (Exception e) {
            e.printStackTrace();
            return "Customer OTP updated but failed to send notification!";
        }

        return "OTP generated and sent successfully to " + email;
    }

    public String verifyOtp(String email, int otp) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        // Search customer by email
        ApiFuture<QuerySnapshot> query = db.collection(COLLECTION_NAME)
                .whereEqualTo("email", email)
                .get();

        List<QueryDocumentSnapshot> docs = query.get().getDocuments();
        if (docs.isEmpty()) {
            return "Error: Customer not found";
        }

        Customer customer = docs.get(0).toObject(Customer.class);
        if (customer.getOtpCode() != otp) {
            return "Error: Invalid OTP";
        }

        // Optional: check OTP expiry (5 minutes)
        long now = System.currentTimeMillis();
        long otpTime = customer.getOtpGeneratedTime().getTime();
        if (now - otpTime > 5 * 60 * 1000) {
            return "Error: OTP expired";
        }

        return "success"; // OTP verified
    }

}
