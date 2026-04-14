package org.lnu.teaching.web.application.design.deanery.aspect;

import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.lnu.teaching.web.application.design.deanery.annotation.TrackExecution;
import org.springframework.context.annotation.Configuration;

@Log
@Aspect
@Configuration
public class TrackingExecutionAspect {
    @Around("@annotation(trackExecution)")
    public Object trackExecutionSpecifiedForMethod(ProceedingJoinPoint pjp, TrackExecution trackExecution) throws Throwable {
        return handleTrackingExecution(pjp, trackExecution);
    }

    @Around("@within(trackExecution) && !@annotation(org.lnu.teaching.web.application.design.deanery.annotation.TrackExecution)")
    public Object trackExecutionSpecifiedForClass(ProceedingJoinPoint pjp, TrackExecution trackExecution) throws Throwable {
        return handleTrackingExecution(pjp, trackExecution);
    }

    private Object handleTrackingExecution(ProceedingJoinPoint pjp, TrackExecution trackExecution) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = pjp.proceed(pjp.getArgs());
        long entTime = System.currentTimeMillis();

        if (trackExecution.inEnabled()) {
            Signature signature = pjp.getSignature();
            StringBuilder messageSb = new StringBuilder(String.format("Executed method: %s", signature));
            if (trackExecution.inExecutionTimeEnabled()) {
                long executionTime = entTime - startTime;
                messageSb.append(String.format(". Execution time: %d", executionTime));
            }

            log.info(messageSb.toString());
        }

        return result;
    }
}
