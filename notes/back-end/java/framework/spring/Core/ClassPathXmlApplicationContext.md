# ClassPathXmlApplicationContext

## 类结构：

![](D:\workspace\green-handbook\notes\back-end\java\framework\spring\Core\ClassPathXmlApplicationContext.png)

## 测试方法：

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

## 执行逻辑：

加载类ContextClosedEvent，防止在WebLogic 8.1中应用关闭时发生的奇怪的类加载问题。

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

接下来会调用ClassPathXmlApplicationContext构造器：

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

### super(parent)：

#### 简要说明：

​		 类实例化。

####执行逻辑：

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

------



### setConfigLocations(configLocations)：

#### 简要说明：

​		设置环境及配置信息；

####执行逻辑：

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

```java
其中初始化类变量propertyResolver时，会调用PropertySourcesPropertyResolver构造器

再调用父类AbstractPropertyResolver[解析相关资源配置的基础抽象类]构造器

完成之后继续返回AbstractEnvironment构造器，设置系统变量，包括：

- 系统属性类org.springframework.core.env.PropertiesPropertySource；
- 系统环境类org.springframework.core.env.SystemEnvironmentPropertySource；

至此，环境配置信息设置 getEnvironment()完成。

继续调用AbstractEnvironment#resolveRequiredPlaceholders，解析相关占位符：
    其中会调用org.springframework.util.PropertyPlaceholderHelper构造器；

实例化完成之后，继续执行doResolvePlaceholders解析占位符（具体解析过程后续test中说明）
```

解析完成，代表AbstractRefreshableConfigApplicationContext#setConfigLocations执行完成。

继续调用org.springframework.context.support.AbstractApplicationContext#refresh方法； 		

------



### refresh()：

#### 简要说明：

​		加载或刷新配置信息，信息载体可能是xml文件，properties文件，或关系型数据库表。

​		此方法为启动方法，为了避免资源不明确，如果失败，应该摧毁所有已经创建的单例对象。

​		也说明，调用此方法后，要么没有，要么所有的单例对象会被初始化。

#### 执行逻辑：

```java
public void refresh() throws BeansException, IllegalStateException {
	synchronized (this.startupShutdownMonitor) {  
        // 1. 刷新准备
        // 2. 通知子类将要刷新内置的beanFactory
        // 3. beanFactory使用前准备
        try {
            // 4. 调用自定义的beanFactory后置处理器（模板方法空实现，供子类实现使用）
            // 5. 调用已注册的beanFactory后置处理器
            // 6. 截bean创建的bean后置处理器
            // 7. 初始化消息源
            // 8. 初始化事件广播器
            // 9. 初始化自定义的bean（模板方法空实现，供子类实现使用）
            // 10. 注册监听器
            // 11. 初始化所有剩余的单例对象
            // 12. 发布对应事件
        }
        catch (BeansException ex) {
            // 13. 清除beans
            // 14. 取消刷新
            // 15. 抛出异常
        }
        finally {
            // 16. 重置通用缓存
            resetCommonCaches();
        }
    }
}    
```

#### 源码：

```java
/**
 * Load or refresh the persistent representation of the configuration,
 * which might an XML file, properties file, or relational database schema.
 * <p>As this is a startup method, it should destroy already created singletons
 * if it fails, to avoid dangling resources. In other words, after invocation
 * of that method, either all or no singletons at all should be instantiated.
 * @throws BeansException if the bean factory could not be initialized
 * @throws IllegalStateException if already initialized and multiple refresh
 * attempts are not supported
 */
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

#### 步骤分解:

##### `prepareRefresh()`

执行逻辑：

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



##### `obtainFreshBeanFactory()`

  执行逻辑：

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

  bean注册源码：

  ```java
  public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
  		implements ConfigurableListableBeanFactory, BeanDefinitionRegistry, Serializable {
      
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
  }
  ```

  其他相关类，不再一一说明：

  ​	`AbstractAutowireCapableBeanFactory`	

  ​	`AbstractXmlApplicationContext`

  ​	`AbstractBeanDefinitionReader`

  ​	`XmlBeanDefinitionReader`

  ​	`DefaultDocumentLoader`

  ​	`DefaultBeanDefinitionDocumentReader`

  ​	`BeanDefinitionParserDelegate`

  ​	`BeanDefinitionReaderUtils`




  最终会调回`DefaultListableBeanFactory#registerBeanDefinition`；

