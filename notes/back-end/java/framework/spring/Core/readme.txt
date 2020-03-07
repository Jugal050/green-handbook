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
			因此，当注入的对象是一个构造器或者多参数方法时，须使用@Qualifier（+@Autowired）

			// done 2020-2-23 12:24:37

			@Autowired注解注入流程：（后置处理器： AutowiredAnnotationBeanPostProcessor[在spring-bean包下]）

				AbstractBeanFactory.getBean()
				AbstractBeanFactory.doGetBean()
				AbstractAutowireCapableBeanFactory.createBean()
				AbstractAutowireCapableBeanFactory.doCreateBean()
				AbstractAutowireCapableBeanFactory.populateBean()
				AutowiredAnnotationBeanPostProcessor.postProcessProperties()
				InjectionMetadata.inject()
				AutowiredFieldElement.inject() 		// 如果注解@Autowired加在字段上，使用此方法注入
				AutowiredMethodElement.inject()		// 如果注解@Autowired加在方法上，使用此方法注入

				其中：

					// 属性后置处理
					AutowiredAnnotationBeanPostProcessor.postProcessProperties

						// 查找相关注解（包括：org.springframework.beans.factory.annotation.Autowired， org.springframework.beans.factory.annotation.Value, javax.inject.Inject）
						AutowiredAnnotationBeanPostProcessor.findAutowiringMetadata

						// 属性注入
						InjectionMetadata.inject
							AutowiredFieldElement.inject() 		// 如果注解加在字段上，使用此方法注入
							AutowiredMethodElement.inject()		// 如果注解加在方法上，使用此方法注入


			@Resource注解注入流程：（后置处理器： CommonAnnotationBeanPostProcessor[在spring-context包下]）	

				AbstractBeanFactory.getBean(AbstractBeanFactory.java:202)
				AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:321)
				DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:222)
				AbstractBeanFactory$$Lambda$269.1401702503.getObject(Unknown Source:-1)
				AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:323)
				AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:517)
				AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:594)
				AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1422)
				CommonAnnotationBeanPostProcessor.postProcessProperties(CommonAnnotationBeanPostProcessor.java:334)
				InjectionMetadata.inject(InjectionMetadata.java:133)
				InjectionMetadata.InjectedElement#inject(InjectionMetadata.java:233)

				
				其中：

					// 属性后置处理
					CommonAnnotationBeanPostProcessor.postProcessProperties

						// 查找相关注解(包括：javax.xml.ws.WebServiceRef, javax.ejb.EJB, javax.annotation.Resource):
						CommonAnnotationBeanPostProcessor.findResourceMetadata

						// 属性注入
						InjectionMetadata.inject
							InjectionMetadata.InjectedElement.inject

		1.9.5. Using Generics as Autowiring Qualifiers	

		1.9.6. Using CustomAutowireConfigurer

			<bean id="customAutowireConfigurer" class="org.springframework.beans.factory.annotation.CustomAutowireConfigurer">
			    <property name="customQualifierTypes">
			        <set>
			            <value>example.CustomQualifier</value>
			        </set>
			    </property>
			</bean>	

			The AutowireCandidateResolver determines autowire candidates by:

				The autowire-candidate value of each bean definition

				Any default-autowire-candidates patterns available on the <beans/> element

				The presence of @Qualifier annotations and any custom annotations registered with the CustomAutowireConfigurer

		1.9.7. Injection with @Resource
		
			如果指定了name，Spring会根据名称查找相关bean并注入。

			如果未指定name，则与@Autowried相似，Spring会先根据名称（字段/参数/setter方法名称）查找，如果查找不到，则会根据类型查找。

			i.e:

				public class SimpleMovieLister {

				    private MovieFinder movieFinder;

				    @Resource(name="myMovieFinder") 
				    public void setMovieFinder(MovieFinder movieFinder) {
				        this.movieFinder = movieFinder;
				    }
				}

				public class MovieRecommender {

				    @Resource
				    private CustomerPreferenceDao customerPreferenceDao;  // 先查找名称为"customerPreferenceDao"的bean，查不到则查找类型为"CustomerPreferenceDao"的bean

				    @Resource
				    private ApplicationContext context; 

				    public MovieRecommender() {
				    }

				    // ...
				}

		1.9.8. Using @Value

			作用：用于注入外部的配置属性

			相关：

				-- 属性源通配符配置类：PropertySourcesPlaceholderConfigurer 

					可以通过setPlaceholderPrefix, setPlaceholderSuffix等方法设置通配符前缀、后缀等。

					@Configuration
					public class AppConfig {

					     @Bean
					     public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
					           return new PropertySourcesPlaceholderConfigurer();
					     }
					}

					tips: 

						When configuring a PropertySourcesPlaceholderConfigurer using JavaConfig, the @Bean method must be static.
						使用JavaConfi的形式配置PropertySourcesPlaceholderConfigurer，@Bean必须是static的

				-- 支持简单类型转换（如 Integer -> int, 逗号分隔的值 转换为 数组）
				
				-- 支持添加默认值，如：

					@Component
					public class MovieRecommender {

					    private final String catalog;

					    public MovieRecommender(@Value("${catalog.name:defaultCatalog}") String catalog) {
					        this.catalog = catalog;
					    }
					}

				-- 类型转换接口[BeanPostProcessor处理阶段]： ConversionService 

					@Configuration
					public class AppConfig {

					    @Bean
					    public ConversionService conversionService() {
					        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
					        conversionService.addConverter(new MyCustomConverter());
					        return conversionService;
					    }
					}

				-- 支持SpEL表达式，如：

					 @Value("#{systemProperties['user.catalog'] + 'Catalog' }") String catalog

					 @Value("#{{'Thriller': 100, 'Comedy': 300}}") Map<String, Integer> countOfMoviesPerCatalog


			i.e:

				java:	

					@Component
					public class MovieRecommender {

					    private final String catalog;

					    public MovieRecommender(@Value("${catalog.name}") String catalog) {
					        this.catalog = catalog;
					    }
					}

					@Configuration
					@PropertySource("classpath:application.properties")
					public class AppConfig { }

				properties:
				
					catalog.name=MovieCatalog


		1.9.9. Using @PostConstruct and @PreDestroy

			与@Resource一样，也是通过CommonAnnotationBeanPostProcessor接口来处理。

			tips:

				这些注解为JDK6-8提供的，JDK9中整个javax.annotation包从核心模块中隔离，在JDK11中已完全移除，如果使用的时候，如果jdk版本不一致，可以像maven管理其他包一样手动导入注解包。

			i.e:

				public class CachingMovieLister {

				    @PostConstruct
				    public void populateMovieCache() {
				        // populates the movie cache upon initialization...
				    }

				    @PreDestroy
				    public void clearMovieCache() {
				        // clears the movie cache upon destruction...
				    }
				}

		// done 2020-2-23 18:41:42
		
	1.10. Classpath Scanning and Managed Components			

		1.10.1. @Component and Further Stereotype Annotations

			@Component 任何Spring管理的组件都可以使用的通用构造型注解。

			@Repository 即平常所说的DAO，用于与数据库访问相关的持久层

			@Service 服务层

			@Controller 视图层

			tips:

				可以在任何组件上添加注解@Component，但是，使用其他注解如@Repository，@Service，@Controller，能更准确地标识该组件的意图，
				而且，Spring Framework以后有可能为这些注解添加更多的附加功能，比如，@Respository已经支持持久层的异常自动转换。

		1.10.2. Using Meta-annotations and Composed Annotations

			Spring提供的很多注解都可以看做是元注解（即可以在别的注解上进行注解使用）。比如：

			@Service

				@Target(ElementType.TYPE)
				@Retention(RetentionPolicy.RUNTIME)
				@Documented
				@Component 
				public @interface Service {

				    // ...
				}

			@SessionScope	

				@Target({ElementType.TYPE, ElementType.METHOD})
				@Retention(RetentionPolicy.RUNTIME)
				@Documented
				@Scope(WebApplicationContext.SCOPE_SESSION)
				public @interface SessionScope {

				    /**
				     * Alias for {@link Scope#proxyMode}.
				     * <p>Defaults to {@link ScopedProxyMode#TARGET_CLASS}.
				     */
				    @AliasFor(annotation = Scope.class)
				    ScopedProxyMode proxyMode() default ScopedProxyMode.TARGET_CLASS;

				}

		1.10.3. Automatically Detecting Classes and Registering Bean Definitions
		
			自动扫描配置：		

				java:

					@Configuration
					@ComponentScan(basePackages = "org.example")
					public class AppConfig  {
					    // ...
					}

					tips:

						如果有多个package，可以用逗号，分号，空格分隔。

				xml:
				
					<?xml version="1.0" encoding="UTF-8"?>
					<beans xmlns="http://www.springframework.org/schema/beans"
					    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
					    xmlns:context="http://www.springframework.org/schema/context"
					    xsi:schemaLocation="http://www.springframework.org/schema/beans
					        https://www.springframework.org/schema/beans/spring-beans.xsd
					        http://www.springframework.org/schema/context
					        https://www.springframework.org/schema/context/spring-context.xsd">

					    <context:component-scan base-package="org.example"/>

					</beans>	

					tips:

						<context:component-scan>配置会自动启用<context:annotation-config>的功能，所以有<context:component-scan>时，可以省略<context:annotation-config>

						当使用<context:component-scan>时，容器会自动注册AutowiredAnnotationBeanPostProcessor和CommonAnnotationBeanPostProcessor，即使没有进行相关配置。
						不过可以通过配置<context:annotation-config value="false">，来取消这两个后置处理器的注册。

		1.10.4. Using Filters to Customize Scanning			

			默认情况下， 包括@Component, @Repository, @Service, @Controller, @Configuration以及自定义的注解，这些注解修饰的类容器会自动检测并注册为对应的bean。
			但是，我们可以自定义过滤规则来修改或者扩展默认行为。
			可以使用@ComponentScan(useDefaultFilters = "false") | <context:component-scan use-default-filters="false">来取消这些注解的自动检测和注册行为。

			i.e：（改配置会使容器忽略@Repository，而使用stub包下的repository）

				java:

					@Configuration
					@ComponentScan(basePackages = "org.example",
					        includeFilters = @Filter(type = FilterType.REGEX, pattern = ".*Stub.*Repository"),
					        excludeFilters = @Filter(Repository.class))
					public class AppConfig {
					    ...
					}

				xml:

					<beans>
					    <context:component-scan base-package="org.example">
					        <context:include-filter type="regex" expression=".*Stub.*Repository"/>
					        <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Repository"/>
					    </context:component-scan>
					</beans>

			// done 2020-2-24 12:38:00		

		1.10.5. Defining Bean Metadata within Components

			Spring组件可以为容器提供Bean定义信息。 可以通过在有配置注解（如Component, Comfiguration, Service, Repository, Controller..）的类上添加注解@Bean实现，如：

				java:

					@Component
					public class FactoryMethodComponent {

					    @Bean
					    @Qualifier("public")
					    public TestBean publicInstance() {
					        return new TestBean("publicInstance");
					    }

					    public void doWork() {
					        // Component method implementation omitted
					    }
					}

			@Bean修饰的自动注入方法，同样支持字段/方法的自动注入。如：

				java:

					@Component
					public class FactoryMethodComponent {

					    private static int i;

					    @Bean
					    @Qualifier("public")
					    public TestBean publicInstance() {
					        return new TestBean("publicInstance");
					    }

					    // use of a custom qualifier and autowiring of method parameters
					    @Bean
					    protected TestBean protectedInstance(
					            @Qualifier("public") TestBean spouse,
					            @Value("#{privateInstance.age}") String country) {
					        TestBean tb = new TestBean("protectedInstance", 1);
					        tb.setSpouse(spouse);
					        tb.setCountry(country);
					        return tb;
					    }

					    @Bean
					    private TestBean privateInstance() {
					        return new TestBean("privateInstance", i++);
					    }

					    @Bean
					    @RequestScope
					    public TestBean requestScopedInstance() {
					        return new TestBean("requestScopedInstance", 3);
					    }
					}

			Spring4.3之后，可以通过在参数中声明类型为InjectionPoint(或者其子类DependencyDescriptor)	，获取触发当前bean创建的的请求注入节点。
			但是，这只适用于普通的bean创建过程，并不适用于已存在的实例注入。所以，适用于大多数scope为prototype的bean，对于其他scope类型，只会在其scope范围内有效。

				java:

					@Component
					public class FactoryMethodComponent {

					    @Bean @Scope("prototype")
					    public TestBean prototypeInstance(InjectionPoint injectionPoint) {
					        return new TestBean("prototypeInstance for " + injectionPoint.getMember());
					    }
					}


			tips:

				@Component与@Configuration修饰的类中，@Bean修饰的方法执行方式有区别。

				@Component修饰的类中，@Bean修饰的方法/字段被调用时，只是简单的java程序中的直接调用，不会被CGLIB拦截。

				@Comfiguration修饰的类中，@Bean修饰的方法/字段被调用时，为了生命周期管理以及Spring Bean的代理，会通过Spring容器中CGLIB为其生成的代理对象执行。

				当@Bean修饰的方法是static类型时，由于CGLIB子类只能重载非static方法，所以此时即时是在@Configuration修饰的类中，该方法调用也不会被Spring容器拦截。

				因为@Bean方法的可见性修饰符对Spring容器在处理bean定义信息不会有直接影响，所以我们可以在任何地方（如static方法，没有@Configuration注解的类中）添加@Bean注解，
				但是，常规的@Configuration中的@Bean方法需要被重载，也就不能被声明为private或者是final类型的。

				@Conponent/@Configuration修饰的类的基类/父类中，@Bean方法也会被检测到，包括Java8中接口的默认default实现方法（如果该接口被@Component/@Configuraiton修饰的类实现）。

				一个类中可能有一个bean有多个@Bean修饰的方法，类似贪心算法/有多个@Autowired修饰的构造器的情况，在构造bean时，同样条件下参数最多的方法会被调用。

		1.10.6. Naming Autodetected Components

			BeanName生成规则接口： BeanNameGenerator

			相关实现类：

				默认实现： org.springframework.beans.factory.support.DefaultBeanNameGenerator

				注解组件相关实现类： org.springframework.context.annotation.AnnotationBeanNameGenerator

				全限定注解组件相关实现类（AnnotationBeanNameGenerator子类）：org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator

			如果需要自定义beanName的生成规则。则可以实现接口BeanNameGenerator，确保包含一个默认的无参构造方法，然后配置扫描scanner时提供该配置类的完整全称即可。

				java:

					@Configuration
					@ComponentScan(basePackages = "org.example", nameGenerator = MyNameGenerator.class)
					public class AppConfig {
					    // ...
					}

				xml:

					<beans>
					    <context:component-scan base-package="org.example"
					        name-generator="org.example.MyNameGenerator" />
					</beans>

		1.10.7. Providing a Scope for Autodetected Components
		
			Component组件默认scope是singleton，如果想设置为其他范围，可以使用@Scope组件，如：

				@Scope("prototype")
				@Repository
				public class MovieFinderImpl implements MovieFinder {
				    // ...
				}

			也可以自定义scope（比较容易，用法与现有的一直），或者自定义scope解析器：	

				java:

					@Configuration
					@ComponentScan(basePackages = "org.example", scopeResolver = MyScopeResolver.class)
					public class AppConfig {
					    // ...
					}

				xml:
					
					<beans>
					    <context:component-scan base-package="org.example" scope-resolver="org.example.MyScopeResolver"/>
					</beans>

			如果使用的scope范围不是singleton类型，有必要设置对象生成的代理类型scopeProxy，可选值为：no(不使用代理), interfaces（JDK代理）, targetClass（CGLIB），如：

				java:

					@Configuration
					@ComponentScan(basePackages = "org.example", scopedProxy = ScopedProxyMode.INTERFACES)
					public class AppConfig {
					    // ...
					}

				xml:

					<beans>
					    <context:component-scan base-package="org.example" scoped-proxy="interfaces"/>
					</beans>


		1.10.8. Providing Qualifier Metadata with Annotations		

			与xml配置对应：

				@Component
				@Qualifier("Action")
				public class ActionMovieCatalog implements MovieCatalog {
				    // ...
				}

				@Component
				@Genre("Action")
				public class ActionMovieCatalog implements MovieCatalog {
				    // ...
				}

				@Component
				@Offline
				public class CachingMovieCatalog implements MovieCatalog {
				    // ...
				}

		1.10.9. Generating an Index of Candidate Components		

			由于类路径扫描很快，可以通过在编译期创建一个静态的候选名单来提升启动性能。 此模式下，所有组件扫描的模块都将被应用到。

			maven配置:

				<dependencies>
				    <dependency>
				        <groupId>org.springframework</groupId>
				        <artifactId>spring-context-indexer</artifactId>
				        <version>5.2.3.RELEASE</version>
				        <optional>true</optional>
				    </dependency>
				</dependencies>

			如果类路径下存在：META-INF/spring.components，索引会自动启用，可以通过设置spring.index.ignore = true关闭（spring.properties/system property）。

			// done 2020-2-24 18:52:02

	1.11. Using JSR 330 Standard Annotations

		maven:

			<dependency>
			    <groupId>javax.inject</groupId>
			    <artifactId>javax.inject</artifactId>
			    <version>1</version>
			</dependency>
	
		1.11.1. Dependency Injection with @Inject and @Named	

			== @Autowired

			java:

				import javax.inject.Inject;

				public class SimpleMovieLister {

				    private MovieFinder movieFinder;

				    @Inject
				    public void setMovieFinder(MovieFinder movieFinder) {
				        this.movieFinder = movieFinder;
				    }

				    public void listMovies() {
				        this.movieFinder.findMovies(...);
				        // ...
				    }
				}	

				import javax.inject.Inject;
				import javax.inject.Provider;

				public class SimpleMovieLister {

				    private Provider<MovieFinder> movieFinder;

				    @Inject
				    public void setMovieFinder(Provider<MovieFinder> movieFinder) {
				        this.movieFinder = movieFinder;
				    }

				    public void listMovies() {
				        this.movieFinder.get().findMovies(...);
				        // ...
				    }
				}

				import javax.inject.Inject;
				import javax.inject.Named;

				public class SimpleMovieLister {

				    private MovieFinder movieFinder;

				    @Inject
				    public void setMovieFinder(@Named("main") MovieFinder movieFinder) {
				        this.movieFinder = movieFinder;
				    }

				    // ...
				}

				public class SimpleMovieLister {

				    @Inject
				    public void setMovieFinder(Optional<MovieFinder> movieFinder) {
				        // ...
				    }
				}

				public class SimpleMovieLister {

				    @Inject
				    public void setMovieFinder(@Nullable MovieFinder movieFinder) {
				        // ...
				    }
				}

		1.11.2. @Named and @ManagedBean: Standard Equivalents to the @Component Annotation	

			java:

				import javax.inject.Inject;
				import javax.inject.Named;

				@Named("movieListener")  // @ManagedBean("movieListener") could be used as well
				public class SimpleMovieLister {

				    private MovieFinder movieFinder;

				    @Inject
				    public void setMovieFinder(MovieFinder movieFinder) {
				        this.movieFinder = movieFinder;
				    }

				    // ...
				}

				@Configuration
				@ComponentScan(basePackages = "org.example")
				public class AppConfig  {
				    // ...
				}

			tips:
			
				与@Component不同，@Named和ManagedBean不能组合使用。如果想自定义组件注解，需要使用Spring的结构模型。

		1.11.3. Limitations of JSR-330 Standard Annotations
		
			Spring				javax.inject.*				javax.inject restrictions / comments		

			@Autowired 			@Inject 					@Inject没有'required'属性. 在Java8中可以使用Optional代替

			@Components 		@Named / @ManagedBean		JSR-330没有提供一个可组合的模型, 只有一种方法区标识带名称的组件

			@Scope("singleton") @Singleton        			JSR-330默认的scope类似Spring的prototype。 
															所以为了完整添加了@Singleton注解。虽然也提供了@Scope注解，但是，仅使用与创建自己的组件时使用。

			@Qualifier 			@Qualifier / @Named 		javax.inject.Qualifier只是为了创建标识的元注解，可以通过javax.inject.Named添加标识字符。

			@Value 				-							no equivalent

			@Required 			-							no equivalent

			@Lazy				-							no equivalent

			ObjectFactory 		Provider 					javax.inject.Provider是ObjectFactory的变形, 只提供了get()方法。
															可以和@Autowired组合使用或者在没有注解的构造器/setter方法上使用。

			// done 2020-2-24 19:14:38

	1.12. Java-based Container Configuration
	
		1.12.1. Basic Concepts: @Bean and @Configuration

			java：

				@Configuration
				public class AppConfig {

				    @Bean
				    public MyService myService() {
				        return new MyServiceImpl();
				    }
				}

			xml：
			
				<beans>
				    <bean id="myService" class="com.acme.services.MyServiceImpl"/>
				</beans>	

			tips：
			
				@Configuration修饰的类中，@Bean修饰的方法会被Spring容器进行生命周期管理，同时会被CGLIB代理。

				@Component等其他修饰的类中，@Bean修饰的方法参数不能使用Spring容器中的bean，也不能调用别的@Bean方法，只充当一个轻量级的创建bean的工厂方法，不会被Spring容器进行生命周期管理，也不会被CGLIB代理，所以也没有代理的一些限制（比如类可以是final等等）。

		1.12.2. Instantiating the Spring Container by Using AnnotationConfigApplicationContext

			Simple Construction

				xml文件配置时，可以使用ClassPathXmlApplicationContext初始化容器。

				@Configuration注解配置时，可以使用AnnotationConfigApplicationContext初始化容器。

				java:

					public static void main(String[] args) {
					    ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class, MyServiceImpl.class, Dependency1.class, Dependency2.class);
					    MyService myService = ctx.getBean(MyService.class);
					    myService.doStuff();
					}

			Building the Container Programmatically by Using register(Class<?>…​)
			
				java:

					public static void main(String[] args) {
					    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
					    ctx.register(AppConfig.class, OtherConfig.class);
					    ctx.register(AdditionalConfig.class);
					    ctx.refresh();
					    MyService myService = ctx.getBean(MyService.class);
					    myService.doStuff();
					}	

			Enabling Component Scanning with scan(String…​)
			
				java:

					// 注解
					@Configuration
					@ComponentScan(basePackages = "com.acme") 
					public class AppConfig  {
					    ...
					}	

					// 编码
					public static void main(String[] args) {
					    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
					    ctx.scan("com.acme");
					    ctx.refresh();
					    MyService myService = ctx.getBean(MyService.class);
					}


				xml:
				
					<beans>
					    <context:component-scan base-package="com.acme"/>
					</beans>

				java	

			Support for Web Applications with AnnotationConfigWebApplicationContext
			
				web.xml(Spring mvc的典型配置):

					<web-app>
					    <!-- Configure ContextLoaderListener to use AnnotationConfigWebApplicationContext
					        instead of the default XmlWebApplicationContext -->
					    <context-param>
					        <param-name>contextClass</param-name>
					        <param-value>
					            org.springframework.web.context.support.AnnotationConfigWebApplicationContext
					        </param-value>
					    </context-param>

					    <!-- Configuration locations must consist of one or more comma- or space-delimited
					        fully-qualified @Configuration classes. Fully-qualified packages may also be
					        specified for component-scanning -->
					    <context-param>
					        <param-name>contextConfigLocation</param-name>
					        <param-value>com.acme.AppConfig</param-value>
					    </context-param>

					    <!-- Bootstrap the root application context as usual using ContextLoaderListener -->
					    <listener>
					        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
					    </listener>

					    <!-- Declare a Spring MVC DispatcherServlet as usual -->
					    <servlet>
					        <servlet-name>dispatcher</servlet-name>
					        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
					        <!-- Configure DispatcherServlet to use AnnotationConfigWebApplicationContext
					            instead of the default XmlWebApplicationContext -->
					        <init-param>
					            <param-name>contextClass</param-name>
					            <param-value>
					                org.springframework.web.context.support.AnnotationConfigWebApplicationContext
					            </param-value>
					        </init-param>
					        <!-- Again, config locations must consist of one or more comma- or space-delimited
					            and fully-qualified @Configuration classes -->
					        <init-param>
					            <param-name>contextConfigLocation</param-name>
					            <param-value>com.acme.web.MvcConfig</param-value>
					        </init-param>
					    </servlet>

					    <!-- map all requests for /app/* to the dispatcher servlet -->
					    <servlet-mapping>
					        <servlet-name>dispatcher</servlet-name>
					        <url-pattern>/app/*</url-pattern>
					    </servlet-mapping>
					</web-app>				

		1.12.3. Using the @Bean Annotation

			Declaring a Bean

				java:

					@Configuration
					public class AppConfig {

					    @Bean
					    public TransferServiceImpl transferService() {
					        return new TransferServiceImpl();
					    }
					}

					@Configuration
					public class AppConfig {

					    @Bean
					    public TransferService transferService() {
					        return new TransferServiceImpl();
					    }
					}

				xml:
					
					<beans>
					    <bean id="transferService" class="com.acme.TransferServiceImpl"/>
					</beans>

				context view:

					transferService -> com.acme.TransferServiceImpl

				tips:
					
					虽然可以声明@Bean方法的返回类型为接口类型，但是这会限制该接口	的提前的类型预测可见性。结果就是，只有当该单例bean实例化之后，容器才会知道它的具体类型。
					非懒加载的单例bean是根据他们的声明类型来实例化的，所以当另外的组件试图匹配的bean不是声明的类型（如@Autowired TransferServiceImpl），可能会得到不同的类型匹配结果。

					如果执意倾向于声明返回类型为接口类型，请慎重考虑@Bean返回类型。 对于实现了不同接口的组件和可能引用了他们实现类型的组件，尽可能明确地声明返回类型更安全。

			Bean Dependencies

				java:
			
					@Configuration
					public class AppConfig {

					    @Bean
					    public TransferService transferService(AccountRepository accountRepository) {
					        return new TransferServiceImpl(accountRepository);
					    }
					}

			Receiving Lifecycle Callbacks
			
				java:

					public class BeanOne {

					    public void init() {
					        // initialization logic
					    }
					}

					public class BeanTwo {

					    public void cleanup() {
					        // destruction logic
					    }
					}

					@Configuration
					public class AppConfig {

					    @Bean(initMethod = "init")
					    public BeanOne beanOne() {
					        return new BeanOne();
					    }

					    @Bean(destroyMethod = "cleanup")
					    public BeanTwo beanTwo() {
					        return new BeanTwo();
					    }
					}

				tips:
				
					默认情况下，如果bean中有public的close/shutdown方法，会自动匹配为容器销毁的回调方法。 如果不想在容器销毁时close/shutdown被调用，可以设置@Bean(destroyMethod="")

						java:

							@Bean(destroyMethod="")
							public DataSource dataSource() throws NamingException {
							    return (DataSource) jndiTemplate.lookup("MyDS");
							}
					
			Specifying Bean Scope
			
				Using the @Scope Annotation

					java:

						@Configuration
						public class MyConfiguration {

						    @Bean
						    @Scope("prototype")
						    public Encryptor encryptor() {
						        // ...
						    }
						}

				@Scope and scoped-proxy

					java:

						// an HTTP Session-scoped bean exposed as a proxy
						@Bean
						@SessionScope
						public UserPreferences userPreferences() {
						    return new UserPreferences();
						}

						@Bean
						public Service userService() {
						    UserService service = new SimpleUserService();
						    // a reference to the proxied userPreferences bean
						    service.setUserPreferences(userPreferences());
						    return service;
						}

			Customizing Bean Naming	

				java:

					@Configuration
					public class AppConfig {

					    @Bean(name = "myThing")
					    public Thing thing() {
					        return new Thing();
					    }
					}		

			Bean Aliasing
			
				java:

					@Configuration
					public class AppConfig {

					    @Bean({"dataSource", "subsystemA-dataSource", "subsystemB-dataSource"})
					    public DataSource dataSource() {
					        // instantiate, configure and return DataSource bean...
					    }
					}	

			Bean Description
			
				java:

					@Configuration
					public class AppConfig {

					    @Bean
					    @Description("Provides a basic example of a bean")
					    public Thing thing() {
					        return new Thing();
					    }
					}				

		1.12.4. Using the @Configuration annotation														

			Injecting Inter-bean Dependencies

				java:

					@Configuration
					public class AppConfig {

					    @Bean
					    public BeanOne beanOne() {
					        return new BeanOne(beanTwo());
					    }

					    @Bean
					    public BeanTwo beanTwo() {
					        return new BeanTwo();
					    }
					}

					tips:

						@Configuration可以这样使用，@Component等注解的不可以。

			Lookup Method Injection		

				java:

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


					---------------------

					@Bean
					@Scope("prototype")
					public AsyncCommand asyncCommand() {
					    AsyncCommand command = new AsyncCommand();
					    // inject dependencies here as required
					    return command;
					}

					@Bean
					public CommandManager commandManager() {
					    // return new anonymous implementation of CommandManager with createCommand()
					    // overridden to return a new prototype Command object
					    return new CommandManager() {
					        protected Command createCommand() {
					            return asyncCommand();
					        }
					    }
					}
			
			Further Information About How Java-based Configuration Works Internally

				java:

					@Configuration
					public class AppConfig {

					    @Bean
					    public ClientService clientService1() {
					        ClientServiceImpl clientService = new ClientServiceImpl();
					        clientService.setClientDao(clientDao());
					        return clientService;
					    }

					    @Bean
					    public ClientService clientService2() {
					        ClientServiceImpl clientService = new ClientServiceImpl();
					        clientService.setClientDao(clientDao());
					        return clientService;
					    }

					    @Bean
					    public ClientDao clientDao() {
					        return new ClientDaoImpl();
					    }
					}

				tips:
				
					上边的例子中，clientDao()被调用两次，正常认为会有两个实例。但是在Spring中，默认bean是单例的，在容器启动时，所有的@Configuration修饰类都会被CGLIB代理生成子类，
					在子类中，子类方法会在调用父类方法新建实例之前先检查是否已有缓存的bean实例（第二次调用时已有缓存，则会从缓存中直接获取而不再新建bean实例）。（只针对bean为单例时）

		1.12.5. Composing Java-based Configurations		

			Using the @Import Annotation

				java:

					@Configuration
					public class ConfigA {

					    @Bean
					    public A a() {
					        return new A();
					    }
					}

					@Configuration
					@Import(ConfigA.class)
					public class ConfigB {

					    @Bean
					    public B b() {
					        return new B();
					    }
					}

					public static void main(String[] args) {
					    ApplicationContext ctx = new AnnotationConfigApplicationContext(ConfigB.class);

					    // now both beans A and B will be available...
					    A a = ctx.getBean(A.class);
					    B b = ctx.getBean(B.class);
					}

				tips:	

					Spring4.2以后，@Import同样支持component组件，和AnnotationConfigApplicationContext.register类似。可以使用此方法来避免全包扫描。


				Injecting Dependencies on Imported @Bean Definitions	

					java:

						@Configuration
						public class ServiceConfig {

						    @Bean
						    public TransferService transferService(AccountRepository accountRepository) {
						        return new TransferServiceImpl(accountRepository);
						    }
						}

						@Configuration
						public class RepositoryConfig {

						    @Bean
						    public AccountRepository accountRepository(DataSource dataSource) {
						        return new JdbcAccountRepository(dataSource);
						    }
						}

						@Configuration
						@Import({ServiceConfig.class, RepositoryConfig.class})
						public class SystemTestConfig {

						    @Bean
						    public DataSource dataSource() {
						        // return new DataSource
						    }
						}

						public static void main(String[] args) {
						    ApplicationContext ctx = new AnnotationConfigApplicationContext(SystemTestConfig.class);
						    // everything wires up across configuration classes...
						    TransferService transferService = ctx.getBean(TransferService.class);
						    transferService.transfer(100.00, "A123", "C456");
						}

						---------------------------

						@Configuration
						public class ServiceConfig {

						    @Autowired
						    private AccountRepository accountRepository;

						    @Bean
						    public TransferService transferService() {
						        return new TransferServiceImpl(accountRepository);
						    }
						}

						@Configuration
						public class RepositoryConfig {

						    private final DataSource dataSource;

						    public RepositoryConfig(DataSource dataSource) {
						        this.dataSource = dataSource;
						    }

						    @Bean
						    public AccountRepository accountRepository() {
						        return new JdbcAccountRepository(dataSource);
						    }
						}

						@Configuration
						@Import({ServiceConfig.class, RepositoryConfig.class})
						public class SystemTestConfig {

						    @Bean
						    public DataSource dataSource() {
						        // return new DataSource
						    }
						}

						public static void main(String[] args) {
						    ApplicationContext ctx = new AnnotationConfigApplicationContext(SystemTestConfig.class);
						    // everything wires up across configuration classes...
						    TransferService transferService = ctx.getBean(TransferService.class);
						    transferService.transfer(100.00, "A123", "C456");
						}

						--------------------------

						@Configuration
						public class ServiceConfig {

						    @Autowired
						    private RepositoryConfig repositoryConfig;

						    @Bean
						    public TransferService transferService() {
						        // navigate 'through' the config class to the @Bean method!
						        return new TransferServiceImpl(repositoryConfig.accountRepository());
						    }
						}

						--------------------------

						@Configuration
						public class ServiceConfig {

						    @Autowired
						    private RepositoryConfig repositoryConfig;

						    @Bean
						    public TransferService transferService() {
						        return new TransferServiceImpl(repositoryConfig.accountRepository());
						    }
						}

						@Configuration
						public interface RepositoryConfig {

						    @Bean
						    AccountRepository accountRepository();
						}

						@Configuration
						public class DefaultRepositoryConfig implements RepositoryConfig {

						    @Bean
						    public AccountRepository accountRepository() {
						        return new JdbcAccountRepository(...);
						    }
						}

						@Configuration
						@Import({ServiceConfig.class, DefaultRepositoryConfig.class})  // import the concrete config!
						public class SystemTestConfig {

						    @Bean
						    public DataSource dataSource() {
						        // return DataSource
						    }

						}

						public static void main(String[] args) {
						    ApplicationContext ctx = new AnnotationConfigApplicationContext(SystemTestConfig.class);
						    TransferService transferService = ctx.getBean(TransferService.class);
						    transferService.transfer(100.00, "A123", "C456");
						}

			Conditionally Include @Configuration Classes or @Bean Methods	

				java:

					@Override
					public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
					    // Read the @Profile annotation attributes
					    MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(Profile.class.getName());
					    if (attrs != null) {
					        for (Object value : attrs.get("value")) {
					            if (context.getEnvironment().acceptsProfiles(((String[]) value))) {
					                return true;
					            }
					        }
					        return false;
					    }
					    return true;
					}

			Combining Java and XML Configuration

				Spring配置信息，往往既包含xml文件的配置，也有注解的配置。初始化容器时有两种选择:

					1. 使用XML为主的容器，如 ClassPathXmlApplicationContext

					2. 使用Java为主的容器，如 AnnotationConfigApplicationContext，相关的XML配置文件可以使用@ImportResource来导入。

				XML-centric Use of @Configuration Classes

					Declaring @Configuration classes as plain Spring <bean/> elements

						java:

							@Configuration
							public class AppConfig {

							    @Autowired
							    private DataSource dataSource;

							    @Bean
							    public AccountRepository accountRepository() {
							        return new JdbcAccountRepository(dataSource);
							    }

							    @Bean
							    public TransferService transferService() {
							        return new TransferService(accountRepository());
							    }
							}

							----------------------

							public static void main(String[] args) {
							    ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/com/acme/system-test-config.xml");
							    TransferService transferService = ctx.getBean(TransferService.class);
							    // ...
							}

						system-test-config.xml

							<beans>
							    <!-- enable processing of annotations such as @Autowired and @Configuration -->
							    <context:annotation-config/>
							    <context:property-placeholder location="classpath:/com/acme/jdbc.properties"/>

							    <bean class="com.acme.AppConfig"/>

							    <bean class="org.springframework.jdbc.datasource.DriverManagerDataSource">
							        <property name="url" value="${jdbc.url}"/>
							        <property name="username" value="${jdbc.username}"/>
							        <property name="password" value="${jdbc.password}"/>
							    </bean>
							</beans>


						jdbc.properties
						
							jdbc.url=jdbc:hsqldb:hsql://localhost/xdb
							jdbc.username=sa
							jdbc.password=	

					Using <context:component-scan/> to pick up @Configuration classes	

						system-test-config.xml
					
							<beans>
							    <!-- picks up and registers AppConfig as a bean definition -->
							    <context:component-scan base-package="com.acme"/>
							    <context:property-placeholder location="classpath:/com/acme/jdbc.properties"/>

							    <bean class="org.springframework.jdbc.datasource.DriverManagerDataSource">
							        <property name="url" value="${jdbc.url}"/>
							        <property name="username" value="${jdbc.username}"/>
							        <property name="password" value="${jdbc.password}"/>
							    </bean>
							</beans>

				@Configuration Class-centric Use of XML with @ImportResource
					
					java:

						@Configuration
						@ImportResource("classpath:/com/acme/properties-config.xml")
						public class AppConfig {

						    @Value("${jdbc.url}")
						    private String url;

						    @Value("${jdbc.username}")
						    private String username;

						    @Value("${jdbc.password}")
						    private String password;

						    @Bean
						    public DataSource dataSource() {
						        return new DriverManagerDataSource(url, username, password);
						    }
						}	

						---------------

						public static void main(String[] args) {
						    ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
						    TransferService transferService = ctx.getBean(TransferService.class);
						    // ...
						}

					properties-config.xml
					
						<beans>
						    <context:property-placeholder location="classpath:/com/acme/jdbc.properties"/>
						</beans>				

					jdbc.properties
					
						jdbc.url=jdbc:hsqldb:hsql://localhost/xdb
						jdbc.username=sa
						jdbc.password=	

		// done 2020-2-25 17:42:24				

	1.13. Environment Abstraction	

		1.13.1. Bean Definition Profiles

			environment: 对不同用户意味着是不同的，包括：

				- 开发环境中使用内存数据库，测试/生产环境中通过JNDI寻找相同数据库。
				- 只有高性能环境中发布应用时，注册监控平台。
				- 用户A，用户B部署应用时注册各自的bean实现。

			比如：
			
				测试环境：

					@Bean
					public DataSource dataSource() {
					    return new EmbeddedDatabaseBuilder()
					        .setType(EmbeddedDatabaseType.HSQL)
					        .addScript("my-schema.sql")
					        .addScript("my-test-data.sql")
					        .build();
					}

				生产环境：		

					@Bean(destroyMethod="")
					public DataSource dataSource() throws Exception {
					    Context ctx = new InitialContext();
					    return (DataSource) ctx.lookup("java:comp/env/jdbc/datasource");
					}

			Using @Profile
			
				java:

					@Configuration
					@Profile("development")
					public class StandaloneDataConfig {

					    @Bean
					    public DataSource dataSource() {
					        return new EmbeddedDatabaseBuilder()
					            .setType(EmbeddedDatabaseType.HSQL)
					            .addScript("classpath:com/bank/config/sql/schema.sql")
					            .addScript("classpath:com/bank/config/sql/test-data.sql")
					            .build();
					    }
					}	

					-----------------

					@Configuration
					@Profile("production")
					public class JndiDataConfig {

					    @Bean(destroyMethod="")
					    public DataSource dataSource() throws Exception {
					        Context ctx = new InitialContext();
					        return (DataSource) ctx.lookup("java:comp/env/jdbc/datasource");
					    }
					}	

					

				profile表达式：

					!: A logical “not” of the profile

					&: A logical “and” of the profiles

					|: A logical “or” of the profiles

					tips:

						不能没有分隔直接搭配&和|，比如"production & us-east | eu-central"是无效的，但是可以"production & (us-east | eu-central)"

						表达式表示配置激活状态，比如"production & (us-east | eu-central)"表示：只有production激活 和 us-east/eu-central其中之一激活，才注册该bean。

				自定义Profile注解：
				
					@Target(ElementType.TYPE)
					@Retention(RetentionPolicy.RUNTIME)
					@Profile("production")
					public @interface Production {
					}	

				可以在同一个类中声明：
				
					java:

						@Configuration
						public class AppConfig {

						    @Bean("dataSource")
						    @Profile("development") 
						    public DataSource standaloneDataSource() {
						        return new EmbeddedDatabaseBuilder()
						            .setType(EmbeddedDatabaseType.HSQL)
						            .addScript("classpath:com/bank/config/sql/schema.sql")
						            .addScript("classpath:com/bank/config/sql/test-data.sql")
						            .build();
						    }

						    @Bean("dataSource")
						    @Profile("production") 
						    public DataSource jndiDataSource() throws Exception {
						        Context ctx = new InitialContext();
						        return (DataSource) ctx.lookup("java:comp/env/jdbc/datasource");
						    }
						}	

					tips:

						如果方法同时被@Profile和@Bean修饰，会出现一种情况：@Bean修饰的方法是有重载方法，那么所有重载方法都要添加@Profile注解。这样如果条件不满足，只有第一个重载方法会受影响。因此，同一个bean的工厂方法排在构造方法之后，@Profile并不能有效查找可用的重载方法。
						
						如果想在不同环境中，定义不同实现但是相同名称的bean，可以使用不同的方法名，然后再@Bean中的name属性上配置相同名称。	

			XML Bean Definition Profiles
			
				<beans xmlns="http://www.springframework.org/schema/beans"
				    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				    xmlns:jdbc="http://www.springframework.org/schema/jdbc"
				    xmlns:jee="http://www.springframework.org/schema/jee"
				    xsi:schemaLocation="...">

				    <!-- other bean definitions -->

				    <beans profile="development">
				        <jdbc:embedded-database id="dataSource">
				            <jdbc:script location="classpath:com/bank/config/sql/schema.sql"/>
				            <jdbc:script location="classpath:com/bank/config/sql/test-data.sql"/>
				        </jdbc:embedded-database>
				    </beans>

				    <beans profile="production">
				        <jee:jndi-lookup id="dataSource" jndi-name="java:comp/env/jdbc/datasource"/>
				    </beans>
				</beans>		

				tips:

					可以在不同的文件中配置。也可以在一个文件中，嵌套beans进行配置。但是不支持上边的profile表达式。如果需要，可以使用!或者嵌套beans来配置。如：

						xml:

							<beans xmlns="http://www.springframework.org/schema/beans"
							    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
							    xmlns:jdbc="http://www.springframework.org/schema/jdbc"
							    xmlns:jee="http://www.springframework.org/schema/jee"
							    xsi:schemaLocation="...">

							    <!-- other bean definitions -->

							    <beans profile="production">
							        <beans profile="us-east">
							            <jee:jndi-lookup id="dataSource" jndi-name="java:comp/env/jdbc/datasource"/>
							        </beans>
							    </beans>
							</beans>

			Activating a Profile
			
				java:

					AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
					ctx.getEnvironment().setActiveProfiles("development");
					ctx.register(SomeConfig.class, StandaloneDataConfig.class, JndiDataConfig.class);
					ctx.refresh();				

					ctx.getEnvironment().setActiveProfiles("profile1", "profile2");

				@Annotation	
					
					@ActiveProfiles

						@ExtendWith(SpringExtension.class)
						// ApplicationContext will be loaded from "classpath:/app-config.xml"
						@ContextConfiguration("/app-config.xml")
						@ActiveProfiles("dev")
						class TransferServiceTest {

						    @Autowired
						    TransferService transferService;

						    @Test
						    void testTransferService() {
						        // test the transferService
						    }
						}

				properties:(env, jvm, web.xml, jndi...)
				
					spring.profiles.active	

				JVM start:	

					-Dspring.profiles.active="profile1,profile2"

			Default Profile

				java:
			
					@Configuration
					@Profile("default")
					public class DefaultDataConfig {

					    @Bean
					    public DataSource dataSource() {
					        return new EmbeddedDatabaseBuilder()
					            .setType(EmbeddedDatabaseType.HSQL)
					            .addScript("classpath:com/bank/config/sql/schema.sql")
					            .build();
					    }
					}	

					tips:

						可以通过Environment.setDefaultProfiles()	，或者spring.profiles.default对default进行设置。

			// done 2020-2-26 12:19:13

		1.13.2. PropertySource Abstraction		

			java:	

				ApplicationContext ctx = new GenericApplicationContext();
				Environment env = ctx.getEnvironment();
				boolean containsMyProperty = env.containsProperty("my-property");
				System.out.println("Does my environment contain the 'my-property' property? " + containsMyProperty);	

			StandardServletEnvironment中属性优先层级：

				1. ServletConfig parameters (if applicable — for example, in case of a DispatcherServlet context)
				2. ServletContext parameters (web.xml context-param entries)
				3. JNDI environment variables (java:comp/env/ entries)
				4. JVM system properties (-D command-line arguments)
				5. JVM system environment (operating system environment variables)

			优先级配置调整：
			
				ConfigurableApplicationContext ctx = new GenericApplicationContext();
				MutablePropertySources sources = ctx.getEnvironment().getPropertySources();
				sources.addFirst(new MyPropertySource());	

		1.13.3. Using @PropertySource
		
			java:

				@Configuration
				@PropertySource("classpath:/com/myco/app.properties")
				public class AppConfig {

				    @Autowired
				    Environment env;

				    @Bean
				    public TestBean testBean() {
				        TestBean testBean = new TestBean();
				        testBean.setName(env.getProperty("testbean.name"));
				        return testBean;
				    }
				}

				-------------- 支持SpEL表达式

				@Configuration
				@PropertySource("classpath:/com/${my.placeholder:default/path}/app.properties")
				public class AppConfig {

				    @Autowired
				    Environment env;

				    @Bean
				    public TestBean testBean() {
				        TestBean testBean = new TestBean();
				        testBean.setName(env.getProperty("testbean.name"));
				        return testBean;
				    }
				}

		1.13.4. Placeholder Resolution in Statements

			xml:
		
				<beans>
				    <import resource="com/bank/service/${customer}-config.xml"/>
				</beans>		

			tips:
			
				因为Environment使用Spring容器管理，所以占位符customer不管定义在系统变量，环境变量还是自定义的属性文件中，只要存在即可。	

	1.14. Registering a LoadTimeWeaver
	
		java:

			@Configuration
			@EnableLoadTimeWeaving
			public class AppConfig {
			}		

		xml:
		
			<beans>
			    <context:load-time-weaver/>
			</beans>		

	1.15. Additional Capabilities of the ApplicationContext
	
		1.15.1. Internationalization using MessageSource

			容器启动时，如果没有专门配置"messageSource"，会默认注册一个名为"messageSource"，类型为"DelegatingMessageSource"的bean。

			messageSource实现类：

				ResourceBundleMessageSource 

				StaticMessageSource

				ReloadableResourceBundleMessageSource

			使用：

				常规使用：
			
					xml:

						<beans>
						    <bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource">
						        <property name="basenames">
						            <list>
						                <value>format</value>
						                <value>exceptions</value>
						                <value>windows</value>
						            </list>
						        </property>
						    </bean>
						</beans>	

					properties:
					
						format.properties

							message=Alligators rock!

						exceptions.properties
						
							argument.required=The {0} argument is required.

						windows.properties
						
							xxx = xxx

					java:
					
						public static void main(String[] args) {
						    MessageSource resources = new ClassPathXmlApplicationContext("beans.xml");
						    String message = resources.getMessage("message", null, "Default", Locale.ENGLISH);
						    System.out.println(message);
						}		

						// result: Alligators rock!		

				高级用法：

					--（包含占位符 + 参数）

						xml:

							<beans>

							    <!-- this MessageSource is being used in a web application -->
							    <bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource">
							        <property name="basename" value="exceptions"/>
							    </bean>

							    <!-- lets inject the above MessageSource into this POJO -->
							    <bean id="example" class="com.something.Example">
							        <property name="messages" ref="messageSource"/>
							    </bean>

							</beans>

						java:
						
							public class Example {

							    private MessageSource messages;

							    public void setMessages(MessageSource messages) {
							        this.messages = messages;
							    }

							    public void execute() {
							        String message = this.messages.getMessage("argument.required",
							            new Object [] {"userDao"}, "Required", Locale.ENGLISH);
							        System.out.println(message);
							    }
							}

							// result: The userDao argument is required.

					-- 国际化
					
						exceptions_en_GB.properties
						
							argument.required=Ebagum lad, the ''{0}'' argument is required, I say, required.		

						java:
						
							public static void main(final String[] args) {
							    MessageSource resources = new ClassPathXmlApplicationContext("beans.xml");
							    String message = resources.getMessage("argument.required",
							        new Object [] {"userDao"}, "Required", Locale.UK);
							    System.out.println(message);
							}	

							// result: Ebagum lad, the 'userDao' argument is required, I say, required.
							
				tips:				

					bean("messageSource")创建的时候，如果容器ApplicationContext中有任意MessageSourceAware接口的实现类，都会进行合并再创建。

			// doen 2020-2-26 18:29:33	

		1.15.2. Standard and Custom Events	

			如果容器中存在一个bean，其实现了接口ApplicationListener，那么每次容器ApplicationContext发布事件ApplicationEvent时，该bean都会收到通知。（典型的观察者模式）

			Spring提供的标准事件：

				ContextRefreshedEvent

					Published when the ApplicationContext is initialized or refreshed (for example, by using the refresh() method on the ConfigurableApplicationContext interface). Here, “initialized” means that all beans are loaded, post-processor beans are detected and activated, singletons are pre-instantiated, and the ApplicationContext object is ready for use. As long as the context has not been closed, a refresh can be triggered multiple times, provided that the chosen ApplicationContext actually supports such “hot” refreshes. For example, XmlWebApplicationContext supports hot refreshes, but GenericApplicationContext does not.

				ContextStartedEvent

					Published when the ApplicationContext is started by using the start() method on the ConfigurableApplicationContext interface. Here, “started” means that all Lifecycle beans receive an explicit start signal. Typically, this signal is used to restart beans after an explicit stop, but it may also be used to start components that have not been configured for autostart (for example, components that have not already started on initialization).

				ContextStoppedEvent

					Published when the ApplicationContext is stopped by using the stop() method on the ConfigurableApplicationContext interface. Here, “stopped” means that all Lifecycle beans receive an explicit stop signal. A stopped context may be restarted through a start() call.

				ContextClosedEvent

					Published when the ApplicationContext is being closed by using the close() method on the ConfigurableApplicationContext interface or via a JVM shutdown hook. Here, "closed" means that all singleton beans will be destroyed. Once the context is closed, it reaches its end of life and cannot be refreshed or restarted.

				RequestHandledEvent

					A web-specific event telling all beans that an HTTP request has been serviced. This event is published after the request is complete. This event is only applicable to web applications that use Spring’s DispatcherServlet.

				ServletRequestHandledEvent

					A subclass of RequestHandledEvent that adds Servlet-specific context information.

			也可以创建发布自定义事件：
			
				java:

					// 创建事件: 继承ApplicationEvent
					public class BlackListEvent extends ApplicationEvent {

					    private final String address;
					    private final String content;

					    public BlackListEvent(Object source, String address, String content) {
					        super(source);
					        this.address = address;
					        this.content = content;
					    }

					    // accessor and other methods...
					}

					// 发布事件: 实现ApplicationEventPublisherAware
					public class EmailService implements ApplicationEventPublisherAware {

					    private List<String> blackList;
					    private ApplicationEventPublisher publisher;

					    public void setBlackList(List<String> blackList) {
					        this.blackList = blackList;
					    }

					    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
					        this.publisher = publisher;
					    }

					    public void sendEmail(String address, String content) {
					        if (blackList.contains(address)) {
					            publisher.publishEvent(new BlackListEvent(this, address, content));
					            return;
					        }
					        // send email...
					    }
					}

					// 接收事件: 实现ApplicationListener
					public class BlackListNotifier implements ApplicationListener<BlackListEvent> {

					    private String notificationAddress;

					    public void setNotificationAddress(String notificationAddress) {
					        this.notificationAddress = notificationAddress;
					    }

					    public void onApplicationEvent(BlackListEvent event) {
					        // notify appropriate parties via notificationAddress...
					    }
					}

				xml:
				
					<bean id="emailService" class="example.EmailService">
					    <property name="blackList">
					        <list>
					            <value>known.spammer@example.org</value>
					            <value>known.hacker@example.org</value>
					            <value>john.doe@example.org</value>
					        </list>
					    </property>
					</bean>

					<bean id="blackListNotifier" class="example.BlackListNotifier">
					    <property name="notificationAddress" value="blacklist@example.org"/>
					</bean>	

				tips:
				
					Spring的事件主要用于一些简单的容器内部beans之间的通信，如果有更复杂的企业级组件需求，可以使用spring-integration模块，它支持轻量级，设计模式，事件驱动原理（建立在熟知的Spring编程模块基础上）。


			Annotation-based Event Listeners

				4.2以后，可以使用注解@EventLister监听事件

					java:

						// 监听事件
						public class BlackListNotifier {

						    private String notificationAddress;

						    public void setNotificationAddress(String notificationAddress) {
						        this.notificationAddress = notificationAddress;
						    }

						    @EventListener
						    public void processBlackListEvent(BlackListEvent event) {
						        // notify appropriate parties via notificationAddress...
						    }
						}

						// 指定事件类型
						@EventListener({ContextStartedEvent.class, ContextRefreshedEvent.class})
						public void handleContextStart() {
						    // ...
						}

						// 条件筛选（SpEL表达式）
						@EventListener(condition = "#blEvent.content == 'my-event'")
						public void processBlackListEvent(BlackListEvent blEvent) {
						    // notify appropriate parties via notificationAddress...
						}

						// 发布其他事件（方法返回值设置为事件类型即可，如果是多个事件，可以返回List）
						@EventListener
						public ListUpdateEvent handleBlackListEvent(BlackListEvent event) {
						    // notify appropriate parties via notificationAddress and
						    // then publish a ListUpdateEvent...
						}

			Asynchronous Listeners
			
				java:

					// 异步事件
					@EventListener
					@Async
					public void processBlackListEvent(BlackListEvent event) {
					    // BlackListEvent is processed in a separate thread
					}			

					tips:

						异步事件如果执行期间抛出异常，不会返回给调用方

						异步事件如果想发布其他事件，可以通过添加ApplicationEventPublisher手动发布，不支持通过（添加方法返回事件类型）来发布其他事件。

			Ordering Listeners
			
				java:

					// 事件执行顺序
					@EventListener
					@Order(42)
					public void processBlackListEvent(BlackListEvent event) {
					    // notify appropriate parties via notificationAddress...
					}	

			Generic Events

				java:

					// 添加泛型，直接收指定类型事件的监听器（class PersonCreatedEvent extends EntityCreatedEvent<Person> { …​ } 类似的事件才会被监听）
					@EventListener
					public void onPersonCreated(EntityCreatedEvent<Person> event) {
					    // ...
					}	

					// 如果都是用上边的泛型结构，就会变复杂，可以通过实现ResolvableTypeProvider接口来获取事件驱动类型（对applicationContext，以及任意其他事件对象都可以感知）
					public class EntityCreatedEvent<T> extends ApplicationEvent implements ResolvableTypeProvider {

					    public EntityCreatedEvent(T entity) {
					        super(entity);
					    }

					    @Override
					    public ResolvableType getResolvableType() {
					        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(getSource()));
					    }
					}			

			// done 2020-2-27 11:55:25


		1.15.3. Convenient Access to Low-level Resources	

			资源相关类：

				/**
				 * 为输入流对象提供的简单接口
				 * Spring中资源类@Resource的基础接口
				 * 对于简用的流，任意输入流都可以使用InputStreamResource。
				 * 其实现类如Spring中的ByteArrayResource，基于文件的Resource，都可以用于实例化，多次读取基础的内容流。比如，可以用于邮件附件。
				 */ 
				interface InputStreamSource {}

				/**
				 * 为了从潜在资源的实际类型（如文件资源、类路径资源）中抽象资源描述信息的接口。
 				 * 有具体形态的输入流可以被任意资源类读取，比如一个URL、文件操作之后返回的特定的资源，具体实现各有区别。
 				 */	
				interface Resource extends InputStreamSource {}


				/**
				 * 加载资源的策略接口。 ApplicationContext继承了该类的子类ResourcePatternResolver。
				 * DefaultResourceLoader是一个单独实现类，可以在容器外使用，如ResourceEditor。
				 * 在ApplicationContext中，可以通过不同的资源加载策略，从字符串中读取并填充Bean属性，包括类型资源和资源数组。
				 */
				interface ResourceLoader {}

				/**
				 * 可以将路径模式解析为资源对象的策略接口。
				 * ResourceLoader子类，ApplicationContext继承该类以及其相关功能。
				 * PathMatchingResourcePatternResolver是一个单独实现类，可以在容器外使用，如ResourceArrayPropertyEditor。
				 * 可以用于任意路径模式（如: "WEB-INF/*-context.xml"）: 输入样式必须与相关实现类匹配（该接口只提供了转换方法，并没有制定模式样式）
				 * 该接口同样支持新的资源前缀（"classpath*:"）来加载类路径下的资源。这种情况尽量不要添加路径占位符（如: "/beans.xml"）。
				 * Jar包或者类目录可以包含多个相同名称的文件。
				 */
				interface ResourcePatternResolver extends ResourceLoader {}

				/**
				 * 资源加载器通知接口。
				 */
				interface ResourceLoaderAware extends Aware {}

		1.15.4. Convenient ApplicationContext Instantiation for Web Applications
		
			xml:

				<context-param>
				    <param-name>contextConfigLocation</param-name>
				    <param-value>/WEB-INF/daoContext.xml /WEB-INF/applicationContext.xml</param-value>
				</context-param>

				<listener>
				    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
				</listener>		

		1.15.5. Deploying a Spring ApplicationContext as a Java EE RAR File
		
			如果应用不需要http请求，比如只包含消息端点和定时任务，可以部署为RAR压缩包。详细信息可参考：SpringContextResourceAdapter

		// done 2020-2-27 17:33:44	

	1.16. The BeanFactory

		1.16.1. BeanFactory or ApplicationContext?

			优先选择ApplicationContext，因为：

			Feature													BeanFactory			ApplicationContext

			Bean instantiation/wiring 								Yes 				Yes
			Integrated lifecycle management 						No 					Yes
			Automatic BeanPostProcessor registration  				No 					Yes
			Automatic BeanFactoryPostProcessor registration 		No 					Yes
			Convenient MessageSource access (for internalization) 	No 					Yes
			Built-in ApplicationEvent publication mechanism 		No 					Yes

		// done 2020-2-27 18:03:02
		
