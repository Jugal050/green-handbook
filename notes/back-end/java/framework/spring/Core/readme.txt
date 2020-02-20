Core Technologies 			https://docs.spring.io/spring/docs/5.2.3.RELEASE/spring-framework-reference/core.html#spring-core

1. The IoC Container

	1.1. Introduction to the Spring IoC Container and Beans

		basis packages: 

			org.springframework.beans
			org.springframework.context

		major classes:

			org.springframework.beans.factory.BeanFactory


			org.springframework.context.ApplicationContext

				implements:

					org.springframework.context.support.ClassPathXmlApplicationContext
					org.springframework.context.support.FileSystemXmlApplicationContext


			org.springframework.web.context.WebApplicationContext 

	1.2. Container Overview
	
		1.2.1. Configuration Metadata

			<?xml version="1.0" encoding="UTF-8"?>
			<beans xmlns="http://www.springframework.org/schema/beans"
			    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			    xsi:schemaLocation="http://www.springframework.org/schema/beans
			        https://www.springframework.org/schema/beans/spring-beans.xsd">

			    <bean id="..." class="...">  
			        <!-- collaborators and configuration for this bean go here -->
			    </bean>

			    <bean id="..." class="...">
			        <!-- collaborators and configuration for this bean go here -->
			    </bean>

			    <!-- more bean definitions go here -->

			</beans>

		1.2.2. Instantiating a Container		

			ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");

			service.xml

				<?xml version="1.0" encoding="UTF-8"?>
				<beans xmlns="http://www.springframework.org/schema/beans"
				    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				    xsi:schemaLocation="http://www.springframework.org/schema/beans
				        https://www.springframework.org/schema/beans/spring-beans.xsd">

				    <!-- services -->

				    <bean id="petStore" class="org.springframework.samples.jpetstore.services.PetStoreServiceImpl">
				        <property name="accountDao" ref="accountDao"/>
				        <property name="itemDao" ref="itemDao"/>
				        <!-- additional collaborators and configuration for this bean go here -->
				    </bean>

				    <!-- more bean definitions for services go here -->

				</beans>

			daos.xml
			
				<?xml version="1.0" encoding="UTF-8"?>
				<beans xmlns="http://www.springframework.org/schema/beans"
				    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				    xsi:schemaLocation="http://www.springframework.org/schema/beans
				        https://www.springframework.org/schema/beans/spring-beans.xsd">

				    <bean id="accountDao"
				        class="org.springframework.samples.jpetstore.dao.jpa.JpaAccountDao">
				        <!-- additional collaborators and configuration for this bean go here -->
				    </bean>

				    <bean id="itemDao" class="org.springframework.samples.jpetstore.dao.jpa.JpaItemDao">
				        <!-- additional collaborators and configuration for this bean go here -->
				    </bean>

				    <!-- more bean definitions for data access objects go here -->

				</beans>	

			Composing XML-based Configuration Metadata
			
				<beans>
				    <import resource="services.xml"/>
				    <import resource="resources/messageSource.xml"/>
				    <import resource="/resources/themeSource.xml"/>

				    <bean id="bean1" class="..."/>
				    <bean id="bean2" class="..."/>
				</beans>	
	
		1.2.3. Using the Container

			// create and configure beans
			ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");

			// retrieve configured instance
			PetStoreService service = context.getBean("petStore", PetStoreService.class);

			// use configured instance
			List<String> userList = service.getUsernameList();

			or: 

			GenericApplicationContext context = new GenericApplicationContext();
			new XmlBeanDefinitionReader(context).loadBeanDefinitions("services.xml", "daos.xml");
			context.refresh();

	1.3. Bean Overview
	
		1.3.1. Naming Beans

			Aliasing a Bean outside the Bean Definition

				<alias name="myApp-dataSource" alias="subsystemA-dataSource"/>
				<alias name="myApp-dataSource" alias="subsystemB-dataSource"/>		

		1.3.2. Instantiating Beans
		
				Inner class names： com.example.SomeThing$OtherThing.

			Instantiation with a Constructor

				xml:
			
					<bean id="exampleBean" class="examples.ExampleBean"/>

					<bean name="anotherExample" class="examples.ExampleBeanTwo"/>	

			Instantiation with a Static Factory Method

				xml:

					<bean id="clientService" class="examples.ClientService" factory-method="createInstance"/>

				java:
					
					public class ClientService {
					    private static ClientService clientService = new ClientService();
					    private ClientService() {}

					    public static ClientService createInstance() {
					        return clientService;
					    }
					}

			Instantiation by Using an Instance Factory Method
			
				xml:

					<bean id="serviceLocator" class="examples.DefaultServiceLocator">
					    <!-- inject any dependencies required by this locator bean -->
					</bean>

					<bean id="clientService" factory-bean="serviceLocator" factory-method="createClientServiceInstance"/>

					<bean id="accountService" factory-bean="serviceLocator" factory-method="createAccountServiceInstance"/>			

				java:
				
					public class DefaultServiceLocator {

					    private static ClientService clientService = new ClientServiceImpl();

					    private static AccountService accountService = new AccountServiceImpl();

					    public ClientService createClientServiceInstance() {
					        return clientService;
					    }

					    public AccountService createAccountServiceInstance() {
					        return accountService;
					    }
					}

			BeanFactory与FactoryBean的区别：		
				 
				 BeanFactory是个Factory，也就是IOC容器或对象工厂，FactoryBean是个Bean 。

				 在Spring中，所有的Bean都是由BeanFactory(也就是IOC容器) 来进行管理的。

				 但对FactoryBean而言，这个Bean不是简单的Bean，而是一个能生产或者修饰对象生成的工厂Bean,它的实现与设计模式中的工厂模式和修饰器模式类似。

	1.4. Dependencies

		1.4.1. Dependency Injection

			Constructor-based Dependency Injection 构造器注入

				public class SimpleMovieLister {

				    // the SimpleMovieLister has a dependency on a MovieFinder
				    private MovieFinder movieFinder;

				    // a constructor so that the Spring container can inject a MovieFinder
				    public SimpleMovieLister(MovieFinder movieFinder) {
				        this.movieFinder = movieFinder;
				    }

				    // business logic that actually uses the injected MovieFinder is omitted...
				}

				Constructor Argument Resolution

					构造器参数为引用类型：

						xml:

							<beans>
							    <bean id="beanOne" class="x.y.ThingOne">
							        <constructor-arg ref="beanTwo"/>
							        <constructor-arg ref="beanThree"/>
							    </bean>

							    <bean id="beanTwo" class="x.y.ThingTwo"/>

							    <bean id="beanThree" class="x.y.ThingThree"/>
							</beans>
					
						java:

							package x.y;

							public class ThingOne {

							    public ThingOne(ThingTwo thingTwo, ThingThree thingThree) {
							        // ...
							    }
							}	

					构造器参数为基本类型：
					
						java: 

							package examples;

							public class ExampleBean {

							    // Number of years to calculate the Ultimate Answer
							    private int years;

							    // The Answer to Life, the Universe, and Everything
							    private String ultimateAnswer;

							    public ExampleBean(int years, String ultimateAnswer) {
							        this.years = years;
							        this.ultimateAnswer = ultimateAnswer;
							    }
							}

						xml:

							Constructor argument type matching	

								<bean id="exampleBean" class="examples.ExampleBean">
								    <constructor-arg type="int" value="7500000"/>
								    <constructor-arg type="java.lang.String" value="42"/>
								</bean>

							Constructor argument index

								<bean id="exampleBean" class="examples.ExampleBean">
								    <constructor-arg index="0" value="7500000"/>
								    <constructor-arg index="1" value="42"/>
								</bean>	

							Constructor argument name
							
								<bean id="exampleBean" class="examples.ExampleBean">
								    <constructor-arg name="years" value="7500000"/>
								    <constructor-arg name="ultimateAnswer" value="42"/>
								</bean>	

				Setter-based Dependency Injection	Setter方法注入：

					public class SimpleMovieLister {

					    // the SimpleMovieLister has a dependency on the MovieFinder
					    private MovieFinder movieFinder;

					    // a setter method so that the Spring container can inject a MovieFinder
					    public void setMovieFinder(MovieFinder movieFinder) {
					        this.movieFinder = movieFinder;
					    }

					    // business logic that actually uses the injected MovieFinder is omitted...
					}



				Tips: 	

					## Constructor-based or setter-based DI?

							Since you can mix constructor-based and setter-based DI, it is a good rule of thumb to use constructors for mandatory dependencies and setter methods or configuration methods for optional dependencies. Note that use of the @Required annotation on a setter method can be used to make the property be a required dependency; however, constructor injection with programmatic validation of arguments is preferable.

							The Spring team generally advocates constructor injection, as it lets you implement application components as immutable objects and ensures that required dependencies are not null. Furthermore, constructor-injected components are always returned to the client (calling) code in a fully initialized state. As a side note, a large number of constructor arguments is a bad code smell, implying that the class likely has too many responsibilities and should be refactored to better address proper separation of concerns.

							Setter injection should primarily only be used for optional dependencies that can be assigned reasonable default values within the class. Otherwise, not-null checks must be performed everywhere the code uses the dependency. One benefit of setter injection is that setter methods make objects of that class amenable to reconfiguration or re-injection later. Management through JMX MBeans is therefore a compelling use case for setter injection.

							Use the DI style that makes the most sense for a particular class. Sometimes, when dealing with third-party classes for which you do not have the source, the choice is made for you. For example, if a third-party class does not expose any setter methods, then constructor injection may be the only available form of DI.

						使用构造器注入还是setter方法注入？

							推荐的规则是：对于有强制性的依赖则使用构造器，对于其他可选择的依赖则使用setter方法。
							在setter方法上添加注解@Required可以标识属性为必需的依赖，但是，更推荐使用构造器进行依赖注入然后使用单独校验代码。

							构造器注入是Spring团队通常推荐使用的，因为它可以让你的应用组件作为不可变对象，可以确保必需依赖不能为空。而且，构造器注入的组件是完全初始化的，
							但是另一方面，如果构造器有大量的参数也会使代码很糟糕，也意味着我们需要更多的责任去进行代码重构。

							setter注入，应该只有在可选性依赖可以赋予默认值的时候才使用。否则，用到依赖就要添加非空校验。
							setter注入的优点：一是setter方法可以重复调用，二是可以通过JMX MBeanns进行管理。

							一般类可以使用这两种方法依赖注入。但有一些是第三方的类，我们没有源码的时候，就需要自己判断，如果第三方类没有暴露任何setter方法，就只能使用构造器注入。


					## Circular dependencies 

							If you use predominantly constructor injection, it is possible to create an unresolvable circular dependency scenario.

							For example: Class A requires an instance of class B through constructor injection, and class B requires an instance of class A through constructor injection. If you configure beans for classes A and B to be injected into each other, the Spring IoC container detects this circular reference at runtime, and throws a BeanCurrentlyInCreationException.

							One possible solution is to edit the source code of some classes to be configured by setters rather than constructors. Alternatively, avoid constructor injection and use setter injection only. In other words, although it is not recommended, you can configure circular dependencies with setter injection.

							Unlike the typical case (with no circular dependencies), a circular dependency between bean A and bean B forces one of the beans to be injected into the other prior to being fully initialized itself (a classic chicken-and-egg scenario).	

						循环依赖

							如果使用构造器注入，可能会出现循环依赖问题。

							比如： 类A进行构造器注入时需要B实例，类B进行构造器注入时需要A实例。 如果出现这种互相依赖，容器运行时会检查，并抛出异常BeanCurrentlyInCreationException。

							一个解决方法时使用setter方法注入代替构造器注入。 虽然不推荐，但是可以使用setter注入解决循环依赖问题。

							和普通情况不一样，一个A-B之间的循环依赖会导致必须先有一个类实例完全初始化（典型的 鸡和蛋 问题）。

		1.4.2. Dependencies and Configuration in Detail		

				Straight Values (Primitives, Strings, and so on)			

						<bean id="myDataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
						    <!-- results in a setDriverClassName(String) call -->
						    <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
						    <property name="url" value="jdbc:mysql://localhost:3306/mydb"/>
						    <property name="username" value="root"/>
						    <property name="password" value="masterkaoli"/>
						</bean>

					== 
					
						<beans xmlns="http://www.springframework.org/schema/beans"
						    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
						    xmlns:p="http://www.springframework.org/schema/p"
						    xsi:schemaLocation="http://www.springframework.org/schema/beans
						    https://www.springframework.org/schema/beans/spring-beans.xsd">

						    <bean id="myDataSource" class="org.apache.commons.dbcp.BasicDataSource"
						        destroy-method="close"
						        p:driverClassName="com.mysql.jdbc.Driver"
						        p:url="jdbc:mysql://localhost:3306/mydb"
						        p:username="root"
						        p:password="masterkaoli"/>

						</beans>	

					== 

						<bean id="mappings"
						    class="org.springframework.context.support.PropertySourcesPlaceholderConfigurer">

						    <!-- typed as a java.util.Properties -->
						    <property name="properties">
						        <value>
						            jdbc.driver.className=com.mysql.jdbc.Driver
						            jdbc.url=jdbc:mysql://localhost:3306/mydb
						        </value>
						    </property>
						</bean>	

				The idref element

					<idref/>在<constructor-arg/> or <property/>中使用

						<bean id="theTargetBean" class="..."/>

						<bean id="theClientBean" class="...">
						    <property name="targetName">
						        <idref bean="theTargetBean"/>
						    </property>
						</bean>

					== 

						<bean id="theTargetBean" class="..." />

						<bean id="client" class="...">
						    <property name="targetName" value="theTargetBean"/>
						</bean>
				
				References to Other Beans (Collaborators)	

					<!-- in the parent context -->
					<bean id="accountService" class="com.something.SimpleAccountService">
					    <!-- insert dependencies as required as here -->
					</bean>

					<!-- in the child (descendant) context -->
					<bean id="accountService" <!-- bean name is the same as the parent bean -->
					    class="org.springframework.aop.framework.ProxyFactoryBean">
					    <property name="target">
					        <ref parent="accountService"/> <!-- notice how we refer to the parent bean -->
					    </property>
					    <!-- insert other configuration and dependencies as required here -->
					</bean>

				Inner Beans	

					<bean id="outer" class="...">
						<!-- instead of using a reference to a target bean, simply define the target bean inline -->
						<property name="target">
						    <bean class="com.example.Person"> <!-- this is the inner bean -->
						        <property name="name" value="Fiona Apple"/>
						        <property name="age" value="25"/>
						    </bean>
						</property>
					</bean>

				Collections

					标签： <list/>, <set/>, <map/>, and <props/>

						<bean id="moreComplexObject" class="example.ComplexObject">
						    <!-- results in a setAdminEmails(java.util.Properties) call -->
						    <property name="adminEmails">
						        <props>
						            <prop key="administrator">administrator@example.org</prop>
						            <prop key="support">support@example.org</prop>
						            <prop key="development">development@example.org</prop>
						        </props>
						    </property>
						    <!-- results in a setSomeList(java.util.List) call -->
						    <property name="someList">
						        <list>
						            <value>a list element followed by a reference</value>
						            <ref bean="myDataSource" />
						        </list>
						    </property>
						    <!-- results in a setSomeMap(java.util.Map) call -->
						    <property name="someMap">
						        <map>
						            <entry key="an entry" value="just some string"/>
						            <entry key ="a ref" value-ref="myDataSource"/>
						        </map>
						    </property>
						    <!-- results in a setSomeSet(java.util.Set) call -->
						    <property name="someSet">
						        <set>
						            <value>just some string</value>
						            <ref bean="myDataSource" />
						        </set>
						    </property>
						</bean>

					Collection Merging

						<beans>
						    <bean id="parent" abstract="true" class="example.ComplexObject">
						        <property name="adminEmails">
						            <props>
						                <prop key="administrator">administrator@example.com</prop>
						                <prop key="support">support@example.com</prop>
						            </props>
						        </property>
						    </bean>
						    <bean id="child" parent="parent">
						        <property name="adminEmails">
						            <!-- the merge is specified on the child collection definition -->
						            <props merge="true">
						                <prop key="sales">sales@example.com</prop>
						                <prop key="support">support@example.co.uk</prop>
						            </props>
						        </property>
						    </bean>
						<beans>

					Limitations of Collection Merging

					Strongly-typed collection

						xml:

							<beans>
							    <bean id="something" class="x.y.SomeClass">
							        <property name="accounts">
							            <map>
							                <entry key="one" value="9.99"/>
							                <entry key="two" value="2.75"/>
							                <entry key="six" value="3.99"/>
							            </map>
							        </property>
							    </bean>
							</beans>

						java:
						
							public class SomeClass {

							    private Map<String, Float> accounts;

							    public void setAccounts(Map<String, Float> accounts) {
							        this.accounts = accounts;
							    }
							}	

				Null and Empty String Values

					Empty String: 

						<bean class="ExampleBean">
						    <property name="email" value=""/>
						</bean>

						== exampleBean.setEmail("");

					Null:

						<bean class="ExampleBean">
						    <property name="email">
						        <null/>
						    </property>
						</bean>

						exampleBean.setEmail(null);

				XML Shortcut with the p-namespace

					== <property/>

					<beans xmlns="http://www.springframework.org/schema/beans"
					    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
					    xmlns:p="http://www.springframework.org/schema/p"
					    xsi:schemaLocation="http://www.springframework.org/schema/beans
					        https://www.springframework.org/schema/beans/spring-beans.xsd">

					    <bean name="classic" class="com.example.ExampleBean">
					        <property name="email" value="someone@somewhere.com"/>
					    </bean>

					    <bean name="p-namespace" class="com.example.ExampleBean" p:email="someone@somewhere.com"/>

					    <bean name="john-classic" class="com.example.Person">
					        <property name="name" value="John Doe"/>
					        <property name="spouse" ref="jane"/>
					    </bean>

					    <bean name="john-modern" class="com.example.Person" p:name="John Doe" p:spouse-ref="jane"/>

					    <bean name="jane" class="com.example.Person">
					        <property name="name" value="Jane Doe"/>
					    </bean>
					</beans>

				XML Shortcut with the c-namespace

					== <constructor-arg/>

					<beans xmlns="http://www.springframework.org/schema/beans"
					    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
					    xmlns:c="http://www.springframework.org/schema/c"
					    xsi:schemaLocation="http://www.springframework.org/schema/beans
					        https://www.springframework.org/schema/beans/spring-beans.xsd">

					    <bean id="beanTwo" class="x.y.ThingTwo"/>
					    <bean id="beanThree" class="x.y.ThingThree"/>

					    <!-- traditional declaration with optional argument names -->
					    <bean id="beanOne" class="x.y.ThingOne">
					        <constructor-arg name="thingTwo" ref="beanTwo"/>
					        <constructor-arg name="thingThree" ref="beanThree"/>
					        <constructor-arg name="email" value="something@somewhere.com"/>
					    </bean>

					    <!-- c-namespace declaration with argument names -->
					    <bean id="beanOne" class="x.y.ThingOne" c:thingTwo-ref="beanTwo" c:thingThree-ref="beanThree" c:email="something@somewhere.com"/>

				        <!-- c-namespace index declaration -->
						<bean id="beanOne" class="x.y.ThingOne" c:_0-ref="beanTwo" c:_1-ref="beanThree" c:_2="something@somewhere.com"/> 

					</beans>

				Compound Property Names
				
					<bean id="something" class="things.ThingOne">
					    <property name="fred.bob.sammy" value="123" />
					</bean>	


		1.4.3. Using depends-on		

			<bean id="beanOne" class="ExampleBean" depends-on="manager,accountDao">
			    <property name="manager" ref="manager" />
			</bean>

			<bean id="manager" class="ManagerBean" />
			<bean id="accountDao" class="x.y.jdbc.JdbcAccountDao" />

		1.4.4. Lazy-initialized Beans
		
			<bean id="lazy" class="com.something.ExpensiveToCreateBean" lazy-init="true"/>
			<bean name="not.lazy" class="com.something.AnotherBean"/>	

			<beans default-lazy-init="true">
			    <!-- no beans will be pre-instantiated... -->
			</beans>

		1.4.5. Autowiring Collaborators
		
			
			// 略读，源码待读 2020-2-20 19:31:51


	1.5. Bean Scopes			 

			









	














