package com.flowsphere.agent.plugin.rocketmq.consumer.condition;

import com.flowsphere.common.tag.context.TagManager;

import java.util.function.Predicate;

public class CidSplitMatchGrayCondition implements Predicate<String> {

    private final String tag;

    public CidSplitMatchGrayCondition(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean test(String cid) {
        String[] cidSplit = cid.split("@");
        if (cidSplit.length <= 2) {
            return false;
        }
        return cidSplit[1].equals(tag);
    }

}
