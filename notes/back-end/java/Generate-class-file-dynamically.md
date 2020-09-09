#generate class file dynamically

## desc

## tech

### hard code

```java
import java.io.File; 
import java.io.FileWriter; 
import java.lang.reflect.Method; 
import java.net.URL; 
import java.net.URLClassLoader; 
import java.util.Arrays; 
 
import javax.tools.JavaCompiler; 
import javax.tools.JavaFileObject; 
import javax.tools.StandardJavaFileManager; 
import javax.tools.StandardLocation; 
import javax.tools.ToolProvider; 
 
public class HelloWorld { 
 
    public static void main(String[] args) throws Exception { 
         
        // create an empty source file 
        File sourceFile = File.createTempFile("Hello", ".java"); 
        sourceFile.deleteOnExit(); 
 
        // generate the source code, using the source filename as the class name 
        String classname = sourceFile.getName().split("\\.")[0]; 
        String sourceCode = "public class " + classname + "{ public void hello() { System.out.print(\"Hello world\");}}"; 
 
        // write the source code into the source file 
        FileWriter writer = new FileWriter(sourceFile); 
        writer.write(sourceCode); 
        writer.close(); 
         
        // compile the source file 
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler(); 
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null); 
        File parentDirectory = sourceFile.getParentFile(); 
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(parentDirectory)); 
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile)); 
        compiler.getTask(null, fileManager, null, null, null, compilationUnits).call(); 
        fileManager.close(); 
         
        // load the compiled class 
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { parentDirectory.toURI().toURL() }); 
        Class<?> helloClass = classLoader.loadClass(classname); 
         
        // call a method on the loaded class 
        Method helloMethod = helloClass.getDeclaredMethod("hello"); 
        helloMethod.invoke(helloClass.newInstance()); 
    } 
} 
```



### java dynamic proxy

#### 说明

​		Java动态代理

####示例

```java
/** 审计拦截类 */
public class Auditor {
  	public void audit(String service, String extraData) {
    	// ... Do the auditing
	}
}

/** 计算器接口类 */
public interface Calculator {
  	int add(int left, int right);
}

/** 计算器接口实现类 */
public class CalculatorImpl implements Calculator {
  	public int add(int left, int right) {
    	return left + right;
  	}
}

/** 硬编码实现拦截效果 */
public class AuditingCalculator implements Calculator {
    private Calculator inner;
    private Auditor auditor;
    public AuditingCalculator(Calculator inner, Auditor auditor) {
        this.calculator = calculator;
        this.auditor = auditor;
    }
    public int add(int left, int right) {
        auditor.audit("calculator", "before add");
        int result = inner.add(left, right);
        auditor.audit("calculator", "after add");
        return result;
    }
}

/** 调用处理器实现类 */
public class AuditingInvocationHandler implements InvocationHandler {
  	private final Auditor auditor;
	private final Object target;
	public AuditingInvocationHandler(Auditor auditor, Object target) {
    	this.auditor = auditor;
    	this.target = target;
  	}
  	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    	auditor.audit(target.getClass().getName(), "before " + method.getName());
    	Object returnObject = method.invoke(target, args);
    	auditor.audit(target.getClass().getName(), "after " + method.getName());
    	return returnObject;
  	}
}

/** 动态代理使用示例 */
public class Demo {
    public static void main(String[] args) {
        Auditor auditor = ...;
		Calculator real = new CalculatorImpl();
		InvocationHandler handler = new AuditingInvocationHandler(auditor, real);
		Calculator proxy = (Calculator) Proxy.newProxyInstance(
  		ClassLoader.getSystemClassLoader(), new Class[] { Calculator.class }, handler);
		real.add(2, 2); // Will not be audited
		proxy.add(2, 2); // Will be audited
    }
}
```

#### 其他

- jdk动态代理的具体局限，可参考java.lang.reflect.Proxy类说明文档

### [cglib](http://cglib.sourceforge.net/)

#### 说明

​	CGLIB代理

#### 示例

