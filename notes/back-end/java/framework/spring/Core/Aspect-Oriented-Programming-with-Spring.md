## Aspect Oriented Programming with Spring

### 1. AOP Concepts

##### central AOP concepts and terminology:

| AOP概念和术语 | 说明                                                         |
| ------------- | ------------------------------------------------------------ |
| Aspect        | 切面：对多个类进行横切面的集中模块，如：事务管理。<br />Spring AOP中主要通过xml文件配置或@Aspect注解实现。 |
| Join point    | 连接点：程序执行的节点，如：方法执行、异常处理。<br />Spring AOP中一般代表方法执行。 |
| Advice        | 通知：切面模块在连接点的动作，包括：around-环绕，before-前置，after-后置。 |
| Pointcut      | 切点：连接点匹配断言。通知会关联切点表达式，并在切点匹配的连接点执行。<br />Spring默认使用AspectJ的切点表达式语言。 |
| Introduction  | 简介：在类型代表上声明额外的方法或字段。<br />Spring AOP允许在任何通知对象上引入新的接口或接口实现。 |
| Target object | 目标对象：切面的通知对象。<br />由于Spring AOP是通过运行时代理实现，所以这个对象总是一个代理对象。 |
| AOP proxy     | AOP代理：AOP为了实现切面创建的对象。<br />Spring中，AOP代理是JDK动态代理或这CGLIB代理 |
| Weaving       | 植入：关联切面和创建通知对象的应用或对象。可以在编译时、加载时、运行时关联。<br />Spring AOP，和其他纯Java的AOP框架一样，在运行时进行关联。 |

Spring AOP includes the following types of advice:

| Spring通知类型         | 说明                                                         |
| ---------------------- | ------------------------------------------------------------ |
| Before advice          | Advice that runs before a join point but that does not have the ability to prevent execution flow proceeding to the join point (unless it throws an exception). |
| After returning advice | Advice to be run after a join point completes normally (for example, if a method returns without throwing an exception). |
| After throwing advice  | Advice to be executed if a method exits by throwing an exception. |
| After (finally) advice | Advice to be executed regardless of the means by which a join point exits (normal or exceptional return). |
| Around advice          | Advice that surrounds a join point such as a method invocation. This is the most powerful kind of advice. Around advice can perform custom behavior before and after the method invocation. It is also responsible for choosing whether to proceed to the join point or to shortcut the advised method execution by returning its own return value or throwing an exception. |

### 2. Spring AOP Capabilities and Goals

### 3. AOP Proxies

​	Spring AOP中的AOP代理默认使用标准的JDK动态代理，任何接口均可被代理。

​	Spring AOP支持CGLIB代理，代理除了接口的其他类。

### 4. @Aspect Support

#### 4.1. Enabling @AspectJ Support

`jar包`：`aspectjweaver.jar`

##### Enabling @AspectJ Support with Java Configuration

```java
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {
    
}
```

##### Enabling @AspectJ Support with XML Configuration

```xml
<aop:aspectj-autoproxy/>
```

#### 4.2. Declaring an Aspect

```xml
<bean id="myAspect" class="org.xyz.NotVeryUsefulAspect">
    <!-- configure properties of the aspect here -->
</bean>
```

```java
package org.xyz;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class NotVeryUsefulAspect {

}
```

#### 4.3. Declaring a Pointcut

```java
@Pointcut("execution(* transfer(..))") // the pointcut expression
private void anyOldTransfer() {} // the pointcut signature
```

##### Supported Pointcut Designators

| 切点表达式支持的标识符 | 说明                                             |
| ---------------------- | ------------------------------------------------ |
| execution              | 匹配join point的方法执行。常用。                 |
| within                 | 匹配<在固定类型中>的join point。                 |
| this                   | 匹配<bean引用是给定类型实例>的join point。       |
| target                 | 匹配<目标对象是给定类型实例>的join point。       |
| args                   | 匹配<参数是给定类型实例>的join point。           |
| @target                | 匹配<执行对象类有给定类型的注解>的join point。   |
| @args                  | 匹配<运行时参数有给定类型的注解>的join point。   |
| @within                | 匹配<连接点类型在给定类型的注解中>的join point。 |
| @annotation            | 匹配<连接点有给定注解>的join point。             |

