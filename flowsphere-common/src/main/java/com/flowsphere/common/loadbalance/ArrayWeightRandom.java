package com.flowsphere.common.loadbalance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

// Refactory with code from https://zhuanlan.zhihu.com/p/389788435
public class ArrayWeightRandom {

    private final List<String> items = new ArrayList<>();
    private double[] weights;

    public ArrayWeightRandom(List<TagWeight> tagWeights) {
        this.calWeights(tagWeights);
    }

    /**
     * 计算权重，初始化或者重新定义权重时使用
     */
    public void calWeights(List<TagWeight> tagWeights) {
        items.clear();

        // 计算权重总和
        double originWeightSum = 0;
        for (TagWeight itemWithWeight : tagWeights) {
            double weight = itemWithWeight.getWeight();
            if (weight <= 0) {
                continue;
            }

            items.add(itemWithWeight.getTag());
            if (Double.isInfinite(weight)) {
                weight = 10000.0D;
            }
            if (Double.isNaN(weight)) {
                weight = 1.0D;
            }
            originWeightSum += weight;
        }

        // 计算每个item的实际权重比例
        double[] actualWeightRatios = new double[items.size()];
        int index = 0;
        for (TagWeight itemWithWeight : tagWeights) {
            double weight = itemWithWeight.getWeight();
            if (weight <= 0) {
                continue;
            }
            actualWeightRatios[index++] = weight / originWeightSum;
        }

        // 计算每个item的权重范围
        // 权重范围起始位置
        weights = new double[items.size()];
        double weightRangeStartPos = 0;
        for (int i = 0; i < index; i++) {
            weights[i] = weightRangeStartPos + actualWeightRatios[i];
            weightRangeStartPos += actualWeightRatios[i];
        }
    }

    /**
     * 基于权重随机算法选择
     */
    public String choose() {
        double random = ThreadLocalRandom.current().nextDouble();
        //二分法快速查找范围索引
        int index = Arrays.binarySearch(weights, random);
        if (index < 0) {
            index = -index - 1;
        } else {
            return items.get(index);
        }

        if (index < weights.length && random < weights[index]) {
            return items.get(index);
        }

        // 通常不会走到这里，为了保证能得到正确的返回，这里随便返回一个
        return items.get(0);
    }

}
