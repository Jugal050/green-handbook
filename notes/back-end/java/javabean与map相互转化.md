## javabean与map之间相互转换

- java： 使用java自带的反射机制进行转换

  ```java
  @Test
  public void convert_object_to_map_java() throws Exception {
      NoteBook actionMethodNoteBook = new NoteBook(100, "Action Method Notebook");
      
      // bean -> map
      Map<String, Object> objectAsMap = new HashMap<>();
      BeanInfo info = Introspector.getBeanInfo(actionMethodNoteBook.getClass());
      for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
          Method reader = pd.getReadMethod();
          if (reader != null)
              objectAsMap.put(pd.getName(),reader.invoke(actionMethodNoteBook));
      }
      
      // map -> bean
      NoteBook book = new NoteBook();
      Map<String, Object> map = Maps.newHashMap();
      map.put("numberOfSheets", "1000");
      map.put("description", "desc");
      for (PropertyDescriptor pd : propertyDescriptors) {
  			Method writer = pd.getWriteMethod();
  			if (writer != null) {
  				String name = pd.getName();
  				if (map.containsKey(name)) {
                      // 此处需要进行参数类型判断并转换。所以，最好是全部String类型较为方便。
  					writer.invoke(book, map.get(name)); 
  				}
  			}
  		}
  }
  ```

- jackson： 使用json序列化和反序列化进行

  ```xml
  <dependency>
  	<groupId>com.fasterxml.jackson.core</groupId>
  	<artifactId>jackson-databind</artifactId>
  	<version>2.6.3</version>
  </dependency>
  ```

  ```java
  @Test
  public void convert_object_to_map_jackson() {
      ObjectMapper objectmapper = new ObjectMapper();
      Map<String, Object> map = Maps.newHashMap();
      map.put("id", "id");
      map.put("name", "name");
      Demo demo = null;
      
      // map -> bean
      demo = objectmapper.convertValue(map, Demo.class);
      demo = objectmapper
          .convertValue(map, objectmapper.getTypeFactory().constructType(Demo.class));
      demo = objectmapper.convertValue(map, new TypeReference<Demo>(){});
      
      // bean -> map
      map = objectmapper.convertValue(demo, map.getClass());
      map = objectmapper
          .convertValue(demo, objectmapper.getTypeFactory().constructType(Map.class));
      // Note that it is better to use TypeReference instead of a direct class in this case as the second argument to convertValue().
      // This way we have a checked assignment to correctly typed map instead of Map.
      map = objectmapper.convertValue(map, new TypeReference<Map<String, Object>>(){});
  }
  ```

- apache.common：使用java反射机制，增加缓存信息。

  ```xml
  <dependency>
      <groupId>commons-beanutils</groupId>
      <artifactId>commons-beanutils</artifactId>
      <version>1.9.4</version>
  </dependency>
  ```

  ```java
  @Test
  public void convert_object_to_map_apache_commons () throws IllegalAccessException,
  InvocationTargetException, NoSuchMethodException, InstantiationException {
  
      NoteBook fieldNoteBook = new NoteBook(878, "Field Notebook");
  
      // bean -> map
      // 第一种：仅限于Map<String, String>
      @SuppressWarnings("unchecked")
      Map<String, String> map = BeanUtils.describe(fieldNoteBook);
  
      // 同级非公开类存在问题，生成map为空（原因：非public类读写方法不开放，导致反射异常）
      Demo demo = new Demo();
      demo.setId("id");
      demo.setName("name");
      Map<String, String> demoMap = BeanUtils.describe(demo);
  
      // 第二种：
      Map beanMap = new BeanMap(fieldNoteBook);
  
      // map -> bean 必须先新建实体类实例
      map.put("numberOfSheets", "123");
      map.put("description", "desc");
      BeanUtils.populate(fieldNoteBook, map);
  
  }
  ```
  
- fastjson：使用序列化和反序列化实现
  
```xml
  <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>fastjson</artifactId>
      <version>1.2.62</version>
  </dependency>
  ```
  
  ```java
  @Test
  public void convert_object_to_map_fastjson() {
  
      // map -> bean
      Map<String, Object> map = Maps.newHashMap();
      map.put("numberOfSheets", "123");
      map.put("description", "desc");
      JSONObject jsonObject = new JSONObject(map);
      NoteBook notebook = jsonObject.toJavaObject(NoteBook.class);
  
      // bean -> map
      map = null;
      String json = JSON.toJSONString(notebook);
      map = JSON.parseObject(json, Map.class);
  
  }
  ```

测试过程中使用到的demo:

```java
@Data
class Demo {
	private String id;
	private String name;
}

class NoteBook {

	private double numberOfSheets;
	private String description;

	public NoteBook(double numberOfSheets, String description) {
		super();
		this.numberOfSheets = numberOfSheets;
		this.description = description;
	}

	public double getNumberOfSheets() {
		return numberOfSheets;
	}
	public String getDescription() {
		return description;
	}

}
```


  #### 性能对比

| 执行次数(次-毫秒) | apache-common-beanutil | jackson | fastjson |
| ----------------- | ---------------------- | ------- | -------- |
| 1                 | 116                    | 188     | 92       |
| 10                | 3                      | 10      | 1        |
| 100               | 20                     | 72      | 4        |
| 1000              | 72                     | 297     | 33       |
| 10000             | 414                    | 1099    | 53       |
| 100000            | 4605                   | 3922    | 142      |
| 1000000           | 927366                 | 25942   | 782      |
| 10000000          | ???                    | 255503  | 7226     |
| 100000000         | ???                    | ???     | ???      |

  #### 总结：

- java	 

  ​	实现机制：java内省和反射

  ​	优点：java内部类即可实现，无序引用外部类库。

  ​	缺点：转换过程中需要自己对数据类型进行控制，简单的数据类型还好，如果数据类型复杂，实现代码也并不会很简易。

- apache.common.beanutil

  ​	实现机制：使用java反射机制实现

  ​	优点：增加缓存操作，使用次数越多，效率越高（实际测试时，到10W、100W开始飙升，可能和本身jar包中的日志打印有关系，个人猜测日志应该不是主要原因），由于平时使用较少，暂未发现其他优点。

  ​	缺点：（测试中发现对于非公开的类/读写方法，会导致反射读写异常，不确定这是不是所有基于java反射机制实现都会遇到问题），由于平时使用较少，暂未发现其他缺点。

- jackson

  ​	实现机制：使用序列化和反序列化实现

  ​	优点：可配置性很高。（比如可以通过配置实现不同名称之间的转换）
  
  ​	缺点：速度中规中矩，暂未发现其他缺点。

- fastjson 

  ​	实现机制：使用序列化和反序列化实现，解析json主要是用的String类substring，申请内存次数很少，所以解析起来非常“快”

  ​	优点：测试中速度最快的。

  ​	缺点： 代码质量较差，用很多投机取巧的的做法去实现所谓的“快”，而失去了原本应该兼容的java特性，对json标准遵循也不严格。
  