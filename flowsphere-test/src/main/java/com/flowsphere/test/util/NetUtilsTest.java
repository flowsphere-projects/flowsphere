package com.flowsphere.test.util;

import com.flowsphere.common.utils.NetUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

public class NetUtilsTest {

    @Test
    public void getIpAddressTest() {
        String ipAddress = NetUtils.getIpAddress();
        Assertions.assertTrue(!StringUtils.isEmpty(ipAddress));
    }

}
