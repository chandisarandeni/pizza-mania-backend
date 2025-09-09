package com.pizzamania.pizza_mania_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.pizzamania.pizza_mania_backend.entity.Admin;
import com.pizzamania.pizza_mania_backend.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class AdminService {

    private static final String COLLECTION_NAME = "admins";

    // Save an admin
    public Admin saveAdmin(Admin admin) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();

        if (admin.getUsername() == null || admin.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty!");
        }

        // Check if username already exists
        var existingAdmin = firestore.collection(COLLECTION_NAME)
                .document(admin.getUsername())
                .get()
                .get();

        if (existingAdmin.exists()) {
            throw new IllegalArgumentException("Username already exists!");
        }

        // Generate adminId
        if (admin.getAdminId() == null || admin.getAdminId().isEmpty()) {
            var allAdminsQuery = firestore.collection(COLLECTION_NAME)
                    .orderBy("adminId", com.google.cloud.firestore.Query.Direction.DESCENDING)
                    .limit(1)
                    .get();

            var docs = allAdminsQuery.get().getDocuments();
            String newId;

            if (docs.isEmpty()) {
                newId = "ADMIN001";
            } else {
                String lastId = docs.get(0).getString("adminId");
                int lastNumber = (lastId == null || lastId.isEmpty()) ? 0 : Integer.parseInt(lastId.replace("ADMIN", ""));
                newId = String.format("ADMIN%03d", lastNumber + 1);
            }
            admin.setAdminId(newId);
        }

        // Save admin (using adminId as document ID instead of username for better consistency)
        firestore.collection(COLLECTION_NAME)
                .document(admin.getAdminId())
                .set(admin);

        return admin;
    }

    // Get all admins
    public List<Admin> getAllAdmins() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<com.google.cloud.firestore.QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Admin> admins = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            admins.add(document.toObject(Admin.class));
        }

        return admins;
    }

    // Get admin by ID
    public Admin getAdminById(String adminId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        var docRef = db.collection(COLLECTION_NAME).document(adminId);
        var future = docRef.get();
        var document = future.get();

        if (document.exists()) {
            return document.toObject(Admin.class);
        } else {
            return null; // No admin found with this ID
        }
    }


    // Update admin
    public Admin updateAdmin(String adminId, Admin updatedAdmin) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        var docRef = db.collection(COLLECTION_NAME).document(adminId);
        var future = docRef.get();
        var document = future.get();

        if (!document.exists()) {
            throw new IllegalArgumentException("Admin with ID " + adminId + " not found!");
        }

        // Ensure the adminId in DB remains consistent
        updatedAdmin.setAdminId(adminId);

        // Update document
        docRef.set(updatedAdmin);

        return updatedAdmin;
    }


    // Delete admin by ID
    public String deleteAdmin(String adminId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();

        var docRef = db.collection(COLLECTION_NAME).document(adminId);
        var future = docRef.get();
        var document = future.get();

        if (!document.exists()) {
            throw new IllegalArgumentException("Admin with ID " + adminId + " not found!");
        }

        docRef.delete();
        return "Admin with ID " + adminId + " deleted successfully!";
    }

}