2. Resources

	2.1. Introduction

		URL： Uniform Resource Locator 统一资源定位符

		URI： Uniform Resource Identifier 统一资源标识符

		URLEncoder： HTML表单编码通用组件

		URLDecoder： HTML表单解码通用组件

	2.2. The Resource Interface

		java:

			public interface Resource extends InputStreamSource {

			    boolean exists();

			    boolean isOpen();

			    URL getURL() throws IOException;

			    File getFile() throws IOException;

			    Resource createRelative(String relativePath) throws IOException;

			    String getFilename();

			    String getDescription();
			}

			public interface InputStreamSource {

			    InputStream getInputStream() throws IOException;
			}

	2.3. Built-in Resource Implementations	

		2.3.1. UrlResource

			UrlResource封装了URL，可以获取任何URL可以获取的对象，比如文件（file:）、HTTP对象（https:）、FTP对象（ftp:）或者其他。

		2.3.2. ClassPathResource
		
			代表可以从类路径获取的资源。（使用容器上下文加载器，或者指定类加载器，或者指定资源加载类），支持解析File（文件系统中的，JAR包内不支持）。

		2.3.3. FileSystemResource
		
			处理java.io.File 和 java.nio.file.Path 的实现类。 可以解析File和URL。

		2.3.4. ServletContextResource

			处理ServletContext资源的实现类。 支持处理流stream，URL，文件（web应用程序存档已扩展的并且存在于文件系统中的）

		2.3.5. InputStreamResource
		
			其他Resource实现类（比如ByteArrayResource以及其他基于文件的资源实现类）不可用时可以考虑此类。

			与其他实现类不同，它代表一个已经打开的资源（即isOpen() == true）。如果想保留资源描述符或者需要多次读取流stream时，不要使用此类。

		2.3.6. ByteArrayResource
		
			指定字节数组的资源实现类。

	2.4. The ResourceLoader
	
		java:

			public interface ResourceLoader {

			    Resource getResource(String location);
			}			

		不同的容器返回不同的资源类型：

			// ctx = ClassPathXmlApplicationContext 时返回 ClassPathResource
			// ctx = FileSystemXmlApplicationContext 时返回 FileSystemResource
			// ctx = WebApplicationContext 时返回 ServletContextResource
			Resource template = ctx.getResource("some/resource/path/myTemplate.txt");

		也可以通过修改不同的前缀来指定不同的资源实现类:

			// 添加前缀classpath:，template为ClassPathResource 
			Resource template = ctx.getResource("classpath:some/resource/path/myTemplate.txt");
			// 添加前缀file:或者https:, template为UrlResource 
			Resource template = ctx.getResource("file:///some/resource/path/myTemplate.txt");
			Resource template = ctx.getResource("https://myhost.com/resource/path/myTemplate.txt");

		String -> Resource 转换策略：
		
			Prefix					Example										Explanation

			classpath: 				classpath:com/myapp/config.xml 				Loaded from the classpath.
			file: 					file:///data/config.xml 					Loaded as a URL from the filesystem. See also FileSystemResource Caveats.
			http: 					https://myserver/logo.png 					Loaded as a URL.
			(none) 					/data/config.xml 							Depends on the underlying ApplicationContext.	

	2.5. The ResourceLoaderAware interface

		接口定义：

			java:

				public interface ResourceLoaderAware {

				    void setResourceLoader(ResourceLoader resourceLoader);
				}		

		如果在Spring容器环境中，想提供一个ResourceLoader对象引用，可以:
			1. 实现接口ResourceLoaderAware
			2. 实现接口ApplicationContextAware（如果只需要资源加载器，优先用第一种方法）
			3. 容器组件中，注入ResourceLoader

	2.6. Resources as Dependencies
	
		xml:

			<bean id="myBean" class="...">
				// 默认注入类型：ResourceLoader
			    <property name="template" value="some/resource/path/myTemplate.txt"/>

			    // 添加前缀classpath:，则注入ClassPathResource
			    <property name="template" value="classpath:some/resource/path/myTemplate.txt">

			    // 添加前缀file:，则注入UrlResource
			    <property name="template" value="file:///some/resource/path/myTemplate.txt"/>
			</bean>		

	2.7. Application Contexts and Resource Paths
	
		2.7.1. Constructing Application Contexts

			Constructing ClassPathXmlApplicationContext Instances — Shortcuts

				dir:

					com/
					  foo/
					    services.xml
					    daos.xml
					    MessengerService.class		

				java:
				
					ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] {"services.xml", "daos.xml"}, MessengerService.class);	 

		2.7.2. Wildcards in Application Context Constructor Resource Paths
		
			Ant-style Patterns

				i.e:

					/WEB-INF/*-context.xml
					com/mycompany/**/applicationContext.xml
					file:C:/some/path/*-context.xml
					classpath:com/mycompany/**/applicationContext.xml			

			Implications on Portability		

			The classpath*: Prefix

				ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:conf/appContext.xml");	

			Other Notes Relating to Wildcards
			
		2.7.3. FileSystemResource Caveats

			相对路径（relative paths）： 相对于当前目录。

			绝对路径（absolute paths）： 相对于根目录。
		
			java:

				相对路径，以下几种写法都是类似的：

					ApplicationContext ctx = new FileSystemXmlApplicationContext("conf/context.xml");		

					ApplicationContext ctx = new FileSystemXmlApplicationContext("/conf/context.xml");

					FileSystemXmlApplicationContext ctx = ...;
					ctx.getResource("some/resource/path/myTemplate.txt");

					FileSystemXmlApplicationContext ctx = ...;
					ctx.getResource("/some/resource/path/myTemplate.txt");

				绝对路径，可以使用以下写法：	

					// actual context type doesn't matter, the Resource will always be UrlResource
					ctx.getResource("file:///some/resource/path/myTemplate.txt");

					// force this FileSystemXmlApplicationContext to load its definition via a UrlResource
					ApplicationContext ctx = new FileSystemXmlApplicationContext("file:///conf/context.xml");

		// doen 2020-2-28 18:30:25
		