##### `prepareBeanFactory(beanFactory)`

  简要说明：beanFactory使用前准备

  执行逻辑：

  ```java
  设置Bean类加载器beanClassLoader = 当前类加载器
  设置Bean表达式解析器beanExpressionResolver 
  	= StandardBeanExpressionResolver[标准的Bean表达式解析器]
  添加属性编辑器的注册器propertyEditorRegistrars
  	add ResourceEditorRegistrar[资源编辑器注册器]
  
  添加Bean后置处理器
  	beanPostProcessors add ApplicationContextAwareProcessor[应用通知处理器]
  忽略接口依赖ignoredDependencyInterfaces 
  	add EnvironmentAware[环境通知类]
  	add EmbeddedValueResolverAware[内嵌的值处理器通知类]
  	add ResourceLoaderAware[资源加载器通知类]
  	add ApplicationEventPublisherAware[应用事件发布器通知类]
  	add MessageSourceAware[消息源通知类]
  	add ApplicationContextAware[应用上下文通知类]
  
  注册可解析的依赖resolvableDependencies
  	put {BeanFactory.class， beanFactory}
  	put {ResourceLoader.class.class， this}
  	put {ApplicationEventPublisher.class， this}
  	put {ApplicationContext.class， this}
  
  添加Bean后置处理器beanPostProcessors 
  	add ApplicationListenerDetector[应用监听处理器]
  
  如果beanFactory包含loadTimeWeaver[加载时植入类]，如果有，
  	Bean请求处理器beanPostProcessors 
  		add LoadTimeWeaverAwareProcessor[加载时植入通知处理器]
  	设置临时类加载器tempClassLoader 
  		= ContextTypeMatchClassLoader[上下文类型匹配类加载器]
  
  注册默认的环境相关beans，包括：environment	, systemProperties, systemEnvironment
  ```

  源码：

  ```java
  protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
      // Tell the internal bean factory to use the context's class loader etc.
      beanFactory.setBeanClassLoader(getClassLoader());
      beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));
      beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));
  
      // Configure the bean factory with context callbacks.
      beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
      beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
      beanFactory.ignoreDependencyInterface(EmbeddedValueResolverAware.class);
      beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
      beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
      beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
      beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);
  
      // BeanFactory interface not registered as resolvable type in a plain factory.
      // MessageSource registered (and found for autowiring) as a bean.
      beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
      beanFactory.registerResolvableDependency(ResourceLoader.class, this);
      beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
      beanFactory.registerResolvableDependency(ApplicationContext.class, this);
  
      // Register early post-processor for detecting inner beans as ApplicationListeners.
      beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));
  
      // Detect a LoadTimeWeaver and prepare for weaving, if found.
      if (beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
          beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
          // Set a temporary ClassLoader for type matching.
          beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
      }
  
      // Register default environment beans.
      if (!beanFactory.containsLocalBean(ENVIRONMENT_BEAN_NAME)) {
          beanFactory.registerSingleton(ENVIRONMENT_BEAN_NAME, getEnvironment());
      }
      if (!beanFactory.containsLocalBean(SYSTEM_PROPERTIES_BEAN_NAME)) {
          beanFactory.registerSingleton(SYSTEM_PROPERTIES_BEAN_NAME, getEnvironment().getSystemProperties());
      }
      if (!beanFactory.containsLocalBean(SYSTEM_ENVIRONMENT_BEAN_NAME)) {
          beanFactory.registerSingleton(SYSTEM_ENVIRONMENT_BEAN_NAME, getEnvironment().getSystemEnvironment());
      }
  }
  ```

  

##### `postProcessBeanFactory(beanFactory)`

  简要说明：后置处理context子类中的beanFactory，为子类覆盖提供模板方法

  执行逻辑：

  ```java
  默认实现为空，允许子类覆盖
  ```

  源码：

  ```java
  protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {}
  ```

  

