package org.heartfulness.avtc.security.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.SessionCookieOptions;
import org.heartfulness.avtc.security.auth.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("private")
public class PrivateEndpoint {

    @GetMapping("user-details")
    public ResponseEntity<User> getUserInfo(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }


    @PostMapping("/sessionLogin")
    @ResponseBody
    public Response createSessionCookie(@RequestBody String idToken) {
        long expiresIn = TimeUnit.DAYS.toMillis(5);
        SessionCookieOptions options = SessionCookieOptions.builder()
                .setExpiresIn(expiresIn)
                .build();
        try {
            String sessionCookie = FirebaseAuth.getInstance().createSessionCookie(idToken, options);
            NewCookie cookie = new NewCookie("session", sessionCookie);
            return Response.ok().cookie(cookie).build();
        } catch (FirebaseAuthException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Failed to create a session cookie")
                    .build();
        } 
    }
}