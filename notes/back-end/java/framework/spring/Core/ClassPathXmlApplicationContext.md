# ClassPathXmlApplicationContext

类图：

![](D:\workspace\green-handbook\notes\back-end\java\framework\spring\Core\ClassPathXmlApplicationContext.png)

测试方法：

```java
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
```

Debug执行步骤：

- 加载类ContextClosedEvent，防止在WebLogic 8.1中应用关闭时发生的奇怪的类加载问题。

  其中，ContextClosedEvent为ApplicationContext关闭时触发的事件。

  ```java
  public abstract class AbstractApplicationContext extends DefaultResourceLoader
  		implements ConfigurableApplicationContext {
      
  	static {
  		// Eagerly load the ContextClosedEvent class to avoid weird classloader issues
  		// on application shutdown in WebLogic 8.1. (Reported by Dustin Woods.)
  		ContextClosedEvent.class.getName();
  	}
  
  }
  ```

- 接下来会调用ClassPathXmlApplicationContext构造器：

  ```java
  public ClassPathXmlApplicationContext(
      String[] configLocations, boolean refresh, @Nullable ApplicationContext parent)
      throws BeansException {
  
      super(parent);
      setConfigLocations(configLocations);
      if (refresh) {
          refresh();
      }
      
  }
  ```

  - super(parent)调用链：

    ```java
    /**
     * ApplicationContext的便捷的基础实现类，可以从XML文件中读取配置信息。
     *
     * 子类只需要实现getConfigResources/getConfigLocations，此外：
     *    在特殊环境下，子类可以重写方法getResourceByPath来解析相对路径的配置信息。
     *    或者重写方法getResourcePatternResolver来解析相同扩展类的文件。
     */
    public abstract class AbstractXmlApplicationContext extends AbstractRefreshableConfigApplicationContext {
        
        private boolean validating = true;
        
       	public AbstractXmlApplicationContext(@Nullable ApplicationContext parent) {
    		super(parent);
    	} 
        
    }
    ```

    调用父类构造器：

    ```java
    /**
     * AbstractRefreshableApplicationContext子类，增加了一些处理特殊配置路径的操作。
     * 可以用于基于XML的application context，比如：
     *    ClassPathXmlApplicationContext
     *    FileSystemXmlApplicationContext
     *    XmlWebApplicationContext
     */
    public abstract class AbstractRefreshableConfigApplicationContext extends AbstractRefreshableApplicationContext
    		implements BeanNameAware, InitializingBean {
        
        @Nullable
    	private String[] configLocations;
    
    	private boolean setIdCalled = false;
        
    	public AbstractRefreshableConfigApplicationContext(@Nullable ApplicationContext parent) {
    		super(parent);
    	}
    
    }
    ```

    调用父类构造器：

    ```java
    /**
     * ApplicationContext的基础实现类
     * 支持多次调用refresh()，每次调用会创建一个新的内部bean工厂实例。典型的，有多个配置路径需要加载时
     * 
     * 子类唯一需要实现的方法是loadBeanDefinitions，供每次调用refresh()时用，
     * 建议实现类将bean定义信息加载到DefaultListableBeanFactory。
     * 
     * WebApplicationContexts中有个类似的基础类AbstractRefreshableWebApplicationContext
     * 但是对web环境进行了默认的相关实现，也预先定义了web context的配置路径接收方法。
     *
     * 读取指定bean定义信息格式的子类有：
     *    ClassPathXmlApplicationContext, FileSystemXmlApplicationContext
     * 均继承自AbstractXmlApplicationContext
     *
     * AnnotationConfigApplicationContext支持@Configuration注解的类作为bean定义信息
     */
    public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {
        
        @Nullable
    	private Boolean allowBeanDefinitionOverriding;
    
    	@Nullable
    	private Boolean allowCircularReferences;
    
    	/** Bean factory for this context. */
    	@Nullable
    	private DefaultListableBeanFactory beanFactory;
    
    	/** Synchronization monitor for the internal BeanFactory. */
    	private final Object beanFactoryMonitor = new Object();
        
        public AbstractRefreshableApplicationContext(@Nullable ApplicationContext parent) {
    		super(parent);
    	}
        
    }
    ```

    调用父类构造器：

    ```java
    /**
     * ApplicationContext的抽象实现类。
     *     不要修改配置使用的数据存储类型。只实现通用的方法即可，使用设计模式“模板方法”。
     *
     * 与BeanFactory不同，ApplicationContext在内部bean工厂中默认注册了几个特殊bean，包括：
     *     - org.springframework.beans.factory.config.BeanFactoryPostProcessor
     *     - org.springframework.beans.factory.config.BeanPostProcessor
     *     - org.springframework.context.ApplicationListener
     *
     * 为了消息解析，会默认注册bean：
     *    name为“messageSource"，
     *    type为org.springframework.context.MessageSource
     *
     * 为了事件发布，会默认注册bean:
     *    name为"applicationEventMulticaster"，
     *    type为org.springframework.context.event.ApplicationEventMulticaster，
     *    默认是org.springframework.context.event.SimpleApplicationEventMulticaster
     *
     * 为了资源加载，继承类DefaultResourceLoader
     */
    public abstract class AbstractApplicationContext extends DefaultResourceLoader
    		implements ConfigurableApplicationContext {
    }
    ```
    
    调用父类`DefaultResourceLoader`构造器；
    
    调用父类`PathMatchingResourcePatternResolver`的构造器；
    
    至此，super(parent)结束;
    
  - setConfigLocations(configLocations)执行过程：
  
    ```java
    public abstract class AbstractRefreshableConfigApplicationContext extends AbstractRefreshableApplicationContext
    		implements BeanNameAware, InitializingBean {
    
        public void setConfigLocations(@Nullable String... locations) {
            if (locations != null) {
                Assert.noNullElements(locations, "Config locations must not be null");
                this.configLocations = new String[locations.length];
                for (int i = 0; i < locations.length; i++) {
                    this.configLocations[i] = resolvePath(locations[i]).trim();
                }
            }
            else {
                this.configLocations = null;
            }
        }
        
        protected String resolvePath(String path) {
    		return getEnvironment().resolveRequiredPlaceholders(path);
    	}
        
    }
    ```
  
    会调用`getEnvironment`，此处`environment`为`null`还未初始化，会继续调用`createEnvironment`
  
    ```java
    public abstract class AbstractApplicationContext extends DefaultResourceLoader
    		implements ConfigurableApplicationContext {
     
        @Override
    	public ConfigurableEnvironment getEnvironment() {
    		if (this.environment == null) {
    			this.environment = createEnvironment();
    		}
    		return this.environment;
    	}
        
        protected ConfigurableEnvironment createEnvironment() {
    		return new StandardEnvironment();
    	}
        
    }    
    ```
  
    继续调用`org.springframework.core.env.StandardEnvironment`的构造器
  
    ```java
    public class StandardEnvironment extends AbstractEnvironment {}
    ```
    
    StandardEnvironment类图：
    
     ![](D:\workspace\green-handbook\notes\back-end\java\framework\spring\Core\StandardEnvironment.png)
          
    
    调用父类`AbstractEnvironment`的构造器
    
      		其中初始化类变量`propertyResolver`时，会调用`PropertySourcesPropertyResolver`构造器
    
    再调用父类`AbstractPropertyResolver[解析相关资源配置的基础抽象类]`构造器
    
    完成之后继续返回`AbstractEnvironment`构造器，设置系统变量，包括：
    
    - 系统属性类`org.springframework.core.env.PropertiesPropertySource`；
    - 系统环境类`org.springframework.core.env.SystemEnvironmentPropertySource`；
    
    至此，`环境配置`信息设置`getEnvironment()`完成。
    
    继续调用`AbstractEnvironment#resolveRequiredPlaceholders`，解析相关占位符：
    
    ​		其中会调用`org.springframework.util.PropertyPlaceholderHelper`构造器；
    
    实例化完成之后，继续执行`doResolvePlaceholders`解析占位符（具体解析过程后续test中说明），解析完成，代表`AbstractRefreshableConfigApplicationContext#setConfigLocations`执行完成。
    
    继续调用`org.springframework.context.support.AbstractApplicationContext#refresh`方法：

