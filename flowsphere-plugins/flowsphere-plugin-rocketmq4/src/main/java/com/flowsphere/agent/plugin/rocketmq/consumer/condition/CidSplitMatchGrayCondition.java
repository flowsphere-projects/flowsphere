package com.flowsphere.agent.plugin.rocketmq.consumer.condition;

import com.flowsphere.common.tag.context.TagManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CidSplitMatchGrayCondition implements Predicate<String> {

    @Override
    public boolean test(String cid) {
        String[] cidSplit = cid.split("@");
        if (cidSplit.length <= 2) {
            return false;
        }
        return cidSplit[1].equals(TagManager.getSystemTag());
    }

    public static void main(String[] args) {
        List cids = new ArrayList();
        cids.add("192.168.204.1@tagA1@32a2256e52b542c7819adddc369e7daf");
        cids.add("26.26.26.1@tagA@f6dc43c5e8b34e48868f369508425d5e");
        List<String> includeSystemTagCidList = filterCid(cids, new CidSplitMatchGrayCondition());
        System.out.println(includeSystemTagCidList);
    }


    private static List<String> filterCid(List<String> cidAll, Predicate<String> predicate) {
        return cidAll.stream().filter(predicate).collect(Collectors.toList());
    }
}
