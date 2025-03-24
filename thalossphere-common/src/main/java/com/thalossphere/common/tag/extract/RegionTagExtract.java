package com.thalossphere.common.tag.extract;

import com.thalossphere.common.request.AbstractAttributeResolver;
import com.thalossphere.common.request.HeaderResolver;
import com.thalossphere.common.loadbalance.InstantWeight;
import com.thalossphere.common.loadbalance.RegionWeight;
import com.thalossphere.common.loadbalance.TagWeight;

import java.util.List;
import java.util.Objects;

public class RegionTagExtract extends AbstractTagExtract {

    @Override
    public List<TagWeight> getTagWeight(InstantWeight instantWeight) {
        return instantWeight.getRegionWeight().getTagWeights();
    }



    @Override
    public boolean match(InstantWeight instantWeight, AbstractAttributeResolver attributeResolver) {
        RegionWeight regionWeight = instantWeight.getRegionWeight();
        if (Objects.isNull(regionWeight) || Objects.isNull(regionWeight.getRegions()) || Objects.isNull(regionWeight.getTagWeights())) {
            return false;
        }
        if (regionWeight.getRegions().contains(attributeResolver.getRegion())) {
            return true;
        }
        return false;
    }

}
