package org.heartfulness.avtc.form;

public class ScheduleTimeSlot {
    private Integer TimeSlot;

    private boolean selected;

    public Integer getTimeSlot() {
        return TimeSlot;
    }

    public void setTimeSlot(int timeSlot) {
        TimeSlot = timeSlot;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
