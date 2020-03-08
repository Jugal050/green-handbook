#  Spring代理机制

##### 说明：

		Spring AOP使用JDK动态代理或者CGLIB代理来代理目标对象。Spring AOP 使用JDK动态代理或者CGLIB来为指定的目标对象创建代理，JDK动态代理在JDK中，CGLIB是开源的类定义库（已被重新打包在spring-core中）。
	
		如果代理对象实现了接口，则使用JDK动态代理，所有被代理对象实现的接口都会被代理。如果目标对象没有实现任何接口，则使用CGLIB创建代理。
	
		CGLIB的缺点：
	
			final修饰的方法不能被增强，因为final方法不能被运行期生成的子类重写。
	
			Spring4.0以后，代理对象的构造器不再调用两次，因为CGLIB代理实例是通过Objenesis[实例化特殊class对象的小型类库]创建的。只有当JVM不允许越过构造器时，Spring AOP可能会调用两次
	
		强制使用CGLIB代理：
	
			<aop:config proxy-target-class="true">
			    <!-- other beans defined here... -->
			</aop:config>
	
		当使用@AspectJ自动代理时强制CGLIB代理：
	
			<aop:aspectj-autoproxy proxy-target-class="true"/>
##### AOP代理机制：

Spring AOP是基于代理的。

- 对象直接调用时：

  代码：

  ```java
  public class SimplePojo implements Pojo {
  
      public void foo() {
          // this next method invocation is a direct call on the 'this' reference
          this.bar();
      }
  
      public void bar() {
          // some logic...
      }
  }	
  ```

  ```java
  public class Main {
  
      public static void main(String[] args) {
          Pojo pojo = new SimplePojo();
          // this is a direct method call on the 'pojo' reference
          pojo.foo();
      }
  }
  ```

  图例：

  ![](D:\workspace\green-handbook\notes\back-end\java\framework\spring\Core\aop-proxy-plain-pojo-call.png)

  

- 对象代理调用时：

  代码：

  ```java
  public class Main {
  
      public static void main(String[] args) {
          ProxyFactory factory = new ProxyFactory(new SimplePojo());
          factory.addInterface(Pojo.class);
          factory.addAdvice(new RetryAdvice());
  
          Pojo pojo = (Pojo) factory.getProxy();
          // this is a method call on the proxy!
          pojo.foo();
      }
  }
  ```

  图例：

  ![](D:\workspace\green-handbook\notes\back-end\java\framework\spring\Core\aop-proxy-call.png)

  **关键点：**

  ​		Main类中有一个Proxy代理引用，Pojo对象上的方法调用会发生在代理商。这样，代理就可以添加相关的方法拦截/增强通知。
  ​		但是，一旦调用到达目标对象SimplePojo，再进行内部方法上的调用，比如，this.foo()或者this.bar(), 都会发生再目标对象this上，而不是代理Proxy。
  ​		也就是，内部方法调用将不再会被代理方法拦截/增强通知。	

  ​		**所以，使用AOP代理，最好就是不进行内部调用，这样也可以实现代码侵入最小化。**

  **tips：**

  ​		AspectJ不会有自身调用的问题，因为它不基于代理。（而Spring AOP是基于代理的）

##### 代理统一入口：

```java
// 新建代理工厂 
org.springframework.aop.framework.ProxyFactory#ProxyFactory()

    // 生成代理 
    org.springframework.aop.framework.ProxyFactory#getProxy()

    // 创建AOP代理
    org.springframework.aop.framework.ProxyCreatorSupport#createAopProxy

    // 获取AOP代理工厂，默认为：DefaultAopProxyFactory
    org.springframework.aop.framework.ProxyCreatorSupport#getAopProxyFactory

    // 创建AOP代理
    org.springframework.aop.framework.DefaultAopProxyFactory#createAopProxy

    // 需要优化(optimize)|需要代理目标类(proxyTargetClass)|无接口(hasNoUserSuppliedProxyInterfaces) 使用CGLIB代理
    org.springframework.aop.framework.ObjenesisCglibAopProxy#ObjenesisCglibAopProxy

    // 否则，使用JDK动态代理
    org.springframework.aop.framework.JdkDynamicAopProxy#JdkDynamicAopProxy
```

