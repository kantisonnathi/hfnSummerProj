package org.heartfulness.avtc.security.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.SessionCookieOptions;
import org.heartfulness.avtc.model.LogEvent;
import org.heartfulness.avtc.model.Logger;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.LoggerRepository;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.heartfulness.avtc.security.auth.models.Credentials;
import org.heartfulness.avtc.security.auth.models.SecurityProperties;
import org.heartfulness.avtc.security.auth.models.User;
import org.heartfulness.avtc.security.utils.CookieUtils;
import org.heartfulness.avtc.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    SecurityService securityService;

    @Autowired
    SecurityProperties secProps;

    @Autowired
    CookieUtils cookieUtils;
    @Autowired
    AgentService agentService;

    private final LoggerRepository loggerRepository;

    private final AgentRepository agentRepository;

    public PrivateEndpoint(LoggerRepository loggerRepository, AgentRepository agentRepository) {
        this.loggerRepository = loggerRepository;
        this.agentRepository = agentRepository;
    }

    @PostMapping("/sessionLogin")
    @ResponseBody
    @Produces(MediaType.TEXT_PLAIN)
    public ResponseEntity<?> createSessionCookie(@RequestBody String idToken, @AuthenticationPrincipal User user,HttpServletResponse response) {
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
            Logger log = new Logger();
            log.setLogEvent(LogEvent.LOGIN);
            log.setAgent(this.agentService.findBycontactNumber(user.getPhoneNumber()));
            this.loggerRepository.save(log);
            return new ResponseEntity<>("logged in", HttpStatus.OK);
        } catch (FirebaseAuthException e) {
            return new ResponseEntity<>("failed to create session cookie", HttpStatus.UNAUTHORIZED);
            /*return Response.status(Response.Status.UNAUTHORIZED).entity("Failed to create a session cookie")
                    .build();*/
        }
    }

}