##### Combining Pointcut Expressions

```java
// matches if a method execution join point represents the execution of any public method.
@Pointcut("execution(public * (..))")
private void anyPublicOperation() {} 

// matches if a method execution is in the trading module.
@Pointcut("within(com.xyz.someapp.trading..)")
private void inTrading() {} 

// matches if a method execution represents any public method in the trading module.
@Pointcut("anyPublicOperation() && inTrading()")
private void tradingOperation() {} 
```

##### Sharing Common Pointcut Definitions

```java
package com.xyz.someapp;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SystemArchitecture {

    /
     * A join point is in the web layer if the method is defined
     * in a type in the com.xyz.someapp.web package or any sub-package
     * under that.
     /
    @Pointcut("within(com.xyz.someapp.web..)")
    public void inWebLayer() {}

    /
     * A join point is in the service layer if the method is defined
     * in a type in the com.xyz.someapp.service package or any sub-package
     * under that.
     /
    @Pointcut("within(com.xyz.someapp.service..)")
    public void inServiceLayer() {}

    /
     * A join point is in the data access layer if the method is defined
     * in a type in the com.xyz.someapp.dao package or any sub-package
     * under that.
     /
    @Pointcut("within(com.xyz.someapp.dao..)")
    public void inDataAccessLayer() {}

    /
     * A business service is the execution of any method defined on a service
     * interface. This definition assumes that interfaces are placed in the
     * "service" package, and that implementation types are in sub-packages.
     *
     * If you group service interfaces by functional area (for example,
     * in packages com.xyz.someapp.abc.service and com.xyz.someapp.def.service) then
     * the pointcut expression "execution(* com.xyz.someapp..service..(..))"
     * could be used instead.
     *
     * Alternatively, you can write the expression using the 'bean'
     * PCD, like so "bean(Service)". (This assumes that you have
     * named your Spring service beans in a consistent fashion.)
     */
    @Pointcut("execution( com.xyz.someapp..service..(..))")
    public void businessService() {}

    /*
     * A data access operation is the execution of any method defined on a
     * dao interface. This definition assumes that interfaces are placed in the
     * "dao" package, and that implementation types are in sub-packages.
     */
    @Pointcut("execution( com.xyz.someapp.dao..(..))")
    public void dataAccessOperation() {}

}
```

```xml
<aop:config>
    <aop:advisor
        pointcut="com.xyz.someapp.SystemArchitecture.businessService()"
        advice-ref="tx-advice"/>
</aop:config>

<tx:advice id="tx-advice">
    <tx:attributes>
        <tx:method name="*" propagation="REQUIRED"/>
    </tx:attributes>
</tx:advice>
```

#####  Examples

```xml
execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern) throws-pattern?)
```

```xml
<!-- The execution of any public method: -->
execution(public * *(..))
    
<!-- The execution of any method with a name that begins with set: -->
execution(* set*(..))    

<!-- The execution of any method defined by the AccountService interface: -->
execution(* com.xyz.service.AccountService.*(..))

<!-- The execution of any method defined in the service package: -->
execution(* com.xyz.service..(..))

<!-- The execution of any method defined in the service package or one of its sub-packages: -->
execution(* com.xyz.service...(..))

<!-- Any join point (method execution only in Spring AOP) within the service package: -->
within(com.xyz.service.*)

<!-- Any join point (method execution only in Spring AOP) within the service package or one of its sub-packages: -->
within(com.xyz.service..*)

<!-- Any join point (method execution only in Spring AOP) where the proxy implements the AccountService interface: -->
this(com.xyz.service.AccountService)

<!-- Any join point (method execution only in Spring AOP) where the target object implements the AccountService interface: -->
target(com.xyz.service.AccountService)

<!-- Any join point (method execution only in Spring AOP) that takes a single parameter and where the argument passed at runtime is Serializable: -->
args(java.io.Serializable)

<!-- Any join point (method execution only in Spring AOP) where the target object has a @Transactional annotation: -->
@target(org.springframework.transaction.annotation.Transactional)

<!-- Any join point (method execution only in Spring AOP) where the declared type of the target object has an @Transactional annotation: -->
@within(org.springframework.transaction.annotation.Transactional)

<!-- Any join point (method execution only in Spring AOP) where the executing method has an @Transactional annotation: -->
@annotation(org.springframework.transaction.annotation.Transactional)

<!-- Any join point (method execution only in Spring AOP) which takes a single parameter, and where the runtime type of the argument passed has the @Classified annotation: -->
@args(com.xyz.security.Classified)

<!-- Any join point (method execution only in Spring AOP) on a Spring bean named tradeService: -->
bean(tradeService)

<!-- Any join point (method execution only in Spring AOP) on Spring beans having names that match the wildcard expression *Service: -->
bean(*Service)
```

