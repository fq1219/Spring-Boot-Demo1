package com.example.springbootdemo.DynamicDatasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class DynamicDatasourceAspect {

    @Before("@annotation(com.example.springbootdemo.DynamicDatasource.TargetDatasource)")
    public void before(JoinPoint joinPoint){
        Class clazz = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        Class[] parameters = ((MethodSignature)joinPoint.getSignature()).getParameterTypes();
        try {
            Method method = clazz.getMethod(methodName,parameters);
            if(method.isAnnotationPresent(TargetDatasource.class)) {
                TargetDatasource targetDatasource = method.getAnnotation(TargetDatasource.class);
                DynamicDatasourceContextHolder.setDB(targetDatasource.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @After("@annotation(com.example.springbootdemo.DynamicDatasource.TargetDatasource)")
    public void after(JoinPoint joinPoint){
        DynamicDatasourceContextHolder.clear();

    }
}
