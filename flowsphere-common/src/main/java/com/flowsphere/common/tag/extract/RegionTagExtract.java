package com.flowsphere.common.tag.extract;

import com.flowsphere.common.header.HeaderResolver;
import com.flowsphere.common.loadbalance.InstantWeight;
import com.flowsphere.common.loadbalance.RegionWeight;
import com.flowsphere.common.loadbalance.TagWeight;

import java.util.List;
import java.util.Objects;

public class RegionTagExtract extends AbstractTagExtract {

    @Override
    public List<TagWeight> getTagWeight(InstantWeight instantWeight) {
        return instantWeight.getRegionWeight().getTagWeights();
    }

    @Override
    public boolean match(InstantWeight instantWeight, HeaderResolver headerResolver) {
        RegionWeight regionWeight = instantWeight.getRegionWeight();
        if (Objects.isNull(regionWeight) || Objects.isNull(regionWeight.getRegions()) || Objects.isNull(regionWeight.getTagWeights())) {
            return false;
        }
        if (regionWeight.getRegions().contains(headerResolver.getRegion())) {
            return true;
        }
        return false;
    }

}