#### 4.4. Declaring Advice

##### Before Advice

```java
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class BeforeExample {

    @Before("com.xyz.myapp.SystemArchitecture.dataAccessOperation()")
    public void doAccessCheck() {
        // ...
    }
    
    @Before("execution(* com.xyz.myapp.dao..(..))")
    public void doAccessCheck2() {
        // ...
    }

}
```

##### After Returning Advice

```java
@Aspect
public class AfterReturningExample {

    @AfterReturning("com.xyz.myapp.SystemArchitecture.dataAccessOperation()")
    public void doAccessCheck() {
        // ...
    }
    
    @AfterReturning(pointcut="com.xyz.myapp.SystemArchitecture.dataAccessOperation()",
        returning="retVal")
    public void doAccessCheck2(Object retVal) {
        // ...
    }

}
```

#####  After Throwing Advice

```java
@Aspect
public class AfterThrowingExample {

    @AfterThrowing("com.xyz.myapp.SystemArchitecture.dataAccessOperation()")
    public void doRecoveryActions() {
        // ...
    }
    
    @AfterThrowing(pointcut="com.xyz.myapp.SystemArchitecture.dataAccessOperation()",
        throwing="ex")
    public void doRecoveryActions2(DataAccessException ex) {
        // ...
    }

}
```

##### After (Finally) Advice

```java
@Aspect
public class AfterFinallyExample {

    @After("com.xyz.myapp.SystemArchitecture.dataAccessOperation()")
    public void doReleaseLock() {
        // ...
    }

}
```

##### Around Advice

```java
@Aspect
public class AroundExample {

    @Around("com.xyz.myapp.SystemArchitecture.businessService()")
    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
        // start stopwatch
        Object retVal = pjp.proceed();
        // stop stopwatch
        return retVal;
    }

}
```

#####  Advice Parameters

###### Access to the Current `JoinPoint`

- `getArgs()`: Returns the method arguments.
- `getThis()`: Returns the proxy object.
- `getTarget()`: Returns the target object.
- `getSignature()`: Returns a description of the method that is being advised.
- `toString()`: Prints a useful description of the method being advised.

###### Passing Parameters to Advice

```java
@Before("com.xyz.myapp.SystemArchitecture.dataAccessOperation() && args(account,..)")
public void validateAccount(Account account) {
    // ...
}

@Pointcut("com.xyz.myapp.SystemArchitecture.dataAccessOperation() && args(account,..)")
private void accountDataAccessOperation(Account account) {}

@Before("accountDataAccessOperation(account)")
public void validateAccount(Account account) {
    // ...
}
```

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Auditable {
    AuditCode value();
}
```

```java
@Before("com.xyz.lib.Pointcuts.anyPublicMethod() && @annotation(auditable)")
public void audit(Auditable auditable) {
    AuditCode code = auditable.value();
    // ...
}
```

###### Advice Parameters and Generics

```java
public interface Sample<T> {
    void sampleGenericMethod(T param);
    void sampleGenericCollectionMethod(Collection<T> param);
}
```

```java
@Before("execution(* ..Sample+.sampleGenericMethod(*)) && args(param)")
public void beforeSampleMethod(MyType param) {
    // Advice implementation
}

