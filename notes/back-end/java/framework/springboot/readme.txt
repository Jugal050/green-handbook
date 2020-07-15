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

	Spring Boot Features	 			Profiles, Logging, Security, Caching, Spring Integration, Testing, and more.
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
		11. Working with SQL Databases
		12. Working with NoSQL Technologies
		13. Caching
		14. Messaging
		15. Calling REST Services with RestTemplate
		16. Calling REST Services with WebClient
		17. Validation
		18. Sending Email
		19. Distributed Transactions with JTA
		20. Hazelcast
		21. Quartz Scheduler
		22. Task Execution and Scheduling
		23. Spring Integration
		24. Spring Session
		25. Monitoring and Management over JMX
		26. Testing
		27. WebSockets
		28. Web Services
		29. Creating Your Own Auto-configuration
		30. Kotlin support
		31. Building Docker Images
		32. What to Read Next

	Spring Boot Actuator	 			Monitoring, Metrics, Auditing, and more.

	Deploying Spring Boot Applications	Deploying to the Cloud, Installing as a Unix application.

	Spring Boot CLI	 					Installing the CLI, Using the CLI, Configuring the CLI, and more.

	Build Tool Plugins	 				Maven Plugin, Gradle Plugin, Antlib, and more.

	“How-to” Guides	 					Application Development, Configuration, Embedded Servers, Data Access, and many more.

The reference documentation has the following appendices:

	Application Properties	 			Common application properties that can be used to configure your application.

	Configuration Metadata	 			Metadata used to describe configuration properties.

	Auto-configuration Classes	 		Auto-configuration classes provided by Spring Boot.

	Test Auto-configuration Annotations	Test-autoconfiguration annotations used to test slices of your application.

	Executable Jars	 					Spring Boot’s executable jars, their launchers, and their format.

	Dependency Versions	 				Details of the dependencies that are managed by Spring Boot.