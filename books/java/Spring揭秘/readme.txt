《Spring揭秘》

	第一部分 掀起Spring的盖头来

		第1章 Spring框架的由来 							// done 2020-8-2 10:39:09

			1.1 Spring之崛起
			1.2 Spring框架概述
			1.3 Spring大观园
			1.4 小结

	第二部分　Spring的IoC容器 							

		第2章　IoC的基本概念　							// done 2020-8-2 11:11:26

			2.1 我们的理念是：让别人为你服务
			2.2 手语，呼喊，还是心有灵犀
				2.2.1 构造方法注入
				2.2.2 setter方法注入
				2.2.3 接口注入
				2.2.4 三种注入方式的比较
			2.3 Ioc的附加值
			2.4 小结

			IoC是一种可以帮助我们解耦个业务对象间依赖关系的对象绑定方式！

		第3章　掌管大局的IoC Service Provider　			// done 2020-8-2 11:26:45

			3.1 IoC Service Provider的职责
			3.2 运筹帷幄的秘密————IoC Service Provide如何管理对象间的依赖关系
				3.2.1 直接编码方式
				3.2.2 配置文件方式
				3.2.3 元数据方式
			3.3 小结
		
		第4章　Spring的IoC容器之BeanFactory　				// done 2020-8-2 17:42:58
		
			4.1 拥有BeanFactory之后的生活
			4.2 BeanFactory的对象注册和依赖绑定方式
				4.2.1 直接编码方式
				4.2.2 外部配置文件方式
					1. Properties配置格式的加载
					2. XML配置格式的加载
				4.2.3 注解方式
			4.3 BeanFactory的XML之旅
				4.3.1 <beans>和<bean>
					1. <beans>之唯我独尊
					2. <description>、<import>和<alias>
				4.3.2 孤孤单单一个人
				4.3.3 Help Me, Help You
					1. 构造方式注入的XML之道
					2. setter方法注入的XML之道
					3. <property>和<constructor-arg>中可用的配置项
					4. depends-on
					5. aotowire
					6. dependency-check
					7. lazy-init
				4.3.4 继承？我也会！
				4.3.5 bean的scope
					1. singleton
					2. protoype
					3. request、session和global session
					4. 自定义scope类型
				4.3.6 工厂方法和FactoryBean
					1. 静态工厂方法（Static Factory Method）
					2. 非静态工厂方法（Instance Factory Method）
					3. FactoryBean
				4.3.7 偷梁换柱之术
					1. 方法注入 
					2. 殊途同归
					3. 方法替换
			4.4 容器背后的秘密
				4.4.1 “战略性观望”
					1. 容器启动阶段
					2. Bean实例化阶段
				4.4.2 “插手”容器的启动
					1. PropertyPlaceholderConfigurer
					2. PropertyOverrideConfigurer
					3. CustomEditorConfiguer
				4.4.3 了解Bean的一生
					1. Bean的实例化和BeanWrapper
					2. 各色的Aware接口
					3. BeanPostProcessor
					4. InitializingBean和init-method
					5. DisposableBean和destory-method
			4.5 小结

		第5章　Spring IoC容器ApplicationContext　			// done 2020-8-2 19:11:29

			5.1 统一资源加载策略
				5.1.1 Spring中的Resource
				5.1.2 ResourceLoader，更广泛的“URL”
					1. 可用的ResourceLoader
					2. ResourePatternResolver————批量查找的ResourceLoader
					3. 回顾与展望
				5.1.3 Application和ResourceLoader
					1. 扮演ResourceLoader的角色
					2. RsourceLoader类型的注入
					3. Rsource类型的注入
					4. 在特定情况下，ApplicationContext的Resource加载行为
			5.2 国际化信息支持（I18n MessageSource）
				5.2.1 JavaSE提供的国际化支持
					1. Locale
					2. ResourceBundle
				5.2.2 MessageSource和ApplicationContext
					1. 可用的MessageSource实现
					2. MessageSourceAware和MessageSource的注入
			5.3 容器内部事件发布
				5.3.1 自定义事件发布
				5.3.2 Spring的容器内事件发布类结构分析
			5.4 多配置模块加载的简化
			5.5 小结

				ApplicationContext是Spring在BeanFactory基础容器之上，提供的另一个IoC容器实现。它拥有许多BeanFactory所没有的特性，
				包括统一的资源加载策略、国际化信息支持、容器内事件发布以及简化的多配置文件加载功能。

		第6章　Spring IoC容器之扩展篇　					// done 2020-8-2 19:29:42

			6.1 Spring2.5的基于注解的依赖注入
				6.1.1 注解版的自动绑定（@Autowired）
					1. 从自动绑定（autowire）到@Autowired
					2. @Qualifier的陪伴
				6.1.2 @Autowired之外的选择————使用JSR250标注依赖注入㽑
				6.1.3 将革命进行地更彻底一些（classpath-scanning功能介绍）
			6.2 Spring3.0展望
			6.3 小结

	第三部分　Spring AOP框架

		第7章　一起来看AOP　								// done 2020-8-3 23:43:26

			7.1 AOP的尴尬
			7.2 AOP走向现实
				7.2.1 静态AOP时代
				7.2.2 动态AOP时代
			7.3 Java平台上的AOP实现机制
				7.3.1 动态代理
					java.lang.reflect.InvocationHandler
				7.3.2 动态字节码增强
					ASM/CGLIB
				7.3.3 Java代码生成
				7.3.4 自定义类加载器 
				7.3.5 AOL扩展 
			7.4 AOP国家的公民
				7.4.1 Joinpoint
				7.4.2 Pointcut
					1. Pointcut的表述方式
					2. Pointcut运算
				7.4.3 Advice
					1. Before Advice
					2. After Advice
						- After returning Advice
						- After throwing Advice
						- After Advice
					3. Around Advice
					4. Introduction	
				7.4.4 Aspect
				7.4.5 织入与织入器
					org.springframework.aop.framework.ProxyFactory
				7.4.6 目标对象
			7.5 小结

		第8章　Spring AOP概述及其实现机制　						// done 2020-8-4 22:14:50
		
			8.1 Spring AOP概述
			8.2 Spring AOP的实现机制
				8.2.1 设计模式之代理模式
				8.2.2 动态代理
					java.lang.reflect.Proxy
					java.lang.reflect.InvocationHandler

					java: 
						// To create a proxy for some interface Foo:
				       InvocationHandler handler = new MyInvocationHandler(...);
				       Class<?> proxyClass = Proxy.getProxyClass(Foo.class.getClassLoader(), Foo.class);
				       Foo f = (Foo) proxyClass.getConstructor(InvocationHandler.class).
				                       newInstance(handler);
						   
						// or more simply:
				       Foo f = (Foo) Proxy.newProxyInstance(Foo.class.getClassLoader(),
				                                            new Class<?>[] { Foo.class },
				                                            handler);

				8.2.3 动态字节码生成
					net.sf.cglib.proxy.Callback
					net.sf.cglib.proxy.MethodInterceptor
					net.sf.cglib.proxy.Enhancer

					java: org.springframework.aop.framework.CglibAopProxy#getProxy(java.lang.ClassLoader)
						// Configure CGLIB Enhancer...
						Enhancer enhancer = new Enhancer();
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
						enhancer.setInterceptDuringConstruction(false);
						enhancer.setCallbacks(callbacks);
						return (this.constructorArgs != null && this.constructorArgTypes != null ?
								enhancer.create(this.constructorArgTypes, this.constructorArgs) :
								enhancer.create());

			8.3 小结 

		第9章　Spring AOP一世　							// done 2020-8-5 00:04:26
		
			9.1 Spring AOP中的Joinpoint
			9.2 Spring AOP中的Pointcut
			9.3 Spring AOP中的Advice
			9.4 Spring AOP中的Aspect
			9.5 Spring AOP中的织入
			9.6 TargetSource
			9.7 小结

		第10章　Spring AOP二世　
		


		第11章　AOP应用案例　
		


		第12章　Spring AOP之扩展篇　

	第四部分　使用Spring访问数据

		第13章　统一的数据访问异常层次体系　
		


		第14章　JDBC API的最佳实践　
		


		第15章　Spring对各种ORM的集成　
		


		第16章　Spring数据访问之扩展篇　

	第五部分　事务管理

		第17章　有关事务的楔子　
		


		第18章　群雄逐鹿下的Java事务管理　
		


		第19章　Spring事务王国的架构　
		


		第20章　使用Spring进行事务管理　
		


		第21章　Spring事务管理之扩展篇　

	第六部分　Spring的Web MVC框架

		第22章　迈向Spring MVC的旅程　
		


		第23章　Spring MVC初体验　
		


		第24章　近距离接触Spring MVC主要角色　
		


		第25章　认识更多Spring MVC家族成员　
		


		第26章　Spring MVC中基于注解的Controller　
		


		第27章　Spring MVC之扩展篇　

	第七部分　Spring框架对J2EE服务的集成和支持

		第28章　Spring框架内的JNDI支持　
		


		第29章　Spring框架对JMS的集成　
		


		第30章　使用Spring发送E-mail　
		


		第31章　Spring中的任务调度和线程池支持　
		


		第32章　Spring框架对J2EE服务的集成之扩展篇　
		


		第33章　Spring远程方案	