@Before("execution(* ..Sample+.sampleGenericCollectionMethod(*)) && args(param)")
public void beforeSampleMethod(Collection<MyType> param) {
    // Advice implementation
}
```

##### Determining Argument Names

```java
@Before(value="com.xyz.lib.Pointcuts.anyPublicMethod() && target(bean) && @annotation(auditable)", argNames="bean,auditable")
public void audit(Object bean, Auditable auditable) {
    AuditCode code = auditable.value();
    // ... use code and bean
}

@Before(value="com.xyz.lib.Pointcuts.anyPublicMethod() && target(bean) && @annotation(auditable)", argNames="bean,auditable")
public void audit2(JoinPoint jp, Object bean, Auditable auditable) {
    AuditCode code = auditable.value();
    // ... use code, bean, and jp
}

@Before("com.xyz.lib.Pointcuts.anyPublicMethod()")
public void audit(JoinPoint jp) {
    // ... use jp
}
```

###### Proceeding with Arguments

```java
@Around("execution(List<Account> find*(..)) && " +
        "com.xyz.myapp.SystemArchitecture.inDataAccessLayer() && " +
        "args(accountHolderNamePattern)")
public Object preProcessQueryPattern(ProceedingJoinPoint pjp,
        String accountHolderNamePattern) throws Throwable {
    String newPattern = preProcess(accountHolderNamePattern);
    return pjp.proceed(new Object[] {newPattern});
}
```

###### Advice Ordering

#### 4.5 Introductions

```java
@Aspect
public class UsageTracking {

    @DeclareParents(value="com.xzy.myapp.service.*+", defaultImpl=DefaultUsageTracked.class)
    public static UsageTracked mixin;

    @Before("com.xyz.myapp.SystemArchitecture.businessService() && this(usageTracked)")
    public void recordUsage(UsageTracked usageTracked) {
        usageTracked.incrementUseCount();
    }

}
```

#### 4.6. Aspect Instantiation Models

```java
@Aspect("perthis(com.xyz.myapp.SystemArchitecture.businessService())")
public class MyAspect {

    private int someState;

    @Before(com.xyz.myapp.SystemArchitecture.businessService())
    public void recordServiceUsage() {
        // ...
    }

}
```

####  4.7. An AOP Example

```java
@Aspect
public class ConcurrentOperationExecutor implements Ordered {

    private static final int DEFAULT_MAX_RETRIES = 2;

    private int maxRetries = DEFAULT_MAX_RETRIES;
    private int order = 1;

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Around("com.xyz.myapp.SystemArchitecture.businessService()")
    public Object doConcurrentOperation(ProceedingJoinPoint pjp) throws Throwable {
        int numAttempts = 0;
        PessimisticLockingFailureException lockFailureException;
        do {
            numAttempts++;
            try {
                return pjp.proceed();
            }
            catch(PessimisticLockingFailureException ex) {
                lockFailureException = ex;
            }
        } while(numAttempts <= this.maxRetries);
        throw lockFailureException;
    }

}
```

```xml
<aop:aspectj-autoproxy/>

<bean id="concurrentOperationExecutor" class="com.xyz.myapp.service.impl.ConcurrentOperationExecutor">
    <property name="maxRetries" value="3"/>
    <property name="order" value="100"/>
</bean>
```

```java
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
    // marker annotation
}
```

```java
@Around("com.xyz.myapp.SystemArchitecture.businessService() && " +
        "@annotation(com.xyz.myapp.service.Idempotent)")
public Object doConcurrentOperation(ProceedingJoinPoint pjp) throws Throwable {
    // ...
}
```

### 5. Schema-based AOP Support

#### 5.1. Declaring an Aspect

```xml
<aop:config>
    <aop:aspect id="myAspect" ref="aBean">
        ...
    </aop:aspect>
</aop:config>

<bean id="aBean" class="...">
    ...