```java
/** 审计拦截类 */
public class Auditor {
  	public void audit(String service, String extraData) {
    	// ... Do the auditing
  	}
}

/** 计算器类 */
public class Calculator {
  	public int add(int left, int right) {
    	return left + right;
  	}
}

/** 方法拦截器实现类 */
public class AuditingInterceptor implements MethodInterceptor {
  	private Auditor auditor;
  	private String service;
  	public AuditingInterceptor(Auditor auditor, String service) {
    	this.auditor = auditor;
    	this.service = service;
  	}
  	public Object intercept(Object target, Method method, 
        Object[] args, MethodProxy proxy) throws Throwable {
        auditor.audit(service, "before " + method.getName());
        targetReturn = proxy.invokeSuper(target, args);
        auditor.audit(service, "after " + method.getName());
        return targetReturn;
  	}
}

/** CGLIB代理使用示例 */
public class Demo {
    public static void main(String[] args) {
        Auditor auditor = ...;
        AuditingInterceptor interceptor = new AuditingInterceptor(auditor, "calculator");
        Enhancer e = new Enhancer();
        e.setSuperclass(Calculator.class);
        e.setCallback(interceptor);
        Calculator calc = (Calculator)e.create();
        calc.add(2, 2); // Will be audited
    }
}
```

#### 其他

- Git: https://github.com/cglib/cglib
- 限制：TODO

### [asm](http://asm.ow2.org/)

#### 说明

#### 示例

```java
public class DynamicClassLoader extends ClassLoader {
    public Class<?> defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }
}

public interface Calculator {
    int add(int left, int right);
}

public class Demo {
    public static void main(String[] args) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(V1_7,                              // Java 1.7 
                ACC_PUBLIC,                         // public class
                "dynamic/DynamicCalculatorImpl",    // package and name
                null,                               // signature (null means not generic)
                "java/lang/Object",                 // superclass
                new String[]{ "dynamic/Calculator" }); // interfaces

        /* Build constructor */
        MethodVisitor con = cw.visitMethod(
                ACC_PUBLIC,                         // public method
                "<init>",                           // method name 
                "()V",                              // descriptor
                null,                               // signature (null means not generic)
                null);                              // exceptions (array of strings)


        con.visitCode();                            // Start the code for this method
        con.visitVarInsn(ALOAD, 0);                 // Load "this" onto the stack
        con.visitMethodInsn(INVOKESPECIAL,          // Invoke an instance method (non-virtual)
                "java/lang/Object",                 // Class on which the method is defined
                "<init>",                           // Name of the method
                "()V",                              // Descriptor
                false);                             // Is this class an interface?
        con.visitInsn(RETURN);                      // End the constructor method
        con.visitMaxs(1, 1);                        // Specify max stack and local vars

        /* Build 'add' method */
        MethodVisitor mv = cw.visitMethod(
                ACC_PUBLIC,                         // public method
                "add",                              // name
                "(II)I",                            // descriptor
                null,                               // signature (null means not generic)
                null);                              // exceptions (array of strings)
        mv.visitCode();
        mv.visitVarInsn(ILOAD, 1);                  // Load int value onto stack
        mv.visitVarInsn(ILOAD, 2);                  // Load int value onto stack
        mv.visitInsn(IADD);                         // Integer add from stack and push to stack
        mv.visitInsn(IRETURN);                      // Return integer from top of stack
        mv.visitMaxs(2, 3);                         // Specify max stack and local vars
        cw.visitEnd();                              // Finish the class definition        
        DynamicClassLoader loader = new DynamicClassLoader();
        Class<?> clazz = loader.defineClass("dynamic.DynamicCalculatorImpl", cw.toByteArray());
        System.out.println(clazz.getName());
        Calculator calc = (Calculator)clazz.newInstance();
        System.out.println("2 + 2 = " + calc.add(2, 2));        
        
    }
}
```

####其他

### [javassist](http://www.javassist.org/)

#### 说明

#### 示例

##### maven

```xml
<dependency>
    <groupId>org.javassist</groupId>
    <artifactId>javassist</artifactId>
    <version>${javaassist.version}</version>
</dependency>

<properties>
    <javaassist.version>3.21.0-GA</javaassist.version>
</properties>
```

##### usage

