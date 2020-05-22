The Java EE Tutorial: https://docs.oracle.com/javaee/7/tutorial/index.html 		// start 2020-4-4 11:29:31

	第六章 Web Services

		27 Web Service简介

			27.1 什么是Web Service?

				Web services是通过HTTP协议进行通信的客户端和服务端应用程序。如W3C描述，web services提供了一个不同平台、框架的软件应用之间交互的标准规范。Web services因其强大的可操作性和可扩展性被大家熟知，得益于XML的使用，Web services可以使用一种松耦合的方式来达到复杂的操作。程序提供简单的服务就可以与其他应用进行复杂的增值服务交互。

			27.2 Web Services的类型

				从概念层面来说，一个service就是通过网络端提供的软件组件。服务的消费者和提供者使用消息来交换调用的文档表单里的请求和响应信息，而该文档表单与接收者的技术性能影响很小。

				从技术层面来说，web services可以通过不同的方式实现，本章讨论的两种类型可以分为："big"web service和RESTful web services;

				27.2.1 "big" Web Service

					JavaEE7中，JAX-WS为"big" web services提供了功能，Big web services使用遵循SOAP标准（Simple Object Access Protocal 简单对象传输协议）的XML消息，XML语言定义消息的结构和格式。通常通过WSDL(Web Services Description Language)书写，包含机器可读的该服务提供的功能描述。

					基于SOAP设计的服务必须包含以下元素：

						- 发布的包含webservice提供的接口的描述性合同。可以使用WSDL来描述合同的详细信息，可以包含messages，operations，bindings，和location of web service。在JAX-WS服务中无需发布WSDL也可以处理SOAP信息。

						- 该结构必须指定完整的非功能性要求。许多web services包含特殊的地址要求，和自定义的通用词汇，例如transactions, security, addressing, trust, conrdination 等等

						- 该结构需要处理异步处理和调用。这种情况下，按如下标准提供的基础功能，支持客户端异步调用，可以开箱即用。如：WSRM(Web Services Reliable Messaging)， 和 APIs如JAX-WS。

				27.2.2 RESTful Web Service

					JavaEE7中，JAX-RE为RESTful(Representatinal State Transfer 表示状态转移)的web service提供了功能，REST使用与基础的、特别的场景。相比基于SOAP的web service，可以更好的集成HTTP，而无需使用XML消息和WSDL定义。

					Jersey项目是JAX-RS的一个实现，支持注解，可以让开发者在Java和JVM中，更简单地来构建RESTful的web service.

					由于RESTful的web服务使用了数值的W3C和IETF标准，以及其轻量级的架构，使得其服务开发的代价更低，接收障碍更少。

					当满足如下条件时，可以考虑RESTful服务：

						- web服务是完全无状态的。一个简单的判断方法是，该服务的交互是否可以忍受服务器的重启。

						- 能提高性能的缓存机制。值得注意的是，大多数服务器只允许缓存HTTP的GET请求。

						- 服务的提供者和消费者有一些数值的认知。因为RESTful的web服务没有一个固定的接口描述规范。

						- 带宽很重要，需要被限制。 基于SOAP协议的web服务中，XML中的头信息和其他附加的层级信息会占用资源。

						- RESTful风格的web服务可以很容易地继承到现有的网站。

			27.3 如何决定使用哪一种Web Service？

				基本上，在web上可以使用RESTful web services，而在有较高服务质量要求的企业级集成应用中，使用big web servcies.

		28 使用JAX-WS构建Web Services

			28.1 使用JAX-WS创建一个简单的Web Service和客户端	

				28.1.1
				28.1.2
				28.1.3
				28.1.4
				28.1.5

			28.2 JAX-WS支持类型
			
		29 使用JAX-RE构建RESTfule Web Services		

			29.1
			29.2
			29.3
			29.4

		30 使用JAX-RS客户端API访问REST资源
		
		31 JAX-RS: 高级主题和示例			// done 2020-4-4 23:28:11





