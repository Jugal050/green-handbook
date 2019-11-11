# spring-framework-reference核心篇

1. The IOC Container

   1.1 

   1.2

   1.3

   1.4

   1.15 Additional Capabilities of the ApplicationContext

   ​	1.15.1 internationalization using MessageSource

   ​	1.15.2 Standard and Custom Events

   | 事件                       | 解释                                                     |
   | -------------------------- | -------------------------------------------------------- |
   | ContextRefreshedEvent      | ApplicationContext初始化完成或刷新时执行                 |
   | ContextStartedEvent        | ApplicationContext启动时执行                             |
   | ContextStoppedEvent        | ApplicationContext停止时执行                             |
   | ContextClosedEvent         | ApplicationContext关闭时或者JVM关闭时执行                |
   | RequestHandledEvent        | HTTP请求处理完成后执行                                   |
   | ServletRequestHandledEvent | RequestHandledEvent子类，增加了一些有关Servlet的特殊信息 |

   1.16 The Bean Factory

   ​	1.16.1 BeanFactory or ApplicationContext?

   | Feature                                                 | `BeanFactory` | `ApplicationContext` |
   | :------------------------------------------------------ | :------------ | :------------------- |
   | Bean instantiation/wiring                               | Yes           | Yes                  |
   | Integrated lifecycle management                         | No            | Yes                  |
   | Automatic `BeanPostProcessor` registration              | No            | Yes                  |
   | Automatic `BeanFactoryPostProcessor` registration       | No            | Yes                  |
   | Convenient `MessageSource` access (for internalization) | No            | Yes                  |
   | Built-in `ApplicationEvent` publication mechanism       | No            | Yes                  |

2. Resources

3. Validation, Data Binding, and Type Conversion

4. Spring Expression Language (SpEL)

5. Aspect Oriented Programming with Spring

6. Spring AOP APIs

7. Null-safety

8. Data Buffers And Codes

9. Appendix