package org.heartfulness.avtc.Comparator;

import org.heartfulness.avtc.model.Logger;

import java.util.Comparator;

public class sortByLog implements Comparator<Logger> {

    @Override
    public int compare(Logger o1, Logger o2) {
        return o1.getTimestamp().compareTo(o2.getTimestamp());
    }
}
