# webservice resource

## toolkit

### jdk/bin: https://docs.oracle.com/javase/8/docs/technotes/tools/windows/s10-web-services-tools.html#sthref305

- schemagen: Generates a schema for every name space that is referenced in your Java classes.

  examples: 

  ```bash
  schemagen.sh Foo.java Bar.java ...
  ```

- wsgen: Reads a web service endpoint implementation class (SEI) and generates all of the required artifacts for web service deployment and invocation.

  examples: 

  	The following example generates the wrapper classes for StockService with @WebService annotations inside stock directory.
  	
  		wsgen -d stock -cp myclasspath stock.StockService
  	
  	The following example generates a SOAP 1.1 WSDL and schema for the stock.StockService class with @WebService annotations.
  	
  		wsgen -wsdl -d stock -cp myclasspath stock.StockService
  	
  	The following example generates a SOAP 1.2 WSDL.
  	
  		wsgen -wsdl:Xsoap1.2 -d stock -cp myclasspath stock.StockService 

- wsimport: Generates JAX-WS portable artifacts that can be packaged in a WAR file and provides an Ant task.
    	
    examples:

    ```bash
    The following example generates the Java artifacts and compiles the artifacts by importing http://stockquote.example.com/quote?wsdl
      	
      	wsimport -p stockquote http://stockquote.example.com/quote?wsdl
    ```

- xjc: Compiles an XML schema file into fully annotated Java classes.					

### maven-plugin

- org.codehaus.mojo:jaxb2-maven-plugin:2.5.0

  ```xml
  <plugin>
    	<groupId>org.codehaus.mojo</groupId>
    	<artifactId>jaxb2-maven-plugin</artifactId>
    	<version>2.5.0</version>
    	<executions>
    		<execution>
    			<id>xjc</id>
    			<goals>
    				<goal>xjc</goal>
    			</goals>
    		</execution>
    	</executions>
    	<configuration>
    		<sources>
    			<source>${project.basedir}/src/main/resources/countries.xsd</source>
    		</sources>
    	</configuration>
    </plugin>
  ```

- org.jvnet.jaxb2.maven2:maven-jaxb2-plugin:0.14.0

  ```xml
  <plugin>
  	<groupId>org.jvnet.jaxb2.maven2</groupId>
  	<artifactId>maven-jaxb2-plugin</artifactId>
  	<version>0.14.0</version>
  	<executions>
  		<execution>
  			<goals>
  				<goal>generate</goal>
  			</goals>
  		</execution>
  	</executions>
  	<configuration>
  		<schemaLanguage>WSDL</schemaLanguage>
  		<generatePackage>com.example.consumingwebservice.wsdl</generatePackage>
  		<schemas>
  			<schema>
  				<url>http://localhost:8081/ws/countries.wsdl</url>
  			</schema>
  		</schemas>
  	</configuration>
  </plugin>
  ```

## website
- jaxb: https://javaee.github.io/jaxb-v2/
- jaxb-user-guide: https://javaee.github.io/jaxb-v2/doc/user-guide/ch03.html	
- spring-ws: https://docs.spring.io/spring-ws/docs/3.0.9.RELEASE/reference
- springboot-guide-ws: 
	- producing: 
		guide: https://spring.io/guides/gs/producing-web-service/
		git: https://github.com/spring-guides/gs-producing-web-service
	- consuming:
		guide: https://spring.io/guides/gs/consuming-web-service/
		git: https://github.com/spring-guides/gs-consuming-web-service		
- wsdl: https://www.w3.org/TR/wsdl	
- soap: https://www.w3.org/TR/soap

