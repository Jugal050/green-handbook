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

			Autowiring modes：

				no

				(Default) No autowiring. Bean references must be defined by ref elements. Changing the default setting is not recommended for larger deployments, because specifying collaborators explicitly gives greater control and clarity. To some extent, it documents the structure of a system.

				（默认）无自动注入。 Bean引用必须使用ref元素定义。 大型应用部署时不推荐修改默认值，因为其中会有一些特殊配置需要特别管理和明确。从某种角度来说，它决定了一个系统的结构。

				byName

				Autowiring by property name. Spring looks for a bean with the same name as the property that needs to be autowired. For example, if a bean definition is set to autowire by name and it contains a master property (that is, it has a setMaster(..) method), Spring looks for a bean definition named master and uses it to set the property.

				根据属性名称自动注入。 Spring会查找和需要注入的元素同名的bean。 
				比如，如果一个bean设置为根据名称注入，其中包含一个master的元素（有setMaster方法），Spring会查找名称为master的bean然后注入到此元素master中。

				byType

				Lets a property be autowired if exactly one bean of the property type exists in the container. If more than one exists, a fatal exception is thrown, which indicates that you may not use byType autowiring for that bean. If there are no matching beans, nothing happens (the property is not set).

				根据属性类型自动注入。 如果容器中存在与属性类型相同的单个bean则注入元素。 如果存在多个，代表根据元素类型无法注入bean，则抛出异常。如果没有匹配的beans，则不进行任何操作。

				constructor

				Analogous to byType but applies to constructor arguments. If there is not exactly one bean of the constructor argument type in the container, a fatal error is raised.

				与byType类似，但是使用构造器参数注入。如果容器中没有与构造器参数相同类型的bean，则程序中止。

			Limitations and Disadvantages of Autowiring
			
				Explicit dependencies in property and constructor-arg settings always override autowiring. You cannot autowire simple properties such as primitives, Strings, and Classes (and arrays of such simple properties). This limitation is by-design.

				属性和构造器参数设置的明确依赖往往会覆盖自动注入。 我们不能自动注入简单的属性，比如基本类型，String，Class，以及包含这些类型的数组。 这些属于设计上的限制。

				Autowiring is less exact than explicit wiring. Although, as noted in the earlier table, Spring is careful to avoid guessing in case of ambiguity that might have unexpected results. The relationships between your Spring-managed objects are no longer documented explicitly.

				自动注入比明确注入更加模棱两可，尽管，如原先表格上的笔记所说，Spring已经很谨慎的避免出现，如果有模棱两可的结果时开发者会猜测，但，在spring管理的对象之间关系并不是那么明确

				Wiring information may not be available to tools that may generate documentation from a Spring container.

				对于有些从spring容器中生成文档的工具类，信息注入可能不可用。

				Multiple bean definitions within the container may match the type specified by the setter method or constructor argument to be autowired. For arrays, collections, or Map instances, this is not necessarily a problem. However, for dependencies that expect a single value, this ambiguity is not arbitrarily resolved. If no unique bean definition is available, an exception is thrown.	

				容器中如果有多个bean定义信息，可能会根据setter方法或者构造器参数注入的类型进行匹配。 对于数组array，结合collection，或者映射map实例，这些不是问题。
				但是对于明确仅有一个值的那些依赖，这种模棱两可并不能简单粗暴地解决。 如果没有唯一的bean定义信息可用，则会抛出异常。

			In the latter scenario, you have several options:
			
				Abandon autowiring in favor of explicit wiring.
				放弃自动注入，使用明确注入。

				Avoid autowiring for a bean definition by setting its autowire-candidate attributes to false, as described in the next section.
				通过设置<bean autowire-candidate="false" />防止自动注入。 

				Designate a single bean definition as the primary candidate by setting the primary attribute of its <bean/> element to true.
				设置<bean primary="true" />将此bean设置为注入时的首选。

				Implement the more fine-grained control available with annotation-based configuration, as described in Annotation-based Container Configuration.	
				使用基于注解的配置实现更细粒度的控制。

			Excluding a Bean from Autowiring		

				<bean autowire-candidate="false" /> （优先级更高）

				<beans default-autowire-candidates="*Repository, *Service, ...">  （比bean中设置优先级低）
				</beans>

				tips: 

					只对注入类型为byType的有效，不会对byName产生影响。

					当我们不想通过自动注入将此bean注入到其他bean中时，可以用这种方法，但这并不代表此bean本生不能使用自动注入进行配置，而是，注入其他的bean时它不再是一个候选者。

		1.4.6. Method Injection

			引出问题：

				uppose singleton bean A needs to use non-singleton (prototype) bean B, perhaps on each method invocation on A. The container creates the singleton bean A only once, and thus only gets one opportunity to set the properties. The container cannot provide bean A with a new instance of bean B every time one is needed.

				单例bean A中每次调用方法，需要非单例（prototype）的bean B。 容器只会创建A一次，这样只有一次机会来设置属性。 容器无法每次给A提供一个新创建的B。

			解决方案：
			
				1. 放弃IOC，让bean A	通过实现接口ApplicationContextAware（既可以设置容器），每次A需要B时，调用applicationContext.getBean("B")。

				2. 使用方法注入Method Injection。
		
			Lookup Method Injection

				xml配置：

					java:

						package fiona.apple;

						// no more Spring imports!

						public abstract class CommandManager {

						    public Object process(Object commandState) {
						        // grab a new instance of the appropriate Command interface
						        Command command = createCommand();
						        // set the state on the (hopefully brand new) Command instance
						        command.setState(commandState);
						        return command.execute();
						    }

						    // okay... but where is the implementation of this method?
						    protected abstract Command createCommand();
						}

					xml:
				
						<!-- a stateful bean deployed as a prototype (non-singleton) -->
						<bean id="myCommand" class="fiona.apple.AsyncCommand" scope="prototype">
						    <!-- inject dependencies here as required -->
						</bean>

						<!-- commandProcessor uses statefulCommandHelper -->
						<bean id="commandManager" class="fiona.apple.CommandManager">
						    <lookup-method name="createCommand" bean="myCommand"/>
						</bean>		

				注解实现：

					java:

							public abstract class CommandManager {

							    public Object process(Object commandState) {
							        Command command = createCommand();
							        command.setState(commandState);
							        return command.execute();
							    }

							    @Lookup("myCommand")
							    protected abstract Command createCommand();
							}


						or 
						
							public abstract class CommandManager {

							    public Object process(Object commandState) {
							        MyCommand command = createCommand();
							        command.setState(commandState);
							        return command.execute();
							    }

							    @Lookup
							    protected abstract MyCommand createCommand();
							}	
							
			Arbitrary Method Replacement：
			
				java

					public class MyValueCalculator {

					    public String computeValue(String input) {
					        // some real code...
					    }

					    // some other methods...
					}

					/**
					 * meant to be used to override the existing computeValue(String)
					 * implementation in MyValueCalculator
					 */
					public class ReplacementComputeValue implements MethodReplacer {

					    public Object reimplement(Object o, Method m, Object[] args) throws Throwable {
					        // get the input value, work with it, and return a computed result
					        String input = (String) args[0];
					        ...
					        return ...;
					    }
					}

				xml
				
					<bean id="myValueCalculator" class="x.y.z.MyValueCalculator">
					    <!-- arbitrary method replacement -->
					    <replaced-method name="computeValue" replacer="replacementComputeValue">
					        <arg-type>String</arg-type>
					    </replaced-method>
					</bean>

					<bean id="replacementComputeValue" class="a.b.c.ReplacementComputeValue"/>	

				// done 2020-2-21 20:01:36

	1.5. Bean Scopes	


		Table 3. Bean scopes

		Scope			Description
		singleton 		(Default) Scopes a single bean definition to a single object instance for each Spring IoC container.

		prototype		Scopes a single bean definition to any number of object instances.

		request			Scopes a single bean definition to the lifecycle of a single HTTP request. That is, each HTTP request has its own instance of a bean created off the back of a single bean definition. Only valid in the context of a web-aware Spring ApplicationContext.

		session 		Scopes a single bean definition to the lifecycle of an HTTP Session. Only valid in the context of a web-aware Spring ApplicationContext.

		application 	Scopes a single bean definition to the lifecycle of a ServletContext. Only valid in the context of a web-aware Spring ApplicationContext.

		WebSocket 		Scopes a single bean definition to the lifecycle of a WebSocket. Only valid in the context of a web-aware Spring ApplicationContext.	

		1.5.1. The Singleton Scope

			<bean id="accountService" class="com.something.DefaultAccountService"/>

			<!-- the following is equivalent, though redundant (singleton scope is the default) -->
			<bean id="accountService" class="com.something.DefaultAccountService" scope="singleton"/>	 

			
		1.5.2. The Prototype Scope
		
			<bean id="accountService" class="com.something.DefaultAccountService" scope="prototype"/>

		1.5.3. Singleton Beans with Prototype-bean Dependencies
		
			Method Injection

		1.5.4. Request, Session, Application, and WebSocket Scopes
		
			Initial Web Configuration

				web.xml:

					Servlet 2.5:

						<web-app>
						    ...
						    <listener>
						        <listener-class>
						            org.springframework.web.context.request.RequestContextListener|ServletRequestListener
						        </listener-class>
						    </listener>
						    ...
						</web-app>

					Servlet 3.0+:
					
						WebApplicationInitializer 

					if there are issues:
					
						<web-app>
						    ...
						    <filter>
						        <filter-name>requestContextFilter</filter-name>
						        <filter-class>org.springframework.web.filter.RequestContextFilter</filter-class>
						    </filter>
						    <filter-mapping>
						        <filter-name>requestContextFilter</filter-name>
						        <url-pattern>/*</url-pattern>
						    </filter-mapping>
						    ...
						</web-app>		

			Request scope	

				xml:

					<bean id="loginAction" class="com.something.LoginAction" scope="request"/>

				java:

					@RequestScope
					@Component
					public class LoginAction {
					    // ...
					}		

			Session Scope
			
				xml:

					<bean id="userPreferences" class="com.something.UserPreferences" scope="session"/>

				java:
				
					@SessionScope
					@Component
					public class UserPreferences {
					    // ...
					}

			Application Scope
			
				xml:

					<bean id="appPreferences" class="com.something.AppPreferences" scope="application"/>

				java:	

					@ApplicationScope
					@Component
					public class AppPreferences {
					    // ...
					}

			Scoped Beans as Dependencies

				xml:

					<?xml version="1.0" encoding="UTF-8"?>
					<beans xmlns="http://www.springframework.org/schema/beans"
					    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
					    xmlns:aop="http://www.springframework.org/schema/aop"
					    xsi:schemaLocation="http://www.springframework.org/schema/beans
					        https://www.springframework.org/schema/beans/spring-beans.xsd
					        http://www.springframework.org/schema/aop
					        https://www.springframework.org/schema/aop/spring-aop.xsd">

					    <!-- an HTTP Session-scoped bean exposed as a proxy -->
					    <bean id="userPreferences" class="com.something.UserPreferences" scope="session">
					        <!-- instructs the container to proxy the surrounding bean -->
					        <aop:scoped-proxy/> 
					    </bean>

					    <!-- a singleton-scoped bean injected with a proxy to the above bean -->
					    <bean id="userService" class="com.something.SimpleUserService">
					        <!-- a reference to the proxied userPreferences bean -->
					        <property name="userPreferences" ref="userPreferences"/>
					    </bean>
					</beans>

			Choosing the Type of Proxy to create

				JDK动态代理、CGLIB动态代理详见：5.8. Proxying Mechanisms

					Spring代理机制：

						JDK动态代理基于JDK，CGLIB基于AspectJ，已集成在spring-core包下。

						如果代理对象实现了至少一个接口，则使用JDK动态代理，如果没有实现任何接口，则使用CGLIB。

						问题：

							使用CGLIB时，final修饰的方法不能被代理，因为运行时子类无法重写。

							Spring4.0以后，代理对象构造方法不再会调用两次，因为CGLIB代理实例是通过Objenesis创建的。只有当JVM不允许绕过构造器，spring-support才会两次调用并输出日志。

						如果想强制使用CGLIB代理，可以设置：<aop:config proxy-target-class="true"></aop:config>

						使用注解 @AspectJ 自动代理时如果想强制使用CGLIB代理，可以设置：<aop:aspectj-autoproxy proxy-target-class="true"/>
			
				xml:

					<!-- DefaultUserPreferences implements the UserPreferences interface -->
					<bean id="userPreferences" class="com.stuff.DefaultUserPreferences" scope="session">
					    <aop:scoped-proxy proxy-target-class="false"/>
					</bean>

					<bean id="userManager" class="com.stuff.UserManager">
					    <property name="userPreferences" ref="userPreferences"/>
					</bean>		

		1.5.5. Custom Scopes

			Creating a Custom Scope

				实现接口org.springframework.beans.factory.config.Scope， 

				i.e:

					public class SimpleThreadScope implements Scope {}

			Using a Custom Scope

				注册：

					Scope threadScope = new SimpleThreadScope();
					beanFactory.registerScope("thread", threadScope);
			
				使用：

					xml:

						<bean id="..." class="..." scope="thread">

					i.e:

						<?xml version="1.0" encoding="UTF-8"?>
						<beans xmlns="http://www.springframework.org/schema/beans"
						    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
						    xmlns:aop="http://www.springframework.org/schema/aop"
						    xsi:schemaLocation="http://www.springframework.org/schema/beans
						        https://www.springframework.org/schema/beans/spring-beans.xsd
						        http://www.springframework.org/schema/aop
						        https://www.springframework.org/schema/aop/spring-aop.xsd">

						    <bean class="org.springframework.beans.factory.config.CustomScopeConfigurer">
						        <property name="scopes">
						            <map>
						                <entry key="thread">
						                    <bean class="org.springframework.context.support.SimpleThreadScope"/>
						                </entry>
						            </map>
						        </property>
						    </bean>

						    <bean id="thing2" class="x.y.Thing2" scope="thread">
						        <property name="name" value="Rick"/>
						        <aop:scoped-proxy/>
						    </bean>

						    <bean id="thing1" class="x.y.Thing1">
						        <property name="thing2" ref="thing2"/>
						    </bean>

						</beans>			

			// 2020-2-22 11:48:25			

	1.6. Customizing the Nature of a Bean	

		1.6.1. Lifecycle Callbacks	

			使用Spring提供的特殊接口：				

				InitializingBean#afterPropertiesSet

				DisposableBean#destroy

			不使用Spring提供的特殊接口：

				with JSR-250：  @PostConstruct and @PreDestroy

				without JSR-250:  init-method and destroy-method

			Initialization Callbacks	

				case1: 使用注解@PostConstruct

					java:

						public class ExampleBean {

							@PostConstruct
						    public void init() {
						        // do some initialization work
						    }
						}

				case2: 使用xml配置init-method

					xml:

						<bean id="exampleInitBean" class="examples.ExampleBean" init-method="init"/>

					java:	

						public class ExampleBean {

						    public void init() {
						        // do some initialization work
						    }
						}	

				case3: 实现Spring提供的接口InitializingBean#afterPropertiesSet

					xml:

						<bean id="exampleInitBean" class="examples.AnotherExampleBean"/>

					java:
					
						public class AnotherExampleBean implements InitializingBean {

						    @Override
						    public void afterPropertiesSet() {
						        // do some initialization work
						    }
						}	

			Destruction Callbacks
			
				case1: 使用注解 @PreDestroy

					java:

						public class ExampleBean {

							@PreDestroy
						    public void cleanup() {
						        // do some destruction work (like releasing pooled connections)
						    }
						}

				case2: 使用xml配置destroy-method

					xml:

						<bean id="exampleInitBean" class="examples.ExampleBean" destroy-method="cleanup"/>

					java:	

						public class ExampleBean {

						    public void cleanup() {
						        // do some destruction work (like releasing pooled connections)
						    }
						}	

				case3: 实现Spring提供的接口DisposableBean#destroy

					xml:

						<bean id="exampleInitBean" class="examples.AnotherExampleBean"/>

					java:
					
						public class AnotherExampleBean implements DisposableBean {

						    @Override
						    public void destroy() {
						        // do some destruction work (like releasing pooled connections)
						    }
						}

			Default Initialization and Destroy Methods
			
				xml:

					<beans default-init-method="init">

					    <bean id="blogService" class="com.something.DefaultBlogService">
					        <property name="blogDao" ref="blogDao" />
					    </bean>

					</beans>

				java:		

					public class DefaultBlogService implements BlogService {

					    private BlogDao blogDao;

					    public void setBlogDao(BlogDao blogDao) {
					        this.blogDao = blogDao;
					    }

					    // this is (unsurprisingly) the initialization callback method
					    public void init() {
					        if (this.blogDao == null) {
					            throw new IllegalStateException("The [blogDao] property must be set.");
					        }
					    }
					}	

			Combining Lifecycle Mechanisms
			
				As of Spring 2.5, you have three options for controlling bean lifecycle behavior:

					The InitializingBean and DisposableBean callback interfaces

					Custom init() and destroy() methods

					The @PostConstruct and @PreDestroy annotations. You can combine these mechanisms to control a given bean.	

				tips：
					
					如果一个bean有多个以上配置信息，如果每个配置的方法名不同，则按下边的顺序依次执行。 如果方法名相同，则该方法只执行一次。

				Multiple lifecycle mechanisms configured for the same bean, with different initialization methods, are called as follows:

					Methods annotated with @PostConstruct

					afterPropertiesSet() as defined by the InitializingBean callback interface

					A custom configured init() method

				Destroy methods are called in the same order:

					Methods annotated with @PreDestroy

					destroy() as defined by the DisposableBean callback interface

					A custom configured destroy() method	

			Startup and Shutdown Callbacks

				Lifecycle 				// 生命周期接口

				LifecycleProcessor 		// 生命周期处理器接口（默认实现 DefaultLifecycleProcessor）

				SmartLifecycle 			// 灵活的生命周期（extends extends Lifecycle, Phased）

				Phased					// 阶段的（可用于生命周期管理）
			
			Shutting Down the Spring IoC Container Gracefully in Non-Web Applications	

				如果在Spring非web容器中，可以通过JVM注册ShutdownHook[关闭钩子]来确保优雅的关闭并调用单例bean上的destroy方法释放资源。必须正确配置和实现destroy的回调。

				可以调用接口ConfigurableApplicationContext#registerShutdownHook()来注册ShutdownHook。

				i.e:

					import org.springframework.context.ConfigurableApplicationContext;
					import org.springframework.context.support.ClassPathXmlApplicationContext;

					public final class Boot {

					    public static void main(final String[] args) throws Exception {
					        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");

					        // add a shutdown hook for the above context...
					        ctx.registerShutdownHook();

					        // app runs here...

					        // main method exits, hook is called prior to the app shutting down...
					    }
					}
					
		1.6.2. ApplicationContextAware and BeanNameAware

		1.6.3. Other Aware Interfaces				

			Table 4. Aware interfaces

			Name									Injected Dependency											Explained in…​

			ApplicationContextAware 				Declaring ApplicationContext. 								ApplicationContextAware and BeanNameAware

			ApplicationEventPublisherAware 			Event publisher of the enclosing ApplicationContext. 		Additional Capabilities of the ApplicationContext

			BeanClassLoaderAware 					Class loader used to load the bean classes. 				Instantiating Beans

			BeanFactoryAware 						Declaring BeanFactory. 										ApplicationContextAware and BeanNameAware

			BeanNameAware 						    Name of the declaring bean. 								ApplicationContextAware and BeanNameAware

			BootstrapContextAware 					Resource adapter BootstrapContext the container runs in. 	JCA CCI
													Typically available only in JCA-aware ApplicationContext instances.

			LoadTimeWeaverAware 					Defined weaver for processing class definition at load time. Load-time Weaving with AspectJ in the Spring Framework

			MessageSourceAware 						Configured strategy for resolving messages 					Additional Capabilities of the ApplicationContext
													(with support for parametrization and internationalization).

			NotificationPublisherAware 				Spring JMX notification publisher. 							Notifications

			ResourceLoaderAware 					Configured loader for low-level access to resources. 		Resources

			ServletConfigAware 						Current ServletConfig the container runs in. 				Spring MVC
													Valid only in a web-aware Spring ApplicationContext.

			ServletContextAware 					Current ServletContext the container runs in. 				Spring MVC
													Valid only in a web-aware Spring ApplicationContext.

	1.7. Bean Definition Inheritance

		Bean定义信息相关类：
	
			ChildBeanDefinition 	子类的Bean定义信息

			RootBeanDefinition		基类的Bean定义信息

			GenericBeanDefinition 	通用的Bean定义信息

		i.e:

			<bean id="inheritedTestBean" abstract="true" class="org.springframework.beans.TestBean">
			    <property name="name" value="parent"/>
			    <property name="age" value="1"/>
			</bean>

			<bean id="inheritsWithDifferentClass" class="org.springframework.beans.DerivedTestBean" parent="inheritedTestBean" init-method="initialize">  
			    <property name="name" value="override"/>
			    <!-- the age property value of 1 will be inherited from parent -->
			</bean>

			<bean id="inheritedTestBeanWithoutClass" abstract="true">
			    <property name="name" value="parent"/>
			    <property name="age" value="1"/>
			</bean>

			<bean id="inheritsWithClass" class="org.springframework.beans.DerivedTestBean" parent="inheritedTestBeanWithoutClass" init-method="initialize">
			    <property name="name" value="override"/>
			    <!-- age will inherit the value of 1 from the parent bean definition-->
			</bean>

		tips:

			这里由于父级bean，只是用于bean模板定义信息，并不能完全初始化，所以必须标记为abstract="true"。这样ApplicationContext容器在预初始化单例bean阶段，才不会去初始化父级bean。

	1.8. Container Extension Points

		1.8.1. Customizing Beans by Using a BeanPostProcessor

			顾名思义： Bean后置处理器，对bean进行后置处理。

			可以通过添加BeanPostProcessor实现类，在容器完成bean的实例化、配置、初始化后，添加特殊的逻辑。

			Example: Hello World, BeanPostProcessor-style

				java:

					package scripting;

					import org.springframework.beans.factory.config.BeanPostProcessor;

					public class InstantiationTracingBeanPostProcessor implements BeanPostProcessor {

					    // simply return the instantiated bean as-is
					    public Object postProcessBeforeInitialization(Object bean, String beanName) {
					        return bean; // we could potentially return any object reference here...
					    }

					    public Object postProcessAfterInitialization(Object bean, String beanName) {
					        System.out.println("Bean '" + beanName + "' created : " + bean.toString());
					        return bean;
					    }
					}

				xml:
				
					<?xml version="1.0" encoding="UTF-8"?>
					<beans xmlns="http://www.springframework.org/schema/beans"
					    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
					    xmlns:lang="http://www.springframework.org/schema/lang"
					    xsi:schemaLocation="http://www.springframework.org/schema/beans
					        https://www.springframework.org/schema/beans/spring-beans.xsd
					        http://www.springframework.org/schema/lang
					        https://www.springframework.org/schema/lang/spring-lang.xsd">

					    <lang:groovy id="messenger" script-source="classpath:org/springframework/scripting/groovy/Messenger.groovy">
					        <lang:property name="message" value="Fiona Apple Is Just So Dreamy."/>
					    </lang:groovy>

					    <!--
					    when the above bean (messenger) is instantiated, this custom
					    BeanPostProcessor implementation will output the fact to the system console
					    -->
					    <bean class="scripting.InstantiationTracingBeanPostProcessor"/>

					</beans>	
	
			Example: The RequiredAnnotationBeanPostProcessor

		1.8.2. Customizing Configuration Metadata with a BeanFactoryPostProcessor	

			顾名思义： BeanFactory后置处理器，对beanFactory进行后置处理。

			Spring IOC容器会在实例化bean之前（除了BeanFactoryPostProcessor本身），允许BeanFactoryPostProcessor读取并修改配置信息。

			Example: The Class Name Substitution PropertySourcesPlaceholderConfigurer

				xml:

					<bean class="org.springframework.context.support.PropertySourcesPlaceholderConfigurer">
					    <property name="locations" value="classpath:com/something/jdbc.properties"/>
					</bean>

					<bean id="dataSource" destroy-method="close" class="org.apache.commons.dbcp.BasicDataSource">
					    <property name="driverClassName" value="${jdbc.driverClassName}"/>
					    <property name="url" value="${jdbc.url}"/>
					    <property name="username" value="${jdbc.username}"/>
					    <property name="password" value="${jdbc.password}"/>
					</bean>

				如果使用Spring2.5+的context命名空间：

					<context:property-placeholder location="classpath:com/something/jdbc.properties"/>

			Example: The PropertyOverrideConfigurer
			
				format:

					beanName.property=value

				i.e:			

					dataSource.driverClassName=com.mysql.jdbc.Driver
					dataSource.url=jdbc:mysql:mydb

				如果使用Spring2.5+的context命名空间：	

					<context:property-override location="classpath:override.properties"/>


		1.8.3. Customizing Instantiation Logic with a FactoryBean	

			可以通过实现org.springframework.beans.factory.FactoryBean接口，实现一个对象本身的工厂。

			FactoryBean接口可以实现pring容器实例化逻辑的可插拔性。

			如果我们初始化bean的逻辑很复杂，在java中实现要比在xml中配置更好，可以自定义一个FactoryBean，将复杂的逻辑写到class中，然后再将此FactoryBean添加到Spring容器中。

			The FactoryBean interface provides three methods:

				Object getObject(): Returns an instance of the object this factory creates. The instance can possibly be shared, depending on whether this factory returns singletons or prototypes.

				boolean isSingleton(): Returns true if this FactoryBean returns singletons or false otherwise.

				Class getObjectType(): Returns the object type returned by the getObject() method or null if the type is not known in advance.

			假设bean名称为myBean，则

				applicationContext.getBean("&myBean");  // 获取的是容器中创建"myBean"的FactoryBean

				applicationContext.getBean("myBean");   // 获取的是名为"myBean"的bean，也即该factoryBean.getObject()的返回值。

			// done 2020-2-22 19:05:47	

	1.9. Annotation-based Container Configuration		

		tips:

			注解注入会在XML注入前执行，这样就导致，如果一个属性配置同时使用了这两种途径进行注入，那XML中的配置会覆盖注解的配置信息。

		注解开启方式：

			1. 以特殊的bean定义信息进行注册。

			2. xml中添加如下配置：

				<?xml version="1.0" encoding="UTF-8"?>
				<beans xmlns="http://www.springframework.org/schema/beans"
				    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				    xmlns:context="http://www.springframework.org/schema/context"
				    xsi:schemaLocation="http://www.springframework.org/schema/beans
				        https://www.springframework.org/schema/beans/spring-beans.xsd
				        http://www.springframework.org/schema/context
				        https://www.springframework.org/schema/context/spring-context.xsd">

				    <context:annotation-config/>

				</beans>

			同时会注册的后置处理器包括：
				AutowiredAnnotationBeanPostProcessor, 
				CommonAnnotationBeanPostProcessor, 
				PersistenceAnnotationBeanPostProcessor,
				RequiredAnnotationBeanPostProcessor.

		1.9.1. @Required
		
			作用于bean属性的setter方法，标识该属性必须在配置期间进行填充，如果没有通过明确或注入属性值，容器会抛出异常。

			推荐的做法是在bean类中自行添加断言进行必输判断/初始化，这样即时在容器外部程序也可以正常运行。

			Spring Framework 5.1之后 @Requied 不再推荐使用，更推荐使用构造器注入形式注入必输属性（或者通过实现InitializingBean.afterPropertiesSet()来注入属性）

		1.9.2. Using @Autowired
		
			@Autowired[Spring] == @Inject[JSR 330]

			构造器：

				public class MovieRecommender {

				    private final CustomerPreferenceDao customerPreferenceDao;

				    @Autowired
				    public MovieRecommender(CustomerPreferenceDao customerPreferenceDao) {
				        this.customerPreferenceDao = customerPreferenceDao;
				    }

				    // ...
				}

			setter方法：
			
				public class SimpleMovieLister {

				    private MovieFinder movieFinder;

				    @Autowired
				    public void setMovieFinder(MovieFinder movieFinder) {
				        this.movieFinder = movieFinder;
				    }

				    // ...
				}

			任意方法：
			
				public class MovieRecommender {

				    private MovieCatalog movieCatalog;

				    private CustomerPreferenceDao customerPreferenceDao;

				    @Autowired
				    public void prepare(MovieCatalog movieCatalog,
				            CustomerPreferenceDao customerPreferenceDao) {
				        this.movieCatalog = movieCatalog;
				        this.customerPreferenceDao = customerPreferenceDao;
				    }

				    // ...
				}		

			多种方式混合使用：	

				public class MovieRecommender {

				    private final CustomerPreferenceDao customerPreferenceDao;

				    @Autowired
				    private MovieCatalog movieCatalog;

				    @Autowired
				    public MovieRecommender(CustomerPreferenceDao customerPreferenceDao) {
				        this.customerPreferenceDao = customerPreferenceDao;
				    }

				    // ...
				}

			数组类型（容器会注入该类型的所有bean）： 

				public class MovieRecommender {

				    @Autowired
				    private MovieCatalog[] movieCatalogs;

				    // ...
				}

			集合Set（同数组）：	

				public class MovieRecommender {

				    private Set<MovieCatalog> movieCatalogs;

				    @Autowired
				    public void setMovieCatalogs(Set<MovieCatalog> movieCatalogs) {
				        this.movieCatalogs = movieCatalogs;
				    }

				    // ...
				}

			Map映射（注入该类型的所有bean，key: bean names, value: bean type）：

				public class MovieRecommender {

				    private Map<String, MovieCatalog> movieCatalogs;

				    @Autowired
				    public void setMovieCatalogs(Map<String, MovieCatalog> movieCatalogs) {
				        this.movieCatalogs = movieCatalogs;
				    }

				    // ...
				}

			使用request属性标识该bean非必需：

				public class SimpleMovieLister {

				    private MovieFinder movieFinder;

				    @Autowired(required = false)
				    public void setMovieFinder(MovieFinder movieFinder) {
				        this.movieFinder = movieFinder;
				    }

				    // ...
				}

			Java 8’s java.util.Optional：

				public class SimpleMovieLister {

				    @Autowired
				    public void setMovieFinder(Optional<MovieFinder> movieFinder) {
				        ...
				    }
				}

			注解	@Nullable：

				public class SimpleMovieLister {

				    @Autowired
				    public void setMovieFinder(@Nullable MovieFinder movieFinder) {
				        ...
				    }
				}

			tips:
			
				可以使用@Autowired注入容器内部的一些公开bean，以及他们的扩展接口，如：

					BeanFactory, ApplicationContext, Environment, ResourceLoader, ApplicationEventPublisher, and MessageSource	

					ConfigurableApplicationContext or ResourcePatternResolver

				Autowired, @Inject, @Value, and @Resource在容器中由BeanPostProcessor实现类来处理，即不可以在自定义的BeanPostProcessor或者BeanFactoryPostProcessor上使用这些注解。


		1.9.3. Fine-tuning Annotation-based Autowiring with @Primary

			根据类型注入时，可能会有多个同类型的候选bean，所以有时候需要进行优先级的排列，此时，可以通过@Primary注解来实现。

				java：

					@Configuration
					public class MovieConfiguration {

					    @Bean
					    @Primary
					    public MovieCatalog firstMovieCatalog() { ... }

					    @Bean
					    public MovieCatalog secondMovieCatalog() { ... }

					    // ...
					}	

					public class MovieRecommender {

					    @Autowired
					    private MovieCatalog movieCatalog;		// 此时，注入时使用的是上边的firstMovieCatalog方法。

					    // ...
					}
			
				xml：			

					<?xml version="1.0" encoding="UTF-8"?>
					<beans xmlns="http://www.springframework.org/schema/beans"
					    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
					    xmlns:context="http://www.springframework.org/schema/context"
					    xsi:schemaLocation="http://www.springframework.org/schema/beans
					        https://www.springframework.org/schema/beans/spring-beans.xsd
					        http://www.springframework.org/schema/context
					        https://www.springframework.org/schema/context/spring-context.xsd">

					    <context:annotation-config/>

					    <bean class="example.SimpleMovieCatalog" primary="true">
					        <!-- inject any dependencies required by this bean -->
					    </bean>

					    <bean class="example.SimpleMovieCatalog">
					        <!-- inject any dependencies required by this bean -->
					    </bean>

					    <bean id="movieRecommender" class="example.MovieRecommender"/>

					</beans>

		1.9.4. Fine-tuning Annotation-based Autowiring with Qualifiers		

			根据类型注入时，如果可以确定只有一个主候选bean时，使用@Primary是可以实现注入的。 如果需要对候选bean进行进一步筛选时，可以使用@Qualifier注解，
			可以给qualifier的value值设置一个参数，从而在根据类型注入时，实现从从多个候选bean中根据设置的参数来进行选择注入。

			i.e:

				java:

					public class MovieRecommender {

					    @Autowired
					    @Qualifier("main")
					    private MovieCatalog movieCatalog;

					    // ...
					}

				xml:
				
					<?xml version="1.0" encoding="UTF-8"?>
					<beans xmlns="http://www.springframework.org/schema/beans"
					    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
					    xmlns:context="http://www.springframework.org/schema/context"
					    xsi:schemaLocation="http://www.springframework.org/schema/beans
					        https://www.springframework.org/schema/beans/spring-beans.xsd
					        http://www.springframework.org/schema/context
					        https://www.springframework.org/schema/context/spring-context.xsd">

					    <context:annotation-config/>

					    <bean class="example.SimpleMovieCatalog">
					        <qualifier value="main"/> 

					        <!-- inject any dependencies required by this bean -->
					    </bean>

					    <bean class="example.SimpleMovieCatalog">
					        <qualifier value="action"/> 

					        <!-- inject any dependencies required by this bean -->
					    </bean>

					    <bean id="movieRecommender" class="example.MovieRecommender"/>

					</beans>	

			@Autowired applies to fields, constructors, and multi-argument methods, allowing for narrowing through qualifier annotations at the parameter level.  
			In contrast, @Resource is supported only for fields and bean property setter methods with a single argument. 
			As a consequence, you should stick with qualifiers if your injection target is a constructor or a multi-argument method.	

			@Autowired作用于字段，构造器，多参数方法，可以通过在参数上添加@Qualifier注解来缩小注入范围。
			相比，@Resource只用于字段，单个参数的setter方法。
			因此，当注入的对象是一个构造器或者多参数方法时，须使用@Qualifier

			// done 2020-2-23 12:24:37

		1.9.5. Using Generics as Autowiring Qualifiers	










		




			





			

			










	














