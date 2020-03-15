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

			@BootstrapWith: 

				类注解，可以用来配置如何引导Spring TestContext Framework，可以使用@BootStrapWith来指定一个自定义的TestContextBootstrapper。

			@ContextConfiguration: 

				定义类级别的元注解，集成测试中可以用于决定如何加载和配置ApplicationContext，注解支持声明上下文加载用的资源路径和组件类：
					资源路径是指典型的类路径下的xml配置文件/Groovy脚本，也支持文件系统中的文件/脚本。
					组件类是指典型的@Configuration类，@Component、@Service也支持

				i.e:

					java:

						-------------------- 资源路径 --------------------

						@ContextConfiguration("/test-config.xml") 
						class XmlApplicationContextTests {
						    // class body...
						}

						-------------------- 配置类 --------------------

						@ContextConfiguration(classes = TestConfig.class) 
						class ConfigClassApplicationContextTests {
						    // class body...
						}

						-------------------- 声明上下文初始化类ApplicationContextInitializer --------------------

						@ContextConfiguration(initializers = CustomContextIntializer.class) 
						class ContextInitializerTests {
						    // class body...
						}

						-------------------- 申明上下文加载策略 --------------------
						@ContextConfiguration(locations = "/test-context.xml", loader = CustomContextLoader.class) 
						class CustomLoaderXmlApplicationContextTests {
						    // class body...
						}

			@WebAppConfiguration: 

				类注解，集成测试中可用于申明其加载的ApplicationContext为WebApplicationContext。 必须与@ContextConfiguration一起使用才会生效。

				i.e:

					java:

						@ContextConfiguration
						@WebAppConfiguration 
						class WebAppTests {
						    // class body...
						}

						-------------------- 指定类路径支援 --------------------

						@ContextConfiguration
						@WebAppConfiguration("classpath:test-web-resources") 
						class WebAppTests {
						    // class body...
						}

			@ContextHierarchy:

				类注解，用来定义集成测试中ApplicationContext实例的继承关系。

				i.e:

					@ContextHierarchy({
					    @ContextConfiguration("/parent-config.xml"),
					    @ContextConfiguration("/child-config.xml")
					})
					class ContextHierarchyTests {
					    // class body...
					}

					@WebAppConfiguration
					@ContextHierarchy({
					    @ContextConfiguration(classes = AppConfig.class),
					    @ContextConfiguration(classes = WebConfig.class)
					})
					class WebIntegrationTests {
					    // class body...
					}


			@ActiveProfiles:

				类注解，用来声明集成测试中ApplicationContext加载时激活使用的bean定义配置。可继承父类上的注解信息。
				可以通过实现ActiveProfilesResolver+@ActiveProfiles(resolver)来替代

				i.e:

					java:

						-------------------- dev(profile)被激活使用 --------------------

						@ContextConfiguration
						@ActiveProfiles("dev") 
						class DeveloperTests {
						    // class body...
						}

						-------------------- dev, integration(profile)被激活使用 --------------------

						@ContextConfiguration
						@ActiveProfiles({"dev", "integration"}) 
						class DeveloperIntegrationTests {
						    // class body...
						}

			@TestPropertySource

				类注解，用来配置属性文件的路径，在集成测试中可以添加内部属性到ApplicationContext.Environment.PropertySources中。
				优先级高于操作系统的环境变量、Java系统属性、@PropertySource/编码方式添加的属性源。

				i.e:

					-------------------- 声明类路径下的资源文件 --------------------

					@ContextConfiguration
					@TestPropertySource("/test.properties") 
					class MyIntegrationTests {
					    // class body...
					}

					-------------------- 声明内部属性 --------------------

					@ContextConfiguration
					@TestPropertySource(properties = { "timezone = GMT", "port: 4242" }) 
					class MyIntegrationTests {
					    // class body...
					}


			@DirtiesContext

				表明底层Spring的ApplicationContext在测试执行期间已污染，需要被关闭。 当一个applicationContext被标记为dirty，他将从测试中的框架缓存中移除并关闭。
				这就导致，其他需要同样配置上下文的测试/测试子类中的底层Spring容器会重新创建。

				可以用在类/方法上。 被注解的ApplicationContext是在方法/类执行前还是执行后执行污染标记，取决于配置的属性methodMode/classMode.

				i.e:

					java:

						-------------------- 类注解 + BEFORE_CLASS: 测试类前标记污染 --------------------

						@DirtiesContext(classMode = BEFORE_CLASS) 
						class FreshContextTests {
						    // some tests that require a new Spring container
						}

						-------------------- 类注解 + AFTER_CLASS（默认）: 测试类后标记污染 --------------------

						@DirtiesContext 
						class ContextDirtyingTests {
						    // some tests that result in the Spring container being dirtied
						}

						-------------------- 类注解 + BEFORE_EACH_TEST_METHOD： 每个测试方法前标记污染 --------------------

						@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD) 
						class FreshContextTests {
						    // some tests that require a new Spring container
						}

						-------------------- 类注解 + AFTER_EACH_TEST_METHOD： 每个测试方法后标记污染 --------------------

						@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD) 
						class ContextDirtyingTests {
						    // some tests that result in the Spring container being dirtied
						}

						-------------------- 方法注解 + BEFORE_METHOD: 测试方法前标记污染 --------------------

						@DirtiesContext(methodMode = BEFORE_METHOD) 
						@Test
						void testProcessWhichRequiresFreshAppCtx() {
						    // some logic that requires a new Spring container
						}

						-------------------- 方法注解 + AFTER_METHOD（默认）: 测试方法后标记污染 --------------------

						@DirtiesContext 
						@Test
						void testProcessWhichDirtiesAppCtx() {
						    // some logic that results in the Spring container being dirtied
						}

						-------------------- 上下文继承，参考DirtiesContext.HierarchyMode（EXHAUSTIVE: 所有类、子类都会标记；CURRENT_LEVEL: 只有当前层级会被标记） -------------

						@ContextHierarchy({
						    @ContextConfiguration("/parent-config.xml"),
						    @ContextConfiguration("/child-config.xml")
						})
						class BaseTests {
						    // class body...
						}

						class ExtendedTests extends BaseTests {

						    @Test
						    @DirtiesContext(hierarchyMode = CURRENT_LEVEL) 
						    void test() {
						        // some logic that results in the child context being dirtied
						    }
						}

			@TestExecutionListeners

				类上的元注解，配置需要通过TestContextManager进行注册的TestExecutionListener实现类。通常与@ContextConfiguration搭配使用。

				i.e:

					@ContextConfiguration
					@TestExecutionListeners({CustomTestExecutionListener.class, AnotherTestExecutionListener.class}) 
					class CustomTestExecutionListenerTests {
					    // class body...
					}

			@Commit

				表明支持事务的测试方法在执行完成之后进行事务提交。 支持注解类/方法。

				i.e:

					java:

						-------------------- 提交测试结果到数据库 --------------------

						@Commit 
						@Test
						void testProcessWithoutRollback() {
						    // ...
						}

			@Rollback

				表明支持事务的测试方法在执行完成后是否回滚事务，如果为true，则事务回滚，否则，则事务提交。Spring Context Framework的集成测试中，默认为true（即时不主动声明）。

				i.e:

					java:

						@Rollback(false) 
						@Test
						void testProcessWithoutRollback() {
						    // ...
						}

			@BeforeTransaction

				在@Transaction注解的事务中，表明该注解的方法会在事务开始前执行。修饰的方法无需是public的，也可以是Java8中接口的default实现。

				i.e:

					java:

						@BeforeTransaction 
						void beforeTransaction() {
						    // logic to be executed before a transaction is started
						}

			@AfterTransaction

				在@Transaction注解的事务中，表明该注解的方法会在事务结束后执行。修饰的方法无需是public的，也可以是Java8中接口的default实现。

				i.e:

					java:

						@AfterTransaction 
						void afterTransaction() {
						    // logic to be executed after a transaction has ended
						}

			@Sql

				注解类/方法，配置集成测试中需要在指定数据库上执行的SQL脚本。

				i.e:

					java:

						-------------------- Run two scripts for this test. --------------------

						@Test
						@Sql({"/test-schema.sql", "/test-user-data.sql"}) 
						void userTest() {
						    // execute code that relies on the test schema and test data
						}

			@SqlConfig

				如何解析并执行@Sql注解配置的SQL脚本

				i.e:

					java:

						@Test
						@Sql(
						    scripts = "/test-user-data.sql",
						    config = @SqlConfig(commentPrefix = "`", separator = "@@") 
						)
						void userTest() {
						    // execute code that relies on the test data
						}

			@SqlMergeMode

				用来声明是否合并方法级别的@Sql脚本和类级别的@Sql脚本。 如果无声明，默认为OVERRIDE，方法上的@Sql会覆盖类上的@Sql。

				i.e:

					-------------------- 类中所有方法都设置为MERGE模式 --------------------

					@SpringJUnitConfig(TestConfig.class)
					@Sql("/test-schema.sql")
					@SqlMergeMode(MERGE) 
					class UserTests {

					    @Test
					    @Sql("/user-test-data-001.sql")
					    void standardUserProfile() {
					        // execute code that relies on test data set 001
					    }
					}

					-------------------- 类中指定方法设置为MERGE模式 --------------------

					@SpringJUnitConfig(TestConfig.class)
					@Sql("/test-schema.sql")
					class UserTests {

					    @Test
					    @Sql("/user-test-data-001.sql")
					    @SqlMergeMode(MERGE) 
					    void standardUserProfile() {
					        // execute code that relies on test data set 001
					    }
					}

			@SqlGroup	

				容器注解，汇总其他的@Sql注解（分组）。

				i.e:

					-------------------- 声明一组SQL脚本 --------------------

					@Test
					@SqlGroup({ 
					    @Sql(scripts = "/test-schema.sql", config = @SqlConfig(commentPrefix = "`")),
					    @Sql("/test-user-data.sql")
					)}
					void userTest() {
					    // execute code that uses the test schema and test data
					}

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

			Context Configuration with XML resources

			Context Configuration with Groovy Scripts

			Context Configuration with Component Classes

			Mixing XML, Groovy Scripts, and Component Classes

			Context Configuration with Context Initializers

			Context Configuration Inheritance

			Context Configuration with Environment Profiles

			Context Configuration with Test Property Sources

			Loading a WebApplicationContext

			Context Caching

			Context Hierarchies

		// done 2020-3-12 10:31:53

		3.5.6. Dependency Injection of Test Fixtures	

			监听器： DependencyInjectionTestExecutionListener 

			java:

				@ExtendWith(SpringExtension.class)
				// specifies the Spring configuration to load for this test fixture
				@ContextConfiguration("repository-config.xml")
				class HibernateTitleRepositoryTests {

				    // this instance will be dependency injected by type
				    @Autowired
				    HibernateTitleRepository titleRepository;

				    @Test
				    void findById() {
				        Title title = titleRepository.findById(new Long(10));
				        assertNotNull(title);
				    }
				}

				---------------------------------------

				@ExtendWith(SpringExtension.class)
				// specifies the Spring configuration to load for this test fixture
				@ContextConfiguration("repository-config.xml")
				class HibernateTitleRepositoryTests {

				    // this instance will be dependency injected by type
				    HibernateTitleRepository titleRepository;

				    @Autowired
				    void setTitleRepository(HibernateTitleRepository titleRepository) {
				        this.titleRepository = titleRepository;
				    }

				    @Test
				    void findById() {
				        Title title = titleRepository.findById(new Long(10));
				        assertNotNull(title);
				    }
				}

				---------------------------------------

				// ...

				    @Autowired
				    @Override
				    public void setDataSource(@Qualifier("myDataSource") DataSource dataSource) {
				        super.setDataSource(dataSource);
				    }

				// ...

			xml:
			
				<?xml version="1.0" encoding="UTF-8"?>
				<beans xmlns="http://www.springframework.org/schema/beans"
				    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				    xsi:schemaLocation="http://www.springframework.org/schema/beans
				        https://www.springframework.org/schema/beans/spring-beans.xsd">

				    <!-- this bean will be injected into the HibernateTitleRepositoryTests class -->
				    <bean id="titleRepository" class="com.foo.repository.hibernate.HibernateTitleRepository">
				        <property name="sessionFactory" ref="sessionFactory"/>
				    </bean>

				    <bean id="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
				        <!-- configuration elided for brevity -->
				    </bean>

				</beans>	

		3.5.7. Testing Request- and Session-scoped Beans

			测试request-scoped and session-scoped beans步骤：

				// 确保test类已通过添加注解@WebAppConfiguration加载WebAppConfiguration
				Ensure that a WebApplicationContext is loaded for your test by annotating your test class with @WebAppConfiguration.

				// test实例中注入request/session模拟对象，并准备相应的配置信息
				Inject the mock request or session into your test instance and prepare your test fixture as appropriate.

				// 调用web组件
				Invoke your web component that you retrieved from the configured WebApplicationContext (with dependency injection).

				// 对模拟对象执行断言
				Perform assertions against the mocks.

			xml:

				<!-- request scoped -->			
				<beans>

				    <bean id="userService" class="com.example.SimpleUserService"
				            c:loginAction-ref="loginAction"/>

				    <bean id="loginAction" class="com.example.LoginAction"
				            c:username="#{request.getParameter('user')}"
				            c:password="#{request.getParameter('pswd')}"
				            scope="request">
				        <aop:scoped-proxy/>
				    </bean>

				</beans>

				-------------------------------------------

				<!-- session scoped -->			
				<beans>

				    <bean id="userService" class="com.example.SimpleUserService"
				            c:userPreferences-ref="userPreferences" />

				    <bean id="userPreferences" class="com.example.UserPreferences"
				            c:theme="#{session.getAttribute('theme')}"
				            scope="session">
				        <aop:scoped-proxy/>
				    </bean>

				</beans>		

			java:
			
				// request scoped
				@SpringJUnitWebConfig
				class RequestScopedBeanTests {

				    @Autowired UserService userService;
				    @Autowired MockHttpServletRequest request;

				    @Test
				    void requestScope() {
				        request.setParameter("user", "enigma");
				        request.setParameter("pswd", "$pr!ng");

				        LoginResults results = userService.loginUser();
				        // assert results
				    }
				}

				// session scoped
				@SpringJUnitWebConfig
				class SessionScopedBeanTests {

				    @Autowired UserService userService;
				    @Autowired MockHttpSession session;

				    @Test
				    void sessionScope() throws Exception {
				        session.setAttribute("theme", "blue");

				        Results results = userService.processUserPreferences();
				        // assert results
				    }
				}

		3.5.8. Transaction Management
		
			监听器： TransactionalTestExecutionListener		

			Test-managed Transactions

			Enabling and Disabling Transactions

				java:

					@SpringJUnitConfig(TestConfig.class)
					@Transactional
					class HibernateUserRepositoryTests {

					    @Autowired
					    HibernateUserRepository repository;

					    @Autowired
					    SessionFactory sessionFactory;

					    JdbcTemplate jdbcTemplate;

					    @Autowired
					    void setDataSource(DataSource dataSource) {
					        this.jdbcTemplate = new JdbcTemplate(dataSource);
					    }

					    @Test
					    void createUser() {
					        // track initial state in test database:
					        final int count = countRowsInTable("user");

					        User user = new User(...);
					        repository.save(user);

					        // Manual flush is required to avoid false positive in test
					        sessionFactory.getCurrentSession().flush();
					        assertNumUsers(count + 1);
					    }

					    private int countRowsInTable(String tableName) {
					        return JdbcTestUtils.countRowsInTable(this.jdbcTemplate, tableName);
					    }

					    private void assertNumUsers(int expected) {
					        assertEquals("Number of rows in the [user] table.", expected, countRowsInTable("user"));
					    }
					}

			Transaction Rollback and Commit Behavior		

				默认情况下，测试中的事务在测试完成时会自动回滚。但是可以通过@Commit、@Rollback来执行提交/回滚。

			Programmatic Transaction Management
			
				java:

					@ContextConfiguration(classes = TestConfig.class)
					public class ProgrammaticTransactionManagementTests extends
					        AbstractTransactionalJUnit4SpringContextTests {

					    @Test
					    public void transactionalTest() {
					        // assert initial state in test database:
					        assertNumUsers(2);

					        deleteFromTables("user");

					        // changes to the database will be committed!
					        TestTransaction.flagForCommit();
					        TestTransaction.end();
					        assertFalse(TestTransaction.isActive());
					        assertNumUsers(0);

					        TestTransaction.start();
					        // perform other actions against the database that will
					        // be automatically rolled back after the test completes...
					    }

					    protected void assertNumUsers(int expected) {
					        assertEquals("Number of rows in the [user] table.", expected, countRowsInTable("user"));
					    }
					}	

			Running Code Outside of a Transaction

				@BeforeTransaction | @AfterTransaction
			
			Configuring a Transaction Manager	

				如果有多个PlatformTransactionManager，可以通过以下方式进行区分添加：

					@Transactional("myTxMgr")

					@Transactional(transactionManager = "myTxMgr")

					@Configuration注解的TransactionManagementConfigurer实现类

			Demonstration of All Transaction-related Annotations
			
				java:

					@SpringJUnitConfig
					@Transactional(transactionManager = "txMgr")
					@Commit
					class FictitiousTransactionalTest {

					    @BeforeTransaction
					    void verifyInitialDatabaseState() {
					        // logic to verify the initial state before a transaction is started
					    }

					    @BeforeEach
					    void setUpTestDataWithinTransaction() {
					        // set up test data within the transaction
					    }

					    @Test
					    // overrides the class-level @Commit setting
					    @Rollback
					    void modifyDatabaseWithinTransaction() {
					        // logic which uses the test data and modifies database state
					    }

					    @AfterEach
					    void tearDownWithinTransaction() {
					        // execute "tear down" logic within the transaction
					    }

					    @AfterTransaction
					    void verifyFinalDatabaseState() {
					        // logic to verify the final state after transaction has rolled back
					    }

					}	

					-------------------------------------------	Hibernate

					// ...

					@Autowired
					SessionFactory sessionFactory;

					@Transactional
					@Test // no expected exception!
					public void falsePositive() {
					    updateEntityInHibernateSession();
					    // False positive: an exception will be thrown once the Hibernate
					    // Session is finally flushed (i.e., in production code)
					}

					@Transactional
					@Test(expected = ...)
					public void updateWithSessionFlush() {
					    updateEntityInHibernateSession();
					    // Manual flush is required to avoid false positive in test
					    sessionFactory.getCurrentSession().flush();
					}

					// ...	

					-------------------------------------------	Jpa

					// ...

					@PersistenceContext
					EntityManager entityManager;

					@Transactional
					@Test // no expected exception!
					public void falsePositive() {
					    updateEntityInJpaPersistenceContext();
					    // False positive: an exception will be thrown once the JPA
					    // EntityManager is finally flushed (i.e., in production code)
					}

					@Transactional
					@Test(expected = ...)
					public void updateWithEntityManagerFlush() {
					    updateEntityInJpaPersistenceContext();
					    // Manual flush is required to avoid false positive in test
					    entityManager.flush();
					}

					// ...	

		3.5.9. Executing SQL Scripts

			Executing SQL scripts programmatically

				Spring提供的，可以在集成测试的编码中执行SQL脚本的类：

					org.springframework.jdbc.datasource.init.ScriptUtils

					org.springframework.jdbc.datasource.init.ResourceDatabasePopulator

					org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests

					org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests

				java:
				
					@Test
					void databaseTest() {
					    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
					    populator.addScripts(
					            new ClassPathResource("test-schema.sql"),
					            new ClassPathResource("test-data.sql"));
					    populator.setSeparator("@@");
					    populator.execute(this.dataSource);
					    // execute code that uses the test schema and data
					}	

			Executing SQL scripts declaratively with @Sql

				注解： @Sql
			
				监听器： SqlScriptsTestExecutionListener（默认支持）		

				Path Resource Semantics

					java:

						@SpringJUnitConfig
						@Sql("/test-schema.sql")
						class DatabaseTests {

						    @Test
						    void emptySchemaTest() {
						        // execute code that uses the test schema without any test data
						    }

						    @Test
						    @Sql({"/test-schema.sql", "/test-user-data.sql"})
						    void userTest() {
						        // execute code that uses the test schema and test data
						    }
						}

				Default Script Detection
				
					- 类上添加的注解： 如果类名为com.example.MyTest，默认检测的脚本为： classpath:com/example/MyTest.sql.
					- 方法上添加的注解： 如果类名为com.example.MyTest，方法名为testMethod()，默认检测的脚本为： classpath:com/example/MyTest.testMethod.sql.

				Declaring Multiple @Sql Sets
				
					java8:

						@Test
						@Sql(scripts = "/test-schema.sql", config = @SqlConfig(commentPrefix = "`"))
						@Sql("/test-user-data.sql")
						void userTest() {
						    // execute code that uses the test schema and test data
						}

					java:
					
						@Test
						@SqlGroup({
						    @Sql(scripts = "/test-schema.sql", config = @SqlConfig(commentPrefix = "`")),
						    @Sql("/test-user-data.sql")
						)}
						void userTest() {
						    // execute code that uses the test schema and test data
						}

				Script Execution Phases
				
					默认情况下，SQL脚本是在相关方法执行前执行。如果想改变默认行为，可以使用@Sql中的executionPhase属性。

						java:

							@Test
							@Sql(
							    scripts = "create-test-data.sql",
							    config = @SqlConfig(transactionMode = ISOLATED)
							)
							@Sql(
							    scripts = "delete-test-data.sql",
							    config = @SqlConfig(transactionMode = ISOLATED),
							    executionPhase = AFTER_TEST_METHOD
							)
							void userTest() {
							    // execute code that needs the test data to be committed
							    // to the database outside of the test's transaction
							}	

				Script Configuration with @SqlConfig
				
					Transaction management for @Sql

						java:

							@SpringJUnitConfig(TestDatabaseConfig.class)
							@Transactional
							class TransactionalSqlScriptsTests {

							    final JdbcTemplate jdbcTemplate;

							    @Autowired
							    TransactionalSqlScriptsTests(DataSource dataSource) {
							        this.jdbcTemplate = new JdbcTemplate(dataSource);
							    }

							    @Test
							    @Sql("/test-data.sql")
							    void usersTest() {
							        // verify state in test database:
							        assertNumUsers(2);
							        // execute code that uses the test data...
							    }

							    int countRowsInTable(String tableName) {
							        return JdbcTestUtils.countRowsInTable(this.jdbcTemplate, tableName);
							    }

							    void assertNumUsers(int expected) {
							        assertEquals(expected, countRowsInTable("user"),
							            "Number of rows in the [user] table.");
							    }
							}	

				Merging and Overriding Configuration with @SqlMergeMode							

		// done 2020-3-12 12:11:52		

		3.5.10. Parallel Test Execution

		3.5.11. TestContext Framework Support Classes

			Spring JUnit 4 Runner

				Spring TestContext Framework通过自定义runner提供了JUnit4的完全支持，只要测试类添加了注解@RunWith(SpringJUnit4ClassRunner.class)或@RunWith(SpringRunner.class)，
				开发者就可以使用标准的基于JUnit4的单元、集成测试、同时可以使用TestContext框架，比如加载application contexts, 测试实例的依赖注入，测试方法执行事务等等。

				java:

					@RunWith(SpringRunner.class)
					@TestExecutionListeners({})
					public class SimpleTest {

					    @Test
					    public void testMethod() {
					        // execute test logic...
					    }
					}

			Spring JUnit 4 Rules

				package: org.springframework.test.context.junit4.rules

				rules: SpringClassRule, SpringMethodRule	

				java:

					// Optionally specify a non-Spring Runner via @RunWith(...)
					@ContextConfiguration
					public class IntegrationTest {

					    @ClassRule
					    public static final SpringClassRule springClassRule = new SpringClassRule();

					    @Rule
					    public final SpringMethodRule springMethodRule = new SpringMethodRule();

					    @Test
					    public void testMethod() {
					        // execute test logic...
					    }
					}	

			JUnit 4 Support Classes
			
				package: org.springframework.test.context.junit4

				classes: AbstractJUnit4SpringContextTests, AbstractTransactionalJUnit4SpringContextTests	

			SpringExtension for JUnit Jupiter
			
				相关注解: @ExtendWith(SpringExtension.class)	, @SpringJUnitConfig, @SpringJUnitWebConfig 

				java:

					// Instructs JUnit Jupiter to extend the test with Spring support.
					@ExtendWith(SpringExtension.class)
					// Instructs Spring to load an ApplicationContext from TestConfig.class
					@ContextConfiguration(classes = TestConfig.class)
					class SimpleTests {

					    @Test
					    void testMethod() {
					        // execute test logic...
					    }
					}	

					--------------------------------------

					// Instructs Spring to register the SpringExtension with JUnit
					// Jupiter and load an ApplicationContext from TestConfig.class
					@SpringJUnitConfig(TestConfig.class)
					class SimpleTests {

					    @Test
					    void testMethod() {
					        // execute test logic...
					    }
					}

					--------------------------------------

					// Instructs Spring to register the SpringExtension with JUnit
					// Jupiter and load a WebApplicationContext from TestWebConfig.class
					@SpringJUnitWebConfig(TestWebConfig.class)
					class SimpleWebTests {

					    @Test
					    void testMethod() {
					        // execute test logic...
					    }
					}

			Dependency Injection with SpringExtension
			
				Constructor Injection		

					java:

						@SpringJUnitConfig(TestConfig.class)
						class OrderServiceIntegrationTests {

						    private final OrderService orderService;

						    @Autowired
						    OrderServiceIntegrationTests(OrderService orderService) {
						        this.orderService = orderService;
						    }

						    // tests that use the injected OrderService
						}

						-------------------------------------

						// if spring.test.constructor.autowire.mode = all

						@SpringJUnitConfig(TestConfig.class)
						class OrderServiceIntegrationTests {

						    private final OrderService orderService;

						    OrderServiceIntegrationTests(OrderService orderService) {
						        this.orderService = orderService;
						    }

						    // tests that use the injected OrderService
						}

				Method Injection
				
					java:

						@SpringJUnitConfig(TestConfig.class)
						class OrderServiceIntegrationTests {

						    @Test
						    void deleteOrder(@Autowired OrderService orderService) {
						        // use orderService from the test's ApplicationContext
						    }
						}	

						-------------------------------------

						@SpringJUnitConfig(TestConfig.class)
						class OrderServiceIntegrationTests {

						    @RepeatedTest(10)
						    void placeOrderRepeatedly(RepetitionInfo repetitionInfo,
						            @Autowired OrderService orderService) {

						        // use orderService from the test's ApplicationContext
						        // and repetitionInfo from JUnit Jupiter
						    }
						}

			TestNG Support Classes
			
				package: org.springframework.test.context.testng

				classes: AbstractTestNGSpringContextTests, AbstractTransactionalTestNGSpringContextTests		

		// done 2020-3-12 16:41:22		

	3.6. Spring MVC Test Framework		

		无需运行Servlet容器，即可进行相关测试。	

		3.6.1. Server-Side Tests

			java:

				import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.;
				import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.;

				@SpringJUnitWebConfig(locations = "test-servlet-context.xml")
				class ExampleTests {

				    MockMvc mockMvc;

				    @BeforeEach
				    void setup(WebApplicationContext wac) {
				        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
				    }

				    @Test
				    void getAccount() throws Exception {
				        this.mockMvc.perform(get("/accounts/1")
				                .accept(MediaType.APPLICATION_JSON))
				            .andExpect(status().isOk())
				            .andExpect(content().contentType("application/json"))
				            .andExpect(jsonPath("$.name").value("Lee"));
				    }
				}

			Static Imports	

			Setup Choices	

				java:

					@SpringJUnitWebConfig(locations = "my-servlet-context.xml")
					class MyWebTests {

					    MockMvc mockMvc;

					    @BeforeEach
					    void setup(WebApplicationContext wac) {
					        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
					    }

					    // ...

					}

					-------------------------------------

					class MyWebTests {

					    MockMvc mockMvc;

					    @BeforeEach
					    void setup() {
					        this.mockMvc = MockMvcBuilders.standaloneSetup(new AccountController()).build();
					    }

					    // ...

					}

				xml:	

					<bean id="accountService" class="org.mockito.Mockito" factory-method="mock">
					    <constructor-arg value="org.example.AccountService"/>
					</bean>

				java:
				
					@SpringJUnitWebConfig(locations = "test-servlet-context.xml")
					class AccountTests {

					    @Autowired
					    AccountService accountService;

					    MockMvc mockMvc;

					    @BeforeEach
					    void setup(WebApplicationContext wac) {
					        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
					    }

					    // ...

					}	

			Setup Features

				相关类： 

					// 定义了构造MockMvc实例的通用方法
					org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder
				
				java:

					// 为所有请求声明一个Accpect的头信息，为所有响应设置200的响应码和Content-Type的头信息

						// static import of MockMvcBuilders.standaloneSetup

						MockMvc mockMvc = standaloneSetup(new MusicController())
						    .defaultRequest(get("/").accept(MediaType.APPLICATION_JSON))
						    .alwaysExpect(status().isOk())
						    .alwaysExpect(content().contentType("application/json;charset=UTF-8"))
						    .build();	

					// MockMvcConfigurer，保存并循环使用http请求的session

						// static import of SharedHttpSessionConfigurer.sharedHttpSession

						MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
						        .apply(sharedHttpSession())
						        .build();

						// Use mockMvc to perform requests...

			Performing Requests	

				java:

					---------------------- 可以使用任何http方法 -----------------------

					mockMvc.perform(post("/hotels/{id}", 42).accept(MediaType.APPLICATION_JSON));

					---------------------- 上传文件 ----------------------- 

					// MockMultipartHttpServletRequest 
					mockMvc.perform(multipart("/doc").file("a1", "ABC".getBytes("UTF-8")));	

					---------------------- 指定查询参数 -----------------------

					mockMvc.perform(get("/hotels?thing={thing}", "somewhere"));	

					---------------------- 添加请求参数（查询/表单提交） -----------------------

					mockMvc.perform(get("/hotels").param("thing", "somewhere"));

					---------------------- 完整的请求URI -----------------------

					mockMvc.perform(get("/app/main/hotels/{id}").contextPath("/app").servletPath("/main"))

					---------------------- 设置请求的默认属性 -----------------------

					class MyWebTests {

				    MockMvc mockMvc;

				    @BeforeEach
				    void setup() {
				        mockMvc = standaloneSetup(new AccountController())
				            .defaultRequest(get("/")
				            .contextPath("/app").servletPath("/main")
				            .accept(MediaType.APPLICATION_JSON)).build();
				    }
				}

			Defining Expectations	

				package: MockMvcResultMatchers.* 

				category: 

					响应属性的验证： 响应码、头信息、响应内容

					其他： 处理请求的controller方法，是否有异常，内容模型，结果视图，新增属性等等


				java:

					---------------------- 添加期望值 -----------------------

					mockMvc.perform(get("/accounts/1")).andExpect(status().isOk());

					---------------------- 数据绑定及校验 -----------------------

					mockMvc.perform(post("/persons"))
					    .andExpect(status().isOk())
					    .andExpect(model().attributeHasErrors("person"));

					---------------------- 打印请求处理结果 -----------------------    

				    mockMvc.perform(post("/persons"))
					    .andDo(print())
					    .andExpect(status().isOk())
					    .andExpect(model().attributeHasErrors("person"));

					---------------------- 直接返回 -----------------------      

					MvcResult mvcResult = mockMvc.perform(post("/persons")).andExpect(status().isOk()).andReturn(); 

					---------------------- MockMvc设置通用期望值 -----------------------   

					standaloneSetup(new SimpleController())
					    .alwaysExpect(status().isOk())
					    .alwaysExpect(content().contentType("application/json;charset=UTF-8"))
					    .build()

					---------------------- 使用jsonPath表达式验证JSON响应结果中Spring HATEOAS创建的超链接 ----------------------   

					mockMvc.perform(get("/people").accept(MediaType.APPLICATION_JSON))
    					.andExpect(jsonPath("$.links[?(@.rel == 'self')].href").value("http://localhost:8080/people"));  

					---------------------- 使用xpath表达式验证XML响应结果中Spring HATEOAS创建的超链接 ----------------------   

    				Map<String, String> ns = Collections.singletonMap("ns", "http://www.w3.org/2005/Atom");
						mockMvc.perform(get("/handle").accept(MediaType.APPLICATION_XML))
						    .andExpect(xpath("/person/ns:link[@rel='self']/@href", ns).string("http://localhost:8080/people"));	

			Async Requests
			
				java:

					---------------------- 异步请求 ----------------------

					@Test
					void test() throws Exception {
					    MvcResult mvcResult = this.mockMvc.perform(get("/path"))
					            .andExpect(status().isOk()) 
					            .andExpect(request().asyncStarted()) 
					            .andExpect(request().asyncResult("body")) 
					            .andReturn();

					    this.mockMvc.perform(asyncDispatch(mvcResult)) 
					            .andExpect(status().isOk()) 
					            .andExpect(content().string("body"));  	


            Streaming Responses

        	Filter Registrations

        		java:

        			---------------------- 注册过滤器： MockFilterChain处理，DispatcherServlet集成最后一个过滤器 ----------------------

        			mockMvc = standaloneSetup(new PersonController()).addFilters(new CharacterEncodingFilter()).build();  

			Spring MVC Test vs End-to-End Tests

			Further Examples

				spring-test/test: https://github.com/spring-projects/spring-framework/tree/master/spring-test/src/test/java/org/springframework/test/web/servlet/samples

				project: spring-mvc-showcase 

			// done 2020-3-12 17:48:04
			
		3.6.2. HtmlUnit Integration	

			Why HtmlUnit Integration?

				Integration Testing to the Rescue?

				Enter HtmlUnit Integration

				HtmlUnit Integration Options

					MockMvc and HtmlUnit: Use this option if you want to use the raw HtmlUnit libraries.

					MockMvc and WebDriver: Use this option to ease development and reuse code between integration and end-to-end testing.

					MockMvc and Geb: Use this option if

			MockMvc and HtmlUnit		

				MockMvc and HtmlUnit Setup

					dependency: net.sourceforge.htmlunit:htmlunit 2.18+

					java:

						------------------- 创建Web客户端 -------------------

						WebClient webClient;

						@BeforeEach
						void setup(WebApplicationContext context) {
						    webClient = MockMvcWebClientBuilder
						            .webAppContextSetup(context)
						            .build();
						}

				MockMvc and HtmlUnit Usage

					java:

						------------------- 请求视图 -------------------

						HtmlPage createMsgFormPage = webClient.getPage("http://localhost/messages/form");

						------------------- 表单填充、提交、获取响应页面 -------------------

						HtmlForm form = createMsgFormPage.getHtmlElementById("messageForm");
						HtmlTextInput summaryInput = createMsgFormPage.getHtmlElementById("summary");
						summaryInput.setValueAttribute("Spring Rocks");
						HtmlTextArea textInput = createMsgFormPage.getHtmlElementById("text");
						textInput.setText("In case you didn't know, Spring Rocks!");
						HtmlSubmitInput submit = form.getOneHtmlElementByAttribute("input", "type", "submit");
						HtmlPage newMessagePage = submit.click();

						------------------- 解析、验证响应页面 -------------------

						assertThat(newMessagePage.getUrl().toString()).endsWith("/messages/123");
						String id = newMessagePage.getHtmlElementById("id").getTextContent();
						assertThat(id).isEqualTo("123");
						String summary = newMessagePage.getHtmlElementById("summary").getTextContent();
						assertThat(summary).isEqualTo("Spring Rocks");
						String text = newMessagePage.getHtmlElementById("text").getTextContent();
						assertThat(text).isEqualTo("In case you didn't know, Spring Rocks!");

				Advanced MockMvcWebClientBuilder
				
					java:

						------------------- 基本使用 -------------------

						WebClient webClient;

						@BeforeEach
						void setup(WebApplicationContext context) {
						    webClient = MockMvcWebClientBuilder
						            .webAppContextSetup(context)
						            .build();
						}		

						------------------- 添加配置信息 -------------------

						WebClient webClient;

						@BeforeEach
						void setup() {
						    webClient = MockMvcWebClientBuilder
						        // demonstrates applying a MockMvcConfigurer (Spring Security)
						        .webAppContextSetup(context, springSecurity())
						        // for illustration only - defaults to ""
						        .contextPath("")
						        // By default MockMvc is used for localhost only;
						        // the following will use MockMvc for example.com and example.org as well
						        .useMockMvcForHosts("example.com","example.org")
						        .build();
						}

						------------------- 通过MockMvc添加配置信息 -------------------

						MockMvc mockMvc = MockMvcBuilders
						        .webAppContextSetup(context)
						        .apply(springSecurity())
						        .build();

						webClient = MockMvcWebClientBuilder
						        .mockMvcSetup(mockMvc)
						        // for illustration only - defaults to ""
						        .contextPath("")
						        // By default MockMvc is used for localhost only;
						        // the following will use MockMvc for example.com and example.org as well
						        .useMockMvcForHosts("example.com","example.org")
						        .build();

			MockMvc and WebDriver

				Why WebDriver and MockMvc?

					java:

						HtmlTextInput summaryInput = currentPage.getHtmlElementById("summary");
						summaryInput.setValueAttribute(summary);

						-------------------

						public HtmlPage createMessage(HtmlPage currentPage, String summary, String text) {
						    setSummary(currentPage, summary);
						    // ...
						}

						public void setSummary(HtmlPage currentPage, String summary) {
						    HtmlTextInput summaryInput = currentPage.getHtmlElementById("summary");
						    summaryInput.setValueAttribute(summary);
						}

						-------------------

						public class CreateMessagePage {

						    final HtmlPage currentPage;

						    final HtmlTextInput summaryInput;

						    final HtmlSubmitInput submit;

						    public CreateMessagePage(HtmlPage currentPage) {
						        this.currentPage = currentPage;
						        this.summaryInput = currentPage.getHtmlElementById("summary");
						        this.submit = currentPage.getHtmlElementById("submit");
						    }

						    public <T> T createMessage(String summary, String text) throws Exception {
						        setSummary(summary);

						        HtmlPage result = submit.click();
						        boolean error = CreateMessagePage.at(result);

						        return (T) (error ? new CreateMessagePage(result) : new ViewMessagePage(result));
						    }

						    public void setSummary(String summary) throws Exception {
						        summaryInput.setValueAttribute(summary);
						    }

						    public static boolean at(HtmlPage page) {
						        return "Create Message".equals(page.getTitleText());
						    }
						}

				MockMvc and WebDriver Setup
				
					java:

						------------------- 创建WebDriver ------------------- 

						WebDriver driver;

						@BeforeEach
						void setup(WebApplicationContext context) {
						    driver = MockMvcHtmlUnitDriverBuilder
						            .webAppContextSetup(context)
						            .build();
						}	

				MockMvc and WebDriver Usage
				
					java:

						------------------- 获取页面 -------------------

						CreateMessagePage page = CreateMessagePage.to(driver);

						------------------- 表单填充、提交、获取响应页面 -------------------

						ViewMessagePage viewMessagePage =
        					page.createMessage(ViewMessagePage.class, expectedSummary, expectedText);

    					------------------- 响应页面信息解析、校验 -------------------

    					assertThat(viewMessagePage.getMessage()).isEqualTo(expectedMessage);
						assertThat(viewMessagePage.getSuccess()).isEqualTo("Successfully created a new message");

						------------------- 通过继承AbstractPage简易化操作 -------------------

    					public class CreateMessagePage extends AbstractPage { 

						    
						    private WebElement summary;
						    private WebElement text;

						    
						    @FindBy(css = "input[type=submit]")
						    private WebElement submit;

						    public CreateMessagePage(WebDriver driver) {
						        super(driver);
						    }

						    public <T> T createMessage(Class<T> resultPage, String summary, String details) {
						        this.summary.sendKeys(summary);
						        this.text.sendKeys(details);
						        this.submit.click();
						        return PageFactory.initElements(driver, resultPage);
						    }

						    public static CreateMessagePage to(WebDriver driver) {
						        driver.get("http://localhost:9990/mail/messages/form");
						        return PageFactory.initElements(driver, CreateMessagePage.class);
						    }
						}	

						------------------- 通过ViewMessagePage获取的Message与域对象交互 -------------------

						public Message getMessage() throws ParseException {
						    Message message = new Message();
						    message.setId(getId());
						    message.setCreated(getCreated());
						    message.setSummary(getSummary());
						    message.setText(getText());
						    return message;
						}		

						------------------- 回收driver -------------------

						@AfterEach
						void destroy() {
						    if (driver != null) {
						        driver.close();
						    }
						}

				Advanced MockMvcHtmlUnitDriverBuilder

					java:

						------------------- 基本用法 -------------------

						WebDriver driver;

						@BeforeEach
						void setup(WebApplicationContext context) {
						    driver = MockMvcHtmlUnitDriverBuilder
						            .webAppContextSetup(context)
						            .build();
						}

						------------------- 配置信息 -------------------

						WebDriver driver;

						@BeforeEach
						void setup() {
						    driver = MockMvcHtmlUnitDriverBuilder
						            // demonstrates applying a MockMvcConfigurer (Spring Security)
						            .webAppContextSetup(context, springSecurity())
						            // for illustration only - defaults to ""
						            .contextPath("")
						            // By default MockMvc is used for localhost only;
						            // the following will use MockMvc for example.com and example.org as well
						            .useMockMvcForHosts("example.com","example.org")
						            .build();
						}

						------------------- 通过MockMvc配置信息 -------------------

						MockMvc mockMvc = MockMvcBuilders
						        .webAppContextSetup(context)
						        .apply(springSecurity())
						        .build();

						driver = MockMvcHtmlUnitDriverBuilder
						        .mockMvcSetup(mockMvc)
						        // for illustration only - defaults to ""
						        .contextPath("")
						        // By default MockMvc is used for localhost only;
						        // the following will use MockMvc for example.com and example.org as well
						        .useMockMvcForHosts("example.com","example.org")
						        .build();

	        MockMvc and Geb

	        	Why Geb and MockMvc?

	        	MockMvc and Geb Setup

	        		java:

	        			------------------- 初始化一个 Geb Brower -------------------

	        			def setup() {
						    browser.driver = MockMvcHtmlUnitDriverBuilder
						        .webAppContextSetup(context)
						        .build()
						}

				MockMvc and Geb Usage

					groovy:

						------------------- 获取请求页面 -------------------

						to CreateMessagePage

						------------------- 表单填充、提交、获取响应页面 -------------------

						when:
						form.summary = expectedSummary
						form.text = expectedMessage
						submit.click(ViewMessagePage)

						------------------- 使用页面对象格式 -------------------

						class CreateMessagePage extends Page {
						    static url = 'messages/form'
						    static at = { assert title == 'Messages : Create'; true }
						    static content =  {
						        submit { $('input[type=submit]') }
						        form { $('form') }
						        errors(required:false) { $('label.error, .alert-error')?.text() }
						    }
						}

						------------------- 获取请求页面、校验响应结果 -------------------

						to CreateMessagePage

						then:
						at CreateMessagePage
						errors.contains('This field is required.')

						then:
						at ViewMessagePage
						success == 'Successfully created a new message'
						id
						date
						summary == expectedSummary
						message == expectedMessage

			// done 2020-3-12 19:37:20

		3.6.3. Client-Side REST Tests

			java:

				RestTemplate restTemplate = new RestTemplate();

				MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
				mockServer.expect(requestTo("/greeting")).andRespond(withSuccess());

				// Test code that uses the above RestTemplate ...

				mockServer.verify();

				------------------- ignoreExpectOrder -------------------

				server = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();

				------------------- 指定执行次数 -------------------

				RestTemplate restTemplate = new RestTemplate();

				MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplate).build();
				mockServer.expect(times(2), requestTo("/something")).andRespond(withSuccess());
				mockServer.expect(times(3), requestTo("/somewhere")).andRespond(withSuccess());

				// ...

				mockServer.verify();

				------------------- 绑定MockMvc -------------------

				MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
				this.restTemplate = new RestTemplate(new MockMvcClientHttpRequestFactory(mockMvc));

				// Test code that uses the above RestTemplate ...

			Static Imports
			
			Further Examples of Client-side REST Tests

				spring-test/test: https://github.com/spring-projects/spring-framework/tree/master/spring-test/src/test/java/org/springframework/test/web/client/samples

		// done 2020-3-12 19:44:38

	3.7. WebTestClient	

		3.7.1. Setup

			java:

				------------------- Bind to Controller -------------------

				client = WebTestClient.bindToController(new TestController()).build();

				------------------- Bind to Router Function -------------------

				RouterFunction<?> route = ...
				client = WebTestClient.bindToRouterFunction(route).build();	

				------------------- Bind to ApplicationContext -------------------

				@SpringJUnitConfig(WebConfig.class) 
				class MyTests {

				    WebTestClient client;

				    @BeforeEach
				    void setUp(ApplicationContext context) {  
				        client = WebTestClient.bindToApplicationContext(context).build(); 
				    }
				}

				------------------- Bind to Server -------------------

				client = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();

				------------------- Client Builder -------------------

				client = WebTestClient.bindToController(new TestController())
			        .configureClient()
			        .baseUrl("/test")
			        .build();

        3.7.2. Writing Tests

        	java:

        		------------------- 校验响应码和响应头信息 -------------------

	        	client.get().uri("/persons/1")
			            .accept(MediaType.APPLICATION_JSON)
			            .exchange()
			            .expectStatus().isOk()
			            .expectHeader().contentType(MediaType.APPLICATION_JSON)


	            client.get().uri("/persons")
				        .exchange()
				        .expectStatus().isOk()
				        .expectBodyList(Person.class).hasSize(3).contains(person);
			
				client.get().uri("/persons/1")
				        .exchange()
				        .expectStatus().isOk()
				        .expectBody(Person.class)
				        .consumeWith(result -> {
				            // custom assertions (e.g. AssertJ)...
				        });

		        EntityExchangeResult<Person> result = client.get().uri("/persons/1")
				        .exchange()
				        .expectStatus().isOk()
				        .expectBody(Person.class)
				        .returnResult();

	        No Content

	        	java:

	        		client.get().uri("/persons/123")
					        .exchange()
					        .expectStatus().isNotFound()
					        .expectBody(Void.class);

			        client.post().uri("/persons")
					        .body(personMono, Person.class)
					        .exchange()
					        .expectStatus().isCreated()
					        .expectBody().isEmpty();

	        JSON Content

	        	java:

	        		client.get().uri("/persons/1")
					        .exchange()
					        .expectStatus().isOk()
					        .expectBody()
					        .json("{\"name\":\"Jane\"}")

			        client.get().uri("/persons")
					        .exchange()
					        .expectStatus().isOk()
					        .expectBody()
					        .jsonPath("$[0].name").isEqualTo("Jane")
					        .jsonPath("$[1].name").isEqualTo("Jason");

	        Streaming Responses

	        	java:

	        		FluxExchangeResult<MyEvent> result = client.get().uri("/events")
					        .accept(TEXT_EVENT_STREAM)
					        .exchange()
					        .expectStatus().isOk()
					        .returnResult(MyEvent.class);


			        Flux<Event> eventFlux = result.getResponseBody();

					StepVerifier.create(eventFlux)
					        .expectNext(person)
					        .expectNextCount(4)
					        .consumeNextWith(p -> ...)
					        .thenCancel()
					        .verify();

        	Request Body

4. Further Resources

	JUnit: “A programmer-friendly testing framework for Java”. Used by the Spring Framework in its test suite and supported in the Spring TestContext Framework.

	TestNG: A testing framework inspired by JUnit with added support for test groups, data-driven testing, distributed testing, and other features. Supported in the Spring TestContext Framework

	AssertJ: “Fluent assertions for Java”, including support for Java 8 lambdas, streams, and other features.

	Mock Objects: Article in Wikipedia.

	MockObjects.com: Web site dedicated to mock objects, a technique for improving the design of code within test-driven development.

	Mockito: Java mock library based on the Test Spy pattern. Used by the Spring Framework in its test suite.

	EasyMock: Java library “that provides Mock Objects for interfaces (and objects through the class extension) by generating them on the fly using Java’s proxy mechanism.”

	JMock: Library that supports test-driven development of Java code with mock objects.

	DbUnit: JUnit extension (also usable with Ant and Maven) that is targeted at database-driven projects and, among other things, puts your database into a known state between test runs.

	The Grinder: Java load testing framework.

	SpringMockK: Support for Spring Boot integration tests written in Kotlin using MockK instead of Mockito.	

// done 2020-3-12 20:07:02	

		


