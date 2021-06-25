package org.heartfulness.avtc.Comparator;

import org.heartfulness.avtc.model.Agent;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Comparator;

public class SortByTimeStamp implements Comparator<Agent> {

    @Override
    public int compare(Agent o1, Agent o2) {
        Timestamp ts1=o1.getTimestamp();
        Timestamp ts2=o2.getTimestamp();
        return ts1.compareTo(ts2);

    }
}
