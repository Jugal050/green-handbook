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

		