```java
/** POJO */
public class Point {
    private int x;
    private int y;

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

   //standard constructors/getters/setters
}

/** javap -c Point.class class类文件结构 */
public class com.baeldung.javasisst.Point {
  public com.baeldung.javasisst.Point(int, int);
    Code:
       0: aload__0
       1: invokespecial #1                 //Method java/lang/Object."<init>":()V
       4: aload__0
       5: iload__1
       6: putfield      #2                 //Field x:I
       9: aload__0
      10: iload__2
      11: putfield      #3                 //Field y:I
      14: return

  public void move(int, int);
    Code:
       0: aload__0
       1: iload__1
       2: putfield      #2                 //Field x:I
       5: aload__0
       6: iload__2
       7: putfield      #3                 //Field y:I
      10: return
}

public class Demo {
    public static void main(String[] args) {
        
        /** ---------- 生成类文件 ----------*/
        
        /** java对象层面 */
        ClassFile cf = new ClassFile(false, "com.baeldung.JavassistGeneratedClass", null);
        cf.setInterfaces(new String[]{"java.lang.Cloneable"});

        FieldInfo f = new FieldInfo(cf.getConstPool(), "id", "I");
        f.setAccessFlags(AccessFlag.PUBLIC);
        cf.addField(f);

        ClassPool classPool = ClassPool.getDefault();
        Field[]fields = classPool.makeClass(cf).toClass().getFields();

        assertEquals(fields[0].getName(), "id");
        
        /** java字节码层面 */
        ClassPool cp = ClassPool.getDefault();
        ClassFile cf = cp.get("com.baeldung.javasisst.Point")
          	.getClassFile();
        MethodInfo minfo = cf.getMethod("move");
        CodeAttribute ca = minfo.getCodeAttribute();
        CodeIterator ci = ca.iterator();

        List<String> operations = new LinkedList<>();
        while (ci.hasNext()) {
            int index = ci.next();
            int op = ci.byteAt(index);
            operations.add(Mnemonic.OPCODE[op]);
        }

        assertEquals(operations,
          	Arrays.asList(
          		"aload__0",
          		"iload__1",
          		"putfield",
          		"aload__0",
          		"iload__2",
          		"putfield",
          		"return"));
        
        /** ---------- 添加字段 ----------*/
        ClassFile cf = ClassPool.getDefault()
          	.get("com.baeldung.javasisst.Point").getClassFile();

        FieldInfo f = new FieldInfo(cf.getConstPool(), "id", "I");
        f.setAccessFlags(AccessFlag.PUBLIC);
        cf.addField(f);

        ClassPool classPool = ClassPool.getDefault();
        Field[] fields = classPool.makeClass(cf).toClass().getFields();
        List<String> fieldsList = Stream.of(fields)
          	.map(Field::getName)
          	.collect(Collectors.toList());

        assertTrue(fieldsList.contains("id"));
        
        
        /** ---------- 添加构造器 ----------*/        
        ClassFile cf = ClassPool.getDefault()
          	.get("com.baeldung.javasisst.Point").getClassFile();
        Bytecode code = new Bytecode(cf.getConstPool());
        code.addAload(0);
        code.addInvokespecial("java/lang/Object", MethodInfo.nameInit, "()V");
        code.addReturn(null);

        MethodInfo minfo = new MethodInfo(cf.getConstPool(), MethodInfo.nameInit, "()V");
        minfo.setCodeAttribute(code.toCodeAttribute());
        cf.addMethod(minfo);
        
        CodeIterator ci = code.toCodeAttribute().iterator();
        List<String> operations = new LinkedList<>();
        while (ci.hasNext()) {
            int index = ci.next();
            int op = ci.byteAt(index);
            operations.add(Mnemonic.OPCODE[op]);
        }

        assertEquals(operations, Arrays.asList("aload__0", "invokespecial", "return"));
        
    }
}
```

#### 其他

### [bcel](http://jakarta.apache.org/bcel/)

#### 说明

​		字节码引擎库 Byte Code Engineering Library

#### 示例

##### maven

```xml
<dependency>
    <groupId>org.apache.bcel</groupId>
    <artifactId>bcel</artifactId>
    <version>6.5.0</version>
</dependency>
```

#####usage

