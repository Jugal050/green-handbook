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

### 6. Choosing which AOP Declaration Style to Use

### 7. Mixing Aspect Type
### 8. Proxing Mechanisms
### 9. Programming Creation of @Aspect Proxies
### 10. Using AspectJ with Spring Applications
### 11. Further Resources

​	