##### CGLIB代理原理：

```java
// 生成CGLIB代理	 // done 2020-3-7 19:50:25
org.springframework.aop.framework.CglibAopProxy#getProxy()

	// 获取通知的目标类
	org.springframework.aop.framework.AdvisedSupport#getTargetClass
	
	// 如果目标类是CGLIB代理类，则将其基类的接口也添加到通知接口中
	org.springframework.aop.framework.AdvisedSupport#addInterface

	// 校验类型信息
	org.springframework.aop.framework.CglibAopProxy#validateClassIfNecessary

	// 创建增强器
	org.springframework.aop.framework.CglibAopProxy#createEnhancer

	/* ----------- 设置增强器属性 开始----------- */

    // 如果类加载器不为空，设置类加载器
    org.springframework.cglib.core.AbstractClassGenerator#setClassLoader	

    // 如果类加载器是智能类加载器，并且其中该代理类基类可重复加载，设置用户缓存为false
    org.springframework.cglib.core.AbstractClassGenerator#setUseCache

    // 设置基类
    org.springframework.cglib.proxy.Enhancer#setSuperclass

    // 设置接口
    org.springframework.cglib.proxy.Enhancer#setInterfaces

    // 设置命名规则，默认SpringNamingPolicy（标签为BySpringCGLIB）
    org.springframework.cglib.core.AbstractClassGenerator#setNamingPolicy

    // 设置生成器生成字节码的使用策略，默认ClassLoaderAwareGeneratorStrategy
    org.springframework.cglib.core.AbstractClassGenerator#setStrategy

    // 设置回调过滤器，默认ProxyCallbackFilter
    org.springframework.cglib.proxy.Enhancer#setCallbackFilter

    // 设置回调类型
    org.springframework.cglib.proxy.Enhancer#setCallbackTypes
    
    /* ----------- 设置增强器属性 结束----------- */

	// 创建代理类和代理实例
	org.springframework.aop.framework.CglibAopProxy#createProxyClassAndInstance

		// 设置增强器，构造期是否拦截为false
		org.springframework.cglib.proxy.Enhancer#setInterceptDuringConstruction

		// 设置增强器的回调实现类
		org.springframework.cglib.proxy.Enhancer#setCallbacks

		// 使用基类的构造器生成新类，使用指定的回调生成新对象实例
		org.springframework.cglib.proxy.Enhancer#create() // 无参构造
		org.springframework.cglib.proxy.Enhancer#create(java.lang.Class[], java.lang.Object[]) 	// 有参构造

			// 创建辅助类
			org.springframework.cglib.proxy.Enhancer#createHelper

				// 信息校验: 包括回调类型、过滤器
				org.springframework.cglib.proxy.Enhancer#preValidate

				// 创建多值Key，通过EnhancerKey接口生成的KeyFactory
				org.springframework.cglib.proxy.Enhancer.EnhancerKey#newInstance

				// 反射生成类实例
				org.springframework.cglib.core.AbstractClassGenerator#create

					// 获取类加载器
					org.springframework.cglib.core.AbstractClassGenerator#getClassLoader

					// 查看缓存是否有该类加载器相关的缓存，如果没有，添加相关缓存(key: 类加载器， map: 类加载器信息)
					Map<ClassLoader, ClassLoaderData> newCache = new WeakHashMap<ClassLoader, ClassLoaderData>(cache);

					// 从类加载器信息中获取类生成器

						// 如果使用缓存userCache，则从缓存中获取
						org.springframework.cglib.core.AbstractClassGenerator#unwrapCachedValue(generatedClasses.get(gen));

						// 如果不使用缓存，则使用增强器生成
						org.springframework.cglib.proxy.Enhancer#generate

							// 校验信息，包括：类仅有、回调等属性
							org.springframework.cglib.proxy.Enhancer#validate

							// 根据基类、接口设置名称前缀
							org.springframework.cglib.core.AbstractClassGenerator#setNamePrefix

							// 生成类
							org.springframework.cglib.core.AbstractClassGenerator#generate

								// 保存并设置线程本地变量
								java.lang.ThreadLocal#set 

								// 生成类名
								org.springframework.cglib.core.AbstractClassGenerator#generateClassName

								// 缓存类名
								org.springframework.cglib.core.AbstractClassGenerator.ClassLoaderData#reserveName

								// 设置类名
								org.springframework.cglib.core.AbstractClassGenerator#setClassName

								// 如果尝试加载参数attemptLoad为true，直接使用类加载器加载该类并返回
								java.lang.ClassLoader#loadClass(java.lang.String)

								// 否则，根据生成策略生成字节数组，默认生成策略：DefaultGeneratorStrategy
								org.springframework.cglib.core.DefaultGeneratorStrategy#generate

									// 获取类访问器
									org.springframework.cglib.core.DefaultGeneratorStrategy#getClassVisitor

										// 构造可调试的类写入器，ASM API版本使用AsmApi.value()，类访问器使用ClassWriter
										org.springframework.cglib.core.DebuggingClassWriter#DebuggingClassWriter

										// 生成类文件
										org.springframework.cglib.proxy.Enhancer#generateClass

											// 根据JVMS，通过ClassEmitter、CodeEmitter来生成类文件头信息、字段信息、构造器、方法、尾信息等等class信息

								// 定义class，参数包括： 类名、字节数组、类加载器、受保护的域对象、	上下文类
								org.springframework.cglib.core.ReflectUtils#defineClass

									// 根据JDK版本，使用响应的方法加载类Class对象

										// 首选操作: JDK 9+，如果类加载器匹配，使用Lookup.defineClass API
										// 经典操作: 使用方法 protected ClassLoader.defineClass
										// 回退操作: JDK 9+，即时类加载器不匹配，也使用Lookup.defineClass API

										// 强制静态域初始化

								// 到此结束！！！！！！！！	
```



