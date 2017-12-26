package com.example.springbootdemo.DynamicDatasource;


import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.aspectj.lang.annotation.Before;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan("com.example.springbootdemo")
public class DynamicDatasourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource druidDataSource(){
        return new DruidDataSource();
    }

    @Value("${spring.datasource.master.url}")
    private String masterUrl;

    @Value("${spring.datasource.master.username}")
    private String masterUser;

    @Value("${spring.datasource.master.password}")
    private String masterPassword;

    @Value("${spring.datasource.master.driverClassName}")
    private String masterDriverClass;

    @Bean("master")
    @Primary
    public DataSource master(){
        DruidDataSource dataSource = druidDataSource();
        dataSource.setDriverClassName(masterDriverClass);
        dataSource.setUrl(masterUrl);
        dataSource.setUsername(masterUser);
        dataSource.setPassword(masterPassword);
        return dataSource;
    }

    @Value("${spring.datasource.slave.url}")
    private String slaveUrl;

    @Value("${spring.datasource.slave.username}")
    private String slaveUser;

    @Value("${spring.datasource.slave.password}")
    private String slavePassword;

    @Value("${spring.datasource.slave.driverClassName}")
    private String slaveDriverClass;

    @Bean("slave")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slave(){
        DruidDataSource dataSource = druidDataSource();
        dataSource.setDriverClassName(slaveDriverClass);
        dataSource.setUrl(slaveUrl);
        dataSource.setUsername(slaveUser);
        dataSource.setPassword(slavePassword);
        return dataSource;
    }

    /*    @Bean("master")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource master(){
        return DataSourceBuilder.create().build();
    }*/

/*    @Bean("slave")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slave(){
        return DataSourceBuilder.create().build();
    }*/

    @Bean
    public DynamicRoutingDatasource dynamicRoutingDatasource(){
        DynamicRoutingDatasource dynamicRoutingDatasource = new DynamicRoutingDatasource();
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put(DynamicDatasourceType.MASTER, master());
        map.put(DynamicDatasourceType.SLAVE, slave());

        dynamicRoutingDatasource.setDefaultTargetDataSource(master());
        dynamicRoutingDatasource.setTargetDataSources(map);

        return dynamicRoutingDatasource;
    }

/*    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(){
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dynamicRoutingDatasource());
        return bean;
    }*/

    @Bean("mybatisSqlSession")
    public SqlSessionFactory sqlSessionFactory(ResourceLoader resourceLoader, GlobalConfiguration globalConfiguration) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dynamicRoutingDatasource());//动态数据源
        sqlSessionFactory.setTypeAliasesPackage("");//别名包路径
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        sqlSessionFactory.setConfiguration(configuration);
        //插件
        sqlSessionFactory.setPlugins(new Interceptor[]{
                new PaginationInterceptor(),
                new PerformanceInterceptor()
        });
        //全局设置
        sqlSessionFactory.setGlobalConfig(globalConfiguration);
        return sqlSessionFactory.getObject();
    }

    @Bean
    public GlobalConfiguration globalConfiguration() {
        GlobalConfiguration conf = new GlobalConfiguration(new LogicSqlInjector());
        conf.setLogicDeleteValue("-1");
        conf.setLogicNotDeleteValue("1");
        conf.setIdType(2);
        return conf;
    }

}
