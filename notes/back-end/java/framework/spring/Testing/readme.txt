Testing 			https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#testing

	TDD: test-driven development

1. Introduction to Spring Testing

2. Unit Testing

	2.1. Mock Objects

		Spring提供了许多专门用于模拟测试的package，如下：

		2.1.1. Environment

			org.springframework.mock.env包含了Environment和PropertySource抽象类的模拟实现： MockEnvironment、MockPropertySource。

		2.1.2. JNDI	

			org.springframework.mock.jndi包含了一组JNDI SPI的实现，可以用于测试程序组或者单独应用时设置简单的JNDI环境。
			比如，JDBC的DataSource实例可以像在JavaEE容器中那样在测试中获取同样的JNDI名称，无需修改即可以重复使用测试场景中的应用代码和配置。

		2.1.3. Servlet API
		
			org.springframework.mock.web包含了非常全面的Servlet API模拟对象的集合，可以用于测试web contexts, controllers, and filters.
			这些模拟对象旨在用于Spring-Web MVC框架中，通常比使用动态模拟对象（如EasyMock）或者改良版的Servlet API模拟对象（如MockObjects）更方便。

		2.1.4. Spring Web Reactive
		
			org.springframework.mock.http.server.reactive包含用于WebFlux应用程序的ServerHttpRequest和ServerHttpResponse的模拟实现。	
			org.springframework.mock.web.server包含模拟器ServerWebExchange（依赖上边的请求/响应模拟对象）

	2.2. Unit Testing Support Classes
	
		2.2.1. General Testing Utilities		

			org.springframework.test.util包含一些通用组件。

			ReflectionTestUtils 包含基于反射的通用方法。可以使用这些方法如：需要修改常量值，设置非public的字段，调用非public的set方法，调用非public的生命周期回调，测试场景如：

				ORM框架（如JPA/Hibernate）允许实体域中的属性的set方法是public，而获取方法时private/protected的。

				Spring的注解（如@Autowired、@Inject、@Resource），为private/protected修饰的字段、setter方法、配置方法提供依赖注入。

				生命周期方法中的一些注解如PostConstruct和@PreDestroy。

			AopTestUtils 包含AOP相关的通用方法。 可以通过这些方法来获取Spring代理的底层目标对象引用。	

				Spring AOP中的工具类有： AopUtils, AopProxyUtils

		2.2.2. Spring MVC Testing Utilities
		
			org.springframework.test.web包含ModelAndViewAssert, 可以结合其他测试框架（如JUnit、TestNG）处理SpringMVC中的ModelAndView对象。

			SpringMVC中的controller类作为POJO测试时，使用ModelAndViewAssert，结合MockHttpServletRequest、MockHttpSession以及 Spring API mocks中的相关类。

			当需要全面测试带有容器配WebApplicationContext的SpringMVC以及REST风格的controller时，使用Spring MVC Test框架。

3. Integration Testing

	3.1. Overview

		集成测试时，无需部署应用服务器或者连接企业级基础设施是非常重要的。这样我们就可以测试，比如：

			- Spring IOC容器上下文的注入正确性
			- 使用JDBC或者ORM工具获取数据。 其中包括SQL语句的正确性、Hibernate查询、JPA实体映射等等

		spring-test package: 包含了各种Spring容器的继承测试类，这些测试不依赖应用服务器或者其他部署环境。测试运行的虽然比单元测试要慢些，但是比依赖于应用服务器部署的测试要快。

	3.2. Goals of Integration Testing
	
		3.2.1. Context Management and Caching

		3.2.2. Dependency Injection of Test Fixtures

		3.2.3. Transaction Management

		3.2.4. Support Classes for Integration Testing

	3.3. JDBC Testing Support		

		org.springframework.test.jdbc包含JdbcTestUtils工具类，有许多JDBC相关的通用方法，可以用于简单的标准数据库测试场景：

	// done 2020-3-10 19:42:52	

	3.4. Annotations

		3.4.1. Spring Testing Annotations	

			@BootstrapWith

			@ContextConfiguration

			@WebAppConfiguration

			@ContextHierarchy

			@ActiveProfiles

			@TestPropertySource

			@DirtiesContext

			@TestExecutionListeners

			@Commit

			@Rollback

			@BeforeTransaction

			@AfterTransaction

			@Sql

			@SqlConfig

			@SqlMergeMode

			@SqlGroup	

		3.4.2. Standard Annotation Support

			@Autowired

			@Qualifier

			@Value

			@Resource (javax.annotation) if JSR-250 is present

			@ManagedBean (javax.annotation) if JSR-250 is present

			@Inject (javax.inject) if JSR-330 is present

			@Named (javax.inject) if JSR-330 is present

			@PersistenceContext (javax.persistence) if JPA is present

			@PersistenceUnit (javax.persistence) if JPA is present

			@Required

			@Transactional (org.springframework.transaction.annotation) with limited attribute support

		3.4.3. Spring JUnit 4 Testing Annotations
		
			@IfProfileValue

			@ProfileValueSourceConfiguration

			@Timed

			@Repeat	

		3.4.4. Spring JUnit Jupiter Testing Annotations
		
			@SpringJUnitConfig

			@SpringJUnitWebConfig

			@TestConstructor

			@EnabledIf

			@DisabledIf	

		3.4.5. Meta-Annotation Support for Testing
		
			@BootstrapWith

			@ContextConfiguration

			@ContextHierarchy

			@ActiveProfiles

			@TestPropertySource

			@DirtiesContext

			@WebAppConfiguration

			@TestExecutionListeners

			@Transactional

			@BeforeTransaction

			@AfterTransaction

			@Commit

			@Rollback

			@Sql

			@SqlConfig

			@SqlMergeMode

			@SqlGroup

			@Repeat (only supported on JUnit 4)

			@Timed (only supported on JUnit 4)

			@IfProfileValue (only supported on JUnit 4)

			@ProfileValueSourceConfiguration (only supported on JUnit 4)

			@SpringJUnitConfig (only supported on JUnit Jupiter)

			@SpringJUnitWebConfig (only supported on JUnit Jupiter)

			@TestConstructor (only supported on JUnit Jupiter)

			@EnabledIf (only supported on JUnit Jupiter)

			@DisabledIf (only supported on JUnit Jupiter)	

	3.5. Spring TestContext Framework
	
		3.5.1. Key Abstractions

		3.5.2. Bootstrapping the TestContext Framework

		3.5.3. TestExecutionListener Configuration

			Registering TestExecutionListener Implementations

			Automatic Discovery of Default TestExecutionListener Implementations

			Ordering TestExecutionListener Implementations

			Merging TestExecutionListener Implementations

		3.5.4. Test Execution Events	

			Exception Handling

			Asynchronous Listeners

		// done 2020-3-11 12:18:46	

		3.5.5. Context Management
