package com.flowsphere.test.loadbalance;

import com.flowsphere.common.loadbalance.ArrayWeightRandom;
import com.flowsphere.common.loadbalance.TagWeight;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ArrayWeightRandomTest {

    private static final String TAG_A = "tagA";

    private static final double TAG_A_WEIGHT = 0.3;

    private static final String TAG_B = "tagB";

    private static final double TAG_B_WEIGHT = 0.7;

    @Test
    public void randomLoadBalanceTest() {
        AtomicInteger tagACounter = new AtomicInteger();
        AtomicInteger tagA1Counter = new AtomicInteger();
        for (int i = 0; i < 10000; i++) {
            List<TagWeight> tagWeights = new ArrayList<>();
            tagWeights.add(new TagWeight(TAG_A, TAG_A_WEIGHT));
            tagWeights.add(new TagWeight(TAG_B, TAG_B_WEIGHT));
            ArrayWeightRandom arrayWeightRandom = new ArrayWeightRandom(tagWeights);
            String tag = arrayWeightRandom.choose();
            if (tag.equals(TAG_A)) {
                tagACounter.incrementAndGet();
            }
            if (tag.equals(TAG_B)) {
                tagA1Counter.incrementAndGet();
            }
        }
        //随机会有一定误差，取个范围值就好
        Assertions.assertTrue(tagACounter.get() > 2800);
        Assertions.assertTrue(tagA1Counter.get() > 6900);
    }


}