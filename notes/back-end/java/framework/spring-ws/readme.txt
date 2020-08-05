https://docs.spring.io/spring-ws/docs/3.0.9.RELEASE/reference/ 					// start 2020-8-5 22:01:31	

	I. Introduction

		1. What is Spring Web Services?
			1.1. Introduction
				1.1.1. Powerful mappings
				1.1.2. XML API support
				1.1.3. Flexible XML Marshalling
				1.1.4. Reuses your Spring expertise
				1.1.5. Supports WS-Security
				1.1.6. Integrates with Spring Security
				1.1.7. Apache license
			1.2. Runtime environment
			1.3. Supported standards

		2. Why Contract First?
			2.1. Introduction
			2.2. Object/XML Impedance Mismatch
				2.2.1. XSD extensions
				2.2.2. Unportable types
				2.2.3. Cyclic graphs
			2.3. Contract-first versus Contract-last
				2.3.1. Fragility
				2.3.2. Performance
				2.3.3. Reusability
				2.3.4. Versioning

		3. Writing Contract-First Web Services
			3.1. Introduction
			3.2. Messages
				3.2.1. Holiday
				3.2.2. Employee
				3.2.3. HolidayRequest
			3.3. Data Contract
			3.4. Service contract
			3.5. Creating the project
			3.6. Implementing the Endpoint
				3.6.1. Handling the XML Message
				3.6.2. Routing the Message to the Endpoint
				3.6.3. Providing the Service and Stub implementation
			3.7. Publishing the WSDL

	II. Reference

		4. Shared components 						// done 2020-8-6 00:10:27
			4.1. Web service messages
				4.1.1. WebServiceMessage
				4.1.2. SoapMessage
				4.1.3. Message Factories
				4.1.4. MessageContext
			4.2. TransportContext
			4.3. Handling XML With XPath
				4.3.1. XPathExpression
				4.3.2. XPathTemplate
			4.4. Message Logging and Tracing

		5. Creating a Web service with Spring-WS

		6. Using Spring Web Services on the Client
		
		7. Securing your Web services with Spring-WS

	III. Other Resources