##### JDK动态代理原理：

```java
// 生成JDK动态代理 		// done 2020-3-8 12:11:36
org.springframework.aop.framework.JdkDynamicAopProxy#getProxy()

	// 从AOP配置中提取要代理的所有接口，主要包括目标本身接口，以及几个代理通用接口：SpringProxy、Advised、DecoratingProxy
	org.springframework.aop.framework.AopProxyUtils#completeProxiedInterfaces

	// 查找equals和hashCode方法
	org.springframework.aop.framework.JdkDynamicAopProxy#findDefinedEqualsAndHashCodeMethods

	// 为指定接口生成代理类实例，该实例可以将方法调用转发给指定的调用处理器（Invocation Handler）
	java.lang.reflect.Proxy#newProxyInstance

		// 查找或生成指定的代理类
		java.lang.reflect.Proxy#getProxyClass0

			// 如果该加载器中给定的接口代理实现已存在，直接复制并返回缓存信息。否则，通过ProxyClassFactory创建代理类
			java.lang.reflect.WeakCache#get

				// 根据类加载器和接口数组生成、定义、并返回代理类
				java.lang.reflect.Proxy.ProxyClassFactory#apply

					// 校验信息

					// 生成代理类名

					// 生成指定代理类
					sun.misc.ProxyGenerator#generateProxyClass

						// 生成类文件，包括代理方法、构造器、静态方法，最后返回字节数组
						sun.misc.ProxyGenerator#generateClassFile

						// 如果需要保存生成文件，在文件夹下生成class文件
						java.nio.file.Files#write

					// 定义指定代理类
					java.lang.reflect.Proxy#defineClass0[native]

		// 使用指定的调用处理器调用构造器
		java.lang.reflect.Constructor#newInstance

			// 后续调用JNI本地接口，c++/c/jvm等不是很懂，所以到此为止
			jdk\src\share\native\java\lang\reflect\Proxy.c
			jdk\src\share\javavm\export\jni.h
```

##### 相关类：

- org.springframework.aop.framework.AopProxyFactory 

  ```java
  /**
   * 基于AdvisedSupport配置对象，来创建AOP代理的工厂接口
   *
   * 代理应该遵循以下规则：
   *    实现所有配置信息中需要被代理的接口
   *    实现Advised接口
   *    实现equals方法（为了比较被代理的接口，通知，目标对象）
   *    如果所有的通知和目标对象可序列化，那么代理也应该可以序列化
   *    如果所有的通知和目标对象线程安全，那么代理也应该线程安全 
   *
   * 代理可以允许，也可以不允许通知修改。 如果不允许（比如配置信息被冻结），当试图修改通知时，代理应该抛出异常。
   */
  public interface AopProxyFactory {
      AopProxy createAopProxy(AdvisedSupport config) throws AopConfigException;
  }
  ```
  