##### `invokeBeanFactoryPostProcessors(beanFactory)`

  简要说明：调用beanFactory的后置处理器

  执行逻辑：

  ```java
  调用BeanFactory的后置处理器
  	判断beanFactory是否是Bean定义信息注册类的子类
  		如果是：
  			优先调用Bean定义注册的后置处理器BeanDefinitionRegistryPostProcessor，如果有
  				按照实现的接口优先级顺序遍历调用（PriorityOrdered > Ordered > 其他）
  			再调用BeanFactoryPostProcessor
  				按照顺序遍历调用（registryProcessors > regularPostProcessors）
  		否则：
  			遍历调用指定的后置处理器beanFactoryPostProcessors
  
  	调用其他还未调用过的普通beans的beanFactory后置处理器：
  		按照实现的接口优先级顺序遍历调用（PriorityOrdered > Ordered > 其他）
  
  	至此，所有后置处理器均已调用完成！！！！！
  
  如果 beanFactory中有loadTimeWeaver[加载时植入器] && tempClassLoader[临时类加载器]为null：
  	Bean请求处理器beanPostProcessors 
      	add LoadTimeWeaverAwareProcessor[加载时植入通知处理器]
  	设置临时类加载器tempClassLoader 
      	= ContextTypeMatchClassLoader[上下文类型匹配类加载器]
  
  ```

  源码：

  ```java
  protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
      PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());
  
      // Detect a LoadTimeWeaver and prepare for weaving, if found in the meantime
      // (e.g. through an @Bean method registered by ConfigurationClassPostProcessor)
      if (beanFactory.getTempClassLoader() == null && beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
          beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
          beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
      }
  }
  ```

  ```java
  public static void invokeBeanFactoryPostProcessors(
      ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {
  
      // Invoke BeanDefinitionRegistryPostProcessors first, if any.
      Set<String> processedBeans = new HashSet<>();
  
      if (beanFactory instanceof BeanDefinitionRegistry) {
          BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
          List<BeanFactoryPostProcessor> regularPostProcessors = new ArrayList<>();
          List<BeanDefinitionRegistryPostProcessor> registryProcessors = new ArrayList<>();
  
          for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
              if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
                  BeanDefinitionRegistryPostProcessor registryProcessor =
                      (BeanDefinitionRegistryPostProcessor) postProcessor;
                  registryProcessor.postProcessBeanDefinitionRegistry(registry);
                  registryProcessors.add(registryProcessor);
              }
              else {
                  regularPostProcessors.add(postProcessor);
              }
          }
  
          // Do not initialize FactoryBeans here: We need to leave all regular beans
          // uninitialized to let the bean factory post-processors apply to them!
          // Separate between BeanDefinitionRegistryPostProcessors that implement
          // PriorityOrdered, Ordered, and the rest.
          List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<>();
  
          // First, invoke the BeanDefinitionRegistryPostProcessors that implement PriorityOrdered.
          String[] postProcessorNames =
              beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
          for (String ppName : postProcessorNames) {
              if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
                  currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
                  processedBeans.add(ppName);
              }
          }
          sortPostProcessors(currentRegistryProcessors, beanFactory);
          registryProcessors.addAll(currentRegistryProcessors);
          invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
          currentRegistryProcessors.clear();
  
          // Next, invoke the BeanDefinitionRegistryPostProcessors that implement Ordered.
          postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
          for (String ppName : postProcessorNames) {
              if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
                  currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
                  processedBeans.add(ppName);
              }
          }
          sortPostProcessors(currentRegistryProcessors, beanFactory);
          registryProcessors.addAll(currentRegistryProcessors);
          invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
          currentRegistryProcessors.clear();
  
          // Finally, invoke all other BeanDefinitionRegistryPostProcessors until no further ones appear.
          boolean reiterate = true;
          while (reiterate) {
              reiterate = false;
              postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
              for (String ppName : postProcessorNames) {
                  if (!processedBeans.contains(ppName)) {
                      currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
                      processedBeans.add(ppName);
                      reiterate = true;
                  }
              }
              sortPostProcessors(currentRegistryProcessors, beanFactory);
              registryProcessors.addAll(currentRegistryProcessors);
              invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
              currentRegistryProcessors.clear();
          }
  
          // Now, invoke the postProcessBeanFactory callback of all processors handled so far.
          invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);
          invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
      }
  
      else {
          // Invoke factory processors registered with the context instance.
          invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
      }
  
      // Do not initialize FactoryBeans here: We need to leave all regular beans
      // uninitialized to let the bean factory post-processors apply to them!
      String[] postProcessorNames =
          beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);
  
      // Separate between BeanFactoryPostProcessors that implement PriorityOrdered,
      // Ordered, and the rest.
      List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
      List<String> orderedPostProcessorNames = new ArrayList<>();
      List<String> nonOrderedPostProcessorNames = new ArrayList<>();
      for (String ppName : postProcessorNames) {
          if (processedBeans.contains(ppName)) {
              // skip - already processed in first phase above
          }
          else if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
              priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
          }
          else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
              orderedPostProcessorNames.add(ppName);
          }
          else {
              nonOrderedPostProcessorNames.add(ppName);
          }
      }
  
      // First, invoke the BeanFactoryPostProcessors that implement PriorityOrdered.
      sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
      invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);
  
      // Next, invoke the BeanFactoryPostProcessors that implement Ordered.
      List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<>(orderedPostProcessorNames.size());
      for (String postProcessorName : orderedPostProcessorNames) {
          orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
      }
      sortPostProcessors(orderedPostProcessors, beanFactory);
      invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);
  
      // Finally, invoke all other BeanFactoryPostProcessors.
      List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<>(nonOrderedPostProcessorNames.size());
      for (String postProcessorName : nonOrderedPostProcessorNames) {
          nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
      }
      invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);
  
      // Clear cached merged bean definitions since the post-processors might have
      // modified the original metadata, e.g. replacing placeholders in values...
      beanFactory.clearMetadataCache();
  }
  ```

  

