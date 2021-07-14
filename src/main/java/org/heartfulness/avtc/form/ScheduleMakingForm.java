package org.heartfulness.avtc.form;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.Team;
import org.heartfulness.avtc.model.TimeSlot;

import java.util.HashSet;
import java.util.Set;

public class ScheduleMakingForm {

    private Agent agent;

    private Set<ScheduleTimeSlot> scheduleTimeSlots;

    public ScheduleMakingForm(Team team) {
        Agent manager = team.getManager();
        this.agent = manager;
        Set<TimeSlot> slots = team.getTimeSlots();
        for (TimeSlot slot : slots) {
            ScheduleTimeSlot scheduleTimeSlot = new ScheduleTimeSlot();
            scheduleTimeSlot.setTimeSlot(slot.getStartTime().toLocalTime().getHour());
            this.scheduleTimeSlots.add(scheduleTimeSlot);
        }
    }

    public void addScheduleTimeSlot(ScheduleTimeSlot scheduleTimeSlot) {
        if (this.scheduleTimeSlots == null) {
            this.scheduleTimeSlots = new HashSet<>();
        }
        scheduleTimeSlots.add(scheduleTimeSlot);
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Set<ScheduleTimeSlot> getScheduleTimeSlots() {
        return scheduleTimeSlots;
    }

    public void setScheduleTimeSlots(Set<ScheduleTimeSlot> scheduleTimeSlots) {
        this.scheduleTimeSlots = scheduleTimeSlots;
    }
}
