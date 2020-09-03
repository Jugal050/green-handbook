# springboot webservice guide:

## 发布webservice服务: 

### spring-guide

 		https://spring.io/guides/gs/producing-web-service/

### git

 		https://github.com/spring-guides/gs-producing-web-service

### steps

#### 1. maven中添加webservice依赖: pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.3.2.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.example</groupId>
	<artifactId>producing-web-service</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>producing-web-service</name>
	<description>Demo project for Spring Boot</description>

	<properties>
		<java.version>1.8</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web-services</artifactId>
		</dependency>
		<!-- tag::springws[] -->
		<dependency>
			<groupId>wsdl4j</groupId>
			<artifactId>wsdl4j</artifactId>
		</dependency>
		<!-- end::springws[] -->

    <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
			<exclusions>
				<exclusion>
					<groupId>org.junit.vintage</groupId>
					<artifactId>junit-vintage-engine</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
	</dependencies>

	<profiles>
		<profile>
			<id>java11</id>
			<activation>
				<jdk>[11,)</jdk>
			</activation>

			<dependencies>
				<dependency>
					<groupId>org.glassfish.jaxb</groupId>
					<artifactId>jaxb-runtime</artifactId>
				</dependency>
			</dependencies>
		</profile>
	</profiles>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
			<!-- tag::xsd[] -->
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
			<!-- end::xsd[] -->
		</plugins>
	</build>

</project>
```

#### 2. 创建实体对象的xsd配置：

countries.xsd

```xml
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:tns="http://spring.io/guides/gs-producing-web-service"
           targetNamespace="http://spring.io/guides/gs-producing-web-service" elementFormDefault="qualified">

    <xs:element name="getCountryRequest">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="name" type="xs:string"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:element name="getCountryResponse">
        <xs:complexType>
            <xs:sequence>
                <xs:element name="country" type="tns:country"/>
            </xs:sequence>
        </xs:complexType>
    </xs:element>

    <xs:complexType name="country">
        <xs:sequence>
            <xs:element name="name" type="xs:string"/>
            <xs:element name="population" type="xs:int"/>
            <xs:element name="capital" type="xs:string"/>
            <xs:element name="currency" type="tns:currency"/>
        </xs:sequence>
    </xs:complexType>

    <xs:simpleType name="currency">
        <xs:restriction base="xs:string">
            <xs:enumeration value="GBP"/>
            <xs:enumeration value="EUR"/>
            <xs:enumeration value="PLN"/>
        </xs:restriction>
    </xs:simpleType>
