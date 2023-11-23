package com.yrgo.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Component
public class PerformanceTimingAdvice {

    public Object performTimingMeasurement(ProceedingJoinPoint method) throws Throwable {

        //before
        long startTime = System.nanoTime();

        try {
            //proceed to target
            Object value = method.proceed();
            return value;
        } finally {
            //after
            long endTime = System.nanoTime();
            long timeTaken = endTime - startTime;
            System.out.println("The method " + method.getSignature().getName() + " took " + timeTaken / 1000000);
        }
    }

   /*
    //before Advice
    public void beforeAdviceTesting() {
        System.out.println("Now entering method ....");
    }
    */
}
