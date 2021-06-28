package org.heartfulness.avtc.controller;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.heartfulness.avtc.model.Agent;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MainController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    SecurityProperties secProps;

    @Autowired
    CookieUtils cookieUtils;
    @Autowired
    AgentService agentService;

    private final AgentRepository agentRepository;

    private final LoggerRepository loggerRepository;

    public MainController(AgentRepository agentRepository, LoggerRepository loggerRepository) {
        this.agentRepository = agentRepository;
        this.loggerRepository = loggerRepository;
    }


    @GetMapping("/")
    public String getlogin(ModelMap modelMap) {
        return "main/mainpage";
    }


    @GetMapping("/main")
    public String getMain(ModelMap modelMap) {
        Agent agent = new Agent();
        modelMap.put("userEntity", agent);
        return "main/phone-num-auth";
    }

    @PostMapping("/main")
    public String postMain(Agent agent) {
        //String phoneNumber = principal.getName();
        Agent agent1 = this.agentService.findBycontactNumber("+919550563765");
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
        cookieUtils.deleteCookie("authenticated");
        return "main/login-redirect";
    }



}
