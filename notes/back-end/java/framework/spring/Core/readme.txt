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

			debug/run test ClassPathXmlApplicationContextTest.testSingleConfigLocation

				public class ClassPathXmlApplicationContextTests {

					private static final String PATH = "/org/springframework/context/support/";
					private static final String SIMPLE_CONTEXT = "simpleContext.xml";
					private static final String FQ_SIMPLE_CONTEXT = PATH + "simpleContext.xml";

					@Test
					public void testSingleConfigLocation() {
						ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(FQ_SIMPLE_CONTEXT);
						assertThat(ctx.containsBean("someMessageSource")).isTrue();
						ctx.close();
					}

				}

				-- 加载类ContextClosedEvent，防止在WebLogic 8.1应用关闭时发生的奇怪的类加载问题。其中，ContextClosedEvent：ApplicationContext关闭时触发的事件。

						static {
							// Eagerly load the ContextClosedEvent class to avoid weird classloader issues
							// on application shutdown in WebLogic 8.1. (Reported by Dustin Woods.)
							ContextClosedEvent.class.getName();
						}

				-- 接下来会调用构造器：
				
						public ClassPathXmlApplicationContext(
								String[] configLocations, boolean refresh, @Nullable ApplicationContext parent)
								throws BeansException {

							super(parent);
							setConfigLocations(configLocations);
							if (refresh) {
								refresh();
							}
						}		