3. Validation, Data Binding, and Type Conversion	

	3.1. Validation by Using Spring’s Validator Interface

		相关类：
			
			// 对象校验器接口
			public interface Validator

			// 提供存储和获取对象绑定和对象校验的错误信息（单线程）
			public interface Errors

			// 通用工具类：提供调用校验器和空值校验的方法
			public abstract class ValidationUtils

		i.e:

			java:

				public class Person {

				    private String name;
				    private int age;

				    // the usual getters and setters...
				}

				---------------

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

				-----------------

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

	3.2. Resolving Codes to Error Messages					

		相关类：

			// 从校验错误码中构建消息码的策略接口（数据绑定中可以用于构建对象错误、字段错误信息）
			public interface MessageCodesResolver

			// MessageCodesResolver的默认实现类
			public class DefaultMessageCodesResolver implements MessageCodesResolver, Serializabl

		i.e:
			
			java:

				private DefaultMessageCodesResolver resolver = new DefaultMessageCodesResolver();

				@Test
				public void shouldResolveFieldMessageCode() throws Exception {
					String[] codes = resolver.resolveMessageCodes("errorCode", "objectName", "field",
							TestBean.class);
					assertThat(codes).containsExactly(
							"errorCode.objectName.field",
							"errorCode.field",
							"errorCode.org.springframework.beans.testfixture.beans.TestBean",
							"errorCode");
				}

		// done 2020-2-29 12:07:18

	3.3. Bean Manipulation and the BeanWrapper	

		相关类：

			/**
			 * Spring底层JavaBeans的核心接口。
			 *   
			 *    提供解析or操作Java对象的功能（如属性的get/set，获取属性描述，判断属性是否可读/科协）。
			 *    支持嵌套属性，并且没有嵌套次数的限制
			 *    为了避免get方法调用引起的副作用，extractOldValueForEditor默认为false，设置为true时，则当前属性会暴露给自定义编辑器。
			 */ 
			public interface BeanWrapper extends ConfigurablePropertyAccessor {}

			/**
			 * 封装三个接口的功能：PropertyAccessor（属性访问）、PropertyEditorRegistry（属性编辑器注册表）、TypeConverter（类型转换）
			 */
			public interface ConfigurablePropertyAccessor extends PropertyAccessor, PropertyEditorRegistry, TypeConverter {}

			/**
			 * 名称属性访问的通用接口（名称属性： 对象属性、字段）。 基础实现类： AbstractPropertyAccessor
			 */
			public interface PropertyAccessor {}

			/**
			 * JavaBeans属性编辑器的注册接口。 基础实现类： PropertyEditorRegistrySupport
			 */
			public interface PropertyEditorRegistry {}

			/**
			 * 定义类型转换方法的接口。 基础实现类： TypeConverterSupport
			 */
			public interface TypeConverter {}

			/**
			 * 用户可以通过此类对指定类型的属性值进行编辑。
			 * 
			 * 包含多种不同的获取/设置属性值的方法，大部分属性编辑器只需要实现部分方法即可。
			 */
			public interface PropertyEditor {}

			/**
			 * BeanWrapper的默认实现类（可以满足所有的常用场景，其中为了更高效使用了自省缓存）
			 */
			public class BeanWrapperImpl extends AbstractNestablePropertyAccessor implements BeanWrapper {}

			/**
			 * 包含单独bean属性的信息和值。 使用object而非map，可以更加灵活，在处理有索引的属性时也更有优势。
			 */ 
			public class PropertyValue extends BeanMetadataAttributeAccessor implements Serializable {}

			/**
			 * 支持访问和设置任意对象属性。 基础实现类： AttributeAccessorSupport
			 */			
			public interface AttributeAccessor {}

			/**
			 * 包含一个配置源对象的bean数据源接口
			 */
			public interface BeanMetadataElement {}


		3.3.1. Setting and Getting Basic and Nested Properties

			java:

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

				-----------------------

				usage:

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

		3.3.2. Built-in PropertyEditor Implementations		

			Spring已实现的属性编辑器目录： org/springframework/beans/propertyeditors

			Registering Additional Custom PropertyEditor Implementations

				i.e:

					java:

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

						public class ExoticTypeEditor extends PropertyEditorSupport {

						    public void setAsText(String text) {
						        setValue(new ExoticType(text.toUpperCase()));
						    }
						}

					xml:
					
						<bean id="sample" class="example.DependsOnExoticType">
						    <property name="type" value="aNameForExoticType"/>
						</bean>

						<bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
					    	<property name="customEditors">
						        <map>
						            <entry key="example.ExoticType" value="example.ExoticTypeEditor"/>
						        </map>
						    </property>
						</bean>	

				Using PropertyEditorRegistrar

					java:

						public final class CustomPropertyEditorRegistrar implements PropertyEditorRegistrar {

						    public void registerCustomEditors(PropertyEditorRegistry registry) {

						        // it is expected that new PropertyEditor instances are created
						        registry.registerCustomEditor(ExoticType.class, new ExoticTypeEditor());

						        // you could register as many custom property editors as are required here...
						    }
						}

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

					xml:
					
						<bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
						    <property name="propertyEditorRegistrars">
						        <list>
						            <ref bean="customPropertyEditorRegistrar"/>
						        </list>
						    </property>
						</bean>

						<bean id="customPropertyEditorRegistrar" class="com.foo.editors.spring.CustomPropertyEditorRegistrar"/>	

			// done 2020-2-29 19:14:49
			
	3.4. Spring Type Conversion

		package目录： core.convert 

		3.4.1. Converter SPI

			java: 

				package org.springframework.core.convert.converter;

				public interface Converter<S, T> {

				    T convert(S source);
				}

				-------------------

				package org.springframework.core.convert.support;

				final class StringToInteger implements Converter<String, Integer> {

				    public Integer convert(String source) {
				        return Integer.valueOf(source);
				    }
				}

		3.4.2. Using ConverterFactory
		
			当需要集中化一个类及其所有子类的转换逻辑，可以实现ConverterFactory

				java:

					package org.springframework.core.convert.converter;

					public interface ConverterFactory<S, R> {

					    <T extends R> Converter<S, T> getConverter(Class<T> targetType);
					}	

					-------------------

					package org.springframework.core.convert.support;

					final class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {

					    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
					        return new StringToEnumConverter(targetType);
					    }

					    private final class StringToEnumConverter<T extends Enum> implements Converter<String, T> {

					        private Class<T> enumType;

					        public StringToEnumConverter(Class<T> enumType) {
					            this.enumType = enumType;
					        }

					        public T convert(String source) {
					            return (T) Enum.valueOf(this.enumType, source.trim());
					        }
					    }
					}

		3.4.3. Using GenericConverter
		
			如果需要更复杂的转换实现类，可以实现GenericConverter接口。它比Convertor更灵活，强转更少。 （该接口时最复杂的，所以优先考虑使用Converter和ConverterFactory）

				java:

					package org.springframework.core.convert.converter;

					public interface GenericConverter {

					    public Set<ConvertiblePair> getConvertibleTypes();

					    Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);
					}	

					---------------------

					final class ArrayToCollectionConverter implements ConditionalGenericConverter {

					private final ConversionService conversionService;


					public ArrayToCollectionConverter(ConversionService conversionService) {
						this.conversionService = conversionService;
					}


					@Override
					public Set<ConvertiblePair> getConvertibleTypes() {
						return Collections.singleton(new ConvertiblePair(Object[].class, Collection.class));
					}

					@Override
					public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
						return ConversionUtils.canConvertElements(
								sourceType.getElementTypeDescriptor(), targetType.getElementTypeDescriptor(), this.conversionService);
					}

					@Override
					@Nullable
					public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
						if (source == null) {
							return null;
						}

						int length = Array.getLength(source);
						TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
						Collection<Object> target = CollectionFactory.createCollection(targetType.getType(),
								(elementDesc != null ? elementDesc.getType() : null), length);

						if (elementDesc == null) {
							for (int i = 0; i < length; i++) {
								Object sourceElement = Array.get(source, i);
								target.add(sourceElement);
							}
						}
						else {
							for (int i = 0; i < length; i++) {
								Object sourceElement = Array.get(source, i);
								Object targetElement = this.conversionService.convert(sourceElement,
										sourceType.elementTypeDescriptor(sourceElement), elementDesc);
								target.add(targetElement);
							}
						}
						return target;
					}

				}

			Using ConditionalGenericConverter

				如果想要在特定条件下才进行转换时（比如有某些特定注解，特定方法）。可以考虑ConditionalGenericConverter（）

				java:

					public interface ConditionalConverter {

					    boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType);
					}

					public interface ConditionalGenericConverter extends GenericConverter, ConditionalConverter {
					}

		3.4.4. The ConversionService API

			ConversionService为类型转换定义了一个统一的接口。（其他转换类一般在该门面接口中执行）

			java:

				package org.springframework.core.convert;

				public interface ConversionService {

				    boolean canConvert(Class<?> sourceType, Class<?> targetType);

				    <T> T convert(Object source, Class<T> targetType);

				    boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType);

				    Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);

				}

				-------------------------

				GenericConversionService 适用于大多数情况的常规实现类

				ConversionServiceFactory 创建常用conversionService配置的工厂类

		3.4.5. Configuring a ConversionService
		
			注册：

				xml:

					<bean id="conversionService" class="org.springframework.context.support.ConversionServiceFactoryBean"/>

					<bean id="conversionService" class="org.springframework.context.support.ConversionServiceFactoryBean">
					    <property name="converters">
					        <set>
					            <bean class="example.MyCustomConverter"/>
					        </set>
					    </property>
					</bean>

		3.4.6. Using a ConversionService Programmatically
		
			java:

				@Service
				public class MyService {

				    public MyService(ConversionService conversionService) {
				        this.conversionService = conversionService;
				    }

				    public void doIt() {
				        this.conversionService.convert(...)
				    }
				}	

				-------------------

				DefaultConversionService cs = new DefaultConversionService();

				List<Integer> input = ...
				cs.convert(input,
				    TypeDescriptor.forObject(input), // List<Integer> type descriptor
				    TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(String.class)));

			tips:
				
				DefaultConversionService会环境中大多数转换器。包括集合，scalar，基本的对象->string。

				我们可以通过DefaultConversionService.addDefaultConverters添加自定义的转换器。

				对于数组/集合中的值类型，转换器是可以复用的，所以无需单独创建一个从Collection<S>到Collection<T>的转换器。

		// done 2020-3-1 12:15:17	

	3.5. Spring Field Formatting
	
		3.5.1. The Formatter SPI

			接口定义：

				java:

					public interface Formatter<T> extends Printer<T>, Parser<T> {}	

					@FunctionalInterface
					public interface Printer<T> {

						String print(T object, Locale locale);

					}

					@FunctionalInterface
					public interface Parser<T> {

						T parse(String text, Locale locale) throws ParseException;

					}

			实现类：

				java:

					public final class DateFormatter implements Formatter<Date> {

					    private String pattern;

					    public DateFormatter(String pattern) {
					        this.pattern = pattern;
					    }

					    public String print(Date date, Locale locale) {
					        if (date == null) {
					            return "";
					        }
					        return getDateFormat(locale).format(date);
					    }

					    public Date parse(String formatted, Locale locale) throws ParseException {
					        if (formatted.length() == 0) {
					            return null;
					        }
					        return getDateFormat(locale).parse(formatted);
					    }

					    protected DateFormat getDateFormat(Locale locale) {
					        DateFormat dateFormat = new SimpleDateFormat(this.pattern, locale);
					        dateFormat.setLenient(false);
					        return dateFormat;
					    }
					}
						
		3.5.2. Annotation-driven Formatting
		
			字段格式化可以通过字段类型或注解进行配置。 可以通过AnnotationFormatterFactory接口来绑定格式化注解。	

			接口定义：

				public interface AnnotationFormatterFactory<A extends Annotation> {

				    Set<Class<?>> getFieldTypes();

				    Printer<?> getPrinter(A annotation, Class<?> fieldType);

				    Parser<?> getParser(A annotation, Class<?> fieldType);
				}			

			实现类:

				public class DateTimeFormatAnnotationFormatterFactory  extends EmbeddedValueResolutionSupport
						implements AnnotationFormatterFactory<DateTimeFormat> {

					private static final Set<Class<?>> FIELD_TYPES;

					static {
						Set<Class<?>> fieldTypes = new HashSet<>(4);
						fieldTypes.add(Date.class);
						fieldTypes.add(Calendar.class);
						fieldTypes.add(Long.class);
						FIELD_TYPES = Collections.unmodifiableSet(fieldTypes);
					}


					@Override
					public Set<Class<?>> getFieldTypes() {
						return FIELD_TYPES;
					}

					@Override
					public Printer<?> getPrinter(DateTimeFormat annotation, Class<?> fieldType) {
						return getFormatter(annotation, fieldType);
					}

					@Override
					public Parser<?> getParser(DateTimeFormat annotation, Class<?> fieldType) {
						return getFormatter(annotation, fieldType);
					}

					protected Formatter<Date> getFormatter(DateTimeFormat annotation, Class<?> fieldType) {
						DateFormatter formatter = new DateFormatter();
						String style = resolveEmbeddedValue(annotation.style());
						if (StringUtils.hasLength(style)) {
							formatter.setStylePattern(style);
						}
						formatter.setIso(annotation.iso());
						String pattern = resolveEmbeddedValue(annotation.pattern());
						if (StringUtils.hasLength(pattern)) {
							formatter.setPattern(pattern);
						}
						return formatter;
					}

				}

			------------------------

			Format Annotation API	

				@Documented
				@Retention(RetentionPolicy.RUNTIME)
				@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
				public @interface DateTimeFormat {

					String style() default "SS";

					ISO iso() default ISO.NONE;

					String pattern() default "";

					enum ISO {

						DATE,

						TIME,

						DATE_TIME,

						NONE

					}

				}	

			用法：
			
				public static class DateTimeBean {

					private LocalDate localDate;

					@DateTimeFormat(style = "M-")
					private LocalDate localDateAnnotated;

					private LocalTime localTime;

					@DateTimeFormat(style = "-M")
					private LocalTime localTimeAnnotated;

					private LocalDateTime localDateTime;

					@DateTimeFormat(style = "MM")
					private LocalDateTime localDateTimeAnnotated;

					@DateTimeFormat(pattern = "M/d/yy h:mm a")
					private LocalDateTime dateTimeAnnotatedPattern;

					@DateTimeFormat(iso = ISO.DATE)
					private LocalDate isoDate;

					@DateTimeFormat(iso = ISO.TIME)
					private LocalTime isoTime;

					@DateTimeFormat(iso = ISO.DATE_TIME)
					private LocalDateTime isoDateTime;

					private Instant instant;

					private Period period;

					private Duration duration;

					private Year year;

					private Month month;

					private YearMonth yearMonth;

					private MonthDay monthDay;

				}	

		3.5.3. The FormatterRegistry SPI
		
			接口定义： 

				// 字段格式化规则的注册器
				public interface FormatterRegistry extends ConverterRegistry {

					void addPrinter(Printer<?> printer);

					void addParser(Parser<?> parser);

					void addFormatter(Formatter<?> formatter);

					void addFormatterForFieldType(Class<?> fieldType, Formatter<?> formatter);

					void addFormatterForFieldType(Class<?> fieldType, Printer<?> printer, Parser<?> parser);

					void addFormatterForFieldAnnotation(AnnotationFormatterFactory<? extends Annotation> annotationFormatterFactory);

				}

				// 类型转换器的注册器
				public interface ConverterRegistry {

					void addConverter(Converter<?, ?> converter);

					<S, T> void addConverter(Class<S> sourceType, Class<T> targetType, Converter<? super S, ? extends T> converter);

					void addConverter(GenericConverter converter);

					void addConverterFactory(ConverterFactory<?, ?> factory);

					void removeConvertible(Class<?> sourceType, Class<?> targetType);

				}

		3.5.4. The FormatterRegistrar SPI	

			接口定义：

				// 通过FormatterRegistry接口，使用FormattingConversionService注册Converters类型转换器和Formatters格式化工具
				public interface FormatterRegistrar {

				    void registerFormatters(FormatterRegistry registry);
				}

			实现类：
			
				public class DateFormatterRegistrar implements FormatterRegistrar {

					@Nullable
					private DateFormatter dateFormatter;

					public void setFormatter(DateFormatter dateFormatter) {
						Assert.notNull(dateFormatter, "DateFormatter must not be null");
						this.dateFormatter = dateFormatter;
					}

					@Override
					public void registerFormatters(FormatterRegistry registry) {
						addDateConverters(registry);
						// In order to retain back compatibility we only register Date/Calendar
						// types when a user defined formatter is specified (see SPR-10105)
						if (this.dateFormatter != null) {
							registry.addFormatter(this.dateFormatter);
							registry.addFormatterForFieldType(Calendar.class, this.dateFormatter);
						}
						registry.addFormatterForFieldAnnotation(new DateTimeFormatAnnotationFormatterFactory());
					}

					/**
					 * Add date converters to the specified registry.
					 * @param converterRegistry the registry of converters to add to
					 */
					public static void addDateConverters(ConverterRegistry converterRegistry) {
						converterRegistry.addConverter(new DateToLongConverter());
						converterRegistry.addConverter(new DateToCalendarConverter());
						converterRegistry.addConverter(new CalendarToDateConverter());
						converterRegistry.addConverter(new CalendarToLongConverter());
						converterRegistry.addConverter(new LongToDateConverter());
						converterRegistry.addConverter(new LongToCalendarConverter());
					}


					private static class DateToLongConverter implements Converter<Date, Long> {

						@Override
						public Long convert(Date source) {
							return source.getTime();
						}
					}


					private static class DateToCalendarConverter implements Converter<Date, Calendar> {

						@Override
						public Calendar convert(Date source) {
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(source);
							return calendar;
						}
					}


					private static class CalendarToDateConverter implements Converter<Calendar, Date> {

						@Override
						public Date convert(Calendar source) {
							return source.getTime();
						}
					}


					private static class CalendarToLongConverter implements Converter<Calendar, Long> {

						@Override
						public Long convert(Calendar source) {
							return source.getTimeInMillis();
						}
					}


					private static class LongToDateConverter implements Converter<Long, Date> {

						@Override
						public Date convert(Long source) {
							return new Date(source);
						}
					}


					private static class LongToCalendarConverter implements Converter<Long, Calendar> {

						@Override
						public Calendar convert(Long source) {
							Calendar calendar = Calendar.getInstance();
							calendar.setTimeInMillis(source);
							return calendar;
						}
					}

				}	

		3.5.5. Configuring Formatting in Spring MVC
		
	3.6. Configuring a Global Date and Time Format
	
		默认情况下，没有添加注解@DateTimeFormat的时间日期字段，类型转换时使用默认样式DateFormat.SHORT。 可以通过修改全局格式配置来改变此默认值。

		但要确保Spring没有注册别的格式化程序。

		i.e:	

			java:			

				@Configuration
				public class AppConfig {

				    @Bean
				    public FormattingConversionService conversionService() {

				        // Use the DefaultFormattingConversionService but do not register defaults
				        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);

				        // Ensure @NumberFormat is still supported
				        conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());

				        // Register JSR-310 date conversion with a specific global format
				        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
				        registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyyMMdd"));
				        registrar.registerFormatters(conversionService);

				        // Register date conversion with a specific global format
				        DateFormatterRegistrar registrar = new DateFormatterRegistrar();
				        registrar.setFormatter(new DateFormatter("yyyyMMdd"));
				        registrar.registerFormatters(conversionService);

				        return conversionService;
				    }
				}

			xml:
			
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

	3.7. Java Bean Validation
	
		3.7.1. Overview of Bean Validation

			Bean Validation API综合信息： https://beanvalidation.org/

			Hibernate Validator 文档： https://hibernate.org/validator/ 

			i.e:

				java:

					public class PersonForm {

					    @NotNull
					    @Size(max=64)
					    private String name;

					    @Min(0)
					    private int age;
					}

	3.7.2. Configuring a Bean Validation Provider
	
		Spring中配置默认的校验器:

			@Configuration
			public class AppConfig {

			    @Bean
			    public LocalValidatorFactoryBean validator() {
			        return new LocalValidatorFactoryBean;
			    }
			}

		Injecting a Validator
		
			import javax.validation.Validator;

			@Service
			public class MyService {

			    @Autowired
			    private Validator validator;
			}	

			------------------------or 

			import org.springframework.validation.Validator;

			@Service
			public class MyService {

			    @Autowired
			    private Validator validator;
			}

		Configuring Custom Constraints

			每个bean校验的约束都包含两部分：
				1. 代表约束和配置属性的@Contraint注解
				2. javax.validation.ConstraintValidator校验器实现类，包含校验的具体规则

			接口定义：
			
				public interface ConstraintValidator<A extends Annotation, T> {

					default void initialize(A constraintAnnotation) {}

					boolean isValid(T value, ConstraintValidatorContext context);

				}	

				@Documented
				@Target({ ANNOTATION_TYPE })
				@Retention(RUNTIME)
				public @interface Constraint {

					Class<? extends ConstraintValidator<?, ?>>[] validatedBy();

				}

			i.e:	
		
			   @Documented
			   @Constraint(validatedBy = OrderNumberValidator.class)
			   @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
			   @Retention(RUNTIME)
			   public @interface OrderNumber {
			       String message() default "{com.acme.constraint.OrderNumber.message}";
			       Class<?>[] groups() default {};
			       Class<? extends Payload>[] payload() default {};
			   }

			   public class OrderNumberValidator implements ConstraintValidator {

				    @Autowired;
				    private Foo aDependency;

				    // ...
				}

			Spring-driven Method Validation

				同样可以继承方法校验规则（已被Bean Validation1.1支持）

				配置：
			
					@Configuration
					public class AppConfig {

					    @Bean
					    public MethodValidationPostProcessor validationPostProcessor() {
					        return new MethodValidationPostProcessor;
					    }
					}	

				用法:

					public @NotNull Object myValidMethod(@NotNull String arg1, @Max(10) int arg2)

				说明：
					
					为了能查到内部的约束注解，需要在类型上添加@Validated注解

		3.7.3. Configuring a DataBinder
		
			Spring3.0+以后，可以在DataBinder实例中配置校验器Validator，一旦配置成功，便可以通过调用binder.validate()调用校验器，所有的校验异常信息都会自动封装到其BindingResult中。

			也可以通过dataBinder.addValidators，dataBinder.replaceValidators给DataBinder实例配置多个校验器。

			用法：

				Foo target = new Foo();
				DataBinder binder = new DataBinder(target);
				binder.setValidator(new FooValidator());

				// bind to the target object
				binder.bind(propertyValues);

				// validate the target object
				binder.validate();

				// get BindingResult that includes any validation errors
				BindingResult results = binder.getBindingResult();

		3.7.4. Spring MVC 3 Validation
		
			默认情况下，如果类路径下存在Bean Validation实现类（比如 Hibernate Validator），LocalValidatorFactoryBean 就会自动注册为全局的校验器。

			配置：

				java:

					@Configuration
					@EnableWebMvc
					public class WebConfig implements WebMvcConfigurer {

					    @Override
					    public Validator getValidator() {
					        // ...
					    }
					}

				xml:
				
					<?xml version="1.0" encoding="UTF-8"?>
					<beans xmlns="http://www.springframework.org/schema/beans"
					    xmlns:mvc="http://www.springframework.org/schema/mvc"
					    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
					    xsi:schemaLocation="
					        http://www.springframework.org/schema/beans
					        https://www.springframework.org/schema/beans/spring-beans.xsd
					        http://www.springframework.org/schema/mvc
					        https://www.springframework.org/schema/mvc/spring-mvc.xsd">

					    <mvc:annotation-driven validator="globalValidator"/>

					</beans>	

			注册校验器实现类：
			
				@Controller
				public class MyController {

				    @InitBinder
				    protected void initBinder(WebDataBinder binder) {
				        binder.addValidators(new FooValidator());
				    }
				}	

			说明：
				
				如果已经有其他校验器的情况下，想使用LocalValidatorFactoryBean，可以在该bean上添加@Primary注解防止冲突。

		// done 2020-3-1 19:05:06

			相关类说明：

				ResolvableType: 

					说明：封装了java.lang.refelect.Type类型，能够对Class进行基本解析，还可以从字段、方法参数、方法返回、Class类型解析生成，完成之后可以获取其父类、接口、常规参数等。

					用法：

						public class Demo {

							private HashMap<Integer, List<String>> myMap;

							public void example() {

								ResolvableType t = ResolvableType.forField(getClass().getDeclaredField("myMap"));
								t.getSuperType(); // AbstractMap<Integer, List<String>>
								t.asMap(); // Map<Integer, List<String>>
								t.getGeneric(0).resolve(); // Integer
								t.getGeneric(1).resolve(); // List
								t.getGeneric(1); // List<String>
								t.resolveGeneric(1, 0); // String

							}

						}


				TypeDescriptor: 

					说明：类型相关信息说明，包括数组/集合。

					用法：

						public class Demo {

							@Test
							void parameterPrimitive() throws Exception {
								TypeDescriptor desc = new TypeDescriptor(new MethodParameter(getClass().getMethod("testParameterPrimitive", int.class), 0));
								assertThat(desc.getType()).isEqualTo(int.class);
								assertThat(desc.getObjectType()).isEqualTo(Integer.class);
								assertThat(desc.getName()).isEqualTo("int");
								assertThat(desc.toString()).isEqualTo("int");
								assertThat(desc.isPrimitive()).isTrue();
								assertThat(desc.getAnnotations().length).isEqualTo(0);
								assertThat(desc.isCollection()).isFalse();
								assertThat(desc.isMap()).isFalse();
							}

							public void testParameterPrimitive(int primitive) {}	

						}
		