##### `registerBeanPostProcessors(beanFactory)`

  简要说明：注册拦截bean创建的bean后置处理器

  执行逻辑：

  ```java
  从beanFactory中获取BeanPostProcessor类型的beans
      按顺序添加bean后置处理器： [beanPostProcessors add ...]
         	BeanPostProcessorChecker[BeanPostProcessor检查器]
         	实现了PriorityOrdered的BeanPostProcessors
      	实现了Ordered的BeanPostProcessors
      	其他普通的BeanPostProcessors
      	内置的BeanPostProcessors
      	应用监听器探测器[ApplicationListenerDetector]
  ```

  源码：

  ```java
  protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
      PostProcessorRegistrationDelegate.registerBeanPostProcessors(beanFactory, this);
  }
  ```

  ```java
  public static void registerBeanPostProcessors(
      ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext) {
  
      String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);
  
      // Register BeanPostProcessorChecker that logs an info message when
      // a bean is created during BeanPostProcessor instantiation, i.e. when
      // a bean is not eligible for getting processed by all BeanPostProcessors.
      int beanProcessorTargetCount = beanFactory.getBeanPostProcessorCount() + 1 + postProcessorNames.length;
      beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));
  
      // Separate between BeanPostProcessors that implement PriorityOrdered,
      // Ordered, and the rest.
      List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
      List<BeanPostProcessor> internalPostProcessors = new ArrayList<>();
      List<String> orderedPostProcessorNames = new ArrayList<>();
      List<String> nonOrderedPostProcessorNames = new ArrayList<>();
      for (String ppName : postProcessorNames) {
          if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
              BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
              priorityOrderedPostProcessors.add(pp);
              if (pp instanceof MergedBeanDefinitionPostProcessor) {
                  internalPostProcessors.add(pp);
              }
          }
          else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
              orderedPostProcessorNames.add(ppName);
          }
          else {
              nonOrderedPostProcessorNames.add(ppName);
          }
      }
  
      // First, register the BeanPostProcessors that implement PriorityOrdered.
      sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
      registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);
  
      // Next, register the BeanPostProcessors that implement Ordered.
      List<BeanPostProcessor> orderedPostProcessors = new ArrayList<>(orderedPostProcessorNames.size());
      for (String ppName : orderedPostProcessorNames) {
          BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
          orderedPostProcessors.add(pp);
          if (pp instanceof MergedBeanDefinitionPostProcessor) {
              internalPostProcessors.add(pp);
          }
      }
      sortPostProcessors(orderedPostProcessors, beanFactory);
      registerBeanPostProcessors(beanFactory, orderedPostProcessors);
  
      // Now, register all regular BeanPostProcessors.
      List<BeanPostProcessor> nonOrderedPostProcessors = new ArrayList<>(nonOrderedPostProcessorNames.size());
      for (String ppName : nonOrderedPostProcessorNames) {
          BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
          nonOrderedPostProcessors.add(pp);
          if (pp instanceof MergedBeanDefinitionPostProcessor) {
              internalPostProcessors.add(pp);
          }
      }
      registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);
  
      // Finally, re-register all internal BeanPostProcessors.
      sortPostProcessors(internalPostProcessors, beanFactory);
      registerBeanPostProcessors(beanFactory, internalPostProcessors);
  
      // Re-register post-processor for detecting inner beans as ApplicationListeners,
      // moving it to the end of the processor chain (for picking up proxies etc).
      beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(applicationContext));
  }
  ```

  

