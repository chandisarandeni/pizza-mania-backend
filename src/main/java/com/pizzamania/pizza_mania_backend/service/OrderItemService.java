package com.pizzamania.pizza_mania_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.pizzamania.pizza_mania_backend.dto.OrderItemDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class OrderItemService {

    private static final String COLLECTION_NAME = "OrderItems";

    // ✅ Add new Order Item
    public String addOrderItem(OrderItemDTO orderItem) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();

        // Auto-generate ID
        if (orderItem.getOrderItemId() == null || orderItem.getOrderItemId().isEmpty()) {
            ApiFuture<QuerySnapshot> query = firestore.collection(COLLECTION_NAME)
                    .orderBy("orderItemId", Query.Direction.DESCENDING)
                    .limit(1)
                    .get();

            List<QueryDocumentSnapshot> docs = query.get().getDocuments();
            String newId;

            if (docs.isEmpty()) {
                newId = "ORDI001";
            } else {
                String lastId = docs.get(0).getString("orderItemId");
                int lastNumber = Integer.parseInt(lastId.replace("ORDI", ""));
                newId = String.format("ORDI%03d", lastNumber + 1);
            }
            orderItem.setOrderItemId(newId);
        }

        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME)
                .document(orderItem.getOrderItemId())
                .set(orderItem);

        return "OrderItem saved with ID: " + orderItem.getOrderItemId() +
                " at " + future.get().getUpdateTime();
    }

    // ✅ Get all Order Items
    public List<OrderItemDTO> getAllOrderItems() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<OrderItemDTO> items = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            items.add(doc.toObject(OrderItemDTO.class));
        }
        return items;
    }

    // ✅ Get Order Item by ID
    public OrderItemDTO getOrderItemById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);
        DocumentSnapshot document = docRef.get().get();

        if (document.exists()) {
            return document.toObject(OrderItemDTO.class);
        }
        return null;
    }

    // ✅ Update Order Item
    public String updateOrderItem(OrderItemDTO orderItem) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(orderItem.getOrderItemId());
        ApiFuture<WriteResult> future = docRef.set(orderItem);
        return "OrderItem updated at " + future.get().getUpdateTime();
    }

    // ✅ Delete Order Item
    public String deleteOrderItem(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);
        ApiFuture<WriteResult> future = docRef.delete();
        return "OrderItem deleted at " + future.get().getUpdateTime();
    }

    // ✅ Get Order Items by Order ID
    public List<OrderItemDTO> getOrderItemsByOrderId(String orderId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = db.collection(COLLECTION_NAME)
                .whereEqualTo("orderId", orderId)
                .get();

        List<QueryDocumentSnapshot> docs = query.get().getDocuments();
        List<OrderItemDTO> items = new ArrayList<>();
        for (QueryDocumentSnapshot doc : docs) {
            items.add(doc.toObject(OrderItemDTO.class));
        }
        return items;
    }
}