```java
/** Method to be timed */
public class StringBuilder
{
    private String buildString(int length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            result += (char)(i%26 + 'a');
        }
        return result;
    }
     
    public static void main(String[] argv) {
        StringBuilder inst = new StringBuilder();
        for (int i = 0; i < argv.length; i++) {
            String result = inst.buildString(Integer.parseInt(argv[i]));
            System.out.println("Constructed string of length " +
                result.length());
        }
    }
}

/** Timing added to original method */
public class StringBuilder
{
    private String buildString$impl(int length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            result += (char)(i%26 + 'a');
        }
        return result;
    }
     
    private String buildString(int length) {
        long start = System.currentTimeMillis();
        String result = buildString$impl(length);
        System.out.println("Call to buildString$impl took " +
            (System.currentTimeMillis()-start) + " ms.");
        return result;
    }
     
    public static void main(String[] argv) {
        StringBuilder inst = new StringBuilder();
        for (int i = 0; i < argv.length; i++) {
            String result = inst.buildString(Integer.parseInt(argv[i]));
            System.out.println("Constructed string of length " +
                result.length());
        }
    }
}

public class BCELTiming
{
    private static void addWrapper(ClassGen cgen, Method method) {
         
        // set up the construction tools
        InstructionFactory ifact = new InstructionFactory(cgen);
        InstructionList ilist = new InstructionList();
        ConstantPoolGen pgen = cgen.getConstantPool();
        String cname = cgen.getClassName();
        MethodGen wrapgen = new MethodGen(method, cname, pgen);
        wrapgen.setInstructionList(ilist);
         
        // rename a copy of the original method
        MethodGen methgen = new MethodGen(method, cname, pgen);
        cgen.removeMethod(method);
        String iname = methgen.getName() + "$impl";
        methgen.setName(iname);
        cgen.addMethod(methgen.getMethod());
        Type result = methgen.getReturnType();
         
        // compute the size of the calling parameters
        Type[] types = methgen.getArgumentTypes();
        int slot = methgen.isStatic() ? 0 : 1;
        for (int i = 0; i < types.length; i++) {
            slot += types[i].getSize();
        }
         
        // save time prior to invocation
        ilist.append(ifact.createInvoke("java.lang.System",
            "currentTimeMillis", Type.LONG, Type.NO_ARGS, 
            Constants.INVOKESTATIC));
        ilist.append(InstructionFactory.
            createStore(Type.LONG, slot));
         
        // call the wrapped method
        int offset = 0;
        short invoke = Constants.INVOKESTATIC;
        if (!methgen.isStatic()) {
            ilist.append(InstructionFactory.
                createLoad(Type.OBJECT, 0));
            offset = 1;
            invoke = Constants.INVOKEVIRTUAL;
        }
        for (int i = 0; i < types.length; i++) {
            Type type = types[i];
            ilist.append(InstructionFactory.
                createLoad(type, offset));
            offset += type.getSize();
        }
        ilist.append(ifact.createInvoke(cname, 
            iname, result, types, invoke));
         
        // store result for return later
        if (result != Type.VOID) {
            ilist.append(InstructionFactory.
                createStore(result, slot+2));
        }
         
        // print time required for method call
        ilist.append(ifact.createFieldAccess("java.lang.System",
            "out",  new ObjectType("java.io.PrintStream"),
            Constants.GETSTATIC));
        ilist.append(InstructionConstants.DUP);
        ilist.append(InstructionConstants.DUP);
        String text = "Call to method " + methgen.getName() +
            " took ";
        ilist.append(new PUSH(pgen, text));
        ilist.append(ifact.createInvoke("java.io.PrintStream",
            "print", Type.VOID, new Type[] { Type.STRING },
            Constants.INVOKEVIRTUAL));
        ilist.append(ifact.createInvoke("java.lang.System", 
            "currentTimeMillis", Type.LONG, Type.NO_ARGS, 
            Constants.INVOKESTATIC));
        ilist.append(InstructionFactory.
            createLoad(Type.LONG, slot));
        ilist.append(InstructionConstants.LSUB);
        ilist.append(ifact.createInvoke("java.io.PrintStream",
            "print", Type.VOID, new Type[] { Type.LONG },
            Constants.INVOKEVIRTUAL));
        ilist.append(new PUSH(pgen, " ms."));
        ilist.append(ifact.createInvoke("java.io.PrintStream",
            "println", Type.VOID, new Type[] { Type.STRING },
            Constants.INVOKEVIRTUAL));
             
        // return result from wrapped method call
        if (result != Type.VOID) {
            ilist.append(InstructionFactory.
                createLoad(result, slot+2));
        }
        ilist.append(InstructionFactory.createReturn(result));
         
        // finalize the constructed method
        wrapgen.stripAttributes(true);
        wrapgen.setMaxStack();
        wrapgen.setMaxLocals();
        cgen.addMethod(wrapgen.getMethod());
        ilist.dispose();
    }
     
    public static void main(String[] argv) {
        if (argv.length == 2 && argv[0].endsWith(".class")) {
            try {
             
                JavaClass jclas = new ClassParser(argv[0]).parse();
                ClassGen cgen = new ClassGen(jclas);
                Method[] methods = jclas.getMethods();
                int index;
                for (index = 0; index < methods.length; index++) {
                    if (methods[index].getName().equals(argv[1])) {
                        break;
                    }
                }
                if (index < methods.length) {
                    addWrapper(cgen, methods[index]);
                    FileOutputStream fos =
                        new FileOutputStream(argv[0]);
                    cgen.getJavaClass().dump(fos);
                    fos.close();
                } else {
                    System.err.println("Method " + argv[1] + 
                        " not found in " + argv[0]);
                }
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
             
        } else {
            System.out.println
                ("Usage: BCELTiming class-file method-name");
        }
    }
}
```



