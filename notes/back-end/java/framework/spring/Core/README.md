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

   ​	`related package`: `org.springframework.beans.propertyeditors`

   3.4 Spring Type Conversion

   ​	3.4.1 Converter SPI

   ```java
   package org.springframework.core.convert.converter;
   import org.springframework.lang.Nullable;
   @FunctionalInterface
   public interface Converter<S, T> {
       @Nullable
       T convert(S s);
   }
   ```

   ​	3.4.2 Using ConverterFactory

   ```java
   package org.springframework.core.convert.converter;
   public interface ConverterFactory<S, R> {
       <T extends R> Converter<S, T> getConverter(Class<T> targetType);
   }
   ```

   ​	3.4.3 Using GenericConverter

   ```java
   package org.springframework.core.convert.converter;
   public interface GenericConverter {
       
       @Nullable
   	Set<ConvertiblePair> getConvertibleTypes();
       
       @Nullable
   	Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType);
       
       final class ConvertiblePair {
   
   		private final Class<?> sourceType;
   
   		private final Class<?> targetType;
   
   		/**
   		 * Create a new source-to-target pair.
   		 * @param sourceType the source type
   		 * @param targetType the target type
   		 */
   		public ConvertiblePair(Class<?> sourceType, Class<?> targetType) {
   			Assert.notNull(sourceType, "Source type must not be null");
   			Assert.notNull(targetType, "Target type must not be null");
   			this.sourceType = sourceType;
   			this.targetType = targetType;
   		}
   
   		public Class<?> getSourceType() {
   			return this.sourceType;
   		}
   
   		public Class<?> getTargetType() {
   			return this.targetType;
   		}
   
   		@Override
   		public boolean equals(@Nullable Object other) {
   			if (this == other) {
   				return true;
   			}
   			if (other == null || other.getClass() != ConvertiblePair.class) {
   				return false;
   			}
   			ConvertiblePair otherPair = (ConvertiblePair) other;
   			return (this.sourceType == otherPair.sourceType && this.targetType == otherPair.targetType);
   		}
   
   		@Override
   		public int hashCode() {
   			return (this.sourceType.hashCode() * 31 + this.targetType.hashCode());
   		}
   
   		@Override
   		public String toString() {
   			return (this.sourceType.getName() + " -> " + this.targetType.getName());
   		}
   	}
   
   }
   
   public interface ConditionalConverter {
       boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType);
   }
   
   public interface ConditionalGenericConverter extends GenericConverter, ConditionalConverter {
       
   }
   ```

   ​	3.4.4 The ConversionService API

   ```java
   package org.springframework.core.convert;
   
   public interface ConversionService {
   
       boolean canConvert(Class<?> sourceType, Class<?> targetType);
   
       <T> T convert(Object source, Class<T> targetType);
   
       boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType);
   
       Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);
   
   }
   ```

   ​	3.4.5 Configuring a ConversionService

   ```xml
   <bean id="conversionService"
           class="org.springframework.context.support.ConversionServiceFactoryBean">
       <property name="converters">
           <set>
               <bean class="example.MyCustomConverter"/>
           </set>
       </property>
   </bean>
   ```

   ​	3.4.6 Using a ConversionService Programmatically

   ```java
   DefaultConversionService cs = new DefaultConversionService();
   
   List<Integer> input = ...
   cs.convert(input,
       TypeDescriptor.forObject(input), // List<Integer> type descriptor
       TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(String.class)));
   ```

   3.5 Spring Field Formatting

   ​	3.5.1 The `Formatter` SPI

   ```java
   public interface Formatter<T> extends Printer<T>, Parser<T> {
   
   }
   @FunctionalInterface
   public interface Printer<T> {
       String print(T object, Locale locale);
   }
   @FunctionalInterface
   public interface Parser<T> {
       T parse(String text, Locale locale) throws ParseException;
   }
   ```

   ​	 3.5.2 Annotation-driven Formatting

   ```java
   package org.springframework.format;
   
   public interface AnnotationFormatterFactory<A extends Annotation> {
   
       Set<Class<?>> getFieldTypes();
   
       Printer<?> getPrinter(A annotation, Class<?> fieldType);
   
       Parser<?> getParser(A annotation, Class<?> fieldType);
   }
   ```

   ```java
   public final class NumberFormatAnnotationFormatterFactory
           implements AnnotationFormatterFactory<NumberFormat> {
   
       public Set<Class<?>> getFieldTypes() {
           return new HashSet<Class<?>>(asList(new Class<?>[] {
               Short.class, Integer.class, Long.class, Float.class,
               Double.class, BigDecimal.class, BigInteger.class }));
       }
   
       public Printer<Number> getPrinter(NumberFormat annotation, Class<?> fieldType) {
           return configureFormatterFrom(annotation, fieldType);
       }
   
       public Parser<Number> getParser(NumberFormat annotation, Class<?> fieldType) {
           return configureFormatterFrom(annotation, fieldType);
       }
   
       private Formatter<Number> configureFormatterFrom(NumberFormat annotation, Class<?> fieldType) {
           if (!annotation.pattern().isEmpty()) {
               return new NumberStyleFormatter(annotation.pattern());
           } else {
               Style style = annotation.style();
               if (style == Style.PERCENT) {
                   return new PercentStyleFormatter();
               } else if (style == Style.CURRENCY) {
                   return new CurrencyStyleFormatter();
               } else {
                   return new NumberStyleFormatter();
               }
           }
       }
   }
   ```

   ```java
   public class MyModel {
       @NumberFormat(style=Style.CURRENCY)
       private BigDecimal decimal;
       
       @DateTimeFormat(iso=ISO.DATE)
       private Date date;
   }
   ```

   ​	3.5.3 The `FormatterRegistry` SPI

   ```java
   package org.springframework.format;
   
   public interface FormatterRegistry extends ConverterRegistry {
   
       void addFormatterForFieldType(Class<?> fieldType, Printer<?> printer, Parser<?> parser);
   
       void addFormatterForFieldType(Class<?> fieldType, Formatter<?> formatter);
   
       void addFormatterForFieldType(Formatter<?> formatter);
   
       void addFormatterForAnnotation(AnnotationFormatterFactory<?> factory);
   }
   ```

   ​	3.5.4 The `FormatterRegistrar` SPI

   ```java
   package org.springframework.format;
   
   public interface FormatterRegistrar {
   
       void registerFormatters(FormatterRegistry registry);
   }
   ```

   ​	3.5.5 Configuring Formatting in Spring MVC

   3.6 Configuring a Global Date and Time Format

   ```java
   @Configuration
   public class AppConfig {
   
       @Bean
       public FormattingConversionService conversionService() {
   
           // Use the DefaultFormattingConversionService but do not register defaults
           DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);
   
           // Ensure @NumberFormat is still supported
           conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());
   
           // Register date conversion with a specific global format
           DateFormatterRegistrar registrar = new DateFormatterRegistrar();
           registrar.setFormatter(new DateFormatter("yyyyMMdd"));
           registrar.registerFormatters(conversionService);
   
           return conversionService;
       }
   }
   ```

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
           http://www.springframework.org/schema/beans
           https://www.springframework.org/schema/beans/spring-beans.xsd>
   
       <bean id="conversionService" class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
           <property name="registerDefaultFormatters" value="false" />
           <property name="formatters">
               <set>
                   <bean class="org.springframework.format.number.NumberFormatAnnotationFormatterFactory" />
               </set>
           </property>
           <property name="formatterRegistrars">
               <set>
                   <bean class="org.springframework.format.datetime.joda.JodaTimeFormatterRegistrar">
                       <property name="dateFormatter">
                           <bean class="org.springframework.format.datetime.joda.DateTimeFormatterFactoryBean">
                               <property name="pattern" value="yyyyMMdd"/>
                           </bean>
                       </property>
                   </bean>
               </set>
           </property>
       </bean>
   </beans>
   ```

   3.7 Spring Validation

   ​	3.7.1 Overview of the JSR-303 Bean Validation API

   ```java
   public class PersonForm {
   
       @NotNull
       @Size(max=64)
       private String name;
   
       @Min(0)
       private int age;
   }
   ```

   ​	3.7.2 Configuring a Bean Validation Provider

   ```xml
   <bean id="validator"
       class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean"/>
   ```

   ​	3.7.3 Configuring a `DataBinder`

   ```java
   public class DataBinderValidation {
   
       public static void main(String[] args) {
           Foo foo = new Foo();
           DataBinder dataBinder = new DataBinder(foo);
           dataBinder.setValidator(new FooValidator());
           MutablePropertyValues pvs = new MutablePropertyValues();
           pvs.add("id", "123").add("name", "binvi");
           dataBinder.bind(pvs);
           dataBinder.validate();
           BindingResult bindingResult = dataBinder.getBindingResult();
       }
   
   }
   
   @Data
   class Foo {
       @NotNull
       String id;
       @Size(min = 10, max = 200)
       String name;
   }
   
   class FooValidator implements Validator {
   
       @Override
       public boolean supports(Class<?> clazz) {
           return true;
       }
   
       @Override
       public void validate(Object target, Errors errors) {
   
       }
   }
   ```

   ​	3.7.4 Spring MVC 3 Validation

4. Spring Expression Language (SpEL)

     4.1 Evalution

   ```java
   /**
    * SpEL测试类
    */
   public class EvaluationTest {
   
       private static final Logger log = LoggerFactory.getLogger(EvaluationTest.class);
   
       public static void main(String[] args) {
           EvaluationTest test = new EvaluationTest();
           test.test();
       }
   
       /**
        * 简单测试类
        */
       private void test() {
           // the SpEL API to evaluate the literal string expression
           SpelExpressionParser parser = new SpelExpressionParser();
           Expression expression = parser.parseExpression("'Hello World'");
           String value = (String) expression.getValue();
           log.info(value);
   
           // SpEL supports a wide range of features, such as calling methods, accessing properties, and calling constructors.
           expression = parser.parseExpression("'Hello World'.concat('!')");
           value = (String)expression.getValue();
           log.info(value);
   
           // calling a JavaBean property calls the String property Bytes:
           expression = parser.parseExpression("'Hello World!'.bytes");
           byte[] bytes = (byte[])expression.getValue();
           log.info(new String(bytes));
   
           // SpEL also supports nested properties by using the standard dot notation (such as prop1.prop2.prop3) and also the corresponding setting of property values. Public fields may also be accessed.
           expression = parser.parseExpression("'Hello World!'.bytes.length");
           Integer length = (Integer)expression.getValue();
           log.info(String.valueOf(length));
   
           // The String’s constructor can be called instead of using a string literal, as the following example shows:
           expression = parser.parseExpression("new String('Hello World').toUpperCase()");
           value = (String)expression.getValue(String.class);
           log.info(value);
   
           // The more common usage of SpEL is to provide an expression string that is evaluated against a specific object instance (called the root object).
           Inventor inventor = new Inventor("binvi", new Date(), "china");
           expression = parser.parseExpression("name");
           String name = (String)expression.getValue(inventor);
           expression = parser.parseExpression("name == 'binvi'");
           boolean result = expression.getValue(inventor, Boolean.class);
           log.info(String.valueOf(result));
       }
   
   }
   
   @Data
   @AllArgsConstructor
   class Inventor {
       private String name;
       private Date birthday;
       private String nationality;
   }
   ```

   ​	4.1.1 Understanding `EvaluationContext`

   - `SimpleEvaluationContext`： Exposes a subset of essential SpEL language features and configuration options, for categories of expressions that do not require the full extent of the SpEL language syntax and should be meaningfully restricted. 

   - `StandardEvaluationContext`:  Exposes the full set of SpEL language features and configuration options. 

     ```java
     class Simple {
         public List<Boolean> booleanList = new ArrayList<Boolean>();
     }
     
     Simple simple = new Simple();
     simple.booleanList.add(true);
     
     EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
     
     // "false" is passed in here as a String. SpEL and the conversion service
     // will recognize that it needs to be a Boolean and convert it accordingly.
     parser.parseExpression("booleanList[0]").setValue(context, simple, "false");
     
     // b is false
     Boolean b = simple.booleanList.get(0);
     ```

     4.1.2 Parser Configuration

     ```java
     class Demo {
         public List<String> list;
     }
     
     // Turn on:
     // - auto null reference initialization
     // - auto collection growing
     SpelParserConfiguration config = new SpelParserConfiguration(true,true);
     
     ExpressionParser parser = new SpelExpressionParser(config);
     
     Expression expression = parser.parseExpression("list[3]");
     
     Demo demo = new Demo();
     
     Object o = expression.getValue(demo);
     
     // demo.list will now be a real collection of 4 entries
     // Each entry is a new empty String
     ```

     4.1.3 SpEL Compilation

     ​	Compiler Configuration: 

     - `org.springframework.expression.spel.SpelCompilerMode`: 

       - `OFF`(default) : The compiler is switched off.
       - `IMMEDIATE` : In immediate mode, the expressions are compiled as soon as possible. 
       - `MIXED` :  In mixed mode, the expressions silently switch between interpreted and compiled mode over time. 

     - `spring.expression.compiler.mode`: `off`, `immediate`, or `mixed`

       ```java
       SpelParserConfiguration config = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE,
           this.getClass().getClassLoader());
       
       SpelExpressionParser parser = new SpelExpressionParser(config);
       
       Expression expr = parser.parseExpression("payload");
       
       MyMessage message = new MyMessage();
       
       Object payload = expr.getValue(message);
       ```

   4.2 Expressions in Bean Definitions

   ​	4.2.1 XML Configuration

           ```xml
   <bean id="numberGuess" class="org.spring.samples.NumberGuess">
       <property name="randomNumber" value="#{ T(java.lang.Math).random() * 100.0 }"/>
   </bean>
   
   <bean id="shapeGuess" class="org.spring.samples.ShapeGuess">
       <property name="initialShapeSeed" value="#{ numberGuess.randomNumber }"/>
   </bean>
   
   <bean id="numberGuess" class="org.spring.samples.NumberGuess">
       <property name="randomNumber" value="#{ T(java.lang.Math).random() * 100.0 }"/>
   </bean>
           ```

   ​	4.2.2 Annotation Configuration

   ```java
   public class TestBean {
   
       // 1. on fields
       @Value("#{ systemProperties['user.region'] }")
       private String defaultLocale;
   
       // 2. on setter methods
       @Value("#{ systemProperties['user.region'] }")
       public void setDefaultLocale(String defaultLocale) {
           this.defaultLocale = defaultLocale;
       }
   
       public String getDefaultLocale() {
           return this.defaultLocale;
       }
       
       // 3. on @Autowired methods
       @Autowired
       public void configure(
               @Value("#{ systemProperties['user.region'] }") String defaultLocale) {
           this.defaultLocale = defaultLocale;
       }
       
       // 4. on constructors
       public TestBean(
           @Value("#{systemProperties['user.country']}") String defaultLocale) {
           this.defaultLocale = defaultLocale;
       }
   }
   ```

   4.3 Language Reference

   ```java
       /**
        * 4.3 Language Reference: describes how the Spring Expression Language works.
        */
       private void testSpELComplete() throws NoSuchMethodException {
           SpelExpressionParser parser = new SpelExpressionParser();
   
           // 4.3.1. Literal Expressions
           String helloWorld = (String) parser.parseExpression("'Hello World'").getValue();
           double avogadrosNumber = (Double) parser.parseExpression("6.0221415E+23").getValue();
           int maxValue = (Integer) parser.parseExpression("0x7FFFFFFF").getValue();
           boolean trueValue = (Boolean) parser.parseExpression("true").getValue();
           Object nullValue = parser.parseExpression("null").getValue();
   
           // 4.3.2. Properties, Arrays, Lists, Maps, and Indexers
           Inventor tesla = new Inventor("tesla", new Date(), "america");
           Inventor ieee = new Inventor("ieee", new Date(), "america");
           int year = (Integer) parser.parseExpression("Birthdate.Year + 1900").getValue(tesla);
           String city = (String) parser.parseExpression("placeOfBirth.City").getValue(tesla);
   
           EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
           // Inventions Array
           // evaluates to "Induction motor"
           String invention = parser.parseExpression("inventions[3]").getValue(context, tesla, String.class);
           // Members List
           // evaluates to "Nikola Tesla"
           String name = parser.parseExpression("Members[0].Name").getValue(context, ieee, String.class);
           // List and Array navigation
           // evaluates to "Wireless communication"
           invention = parser.parseExpression("Members[0].Inventions[6]").getValue(context, ieee, String.class);
   
           // Officer's Dictionary
           SimpleEvaluationContext societyContext = SimpleEvaluationContext.forReadOnlyDataBinding().build();
           Inventor pupin = parser.parseExpression("Officers['president']").getValue(societyContext, Inventor.class);
           city = parser.parseExpression("Officers['president'].PlaceOfBirth.City").getValue(societyContext, String.class);
           parser.parseExpression("Officers['advisors'][0].PlaceOfBirth.Country").setValue(societyContext, "Croatia");
   
           // 4.3.3. Inline Lists
           // evaluates to a Java list containing the four numbers
           List numbers = (List) parser.parseExpression("{1,2,3,4}").getValue(context);
           List listOfLists = (List) parser.parseExpression("{{'a','b'},{'x','y'}}").getValue(context);
   
           // 4.3.4. Inline Maps
           // evaluates to a Java map containing the two entries
           Map inventorInfo = (Map) parser.parseExpression("{name:'Nikola',dob:'10-July-1856'}").getValue(context);
           Map mapOfMaps = (Map) parser.parseExpression("{name:{first:'Nikola',last:'Tesla'},dob:{day:10,month:'July',year:1856}}").getValue(context);
   
           // 4.3.5. Array Construction
           int[] numbers1 = (int[]) parser.parseExpression("new int[4]").getValue(context);
           // Array with initializer
           int[] numbers2 = (int[]) parser.parseExpression("new int[]{1,2,3}").getValue(context);
           // Multi dimensional array
           int[][] numbers3 = (int[][]) parser.parseExpression("new int[4][5]").getValue(context);
   
           // 4.3.6. Methods
           // string literal, evaluates to "bc"
           String bc = parser.parseExpression("'abc'.substring(1, 3)").getValue(String.class);
           // evaluates to true
           boolean isMember = parser.parseExpression("isMember('Mihajlo Pupin')").getValue(
                   societyContext, Boolean.class);
   
           // 4.3.7. Operators
           // Relational Operators // lt(<) gt(>) le(<=) ge(>=) eq(==) ne(!=) div(/) mod(%) not(!)
           trueValue = parser.parseExpression("2 == 2").getValue(Boolean.class);
           boolean falseValue = parser.parseExpression("2 < -5.0").getValue(Boolean.class);
           trueValue = parser.parseExpression("'black' < 'block'").getValue(Boolean.class);
           falseValue = parser.parseExpression("'xyz' instanceof T(Integer)").getValue(Boolean.class);
           trueValue = parser.parseExpression("'5.00' matches '^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);
           falseValue = parser.parseExpression("'5.0067' matches '^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);
           // Logical Operators // and or not
           falseValue = parser.parseExpression("true and false").getValue(Boolean.class);
           String expression = "isMember('Nikola Tesla') and isMember('Mihajlo Pupin')";
           trueValue = parser.parseExpression(expression).getValue(societyContext, Boolean.class);
           trueValue = parser.parseExpression("true or false").getValue(Boolean.class);
           expression = "isMember('Nikola Tesla') or isMember('Albert Einstein')";
           trueValue = parser.parseExpression(expression).getValue(societyContext, Boolean.class);
           falseValue = parser.parseExpression("!true").getValue(Boolean.class);
           expression = "isMember('Nikola Tesla') and !isMember('Mihajlo Pupin')";
           falseValue = parser.parseExpression(expression).getValue(societyContext, Boolean.class);
           // Mathematical Operators
           int two = parser.parseExpression("1 + 1").getValue(Integer.class);  // 2
           String testString = parser.parseExpression("'test' + ' ' + 'string'").getValue(String.class);  // 'test string'
           int four = parser.parseExpression("1 - -3").getValue(Integer.class);  // 4
           double d = parser.parseExpression("1000.00 - 1e4").getValue(Double.class);  // -9000
           int six = parser.parseExpression("-2 * -3").getValue(Integer.class);  // 6
           double twentyFour = parser.parseExpression("2.0 * 3e0 * 4").getValue(Double.class);  // 24.0
           int minusTwo = parser.parseExpression("6 / -3").getValue(Integer.class);  // -2
           double oneD = parser.parseExpression("8.0 / 4e0 / 2").getValue(Double.class);  // 1.0
           int three = parser.parseExpression("7 % 4").getValue(Integer.class);  // 3
           int one = parser.parseExpression("8 / 5 % 2").getValue(Integer.class);  // 1
           int minusTwentyOne = parser.parseExpression("1+2-3*8").getValue(Integer.class);  // -21
           // The Assignment Operator
           Inventor inventor = new Inventor();
           context = SimpleEvaluationContext.forReadWriteDataBinding().build();
           parser.parseExpression("Name").setValue(context, inventor, "Aleksandar Seovic");
           String aleks = parser.parseExpression("Name = 'Aleksandar Seovic'").getValue(context, inventor, String.class);
   
           // 4.3.8. Types
           Class dateClass = parser.parseExpression("T(java.util.Date)").getValue(Class.class);
           Class stringClass = parser.parseExpression("T(String)").getValue(Class.class);
           trueValue = parser.parseExpression(
                   "T(java.math.RoundingMode).CEILING < T(java.math.RoundingMode).FLOOR")
                   .getValue(Boolean.class);
   
           // 4.3.9. Constructors
           Inventor einstein = parser.parseExpression(
                   "new org.spring.samples.spel.inventor.Inventor('Albert Einstein', 'German')")
                   .getValue(Inventor.class);
           parser.parseExpression(
                   "Members.add(new org.spring.samples.spel.inventor.Inventor('Albert Einstein', 'German'))")
                   .getValue(societyContext);
   
           // 4.3.10. Variables
           tesla = new Inventor("Nikola Tesla", "Serbian");
           context = SimpleEvaluationContext.forReadWriteDataBinding().build();
           context.setVariable("newName", "Mike Tesla");
           parser.parseExpression("Name = #newName").getValue(context, tesla);
           System.out.println(tesla.getName());  // "Mike Tesla"
           // The #this and #root Variables
           List<Integer> primes = new ArrayList<Integer>();
           primes.addAll(Arrays.asList(2,3,5,7,11,13,17));
           parser = new SpelExpressionParser();
           context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
           context.setVariable("primes", primes);
           List<Integer> primesGreaterThanTen = (List<Integer>) parser.parseExpression("#primes.?[#this>10]").getValue(context);
   
           // 4.3.11. Functions
           Method method = String.class.getMethod("equals", Object.class);
           context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
           context.setVariable("myFunction", method);
           parser = new SpelExpressionParser();
           context.setVariable("concat", String.class.getDeclaredMethod("concat", String.class));
           helloWorld = parser.parseExpression("#concat('hello')").getValue(context, String.class);
   
           // 4.3.12. Bean References
           parser = new SpelExpressionParser();
           StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
           standardEvaluationContext.setBeanResolver(new BeanFactoryResolver(new ClassPathXmlApplicationContext()));
           Object bean = parser.parseExpression("@something").getValue(context);
   
           // 4.3.13. Ternary Operator (If-Then-Else)
           String falseString = parser.parseExpression("false ? 'trueExp' : 'falseExp'").getValue(String.class);
           parser.parseExpression("Name").setValue(societyContext, "IEEE");
           societyContext.setVariable("queryName", "Nikola Tesla");
           expression = "isMember(#queryName)? #queryName + ' is a member of the ' " +
                   "+ Name + ' Society' : #queryName + ' is not a member of the ' + Name + ' Society'";
           String queryResultString = parser.parseExpression(expression).getValue(societyContext, String.class);
   
           // 4.3.14. The Elvis Operator
           name = "Elvis Presley";
           String displayName = (name != null ? name : "Unknown");
           parser = new SpelExpressionParser();
           name = parser.parseExpression("name?:'Unknown'").getValue(String.class);
           System.out.println(name);  // 'Unknown'
   
           parser = new SpelExpressionParser();
           context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
           tesla = new Inventor("Nikola Tesla", "Serbian");
           name = parser.parseExpression("Name?:'Elvis Presley'").getValue(context, tesla, String.class);
           System.out.println(name);  // Nikola Tesla
           tesla.setName(null);
           name = parser.parseExpression("Name?:'Elvis Presley'").getValue(context, tesla, String.class);
           System.out.println(name);  // Elvis Presley
   
           // 4.3.15. Safe Navigation Operator
           parser = new SpelExpressionParser();
           context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
           tesla = new Inventor("Nikola Tesla", "Serbian");
           tesla.setPlaceOfBirth(new PlaceOfBirth("Smiljan"));
           city = parser.parseExpression("PlaceOfBirth?.City").getValue(context, tesla, String.class);
           System.out.println(city);  // Smiljan
           tesla.setPlaceOfBirth(null);
           city = parser.parseExpression("PlaceOfBirth?.City").getValue(context, tesla, String.class);
           System.out.println(city);  // null - does not throw NullPointerException!!!
   
           // 4.3.16. Collection Selection
           List<Inventor> list = (List<Inventor>) parser.parseExpression("Members.?[Nationality == 'Serbian']").getValue(societyContext);
           Map newMap = (Map) parser.parseExpression("map.?[value<27]").getValue();
   
           // 4.3.17. Collection Projection
           List placesOfBirth = (List)parser.parseExpression("Members.![placeOfBirth.city]");
   
           // 4.3.18. Expression templating
           String randomPhrase = parser.parseExpression(
                   "random number is #{T(java.lang.Math).random()}",
                   new TemplateParserContext()).getValue(String.class);
       }
   ```

   4.4 Classes Used in the Examples

   ```java
   public class Inventor {
   
       private String name;
       private String nationality;
       private String[] inventions;
       private Date birthdate;
       private PlaceOfBirth placeOfBirth;
   
       public Inventor(String name, String nationality) {
           GregorianCalendar c= new GregorianCalendar();
           this.name = name;
           this.nationality = nationality;
           this.birthdate = c.getTime();
       }
   
       public Inventor(String name, Date birthdate, String nationality) {
           this.name = name;
           this.nationality = nationality;
           this.birthdate = birthdate;
       }
   
       public Inventor() {
       }
   
       public String getName() {
           return name;
       }
   
       public void setName(String name) {
           this.name = name;
       }
   
       public String getNationality() {
           return nationality;
       }
   
       public void setNationality(String nationality) {
           this.nationality = nationality;
       }
   
       public Date getBirthdate() {
           return birthdate;
       }
   
       public void setBirthdate(Date birthdate) {
           this.birthdate = birthdate;
       }
   
       public PlaceOfBirth getPlaceOfBirth() {
           return placeOfBirth;
       }
   
       public void setPlaceOfBirth(PlaceOfBirth placeOfBirth) {
           this.placeOfBirth = placeOfBirth;
       }
   
       public void setInventions(String[] inventions) {
           this.inventions = inventions;
       }
   
       public String[] getInventions() {
           return inventions;
       }
   }
   ```

   ```java
   public class PlaceOfBirth {
   
       private String city;
       private String country;
   
       public PlaceOfBirth(String city) {
           this.city=city;
       }
   
       public PlaceOfBirth(String city, String country) {
           this(city);
           this.country = country;
       }
   
       public String getCity() {
           return city;
       }
   
       public void setCity(String s) {
           this.city = s;
       }
   
       public String getCountry() {
           return country;
       }
   
       public void setCountry(String country) {
           this.country = country;
       }
   }
   ```

   ```java
   public class Society {
   
       private String name;
   
       public static String Advisors = "advisors";
       public static String President = "president";
   
       private List<Inventor> members = new ArrayList<Inventor>();
       private Map officers = new HashMap();
   
       public List getMembers() {
           return members;
       }
   
       public Map getOfficers() {
           return officers;
       }
   
       public String getName() {
           return name;
       }
   
       public void setName(String name) {
           this.name = name;
       }
   
       public boolean isMember(String name) {
           for (Inventor inventor : members) {
               if (inventor.getName().equals(name)) {
                   return true;
               }
           }
           return false;
       }
   }
   ```

5. Aspect Oriented Programming with Spring

6. Spring AOP APIs

7. Null-safety

8. Data Buffers And Codes

9. Appendix