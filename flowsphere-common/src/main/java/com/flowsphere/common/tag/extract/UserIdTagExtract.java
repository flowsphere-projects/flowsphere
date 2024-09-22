package com.flowsphere.common.tag.extract;

import com.flowsphere.common.request.AbstractAttributeResolver;
import com.flowsphere.common.request.HeaderResolver;
import com.flowsphere.common.loadbalance.InstantWeight;
import com.flowsphere.common.loadbalance.TagWeight;
import com.flowsphere.common.loadbalance.UserWeight;

import java.util.List;
import java.util.Objects;

public class UserIdTagExtract extends AbstractTagExtract {

    @Override
    public List<TagWeight> getTagWeight(InstantWeight instantWeight) {
        return instantWeight.getUserWeight().getTagWeights();
    }

    @Override
    public boolean match(InstantWeight instantWeight,  AbstractAttributeResolver attributeResolver) {
        UserWeight userWeight = instantWeight.getUserWeight();
        if (Objects.isNull(userWeight) || Objects.isNull(userWeight.getUserIds()) || Objects.isNull(userWeight.getTagWeights())) {
            return false;
        }
        if (userWeight.getUserIds().contains(attributeResolver.getUserId())) {
            return true;
        }
        return false;
    }

}
