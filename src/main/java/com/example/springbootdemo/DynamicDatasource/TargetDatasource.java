package com.example.springbootdemo.DynamicDatasource;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDatasource {
    DynamicDatasourceType value();
}