</bean>
```

#### 5.2. Declaring a Pointcut

```xml
<aop:config>

    <aop:pointcut id="businessService"
        expression="execution(* com.xyz.myapp.service.*.*(..))"/>
    
    <aop:pointcut id="businessService"
            expression="execution(* com.xyz.myapp.service.*.*(..))"/>
    
    <aop:aspect id="myAspect" ref="aBean">

        <aop:pointcut id="businessService"
            expression="execution(* com.xyz.myapp.service.*.*(..)) &amp;&amp; this(service)"/>
        
        <aop:pointcut id="businessService"
            expression="execution(* com.xyz.myapp.service.*.*(..)) and this(service)"/>

        <aop:before pointcut-ref="businessService" method="monitor"/>

        ...

    </aop:aspect>

</aop:config>
```

#### 5.3. Declaring Advice

```xml
<aop:aspect id="beforeExample" ref="aBean">

    <aop:before
        pointcut-ref="dataAccessOperation"
        method="doAccessCheck"/>
    
    <aop:before
        pointcut="execution(* com.xyz.myapp.dao.*.*(..))"
        method="doAccessCheck"/>
    
    <!-- Advice Parameters -->
    <aop:before
    	pointcut="com.xyz.lib.Pointcuts.anyPublicMethod() and @annotation(auditable)"
    	method="audit"
    	arg-names="auditable"/>
    
    <aop:after-returning
        pointcut-ref="dataAccessOperation"
        method="doAccessCheck"/>
    
    <!-- 带返回值：方法定义：public void doAccessCheck(Object retVal) {...} -->
    <aop:after-returning
        pointcut-ref="dataAccessOperation"
        returning="retVal"
        method="doAccessCheck"/>
    
    <aop:after-throwing
        pointcut-ref="dataAccessOperation"
        method="doRecoveryActions"/>
    
    <!-- 带异常返回：public void doRecoveryActions(DataAccessException dataAccessEx) {} -->
    <aop:after-throwing
        pointcut-ref="dataAccessOperation"
        throwing="dataAccessEx"
        method="doRecoveryActions"/>
    
    <aop:after
        pointcut-ref="dataAccessOperation"
        method="doReleaseLock"/>

    <aop:around
        pointcut-ref="businessService"
        method="doBasicProfiling"/>
    ...

</aop:aspect>
```

#### 5.4. Introductions

```xml
<aop:aspect id="usageTrackerAspect" ref="usageTracking">

    <aop:declare-parents
        types-matching="com.xzy.myapp.service.*+"
        implement-interface="com.xyz.myapp.service.tracking.UsageTracked"
        default-impl="com.xyz.myapp.service.tracking.DefaultUsageTracked"/>

    <aop:before
        pointcut="com.xyz.myapp.SystemArchitecture.businessService()
            and this(usageTracked)"
            method="recordUsage"/>

</aop:aspect>
```

```java
public void recordUsage(UsageTracked usageTracked) {
    usageTracked.incrementUseCount();
}
```

```java
UsageTracked usageTracked = (UsageTracked) context.getBean("myService");
```

#### 5.5. Aspect Instantiation Models

#### 5.6. Advisors

```xml
<aop:config>

    <aop:pointcut id="businessService"
        expression="execution(* com.xyz.myapp.service.*.*(..))"/>

    <aop:advisor
        pointcut-ref="businessService"
        advice-ref="tx-advice"/>

</aop:config>

<tx:advice id="tx-advice">
    <tx:attributes>
        <tx:method name="*" propagation="REQUIRED"/>
    </tx:attributes>
</tx:advice>
```

#### 5.7. An AOP Schema Example

```java
public class ConcurrentOperationExecutor implements Ordered {

    private static final int DEFAULT_MAX_RETRIES = 2;

    private int maxRetries = DEFAULT_MAX_RETRIES;
    private int order = 1;

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Object doConcurrentOperation(ProceedingJoinPoint pjp) throws Throwable {
        int numAttempts = 0;
        PessimisticLockingFailureException lockFailureException;
        do {
            numAttempts++;
            try {
                return pjp.proceed();
            }
            catch(PessimisticLockingFailureException ex) {
                lockFailureException = ex;
            }
        } while(numAttempts <= this.maxRetries);
        throw lockFailureException;
    }

}
```

```xml
<aop:config>

    <aop:aspect id="concurrentOperationRetry" ref="concurrentOperationExecutor">

        <aop:pointcut id="idempotentOperation"
            expression="execution(* com.xyz.myapp.service.*.*(..))"/>

        <aop:around
            pointcut-ref="idempotentOperation"
            method="doConcurrentOperation"/>

    </aop:aspect>