##### `initMessageSource()`

  简要说明：初始化消息源

  执行逻辑：

  ```java
  判断beanFacory中是否有名称为'messageSource'的bean：
      如果有：
      	如果父容器不为空，且此messageSource可继承
      	如果父容器的数据源为空
      	设置父容器消息源
      如果没有：
      	设置父容器消息源[dms = new DelegatingMessageSource()]
      	设置此容器消息源[this.messageSource = dms]
      	注册单例bean，名称为[messageSource]，内容为[this.messageSource]
  ```

  源码：

  ```java
  protected void initMessageSource() {
      ConfigurableListableBeanFactory beanFactory = getBeanFactory();
      if (beanFactory.containsLocalBean(MESSAGE_SOURCE_BEAN_NAME)) {
          this.messageSource = beanFactory.getBean(MESSAGE_SOURCE_BEAN_NAME, MessageSource.class);
          // Make MessageSource aware of parent MessageSource.
          if (this.parent != null && this.messageSource instanceof HierarchicalMessageSource) {
              HierarchicalMessageSource hms = (HierarchicalMessageSource) this.messageSource;
              if (hms.getParentMessageSource() == null) {
                  // Only set parent context as parent MessageSource if no parent MessageSource
                  // registered already.
                  hms.setParentMessageSource(getInternalParentMessageSource());
              }
          }
          if (logger.isTraceEnabled()) {
              logger.trace("Using MessageSource [" + this.messageSource + "]");
          }
      }
      else {
          // Use empty MessageSource to be able to accept getMessage calls.
          DelegatingMessageSource dms = new DelegatingMessageSource();
          dms.setParentMessageSource(getInternalParentMessageSource());
          this.messageSource = dms;
          beanFactory.registerSingleton(MESSAGE_SOURCE_BEAN_NAME, this.messageSource);
          if (logger.isTraceEnabled()) {
              logger.trace("No '" + MESSAGE_SOURCE_BEAN_NAME + "' bean, using [" + this.messageSource + "]");
          }
      }
  }
  ```

  

