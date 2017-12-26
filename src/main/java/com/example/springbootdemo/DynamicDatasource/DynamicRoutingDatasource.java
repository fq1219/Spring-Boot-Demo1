package com.example.springbootdemo.DynamicDatasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicRoutingDatasource extends AbstractRoutingDataSource{
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDatasourceContextHolder.getDB();
    }
}
