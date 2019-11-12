# spring-framework-reference核心篇

1. The IOC Container

   1.1 

   1.2

   1.3

   1.4

   1.15 Additional Capabilities of the ApplicationContext

   ​	1.15.1 internationalization using MessageSource

   ​	1.15.2 Standard and Custom Events

   | 事件                       | 说明                                                     |
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

   2.1 Introduction

   2.2 The Resource Interface

   ```java
   public interface InputStreamSource {
       InputStream getInputStream() throws IOException;
   }
   
   public interface Resource extends InputStreamSource {
       boolean exists();
   	default boolean isReadable() { return exists(); }
       default boolean isOpen() { return false; }
       default boolean isFile() { return false; }
       URL getURL() throws IOException;
       URI getURI() throws IOException;
       File getFile() throws IOException;
       default ReadableByteChannel readableChannel() throws IOException {
   		return Channels.newChannel(getInputStream());
   	}
       long contentLength() throws IOException;
       long lastModified() throws IOException;
       Resource createRelative(String relativePath) throws IOException;
       String getFilename();
       String getDescription();
   }
   ```

   2.3 Built-in Resource Implementations

   | Resource实现           | 说明                                                         |
   | ---------------------- | ------------------------------------------------------------ |
   | UrlResource            | 包装`java.net.URL`，解析URL(string), files(file:), http(http:), ftp(ftp:)等 |
   | ClassPathResource      | 类路径资源解析(`classpath:`)                                 |
   | FileSystemResource     | 文件系统资源，入参: `java.io.File` or `java.nio.file.Path`, 可返回`File`,`URL`。 |
   | ServletContextResource | ServletContext上下文资源，解析web应用根目录下的相对路径的资源。 |
   | InputStreamResource    | InputStream资源类实现，仅当没有其他的资源类实现时使用        |
   | ByteArrayResource      | 字节数组资源类实现，                                         |

   2.4 The ResourceLoader

   ```java
   public interface ResourceLoader {
       Resource getResource(String location);
   }
   /* 获取资源测试类 */
   public class test {
       public static void main(String[] args) {
           ApplicationContext ctx = new ClassPathXmlApplicationContext("conf/context.xml");
           Resource template = ctx.getResource("some/resource/path/myTemplate.txt");
           template = ctx.getResource("classpath:some/resource/path/myTemplate.txt");
           template = ctx.getResource("file:///some/resource/path/myTemplate.txt");
           template = ctx.getResource("https://myhost.com/resource/path/myTemplate.txt";
       }
   }
   ```

   2.5 The ResourceLoaderAware interface

   2.6 Resources as Dependencies

   ```xml
   <bean id="myBean" class="...">
       <property name="template" value="some/resource/path/myTemplate.txt"/>
       <property name="template" value="classpath:some/resource/path/myTemplate.txt">
       <property name="template" value="file:///some/resource/path/myTemplate.txt"/>
   </bean>
   ```

   2.7 Application Contexts and Resource Paths

   ​	2.7.1 Constructing Application Contexts

   ```java
   ApplicationContext ctx = new ClassPathXmlApplicationContext("conf/appContext.xml");
   ctx = new FileSystemXmlApplicationContext("conf/appContext.xml");
   ctx = new FileSystemXmlApplicationContext("classpath:conf/appContext.xml");
   ctx = new ClassPathXmlApplicationContext(
       new String[] {"services.xml", "daos.xml"}, MessengerService.class);
   ```

   ​	2.7.2 Wildcards in Application Context Construtor Resource Paths

   ​	2.7.3 FileSystemResource Caveats

3. Validation, Data Binding, and Type Conversion

4. Spring Expression Language (SpEL)

5. Aspect Oriented Programming with Spring

6. Spring AOP APIs

7. Null-safety

8. Data Buffers And Codes

9. Appendix