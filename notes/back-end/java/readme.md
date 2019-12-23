# Java常用工具集锦

## bean map相互转换

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

- java

  ```java
  @Test
  public void convert_object_to_map_java() throws IntrospectionException,
  IllegalAccessException, IllegalArgumentException,
  InvocationTargetException {
  
      NoteBook actionMethodNoteBook = new NoteBook(100, "Action Method Notebook");
  
      Map<String, Object> objectAsMap = new HashMap<>();
      BeanInfo info = Introspector.getBeanInfo(actionMethodNoteBook.getClass());
      for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
          Method reader = pd.getReadMethod();
          if (reader != null)
              objectAsMap.put(pd.getName(),reader.invoke(actionMethodNoteBook));
      }
  
      assertThat(objectAsMap, hasEntry("numberOfSheets", new Double(100.0)));
      assertThat(objectAsMap, hasEntry("description", "Action Method Notebook"));
  }
  ```

- jackson

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
      demo = objectmapper.convertValue(map, objectmapper.getTypeFactory().constructType(Demo.class));
      demo = objectmapper.convertValue(map, new TypeReference<Demo>(){});
      // bean -> map
      map = objectmapper.convertValue(demo, map.getClass());
      map = objectmapper.convertValue(demo, objectmapper.getTypeFactory().constructType(Map.class));
      // Note that it is better to use TypeReference instead of a direct class in this case as the second argument to convertValue().
      // This way we have a checked assignment to correctly typed map instead of Map.
      map = objectmapper.convertValue(map, new TypeReference<Map<String, Object>>(){});
  }
  ```

- apache.common

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
  InvocationTargetException, NoSuchMethodException {
  
  NoteBook fieldNoteBook = new NoteBook(878, "Field Notebook");
  
  @SuppressWarnings("unchecked")
  Map<String, String> map = BeanUtils.describe(fieldNoteBook);
  BeanUtils.populate(fieldNoteBook, map);
  
  assertThat(map, hasEntry("numberOfSheets", (Object) "878.0"));
  assertThat(map, hasEntry("description", (Object)  "Field Notebook"));
  }
  ```

- 

