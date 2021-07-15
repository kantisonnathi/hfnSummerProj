package org.heartfulness.avtc.form;

import org.heartfulness.avtc.model.Agent;
import org.heartfulness.avtc.model.TimeSlot;

public class ScheduleForm {

    private Agent agent;

    private TimeSlot slot;

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public TimeSlot getSlot() {
        return slot;
    }

    public void setSlot(TimeSlot slot) {
        this.slot = slot;
    }
}
