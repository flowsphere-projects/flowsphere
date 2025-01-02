package com.flowsphere.feature.removal;

import com.flowsphere.common.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Map;

import static com.sun.webkit.graphics.GraphicsDecoder.SCALE;


@Slf4j
public class RemovalThread implements Runnable {

    private final long recoveryTime;

    public RemovalThread(long recoveryTime) {
        this.recoveryTime = recoveryTime;
    }

    @Override
    public void run() {
        Map<String, ServiceNode> instanceCallResult = ServiceNodeCache.getInstanceCallResult();
        if (instanceCallResult.isEmpty()) {
            return;
        }
        for (Iterator<Map.Entry<String, ServiceNode>> iterator = instanceCallResult.entrySet().iterator();
             iterator.hasNext(); ) {
            ServiceNode info = iterator.next().getValue();
//            if (System.currentTimeMillis() - info.getLastInvokeTime() >= recoveryTime) {
//                iterator.remove();
//                if (info.getRemovalStatus().get()) {
//                    //TODO 通知server状态更新了
//                }
//                continue;
//            }
            info.setErrorRate(calErrorRate(info));
            System.out.println("计算服务错误率：" + JacksonUtils.toJson(info));
//            log.info("ServiceNode calculating error rate all info {}", info);
        }
    }


    private float calErrorRate(ServiceNode info) {
        if (info.getRequestNum().get() == 0 || info.getRequestFailNum().get() == 0) {
            return 0;
        } else {
            BigDecimal count = new BigDecimal(info.getRequestNum().get());
            BigDecimal failNum = new BigDecimal(info.getRequestFailNum().get());
            return failNum.divide(count, SCALE, RoundingMode.HALF_UP).floatValue();
        }
    }


}
