# Objenesis

##### 官网：http://objenesis.org/index.html

##### 源码：https://github.com/easymock/objenesis

##### 说明： 

​		java小型类库，只为一个目的，就是特殊类的实例化。

##### 由来：

​		java本身已经支持使用Class.newInstance()来动态实例化，然后，只有当类有合适的构造器时才会起作用，如果一个类包含以下几种情况，就很难通过该方式进行实例化：

   - 构造器带参数

   - 构造器有副作用

   - 构造器会抛异常

​		所以，在很多地方，我们可以看见类似"该类必须有默认构造器"的限制，Objenesis的宗旨就是实例化对象时不受这些限制。

##### 使用：

  - 添加jar包

    ```xml
    <!-- https://mvnrepository.com/artifact/org.objenesis/objenesis -->
    <dependency>
        <groupId>org.objenesis</groupId>
        <artifactId>objenesis</artifactId>
        <version>3.1</version>
        <scope>test</scope>
    </dependency>
    ```

  - 主要接口

    ObjectInstantiator: 单个类多次实例化

    ```java
    public interface ObjectInstantiator<T> {
      T newInstance();
    }
    ```

    InstantiatorStrategy: 实例化策略（类型不同策略不同）

    ```java
    interface InstantiatorStrategy {
      <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type);
    }
    ```

    Objenesis：所有Objenesis对象的通用接口

    ```java
    public interface Objenesis {
        <T> T newInstance(Class<T> clazz);
        <T> ObjectInstantiator<T> getInstantiatorOf(Class<T> clazz);
    }    
    ```

  - 示例

    ```java
    Objenesis objenesis = new ObjenesisStd(); // or ObjenesisSerializer
    MyThingy thingy1 = (MyThingy) objenesis.newInstance(MyThingy.class);
    
    // or (a little bit more efficient if you need to create many objects)
    
    Objenesis objenesis = new ObjenesisStd(); // or ObjenesisSerializer
    ObjectInstantiator thingyInstantiator = objenesis.getInstantiatorOf(MyThingy.class);
    
    MyThingy thingy2 = (MyThingy)thingyInstantiator.newInstance();
    MyThingy thingy3 = (MyThingy)thingyInstantiator.newInstance();
    MyThingy thingy4 = (MyThingy)thingyInstantiator.newInstance();
    ```

  - 性能以及线程安全性

    ObjenesisBase中默认内置了缓存，缓存保存了每个类的ObjectInstantiator创建的实例化对象（单例）。

    ObjectInstantiator和InstantiatorStrategy都支持多线程，都是线程安全的。

  - 策略

    1. Stardard 不会调用构造器
    2. Serializable compliant 和java标准的实例化对象方式类似，调用第一个非序列化父类的构造器。

    tips：

    ​		程序会自动判断最佳策略

    ​		如果Objenesis环境不支持，或者想自定义指定JVM下的实例化，可以自定义策略，示例：

    ```java
    // 创建
    public class Sun14Strategy implements InstantiatorStrategy {
        public ObjectInstantiator newInstantiatorOf(Class type) {
            // return sun dedicated instantiator
            return new SunReflectionFactoryInstantiator(type);
        }
    }
    
    // 使用
    // Directly
    Objenesis o = new ObjenesisBase(new Sun14Strategy());
    
    // Or inside your Objenesis own implementation
    public class ObjenesisSun14 extends ObjenesisBase {
        public ObjenesisSun14() {
           super(new Sun14Strategy());
        }
    }
    ```
    
  - 其他内置类
    
	- ObjenesisHelper: 内置了[ObjenesisStd](http://objenesis.org/apidocs/org/objenesis/ObjenesisStd.html) 和[ObjenesisSerializer](http://objenesis.org/apidocs/org/objenesis/ObjenesisSerializer.html) ，使用示例：
    
      ```java
      Object o1 = ObjenesisHelper.newInstance(MyClass.class);
      Object o2 = ObjenesisHelper.newSerializableInstance(MyClass.class);
      ObjectInstantiator o3 = ObjenesisHelper.getInstantiatorOf(MyClass.class);
      ObjectInstantiator o4 = ObjenesisHelper.getSerializableObjectInstantiatorOf(MyClass.class);
      ```
    
  - 异常处理
    

如果使用Objenesis过程中发生异常，会得到一个ObjenesisException包装的运行时异常，无需捕获。
    
    发生该异常的原因有：
    
    		- 使用ObjenesisSerializer实例化的类，不能序列化。
    		- 使用ObjenesisSerializer实例化的类，第一个父类的构造器不能序列化。
    		- 环境SecurityManager影响Objnenesis的正常运行。
    		- 其他原因。


​    
​    



​			

