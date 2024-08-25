package com.flowsphere.common.tag.extract;

import com.flowsphere.common.header.HeaderResolver;
import com.flowsphere.common.loadbalance.InstantWeight;

import java.util.LinkedList;
import java.util.List;

public class TagExtractManager {

    private static final List<TagExtract> TAG_SELECTOR_LIST = new LinkedList<>();

    static {
        TAG_SELECTOR_LIST.add(new ClientDefaultTagExtract());
        TAG_SELECTOR_LIST.add(new RegionTagExtract());
        TAG_SELECTOR_LIST.add(new UserIdTagExtract());
    }

    public static String extract(InstantWeight instantWeight, HeaderResolver headerResolver) {
        for (TagExtract selector : TAG_SELECTOR_LIST) {
            if (selector.match(instantWeight, headerResolver)) {
                return selector.extract(instantWeight, headerResolver);
            }
        }
        return null;
    }

}
