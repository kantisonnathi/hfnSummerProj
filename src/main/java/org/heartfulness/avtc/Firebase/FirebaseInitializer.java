package org.heartfulness.avtc.Firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FirebaseInitializer {
    @PostConstruct
    private void initDB() throws IOException {
        InputStream serviceAccount = (InputStream) this.getClass().getClassLoader().getResourceAsStream("./avtc-hfn-firebase-adminsdk-1ufj3-64b76ac869.json");


        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
      if(FirebaseApp.getApps().isEmpty()) {
          FirebaseApp.initializeApp(options);
      }
    }
 public Firestore getFirebase(){
        return FirestoreClient.getFirestore();
 }
 public FirebaseAuth getClient(){
        return FirebaseAuth.getInstance();
 }

}
