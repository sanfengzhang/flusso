package com.hanl.datamgr.web;

import com.hanl.datamgr.vo.EventVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author: Hanl
 * @date :2019/10/9
 * @desc:
 */
@Slf4j
public class WebRequestEventInterceptor implements HandlerInterceptor {

    ThreadLocal<EventVO> userEventVOThreadLocal = new ThreadLocal<>();  //线程副本类去记录各个线程的开始时间

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        EventVO eventVO = userEventVOThreadLocal.get();
        String id = UUID.randomUUID().toString();
        if (null == eventVO) {
            eventVO = new EventVO();
            eventVO.setId(id);
            //  eventVO.setEventType(0);
            eventVO.setEventStartTime(System.currentTimeMillis());
            String input = request.getRequestURL().toString() + "|" + request.getMethod();
            eventVO.setInput(input);
            userEventVOThreadLocal.set(eventVO);
        }
        if (log.isDebugEnabled()) {
            log.debug("before request event={}", eventVO);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            EventVO eventVO = userEventVOThreadLocal.get();
            if (null != eventVO) {
                long tookTime = System.currentTimeMillis() - eventVO.getEventStartTime();
                eventVO.setTookTime(tookTime);
                eventVO.setOutput(response.getStatus());
            }
            if (eventVO.getTookTime() > 50) {
                log.info("end request event={}", eventVO);
            }
        } finally {
            userEventVOThreadLocal.remove();
        }
    }
}
