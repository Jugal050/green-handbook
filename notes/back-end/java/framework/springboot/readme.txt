https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/html/ 		// start 2020-7-13 22:15:23

The reference documentation consists of the following sections:

	Legal								Legal information.

	Documentation Overview				About the Documentation, Getting Help, First Steps, and more. 		// done 2020-7-13 22:29:48

		1. About the Documentation
		2. Getting Help
		3. First Steps
		4. Working with Spring Boot
		5. Learning about Spring Boot Features
		6. Moving to Production
		7. Advanced Topics

	Getting Started	 					Introducing Spring Boot, System Requirements, Servlet Containers, Installing Spring Boot, Developing Your First Spring Boot Application 																				// done 2020-7-13 23:03:42

		1. Introducing Spring Boot
		2. System Requirements
			2.1. Servlet Containers
		3. Installing Spring Boot
			3.1. Installation Instructions for the Java Developer
			3.2. Installing the Spring Boot CLI
			3.3. Upgrading from an Earlier Version of Spring Boot
		4. Developing Your First Spring Boot Application
			4.1. Creating the POM
			4.2. Adding Classpath Dependencies
			4.3. Writing the Code
			4.4. Running the Example
			4.5. Creating an Executable Jar
		5. What to Read Ne

	Using Spring Boot	 				Build Systems, Structuring Your Code, Configuration, Spring Beans and Dependency Injection, and more.  
																	// done 2020-7-14 00:31:27

		1. Build Systems 											// done 2020-7-13 23:56:09
			1.1. Dependency Management
			1.2. Maven
			1.3. Gradle
			1.4. Ant
			1.5. Starters
		2. Structuring Your Code
			2.1. Using the “default” Package
			2.2. Locating the Main Application Class
		3. Configuration Classes
			3.2. Importing XML Configuration
			3.1. Importing Additional Configuration Classes
		4. Auto-configuration
			4.1. Gradually Replacing Auto-configuration
			4.2. Disabling Specific Auto-configuration Classes
		5. Spring Beans and Dependency Injection
		6. Using the @SpringBootApplication Annotation
		7. Running Your Application
			7.1. Running from an IDE
			7.2. Running as a Packaged Application
			7.3. Using the Maven Plugin
			7.4. Using the Gradle Plugin
			7.5. Hot Swapping
		8. Developer Tools
			8.1. Property Defaults
			8.2. Automatic Restart
			8.3. LiveReload
			8.4. Global Settings
			8.5. Remote Applications
				8.5.1. Running the Remote Client Application
				8.5.2. Remote Update
		9. Packaging Your Application for Production
		10. What to Read Next 										// done 2020-7-14 00:31:27

	Spring Boot Features	 			Profiles, Logging, Security, Caching, Spring Integration, Testing, and more. 			// done 2020-7-19 19:23:57
		1. SpringApplication 										// done 2020-7-14 08:40:00
			1.1. Startup Failure
			1.2. Lazy Initialization
			1.3. Customizing the Banner
			1.4. Customizing SpringApplication
			1.5. Fluent Builder API
			1.6. Application Availability
			1.7. Application Events and Listeners
			1.8. Web Environment
			1.9. Accessing Application Arguments
			1.10. Using the ApplicationRunner or CommandLineRunner
			1.11. Application Exit
			1.12. Admin Features
		2. Externalizd Configuration 								// done 2020-7-14 13:48:00
			2.1. Configuring Random Values
			2.2. Accessing Command Line Properties
			2.3. Application Property Files
			2.4. Profile-specific Properties
			2.5. Placeholders in Properties
			2.6. Encrypting Properties
			2.7. Using YAML Instead of Properties
			2.8. Type-safe Configuration Properties
		3. Profiles 												
			3.1. Adding Active Profiles
			3.2. Programmatically Setting Profiles
			3.3. Profile-specific Configuration Files
		4. Logging
			4.1. Log Format
			4.2. Console Output
			4.3. File Output
			4.4. Log Levels
			4.5. Log Groups
			4.6. Custom Log Configuration
			4.7. Logback Extensions
		5. Internationalization
		6. JSON
			6.1. Jackson
			6.2. Gson
			6.3. JSON-B
		7. Developing Web Applications 								// done 2020-7-15 23:24:41
			7.1. The “Spring Web MVC Framework” 					
			7.2. The “Spring WebFlux Framework” 					
			7.3. JAX-RS and Jersey
			7.4. Embedded Servlet Container Support
			7.5. Embedded Reactive Server Support
			7.6. Reactive Server Resources Configuration
		8. Graceful shutdown
		9. RSocket
			9.1. RSocket Strategies Auto-configuration
			9.2. RSocket server Auto-configuration
			9.3. Spring Messaging RSocket support
			9.4. Calling RSocket Services with RSocketRequester
		10. Security 												// done 2020-7-15 23:46:13
			10.1. MVC Security
			10.2. WebFlux Security
			10.3. OAuth2
			10.4. SAML 2.0
			10.5. Actuator Security
		11. Working with SQL Databases 								// doen 2020-7-18 16:05:25
 
			11.1. Configure a DataSource

				Embedded Database Support: 

					H2: https://www.h2database.com
					HSQL: http://hsqldb.org/
					Derby: https://db.apache.org/derby/

				Connection to a Production Database: 

					HikariCP: https://github.com/brettwooldridge/HikariCP
					Tomcat pooling DataSource
					Commons DBCP2: https://commons.apache.org/proper/commons-dbcp/

			11.2. Using JdbcTemplate

			11.3. JPA and Spring Data JPA

			11.4. Spring Data JDBC

			11.5. Using H2’s Web Console

			11.6. Using jOOQ

				jOOQ(Java Object Oriented Query): https://www.jooq.org/ 		// doen 2020-7-18 15:51:24

				desc: jOOQ generates Java code from your database and lets you build type safe SQL queries through its fluent API.

			11.7. Using R2DBC

		 		 R2DBC(Reactive Relational Database Connectivity): https://r2dbc.io/

		 		 desc: The Reactive Relational Database Connectivity (R2DBC) project brings reactive programming APIs to relational databases.

		12. Working with NoSQL Technologies 						// done 2020-7-18 17:00:58

	 		12.1. Redis 

	 			url: https://redis.io/

	 			desc: Redis is a cache, message broker, and richly-featured key-value store. 

	 			client: Spring Boot offers basic auto-configuration for the Lettuce and Jedis client libraries and the abstractions on top of them provided by Spring Data Redis.
			
			12.2. MongoDB

				url: https://www.mongodb.com/

				desc: MongoDB is an open-source NoSQL document database that uses a JSON-like schema instead of traditional table-based relational data. 
			
			12.3. Neo4j

				url: https://neo4j.com

				desc: Neo4j is an open-source NoSQL graph database that uses a rich data model of nodes connected by first class relationships, which is better suited for connected big data than traditional RDBMS approaches. 
			
			12.4. Solr

				url: https://lucene.apache.org/solr/

				desc: Apache Solr is a search engine.
			
			12.5. Elasticsearch

				url: https://www.elastic.co/products/elasticsearch

				desc: Elasticsearch is an open source, distributed, RESTful search and analytics engine. 

				client: 

					The official Java "Low Level" and "High Level" REST clients

					The ReactiveElasticsearchClient provided by Spring Data Elasticsearch
			
			12.6. Cassandra

				url: https://cassandra.apache.org/

				desc: Cassandra is an open source, distributed database management system designed to handle large amounts of data across many commodity servers. 
			
			12.7. Couchbase

				url: https://www.couchbase.com/

				desc: Couchbase is an open-source, distributed, multi-model NoSQL document-oriented database that is optimized for interactive applications. 
			
			12.8. LDAP

				url: https://en.wikipedia.org/wiki/Lightweight_Directory_Access_Protocol

				desc: LDAP (Lightweight Directory Access Protocol) is an open, vendor-neutral, industry standard application protocol for accessing and maintaining distributed directory information services over an IP network. 
			
			12.9. InfluxDB

				url: https://www.influxdata.com/

				desc: InfluxDB is an open-source time series database optimized for fast, high-availability storage and retrieval of time series data in fields such as operations monitoring, application metrics, Internet-of-Things sensor data, and real-time analytics.

		13. Caching 												// done 2020-7-18 18:15:04

			13.1. Supported Cache Providers

				13.1.1. Generic
				
				13.1.2. JCache (JSR-107)

					url: https://jcp.org/en/jsr/detail?id=107
				
				13.1.3. EhCache 2.x

					url: https://www.ehcache.org/
				
				13.1.4. Hazelcast
				
				13.1.5. Infinispan

					url: https://github.com/infinispan/infinispan-spring-boot
				
				13.1.6. Couchbase

					url: https://www.couchbase.com/
				
				13.1.7. Redis
				
				13.1.8. Caffeine

					desc: Caffeine is a Java 8 rewrite of Guava’s cache that supersedes support for Guava.
				
				13.1.9. Simple
				
				13.1.10. None

		14. Messaging 									// done 2020-7-19 13:59:36

			14.1. JMS

				ActiveMQ: https://activemq.apache.org/

				Artemis: https://activemq.apache.org/components/artemis/
			
			14.2. AMQP

				desc: The Advanced Message Queuing Protocol (AMQP) is a platform-neutral, wire-level protocol for message-oriented middleware.

				RabbitMQ: https://www.rabbitmq.com/

					desc: RabbitMQ is a lightweight, reliable, scalable, and portable message broker based on the AMQP protocol. 

				Apache Kafka: https://kafka.apache.org/	
			
			14.3. Apache Kafka Support

		15. Calling REST Services with RestTemplate		// done 2020-7-19 14:19:27

			15.1. RestTemplate Customization

		16. Calling REST Services with WebClient		// doen 2020-7-19 14:23:13

			16.1. WebClient Runtime

			16.2. WebClient Customization

		17. Validation 									// done 2020-7-19 14:26:33

		18. Sending Email 								// done 2020-7-19 14:26:29

		19. Distributed Transactions with JTA 			// done 2020-7-19 14:44:26

			19.1. Using an Atomikos Transaction Manager

				Atomikos: 

					url: https://www.atomikos.com/

					desc: Reliability through Atomicity Manage your distributed transactions and protect your mission critical data
			
			19.2. Using a Bitronix Transaction Manager

				Bitronix: 

					tips: Deprecated support for using a Bitronix embedded transaction manager is also provided but it will be removed in a future release.
			
					url: https://github.com/bitronix/btm	

					desc: The Bitronix Transaction Manager (BTM) is a simple but complete implementation of the JTA 1.1 API. It is a fully working XA transaction manager that provides all services required by the JTA API while trying to keep the code as simple as possible for easier understanding of the XA semantics.
			
			19.3. Using a Java EE Managed Transaction Manager
			
			19.4. Mixing XA and Non-XA JMS Connections
			
			19.5. Supporting an Alternative Embedded Transaction Manager

		20. Hazelcast 									// done 2020-7-19 15:01:46

			url: https://hazelcast.com/

			desc: Application performance at scale. Simplified. lets you build the fastest applications. Access a scalable, shared pool of RAM across a cluster of computers

			doc: https://docs.hazelcast.org/docs/latest/manual/html-single/

		21. Quartz Scheduler 							// doen 2020-7-19 15:25:31

		22. Task Execution and Scheduling 				// done 2020-7-19 15:31:53

		23. Spring Integration 							// done 2020-7-19 15:37:29
		
		24. Spring Session 								// done 2020-7-19 15:37:29
		
		25. Monitoring and Management over JMX 			// done 2020-7-19 15:38:17

		26. Testing 									// done 2020-7-19 16:51:18

			26.1. Test Scope Dependencies

				- JUnit 5 (including the vintage engine for backward compatibility with JUnit 4): The de-facto standard for unit testing Java applications.

					url: https://junit.org/junit5/

				- Spring Test & Spring Boot Test: Utilities and integration test support for Spring Boot applications.

					url: https://docs.spring.io/spring/docs/5.2.7.RELEASE/spring-framework-reference/testing.html#integration-testing

				- AssertJ: A fluent assertion library.

					url: https://assertj.github.io/doc/

				- Hamcrest: A library of matcher objects (also known as constraints or predicates).

					url: https://github.com/hamcrest/JavaHamcrest

				- Mockito: A Java mocking framework.

					url: https://site.mockito.org/

				- JSONassert: An assertion library for JSON.

					url: https://github.com/skyscreamer/JSONassert

				- JsonPath: XPath for JSON.

					url: https://github.com/jayway/JsonPath

			26.2. Testing Spring Applications

				Spock: http://spockframework.org/spock/docs/1.2/modules.html#_spring_module
			
			26.3. Testing Spring Boot Applications
			
			26.4. Test Utilities	
		
		27. WebSockets 									// done 2020-7-19 16:51:56
		
		28. Web Services 								// done 2020-7-19 16:55:22
		
		29. Creating Your Own Auto-configuration 		// done 2020-7-19 17:13:15

			29.1. Understanding Auto-configured Beans
			
			29.2. Locating Auto-configuration Candidates
			
			29.3. Condition Annotations

				ASM: https://asm.ow2.io/

				desc: ASM is an all purpose Java bytecode manipulation and analysis framework. It can be used to modify existing classes or to dynamically generate classes, directly in binary form. ASM provides some common bytecode transformations and analysis algorithms from which custom complex transformations and code analysis tools can be built. ASM offers similar functionality as other Java bytecode frameworks, but is focused on performance. Because it was designed and implemented to be as small and as fast as possible, it is well suited for use in dynamic systems (but can of course be used in a static way too, e.g. in compilers).

				usage: ASM is used in many projects, including:

					- the OpenJDK, to generate the lambda call sites, and also in the Nashorn compiler,
					- the Groovy compiler and the Kotlin compiler,
					- Cobertura and Jacoco, to instrument classes in order to measure code coverage,
					- CGLIB, to dynamically generate proxy classes (which are used in other projects such as Mockito and EasyMock),
					- Gradle, to generate some classes at runtime.
			
			29.4. Testing your Auto-configuration
			
			29.5. Creating Your Own Starter
		
		30. Kotlin support 								// done 2020-7-19 18:03:19

			url: https://kotlinlang.org/

			desc: Kotlin is a statically-typed language targeting the JVM (and other platforms) which allows writing concise and elegant code while providing interoperability with existing libraries written in Java.

			30.1. Requirements
			
			30.2. Null-safety
			
			30.3. Kotlin API
			
			30.4. Dependency management
			
			30.5. @ConfigurationProperties
			
			30.6. Testing

			30.7. Resources
		
		31. Building Docker Images 						// done 2020-7-19 19:23:11

			31.1. Layering Docker Images

			31.2. Writing the Dockerfile

			31.3. Buildpacks
		
		32. What to Read Next 							// done 2020-7-19 19:23:17

	Spring Boot Actuator	 			Monitoring, Metrics, Auditing, and more. 	// start 2020-7-20 21:47:14 done 2020-7-20 23:58:58

		1. Enabling Production-ready Features
		
		2. Endpoints 									// done 2020-7-20 23:09:02

			Jolokia: https://jolokia.org/

				desc: Jolokia is remote JMX with JSON over HTTP. It is fast, simple, polyglot and has unique features. It's JMX on Capsaicin.

			prometheus: https://prometheus.io/	

				desc: From metrics to insight. Power your metrics and alerting with a leading open-source monitoring solution.

			actuator-api: https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/actuator-api/html/	
		
			2.1. Enabling Endpoints
		
			2.2. Exposing Endpoints
		
			2.3. Securing HTTP Endpoints
		
			2.4. Configuring Endpoints
		
			2.5. Hypermedia for Actuator Web Endpoints
		
			2.6. CORS Support
		
			2.7. Implementing Custom Endpoints
		
			2.8. Health Information
		
			2.9. Kubernetes Probes

				url: https://kubernetes.io/docs/concepts/workloads/pods/pod-lifecycle/#container-probes
		
			2.10. Application Information
		
		3. Monitoring and Management over HTTP 			// done 2020-7-20 23:15:55

			3.1. Customizing the Management Endpoint Paths
			
			3.2. Customizing the Management Server Port
			
			3.3. Configuring Management-specific SSL
			
			3.4. Customizing the Management Server Address
			
			3.5. Disabling HTTP Endpoints
		
		4. Monitoring and Management over JMX 			// done 2020-7-20 23:19:06
			
			4.1. Customizing MBean Names
			
			4.2. Disabling JMX Endpoints
			
			4.3. Using Jolokia for JMX over HTTP
		
		5. Loggers 										// done 2020-7-20 23:21:22

			5.1. Configure a Logger
		
		6. Metrics 										// done 2020-7-20 23:47:40

			micrometer: 

				url: https://micrometer.io/

				desc: Vendor-neutral application metrics facade
					  Micrometer provides a simple facade over the instrumentation clients for the most popular monitoring systems, allowing you to instrument your JVM-based application code without vendor lock-in. Think SLF4J, but for metrics.

				doc: https://micrometer.io/docs

				concepts: https://micrometer.io/docs/concepts	  


			Signalfx: 

				url: https://www.signalfx.com/
			
				desc: Real-Time Cloud Monitoring and Observability for Infrastructure, Microservices and Applications	

			6.1. Getting started
			
			6.2. Supported monitoring systems
			
			6.3. Supported Metrics
			
			6.4. Registering custom metrics
			
			6.5. Customizing individual metrics
			
			6.6. Metrics endpoint	
		
		7. Auditing 									// done 2020-7-20 23:49:28

			7.1. Custom Auditing
		
		8. HTTP Tracing 								// done 2020-7-20 23:50:54

			8.1. Custom HTTP tracing
		
		9. Process Monitoring 							// done 2020-7-20 23:52:06

			9.1. Extending Configuration
			
			9.2. Programmatically
		
		10. Cloud Foundry Support 						// done 2020-7-20 23:55:07

			10.1. Disabling Extended Cloud Foundry Actuator Support
			
			10.2. Cloud Foundry Self-signed Certificates
			
			10.3. Custom context path
		
		11. What to Read Next 							// done 2020-7-20 23:59:32

			Graphite: https://graphiteapp.org/

				desc: Graphite is an enterprise-ready monitoring tool that runs equally well on cheap hardware or Cloud infrastructure. Teams use Graphite to track the performance of their websites, applications, business services, and networked servers. It marked the start of a new generation of monitoring tools, making it easier than ever to store, retrieve, share, and visualize time-series data.

	Deploying Spring Boot Applications	Deploying to the Cloud, Installing as a Unix application. 		// done 2020-7-21 22:39:50

		1. Deploying to Containers 						// done 2020-7-21 22:10:49
		
		2. Deploying to the Cloud 						// done 2020-7-21 22:23:36
		
			2.1. Cloud Foundry

				url: https://www.cloudfoundry.org/

				desc: The Cloud Foundry Developer Experience On Kubernetes
					  Get the simplicity, control and speed of Cloud Foundry on the industry standard container platform: Kubernetes.
			
			2.2. Kubernetes
			
			2.3. Heroku

				url: https://www.heroku.com/

				desc: Developers, teams, and businesses of all sizes use Heroku to deploy, manage, and scale apps.
			
			2.4. OpenShift

				url: https://www.openshift.com/

				desc: The Kubernetes platform for big ideas
				 	  Empower developers to innovate and ship faster with the leading hybrid cloud, enterprise container platform
			
			2.5. Amazon Web Services (AWS)
			
			2.6. Boxfuse and Amazon Web Services

				Boxfuse: https://boxfuse.com/

					desc: Deploy your JVM, Node.js and Go apps to AWS. Effortlessly.
						  Build minimal fully-provisioned images in seconds. 
						  Test on VirtualBox, deploy atomically to AWS. 
						  No legacy OS. No containers. No drift.
			
			2.7. Google Cloud

				Google Cloud SDK: https://cloud.google.com/sdk/install
		
		3. Installing Spring Boot Applications 				// done 2020-7-21 22:40:16

			3.1. Supported Operating Systems
			
			3.2. Unix/Linux Services
			
			3.3. Microsoft Windows Services

				winsw: https://github.com/winsw/winsw

					desc: WinSW is an executable binary, which can be used to wrap and manage a custom process as a Windows service. Once you download the installation package, you can rename WinSW.exe to any name, e.g. MyService.exe.

		4. What to Read Next

	Spring Boot CLI	 					Installing the CLI, Using the CLI, Configuring the CLI, and more. 	// done 2020-7-22 21:09:18

		1. Installing the CLI
		
		2. Using the CLI

			2.1. Running Applications with the CLI
			
			2.2. Applications with Multiple Source Files
			
			2.3. Packaging Your Application
			
			2.4. Initialize a New Project
			
			2.5. Using the Embedded Shell
			
			2.6. Adding Extensions to the CLI
		
		3. Developing Applications with the Groovy Beans DSL

			Grails: https://grails.org/

				desc: A powerful Groovy-based web application framework for the JVM built on top of Spring Boot
		
		4. Configuring the CLI with settings.xml
		
		5. What to Read Next

			source code: https://github.com/spring-projects/spring-boot/tree/v2.3.1.RELEASE/spring-boot-project/spring-boot-cli/src/main/java/org/springframework/boot/cli

	Build Tool Plugins	 				Maven Plugin, Gradle Plugin, Antlib, and more. 			// done 2020-7-22 21:17:36

		1. Spring Boot Maven Plugin
		
		2. Spring Boot Gradle Plugin
		
		3. Spring Boot AntLib Module

			3.1. Spring Boot Ant Tasks
			
			3.2. Using the “findmainclass” Task
		
		4. Supporting Other Build Systems

			4.1. Repackaging Archives

			4.2. Nested Libraries

			4.3. Finding a Main Class

			4.4. Example Repackage Implementation

		5. What to Read Next

	“How-to” Guides	 					Application Development, Configuration, Embedded Servers, Data Access, and many more. 	// doen 2020-7-26 16:52:47

		1. Spring Boot Application 					// done 2020-7-25 15:04:22
		
			1.1. Create Your Own FailureAnalyzer
			1.2. Troubleshoot Auto-configuration
			1.3. Customize the Environment or ApplicationContext Before It Starts
			1.4. Build an ApplicationContext Hierarchy (Adding a Parent or Root Context)
			1.5. Create a Non-web Application
		
		2. Properties and Configuration 			// done 2020-7-25 16:48:22

			2.1. Automatically Expand Properties at Build Time
			2.2. Externalize the Configuration of SpringApplication
			2.3. Change the Location of External Properties of an Application
			2.4. Use ‘Short’ Command Line Arguments
			2.5. Use YAML for External Properties
			2.6. Set the Active Spring Profiles
			2.7. Change Configuration Depending on the Environment
			2.8. Discover Built-in Options for External Properties
		
		3. Embedded Web Servers 					// done 2020-7-25 17:35:49

			3.1. Use Another Web Server
			3.2. Disabling the Web Server
			3.3. Change the HTTP Port
			3.4. Use a Random Unassigned HTTP Port
			3.5. Discover the HTTP Port at Runtime
			3.6. Enable HTTP Response Compression
			3.7. Configure SSL
			3.8. Configure HTTP/2
			3.9. Configure the Web Server
			3.10. Add a Servlet, Filter, or Listener to an Application
			3.11. Configure Access Logging
			3.12. Running Behind a Front-end Proxy Server
			3.13. Enable Multiple Connectors with Tomcat
			3.14. Use Tomcat’s LegacyCookieProcessor
			3.15. Enable Tomcat’s MBean Registry
			3.16. Enable Multiple Listeners with Undertow
			3.17. Create WebSocket Endpoints Using @ServerEndpoint
		
		4. Spring MVC 								// done 2020-7-25 18:02:05

			4.1. Write a JSON REST Service
			4.2. Write an XML REST Service
			4.3. Customize the Jackson ObjectMapper
			4.4. Customize the @ResponseBody Rendering
			4.5. Handling Multipart File Uploads
			4.6. Switch Off the Spring MVC DispatcherServlet
			4.7. Switch off the Default MVC Configuration
			4.8. Customize ViewResolvers
		
		5. Testing With Spring Security 			// done 2020-7-25 18:03:01
		
		6. Jersey 									// done 2020-7-25 18:08:54

			url: https://eclipse-ee4j.github.io/jersey/

			desc: Eclipse Jersey is a REST framework that provides a JAX-RS (JSR-370) implementation and more

			6.1. Secure Jersey endpoints with Spring Security
			6.2. Use Jersey Alongside Another Web Framework
		
		7. HTTP Clients 							// done 2020-7-25 18:12:03

			7.1. Configure RestTemplate to Use a Proxy
			7.2. Configure the TcpClient used by a Reactor Netty-based WebClient
		
		8. Logging 									// done 2020-7-25 18:21:54

			8.1. Configure Logback for Logging
			8.2. Configure Log4j for Logging
		
		9. Data Access 								// done 2020-7-26 15:34:52

			9.1. Configure a Custom DataSource
			9.2. Configure Two DataSources
			9.3. Use Spring Data Repositories
			9.4. Separate @Entity Definitions from Spring Configuration
			9.5. Configure JPA Properties
			9.6. Configure Hibernate Naming Strategy
			9.7. Configure Hibernate Second-Level Caching
			9.8. Use Dependency Injection in Hibernate Components
			9.9. Use a Custom EntityManagerFactory
			9.10. Use Two EntityManagers
			9.11. Use a Traditional persistence.xml File
			9.12. Use Spring Data JPA and Mongo Repositories
			9.13. Customize Spring Data’s Web Support
			9.14. Expose Spring Data Repositories as REST Endpoint
			9.15. Configure a Component that is Used by JPA
			9.16. Configure jOOQ with Two DataSources
		
		10. Database Initialization 				// done 2020-7-26 15:56:11

			10.1. Initialize a Database Using JPA
			10.2. Initialize a Database Using Hibernate
			10.3. Initialize a Database using basic SQL scripts
			10.4. Initialize a Database Using R2DBC
			10.5. Initialize a Spring Batch Database
			10.6. Use a Higher-level Database Migration Tool

			tips：
				- Flyway: https://flywaydb.org/
					desc: Version control for your database. Robust schema evolution across all your environments. With ease, pleasure and plain SQL.
				- Liquibase: https://www.liquibase.org/
					desc: Track, version, and deploy database changes
		
		11. Messaging 								// done 2020-7-26 15:59:20

			11.1. Disable Transacted JMS Session
		
		12. Batch Applications 						// done 2020-7-26 16:03:09

			12.1. Specifying a Batch Data Source
			12.2. Running Spring Batch Jobs on Startup
			12.3. Running from the Command Line
			12.4. Storing the Job Repository

			tips:
				- spring batch projcet: https://spring.io/projects/spring-batch
		
		13. Actuator 								// done 2020-7-26 16:20:11

			13.1. Change the HTTP Port or Address of the Actuator Endpoints
			13.2. Customize the ‘whitelabel’ Error Page
			13.3. Sanitize Sensitive Values
			13.4. Map Health Indicators to Micrometer Metrics
		
		14. Security 								// done 2020-7-26 16:24:11

			14.1. Switch off the Spring Boot Security Configuration
			14.2. Change the UserDetailsService and Add User Accounts
			14.3. Enable HTTPS When Running behind a Proxy Server

			tips:
				 - Spring Security project page: https://spring.io/projects/spring-security
		
		15. Hot Swapping 							// done 2020-7-26 16:28:04

			15.1. Reload Static Content
			15.2. Reload Templates without Restarting the Container
			15.3. Fast Application Restarts
			15.4. Reload Java Classes without Restarting the Container
		
		16. Build 									// done 2020-7-26 16:41:27

			16.1. Generate Build Information
			16.2. Generate Git Information
			16.3. Customize Dependency Versions
			16.4. Create an Executable JAR with Maven
			16.5. Use a Spring Boot Application as a Dependency
			16.6. Extract Specific Libraries When an Executable Jar Runs
			16.7. Create a Non-executable JAR with Exclusions
			16.8. Remote Debug a Spring Boot Application Started with Maven
			16.9. Build an Executable Archive from Ant without Using spring-boot-antlib
		
		17. Traditional Deployment					// done 2020-7-26 16:53:48

			17.1. Create a Deployable War File
			17.2. Convert an Existing Application to Spring Boot
			17.3. Deploying a WAR to WebLogic
			17.4. Use Jedis Instead of Lettuce
			17.5. Use Testcontainers for integration testing

			tips:

				- Testcontainers: https://www.testcontainers.org/
					desc: Testcontainers is a Java library that supports JUnit tests, providing lightweight, throwaway instances of common databases, Selenium web browsers, or anything else that can run in a Docker container.

