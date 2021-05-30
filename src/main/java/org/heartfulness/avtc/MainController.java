package org.heartfulness.avtc;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class MainController {

    @GetMapping("/main")
    public String getMain(ModelMap modelMap) {
        User user = new User();
        modelMap.put("userEntity", user);
        return "main/phone-num-auth";
    }

    @GetMapping("/test")
    public String test() {
        return "main/test";
    }

    @GetMapping(path="/check/{contactNumber}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> sayHello(@PathVariable("contactNumber")String contactNumber) {
        //Get data from service layer into entityList.

        List<JSONObject> entities = new ArrayList<JSONObject>();
        JSONObject entity = new JSONObject();
        if (contactNumber.equals("+917338897712")) {
            entity.put("phoneNumber", "success");
        } else {
            entity.put("phoneNumber", "failure");
        }
        entities.add(entity);
        return new ResponseEntity<Object>(entities, HttpStatus.OK);
    }




}
