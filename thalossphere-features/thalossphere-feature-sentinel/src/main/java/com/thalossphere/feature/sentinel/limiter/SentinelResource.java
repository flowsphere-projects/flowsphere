package com.thalossphere.feature.sentinel.limiter;

import com.alibaba.csp.sentinel.EntryType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SentinelResource {

    private String resourceName;

    private String contextName;

    private String origin;

    private EntryType entryType = EntryType.OUT;

}
