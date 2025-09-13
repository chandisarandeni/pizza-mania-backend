package com.pizzamania.pizza_mania_backend.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.pizzamania.pizza_mania_backend.entity.Feedback;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FeedbackService {

    private static final String COLLECTION_NAME = "feedbacks";

    // ✅ Add new feedback
    public String addFeedback(Feedback feedback) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreClient.getFirestore();

        // Auto-generate ID
        if (feedback.getFeedbackId() == null || feedback.getFeedbackId().isEmpty()) {
            ApiFuture<QuerySnapshot> query = firestore.collection(COLLECTION_NAME)
                    .orderBy("feedbackId", Query.Direction.DESCENDING)
                    .limit(1)
                    .get();

            List<QueryDocumentSnapshot> docs = query.get().getDocuments();
            String newId;

            if (docs.isEmpty()) {
                newId = "FB001";
            } else {
                String lastId = docs.get(0).getString("feedbackId");
                int lastNumber = Integer.parseInt(lastId.replace("FB", ""));
                newId = String.format("FB%03d", lastNumber + 1);
            }
            feedback.setFeedbackId(newId);
        }

        // Save feedback
        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME)
                .document(feedback.getFeedbackId())
                .set(feedback);

        return "Feedback saved with ID: " + feedback.getFeedbackId() +
                " at " + future.get().getUpdateTime();
    }

    // ✅ Get all feedbacks
    public List<Feedback> getAllFeedbacks() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Feedback> feedbacks = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            feedbacks.add(doc.toObject(Feedback.class));
        }
        return feedbacks;
    }

    // ✅ Get feedback by ID
    public Feedback getFeedbackById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);
        DocumentSnapshot document = docRef.get().get();

        if (document.exists()) {
            return document.toObject(Feedback.class);
        }
        return null;
    }

    // ✅ Update feedback
    public String updateFeedback(Feedback feedback) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(feedback.getFeedbackId());
        ApiFuture<WriteResult> future = docRef.set(feedback);
        return "Feedback updated at " + future.get().getUpdateTime();
    }

    // ✅ Delete feedback
    public String deleteFeedback(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);
        ApiFuture<WriteResult> future = docRef.delete();
        return "Feedback deleted at " + future.get().getUpdateTime();
    }

    // ✅ Get feedbacks by customerId
    public List<Feedback> getFeedbacksByCustomerId(String customerId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = db.collection(COLLECTION_NAME)
                .whereEqualTo("customerId", customerId)
                .get();

        List<QueryDocumentSnapshot> docs = query.get().getDocuments();
        List<Feedback> feedbacks = new ArrayList<>();
        for (QueryDocumentSnapshot doc : docs) {
            feedbacks.add(doc.toObject(Feedback.class));
        }
        return feedbacks;
    }

    // ✅ Get feedbacks by orderId
    public List<Feedback> getFeedbacksByOrderId(String orderId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> query = db.collection(COLLECTION_NAME)
                .whereEqualTo("orderId", orderId)
                .get();

        List<QueryDocumentSnapshot> docs = query.get().getDocuments();
        List<Feedback> feedbacks = new ArrayList<>();
        for (QueryDocumentSnapshot doc : docs) {
            feedbacks.add(doc.toObject(Feedback.class));
        }
        return feedbacks;
    }
}
