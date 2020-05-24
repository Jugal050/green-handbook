Web on Servlet Stack

1. Spring Web MVC

	1.1. DispatcherServlet 											// done 2020-5-19 23:35:48

		DispatcherServlet配置注册及初始化：	

		``java

			public class MyWebApplicationInitializer implements WebApplicationInitializer {

			    @Override
			    public void onStartup(ServletContext servletCxt) {

			        // Load Spring web application configuration
			        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
			        ac.register(AppConfig.class);
			        ac.refresh();

			        // Create and register the DispatcherServlet
			        DispatcherServlet servlet = new DispatcherServlet(ac);
			        ServletRegistration.Dynamic registration = servletCxt.addServlet("app", servlet);
			        registration.setLoadOnStartup(1);
			        registration.addMapping("/app/*");
			    }
			}

		``xml: web.xml

			<web-app>

			    <listener>
			        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
			    </listener>

			    <context-param>
			        <param-name>contextConfigLocation</param-name>
			        <param-value>/WEB-INF/app-context.xml</param-value>
			    </context-param>

			    <servlet>
			        <servlet-name>app</servlet-name>
			        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
			        <init-param>
			            <param-name>contextConfigLocation</param-name>
			            <param-value></param-value>
			        </init-param>
			        <load-on-startup>1</load-on-startup>
			    </servlet>

			    <servlet-mapping>
			        <servlet-name>app</servlet-name>
			        <url-pattern>/app/*</url-pattern>
			    </servlet-mapping>

			</web-app>

		1.1.1. Context Hierarchy
		
			``java

				public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

				    @Override
				    protected Class<?>[] getRootConfigClasses() {
				        return new Class<?>[] { RootConfig.class };
				    }

				    @Override
				    protected Class<?>[] getServletConfigClasses() {
				        return new Class<?>[] { App1Config.class };
				    }

				    @Override
				    protected String[] getServletMappings() {
				        return new String[] { "/app1/*" };
				    }
				}		

			``xml: web.xml（与上边的定义等价）
			
				<web-app>

				    <listener>
				        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
				    </listener>

				    <context-param>
				        <param-name>contextConfigLocation</param-name>
				        <param-value>/WEB-INF/root-context.xml</param-value>
				    </context-param>

				    <servlet>
				        <servlet-name>app1</servlet-name>
				        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
				        <init-param>
				            <param-name>contextConfigLocation</param-name>
				            <param-value>/WEB-INF/app1-context.xml</param-value>
				        </init-param>
				        <load-on-startup>1</load-on-startup>
				    </servlet>

				    <servlet-mapping>
				        <servlet-name>app1</servlet-name>
				        <url-pattern>/app1/*</url-pattern>
				    </servlet-mapping>

				</web-app>

		1.1.2. Special Bean Types 										// done 2020-5-19 22:52:14	

	1.2. Filters 														// done 2020-5-20 23:08:33		

		1.2.1. Form Data

			FormContentFilter

		1.2.2. Forwarded Headers
		
			ForwardedHeaderFilter

		1.2.3. Shallow ETag
		
			ShallowEtagHeaderFilter 

		1.2.4. CORS														// done 2020-5-21 22:02:22

			CorsFilter 			

	1.3. Annotated Controllers 											// done 2020-5-23 15:52:57 暂未使用，略读

	1.4. Functional Endpoints 											// done 2020-5-24 10:34:54 暂未使用，略读

	1.5. URI Links	 													// done 2020-5-24 10:34:54 暂未使用，略读

	1.6. Asynchronous Requests 											// done 2020-5-24 10:34:54 暂未使用，略读	

