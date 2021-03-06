package org.heartfulness.avtc.controller;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import net.minidev.json.JSONObject;
import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.enums.LogEvent;
import org.heartfulness.avtc.model.Logger;
import org.heartfulness.avtc.repository.AgentRepository;
import org.heartfulness.avtc.repository.LoggerRepository;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.heartfulness.avtc.security.auth.models.Credentials;
import org.heartfulness.avtc.security.auth.models.SecurityProperties;
import org.heartfulness.avtc.security.auth.models.User;
import org.heartfulness.avtc.security.utils.CookieUtils;
import org.heartfulness.avtc.service.AgentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MainController {

    //TODO: make proper config tables.

    private final SecurityService securityService;
    private final SecurityProperties secProps;
    private final CookieUtils cookieUtils;
    private final AgentService agentService;
    private final LoggerRepository loggerRepository;

    public MainController(SecurityService securityService, SecurityProperties secProps, CookieUtils cookieUtils, AgentService agentService, AgentRepository agentRepository, LoggerRepository loggerRepository) {
        this.securityService = securityService;
        this.secProps = secProps;
        this.cookieUtils = cookieUtils;
        this.agentService = agentService;
        this.loggerRepository = loggerRepository;
    }

   /* @GetMapping("/")
    public String getlogin(ModelMap modelMap) {
        return "main/mainpage";
    }*/


    @GetMapping("/main")
    public String getMain(ModelMap modelMap) {
        if (this.securityService.getUser() != null) {
            return "redirect:/success";
        }
        Agent agent = new Agent();
        modelMap.put("userEntity", agent);
        return "main/phone-num-auth";
    }

    @PostMapping("/main")
    public String postMain(Agent agent) {
        //String phoneNumber = principal.getName();
        Agent agent1 = this.agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        if (agent1 == null) {
            return "main/error";
        }
       if (agent1.validate()) {
            //agent is validated. navigate to success page
            return "redirect:/success";
        }
        //return redirected url for editing details
        return "redirect:/addDetails";
    }

    @GetMapping("/private/sessionLogout")
    public String deleteSessionCookie(@AuthenticationPrincipal User user) {
        if (securityService.getCredentials().getType() == Credentials.CredentialType.SESSION
                && secProps.getFirebaseProps().isEnableLogoutEverywhere()) {
            try {
                FirebaseAuth.getInstance().revokeRefreshTokens(securityService.getUser().getUid());
            } catch (FirebaseAuthException e) {
                e.printStackTrace();
            }
        }
        //cookieUtils.deleteSecureCookie("session");
        Logger logger = new Logger();
        logger.setAgent(this.agentService.findBycontactNumber(user.getPhoneNumber()));
        logger.setLogEvent(LogEvent.MANUAL_LOGOUT);
        this.loggerRepository.save(logger);
        cookieUtils.deleteCookie("session");
        cookieUtils.deleteCookie("JSESSIONID");
        cookieUtils.deleteCookie("authenticated");
        return "main/login-redirect";
    }

    @PostMapping("/check")
    @ResponseBody
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public ResponseEntity<?> createSessionCookie(@RequestBody String contactNumber) {
        List<JSONObject> entities = new ArrayList<JSONObject>();
        JSONObject entity = new JSONObject();
        Agent agent = this.agentService.findBycontactNumber(contactNumber);
        if (agent == null) {
            entity.put("phoneNumber", "failure");
        } else {
            if (agent.getCertified()) {
                entity.put("phoneNumber", "success");
            } else {
                entity.put("phoneNumber", "failure");
            }
        }
        entities.add(entity);
        return new ResponseEntity<Object>(entities, HttpStatus.OK);
    }

    @GetMapping("/dashboard")
    public String getDashboard(ModelMap modelMap) {
        Map<String, Integer> map = new TreeMap<>();
        map.put("Missed calls", 5);
        map.put("Accepted calls", 4);
        modelMap.put("chartData", map);
        return "main/dashboard";
    }


    @GetMapping("/unauthorized")
    public String unauthUser(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "You are not authorized to view this page!");
        return "redirect:/success";
    }



}
