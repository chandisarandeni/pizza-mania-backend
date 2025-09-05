package com.pizzamania.pizza_mania_backend;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.api.core.ApiFuture;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@SpringBootApplication
public class PizzaManiaBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PizzaManiaBackendApplication.class, args);
    }
}

@Service
class FirebaseInitialization {

    @PostConstruct
    public void initialization() {
        try {
            // Load from resources folder
            InputStream serviceAccount = new ClassPathResource("serviceAccountKey.json").getInputStream();

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);

            // 🔹 Test Firestore connection
            Firestore db = FirestoreOptions.getDefaultInstance().getService();
            DocumentReference docRef = db.collection("connectionTest").document("status");

            ApiFuture<WriteResult> result = docRef.set(new Status("OK"));
            System.out.println("✅ Firestore connected successfully at: " + result.get().getUpdateTime());

        } catch (Exception e) {
            System.err.println("❌ Firebase initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper record to write simple JSON { "status": "OK" }
    private record Status(String status) {
    }
}