4. Spring Expression Language (SpEL)				

	相关类：

		// 包含每个表达式的解析状态，发生改变时对其他表达式不可见，可以保存本地变量，复杂表达式中的表达式组件之间进行信息沟通。也为不同的树节点定义了常用的通用程序。
		org.springframework.expression.spel.ExpressionState[class]

		// 表达式解析（包括其中的对象引用解析）在此类中进行。 默认实现类： org.springframework.expression.spel.support.StandardEvaluationContext
		org.springframework.expression.EvaluationContext[interface]

		// 转换表达式的AST树节点
		org.springframework.expression.spel.SpelNode[interface]

		// JVM操作码
		org.springframework.asm.Opcodes[interface]

		// SpEL表达式解析的所有AST节点的通用父类
		org.springframework.expression.spel.ast.SpelNodeImpl[abstract]

		// 将String字符串转换为可解析的编译表达式，支持转换模板和标准的表达式字符串。
		org.springframework.expression.ExpressionParser[interface]

		// 可解析模板的表达式解析器，可以作为其他被还未支持模板解析的父类
		org.springframework.expression.common.TemplateAwareExpressionParser[abstract]

		// 手写的SpEL解析器，实例可以重复使用，但非线程安全
		org.springframework.expression.spel.standard.InternalSpelExpressionParser

		// SpEL解析器，实例可以重复使用，但非线程安全
		org.springframework.expression.spel.standard.SpelExpressionParser

		// SpEL解析器配置信息对象
		org.springframework.expression.spel.SpelParserConfiguration

		// 可以解析对象的表达式，封装了已转换的字符串表达式的详细信息，表达式解析的通用抽象接口。
		org.springframework.expression.Expression[interface]

		// 已转换待解析的SpEL表达式，可以单独或在上下文中解析。 表达式解析包括types、beans、properties和methods等元素的解析。
		org.springframework.expression.spel.standard.SpelExpression

		// 可以将输入数据转换为可解析的标识流
		org.springframework.expression.spel.standard.Tokenizer

		// 特定标识（输入数据的信息和位置）的持有者
		org.springframework.expression.spel.standard.Token

		// 包含一个object对象和它的类型描述信息
		org.springframework.expression.TypedValue

	相关数据结构：
	
		AST: Astract Syntax Tree, 抽象语法树。 是源代码语法结构的一种抽象表示。它以树状的形式表现编程语言的语法结构，树上的每个节点都表示源代码中的一种结构。

		Deque: double-ended queue, 双端列队。 具有列队和栈的性质，双端列队中的元素可以从两端弹出，插入/删除操作也在列队两端进行。

		Stack: 栈。 后进push先出pop。（如子弹压膛）

		Queue: 列队。 先进先出。 （如安检）

	4.1. Evaluation
	
		i.e:	

			java:

				// String表达式
				ExpressionParser parser = new SpelExpressionParser();
				Expression exp = parser.parseExpression("'Hello World'"); 
				String message = (String) exp.getValue();

				// 方法调用: object.invoke()
				ExpressionParser parser = new SpelExpressionParser();
				Expression exp = parser.parseExpression("'Hello World'.concat('!')"); 
				String message = (String) exp.getValue();

				// 方法调用: .bytes = invokes 'getBytes()'
				ExpressionParser parser = new SpelExpressionParser();
				Expression exp = parser.parseExpression("'Hello World'.bytes"); 
				byte[] bytes = (byte[]) exp.getValue();

				// 属性获取: prop1.prop2.prop3
				ExpressionParser parser = new SpelExpressionParser();
				Expression exp = parser.parseExpression("'Hello World'.bytes.length"); 
				int length = (Integer) exp.getValue();

				// 构造器调用:
				ExpressionParser parser = new SpelExpressionParser();
				Expression exp = parser.parseExpression("new String('hello world').toUpperCase()"); 
				String message = exp.getValue(String.class);

				// 对象解析
				// Create and set a calendar
				GregorianCalendar c = new GregorianCalendar();
				c.set(1856, 7, 9);

				// The constructor arguments are name, birthday, and nationality.
				Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");

				ExpressionParser parser = new SpelExpressionParser();

				Expression exp = parser.parseExpression("name"); // Parse name as an expression
				String name = (String) exp.getValue(tesla);
				// name == "Nikola Tesla"

				exp = parser.parseExpression("name == 'Nikola Tesla'");
				boolean result = exp.getValue(tesla, Boolean.class);
				// result == true

		4.1.1. Understanding EvaluationContext

			EvaluationContext： 表达式解析（包括其中的对象引用解析）在此类中进行。 实现类： 

				// 简单实现类
				org.springframework.expression.spel.support.SimpleEvaluationContext

				// 标准实现类（默认）
				org.springframework.expression.spel.support.StandardEvaluationContext

			Type Conversion

				默认使用的是Spring中的类型转换器ConversionService。如果SpEL解析过程中需要进行类型转化，则会自动调用类型转换器。
			
				i.e:

					java:

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

		4.1.2. Parser Configuration	

			可以通过SpelParserConfiguration对SpEL转换器进行自定义配置。

			i.e:

				java:

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

		4.1.3. SpEL Compilation
		
			Compiler Configuration

				编译器默认未开启，可以通过两种方法进行开启。

				编译器有三种模式：

					OFF (default): 关闭
					IMMEDIATE: 即时开启，表达式会尽早编译（在第一次解析拦截后）。编译失败会抛出异常。
					MIXED: 混合模式，异常会被内部处理。

				i.e:
				
					java:

						SpelParserConfiguration config = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, this.getClass().getClassLoader());

						SpelExpressionParser parser = new SpelExpressionParser(config);

						Expression expr = parser.parseExpression("payload");

						MyMessage message = new MyMessage();

						Object payload = expr.getValue(message);

					properties:
					
						spring.expression.compiler.mode = off/immediate/mixed		

			Compiler Limitations			

				Spring4.1以后，基础的编译框架已完成，但是还未支持所有种类的表达式编译（通用表达式已支持）。当前版本中，以下情形还未支持编译：

					Expressions involving assignment

					Expressions relying on the conversion service

					Expressions using custom resolvers or accessors

					Expressions using selection or projection

		// done 2020-3-3 12:46:06				

	4.2. Expressions in Bean Definitions

		格式： #{ <expression string> }				

		4.2.1. XML Configuration

			i.e:

				<!-- 构造器参数/字段属性值 -->

					<bean id="numberGuess" class="org.spring.samples.NumberGuess">
					    <property name="randomNumber" value="#{ T(java.lang.Math).random() * 100.0 }"/>

					    <!-- other properties -->
					</bean>

				<!-- 预先定义的系统属性 -->

					<bean id="taxCalculator" class="org.spring.samples.TaxCalculator">
					    <property name="defaultLocale" value="#{ systemProperties['user.region'] }"/>

					    <!-- other properties -->
					</bean>

				<!-- 引用别的bean中的属性值 -->

					<bean id="numberGuess" class="org.spring.samples.NumberGuess">
					    <property name="randomNumber" value="#{ T(java.lang.Math).random() * 100.0 }"/>

					    <!-- other properties -->
					</bean>

					<bean id="shapeGuess" class="org.spring.samples.ShapeGuess">
					    <property name="initialShapeSeed" value="#{ numberGuess.randomNumber }"/>

					    <!-- other properties -->
					</bean>

		4.2.2. Annotation Configuration			

			可以通过在字段、方法、构造器参数上添加@Value注解来设置默认值。

			i.e:

				// 字段

					public class FieldValueTestBean {

					    @Value("#{ systemProperties['user.region'] }")
					    private String defaultLocale;

					    public void setDefaultLocale(String defaultLocale) {
					        this.defaultLocale = defaultLocale;
					    }

					    public String getDefaultLocale() {
					        return this.defaultLocale;
					    }
					}

				// setter方法
				
					public class PropertyValueTestBean {

					    private String defaultLocale;

					    @Value("#{ systemProperties['user.region'] }")
					    public void setDefaultLocale(String defaultLocale) {
					        this.defaultLocale = defaultLocale;
					    }

					    public String getDefaultLocale() {
					        return this.defaultLocale;
					    }
					}	

				// @Autowired方法
				
					public class SimpleMovieLister {

					    private MovieFinder movieFinder;
					    private String defaultLocale;

					    @Autowired
					    public void configure(MovieFinder movieFinder,
					            @Value("#{ systemProperties['user.region'] }") String defaultLocale) {
					        this.movieFinder = movieFinder;
					        this.defaultLocale = defaultLocale;
					    }

					    // ...
					}	

				// 构造器
				
					public class MovieRecommender {

					    private String defaultLocale;

					    private CustomerPreferenceDao customerPreferenceDao;

					    public MovieRecommender(CustomerPreferenceDao customerPreferenceDao,
					            @Value("#{systemProperties['user.country']}") String defaultLocale) {
					        this.customerPreferenceDao = customerPreferenceDao;
					        this.defaultLocale = defaultLocale;
					    }

					    // ...
					}

	4.3. Language Reference

		4.3.1. Literal Expressions

			字符串用一个单引号标识，字符用两个单引号标识。

			i.e: 

				ExpressionParser parser = new SpelExpressionParser();

				// evals to "Hello World"
				String helloWorld = (String) parser.parseExpression("'Hello World'").getValue();

				double avogadrosNumber = (Double) parser.parseExpression("6.0221415E+23").getValue();

				// evals to 2147483647
				int maxValue = (Integer) parser.parseExpression("0x7FFFFFFF").getValue();

				boolean trueValue = (Boolean) parser.parseExpression("true").getValue();

				Object nullValue = parser.parseExpression("null").getValue();

		4.3.2. Properties, Arrays, Lists, Maps, and Indexers

			首字符大小写无关。
		
			i.e:

				// Properties（点.）

					// evals to 1856
					int year = (Integer) parser.parseExpression("Birthdate.Year + 1900").getValue(context);

					String city = (String) parser.parseExpression("placeOfBirth.City").getValue(context);

				// Arrays/Lists（方括号[]）

					ExpressionParser parser = new SpelExpressionParser();
					EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();

					// Inventions Array

					// evaluates to "Induction motor"
					String invention = parser.parseExpression("inventions[3]").getValue(
					        context, tesla, String.class);

					// Members List

					// evaluates to "Nikola Tesla"
					String name = parser.parseExpression("Members[0].Name").getValue(
					        context, ieee, String.class);

					// List and Array navigation
					// evaluates to "Wireless communication"
					String invention = parser.parseExpression("Members[0].Inventions[6]").getValue(
					        context, ieee, String.class);	

			    // Maps （方括号['key']）     

					// Officer's Dictionary

					Inventor pupin = parser.parseExpression("Officers['president']").getValue(
					        societyContext, Inventor.class);

					// evaluates to "Idvor"
					String city = parser.parseExpression("Officers['president'].PlaceOfBirth.City").getValue(
					        societyContext, String.class);

					// setting values
					parser.parseExpression("Officers['advisors'][0].PlaceOfBirth.Country").setValue(
					        societyContext, "Croatia");  

	    4.3.3. Inline Lists
	    
          	使用花括号{}标识list

          	i.e:

          		// evaluates to a Java list containing the four numbers
				List numbers = (List) parser.parseExpression("{1,2,3,4}").getValue(context);

				List listOfLists = (List) parser.parseExpression("{{'a','b'},{'x','y'}}").getValue(context);

		4.3.4. Inline Maps
		
			使用{key:value}标识map

			i.e:

				// evaluates to a Java map containing the two entries
				Map inventorInfo = (Map) parser.parseExpression("{name:'Nikola',dob:'10-July-1856'}").getValue(context);

				Map mapOfMaps = (Map) parser.parseExpression("{name:{first:'Nikola',last:'Tesla'},dob:{day:10,month:'July',year:1856}}").getValue(context);	

		4.3.5. Array Construction
		
			使用java通用的数组表达方式即可，支持在创建时进行初始化（多元数组不支持）。

			i.e:

				int[] numbers1 = (int[]) parser.parseExpression("new int[4]").getValue(context);

				// Array with initializer
				int[] numbers2 = (int[]) parser.parseExpression("new int[]{1,2,3}").getValue(context);

				// Multi dimensional array
				int[][] numbers3 = (int[][]) parser.parseExpression("new int[4][5]").getValue(context);

		4.3.6. Methods
		
			可以使用java编码方式，也可以使用方法名。		

			i.e:

				// string literal, evaluates to "bc"
				String bc = parser.parseExpression("'abc'.substring(1, 3)").getValue(String.class);

				// evaluates to true
				boolean isMember = parser.parseExpression("isMember('Mihajlo Pupin')").getValue(societyContext, Boolean.class);	

		4.3.7. Operators
		
			Relational Operators

				i.e:

					// evaluates to true
					boolean trueValue = parser.parseExpression("2 == 2").getValue(Boolean.class);

					// evaluates to false
					boolean falseValue = parser.parseExpression("2 < -5.0").getValue(Boolean.class);

					// evaluates to true
					boolean trueValue = parser.parseExpression("'black' < 'block'").getValue(Boolean.class);

					// evaluates to false
					boolean falseValue = parser.parseExpression("'xyz' instanceof T(Integer)").getValue(Boolean.class);

					// evaluates to true
					boolean trueValue = parser.parseExpression("'5.00' matches '^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);

					//evaluates to false
					boolean falseValue = parser.parseExpression("'5.0067' matches '^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);

				tips:
				
					对于null值： X > null is always true, X < null is always false	

					对于基本类型自动装箱问题： 1 instanceof T(int) 为 false, 1 instanceof T(Integer) 为true

					支持字母表示关系型操作符： lt (<) gt (>) le (<=) ge (>=) eq (==) ne (!=) div (/) mod (%) not (!).


			Logical Operators

				支持的逻辑运算符包括： and (&&) or (||) not (!)

				i.e:

					// -- AND --

					// evaluates to false
					boolean falseValue = parser.parseExpression("true and false").getValue(Boolean.class);

					// evaluates to true
					String expression = "isMember('Nikola Tesla') and isMember('Mihajlo Pupin')";
					boolean trueValue = parser.parseExpression(expression).getValue(societyContext, Boolean.class);

					// -- OR --

					// evaluates to true
					boolean trueValue = parser.parseExpression("true or false").getValue(Boolean.class);

					// evaluates to true
					String expression = "isMember('Nikola Tesla') or isMember('Albert Einstein')";
					boolean trueValue = parser.parseExpression(expression).getValue(societyContext, Boolean.class);

					// -- NOT --

					// evaluates to false
					boolean falseValue = parser.parseExpression("!true").getValue(Boolean.class);

					// -- AND and NOT --
					String expression = "isMember('Nikola Tesla') and !isMember('Mihajlo Pupin')";
					boolean falseValue = parser.parseExpression(expression).getValue(societyContext, Boolean.class);

			Mathematical Operators

				数值类型支持： + - * / % ^

				字符串类型支持： +

				i.e:

					// Addition
					int two = parser.parseExpression("1 + 1").getValue(Integer.class);  // 2

					String testString = parser.parseExpression(
					        "'test' + ' ' + 'string'").getValue(String.class);  // 'test string'

					// Subtraction
					int four = parser.parseExpression("1 - -3").getValue(Integer.class);  // 4

					double d = parser.parseExpression("1000.00 - 1e4").getValue(Double.class);  // -9000

					// Multiplication
					int six = parser.parseExpression("-2 * -3").getValue(Integer.class);  // 6

					double twentyFour = parser.parseExpression("2.0 * 3e0 * 4").getValue(Double.class);  // 24.0

					// Division
					int minusTwo = parser.parseExpression("6 / -3").getValue(Integer.class);  // -2

					double one = parser.parseExpression("8.0 / 4e0 / 2").getValue(Double.class);  // 1.0

					// Modulus
					int three = parser.parseExpression("7 % 4").getValue(Integer.class);  // 3

					int one = parser.parseExpression("8 / 5 % 2").getValue(Integer.class);  // 1

					// Operator precedence
					int minusTwentyOne = parser.parseExpression("1+2-3*8").getValue(Integer.class);  // -21

			The Assignment Operator		

				通过等号=设置属性值 

				i.e:

					Inventor inventor = new Inventor();
					EvaluationContext context = SimpleEvaluationContext.forReadWriteDataBinding().build();

					parser.parseExpression("Name").setValue(context, inventor, "Aleksandar Seovic");

					// alternatively
					String aleks = parser.parseExpression("Name = 'Aleksandar Seovic'").getValue(context, inventor, String.class);

		4.3.8. Types
		
			可以使用T指定Class类型。 StandardEvaluationContext 使用 TypeLocator 来查找类型。 
			StandardTypeLocator 默认识别 java.lang包，所以，如果是java.lang包下，可以简写。其他包下需要全限定类名。

			i.e:

				Class dateClass = parser.parseExpression("T(java.util.Date)").getValue(Class.class);

				Class stringClass = parser.parseExpression("T(String)").getValue(Class.class);

				boolean trueValue = parser.parseExpression(
				        "T(java.math.RoundingMode).CEILING < T(java.math.RoundingMode).FLOOR")
				        .getValue(Boolean.class);

        4.3.9. Constructors

        	可以使用new调用构造器方法。 除了基本类型，都需要全限定类名。

        	i.e:

        		Inventor einstein = p.parseExpression(
				        "new org.spring.samples.spel.inventor.Inventor('Albert Einstein', 'German')")
				        .getValue(Inventor.class);

				//create new inventor instance within add method of List
				p.parseExpression(
				        "Members.add(new org.spring.samples.spel.inventor.Inventor(
				            'Albert Einstein', 'German'))").getValue(societyContext);


        4.3.10. Variables

        	可以通过#variableName语法来引用表达式中的变量。

        	i.e:

        		Inventor tesla = new Inventor("Nikola Tesla", "Serbian");

				EvaluationContext context = SimpleEvaluationContext.forReadWriteDataBinding().build();
				context.setVariable("newName", "Mike Tesla");

				parser.parseExpression("Name = #newName").getValue(context, tesla);
				System.out.println(tesla.getName())  // "Mike Tesla"

			The #this and #root Variables
			
				#this指向当前解析对象。 #root指向基础的上下文对象。

				i.e:

					// create an array of integers
					List<Integer> primes = new ArrayList<Integer>();
					primes.addAll(Arrays.asList(2,3,5,7,11,13,17));

					// create parser and set variable 'primes' as the array of integers
					ExpressionParser parser = new SpelExpressionParser();
					EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataAccess();
					context.setVariable("primes", primes);

					// all prime numbers > 10 from the list (using selection ?{...})
					// evaluates to [11, 13, 17]
					List<Integer> primesGreaterThanTen = (List<Integer>) parser.parseExpression(
					        "#primes.?[#this>10]").getValue(context);

        4.3.11. Functions	

        	可以自定义函数方法，便于在表达式中调用。

        	注册：

        		Method method = ...;

				EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
				context.setVariable("myFunction", method);

			使用:

				public abstract class StringUtils {

				    public static String reverseString(String input) {
				        StringBuilder backwards = new StringBuilder(input.length());
				        for (int i = 0; i < input.length(); i++) {
				            backwards.append(input.charAt(input.length() - 1 - i));
				        }
				        return backwards.toString();
				    }
				}

				----------------------

				ExpressionParser parser = new SpelExpressionParser();

				EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
				context.setVariable("reverseString",
				        StringUtils.class.getDeclaredMethod("reverseString", String.class));

				String helloWorldReversed = parser.parseExpression(
				        "#reverseString('hello')").getValue(context, String.class);

        4.3.12. Bean References

        	如果解析上下文已注册了bean解析器，则可以通过在表达式中添加@圈查找bean。

        	i.e:

        		ExpressionParser parser = new SpelExpressionParser();
				StandardEvaluationContext context = new StandardEvaluationContext();
				context.setBeanResolver(new MyBeanResolver());

				// This will end up calling resolve(context,"something") on MyBeanResolver during evaluation
				Object bean = parser.parseExpression("@something").getValue(context);

			查找FactoryBean，需要在表达式中添加&

			i.e:

				ExpressionParser parser = new SpelExpressionParser();
				StandardEvaluationContext context = new StandardEvaluationContext();
				context.setBeanResolver(new MyBeanResolver());

				// This will end up calling resolve(context,"&foo") on MyBeanResolver during evaluation
				Object bean = parser.parseExpression("&foo").getValue(context);	

		4.3.13. Ternary Operator (If-Then-Else) 

			三目运算符: boolean ? trueResult  : falseResult

			i.e:

				String falseString = parser.parseExpression("false ? 'trueExp' : 'falseExp'").getValue(String.class);

				parser.parseExpression("Name").setValue(societyContext, "IEEE");
				societyContext.setVariable("queryName", "Nikola Tesla");

				expression = "isMember(#queryName)? #queryName + ' is a member of the ' " +
				        "+ Name + ' Society' : #queryName + ' is not a member of the ' + Name + ' Society'";

				String queryResultString = parser.parseExpression(expression)
				        .getValue(societyContext, String.class);
				// queryResultString = "Nikola Tesla is a member of the IEEE Society"

		4.3.14. The Elvis Operator

			Elvis运算符: expression ?: otherResult // 如果expression不为空/false，返回本身，否则返回otherResult

			i.e:

				String name = "Elvis Presley";
				String displayName = (name != null ? name : "Unknown");


				ExpressionParser parser = new SpelExpressionParser();

				String name = parser.parseExpression("name?:'Unknown'").getValue(String.class);
				System.out.println(name);  // 'Unknown'


				ExpressionParser parser = new SpelExpressionParser();
				EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();

				Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
				String name = parser.parseExpression("Name?:'Elvis Presley'").getValue(context, tesla, String.class);
				System.out.println(name);  // Nikola Tesla

				tesla.setName(null);
				name = parser.parseExpression("Name?:'Elvis Presley'").getValue(context, tesla, String.class);
				System.out.println(name);  // Elvis Presley	

				// This will inject a system property pop3.port if it is defined or 25 if not.
				@Value("#{systemProperties['pop3.port'] ?: 25}")		
				private int port;

		4.3.15. Safe Navigation Operator

			安全导航操作符： object?.	property // 如果object不为空，返回object.property，否则返回null

			i.e:

				ExpressionParser parser = new SpelExpressionParser();
				EvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();

				Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
				tesla.setPlaceOfBirth(new PlaceOfBirth("Smiljan"));

				String city = parser.parseExpression("PlaceOfBirth?.City").getValue(context, tesla, String.class);
				System.out.println(city);  // Smiljan

				tesla.setPlaceOfBirth(null);
				city = parser.parseExpression("PlaceOfBirth?.City").getValue(context, tesla, String.class);
				System.out.println(city);  // null - does not throw NullPointerException!!!

		4.3.16. Collection Selection

			集合选择： .?[selectionExpression] // 过滤并返回该集合中满足条件表达式selectionExpression的元素集合

			i.e:

				List<Inventor> list = (List<Inventor>) parser.parseExpression("Members.?[Nationality == 'Serbian']").getValue(societyContext);

				Map newMap = parser.parseExpression("map.?[value<27]").getValue();

			tips:
			
				除了返回所有满足条件的元素外，也可以获取返回集合的首位元素：

					满足条件的第一个元素： 	.^[selectionExpression]

					满足条件的最后一个元素： .$[selectionExpression]

		4.3.17. Collection Projection

			集合项目： .![projectionExpression]  // 返回集合中子元素的集合

			i.e:

				// 返回Members中的所有inventors的出生地的集合
				// returns ['Smiljan', 'Idvor' ]
				List placesOfBirth = (List)parser.parseExpression("Members.![placeOfBirth.city]");

		4.3.18. Expression templating
		
			默认表达式模板： #{}

			自定义模板：

				public class TemplateParserContext implements ParserContext {

				    public String getExpressionPrefix() {
				        return "#{";
				    }

				    public String getExpressionSuffix() {
				        return "}";
				    }

				    public boolean isTemplate() {
				        return true;
				    }
				}

			i.e:

				String randomPhrase = parser.parseExpression(
			        "random number is #{T(java.lang.Math).random()}",
			        new TemplateParserContext()).getValue(String.class);		

	4.4. Classes Used in the Examples						

		i.e:

			import java.util.Date;
			import java.util.GregorianCalendar;

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

			-----------------------------

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

			-----------------------------


			import java.util.*;

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

	// done 2020-3-3 19:17:59

			# SpEL解析过程：
		
			代码块：

				SpelExpressionParser parser = new SpelExpressionParser();
				SpelExpression expr = parser.parseRaw("2      +    3");		
				assertThat(expr.getValue()).isEqualTo(5);

			执行逻辑：

				// 实例化SpEL表达式转化器
				SpelExpressionParser parser = new SpelExpressionParser();
				
					// 调用SpEL转化器的构造器
					org.springframework.expression.spel.standard.SpelExpressionParser	

						// 调用SpEL转化器配置的构造器，
						// 配置包括： 编译模式[默认OFF]、编译的类加载器[默认NULL]、是否自动添加null引用[默认false]、集合是否自动扩容[默认false]、集合扩容最大长度[默认Integer.MAX_VALUE]
						org.springframework.expression.spel.SpelParserConfiguration 

				// 转化SpEL表达式		
				SpelExpression expr = parser.parseRaw("2      +    3");		

					// 转化原始的SpEL表达式
					org.springframework.expression.spel.standard.SpelExpressionParser#parseRaw(String expressionString)

						// 根据转化上下文执行转化逻辑
						org.springframework.expression.spel.standard.SpelExpressionParser#doParseExpression(String expressionString, @Nullable ParserContext context)

							// 构造内置的SpEL转化器
							org.springframework.expression.spel.standard.InternalSpelExpressionParser#InternalSpelExpressionParser(SpelParserConfiguration configuration)

							// 使用内置的SpEL转化器转化表达式
							org.springframework.expression.spel.standard.InternalSpelExpressionParser#doParseExpression(String expressionString, @Nullable ParserContext context)

								// 根据SpEL表达式构造分词器： Tokenizer tokenizer = new Tokenizer(expressionString);
								org.springframework.expression.spel.standard.Tokenizer#Tokenizer(String inputData)

								// 将分词器处理为标识符集合： this.tokenStream = tokenizer.process();
								// 将字符解析为一个个标识符，如：数学运算符（+ - * / % ^）、逻辑运算符（and (&&) or (||) not (!)）、关系运算符（> == < != instanceof match）、赋值（=）
								org.springframework.expression.spel.standard.Tokenizer#process()

								// 将表达式一步步处理为AST树节点： SpelNodeImpl ast = eatExpression();
								// 将表达式中的各个标识符进行逻辑处理，如： 数学运算、逻辑运算、关系比较、赋值运算等等
								org.springframework.expression.spel.standard.InternalSpelExpressionParser#eatExpression()

								// 构造SpEL表达式： return new SpelExpression(expressionString, ast, this.configuration);
								org.springframework.expression.spel.standard.SpelExpression(String expression, SpelNodeImpl ast, SpelParserConfiguration configuration)


				// 获取SpEL表达式结果
				expr.getValue()

					// 获取SpEL表达式的结果值
					org.springframework.expression.spel.standard.SpelExpression#getValue()

						// 获取表达式解析上下文
						org.springframework.expression.spel.standard.SpelExpression#getEvaluationContext

							// 构造标准的表达式解析上下文
							org.springframework.expression.spel.support.StandardEvaluationContext#StandardEvaluationContext()

						// 构建表达式状态信息： ExpressionState expressionState = new ExpressionState(getEvaluationContext(), this.configuration);
						org.springframework.expression.spel.ExpressionState#ExpressionState(EvaluationContext context, SpelParserConfiguration configuration)

						// 获取SpEL节点结果值： Object result = this.ast.getValue(expressionState);
						org.springframework.expression.spel.ast.SpelNodeImpl#getValue(ExpressionState expressionState)

							// 获取内部的操作运算结果值（本例中为加法运算符）
							org.springframework.expression.spel.ast.OpPlus#getValueInternal(ExpressionState state)

						// 解析次数超过触发编译的阈值时，执行表达式编译
						// org.springframework.expression.spel.standard.SpelExpression#checkCompile(ExpressionState expressionState)	

						// 返回结果值： return result;

5. Aspect Oriented Programming with Spring	

	5.1. AOP Concepts		

	5.2. Spring AOP Capabilities and Goals

	5.3. AOP Proxies

	5.4. @AspectJ support 							

		5.4.1. Enabling @AspectJ Support

			Enabling @AspectJ Support with Java Configuration

				@Configuration
				@EnableAspectJAutoProxy
				public class AppConfig {

				}

			Enabling @AspectJ Support with XML Configuration

				<aop:aspectj-autoproxy/>

		5.4.2. Declaring an Aspect

		5.4.3. Declaring a Pointcut

			Supported Pointcut Designators

			Combining Pointcut Expressions

			Sharing Common Pointcut Definitions

			Examples

			Writing Good Pointcuts

		5.4.4. Declaring Advice

			Before Advice

			After Returning Advice

			After Throwing Advice

			After (Finally) Advice

			Around Advice

			Advice Parameters

				Access to the Current JoinPoint

				Passing Parameters to Advice

				Determining Argument Names

				Proceeding with Arguments

			Advice Ordering

		5.4.5. Introductions

			java:

				@Aspect
				public class UsageTracking {

				    @DeclareParents(value="com.xzy.myapp.service.*+", defaultImpl=DefaultUsageTracked.class)
				    public static UsageTracked mixin;

				    @Before("com.xyz.myapp.SystemArchitecture.businessService() && this(usageTracked)")
				    public void recordUsage(UsageTracked usageTracked) {
				        usageTracked.incrementUseCount();
				    }

				}	

		5.4.6. Aspect Instantiation Models

			默认，applicationContext中每个aspect切面都是单例的，切面调用每次都是调用该单例切面模型。 
			但是，我们可以修改切面的生命周期，Spring支持AspectJ中的perthis，pertarget实例模型（其他的，如percflow, percflowbelow, pertypewithin还不支持）

			i.e:

				@Aspect("perthis(com.xyz.myapp.SystemArchitecture.businessService())")
				public class MyAspect {

				    private int someState;

				    @Before(com.xyz.myapp.SystemArchitecture.businessService())
				    public void recordServiceUsage() {
				        // ...
				    }

				}

		5.4.7. An AOP Example	

			i.e:

				java:

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

					-------------------------

					@Retention(RetentionPolicy.RUNTIME)
					public @interface Idempotent {
					    // marker annotation
					}

					-------------------------

					@Around("com.xyz.myapp.SystemArchitecture.businessService() && " +
					        "@annotation(com.xyz.myapp.service.Idempotent)")
					public Object doConcurrentOperation(ProceedingJoinPoint pjp) throws Throwable {
					    // ...
					}

					-------------------------

				xml:
				
					<aop:aspectj-autoproxy/>

					<bean id="concurrentOperationExecutor" class="com.xyz.myapp.service.impl.ConcurrentOperationExecutor">
					    <property name="maxRetries" value="3"/>
					    <property name="order" value="100"/>
					</bean>	

		// to complete 2020-3-5 12:19:07

	5.5. Schema-based AOP Support
	
		module: spring-aop

		element: <aop:config>	

		tips: 
			<aop:config>格式的配置会使spring的自动代理机制重量化，如果已经了类似BeanNameAutoProxyCreator的自动代理技术会产生问题（比如添加的切面通知植入失败），
			所以，使用时要二选一。

		5.5.1. Declaring an Aspect	
		
			xml: 

				<aop:config>
				    <aop:aspect id="myAspect" ref="aBean">
				        ...
				    </aop:aspect>
				</aop:config>

				<bean id="aBean" class="...">
				    ...
				</bean>	

		5.5.2. Declaring a Pointcut

			xml:
		
				<aop:config>

				    <aop:pointcut id="businessService" expression="execution(* com.xyz.myapp.service.*.*(..))"/>

				    <aop:pointcut id="businessService" expression="com.xyz.myapp.SystemArchitecture.businessService()"/>

				</aop:config>	

				----------------------------

				<aop:config>

				    <aop:aspect id="myAspect" ref="aBean">

				        <aop:pointcut id="businessService"
				            expression="execution(* com.xyz.myapp.service.*.*(..))"/>

				        ...

				    </aop:aspect>

				</aop:config>	

				----------------------------

				<aop:config>

				    <aop:aspect id="myAspect" ref="aBean">

				        <aop:pointcut id="businessService" expression="execution(* com.xyz.myapp.service.*.*(..)) &amp;&amp; this(service)"/>

				        <!--  && || ! 也可以使用 and or not关键字代替 -->
				        <aop:pointcut id="businessService" expression="execution(* com.xyz.myapp.service.*.*(..)) and this(service)"/>

				        <aop:before pointcut-ref="businessService" method="monitor"/>

				        ...

				    </aop:aspect>

				</aop:config>

				----------------------------

			java:
			
				public void monitor(Object service) {
				    // ...
				}

		5.5.3. Declaring Advice

			Before Advice : 在匹配的方法执行前，执行该通知方法。

				xml:

					<aop:aspect id="beforeExample" ref="aBean">

					    <aop:before pointcut-ref="dataAccessOperation" method="doAccessCheck"/>

					    <!-- 也可以使用内部pointcut代替 -->	
					    <aop:before pointcut="execution(* com.xyz.myapp.dao.*.*(..))" method="doAccessCheck"/>

					    ...

					</aop:aspect>

			After Returning Advice : 在匹配的方法完成后，执行该通知方法。
			
				xml:

					<aop:aspect id="afterReturningExample" ref="aBean">

						<aop:after-returning pointcut-ref="dataAccessOperation" method="doAccessCheck"/>

						<!-- 也可以获取该返回值，但是必须在method方法参数中添加该返回值 -->
					    <aop:after-returning pointcut-ref="dataAccessOperation" returning="retVal" method="doAccessCheck"/>
					    ...

					</aop:aspect>		
		
				java:

					public void doAccessCheck(Object retVal) {...}

			After Throwing Advice : 在匹配的方法执行中抛出异常时，执行该通知方法。
			
				xml:

					<aop:aspect id="afterThrowingExample" ref="aBean">

						<aop:after-throwing pointcut-ref="dataAccessOperation" method="doRecoveryActions"/>

						<!-- 也可以获取该异常，但是必须在method方法参数中添加该异常 -->
					    <aop:after-throwing pointcut-ref="dataAccessOperation" throwing="dataAccessEx" method="doRecoveryActions"/>
					    ...

					</aop:aspect>		

				java:
				
					public void doRecoveryActions(DataAccessException dataAccessEx) {...}	

			After (Finally) Advice : 不管匹配的方法如何执行，之后都会执行该通知方法。
			
				xml:

					<aop:aspect id="afterFinallyExample" ref="aBean">

					    <aop:after
					        pointcut-ref="dataAccessOperation"
					        method="doReleaseLock"/>

					    ...

					</aop:aspect>

			Around Advice

				说明： 环绕匹配的方法运行周围。 可以是该方法执行前、执行后，还是自行决定何时执行、如何执行。通常用在线程安全的操作类中共享方法执行前后的状态信息。
			
				xml:

					<aop:aspect id="aroundExample" ref="aBean">

					    <aop:around
					        pointcut-ref="businessService"
					        method="doBasicProfiling"/>

					    ...

					</aop:aspect>

				java:
				
					// 该方法第一个参数必须是ProceedingJoinPoint，.proceed()方法是执行切面匹配的方法。
					public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable {
					    // start stopwatch
					    Object retVal = pjp.proceed();
					    // stop stopwatch
					    return retVal;
					}	

			Advice Parameters
			
				xml:

					<aop:before pointcut="com.xyz.lib.Pointcuts.anyPublicMethod() and @annotation(auditable)" method="audit" arg-names="auditable"/>

					------------------------

					<beans xmlns="http://www.springframework.org/schema/beans"
					    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
					    xmlns:aop="http://www.springframework.org/schema/aop"
					    xsi:schemaLocation="
					        http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
					        http://www.springframework.org/schema/aop https://www.springframework.org/schema/aop/spring-aop.xsd">

					    <!-- this is the object that will be proxied by Spring's AOP infrastructure -->
					    <bean id="personService" class="x.y.service.DefaultPersonService"/>

					    <!-- this is the actual advice itself -->
					    <bean id="profiler" class="x.y.SimpleProfiler"/>

					    <aop:config>
					        <aop:aspect ref="profiler">

					            <aop:pointcut id="theExecutionOfSomePersonServiceMethod"
					                expression="execution(* x.y.service.PersonService.getPerson(String,int))
					                and args(name, age)"/>

					            <aop:around pointcut-ref="theExecutionOfSomePersonServiceMethod"
					                method="profile"/>

					        </aop:aspect>
					    </aop:config>

					</beans>

				java:

					package x.y.service;

					public interface PersonService {

					    Person getPerson(String personName, int age);
					}

					public class DefaultFooService implements FooService {

					    public Person getPerson(String name, int age) {
					        return new Person(name, age);
					    }
					}

					-------------------------

					package x.y;

					import org.aspectj.lang.ProceedingJoinPoint;
					import org.springframework.util.StopWatch;

					public class SimpleProfiler {

					    public Object profile(ProceedingJoinPoint call, String name, int age) throws Throwable {
					        StopWatch clock = new StopWatch("Profiling for '" + name + "' and '" + age + "'");
					        try {
					            clock.start(call.toShortString());
					            return call.proceed();
					        } finally {
					            clock.stop();
					            System.out.println(clock.prettyPrint());
					        }
					    }
					}

					-------------------------

					import org.springframework.beans.factory.BeanFactory;
					import org.springframework.context.support.ClassPathXmlApplicationContext;
					import x.y.service.PersonService;

					public final class Boot {

					    public static void main(final String[] args) throws Exception {
					        BeanFactory ctx = new ClassPathXmlApplicationContext("x/y/plain.xml");
					        PersonService person = (PersonService) ctx.getBean("personService");
					        person.getPerson("Pengo", 12);
					    }
					}

			Advice Ordering
			
				实现Order接口或者添加@Order注解	

		5.5.4. Introductions

			说明，在Aspect中称为内部类型声明，通过让声明通知对象的切面实现指定接口，来提供相关对象的接口功能。

			xml:

				<aop:aspect id="usageTrackerAspect" ref="usageTracking">

				    <aop:declare-parents
				        types-matching="com.xzy.myapp.service.*+"
				        implement-interface="com.xyz.myapp.service.tracking.UsageTracked"
				        default-impl="com.xyz.myapp.service.tracking.DefaultUsageTracked"/>

				    <aop:before
				        pointcut="com.xyz.myapp.SystemArchitecture.businessService()
				            and this(usageTracked)"
				            method="recordUsage"/>

				</aop:aspect>

			java:
			
				public void recordUsage(UsageTracked usageTracked) {
				    usageTracked.incrementUseCount();
				}	
		
		5.5.5. Aspect Instantiation Models

			schema-defined的切面实力模型目前只支持单例模式，其他支持可能会在以后添加。

		5.5.6. Advisors
		
			xml:

				<aop:config>

				    <aop:pointcut id="businessService" expression="execution(* com.xyz.myapp.service.*.*(..))"/>

				    <aop:advisor pointcut-ref="businessService" advice-ref="tx-advice"/>
				</aop:config>

				<tx:advice id="tx-advice">
				    <tx:attributes>
				        <tx:method name="*" propagation="REQUIRED"/>
				    </tx:attributes>
				</tx:advice>	

		5.5.7. An AOP Schema Example
		
			java:

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


			xml:

				<aop:config>

				    <aop:aspect id="concurrentOperationRetry" ref="concurrentOperationExecutor">

				        <aop:pointcut id="idempotentOperation"
				            expression="execution(* com.xyz.myapp.service.*.*(..))"/>

				        <aop:around
				            pointcut-ref="idempotentOperation"
				            method="doConcurrentOperation"/>

				    </aop:aspect>

				</aop:config>

				<bean id="concurrentOperationExecutor"
				    class="com.xyz.myapp.service.impl.ConcurrentOperationExecutor">
				        <property name="maxRetries" value="3"/>
				        <property name="order" value="100"/>
				</bean>

	5.6. Choosing which AOP Declaration Style to Use
	
		5.6.1. Spring AOP or Full AspectJ?

			原则： 能简单实现就不搞复杂化。 Spring AOP要比整个AspectJ更简单（没有在开发或者构建应用过程中添加AspectJ编译和植入）。

				如果只需要对在Spring beans上的方法执行进行通知，选择Spring AOP，

				如果需要对非Spring容器管理的对象进行通知，或者通知的匹配方法规则要求很高很复杂，选择AspectJ。

				如果选择AspectJ，是选择编码格式还是注解格式？

					如果使用的java版本1.5或者更早，选择编码格式。

					如果应用设计时，切面相关的占比很高，而且可以使用Eclipse中的AJDT插件，选择编码格式。

					如果不使用Eclipse，aspect切面相关的占比很低，使用注解格式。

		5.6.2. @AspectJ or XML for Spring AOP?			

			如果选择使用Spring AOP，是使用注解格式还是XML格式？

				XML优点：

					使用Spring的用户对XML风格比较熟悉，而且它实际上也属于比较传统的POJO。

					企业服务中把AOP作为工具使用时，使用XML格式，可以让系统中的切面配置保持整洁。

				XML缺点：

					XML其实是将需求与实现（后端的bean类与切面配置）进行分隔了，违背了DRY原则（系统中的任何片段都要独立、明确、有代表性）。

					XML配置要比@AspectJ注解表达上限制更多，比如，切面模型只支持单例模式，xml中无法合并使用已添加的切入点pointcuts。

	5.7. Mixing Aspect Types

		// done 2020-3-5 19:59:12

	5.8. Proxying Mechanisms

		Spring AOP 使用JDK动态代理或者CGLIB来为指定的目标对象创建代理，JDK动态代理在JDK中，CGLIB是开源的类定义库（已被重新打包在spring-core中）。

		如果代理对象实现了接口，则使用JDK动态代理，所有被代理对象实现的接口都会被代理。如果目标对象没有实现任何接口，则使用CGLIB创建代理。

		CGLIB的缺点：

			final修饰的方法不能被增强，因为final方法不能被运行期生成的子类重写。

			Spring4.0以后，代理对象的构造器不再调用两次，因为CGLIB代理实例是通过Objenesis[实例化特殊class对象的小型类库]创建的。只有当JVM不允许越过构造器时，Spring AOP可能会调用两次

		强制使用CGLIB代理：

			<aop:config proxy-target-class="true">
			    <!-- other beans defined here... -->
			</aop:config>

		当使用@AspectJ自动代理时强制CGLIB代理：

			<aop:aspectj-autoproxy proxy-target-class="true"/>

		5.8.1. Understanding AOP Proxies

			Spring AOP是基于代理的。
		
			对象直接调用时：

				java:

					public class SimplePojo implements Pojo {

					    public void foo() {
					        // this next method invocation is a direct call on the 'this' reference
					        this.bar();
					    }

					    public void bar() {
					        // some logic...
					    }
					}	

					------------------------

					public class Main {

					    public static void main(String[] args) {
					        Pojo pojo = new SimplePojo();
					        // this is a direct method call on the 'pojo' reference
					        pojo.foo();
					    }
					}

				image:
				
					Calling code          pojo.foo()
					   |  /|\
					   |   |
					   |   |
					   |   |
					  \|/  |
					Plain Object ————————>foo() on the object


			使用代理调用时：			

				java:

					public class Main {

					    public static void main(String[] args) {
					        ProxyFactory factory = new ProxyFactory(new SimplePojo());
					        factory.addInterface(Pojo.class);
					        factory.addAdvice(new RetryAdvice());

					        Pojo pojo = (Pojo) factory.getProxy();
					        // this is a method call on the proxy!
					        pojo.foo();
					    }
					}



				image:

					Calling code          
					   |  /|\             __Proxy
					   |   | pojo.foo()  /
				|------|---|------------|
				|	   |———|————————————|————> foo() on the proxy
				|	  \|/  |            |
				|	Plain Object ———————|————> then foo() on the object
				|-----------------------|

			关键点，Main类中有一个Proxy代理引用，Pojo对象上的方法调用会发生在代理商。这样，代理就可以添加相关的方法拦截/增强通知。
			但是，一旦调用到达目标对象SimplePojo，再进行内部方法上的调用，比如，this.foo()或者this.bar(), 都会发生再目标对象this上，而不是代理Proxy。
			也就是，内部方法调用将不再会被代理方法拦截/增强通知。

			那么，我们应该怎么办呢？

				最好就是不进行内部调用，这样也可以实现代码侵入最小化。

				另一种方法有点可怕，是非常不推荐的，就是在代码中手动添加Spring AOP逻辑。

					i.e:
						java:

							public class SimplePojo implements Pojo {

							    public void foo() {
							        // this works, but... gah!
							        ((Pojo) AopContext.currentProxy()).bar();
							    }

							    public void bar() {
							        // some logic...
							    }
							}

							------------------------（需要的额外配置）

							public class Main {

							    public static void main(String[] args) {
							        ProxyFactory factory = new ProxyFactory(new SimplePojo());
							        factory.addInterface(Pojo.class);
							        factory.addAdvice(new RetryAdvice());
							        factory.setExposeProxy(true);

							        Pojo pojo = (Pojo) factory.getProxy();
							        // this is a method call on the proxy!
							        pojo.foo();
							    }
							}

				tips:			

					AspectJ不会有自身调用的问题，因为它不基于代理。（而Spring AOP是基于代理的）


		CGLIB代理原理： 			

			/**
			 * 基于AdvisedSupport配置对象，来创建AOP代理的工厂接口
			 *
			 * 代理应该遵循以下规则：
			 *    实现所有配置信息中需要被代理的接口
			 *    实现Advised接口
			 *    实现equals方法（为了比较被代理的接口，通知，目标对象）
			 *    如果所有的通知和目标对象可序列化，那么代理也应该可以序列化
			 *    如果所有的通知和目标对象线程安全，那么代理也应该线程安全 
			 *
			 * 代理可以允许，也可以不允许通知修改。 如果不允许（比如配置信息被冻结），当试图修改通知时，代理应该抛出异常。
			 */
			org.springframework.aop.framework.AopProxyFactory 

				public interface AopProxyFactory {
					AopProxy createAopProxy(AdvisedSupport config) throws AopConfigException;
				}

			/**
			 * AopProxyFactory接口的默认实现，创建一个CGLIB代理或者JDK动态代理。
			 * 
			 * 如果指定的AdvisedSupport满足以下任一条件，创建CGLIB代理：
			 *    设置了优化标识optimize
			 *    设置了代理目标类标识proxyTargetClass
			 *    没有指定代理接口
			 *
			 * 通常，通过设置proxyTargetClass来强制使用CGLIB代理，或者通过指定接口来使用JDK动态代理。
			 */
			org.springframework.aop.framework.DefaultAopProxyFactory

				public class DefaultAopProxyFactory implements AopProxyFactory, Serializable {

					@Override
					public AopProxy createAopProxy(AdvisedSupport config) throws AopConfigException {
						if (config.isOptimize() || config.isProxyTargetClass() || hasNoUserSuppliedProxyInterfaces(config)) {
							Class<?> targetClass = config.getTargetClass();
							if (targetClass == null) {
								throw new AopConfigException("TargetSource cannot determine target class: " +
										"Either an interface or a target is required for proxy creation.");
							}
							if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
								return new JdkDynamicAopProxy(config);
							}
							return new ObjenesisCglibAopProxy(config);
						}
						else {
							return new JdkDynamicAopProxy(config);
						}
					}

					/**
					 * Determine whether the supplied {@link AdvisedSupport} has only the
					 * {@link org.springframework.aop.SpringProxy} interface specified
					 * (or no proxy interfaces specified at all).
					 */
					private boolean hasNoUserSuppliedProxyInterfaces(AdvisedSupport config) {
						Class<?>[] ifcs = config.getProxiedInterfaces();
						return (ifcs.length == 0 || (ifcs.length == 1 && SpringProxy.class.isAssignableFrom(ifcs[0])));
					}

				}

			/**
			 * 已配置的AOP代理的委托接口，可以创建实际的代理对象
			 *	
			 * DefaultAopProxyFactory提供了JDK动态代理和CGLIB代理开箱即用的实现类
			 */ 	
			org.springframework.aop.framework.AopProxy	

				public interface AopProxy {

					// 使用默认的AopProxy类加载器（通常为当前线程的类加载器）来创建一个新的代理对象
					Object getProxy();

					// 使用指定的类加载器来创建一个新的代理对象，参数为null时，会下传参数导致低级的代理默认配置，与getPrexy()不同。
					Object getProxy(@Nullable ClassLoader classLoader);
				}

			/**
			 * Spring AOP框架中，AopProxy基于CGLIB的实现。
			 * 
			 * 需要根据AdvisedSupport对象的配置，通过代理工厂获取。 该类是Spring AOP框架中内置类，无需客户端直接编码调用。
			 *
			 * 如果需要（比如有代理目标类），DefaultAopProxyFactory会自动创建基于CGLIB的代理。
			 *
			 * 如果底层的目标类是线程安全的，使用此类创建的代理对象也是现成安全的。
			 */ 
			org.springframework.aop.framework.CglibAopProxy

				class CglibAopProxy implements AopProxy, Serializable {


					@Override
					public Object getProxy() {
						return getProxy(null);
					}

					@Override
					public Object getProxy(@Nullable ClassLoader classLoader) {
						if (logger.isTraceEnabled()) {
							logger.trace("Creating CGLIB proxy: " + this.advised.getTargetSource());
						}

						try {
							Class<?> rootClass = this.advised.getTargetClass();
							Assert.state(rootClass != null, "Target class must be available for creating a CGLIB proxy");

							Class<?> proxySuperClass = rootClass;
							if (rootClass.getName().contains(ClassUtils.CGLIB_CLASS_SEPARATOR)) {
								proxySuperClass = rootClass.getSuperclass();
								Class<?>[] additionalInterfaces = rootClass.getInterfaces();
								for (Class<?> additionalInterface : additionalInterfaces) {
									this.advised.addInterface(additionalInterface);
								}
							}

							// Validate the class, writing log messages as necessary.
							validateClassIfNecessary(proxySuperClass, classLoader);

							// Configure CGLIB Enhancer...
							Enhancer enhancer = createEnhancer();
							if (classLoader != null) {
								enhancer.setClassLoader(classLoader);
								if (classLoader instanceof SmartClassLoader &&
										((SmartClassLoader) classLoader).isClassReloadable(proxySuperClass)) {
									enhancer.setUseCache(false);
								}
							}
							enhancer.setSuperclass(proxySuperClass);
							enhancer.setInterfaces(AopProxyUtils.completeProxiedInterfaces(this.advised));
							enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
							enhancer.setStrategy(new ClassLoaderAwareGeneratorStrategy(classLoader));

							Callback[] callbacks = getCallbacks(rootClass);
							Class<?>[] types = new Class<?>[callbacks.length];
							for (int x = 0; x < types.length; x++) {
								types[x] = callbacks[x].getClass();
							}
							// fixedInterceptorMap only populated at this point, after getCallbacks call above
							enhancer.setCallbackFilter(new ProxyCallbackFilter(
									this.advised.getConfigurationOnlyCopy(), this.fixedInterceptorMap, this.fixedInterceptorOffset));
							enhancer.setCallbackTypes(types);

							// Generate the proxy class and create a proxy instance.
							return createProxyClassAndInstance(enhancer, callbacks);
						}
						catch (CodeGenerationException | IllegalArgumentException ex) {
							throw new AopConfigException("Could not generate CGLIB subclass of " + this.advised.getTargetClass() +
									": Common causes of this problem include using a final class or a non-visible class",
									ex);
						}
						catch (Throwable ex) {
							// TargetSource.getTarget() failed
							throw new AopConfigException("Unexpected AOP exception", ex);
						}
					}

				}


			/**
			 * 基于Objenesis的CglibAopProxy的子类，无需通过调用类构造器来创建代理实例。 Spring4之后默认使用。
			 */ 	
			org.springframework.aop.framework.ObjenesisCglibAopProxy	
				class ObjenesisCglibAopProxy extends CglibAopProxy {	

					private static final SpringObjenesis objenesis = new SpringObjenesis();

					public ObjenesisCglibAopProxy(AdvisedSupport config) {
						super(config);
					}


					@Override
					protected Object createProxyClassAndInstance(Enhancer enhancer, Callback[] callbacks) {
						Class<?> proxyClass = enhancer.createClass();
						Object proxyInstance = null;

						if (objenesis.isWorthTrying()) {
							try {
								proxyInstance = objenesis.newInstance(proxyClass, enhancer.getUseCache());
							}
							catch (Throwable ex) {
								logger.debug("Unable to instantiate proxy using Objenesis, " +
										"falling back to regular proxy construction", ex);
							}
						}

						if (proxyInstance == null) {
							// Regular instantiation via default constructor...
							try {
								Constructor<?> ctor = (this.constructorArgs != null ?
										proxyClass.getDeclaredConstructor(this.constructorArgTypes) :
										proxyClass.getDeclaredConstructor());
								ReflectionUtils.makeAccessible(ctor);
								proxyInstance = (this.constructorArgs != null ?
										ctor.newInstance(this.constructorArgs) : ctor.newInstance());
							}
							catch (Throwable ex) {
								throw new AopConfigException("Unable to instantiate proxy using Objenesis, " +
										"and regular proxy instantiation via default constructor fails as well", ex);
							}
						}

						((Factory) proxyInstance).setCallbacks(callbacks);
						return proxyInstance;
					}

				}

			}		

			/**
			 *
			 *
			 *
			 *
			 *
			 *
			 *
			 *
			 */
			org.springframework.cglib.proxy.Enhancer

				public class Enhancer extends AbstractClassGenerator {



				}

				
















		

