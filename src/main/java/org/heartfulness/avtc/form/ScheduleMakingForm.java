package org.heartfulness.avtc.form;

import org.heartfulness.avtc.model.Agent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScheduleMakingForm {

    private Agent agent;

    private ScheduleMakingFormHelper scheduleMakingFormHelper;

    public ScheduleMakingForm(Agent agent) {
        this.agent = agent;
        this.scheduleMakingFormHelper = new ScheduleMakingFormHelper();
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public ScheduleMakingFormHelper getScheduleMakingFormHelper() {
        return scheduleMakingFormHelper;
    }

    public void setScheduleMakingFormHelper(ScheduleMakingFormHelper scheduleMakingFormHelper) {
        this.scheduleMakingFormHelper = scheduleMakingFormHelper;
    }
}
