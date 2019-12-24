## javabean与map之间相互转换

```jav
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

  总结：

  ​        java：

  ​                bean转map还好，map转bean需要进行数据类型判断。

  ​		apache.common.beanutil:

  ​				优点：增加缓存操作，使用次数越多，效率越高。

  ​				缺点：对于非公开的类/读写方法，会导致反射读写异常。

  ​		fasterxml.jackson:

  ​                先使用json序列化/反序列化机制，可使用配置/注解类进行特制转换。

  ​				

  ​				

     