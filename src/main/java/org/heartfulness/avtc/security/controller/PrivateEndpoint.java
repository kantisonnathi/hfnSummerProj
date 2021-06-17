package org.heartfulness.avtc.security.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.SessionCookieOptions;
import org.heartfulness.avtc.security.auth.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
    @Produces(MediaType.TEXT_PLAIN)
    public ResponseEntity<?> createSessionCookie(@RequestBody String idToken, HttpServletResponse response) {
        long expiresIn = TimeUnit.DAYS.toMillis(5);
        SessionCookieOptions options = SessionCookieOptions.builder()
                .setExpiresIn(expiresIn)
                .build();
        try {
            String sessionCookie = FirebaseAuth.getInstance().createSessionCookie(idToken, options);
            Cookie cookie = new Cookie("session", sessionCookie);
            cookie.setSecure(false);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            return new ResponseEntity<>("logged in", HttpStatus.OK);
        } catch (FirebaseAuthException e) {
            return new ResponseEntity<>("failed to create session cookie", HttpStatus.UNAUTHORIZED);
            /*return Response.status(Response.Status.UNAUTHORIZED).entity("Failed to create a session cookie")
                    .build();*/
        } 
    }

    @GetMapping("/sessionLogout")
    public ResponseEntity<?> deleteSessionCookie(HttpServletResponse response) {
        String cookieContent = "";
        Cookie cookie = new Cookie("session", cookieContent);
        cookie.setSecure(false);
        cookie.setPath("/");
        response.addCookie(cookie);
        return new ResponseEntity<>("logged out", HttpStatus.OK);
    }
}