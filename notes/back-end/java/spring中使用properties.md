# Spring中，properties文件的两种引入方式：

## 方式一：<util:properties>

### 说明：

以声明bean方式来使用，创建了一个bean，通过SpEL表达式#{}获取bean的属性

### 示例：

#### ApplicationContext.xml

###### Before

```xml
<!-- creates a java.util.Properties instance with values loaded from the supplied location -->
<bean id="jdbcConfiguration" class="org.springframework.beans.factory.config.PropertiesFactoryBean">
<property name="location" value="classpath:com/foo/jdbc-production.properties"/>
</bean>
```

##### After

```xml
<!-- creates a java.util.Properties instance with values loaded from the supplied location -->
<util:properties location="classpath:com/foo/jdbc.properties" id="jdbcConfig"/>
<util:properties location="classpath:com/foo/api.properties" id="apiConfig"/>
<util:properties location="classpath:com/foo/env.properties" id="envConfig"/>
```

#### jdbc.properties

```properties
jdbc.driverClassName=org.hsqldb.jdbcDriver
jdbc.url=jdbc:hsqldb:hsql://production:9002
jdbc.username=sa
jdbc.password=root
```

#### service

```java
public class XXXService {

    @Value("#{jdbcConfig["jdbc.url"]}")
    private String url;

    @Value("#{jdbcConfig["jdbc.driverClassName"]}")
    private String driverClassName;

    @Value("#{jdbcConfig["jdbc.username"]}")
    private String username;

    @Value("#{jdbcConfig["jdbc.password"]}")
    private String password;
    
}
```

## 方式二：<context:property-placeholder>

### 说明：

将配置文件加载至spring上下文中，通过${}获取值，常用于bean的属性上。

### 示例：

#### ApplicationContext.xml

```xml
<!-- 多个配置用逗号隔开 -->
<context:property-placeholder location="classpath:com/foo/jdbc.properties,classpath:com/foo/api.properties,classpath:com/foo/env.properties"/>

<!-- 等价于 -->
<bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
	<property name="locations" value="classpath:com/foo/jdbc.properties,classpath:com/foo/api.properties,classpath:com/foo/env.properties">
</bean>

<!-- 等价于 -->
<beans:bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
	<beans:properties name="locations">
		<beans:list>									
            <bean:value>classpath:com/foo/jdbc.properties</bean:value>
            <bean:value>classpath:com/foo/api.properties</bean:value>
            <bean:value>classpath:com/foo/env.properties</bean:value>
		</beans:list>
	</beans:properties>
</beans:bean>

<!-- 运行时使用 -->	
<bean id="dataSource" destroy-method="close" class="org.apache.commons.dbcp.BasicDataSource">
	<property name="driverClassName" value="${jdbc.driverClassName}"/>
    <property name="url" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>
```

#### jdbc.properties

```properties
jdbc.driverClassName=org.hsqldb.jdbcDriver
jdbc.url=jdbc:hsqldb:hsql://production:9002
jdbc.username=sa
jdbc.password=root
```

#### service

```java
public class XXXService {
    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.driverClassName}")
    private String driverClassName;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;
}
```