The reference documentation has the following appendices:

	Application Properties	 			Common application properties that can be used to configure your application. 	// done 2020-7-26 16:59:43

		1. Core properties
		2. Cache properties
		3. Mail properties
		4. JSON properties
		5. Data properties
		6. Transaction properties
		7. Data migration properties
		8. Integration properties
		9. Web properties
		10. Templating properties
		11. Server properties
		12. Security properties
		13. RSocket properties
		14. Actuator properties
		15. Devtools properties
		16. Testing properties

	Configuration Metadata	 			Metadata used to describe configuration properties. 	// done 2020-7-26 17:10:53

		1. Metadata Format
			1.1. Group Attributes
			1.2. Property Attributes
			1.3. Hint Attributes
			1.4. Repeated Metadata Items
		2. Providing Manual Hints
			2.1. Value Hint
			2.2. Value Providers
		3. Generating Your Own Metadata by Using the Annotation Processor
			3.1. Nested Properties
			3.2. Adding Additional Metadata

	Auto-configuration Classes	 		Auto-configuration classes provided by Spring Boot. 	// done 2020-7-26 17:14:35

		1. spring-boot-autoconfigure
		2. spring-boot-actuator-autoconfigure

	Test Auto-configuration Annotations	Test-autoconfiguration annotations used to test slices of your application. 	// done 2020-7-26 17:15:36

		1. Test Slices

	Executable Jars	 					Spring Boot’s executable jars, their launchers, and their format. 		// done 2020-7-26 17:22:54

		1. Nested JARs
		2. Spring Boot’s “JarFile” Class
		3. Launching Executable Jars
		4. PropertiesLauncher Features
		5. Executable Jar Restrictions
		6. Alternative Single Jar Solutions

	Dependency Versions	 				Details of the dependencies that are managed by Spring Boot. 			// done 2020-7-26 17:25:41
 	
		1. Managed Dependency Coordinates
		2. Version Properties