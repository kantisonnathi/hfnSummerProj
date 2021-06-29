package org.heartfulness.avtc.controller;

import net.minidev.json.JSONObject;
import org.heartfulness.avtc.config.NodeConfiguration;
import org.heartfulness.avtc.model.*;
import org.heartfulness.avtc.model.AfterCallClasses.CategoryCreationDTO;
import org.heartfulness.avtc.repository.*;
import org.heartfulness.avtc.security.auth.SecurityService;
import org.heartfulness.avtc.service.AgentService;
import org.heartfulness.avtc.service.AgentServiceImpl;
import org.heartfulness.avtc.service.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AgentController {

    @Autowired
    SecurityService securityService;

    @Autowired
    NodeConfiguration nodeConfiguration;

    @Autowired
    private CallService callService;
   @Autowired
   private AgentService agentService;

    private final AgentRepository agentRepository;
    private final CallRepository callRepository;
    private final TeamRepository teamRepository;
    private final LoggerRepository loggerRepository;
    private final ScheduleRepository scheduleRepository;

    public AgentController(AgentRepository agentRepository, CallRepository callRepository, TeamRepository teamRepository, LoggerRepository loggerRepository, ScheduleRepository scheduleRepository) {
        this.agentRepository = agentRepository;
        this.callRepository = callRepository;
        this.teamRepository = teamRepository;
        this.loggerRepository = loggerRepository;
        this.scheduleRepository = scheduleRepository;
    }


    @GetMapping("/mark/{status}")
    public String markStatus(@PathVariable("status") String status) {
        Agent agent = agentService.findBycontactNumber(this.securityService.getUser().getPhoneNumber());
        LocalDateTime localDateTime = LocalDateTime.now();
        Logger logger=new Logger();
        HashMap<Integer, Character> m = new HashMap<>();
        logger.setAgent(agent);
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        logger.setTimestamp(timestamp);
        if (status.equals("online")) {
            logger.setLogEvent(LogEvent.TURNED_ONLINE);
            agent.setStatus(AgentStatus.ONLINE);
        } else if (status.equals("offline")) {
            logger.setLogEvent(LogEvent.MANUAL_OFFLINE);
            agent.setStatus(AgentStatus.OFFLINE);
        }
        agent.setTimestamp(timestamp);
        this.loggerRepository.save(logger);
        this.agentRepository.save(agent);
        return "main/error";
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

    @GetMapping("/success")
    public String getMainPage(ModelMap modelMap) {
        Agent agent = agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());
        modelMap.put("agent", agent);
        CategoryCreationDTO categoryCreationDTO=new CategoryCreationDTO();
        List<Call> calls = this.callService.getAllCalls();
        // parse through calls and keep unsaved ones at the top
        List<Call> unsavedCalls = new ArrayList<>();
        List<Call> saved = new ArrayList<>();
        for (Call call : calls) {
            if (call.isSaved()) {
                saved.add(call);
            } else {
                unsavedCalls.add(call);
            }
            call.setCategory(CallCategory.ADJUSTMENT_DISORDERS);
        }
        Collections.reverse(unsavedCalls);
        Collections.reverse(saved);
        calls.clear();
        calls.addAll(unsavedCalls);
        calls.addAll(saved);
        categoryCreationDTO.setCallList(calls);

        Other other = new Other();
        /*for(int i = 0; i < calls.size(); i++) {
            calls.get(i).setCategory(CallCategory.ADJUSTMENT_DISORDERS);
            categoryCreationDTO.addCall(calls.get(i));
        }*/
        modelMap.put("role", agent.getRole().toString());
        modelMap.put("other",other);
       // schedule.get(0).setId(1L);
        modelMap.put("calls",categoryCreationDTO);
        //agent is validated
        //    List<Call> calls = this.callRepository.findAllByAgent(agent);
        //  modelMap.put("calls",calls);
        return "main/success";

        // return "main/success";
    }

    @PostMapping("/schedule")
    public String getTime(@ModelAttribute("other") Other other) throws ParseException {
        LocalTime t= LocalTime.now();
       // Time time=Time.valueOf(t);
        String x=other.getEndtime();
        Agent agent=agentService.findBycontactNumber(securityService.getUser().getPhoneNumber());

        System.out.println(x);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime endtime = LocalTime.parse(x, formatter);
        Time end=Time.valueOf(endtime);
        agent.setTime(end);
        agentRepository.save(agent);
        return "redirect:/success";
    }

    @GetMapping("/team/view")
    public String viewTeam(ModelMap modelMap) {
        Agent loggedInAgent =agentService.findBycontactNumber(securityService.getUser().getPhoneNumber()) ;
        List<Agent> agents = new ArrayList<>();
        agents.add(loggedInAgent);
        Team team = this.teamRepository.findTeamByAgentsIn(agents);
        if (team == null) {
            return "redirect:/success";
        }
        modelMap.put("team", team);
        return "main/viewTeam";
    }

    @PostMapping("/editCall")
    public String addDescription(@ModelAttribute("calls") CategoryCreationDTO categoryCreationDTO) {

        List<Call> calls = categoryCreationDTO.getCallList();
        for (Call call : calls) {
            Call add = callRepository.findById(call.getId()).get();
            add.setDescription(call.getDescription());
            add.setCategory(call.getCategory());
            callRepository.save(add);
        }
        return "redirect:/success";
    }

    @GetMapping("/agent/{agentid}/viewAllCalls")
    public String viewAllAgentCalls(@PathVariable("agentid") Long agentId, ModelMap modelMap) {
        Agent agent = this.agentService.findById(agentId);
        return findPaginatedByAgent(1, "id", "asc", agent, modelMap);
    }
    @GetMapping("/Agentpage/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,@RequestParam("sortField") String sortField,@RequestParam("sortDir") String sortDir ,Model model){
        int pageSize=10;
        Page<Agent> page= agentService.findPaginated(pageNo,pageSize,sortField,sortDir);
       List<Agent> agentList=page.getContent();
       model.addAttribute("currentPage",pageNo);
       model.addAttribute("totalPages",page.getTotalPages());
       model.addAttribute("totalItems",page.getTotalElements());
       model.addAttribute("listAgent",agentList);
       model.addAttribute("sortField",sortField);
       model.addAttribute("sortDir",sortDir);
       model.addAttribute("reverseSortDir",sortDir.equals("asc")?"desc":"asc");
       return "agents/viewAgents";
    }
   @GetMapping("/viewAllAgents")
   public String viewAllagents(Model model)
   {
       return findPaginated(1,"name","asc",model);
   }
   @GetMapping("/viewTeam")
   public String viewAgentsinTeam(Model model)
   {
       return findPaginated(1,"name","asc",model);
   }
    @GetMapping("/pageAgent/{pageNo}")
    public String findPaginatedByAgent(@PathVariable("pageNo") Integer pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir, Agent agent, ModelMap modelMap) {
        int pageSize = 10;

        Page<Call> page = callService.findAllByAgent(agent, pageNo, pageSize, sortField, sortDir);
        List<Call> listCalls = page.getContent();

        modelMap.put("currentPage", pageNo);
        modelMap.put("totalPages", page.getTotalPages());
        modelMap.put("totalItems", page.getTotalElements());

        modelMap.put("sortField", sortField);
        modelMap.put("sortDir", sortDir);
        modelMap.put("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        modelMap.put("listCalls", listCalls);
        return "calls/viewCallsAgent";
    }

    @GetMapping("/team/{teamid}/viewAllCalls")
    public String viewAllTeamCalls(@PathVariable("teamid") Long teamId, ModelMap modelMap) {
        Team team = this.teamRepository.findById(teamId);
        return findPaginatedByTeam(1, "id", "asc", team, modelMap);
    }

    @GetMapping("/teamPage/{pageno}")
    public String findPaginatedByTeam(@PathVariable("pageno") Integer pageNo,
                                      @RequestParam("sortField") String sortField,
                                      @RequestParam("sortDir") String sortDir, Team team, ModelMap modelMap) {
        int pageSize = 10;

        Page<Call> page = callService.findAllByTeam(team, pageNo, pageSize, sortField, sortDir);

        List<Call> listCalls = page.getContent();

        modelMap.put("currentPage", pageNo);
        modelMap.put("totalPages", page.getTotalPages());
        modelMap.put("totalItems", page.getTotalElements());

        modelMap.put("sortField", sortField);
        modelMap.put("sortDir", sortDir);
        modelMap.put("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        modelMap.put("listCalls", listCalls);
        return "calls/viewTeamCalls";
    }

}