- org.springframework.aop.framework.DefaultAopProxyFactory

  ```java
  /**
   * AopProxyFactory接口的默认实现，创建一个CGLIB代理或者JDK动态代理。
   * 
   * 如果指定的AdvisedSupport满足以下任一条件，创建CGLIB代理：
   *    设置了优化标识optimize
   *    设置了代理目标类标识proxyTargetClass
   *    没有指定代理接口
   *
   * 通常，通过设置proxyTargetClass来强制使用CGLIB代理，或者通过指定接口来使用JDK动态代理。
   */
  public class DefaultAopProxyFactory implements AopProxyFactory, Serializable {
  
      @Override
      public AopProxy createAopProxy(AdvisedSupport config) throws AopConfigException {
          if (config.isOptimize() || config.isProxyTargetClass() || hasNoUserSuppliedProxyInterfaces(config)) {
              Class<?> targetClass = config.getTargetClass();
              if (targetClass == null) {
                  throw new AopConfigException("TargetSource cannot determine target class: " +
                                               "Either an interface or a target is required for proxy creation.");
              }
              if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
                  return new JdkDynamicAopProxy(config);
              }
              return new ObjenesisCglibAopProxy(config);
          }
          else {
              return new JdkDynamicAopProxy(config);
          }
      }
  
      /**
  		 * Determine whether the supplied {@link AdvisedSupport} has only the
  		 * {@link org.springframework.aop.SpringProxy} interface specified
  		 * (or no proxy interfaces specified at all).
  		 */
      private boolean hasNoUserSuppliedProxyInterfaces(AdvisedSupport config) {
          Class<?>[] ifcs = config.getProxiedInterfaces();
          return (ifcs.length == 0 || (ifcs.length == 1 && SpringProxy.class.isAssignableFrom(ifcs[0])));
      }
  
  }
  ```
  
- org.springframework.aop.framework.AopProxy	

  ```java
  /**
   * 已配置的AOP代理的委托接口，可以创建实际的代理对象
   *	
   * DefaultAopProxyFactory提供了JDK动态代理和CGLIB代理开箱即用的实现类
   */ 	
  public interface AopProxy {
  
      // 使用默认的AopProxy类加载器（通常为当前线程的类加载器）来创建一个新的代理对象
      Object getProxy();
  
      // 使用指定的类加载器来创建一个新的代理对象，参数为null时，会下传参数导致低级的代理默认配置，与getPrexy()不同。
      Object getProxy(@Nullable ClassLoader classLoader);
  }
  ```
  
- org.springframework.aop.framework.CglibAopProxy

  ```java
  /**
   * Spring AOP框架中，AopProxy基于CGLIB的实现。
   * 
   * 需要根据AdvisedSupport对象的配置，通过代理工厂获取。 
   * 该类是Spring AOP框架中内置类，无需客户端直接编码调用。
   *
   * 如果需要（比如有代理目标类），DefaultAopProxyFactory会自动创建基于CGLIB的代理。
   *
   * 如果底层的目标类是线程安全的，使用此类创建的代理对象也是现成安全的。
   */ 
  class CglibAopProxy implements AopProxy, Serializable {
  
  
      @Override
      public Object getProxy() {
          return getProxy(null);
      }
  
      @Override
      public Object getProxy(@Nullable ClassLoader classLoader) {
          if (logger.isTraceEnabled()) {
              logger.trace("Creating CGLIB proxy: " + this.advised.getTargetSource());
          }
  
          try {
              Class<?> rootClass = this.advised.getTargetClass();
              Assert.state(rootClass != null, "Target class must be available for creating a CGLIB proxy");
  
              Class<?> proxySuperClass = rootClass;
              if (rootClass.getName().contains(ClassUtils.CGLIB_CLASS_SEPARATOR)) {
                  proxySuperClass = rootClass.getSuperclass();
                  Class<?>[] additionalInterfaces = rootClass.getInterfaces();
                  for (Class<?> additionalInterface : additionalInterfaces) {
                      this.advised.addInterface(additionalInterface);
                  }
              }
  
              // Validate the class, writing log messages as necessary.
              validateClassIfNecessary(proxySuperClass, classLoader);
  
              // Configure CGLIB Enhancer...
              Enhancer enhancer = createEnhancer();
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
              return createProxyClassAndInstance(enhancer, callbacks);
          }
          catch (CodeGenerationException | IllegalArgumentException ex) {
              throw new AopConfigException("Could not generate CGLIB subclass of " + this.advised.getTargetClass() +
                                           ": Common causes of this problem include using a final class or a non-visible class",
                                           ex);
          }
          catch (Throwable ex) {
              // TargetSource.getTarget() failed
              throw new AopConfigException("Unexpected AOP exception", ex);
          }
      }
  
  }
  ```
  