</xs:schema>
```

#### 3. 根据xsd实体配置信息生成实体类文件：

  使用maven插件[org.codehaus.mojo:jaxb2-maven-plugin:2.5.0]生成实体类文件

  - 执行插件命令: 

    ```bash
    mvn org.codehaus.mojo:jaxb2-maven-plugin:xjc -e -X
    ```

  - 生成的实体类文件：

    country.java

    ```java
    //
    // 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.3.2 生成的
    // 请访问 <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
    // 在重新编译源模式时, 对此文件的所有修改都将丢失。
    // 生成时间: 2020.09.03 时间 10:07:17 PM CST 
    //
    
    
    package io.spring.guides.gs_producing_web_service;
    
    import javax.xml.bind.annotation.XmlAccessType;
    import javax.xml.bind.annotation.XmlAccessorType;
    import javax.xml.bind.annotation.XmlElement;
    import javax.xml.bind.annotation.XmlSchemaType;
    import javax.xml.bind.annotation.XmlType;
    
    
    /**
     * <p>country complex type的 Java 类。
     * 
     * <p>以下模式片段指定包含在此类中的预期内容。
     * 
     * <pre>
     * &lt;complexType name="country"&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="population" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
     *         &lt;element name="capital" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="currency" type="{http://spring.io/guides/gs-producing-web-service}currency"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "country", propOrder = {
        "name",
        "population",
        "capital",
        "currency"
    })
    public class Country {
    
        @XmlElement(required = true)
        protected String name;
        protected int population;
        @XmlElement(required = true)
        protected String capital;
        @XmlElement(required = true)
        @XmlSchemaType(name = "string")
        protected Currency currency;
    
        /**
         * 获取name属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }
    
        /**
         * 设置name属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }
    
        /**
         * 获取population属性的值。
         * 
         */
        public int getPopulation() {
            return population;
        }
    
        /**
         * 设置population属性的值。
         * 
         */
        public void setPopulation(int value) {
            this.population = value;
        }
    
        /**
         * 获取capital属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCapital() {
            return capital;
        }
    
        /**
         * 设置capital属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCapital(String value) {
            this.capital = value;
        }
    
        /**
         * 获取currency属性的值。
         * 
         * @return
         *     possible object is
         *     {@link Currency }
         *     
         */
        public Currency getCurrency() {
            return currency;
        }
    
        /**
         * 设置currency属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link Currency }
         *     
         */
        public void setCurrency(Currency value) {
            this.currency = value;
        }
    
    }
    
    ```

    Currency.java

    ```java
    //
    // 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.3.2 生成的
    // 请访问 <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
    // 在重新编译源模式时, 对此文件的所有修改都将丢失。
    // 生成时间: 2020.09.03 时间 10:07:17 PM CST 
    //
    
    
    package io.spring.guides.gs_producing_web_service;
    
    import javax.xml.bind.annotation.XmlEnum;
    import javax.xml.bind.annotation.XmlType;
    
    
    /**
     * <p>currency的 Java 类。
     * 
     * <p>以下模式片段指定包含在此类中的预期内容。
     * <p>
     * <pre>
     * &lt;simpleType name="currency"&gt;
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *     &lt;enumeration value="GBP"/&gt;
     *     &lt;enumeration value="EUR"/&gt;
     *     &lt;enumeration value="PLN"/&gt;
     *   &lt;/restriction&gt;
     * &lt;/simpleType&gt;
     * </pre>
     * 
     */
    @XmlType(name = "currency")
    @XmlEnum
    public enum Currency {
    
        GBP,
        EUR,
        PLN;
    
        public String value() {
            return name();
        }
    
        public static Currency fromValue(String v) {
            return valueOf(v);
        }
    
    }
    
    ```

    GetCountryRequest.java

    ```java
    //
    // 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.3.2 生成的
    // 请访问 <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
    // 在重新编译源模式时, 对此文件的所有修改都将丢失。
    // 生成时间: 2020.09.03 时间 10:07:17 PM CST 
    //
    
    
    package io.spring.guides.gs_producing_web_service;
    
    import javax.xml.bind.annotation.XmlAccessType;
    import javax.xml.bind.annotation.XmlAccessorType;
    import javax.xml.bind.annotation.XmlElement;
    import javax.xml.bind.annotation.XmlRootElement;
    import javax.xml.bind.annotation.XmlType;
    
    
    /**
     * <p>anonymous complex type的 Java 类。
     * 
     * <p>以下模式片段指定包含在此类中的预期内容。
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "name"
    })
    @XmlRootElement(name = "getCountryRequest")
    public class GetCountryRequest {
    
        @XmlElement(required = true)
        protected String name;
    
        /**
         * 获取name属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }
    
        /**
         * 设置name属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }
    
    }
    
    ```

    GetCountryResponse.java

    ```java
    //
    // 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.3.2 生成的
    // 请访问 <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
    // 在重新编译源模式时, 对此文件的所有修改都将丢失。
    // 生成时间: 2020.09.03 时间 10:07:17 PM CST 
    //
    
    
    package io.spring.guides.gs_producing_web_service;
    
    import javax.xml.bind.annotation.XmlAccessType;
    import javax.xml.bind.annotation.XmlAccessorType;
    import javax.xml.bind.annotation.XmlElement;
    import javax.xml.bind.annotation.XmlRootElement;
    import javax.xml.bind.annotation.XmlType;
    
    
    /**
     * <p>anonymous complex type的 Java 类。
     * 
     * <p>以下模式片段指定包含在此类中的预期内容。
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="country" type="{http://spring.io/guides/gs-producing-web-service}country"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "country"
    })
    @XmlRootElement(name = "getCountryResponse")
    public class GetCountryResponse {
    
        @XmlElement(required = true)
        protected Country country;
    
        /**
         * 获取country属性的值。
         * 
         * @return
         *     possible object is
         *     {@link Country }
         *     
         */
        public Country getCountry() {
            return country;
        }
    
        /**
         * 设置country属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link Country }
         *     
         */
        public void setCountry(Country value) {
            this.country = value;
        }
    
    }
    
    ```

#### 4. 创建Country Repository

  ```java
  package com.example.producingwebservice;
  
  import javax.annotation.PostConstruct;
  import java.util.HashMap;
  import java.util.Map;
  
  import io.spring.guides.gs_producing_web_service.Country;
  import io.spring.guides.gs_producing_web_service.Currency;
  import org.springframework.stereotype.Component;
  import org.springframework.util.Assert;
  
  @Component
  public class CountryRepository {
  	private static final Map<String, Country> countries = new HashMap<>();
  
  	@PostConstruct
  	public void initData() {
  		Country spain = new Country();
  		spain.setName("Spain");
  		spain.setCapital("Madrid");
  		spain.setCurrency(Currency.EUR);
  		spain.setPopulation(46704314);
  
  		countries.put(spain.getName(), spain);
  
  		Country poland = new Country();
  		poland.setName("Poland");
  		poland.setCapital("Warsaw");
  		poland.setCurrency(Currency.PLN);
  		poland.setPopulation(38186860);
  
  		countries.put(poland.getName(), poland);
  
  		Country uk = new Country();
  		uk.setName("United Kingdom");
  		uk.setCapital("London");
  		uk.setCurrency(Currency.GBP);
  		uk.setPopulation(63705000);
  
  		countries.put(uk.getName(), uk);
  	}
  
  	public Country findCountry(String name) {
  		Assert.notNull(name, "The country's name must not be null");
  		return countries.get(name);
  	}
  }
  
  ```

#### 5. 创建Country Service Endpoint

  ```java
  package com.example.producingwebservice;
  
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.ws.server.endpoint.annotation.Endpoint;
  import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
  import org.springframework.ws.server.endpoint.annotation.RequestPayload;
  import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
  
  import io.spring.guides.gs_producing_web_service.GetCountryRequest;
  import io.spring.guides.gs_producing_web_service.GetCountryResponse;
  
  @Endpoint
  public class CountryEndpoint {
  	private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";
  
  	private CountryRepository countryRepository;
  
  	@Autowired
  	public CountryEndpoint(CountryRepository countryRepository) {
  		this.countryRepository = countryRepository;
  	}
  
  	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCountryRequest")
  	@ResponsePayload
  	public GetCountryResponse getCountry(@RequestPayload GetCountryRequest request) {
  		GetCountryResponse response = new GetCountryResponse();
  		response.setCountry(countryRepository.findCountry(request.getName()));
  
  		return response;
  	}
  }
  
  ```

#### 6. 配置Web Service Beans

  ```java
  package com.example.producingwebservice;
  
  import org.springframework.boot.web.servlet.ServletRegistrationBean;
  import org.springframework.context.ApplicationContext;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.core.io.ClassPathResource;
  import org.springframework.ws.config.annotation.EnableWs;
  import org.springframework.ws.config.annotation.WsConfigurerAdapter;
  import org.springframework.ws.transport.http.MessageDispatcherServlet;
  import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
  import org.springframework.xml.xsd.SimpleXsdSchema;
  import org.springframework.xml.xsd.XsdSchema;
  
  @EnableWs
  @Configuration
  public class WebServiceConfig extends WsConfigurerAdapter {
  	@Bean
  	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
  		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
  		servlet.setApplicationContext(applicationContext);
  		servlet.setTransformWsdlLocations(true);
  		return new ServletRegistrationBean(servlet, "/ws/*");
  	}
  
  	@Bean(name = "countries")
  	public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema countriesSchema) {
  		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
  		wsdl11Definition.setPortTypeName("CountriesPort");
  		wsdl11Definition.setLocationUri("/ws");
  		wsdl11Definition.setTargetNamespace("http://spring.io/guides/gs-producing-web-service");
  		wsdl11Definition.setSchema(countriesSchema);
  		return wsdl11Definition;
  	}
  
  	@Bean
  	public XsdSchema countriesSchema() {
  		return new SimpleXsdSchema(new ClassPathResource("countries.xsd"));
  	}
  }
  
  ```

#### 7. 启动服务

  ```java
  package com.example.producingwebservice;
  
  import org.springframework.boot.SpringApplication;
  import org.springframework.boot.autoconfigure.SpringBootApplication;
  
  @SpringBootApplication
  public class ProducingWebServiceApplication {
  
  	public static void main(String[] args) {
  		SpringApplication.run(ProducingWebServiceApplication.class, args);
  	}
  }
  ```

- 构建可执行jar包

  - maven运行服务

    ```bash
    ./mvnw spring-boot:run
    ```

  - maven打包

    ```
    ./mvnw clean package
    ```

  - 运行jar包

    ```java
    java -jar target/gs-soap-service-0.1.0.jar
    ```

#### 8. 测试

  - 创建request文件

    ```xml
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
    				  xmlns:gs="http://spring.io/guides/gs-producing-web-service">
       <soapenv:Header/>
       <soapenv:Body>
          <gs:getCountryRequest>
             <gs:name>Spain</gs:name>
          </gs:getCountryRequest>
       </soapenv:Body>
    </soapenv:Envelope>
    ```

  - 使用SoapUI或者curl命令测试

    ```bash
    # Use data from file
    curl --header "content-type: text/xml" -d @request.xml http://localhost:8080/ws
    ```

    ```bash
    # Use inline XML data
    curl <<-EOF -fsSL -H "content-type: text/xml" -d @- http://localhost:8080/ws \
      > target/response.xml && xmllint --format target/response.xml
    
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                                      xmlns:gs="http://spring.io/guides/gs-producing-web-service">
       <soapenv:Header/>
       <soapenv:Body>
          <gs:getCountryRequest>
             <gs:name>Spain</gs:name>
          </gs:getCountryRequest>
       </soapenv:Body>
    </soapenv:Envelope>
    
    EOF
    ```

  - 测试结果：

    ```xml
    <?xml version="1.0"?>
    <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
      <SOAP-ENV:Header/>
      <SOAP-ENV:Body>
        <ns2:getCountryResponse xmlns:ns2="http://spring.io/guides/gs-producing-web-service">
          <ns2:country>
            <ns2:name>Spain</ns2:name>
            <ns2:population>46704314</ns2:population>
            <ns2:capital>Madrid</ns2:capital>
            <ns2:currency>EUR</ns2:currency>
          </ns2:country>
        </ns2:getCountryResponse>
      </SOAP-ENV:Body>
    </SOAP-ENV:Envelope>
    ```
    
    

------



## 调用webservice服务: 

### spring-guide
 		https://spring.io/guides/gs/consuming-web-service/

### git
 		https://github.com/spring-guides/gs-consuming-web-service

### steps

#### 1. 添加maven依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.3.2.RELEASE</version>
		<!-- lookup parent from repository -->
		<relativePath/>
	</parent>
	<groupId>com.example</groupId>
	<artifactId>consuming-web-service</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>consuming-web-service</name>
	<description>Demo project for Spring Boot</description>

	<properties>
		<java.version>1.8</java.version>
	</properties>

	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter</artifactId>
		</dependency>
		<!-- tag::dependency[] -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web-services</artifactId>
			<exclusions>
				<exclusion>
					<groupId>org.springframework.boot</groupId>
					<artifactId>spring-boot-starter-tomcat</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
		<!-- end::dependency[] -->

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
			<exclusions>
				<exclusion>
					<groupId>org.junit.vintage</groupId>
					<artifactId>junit-vintage-engine</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
	</dependencies>

	<!-- tag::profile[] -->
	<profiles>
		<profile>
			<id>java11</id>
			<activation>
				<jdk>[11,)</jdk>
			</activation>

			<dependencies>
				<dependency>
					<groupId>org.glassfish.jaxb</groupId>
					<artifactId>jaxb-runtime</artifactId>
				</dependency>
			</dependencies>
		</profile>
	</profiles>
	<!-- end::profile[] -->

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
			<!-- tag::wsdl[] -->
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
			<!-- end::wsdl[] -->
		</plugins>
	</build>

</project>

```

