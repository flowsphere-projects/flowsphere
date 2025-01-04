package com.flowsphere.common.tag.context;

import com.flowsphere.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import static com.flowsphere.common.constant.CommonConstant.SERVER_TAG;

@Slf4j
public class TagManager {

    public static String getTag() {
        String tag = TagContext.get();
        if (StringUtils.isNotEmpty(tag)) {
            return tag;
        }
        if (log.isDebugEnabled()) {
            log.debug("[flowsphere] TagManager tag context is null");
        }
        return System.getProperty(SERVER_TAG);
    }

    public static String getSystemTag() {
        return System.getProperty(SERVER_TAG);
    }


}