</aop:config>

<bean id="concurrentOperationExecutor"
    class="com.xyz.myapp.service.impl.ConcurrentOperationExecutor">
        <property name="maxRetries" value="3"/>
        <property name="order" value="100"/>
</bean>
```

```java
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {
    // marker annotation
}
```

```xml
<aop:pointcut id="idempotentOperation"
        expression="execution(* com.xyz.myapp.service.*.*(..)) and
        @annotation(com.xyz.myapp.service.Idempotent)"/>
```

### 6. Choosing which AOP Declaration Style to Use

#### 6.1. Spring AOP or Full AspectJ?

如果需要在Spring beans上通知操作执行，使用Spring AOP。

如果通知对象没有被Spring容器管理（比如：域对象），或者通知的连接点不止简单的执行方法（比如：连接点是字段的get/set方法），使用AspectJ。

#### 6.2. @AspectJ or XML for Spring AOP?

@Aspect方式（推荐）：

```java
@Pointcut("execution(* get*())")
public void propertyAccess() {}

@Pointcut("execution(org.xyz.Account+ *(..))")
public void operationReturningAnAccount() {}

@Pointcut("propertyAccess() && operationReturningAnAccount()")
public void accountPropertyAccess() {}
```

XML方式：

```xml
<aop:pointcut id="propertyAccess"
        expression="execution(* get*())"/>

<aop:pointcut id="operationReturningAnAccount"
        expression="execution(org.xyz.Account+ *(..))"/>
```

XML方式的两个缺点：

- 不能完全概述一个单点的需求实现。
- 比@Aspect方式有一些限制（比如：不能并列使用已声明的切点名称，如上。）

### 7. Mixing Aspect Type
### 8. Proxing Mechanisms

如果代理的目标对象至少实现了一个接口，则使用JDK动态代理，这个实现类实现的所有接口都会被代理。

如果代理的目标对象没有实现任何接口，则使用CGLIB代理。

如果想强制使用CGLIB代理（比如：代理的目标对象的方法不止只是实现接口的方法）。但要考虑如下问题:

- CGLIB通知不到final修饰的方法，因为在运行期生成的子类中不能重写final方法。
- Spring4.0中，由于CGLIB代理类实例是通过Objenesis类创建的，代理对象的构造器不再会调用两次。只有当JVM不允许绕过构造器时，才可能出现两次调用。

强制使用CGLIB代理的方法：

```xml
<aop:config proxy-target-class="true">
    <!-- other beans defined here... -->
</aop:config>

<!-- 如果使用的是@AspectJ -->
<aop:aspectj-autoproxy proxy-target-class="true"/>
```

#### 8.1. Understanding AOP Proxies

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;

public class SimplePojo implements Pojo {

    private static final Logger log = LoggerFactory.getLogger(SimplePojo.class);

    @Override
    public void foo() {
        log.info("foo start");
        // 同类调用不会使用代理，而是直接调用。
        this.bar();
        // 如果同类调用想使用代理，可以使用如下方法，但是强烈不推荐这样使用
        ((Pojo) AopContext.currentProxy()).bar();
    }

    @Override
    public void bar() {
        log.info("bar start");
    }
}

import org.springframework.aop.aspectj.AspectJAfterAdvice;
import org.springframework.aop.framework.ProxyFactory;

public class ProxyInvocationTest {

    public static void main(String[] args) {
        ProxyInvocationTest test = new ProxyInvocationTest();
        test.proxyInvocationTest();
    }

    /**
     * 常规调用
     */
    public void commonInvocationTest() {
        Pojo pojo = new SimplePojo();
        pojo.foo();
    }

    /**
     * 代理调用
     */
    public void proxyInvocationTest() {
        ProxyFactory factory = new ProxyFactory(new SimplePojo());
        factory.addInterface(Pojo.class);
        Pojo pojo = (Pojo)factory.getProxy();
        pojo.foo();
    }

}
```