#### 2. 根据wsdl文件生成对应实体类

```bash
mvn org.jvnet.jaxb2.maven2:maven-jaxb2-plugin:0.14.0:generate -e -X
```

与发布webservice服务时生成的实体类对象相同

#### 3. 创建Counry Service Client

```java

package com.example.consumingwebservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.example.consumingwebservice.wsdl.GetCountryRequest;
import com.example.consumingwebservice.wsdl.GetCountryResponse;

public class CountryClient extends WebServiceGatewaySupport {

	private static final Logger log = LoggerFactory.getLogger(CountryClient.class);

	public GetCountryResponse getCountry(String country) {

		GetCountryRequest request = new GetCountryRequest();
		request.setName(country);

		log.info("Requesting location for " + country);

		GetCountryResponse response = (GetCountryResponse) getWebServiceTemplate()
				.marshalSendAndReceive("http://localhost:8081/ws/countries", request,
						new SoapActionCallback(
								"http://spring.io/guides/gs-producing-web-service/GetCountryRequest"));

		return response;
	}

}

```

#### 4. 配置Web Service组件

```java

package com.example.consumingwebservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class CountryConfiguration {

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		// this package must match the package in the <generatePackage> specified in
		// pom.xml
		marshaller.setContextPath("com.example.consumingwebservice.wsdl");
		return marshaller;
	}

	@Bean
	public CountryClient countryClient(Jaxb2Marshaller marshaller) {
		CountryClient client = new CountryClient();
		client.setDefaultUri("http://localhost:8081/ws");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

}

```

#### 5. 运行程序

```java

package com.example.consumingwebservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.consumingwebservice.wsdl.GetCountryResponse;

@SpringBootApplication
public class ConsumingWebServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsumingWebServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner lookup(CountryClient quoteClient) {
		return args -> {
			String country = "Spain";

			if (args.length > 0) {
				country = args[0];
			}
			GetCountryResponse response = quoteClient.getCountry(country);
			System.err.println(response.getCountry().getCurrency());
		};
	}

}

```

- maven命令行启动

  ```bash
  ./mvnw spring-boot:run
  ```

- maven打包

  ```bash
  ./mvnw clean package
  ```

- 执行jar包

  ```bash
  java -jar target/gs-consuming-web-service-0.1.0.jar
  ```

- 带参数执行jar包

  ```bash
  java -jar build/libs/gs-consuming-web-service-0.1.0.jar Poland
  ```




## 其他资源

- spring-ws: https://docs.spring.io/spring-ws/docs/3.0.9.RELEASE/reference
- wsdl: https://www.w3.org/TR/wsdl
- soap: https://www.w3.org/TR/soap