##### `initApplicationEventMulticaster()`

  简要说明：初始化事件广播器

  执行逻辑：

  ```java
  判断beanFacory中是否有名称为'applicationEventMulticaster'的bean：				
      如果有：
      	获取并设置为当前的applicationEventMulticaster
      如果没有：
      	新建并设置为当前的applicationEventMulticaster
      		= new SimpleApplicationEventMulticaster(beanFactory);
  		注册单例bean，
              名称为[applicationEventMulticaster]，内容为[this.applicationEventMulticaster]
  ```

  源码：

  ```java
  protected void initApplicationEventMulticaster() {
      ConfigurableListableBeanFactory beanFactory = getBeanFactory();
      if (beanFactory.containsLocalBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME)) {
          this.applicationEventMulticaster =
              beanFactory.getBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, ApplicationEventMulticaster.class);
          if (logger.isTraceEnabled()) {
              logger.trace("Using ApplicationEventMulticaster [" + this.applicationEventMulticaster + "]");
          }
      }
      else {
          this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
          beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, this.applicationEventMulticaster);
          if (logger.isTraceEnabled()) {
              logger.trace("No '" + APPLICATION_EVENT_MULTICASTER_BEAN_NAME + "' bean, using " +
                           "[" + this.applicationEventMulticaster.getClass().getSimpleName() + "]");
          }
      }
  }
  ```

  

##### `onRefresh()`

  简要说明：初始化容器子类中的特殊bean

  执行逻辑：

  ```java
  模板方法，默认为空，可被子类重写。	
  ```

  源码：

  ```java
  /**
   * Template method which can be overridden to add context-specific refresh work.
   * Called on initialization of special beans, before instantiation of singletons.
   * <p>This implementation is empty.
   * @throws BeansException in case of errors
   * @see #refresh()
   */
  protected void onRefresh() throws BeansException {
  	// For subclasses: do nothing by default.
  }
  ```

  

##### `registerListeners()`

  简要说明：检查并注册监听器

  执行逻辑：

  ```java
  注册容器中的静态特殊监听器
  从beanFactory中获取ApplicationListener类的beans，并注册
  提前发布 需要提前发布的 应用事件[监听器提早执行]
  ```

  源码：

  ```java
  /**
   * Add beans that implement ApplicationListener as listeners.
   * Doesn't affect other listeners, which can be added without being beans.
   */
  protected void registerListeners() {
  	// Register statically specified listeners first.
  	for (ApplicationListener<?> listener : getApplicationListeners()) {
  		getApplicationEventMulticaster().addApplicationListener(listener);
  	}
  
  	// Do not initialize FactoryBeans here: We need to leave all regular beans
  	// uninitialized to let post-processors apply to them!
  	String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
  	for (String listenerBeanName : listenerBeanNames) {
  		getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
  	}
  
  	// Publish early application events now that we finally have a multicaster...
  	Set<ApplicationEvent> earlyEventsToProcess = this.earlyApplicationEvents;
  	this.earlyApplicationEvents = null;
  	if (earlyEventsToProcess != null) {
  		for (ApplicationEvent earlyEvent : earlyEventsToProcess) {
  			getApplicationEventMulticaster().multicastEvent(earlyEvent);
  		}
  	}
  }
  ```

  

##### `finishBeanFactoryInitialization(beanFactory)`

  简要说明：初始化所有剩余的单例bean

  执行逻辑：

  ```java
  初始化conversionService
  如果没有内置的取值解析器embeddedValueResolvers，则注册一个默认的
  提前初始化加载时植入通知器LoadTimeWeaverAware，以便进行提前处理
  停止使用类型匹配的临时类加载器: this.tempClassLoader = null
  缓存所有的bean定义元数据，不再修改: 
  	this.configurationFrozen = true; 
  	this.frozenBeanDefinitionNames = StringUtils.toStringArray(this.beanDefinitionNames);
  初始化其他的单例bean（非懒加载的）
  ```

  源码：

  ```java
  /**
   * Finish the initialization of this context's bean factory,
   * initializing all remaining singleton beans.
   */
  protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
  	// Initialize conversion service for this context.
  	if (beanFactory.containsBean(CONVERSION_SERVICE_BEAN_NAME) &&
  			beanFactory.isTypeMatch(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class)) {
  		beanFactory.setConversionService(
  				beanFactory.getBean(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class));
  	}
  
  	// Register a default embedded value resolver if no bean post-processor
  	// (such as a PropertyPlaceholderConfigurer bean) registered any before:
  	// at this point, primarily for resolution in annotation attribute values.
  	if (!beanFactory.hasEmbeddedValueResolver()) {
  		beanFactory.addEmbeddedValueResolver(strVal -> getEnvironment().resolvePlaceholders(strVal));
  	}
  
  	// Initialize LoadTimeWeaverAware beans early to allow for registering their transformers early.
  	String[] weaverAwareNames = beanFactory.getBeanNamesForType(LoadTimeWeaverAware.class, false, false);
  	for (String weaverAwareName : weaverAwareNames) {
  		getBean(weaverAwareName);
  	}
  
  	// Stop using the temporary ClassLoader for type matching.
  	beanFactory.setTempClassLoader(null);
  
  	// Allow for caching all bean definition metadata, not expecting further changes.
  	beanFactory.freezeConfiguration();
  
  	// Instantiate all remaining (non-lazy-init) singletons.
  	beanFactory.preInstantiateSingletons();
  }
  ```

  

