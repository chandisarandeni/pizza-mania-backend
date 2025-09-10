package com.pizzamania.pizza_mania_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.pizzamania.pizza_mania_backend.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final String COLLECTION_NAME = "products";

    // ✅ Add new product with auto-generated ID
    public String addProduct(Product product) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();

        // Get all products to find the latest ID
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        // Find the max numeric ID
        int maxId = 0;
        for (QueryDocumentSnapshot doc : documents) {
            String id = doc.getId(); // Example: PRDCT001
            try {
                int num = Integer.parseInt(id.replace("PRDCT", ""));
                if (num > maxId) {
                    maxId = num;
                }
            } catch (NumberFormatException ignored) {
            }
        }

        // Generate new ID
        int newId = maxId + 1;
        String productId = String.format("PRDCT%03d", newId);
        product.setProductId(productId);

        // Save product
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(productId);
        ApiFuture<WriteResult> writeResult = docRef.set(product);

        return "Product added successfully with ID " + productId +
                " at " + writeResult.get().getUpdateTime();
    }


    // ✅ Get product by ID
    public Product getProductById(String productId) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(productId);
        DocumentSnapshot document = docRef.get().get();

        if (document.exists()) {
            return document.toObject(Product.class);
        } else {
            return null;
        }
    }

    // ✅ Get all products
    public List<Product> getAllProducts() throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        return documents.stream()
                .map(doc -> doc.toObject(Product.class))
                .collect(Collectors.toList());
    }

    // ✅ Update product
    public String updateProduct(Product product) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();

        if (product.getProductId() == null || product.getProductId().isEmpty()) {
            return "Error: Product ID cannot be null or empty!";
        }

        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(product.getProductId());
        DocumentSnapshot document = docRef.get().get();

        if (!document.exists()) {
            return "Error: Product with ID " + product.getProductId() + " does not exist!";
        }

        ApiFuture<WriteResult> future = docRef.set(product);
        return "Product updated successfully at " + future.get().getUpdateTime();
    }

    // ✅ Delete product
    public String deleteProduct(String productId) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(productId);
        DocumentSnapshot document = docRef.get().get();

        if (!document.exists()) {
            return "Error: Product with ID " + productId + " does not exist!";
        }

        ApiFuture<WriteResult> future = docRef.delete();
        return "Product deleted successfully at " + future.get().getUpdateTime();
    }
}
