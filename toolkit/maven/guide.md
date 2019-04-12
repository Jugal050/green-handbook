

## Maven guide

#### 升级maven工程及子模块的版本号：

解决办法：先把parent project更新到指定version，然后再更新子模块，让其与父模块的版本号保持一致：

```
1. mvn versions:set -DnewVersion=1.0.1-SNAPSHOT
2. mvn -N versions:update-child-modules
3. 执行后maven会将pom.xml保留一个备份：pom.xm.versionsBackup
4. 确认升级版本无问题后，执行mvn versions:commit 确认升级，去掉备份的pom.xml.versionsBackup
```

#### 在根目录下统一修改版本号：

```
mvn versions:set -DnewVersion=1.16.27-SNAPSHOT
```

#### 把父模块更新到指定版本号，然后更新子模块，与父模块有相同的版本号：

```
mvn versions:set -DnewVersion=1.0.1-SNAPSHOT 和 mvn -N versions:update-child-modules  
```

注意，如果子模块的版本号已经与父模块不一致，则先运行后面一条命令统一，在更新父模块版本。

#### 命令：

* maven清理指令：

  ```
  mvn clean 
  清理项目生产的临时文件,一般是模块下的target目录 
  ```

* maven打包：

  ```
  根目录下：
  mvn clean package
  项目打包工具,会在模块下的target目录生成jar或war等文件
  ```

* maven测试：

  ```
  mvn test 
  测试命令,或执行src/test/java/下junit的测试用例.
  ```

* 把生成的Jar文件放入本地仓库：

  ```
  根目录下：
  mvn clean install
  模块安装命令 
  将打包的的jar/war文件复制到你的本地仓库中,供其他模块使用
   
  -Dmaven.test.skip=true 
  跳过测试(同时会跳过test compile) 
  ```

* 发布命令：

  ```
  mvn deploy
  将打包的文件发布到远程参考,提供其他人员进行下载依赖eclipse:
  ```

* eclipse:

  ```
  mvn eclipse:clean eclipse:eclipse
  ```

* maven-eclipse-plugin插件依赖： 

  ```
  mvn eclipse:eclipse 
  生成eclipse配置文件,导入到eclipse开发,如果是使用m2eclipse插件,则可以不用此命令.直接使用插件导入到eclipse进行开发
  注:通过次命令生产的项目,需要在eclipse中配置M2_REPO的命令,指向你的本地仓库文件夹. 
  mvn eclipse:m2eclipse 
  生成eclipse配置文件,该配置文件需依赖eclipse 中有m2eclipse 
  -DdownloadSources=true 下载依赖包的源码文件 
  -Declipse.addVersionToProjectName=true 添加版本信息到项目名称中 
  ```

* 清除eclipse的项目文件:

  ```
  mvn eclipse:clean 
  ```

* maven-jetty-plugin插件命令：

  ```
  mvn jetty:run 
  可以直接用jetty的服务器运行 
  注:此命令只适用于war的模块,即web模块. 
  ```

* maven-archetype-plugin 插件命令： 

  ```
  mvn archetype:generate 
  模块创建命令, 
  执行命令后，会提示选择创建项目的模版，这里选18(maven-archetype-quickstart) 
  后面会提示你输入groupId(包存放的路径): 
  eg:com.test 
  提示输入artifactId(模块名称)： 
  eg:test-core 
  提示输入version(版本): 
  1.0.0-SNAPSHOT 
  提示输入package(指项目中基本的包路径): 
  eg:com.test 
  提示确认,回车即可
  ```

* Maven 版本管理 

  ```
  maven-release-plugin插件 
  说明: 
  发行版本,可与scm工具集成,来提供版本管理.不等同与版本控制.允许是必须有goal.两个常用的goal如下: 
  
  mvn release:clean 
  清理release操作是遗留下来的文件 
  
  mvn release:branch 
  说明: 
  创建分支,会在分支下创建执行的分支路径 
  -DbranchName=xxxx-100317 分支中的名称 
  -DupdateBranchVersions=false 是否更新分支的版本信息,默认为false 
  -DupdateWorkingCopyVersions=false 是否更新主干的版本信息,默认为true 
  
  mvn release:prepare 
  创建标记,会有交互过程,提示tag中pom的版本及trunk下的新版本号,每个模块都会询问,默认是最小版本号+1 
  -Dtag = 4.4.0 将在tags创建该名称文件夹 
  -DdryRun=true 检查各项设置是否正确,可做测试用,会产生一些修改的配置文件信息. 
  
  mvn release:perform 
  次命令会自动帮我们签出刚才打的tag，然后打包，分发到远程Maven仓库中 
  
  ```

* Maven站点报表 

  ```
  mvn site 生产项目报表 
  mvn project-info-reports:dependencies 生成项目依赖的报表 
  ```

* 依赖命令 

  ```
  mvn dependency:resolve 
  查看项目依赖情况
  
  mvn dependency:tree 
  打印出项目的整个依赖树
  
  mvn dependency:analyze 
  帮助你分析依赖关系, 用来取出无用, 重复依赖的好帮手
  
  mvn install -X 
  追踪依赖的完整轨迹 
  ```

* 生命周期 

  ```
  resource->compile->process-classes->process-test-resources->test-compile->test->prepare-package->package 
  
  resources:resources 绑定在resource处理阶段, 用来将src/main/resources下或者任何指定其他目录下的文件copy到输出目录中 
  
  resources:testResources 将test下的resources目录或者任何指定其他目录copy到test输出目录下 
  
  compiler:testCompile 将测试类编译(包括copy资源文件) 
  
  surefire:test 运行测试用例 
  
  jar:jar 打jar包
  ```

来自 <https://www.cnblogs.com/Vae1990Silence/p/4673949.html> 
