package com.flowsphere.extension.sentinel.limiter;

import com.alibaba.csp.sentinel.Entry;

import java.util.LinkedList;
import java.util.List;

public class EntryContext {

    private static ThreadLocal<LinkedList<Entry>> ENTRY_CONTEXT = new ThreadLocal<LinkedList<Entry>>() {

        @Override
        protected LinkedList<Entry> initialValue() {
            return new LinkedList<>();
        }

    };

    public static void add(Entry entry) {
        ENTRY_CONTEXT.get().addFirst(entry);
    }

    public static List<Entry> get() {
        return ENTRY_CONTEXT.get();
    }

    public static void clear() {
        exit();
        ENTRY_CONTEXT.remove();
    }

    public static void exit() {
        for (Entry entry : get()) {
            entry.exit();
        }
    }


}