------


  - `AbstractApplicationContext.refresh()`执行流程：
  
    ```java
    @Override
    public void refresh() throws BeansException, IllegalStateException {
        synchronized (this.startupShutdownMonitor) {
            // Prepare this context for refreshing.
            prepareRefresh();
    
            // Tell the subclass to refresh the internal bean factory.
            ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
    
            // Prepare the bean factory for use in this context.
            prepareBeanFactory(beanFactory);
    
            try {
                // Allows post-processing of the bean factory in context subclasses.
                postProcessBeanFactory(beanFactory);
    
                // Invoke factory processors registered as beans in the context.
                invokeBeanFactoryPostProcessors(beanFactory);
    
                // Register bean processors that intercept bean creation.
                registerBeanPostProcessors(beanFactory);
    
                // Initialize message source for this context.
                initMessageSource();
    
                // Initialize event multicaster for this context.
                initApplicationEventMulticaster();
    
                // Initialize other special beans in specific context subclasses.
                onRefresh();
    
                // Check for listener beans and register them.
                registerListeners();
    
                // Instantiate all remaining (non-lazy-init) singletons.
                finishBeanFactoryInitialization(beanFactory);
    
                // Last step: publish corresponding event.
                finishRefresh();
            }
    
            catch (BeansException ex) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Exception encountered during context initialization - " +
                                "cancelling refresh attempt: " + ex);
                }
    
                // Destroy already created singletons to avoid dangling resources.
                destroyBeans();
    
                // Reset 'active' flag.
                cancelRefresh(ex);
    
                // Propagate exception to caller.
                throw ex;
            }
    
            finally {
                // Reset common introspection caches in Spring's core, since we
                // might not ever need metadata for singleton beans anymore...
                resetCommonCaches();
            }
        }
    }
    ```
  
    我们一个一个来说明：
  
    - `prepareRefresh()`
    
      逻辑：
    
      ```java
      ApplicatoinContext刷新准备，包括：
          - 设置开始时间startupDate为当前时间
          - 设置关闭状态closed为false
          - 设置激活状态active为true
          - 初始化属性信息（方便子类实现使用，默认为空方法）
          - 校验必要的属性信息requiredProperties(通过propertyResolver进行校验)
          - 保存刷新前启动的监听器：
            -- 如果刷新前启动的监听器earlyApplicationListeners为空：初始化提前监听器为应用监听器：this.applicationListeners = new LinkedHashSet<>(this.applicationListeners)。
            -- 如果刷新前启动的监听器earlyApplicationListeners不为空：
               -- 清空应用监听器集合：this.applicationListeners.clear();
               -- 将刷新前启动的监听器添加到应用监听器列表中：this.applicationListeners.addAll(this.earlyApplicationListeners);
           - 初始化广播设置前发布的应用事件earlyApplicationEvents为空列表（为了广播一旦可用时，即可发布相关事件）
      ```
    
      源码：
    
      ```java
      /** Prepare this context for refreshing, setting its startup date and
       * active flag as well as performing any initialization of property sources.
       */ 
      protected void prepareRefresh() {
          // Switch to active.
          this.startupDate = System.currentTimeMillis();
          this.closed.set(false);
          this.active.set(true);
      
          // Initialize any placeholder property sources in the context environment.
          initPropertySources();
      
          // Validate that all properties marked as required are resolvable:
          // see ConfigurablePropertyResolver#setRequiredProperties
          getEnvironment().validateRequiredProperties();
      
          // Store pre-refresh ApplicationListeners...
          if (this.earlyApplicationListeners == null) {
              this.earlyApplicationListeners = new LinkedHashSet<>(this.applicationListeners);
          }
          else {
              // Reset local application listeners to pre-refresh state.
              this.applicationListeners.clear();
              this.applicationListeners.addAll(this.earlyApplicationListeners);
          }
      
          // Allow for the collection of early ApplicationEvents,
          // to be published once the multicaster is available...
          this.earlyApplicationEvents = new LinkedHashSet<>();
      }
      
      protected void initPropertySources() {
          // For subclasses: do nothing by default.
      }
      ```
    
      
    
    - `obtainFreshBeanFactory()`
    
      逻辑：
    
      ```java
      刷新BeanFactory
          先判断是否已有BeanFactory，判断方法：this.beanFactory != null（synchronized同步）
              如果有：
          		摧毁Beans
          			// TODO
          		关闭BeanFactory: this.beanFactory = null;
              如果没有：（第一次刷新时，并无BeanFactory，所以会执行此分支）
      			创建BeanFactory（入参包括内部的父级BeanFactory，默认为空）
                  	初始化默认的BeanFactory类DefaultListableBeanFactory
                  		调用父类构造方法AbstractAutowireCapableBeanFactory
                  			调用所有父类、父类的父类...进行类初始化
                  			忽略依赖接口，包括：
                  				BeanNameAware,BeanFactoryAware,BeanClassLoaderAware						   设置父级BeanFactory（默认为空）
      			设置序列化编号
                  定制BeanFactory，包括：是否允许重写Bean定义信息，是否允许循环引用（默认false）
                  加载Bean定义信息：AbstractXmlApplicationContext#loadBeanDefinitions
                  	新建Xml的Bean定义信息读取器XmlBeanDefinitionReader
                  	设置系统环境信息
                  	设置资源加载信息
                  	设置实体解析信息
                  	初始化beanDefinitionReader（设置是否校验为true）
                  	加载Bean定义信息AbstractXmlApplicationContext#loadBeanDefinitions
                  		获取配置资源信息：configResources,如果不为空，加载Bean定义信息
                  		获取配置路径信息：configLocations,如果不为空，加载Bean定义信息：
                              XmlBeanDefinitionReader#doLoadBeanDefinitions
                              // 文件解析器将xml文件流解析为Document类型doc
                              XmlBeanDefinitionReader#doLoadDocument
                              // 根据doc注册Bean
                              XmlBeanDefinitionReader#registerBeanDefinitions
                  				// 创建Bean定义信息解析器documentReader
                  				createBeanDefinitionDocumentReader();
                           		// 获取已注册的bean数量countBefore
      							getRegistry().getBeanDefinitionCount();
                  				// ！！！！！注册bean定义信息！！！！！
      							documentReader.registerBeanDefinitions
                                      
                  设置当前工厂类为该分支新建的工厂: this.beanFactory = beanFactory;
      ```
    
      执行堆栈：
    
      ```java
      "main@1" prio=5 tid=0x1 nid=NA runnable
        java.lang.Thread.State: RUNNABLE
      	  at org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader.doRegisterBeanDefinitions(DefaultBeanDefinitionDocumentReader.java:152)
      	  at org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader.registerBeanDefinitions(DefaultBeanDefinitionDocumentReader.java:96)
      	  at org.springframework.beans.factory.xml.XmlBeanDefinitionReader.registerBeanDefinitions(XmlBeanDefinitionReader.java:514)
      	  at org.springframework.beans.factory.xml.XmlBeanDefinitionReader.doLoadBeanDefinitions(XmlBeanDefinitionReader.java:394)
      	  at org.springframework.beans.factory.xml.XmlBeanDefinitionReader.loadBeanDefinitions(XmlBeanDefinitionReader.java:337)
      	  at org.springframework.beans.factory.xml.XmlBeanDefinitionReader.loadBeanDefinitions(XmlBeanDefinitionReader.java:305)
      	  at org.springframework.beans.factory.support.AbstractBeanDefinitionReader.loadBeanDefinitions(AbstractBeanDefinitionReader.java:188)
      	  at org.springframework.beans.factory.support.AbstractBeanDefinitionReader.loadBeanDefinitions(AbstractBeanDefinitionReader.java:224)
      	  at org.springframework.beans.factory.support.AbstractBeanDefinitionReader.loadBeanDefinitions(AbstractBeanDefinitionReader.java:195)
      	  at org.springframework.beans.factory.support.AbstractBeanDefinitionReader.loadBeanDefinitions(AbstractBeanDefinitionReader.java:257)
      	  at org.springframework.context.support.AbstractXmlApplicationContext.loadBeanDefinitions(AbstractXmlApplicationContext.java:128)
      	  at org.springframework.context.support.AbstractXmlApplicationContext.loadBeanDefinitions(AbstractXmlApplicationContext.java:94)
      	  at org.springframework.context.support.AbstractRefreshableApplicationContext.refreshBeanFactory(AbstractRefreshableApplicationContext.java:133)
      	  at org.springframework.context.support.AbstractApplicationContext.obtainFreshBeanFactory(AbstractApplicationContext.java:637)
      	  at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:522)
      	  - locked <0xaac> (a java.lang.Object)
      	  at org.springframework.context.support.ClassPathXmlApplicationContext.<init>(ClassPathXmlApplicationContext.java:144)
      	  at org.springframework.context.support.ClassPathXmlApplicationContext.<init>(ClassPathXmlApplicationContext.java:85)
      	  at org.springframework.context.support.ClassPathXmlApplicationContextTests.testSingleConfigLocation(ClassPathXmlApplicationContextTests.java:78)
      ```
    
      
    
      源码：
    
      `AbstractApplicationContext`
    
      ```java
      /**
        * Tell the subclass to refresh the internal bean factory.
        * @return the fresh BeanFactory instance
        * @see #refreshBeanFactory()
        * @see #getBeanFactory()
        */
      protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
          refreshBeanFactory();
          return getBeanFactory();
      }
      
      protected void destroyBeans() {
          getBeanFactory().destroySingletons();
      }
      ```
    
      `AbstractRefreshableApplicationContext#refreshBeanFactory`
    
      ```java
      @Override
      protected final void refreshBeanFactory() throws BeansException {
          if (hasBeanFactory()) {
              destroyBeans();
              closeBeanFactory();
          }
          try {
              DefaultListableBeanFactory beanFactory = createBeanFactory();
              beanFactory.setSerializationId(getId());
              customizeBeanFactory(beanFactory);
              loadBeanDefinitions(beanFactory);
              synchronized (this.beanFactoryMonitor) {
                  this.beanFactory = beanFactory;
              }
          }
          catch (IOException ex) {
              throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
          }
      }
      
      protected final boolean hasBeanFactory() {
          synchronized (this.beanFactoryMonitor) {
              return (this.beanFactory != null);
          }
      }
      
      protected DefaultListableBeanFactory createBeanFactory() {
          return new DefaultListableBeanFactory(getInternalParentBeanFactory());
      }
      ```
    
      默认的Bean工厂`DefaultListableBeanFactory`：
    
      类图:
    
      ![](D:\workspace\green-handbook\notes\back-end\java\framework\spring\Core\DefaultListableBeanFactory.png) 
    
      源码：
    
      ```java
      public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
      		implements ConfigurableListableBeanFactory, BeanDefinitionRegistry, Serializable {
          
      	@Nullable
      	private static Class<?> javaxInjectProviderClass;
      
      	static {
      		try {
      			javaxInjectProviderClass =
      					ClassUtils.forName("javax.inject.Provider", DefaultListableBeanFactory.class.getClassLoader());
      		}
      		catch (ClassNotFoundException ex) {
      			// JSR-330 API not available - Provider interface simply not supported then.
      			javaxInjectProviderClass = null;
      		}
      	}
      
      
      	/** Map from serialized id to factory instance. */
      	private static final Map<String, Reference<DefaultListableBeanFactory>> serializableFactories =
      			new ConcurrentHashMap<>(8);
      
      	/** Optional id for this factory, for serialization purposes. */
      	@Nullable
      	private String serializationId;
      
      	/** Whether to allow re-registration of a different definition with the same name. */
      	private boolean allowBeanDefinitionOverriding = true;
      
      	/** Whether to allow eager class loading even for lazy-init beans. */
      	private boolean allowEagerClassLoading = true;
      
      	/** Optional OrderComparator for dependency Lists and arrays. */
      	@Nullable
      	private Comparator<Object> dependencyComparator;
      
      	/** Resolver to use for checking if a bean definition is an autowire candidate. */
      	private AutowireCandidateResolver autowireCandidateResolver = new SimpleAutowireCandidateResolver();
      
      	/** Map from dependency type to corresponding autowired value. */
      	private final Map<Class<?>, Object> resolvableDependencies = new ConcurrentHashMap<>(16);
      
      	/** Map of bean definition objects, keyed by bean name. */
      	private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
      
      	/** Map of singleton and non-singleton bean names, keyed by dependency type. */
      	private final Map<Class<?>, String[]> allBeanNamesByType = new ConcurrentHashMap<>(64);
      
      	/** Map of singleton-only bean names, keyed by dependency type. */
      	private final Map<Class<?>, String[]> singletonBeanNamesByType = new ConcurrentHashMap<>(64);
      
      	/** List of bean definition names, in registration order. */
      	private volatile List<String> beanDefinitionNames = new ArrayList<>(256);
      
      	/** List of names of manually registered singletons, in registration order. */
      	private volatile Set<String> manualSingletonNames = new LinkedHashSet<>(16);
      
      	/** Cached array of bean definition names in case of frozen configuration. */
      	@Nullable
      	private volatile String[] frozenBeanDefinitionNames;
      
      	/** Whether bean definition metadata may be cached for all beans. */
      	private volatile boolean configurationFrozen = false;
      
      
      	/**
      	 * Create a new DefaultListableBeanFactory.
      	 */
      	public DefaultListableBeanFactory() {
      		super();
      	}
      
      	/**
      	 * Create a new DefaultListableBeanFactory with the given parent.
      	 * @param parentBeanFactory the parent BeanFactory
      	 */
      	public DefaultListableBeanFactory(@Nullable BeanFactory parentBeanFactory) {
      		super(parentBeanFactory);
      	}
          
          /**
           * 根据bean定义信息注册bean
           *    校验bean信息
           *    获取bean定义集合beanDefinitionMap：
           *       如果不为空，判断是否允许覆盖：
           *          如果不允许覆盖，抛出异常
           *          如果允许覆盖，用新bean覆盖旧bean
           *       如果为空，判断bean创建流程是否已开始：
           *          如果已开始，说明不是启动注册阶段，后续流程加锁操作：
           *          如果未开始，说明是启动注册阶段，后续无需加锁，直接操作：
           *             已注册的bean信息集合beanDefinitionMap中添加该bean信息
           *             已注册的bean名称集合beanDefinitionNames中添加该bean名称
           */
          @Override
      	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
      			throws BeanDefinitionStoreException {
      
      		if (beanDefinition instanceof AbstractBeanDefinition) {
      			try {
      				((AbstractBeanDefinition) beanDefinition).validate();
      			}
      			catch (BeanDefinitionValidationException ex) {
      				throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName,
      						"Validation of bean definition failed", ex);
      			}
      		}
      
      		BeanDefinition existingDefinition = this.beanDefinitionMap.get(beanName);
      		if (existingDefinition != null) {
      			if (!isAllowBeanDefinitionOverriding()) {
      				throw new BeanDefinitionOverrideException(beanName, beanDefinition, existingDefinition);
      			}
      			else {
                      // ouput logs
      			}
      			this.beanDefinitionMap.put(beanName, beanDefinition);
      		}
      		else {
      			if (hasBeanCreationStarted()) {
      				// Cannot modify startup-time collection elements anymore (for stable iteration)
      				synchronized (this.beanDefinitionMap) {
      					this.beanDefinitionMap.put(beanName, beanDefinition);
      					List<String> updatedDefinitions = new ArrayList<>(this.beanDefinitionNames.size() + 1);
      					updatedDefinitions.addAll(this.beanDefinitionNames);
      					updatedDefinitions.add(beanName);
      					this.beanDefinitionNames = updatedDefinitions;
      					removeManualSingletonName(beanName);
      				}
      			}
      			else {
      				// Still in startup registration phase
      				this.beanDefinitionMap.put(beanName, beanDefinition);
      				this.beanDefinitionNames.add(beanName);
      				removeManualSingletonName(beanName);
      			}
      			this.frozenBeanDefinitionNames = null;
      		}
      
      		if (existingDefinition != null || containsSingleton(beanName)) {
      			resetBeanDefinition(beanName);
      		}
      	}
          
          /**
           * 删除人工注册的单例bean名称
           */
          private void removeManualSingletonName(String beanName) {
      		updateManualSingletonNames(set -> set.remove(beanName), set -> set.contains(beanName));
      	}
          
          /**
           * 修改bean工厂内部的手动注册的单例bean名称集合
           */ 
          private void updateManualSingletonNames(Consumer<Set<String>> action, Predicate<Set<String>> condition) {
      		if (hasBeanCreationStarted()) {
      			// Cannot modify startup-time collection elements anymore (for stable iteration)
      			synchronized (this.beanDefinitionMap) {
      				if (condition.test(this.manualSingletonNames)) {
      					Set<String> updatedSingletons = new LinkedHashSet<>(this.manualSingletonNames);
      					action.accept(updatedSingletons);
      					this.manualSingletonNames = updatedSingletons;
      				}
      			}
      		}
      		else {
      			// Still in startup registration phase
      			if (condition.test(this.manualSingletonNames)) {
      				action.accept(this.manualSingletonNames);
      			}
      		}
      	}
      
      }
      ```
      
      
      
      抽象的可自动装备的Bean工厂`AbstractAutowireCapableBeanFactory`：
      
      ```java
      public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory
      		implements AutowireCapableBeanFactory {
          
      	public AbstractAutowireCapableBeanFactory() {
      		super();
      		ignoreDependencyInterface(BeanNameAware.class);
      		ignoreDependencyInterface(BeanFactoryAware.class);
      		ignoreDependencyInterface(BeanClassLoaderAware.class);
      	}
      
      	public AbstractAutowireCapableBeanFactory(@Nullable BeanFactory parentBeanFactory) {
      		this();
      		setParentBeanFactory(parentBeanFactory);
      	}
      }
      ```
    
      抽象的Xml的ApplicationContext类`AbstractXmlApplicationContext`：
    
      ```java
    public abstract class AbstractXmlApplicationContext extends AbstractRefreshableConfigApplicationContext {
      
      	private boolean validating = true;
          
          @Override
      	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
      		// Create a new XmlBeanDefinitionReader for the given BeanFactory.
      		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
      
      		// Configure the bean definition reader with this context's
      		// resource loading environment.
      		beanDefinitionReader.setEnvironment(this.getEnvironment());
      		beanDefinitionReader.setResourceLoader(this);
      		beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));
      
      		// Allow a subclass to provide custom initialization of the reader,
      		// then proceed with actually loading the bean definitions.
      		initBeanDefinitionReader(beanDefinitionReader);
      		loadBeanDefinitions(beanDefinitionReader);
      	}
      
          protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
      		Resource[] configResources = getConfigResources();
      		if (configResources != null) {
      			reader.loadBeanDefinitions(configResources);
      		}
      		String[] configLocations = getConfigLocations();
      		if (configLocations != null) {
      			reader.loadBeanDefinitions(configLocations);
      		}
      	}
      
      }
      ```
      
      抽象的Bean定义信息读取类`AbstractBeanDefinitionReader[BeanDefinitionReader接口的抽象的基础实现类]`：
      
      Xml配置的Bean定义信息读取类`XmlBeanDefinitionReader`
      
      ```java
      /**
       * Xml配置的Bean定义信息读取类，xml文件读取为BeanDefinitionDocumentReader接口的实现类
       *
       */
      public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {}
      ```
      
      默认的文件加载实现类`org.springframework.beans.factory.xml.DefaultDocumentLoader`：
      
      ```java
      public class DefaultDocumentLoader implements DocumentLoader {}
      ```
      
      默认的bean的文件解析器类`DefaultBeanDefinitionDocumentReader`：
      
      ```java
      public class DefaultBeanDefinitionDocumentReader implements BeanDefinitionDocumentReader {
          
          /**
           * 注册给定root元素节点<beans/>下的所有bean
           *
      	 * Register each bean definition within the given root {@code <beans/>} element.
      	 */
      	@SuppressWarnings("deprecation")  // for Environment.acceptsProfiles(String...)
      	protected void doRegisterBeanDefinitions(Element root) {
      		// Any nested <beans> elements will cause recursion in this method. In
      		// order to propagate and preserve <beans> default-* attributes correctly,
      		// keep track of the current (parent) delegate, which may be null. Create
      		// the new (child) delegate with a reference to the parent for fallback purposes,
      		// then ultimately reset this.delegate back to its original (parent) reference.
    		// this behavior emulates a stack of delegates without actually necessitating one.
      		BeanDefinitionParserDelegate parent = this.delegate;
    		this.delegate = createDelegate(getReaderContext(), root, parent);
      
      		if (this.delegate.isDefaultNamespace(root)) {
      			String profileSpec = root.getAttribute(PROFILE_ATTRIBUTE);
      			if (StringUtils.hasText(profileSpec)) {
      				String[] specifiedProfiles = StringUtils.tokenizeToStringArray(
      						profileSpec, BeanDefinitionParserDelegate.MULTI_VALUE_ATTRIBUTE_DELIMITERS);
      				// We cannot use Profiles.of(...) since profile expressions are not supported
      				// in XML config. See SPR-12458 for details.
      				if (!getReaderContext().getEnvironment().acceptsProfiles(specifiedProfiles)) {
      					if (logger.isDebugEnabled()) {
      						logger.debug("Skipped XML bean definition file due to specified profiles [" + profileSpec +
      								"] not matching: " + getReaderContext().getResource());
      					}
      					return;
      				}
      			}
      		}
      		
      		preProcessXml(root);
      		parseBeanDefinitions(root, this.delegate);
      		postProcessXml(root);
      
      		this.delegate = parent;
      	}
      
          /**
           * 允许在bean注册前预处理xml文件中自定义的元素类型。（默认空实现）
           */
      	protected void preProcessXml(Element root) {
      	}
      
          /**
           * 解析root级的元素：import, alias, bean
           */
          protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
    		if (delegate.isDefaultNamespace(root)) {
      			NodeList nl = root.getChildNodes();
    			for (int i = 0; i < nl.getLength(); i++) {
      				Node node = nl.item(i);
      				if (node instanceof Element) {
      					Element ele = (Element) node;
      					if (delegate.isDefaultNamespace(ele)) {
      						parseDefaultElement(ele, delegate);
      					}
      					else {
      						delegate.parseCustomElement(ele);
      					}
      				}
      			}
      		}
      		else {
      			delegate.parseCustomElement(root);
      		}
      	}
          
          private void parseDefaultElement(Element ele, BeanDefinitionParserDelegate delegate) {
      		if (delegate.nodeNameEquals(ele, IMPORT_ELEMENT)) {
      			importBeanDefinitionResource(ele);
      		}
      		else if (delegate.nodeNameEquals(ele, ALIAS_ELEMENT)) {
      			processAliasRegistration(ele);
      		}
      		else if (delegate.nodeNameEquals(ele, BEAN_ELEMENT)) {
      			processBeanDefinition(ele, delegate);
      		}
      		else if (delegate.nodeNameEquals(ele, NESTED_BEANS_ELEMENT)) {
      			// recurse
      			doRegisterBeanDefinitions(ele);
      		}
      	}
          
          /**
           * 处理指定的元素，解析并注册bean定义信息
           *    解析bean
           *    装饰bean（如有必要）
           *    注册bean
           *    发布组件注册事件
           */
          protected void processBeanDefinition(Element ele, BeanDefinitionParserDelegate delegate) {
      		BeanDefinitionHolder bdHolder = delegate.parseBeanDefinitionElement(ele);
      		if (bdHolder != null) {
      			bdHolder = delegate.decorateBeanDefinitionIfRequired(ele, bdHolder);
      			try {
      				// Register the final decorated instance.
      				BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
      			}
      			catch (BeanDefinitionStoreException ex) {
      				getReaderContext().error("Failed to register bean definition with name '" +
      						bdHolder.getBeanName() + "'", ele, ex);
      			}
      			// Send registration event.
      			getReaderContext().fireComponentRegistered(new BeanComponentDefinition(bdHolder));
      		}
      	}
      
      }    
      ```
      
      `BeanDefinitionParserDelegate`
      
      ```java
      public class BeanDefinitionParserDelegate {
      
          /** 解析bean元素 */
          @Nullable
      	public BeanDefinitionHolder parseBeanDefinitionElement(Element ele) {
      		return parseBeanDefinitionElement(ele, null);
      	}
          
          /**
         * 解析bean元素，解析异常返回null
           */
        @Nullable
      	public BeanDefinitionHolder parseBeanDefinitionElement(Element ele, @Nullable BeanDefinition containingBean) {
      		String id = ele.getAttribute(ID_ATTRIBUTE);
      		String nameAttr = ele.getAttribute(NAME_ATTRIBUTE);
      
      		List<String> aliases = new ArrayList<>();
      		if (StringUtils.hasLength(nameAttr)) {
      			String[] nameArr = StringUtils.tokenizeToStringArray(nameAttr, MULTI_VALUE_ATTRIBUTE_DELIMITERS);
      			aliases.addAll(Arrays.asList(nameArr));
      		}
      
      		String beanName = id;
      		if (!StringUtils.hasText(beanName) && !aliases.isEmpty()) {
      			beanName = aliases.remove(0);
      			if (logger.isTraceEnabled()) {
      				logger.trace("No XML 'id' specified - using '" + beanName +
      						"' as bean name and " + aliases + " as aliases");
      			}
      		}
      
      		if (containingBean == null) {
      			checkNameUniqueness(beanName, aliases, ele);
      		}
      
      		AbstractBeanDefinition beanDefinition = parseBeanDefinitionElement(ele, beanName, containingBean);
      		if (beanDefinition != null) {
      			if (!StringUtils.hasText(beanName)) {
      				try {
      					if (containingBean != null) {
      						beanName = BeanDefinitionReaderUtils.generateBeanName(
      								beanDefinition, this.readerContext.getRegistry(), true);
      					}
      					else {
      						beanName = this.readerContext.generateBeanName(beanDefinition);
      						// Register an alias for the plain bean class name, if still possible,
      						// if the generator returned the class name plus a suffix.
      						// This is expected for Spring 1.2/2.0 backwards compatibility.
      						String beanClassName = beanDefinition.getBeanClassName();
      						if (beanClassName != null &&
      								beanName.startsWith(beanClassName) && beanName.length() > beanClassName.length() &&
      								!this.readerContext.getRegistry().isBeanNameInUse(beanClassName)) {
      							aliases.add(beanClassName);
      						}
      					}
      					if (logger.isTraceEnabled()) {
      						logger.trace("Neither XML 'id' nor 'name' specified - " +
      								"using generated bean name [" + beanName + "]");
      					}
      				}
      				catch (Exception ex) {
      					error(ex.getMessage(), ele);
      					return null;
      				}
      			}
      			String[] aliasesArray = StringUtils.toStringArray(aliases);
      			return new BeanDefinitionHolder(beanDefinition, beanName, aliasesArray);
      		}
      
    		return null;
      	}
        
          /**
           * 解析bean定义信息，不包括name/alias
           */ 
          @Nullable
      	public AbstractBeanDefinition parseBeanDefinitionElement(
      			Element ele, String beanName, @Nullable BeanDefinition containingBean) {
      
      		this.parseState.push(new BeanEntry(beanName));
      
      		String className = null;
      		if (ele.hasAttribute(CLASS_ATTRIBUTE)) {
      			className = ele.getAttribute(CLASS_ATTRIBUTE).trim();
      		}
      		String parent = null;
      		if (ele.hasAttribute(PARENT_ATTRIBUTE)) {
      			parent = ele.getAttribute(PARENT_ATTRIBUTE);
      		}
      
      		try {
      			AbstractBeanDefinition bd = createBeanDefinition(className, parent);
      
      			parseBeanDefinitionAttributes(ele, beanName, containingBean, bd);
      			bd.setDescription(DomUtils.getChildElementValueByTagName(ele, DESCRIPTION_ELEMENT));
      
      			parseMetaElements(ele, bd);
      			parseLookupOverrideSubElements(ele, bd.getMethodOverrides());
      			parseReplacedMethodSubElements(ele, bd.getMethodOverrides());
      
      			parseConstructorArgElements(ele, bd);
      			parsePropertyElements(ele, bd);
      			parseQualifierElements(ele, bd);
      
      			bd.setResource(this.readerContext.getResource());
      			bd.setSource(extractSource(ele));
      
      			return bd;
      		}
      		catch (ClassNotFoundException ex) {
      			error("Bean class [" + className + "] not found", ele, ex);
      		}
      		catch (NoClassDefFoundError err) {
      			error("Class that bean class [" + className + "] depends on not found", ele, err);
      		}
      		catch (Throwable ex) {
      			error("Unexpected failure during bean definition parsing", ele, ex);
      		}
      		finally {
      			this.parseState.pop();
      		}
      
      		return null;
      	}
      
      
      }
      ```
      
      `BeanDefinitionReaderUtils`
      
    ```java
      /**
     * bean定义信息解析器工具类
       */
      public abstract class BeanDefinitionReaderUtils {
          
          /**
           * 使用指定的beanFactory注册指定的bean
           */ 
          public static void registerBeanDefinition(
      			BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
      			throws BeanDefinitionStoreException {
      
      		// Register bean definition under primary name.
      		String beanName = definitionHolder.getBeanName();
      		registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());
      
      		// Register aliases for bean name, if any.
      		String[] aliases = definitionHolder.getAliases();
      		if (aliases != null) {
      			for (String alias : aliases) {
      				registry.registerAlias(beanName, alias);
      			}
      		}
      	}
          
      }
      ```
      
      最终会调回`DefaultListableBeanFactory#registerBeanDefinition`；
    
    - `prepareBeanFactory(beanFactory)`
    - `postProcessBeanFactory(beanFactory)`
    - `invokeBeanFactoryPostProcessors(beanFactory)`
    - `registerBeanPostProcessors(beanFactory)`
    - `initMessageSource()`
    - `initApplicationEventMulticaster()`
    - `onRefresh()`
    - `registerListeners()`
    - `finishBeanFactoryInitialization(beanFactory)`
    - `finishRefresh()`
    - `destroyBeans()`
    - `cancelRefresh(ex)`
    - `resetCommonCaches()`
    
    
    
    
    
    
    
    
    
    
    
    











