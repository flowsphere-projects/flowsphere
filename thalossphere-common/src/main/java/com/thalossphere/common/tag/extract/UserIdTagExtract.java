package com.thalossphere.common.tag.extract;

import com.thalossphere.common.request.AbstractAttributeResolver;
import com.thalossphere.common.request.HeaderResolver;
import com.thalossphere.common.loadbalance.InstantWeight;
import com.thalossphere.common.loadbalance.TagWeight;
import com.thalossphere.common.loadbalance.UserWeight;

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
