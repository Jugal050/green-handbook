webservice相关说明

	# Web Service:

		j2ee7: https://docs.oracle.com/javaee/7/tutorial/webservices-intro002.htm		

		spring-ws: https://docs.spring.io/spring-ws/docs/3.0.9.RELEASE/reference/

		spring: https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/integration.html#remoting-web-services

		springboot: https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-webservices

	# 生成wsdl文件：
	
		org.springframework.remoting.jaxws.AbstractJaxWsServiceExporter#afterPropertiesSet
		org.springframework.remoting.jaxws.SimpleJaxWsServiceExporter#publishEndpoint(javax.xml.ws.Endpoint, javax.jws.WebService)
		com.sun.xml.internal.ws.transport.http.server.EndpointImpl#publish(java.lang.String)
		com.sun.xml.internal.ws.server.EndpointFactory#create
		com.sun.xml.internal.ws.server.EndpointFactory#generateWSDL
		com.sun.xml.internal.ws.db.DatabindingImpl#generateWSDL
		com.sun.xml.internal.ws.wsdl.writer.WSDLGenerator#doGeneration
		com.sun.xml.internal.ws.wsdl.writer.WSDLGenerator#generateDocument


	# 生成wsdl文件相关组件：

		## wsdl生成器相关

			// 运行时模型： 获取wsdl各个标签模型
			com.sun.xml.internal.ws.model.RuntimeModeler 			

			// 数据绑定配置： 包括获取serviceName, portType等等
			com.sun.xml.internal.ws.api.databinding.DatabindingConfig

			// wsdl生成器信息： 包括wsdl解析器，容器，扩展信息等
			com.sun.xml.internal.ws.api.databinding.WSDLGenInfo

			// wsdl生成器
			com.sun.xml.internal.ws.wsdl.writer.WSDLGenerator

				// 生成wsdl
				doGeneration

			// 数据绑定实现类
			com.sun.xml.internal.ws.db.DatabindingImpl

		## wsdl服务发布相关

			// 服务器容器
			com.sun.xml.internal.ws.transport.http.server.ServerContainer

			// 服务器管理类： 包括创建内置的HTTP服务器
			com.sun.xml.internal.ws.transport.http.server.ServerMgr

			// HTTP服务器（jdk内置）： 可以将wsdl发布到该内置服务器上，默认实现类：sun.net.httpserver.HttpServerImpl
			com.sun.net.httpserver.HttpServer

			// 服务器socket通道实现类
			sun.nio.ch.ServerSocketChannelImpl

			// 服务器实现类
			sun.net.httpserver.ServerImpl

			// webservice的http请求处理器
			com.sun.xml.internal.ws.transport.http.server.WSHttpHandler


		## wsdl元素/标签相关

			// 容器元素
			com.sun.xml.internal.txw2.ContainerElement

			// wsdl标签元素所在jar包: com.sun.xml.internal.ws.wsdl.writer.document

				// definitions
				com.sun.xml.internal.ws.wsdl.writer.document.Definitions

			// 开始标签
			com.sun.xml.internal.txw2.StartTag	

			// ws终端实现类
			com.sun.xml.internal.ws.server.WSEndpointImpl