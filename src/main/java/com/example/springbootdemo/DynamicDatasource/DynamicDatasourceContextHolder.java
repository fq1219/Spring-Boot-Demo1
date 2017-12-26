package com.example.springbootdemo.DynamicDatasource;

public class DynamicDatasourceContextHolder {

    private static ThreadLocal<DynamicDatasourceType>  contextHolder = new ThreadLocal<DynamicDatasourceType>();

    public static void setDB(DynamicDatasourceType dbType){
        contextHolder.set(dbType);
    }

    public static DynamicDatasourceType getDB(){
        return contextHolder.get();
    }

    public static void clear(){
        contextHolder.remove();
    }
}
