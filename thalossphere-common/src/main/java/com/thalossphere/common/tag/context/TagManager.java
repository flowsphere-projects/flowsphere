package com.thalossphere.common.tag.context;

import com.thalossphere.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import static com.thalossphere.common.constant.CommonConstant.SERVER_TAG;

@Slf4j
public class TagManager {

    public static String getTag() {
        String tag = TagContext.get();
        if (StringUtils.isNotEmpty(tag)) {
            return tag;
        }
        if (log.isDebugEnabled()) {
            log.debug("[thalossphere] tag context is null");
        }
        return System.getProperty(SERVER_TAG);
    }

    public static String getSystemTag() {
        return System.getProperty(SERVER_TAG);
    }


}