- org.springframework.aop.framework.ObjenesisCglibAopProxy

  ```java
  /**
   * 基于Objenesis的CglibAopProxy的子类，无需通过调用类构造器来创建代理实例。 Spring4之后默认使用。
   */ 	
  	
  class ObjenesisCglibAopProxy extends CglibAopProxy {	
  
      private static final SpringObjenesis objenesis = new SpringObjenesis();
  
      public ObjenesisCglibAopProxy(AdvisedSupport config) {
          super(config);
      }
  
  
      @Override
      protected Object createProxyClassAndInstance(Enhancer enhancer, Callback[] callbacks) {
          Class<?> proxyClass = enhancer.createClass();
          Object proxyInstance = null;
  
          if (objenesis.isWorthTrying()) {
              try {
                  proxyInstance = objenesis.newInstance(proxyClass, enhancer.getUseCache());
              }
              catch (Throwable ex) {
                  logger.debug("Unable to instantiate proxy using Objenesis, " +
                               "falling back to regular proxy construction", ex);
              }
          }
  
          if (proxyInstance == null) {
              // Regular instantiation via default constructor...
              try {
                  Constructor<?> ctor = (this.constructorArgs != null ?
                                         proxyClass.getDeclaredConstructor(this.constructorArgTypes) :
                                         proxyClass.getDeclaredConstructor());
                  ReflectionUtils.makeAccessible(ctor);
                  proxyInstance = (this.constructorArgs != null ?
                                   ctor.newInstance(this.constructorArgs) : ctor.newInstance());
              }
              catch (Throwable ex) {
                  throw new AopConfigException("Unable to instantiate proxy using Objenesis, " +
                                               "and regular proxy instantiation via default constructor fails as well", ex);
              }
          }
  
          ((Factory) proxyInstance).setCallbacks(callbacks);
          return proxyInstance;
      }
  
  }
  
  }
  ```

- org.springframework.cglib.core.AbstractClassGenerator

  ```java
  /**
   * 所有代码生成的CGLIB组件的抽象类。
   * 除了为了提高性能缓存了生成的类之外，
   * 还在生成具体类之前提供了许多入口，包括：自定义类加载器、类名生成规则、类转化等
   */
  abstract public class AbstractClassGenerator<T> implements ClassGenerator {}
  ```

- org.springframework.cglib.proxy.Enhancer

  ```java
  /**
   * 字节码增强器
   * 生成可以方法拦截的动态子类，JDK1.3加入此类是为了代替标准的动态代理支持。
   * 除了实现接口，还可以继承一个新的基类。
   * 动态生成的子类会重写父类中的非final方法，并添加用户定义的拦截器实现回调。
   *
   *
   */
  public class Enhancer extends AbstractClassGenerator {}
  ```

