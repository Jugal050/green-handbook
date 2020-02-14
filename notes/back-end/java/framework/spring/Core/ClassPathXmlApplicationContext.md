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
        
        /** Logger used by this class. Available to subclasses. */
    	protected final Log logger = LogFactory.getLog(getClass());
    
        // ------------------------------- 容器配置 开始 ------------------------------
        
    	/** Unique id for this context, if any. */
    	private String id = ObjectUtils.identityToString(this);
    
    	/** Display name. */
    	private String displayName = ObjectUtils.identityToString(this);
    
    	/** Parent context. */
    	@Nullable
    	private ApplicationContext parent;
    
    	/** Environment used by this context. */
    	@Nullable
    	private ConfigurableEnvironment environment;
    
    	/** BeanFactoryPostProcessors to apply on refresh. */
    	private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();
    
    	/** System time in milliseconds when this context started. */
    	private long startupDate;
    
    	/** Flag that indicates whether this context is currently active. */
    	private final AtomicBoolean active = new AtomicBoolean();
    
    	/** Flag that indicates whether this context has been closed already. */
    	private final AtomicBoolean closed = new AtomicBoolean();
    
    	/** Synchronization monitor for the "refresh" and "destroy". */
    	private final Object startupShutdownMonitor = new Object();
    
    	/** Reference to the JVM shutdown hook, if registered. */
    	@Nullable
    	private Thread shutdownHook;
    
    	/** ResourcePatternResolver used by this context. */
    	private ResourcePatternResolver resourcePatternResolver;
    
    	/** LifecycleProcessor for managing the lifecycle of beans within this context. */
    	@Nullable
    	private LifecycleProcessor lifecycleProcessor;
    
    	/** MessageSource we delegate our implementation of this interface to. */
    	@Nullable
    	private MessageSource messageSource;
    
    	/** Helper class used in event publishing. */
    	@Nullable
    	private ApplicationEventMulticaster applicationEventMulticaster;
    
    	/** Statically specified listeners. */
    	private final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<>();
    
    	/** Local listeners registered before refresh. */
    	@Nullable
    	private Set<ApplicationListener<?>> earlyApplicationListeners;
    
    	/** ApplicationEvents published before the multicaster setup. */
    	@Nullable
    	private Set<ApplicationEvent> earlyApplicationEvents;
        
        // ------------------------------- 容器配置 结束 ------------------------------
        
        public AbstractApplicationContext() {
    		this.resourcePatternResolver = getResourcePatternResolver();
    	}
        
        public AbstractApplicationContext(@Nullable ApplicationContext parent) {
    		this();
    		setParent(parent);
    	}
        
        protected ResourcePatternResolver getResourcePatternResolver() {
    		return new PathMatchingResourcePatternResolver(this);
    	}
        
        @Override
    	public void setParent(@Nullable ApplicationContext parent) {
    		this.parent = parent;
    		if (parent != null) {
    			Environment parentEnvironment = parent.getEnvironment();
    			if (parentEnvironment instanceof ConfigurableEnvironment) {
    				getEnvironment().merge(
                        (ConfigurableEnvironment) parentEnvironment);
    			}
    		}
    	}
    }
    ```

    调用父类构造器：

    ```java
    /**
     * 资源加载器ResourceLoader默认的实现类
     */
    public class DefaultResourceLoader implements ResourceLoader {
        
        @Nullable
    	private ClassLoader classLoader;  // 默认：Launcher$AppClassLoader@441
        
        public DefaultResourceLoader() {
    		this.classLoader = ClassUtils.getDefaultClassLoader();
    	}
        
    }
    ```

    调用PathMatchingResourcePatternResolver的构造器：

    ```java
    /**
     * 资源模式解析器ResourcePatternResolver的实现类，
     * 可以将指定的资源位置路径解析为一个或多个匹配的资源。
     */
    public class PathMatchingResourcePatternResolver implements ResourcePatternResolver {
        
        @Nullable
    	private static Method equinoxResolveMethod;
    
    	static {
    		try {
    			// Detect Equinox OSGi (e.g. on WebSphere 6.1)
    			Class<?> fileLocatorClass = ClassUtils.forName("org.eclipse.core.runtime.FileLocator",
    					PathMatchingResourcePatternResolver.class.getClassLoader());
    			equinoxResolveMethod = fileLocatorClass.getMethod("resolve", URL.class);
    			logger.trace("Found Equinox FileLocator for OSGi bundle URL resolution");
    		}
    		catch (Throwable ex) {
    			equinoxResolveMethod = null;
    		}
    	}
        
        public PathMatchingResourcePatternResolver(ResourceLoader resourceLoader) {
    		Assert.notNull(resourceLoader, "ResourceLoader must not be null");
    		this.resourceLoader = resourceLoader;
    	}
    }
    ```

    -- super(parent)结束;

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

    会调用getEnvironment，此处environment为null还未初始化，会继续调用createEnvironment

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

    继续调用StandardEnvironment的构造器

    ```java
    public class StandardEnvironment extends AbstractEnvironment {
    
    
    }
    ```

    StandardEnvironment类图：

    ![](D:\workspace\green-handbook\notes\back-end\java\framework\spring\Core\StandardEnvironment.png)

  