### 9. Programming Creation of @Aspect Proxies

```java
// create a factory that can generate a proxy for the given target object
AspectJProxyFactory factory = new AspectJProxyFactory(targetObject);

// add an aspect, the class must be an @AspectJ aspect
// you can call this as many times as you need with different aspects
factory.addAspect(SecurityManager.class);

// you can also add existing aspect instances, the type of the object supplied must be an @AspectJ aspect
factory.addAspect(usageTracker);

// now get the proxy object...
MyInterfaceType proxy = factory.getProxy();
```

### 10. Using AspectJ with Spring Applications

#### 10.1. Using AspectJ to Dependency Inject Domain Objects with Spring

#### 10.2. Other Spring aspects for AspectJ

#### 10.3. Configuring AspectJ Aspects by Using Spring IoC

```xml
<bean id="profiler" class="com.xyz.profiler.Profiler" factory-method="aspectOf"> 
    <property name="profilingStrategy" ref="jamonProfilingStrategy"/>
</bean>

<aop:aspectj-autoproxy>
    <aop:include name="thisBean"/>
    <aop:include name="thatBean"/>
</aop:aspectj-autoproxy>
```

#### 10.4. Load-time Weaving with AspectJ in the Spring Framework

```java
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.util.StopWatch;
import org.springframework.core.annotation.Order;

@Aspect
public class ProfilingAspect {

    @Around("methodsToBeProfiled()")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch sw = new StopWatch(getClass().getSimpleName());
        try {
            sw.start(pjp.getSignature().getName());
            return pjp.proceed();
        } finally {
            sw.stop();
            System.out.println(sw.prettyPrint());
        }
    }

    @Pointcut("execution(public * foo...(..))")
    public void methodsToBeProfiled(){}
}
```

`META-INF/aop.xml`

```xml
<!DOCTYPE aspectj PUBLIC "-//AspectJ//DTD//EN" "https://www.eclipse.org/aspectj/dtd/aspectj.dtd">
<aspectj>

    <weaver>
        <!-- only weave classes in our application-specific packages -->
        <include within="foo.*"/>
    </weaver>

    <aspects>
        <!-- weave in just this aspect -->
        <aspect name="foo.ProfilingAspect"/>
    </aspects>

</aspectj>
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <!-- a service object; we will be profiling its methods -->
    <bean id="entitlementCalculationService"
            class="foo.StubEntitlementCalculationService"/>

    <!-- this switches on the load-time weaving -->
    <context:load-time-weaver/>
</beans>
```

###### DefaultContextLoadTimeWeaver LoadTimeWeaversRuntime Environment

| Runtime Environment                                          | `LoadTimeWeaver` implementation |
| :----------------------------------------------------------- | :------------------------------ |
| Running in [Apache Tomcat](https://tomcat.apache.org/)       | `TomcatLoadTimeWeaver`          |
| Running in [GlassFish](https://glassfish.dev.java.net/) (limited to EAR deployments) | `GlassFishLoadTimeWeaver`       |
| Running in Red Hat’s [JBoss AS](https://www.jboss.org/jbossas/) or [WildFly](https://www.wildfly.org/) | `JBossLoadTimeWeaver`           |
| Running in IBM’s [WebSphere](https://www-01.ibm.com/software/webservers/appserv/was/) | `WebSphereLoadTimeWeaver`       |
| Running in Oracle’s [WebLogic](https://www.oracle.com/technetwork/middleware/weblogic/overview/index-085209.html) | `WebLogicLoadTimeWeaver`        |
| JVM started with Spring `InstrumentationSavingAgent` (`java -javaagent:path/to/spring-instrument.jar`) | `InstrumentationLoadTimeWeaver` |
| Fallback, expecting the underlying ClassLoader to follow common conventions (namely `addTransformer` and optionally a `getThrowawayClassLoader` method) | `ReflectiveLoadTimeWeaver`      |

### 11. Further Resources

​	

