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

   ```java
   ApplicationContext ctx =
       new ClassPathXmlApplicationContext("classpath*:conf/appContext.xml");
   ```

   ​	2.7.3 FileSystemResource Caveats

   ```java
   // 相对路径
   
   	// 1. 以下两种写法是等价的 
   	ApplicationContext ctx = new FileSystemXmlApplicationContext("conf/context.xml");
   	ApplicationContext ctx = new FileSystemXmlApplicationContext("/conf/context.xml");
   
   	// 2. 以下两种写法是等价的 
   	FileSystemXmlApplicationContext ctx = ...;
   	ctx.getResource("some/resource/path/myTemplate.txt");
   	ctx.getResource("/some/resource/path/myTemplate.txt");
   
   // 绝对路径
   	ctx.getResource("file:///some/resource/path/myTemplate.txt");
   	ApplicationContext ctx =
       	new FileSystemXmlApplicationContext("file:///conf/context.xml");
   ```

3. Validation, Data Binding, and Type Conversion

   3.1 Validation by Using Spring’s Validator Interface

   ```java
   /**
    * 简单类属性校验
    */
   public class Person {
       private String name;
       private int age;
       // the usual getters and setters...
   }
   public class PersonValidator implements Validator {
   
       /**
        * This Validator validates only Person instances
        */
       public boolean supports(Class clazz) {
           return Person.class.equals(clazz);
       }
   
       public void validate(Object obj, Errors e) {
           ValidationUtils.rejectIfEmpty(e, "name", "name.empty");
           Person p = (Person) obj;
           if (p.getAge() < 0) {
               e.rejectValue("age", "negativevalue");
           } else if (p.getAge() > 110) {
               e.rejectValue("age", "too.darn.old");
           }
       }
   }
   /**
    * 嵌套类属性校验
    *    推荐：每个类均写校验器，如果外层类需要校验内层类，再外层类校验器中调用内层类校验器即可
    */
   public class Customer {
       private String firstName;
       private String surname;
       private Address address;
       // the usual getters and setters...
   }
   public class Address {}
   public class AddressValidator implements Validator {
       // support() and validate()
   }
   public class CustomerValidator implements Validator {
   
       private final Validator addressValidator;
   
       public CustomerValidator(Validator addressValidator) {
           if (addressValidator == null) {
               throw new IllegalArgumentException("The supplied [Validator] is " +
                   "required and must not be null.");
           }
           if (!addressValidator.supports(Address.class)) {
               throw new IllegalArgumentException("The supplied [Validator] must " +
                   "support the validation of [Address] instances.");
           }
           this.addressValidator = addressValidator;
       }
   
       /**
        * This Validator validates Customer instances, and any subclasses of Customer too
        */
       public boolean supports(Class clazz) {
           return Customer.class.isAssignableFrom(clazz);
       }
   
       public void validate(Object target, Errors errors) {
           ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "field.required");
           ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "field.required");
           Customer customer = (Customer) target;
           try {
               errors.pushNestedPath("address");
               ValidationUtils.invokeValidator(this.addressValidator, customer.getAddress(), errors);
           } finally {
               errors.popNestedPath();
           }
       }
   }
   ```

   3.2 Resolving Codes to Error Messages

   ```java
   /**
    * 校验测试类
    */
   public class ValidateCustomer {
   
       public static void main(String[] args) {
           Customer customer = new ValidateCustomer().generateTestCustomer();
           AddressValidator addressValidator = new AddressValidator();
           CustomerValidator customerValidator = new CustomerValidator(addressValidator);
           BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(customer, "customer");
           customerValidator.validate(customer, bindingResult);
           List<ObjectError> allErrors = bindingResult.getAllErrors();
           allErrors.forEach((error) -> {
               System.out.println(error.getCode() + " : " + error.getDefaultMessage());
           });
       }
   
       private Customer generateTestCustomer() {
           Customer customer = new Customer();
           customer.setFirstName("");
           customer.setSurname("");
           customer.setAddress(generateTestAddress());
           return customer;
       }
   
       private Address generateTestAddress() {
           Address address = new Address();
           address.setId("123456789012345678901234567890");
           address.setAddress("");
           return address;
       }
   
   }
   ```

   3.3 Bean Manipulation and the `BeanWrapper`

   ​	3.3.1 Setting and Getting Basic and Nested Properties

   ```java
   public class Company {
   
       private String name;
       private Employee managingDirector;
   
       public String getName() {
           return this.name;
       }
       public void setName(String name) {
           this.name = name;
       }
       public Employee getManagingDirector() {
           return this.managingDirector;
       }
       public void setManagingDirector(Employee managingDirector) {
           this.managingDirector = managingDirector;
       }
   }
   
   public class Employee {
   
       private String name;
       private float salary;
   
       public String getName() {
           return this.name;
       }
       public void setName(String name) {
           this.name = name;
       }
       public float getSalary() {
           return salary;
       }
       public void setSalary(float salary) {
           this.salary = salary;
       }
   }
   
   public class BeanWrapperTest {
       private void test() {
           BeanWrapper company = new BeanWrapperImpl(new Company());
   		// setting the company name..
   		company.setPropertyValue("name", "Some Company Inc.");
   		// ... can also be done like this:
   		PropertyValue value = new PropertyValue("name", "Some Company Inc.");
   		company.setPropertyValue(value);
   
   		// ok, let's create the director and tie it to the company:
   		BeanWrapper jim = new BeanWrapperImpl(new Employee());
   		jim.setPropertyValue("name", "Jim Stravinsky");
   		company.setPropertyValue("managingDirector", jim.getWrappedInstance());
   
   		// retrieving the salary of the managingDirector through the company
   		Float salary = (Float) company.getPropertyValue("managingDirector.salary");
       }
   }
   
   ```

   ​	3.3.2 Built-in `PropertyEditor` Implementations

   ```java
   public class ExoticType {
   
       private String name;
   
       public ExoticType(String name) {
           this.name = name;
       }
   }
   
   public class DependsOnExoticType {
   
       private ExoticType type;
   
       public void setType(ExoticType type) {
           this.type = type;
       }
   }
   
   // converts string representation to ExoticType object
   public class ExoticTypeEditor extends PropertyEditorSupport {
   
       public void setAsText(String text) {
           setValue(new ExoticType(text.toUpperCase()));
       }
   }
   
   // Using PropertyEditorRegistrar
   public final class CustomPropertyEditorRegistrar implements PropertyEditorRegistrar {
   
       public void registerCustomEditors(PropertyEditorRegistry registry) {
   
           // it is expected that new PropertyEditor instances are created
           registry.registerCustomEditor(ExoticType.class, new ExoticTypeEditor());
   
           // you could register as many custom property editors as are required here...
       }
   }
   
   // Controller
   public final class RegisterUserController extends SimpleFormController {
   
       private final PropertyEditorRegistrar customPropertyEditorRegistrar;
   
       public RegisterUserController(PropertyEditorRegistrar propertyEditorRegistrar) {
           this.customPropertyEditorRegistrar = propertyEditorRegistrar;
       }
   
       protected void initBinder(HttpServletRequest request,
               ServletRequestDataBinder binder) throws Exception {
           this.customPropertyEditorRegistrar.registerCustomEditors(binder);
       }
   
       // other methods to do with registering a User
   }
   ```

   ​	`relate package`: `org.springframework.beans.propertyeditors`

   3.4 Spring Type Conversion

   ​	3.4.1 Converter SPI

   ​	3.4.2 Using ConverterFactory

   ​	3.4.3 Using GenericConverter

   ​	3.4.4 The ConversionService API

   ​	3.4.5 Configuring a ConversionService

   ​	3.4.6 Using a ConversionService Programmatically

   3.5 Spring Field Formatting

   3.6 Configuring a Global Date and Time Format

   3.7 Spring Validation

4. Spring Expression Language (SpEL)

5. Aspect Oriented Programming with Spring

6. Spring AOP APIs

7. Null-safety

8. Data Buffers And Codes

9. Appendix