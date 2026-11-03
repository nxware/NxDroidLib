package net.bsnx.jlib.collections;

import java.util.ArrayList;

public class LimitedQueue<T> extends ArrayList<T> {

    private int limit;

    public LimitedQueue(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(T o) {
        boolean added = super.add(o);
        while (added && size() > limit) {
            super.remove(0);
        }
        return added;
    }
}
