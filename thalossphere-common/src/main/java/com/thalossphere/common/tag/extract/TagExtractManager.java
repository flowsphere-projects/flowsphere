package com.thalossphere.common.tag.extract;

import com.thalossphere.common.loadbalance.InstantWeight;
import com.thalossphere.common.request.AbstractAttributeResolver;

import java.util.LinkedList;
import java.util.List;

public class TagExtractManager {

    private static final List<TagExtract> TAG_SELECTOR_LIST = new LinkedList<>();

    static {
        TAG_SELECTOR_LIST.add(new ClientDefaultTagExtract());
        TAG_SELECTOR_LIST.add(new RegionTagExtract());
        TAG_SELECTOR_LIST.add(new UserIdTagExtract());
    }

    public static String extract(InstantWeight instantWeight, AbstractAttributeResolver attributeResolver) {
        for (TagExtract selector : TAG_SELECTOR_LIST) {
            if (selector.match(instantWeight, attributeResolver)) {
                return selector.extract(instantWeight, attributeResolver);
            }
        }
        return null;
    }

}
