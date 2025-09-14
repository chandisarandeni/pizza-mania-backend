// File: src/main/java/com/pizzamania/pizza_mania_backend/service/OrderService.java
package com.pizzamania.pizza_mania_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.pizzamania.pizza_mania_backend.entity.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class OrderService {

    private static final String COLLECTION_NAME = "Orders";

    // Add new order
    public String addOrder(Order order) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();

        // Auto-generate orderId if missing
        if (order.getOrderId() == null || order.getOrderId().isEmpty()) {
            ApiFuture<QuerySnapshot> query = firestore.collection(COLLECTION_NAME)
                    .orderBy("orderId", Query.Direction.DESCENDING)
                    .limit(1)
                    .get();

            List<QueryDocumentSnapshot> docs = query.get().getDocuments();
            String newId;

            if (docs.isEmpty()) {
                newId = "ORD001";
            } else {
                String lastId = docs.get(0).getString("orderId");
                int lastNumber = Integer.parseInt(lastId.replace("ORD", ""));
                newId = String.format("ORD%03d", lastNumber + 1);
            }
            order.setOrderId(newId);
        }

        // Save order
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME)
                .document(order.getOrderId())
                .set(order);

        return "Order saved with ID: " + order.getOrderId() +
                " at " + future.get().getUpdateTime();
    }

    // Get all orders
    public List<Order> getAllOrders() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Order> orders = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            orders.add(doc.toObject(Order.class));
        }
        return orders;
    }

    // Get order by ID
    public Order getOrderById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            return document.toObject(Order.class);
        }
        return null;
    }

    // Update order
    public String updateOrder(Order order) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(order.getOrderId());
        ApiFuture<WriteResult> future = docRef.set(order);
        return "Order updated at " + future.get().getUpdateTime();
    }

    // Delete order
    public String deleteOrder(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);
        ApiFuture<WriteResult> future = docRef.delete();
        return "Order deleted at " + future.get().getUpdateTime();
    }

    // Search by customerId
    public List<Order> getOrdersByCustomerId(String customerId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = db.collection(COLLECTION_NAME)
                .whereEqualTo("customerId", customerId)
                .get();
        List<Order> orders = new ArrayList<>();
        for (QueryDocumentSnapshot doc : query.get().getDocuments()) {
            orders.add(doc.toObject(Order.class));
        }
        return orders;
    }

    // Search by branchId
    public List<Order> getOrdersByBranchId(String branchId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = db.collection(COLLECTION_NAME)
                .whereEqualTo("branchId", branchId)
                .get();
        List<Order> orders = new ArrayList<>();
        for (QueryDocumentSnapshot doc : query.get().getDocuments()) {
            orders.add(doc.toObject(Order.class));
        }
        return orders;
    }
}