##### `finishRefresh()`

  简要说明：最后一步，发布相关事件

  执行逻辑：

  ```java
  清除context级别的资源缓存
  初始化context的生命周期处理器lifecycleProcessor
  通知生命周期处理器refresh事件
  发布最终的事件
  如果可以，参与活动的Beans视图LiveBeansView
  ```

  源码：

  ```java
  /**
   * Finish the refresh of this context, invoking the LifecycleProcessor's
   * onRefresh() method and publishing the
   * {@link org.springframework.context.event.ContextRefreshedEvent}.
   */
  protected void finishRefresh() {
      // Clear context-level resource caches (such as ASM metadata from scanning).
      clearResourceCaches();
  
      // Initialize lifecycle processor for this context.
      initLifecycleProcessor();
  
      // Propagate refresh to lifecycle processor first.
      getLifecycleProcessor().onRefresh();
  
      // Publish the final event.
      publishEvent(new ContextRefreshedEvent(this));
  
      // Participate in LiveBeansView MBean, if active.
      LiveBeansView.registerApplicationContext(this);
  }
  ```

  

##### `destroyBeans()`

  简要说明：摧毁bean

  执行逻辑：

  ```java
  清除已经创建的单例bean
  ```

  源码：

  ```java
  /**
   * Template method for destroying all beans that this context manages.
   * The default implementation destroy all cached singletons in this context,
   * invoking {@code DisposableBean.destroy()} and/or the specified
   * "destroy-method".
   * <p>Can be overridden to add context-specific bean destruction steps
   * right before or right after standard singleton destruction,
   * while the context's BeanFactory is still active.
   * @see #getBeanFactory()
   * @see org.springframework.beans.factory.config.ConfigurableBeanFactory#destroySingletons()
   */
  protected void destroyBeans() {
      getBeanFactory().destroySingletons();
  }
  ```

  

##### `cancelRefresh(ex)`

  简要说明：取消刷新

  执行逻辑：

  ```java
  容器激活标识active设置为false
  ```

  源码：

  ```java
  /**
   * Cancel this context's refresh attempt, resetting the {@code active} flag
   * after an exception got thrown.
   * @param ex the exception that led to the cancellation
   */
  protected void cancelRefresh(BeansException ex) {
  	this.active.set(false);
  }
  ```

  

##### `resetCommonCaches()`

  简要说明：重置通用缓存信息

  执行逻辑：

  ```java
  清除反射工具类缓存
  清除注解工具类缓存
  清除类型解析相关缓存
  清除缓存的自省结果
  ```

  源码：

  ```java
  /**
   * Reset Spring's common reflection metadata caches, in particular the
   * {@link ReflectionUtils}, {@link AnnotationUtils}, {@link ResolvableType}
   * and {@link CachedIntrospectionResults} caches.
   * @since 4.2
   * @see ReflectionUtils#clearCache()
   * @see AnnotationUtils#clearCache()
   * @see ResolvableType#clearCache()
   * @see CachedIntrospectionResults#clearClassLoader(ClassLoader)
   */
  protected void resetCommonCaches() {
  	ReflectionUtils.clearCache();
  	AnnotationUtils.clearCache();
  	ResolvableType.clearCache();
  	CachedIntrospectionResults.clearClassLoader(getClassLoader());
  }
  ```

  
