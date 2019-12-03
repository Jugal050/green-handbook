# PL/SQL Developer使用技巧

- 保存登录历史

  Tools->Preferences->Oracle->Logon History：

  ​	Definition:

  ​		勾选 Store with password

- 自动提示（表、字段、包...）配置

  Tools->Preferences->User Interface->Code Assistant:

  ​	勾选 Automatically activated

- 快捷键设置-语句提示快捷键

  Tools->Preferences->User Interface->Key configuration:

  ​	Tools/Source/Code Assistant: Alt+/

- 自定义常用语句

  Tools->Preferences->User Interface->Editor:

  ​	AutoReplace:

  ​		勾选 Enable

  ​		点击 Edit 添加常用语句，如：

  ​			sf = select * from

  ​			sfw = select * from table where

  ​			iv = insert into table ()  values ()

  ​			df = delect from 

  ​			dfw = delect from table where

  ​			sc = select count(*) from

  ​			us = update table set column1 = value1, column2 = value2 where 

  ​			... 

- 设置关键字大小写

  Tools->Preferences->User Interface->Editor:

  ​	Keyword case: 

  ​		Uppercase

- 显示当前编辑行

  Tools->Preferences->User Interface->Editor:

  ​	勾选 Highlight edit Line	

- 导出所有表、表说明、表结构

  -- 查询用户'SCOTT'下的所有表

  select table_name from all_tables t where t.owner = 'SCOTT' order by t.table_name;

  -- 查询指定表的说明

  select table_name, comments from user_tab_comments where table_name = 'employee';

  -- 导出表信息（表名、表说明、表结构）

  ​	-- 开始导出

  ​	spool D:\doc\table_desc.txt

  ​	-- 导出信息 (以下为导出的内容：已单列为例，可以将所有表名列编辑之后粘贴执行语句)

  ​		-- 导出表名、表说明

  ​		 select table_name, comments from user_tab_comments where table_name = 'employee';

  ​		-- 导出表结构

  ​		desc employee;

  ​	-- 结束导出

  ​	spool off;

- ...






​	