####其他

- 参考：https://www.ibm.com/developerworks/java/library/j-dyn0414/
- 官网：https://commons.apache.org/proper/commons-bcel/
- Git：https://github.com/apache/commons-bcel

### [Burningwave Core](https://github.com/burningwave/core)

#### 说明

 		sources generating components 源代码生成组件

####示例

```java
package org.burningwave.core.examples.classfactory;

import static org.burningwave.core.assembler.StaticComponentContainer.Constructors;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.burningwave.core.Virtual;
import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.assembler.ComponentSupplier;
import org.burningwave.core.classes.AnnotationSourceGenerator;
import org.burningwave.core.classes.ClassFactory;
import org.burningwave.core.classes.ClassSourceGenerator;
import org.burningwave.core.classes.FunctionSourceGenerator;
import org.burningwave.core.classes.GenericSourceGenerator;
import org.burningwave.core.classes.TypeDeclarationSourceGenerator;
import org.burningwave.core.classes.UnitSourceGenerator;
import org.burningwave.core.classes.VariableSourceGenerator;
public class RuntimeClassExtender {
    
    @SuppressWarnings("resource")
    public static void execute() throws Throwable {
        UnitSourceGenerator unitSG = UnitSourceGenerator.create("packagename").addClass(
            ClassSourceGenerator.create(
                TypeDeclarationSourceGenerator.create("MyExtendedClass")
            ).addModifier(
                Modifier.PUBLIC
            //generating new method that override MyInterface.convert(LocalDateTime)
            ).addMethod(
                FunctionSourceGenerator.create("convert").setReturnType(
                    TypeDeclarationSourceGenerator.create(
                        Comparable.class
                    ).addGeneric(
                        GenericSourceGenerator.create(Date.class)
                    )
                )
                .addParameter(VariableSourceGenerator.create(LocalDateTime.class, "localDateTime"))
                .addModifier(Modifier.PUBLIC)
                .addAnnotation(AnnotationSourceGenerator.create(Override.class))
                .addBodyCodeRow(
                    "return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());"
                ).useType(ZoneId.class)
            ).addConcretizedType(
                MyInterface.class
            ).expands(ToBeExtended.class)
        );
        System.out.println("\nGenerated code:\n" + unitSG.make());
        //With this we store the generated source to a path
        unitSG.storeToClassPath(System.getProperty("user.home") + "/Desktop/bw-tests");
        ComponentSupplier componentSupplier = ComponentContainer.getInstance();
        ClassFactory classFactory = componentSupplier.getClassFactory();
        //this method compile all compilation units and upload the generated classes to default
        //class loader declared with property "class-factory.default-class-loader" in 
        //burningwave.properties file (see "Overview and configuration").
        //If you need to upload the class to another class loader use
        //loadOrBuildAndDefine(LoadOrBuildAndDefineConfig) method
        Class<?> generatedClass = classFactory.loadOrBuildAndDefine(
            unitSG
        ).get(
            "packagename.MyExtendedClass"
        );
        ToBeExtended generatedClassObject =
            Constructors.newInstanceOf(generatedClass);
        generatedClassObject.printSomeThing();
        System.out.println(
            ((MyInterface)generatedClassObject).convert(LocalDateTime.now()).toString()
        );
        //You can also invoke methods by casting to Virtual (an interface offered by the
        //library for faciliate use of runtime generated classes)
        Virtual virtualObject = (Virtual)generatedClassObject;
        //Invoke by using reflection
        virtualObject.invoke("printSomeThing");
        //Invoke by using MethodHandle
        virtualObject.invokeDirect("printSomeThing");
        System.out.println(
            ((Date)virtualObject.invokeDirect("convert", LocalDateTime.now())).toString()
        );
    }   

    public static class ToBeExtended {
        public void printSomeThing() {
            System.out.println("Called method printSomeThing");
        }
    }

    public static interface MyInterface {
        public Comparable<Date> convert(LocalDateTime localDateTime);
    }

    public static void main(String[] args) throws Throwable {
        execute();
    }
}
```

#### 其他