- org.springframework.cglib.core.KeyFactory

  ```java
  /**
   * 生成处理多值key的类，以便在map、set中使用。 equals和hashCode方法遵循《Effective Java》中的规则
   *
   * 生成一个KeyFactory，需要提供一个描述key结构的接口，接口需要包含一个名为newInstance、返回值为Object、任意参数的方法。如：
   *
   *      private interface IntStringKey {
   *          public Object newInstance(int i, String s);
   *      }
   * 
   * 当有一个KeyFactory，就可以调用接口中定义的newInstance方法生成一个新key值。
   *
   *      IntStringKey factory = (IntStringKey)KeyFactory.create(IntStringKey.class);
   *      Object key1 = factory.newInstance(4, "Hello");
   *      Object key2 = factory.newInstance(4, "World");
   * 
   * notes: 只有当key1、key2都是同一个工厂生成、并且key1.equals(key2)，key1和key2的hashCode才相等。
   */
  abstract public class KeyFactory {
  
      public static KeyFactory create(Class keyInterface) {
          return create(keyInterface, null);
      }
  
      public static KeyFactory create(Class keyInterface, Customizer customizer) {
          return create(keyInterface.getClassLoader(), keyInterface, customizer);
      }
  
      public static KeyFactory create(Class keyInterface, KeyFactoryCustomizer first, List<KeyFactoryCustomizer> next) {
          return create(keyInterface.getClassLoader(), keyInterface, first, next);
      }
  
      public static KeyFactory create(ClassLoader loader, Class keyInterface, Customizer customizer) {
          return create(loader, keyInterface, customizer, Collections.<KeyFactoryCustomizer>emptyList());
      }
  
      public static KeyFactory create(ClassLoader loader, Class keyInterface, KeyFactoryCustomizer customizer,
                                      List<KeyFactoryCustomizer> next) {
          Generator gen = new Generator();
          gen.setInterface(keyInterface);
          // SPRING PATCH BEGIN
          gen.setContextClass(keyInterface);
          // SPRING PATCH END
  
          if (customizer != null) {
              gen.addCustomizer(customizer);
          }
          if (next != null && !next.isEmpty()) {
              for (KeyFactoryCustomizer keyFactoryCustomizer : next) {
                  gen.addCustomizer(keyFactoryCustomizer);
              }
          }
          gen.setClassLoader(loader);
          return gen.create();
      }
  
  
      public static class Generator extends AbstractClassGenerator {
  
      }	
  
  }
  ```
  
- org.springframework.cglib.core.ClassGenerator

  ```java
  /**
   * 类生成器接口
   */
  public interface ClassGenerator {
      void generateClass(ClassVisitor var1) throws Exception;
  }
  ```
  
- org.springframework.cglib.core.AbstractClassGenerator

  ```java
  /**
   * 所有代码生成的CGLIB组件的抽象类
   *
   * 为了提高性能，除了缓存生成的类文件外，生成代码前还提供了：自定义的类加载器、生成类的名称、转换。
   */
  abstract public class AbstractClassGenerator<T> implements ClassGenerator {}
  ```
  
- org.springframework.asm.ClassVisitor

  ```java
  /**
   * Java类访问器。该类方法调用必须按照如下顺序：
   *   visit [ visitSource ] [ visitModule ][ visitNestHost ][ visitPermittedSubtype ][ visitOuterClass ] 
   *   ( visitAnnotation | visitTypeAnnotation | visitAttribute )* ( visitNestMember | visitInnerClass | visitField | visitMethod )* visitEnd.
   */
  public abstract class ClassVisitor {}
  ```

- org.springframework.asm.ClassWriter

  ```java
  /**
   * 可根据JVMS（JVM规范）生成响应类文件结构的类访问器。
   * 可单独使用生成java class文件，也可以通过类读取器和类访问器修改现有的class文件
   */
  public class ClassWriter extends ClassVisitor {}
  ```

- org.springframework.cglib.core.DebuggingClassWriter

  ```java
  /**
   * 可调式的Class写入器
   */
  public class DebuggingClassWriter extends ClassVisitor {}
  ```

- org.springframework.asm.MethodVisitor
  
  ```java
  /**
   * Java Method访问器。
   */
  public abstract class MethodVisitor {}
  ```
  
- org.springframework.cglib.core.ClassEmitter

  ```java
  /**
   * 类发射器： 生成class类文件相关
   */
  public class ClassEmitter extends ClassTransformer {}
  public abstract class ClassTransformer extends ClassVisitor {}
  ```

- org.springframework.cglib.core.CodeEmitter

  ```java
  /**
   * 代码发射器： 生成class类文件中的代码相关
   */
  public class CodeEmitter extends LocalVariablesSorter {}
  public class LocalVariablesSorter extends MethodVisitor {}
  ```

  



​			











