------------------------------ MySQL 常用语句 ------------------------------


------------------------------ DCL Data Control Language 数据控制语句 ------------------------------

-- grant revoke 等


------------------------------ DDL Data Definition Language 数据定义语言 ------------------------------

-- 新建表结构
CREATE TABLE `user`
(
  `id`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户编号',
  `name`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     DEFAULT NULL COMMENT '用户名称',
  `sex`      tinyint(2)                                                        DEFAULT NULL COMMENT '用户性别：0-男 1-女',
  `birthday` timestamp                                                    NULL DEFAULT NULL COMMENT '出生日期',
  `address`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     DEFAULT NULL COMMENT '住址',
  `remark`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci     DEFAULT NULL COMMENT '备注说明',
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 删除字段
alter table user drop hobby;

-- 添加字段
alter table user
  add hobby varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '爱好';

-- 添加字段（添加到第一个）
alter table user
  add hobby varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '爱好' first ;

-- 添加字段（在某个字段之后）
alter table user
  add hobby varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '爱好' after sex;
  
-- 修改字段类型、名称
alter table user
  modify hobby varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '爱好说明';

alter table user
  change hobby hobbys varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '爱好';
  
-- 修改字段默认值
alter table user
  alter remark set default null;
  
-- 修改表名
alter table user rename to users;  


------------------------------ DML Data Manipulation Language 数据操控语言 ------------------------------

-- 插入数据
insert into user (id, name, sex, birthday, address, remark)
values ('1', 'binvi', 0, now(), 'changping', 'nothing'),
       ('2', 'lily', 0, now(), 'chaoyang', 'nothing'),
       ('3', 'peter', 0, now(), 'tanggu', 'nothing'),
       ('4', 'brand', 0, now(), 'hexi', 'nothing');
	   
-- 修改数据
update user
set name    = 'binvy',
    address = 'hongfuyuan'
where id = '1';

-- 删除数据
delete from user where id = '1';


------------------------------ 常用查询语句 ------------------------------

-- 查看表结构（基本信息）
desc user;
show columns from user;
describe user;
explain user;

-- 查看表结构（详细信息）
select *
from information_schema.columns
where table_schema = 'demo'
  and table_name = 'user';

-- 查看建表语句
show create table user;

-- 查看所有的库
select lower(schema_name) schema_name
from information_schema.schemata;

-- 查看某一库中的所有表
select *
from information_schema.tables
where table_schema = 'demo';

-- 查看某一库下某一表的所有字段
select *
from information_schema.columns
where table_schema = 'demo'
  and table_name = 'user';

-- 查看某一库某一表的索引
SELECT DISTINCT lower(index_name) index_name,
                lower(index_type) type
FROM information_schema.statistics
WHERE table_schema = 'demo'
  AND table_name = 'user';

-- 查看某一个库下某一张表的某一个索引
SELECT lower(column_name) column_name,
       seq_in_index       column_position
FROM information_schema.statistics
WHERE table_schema = 'demo'
  AND table_name = 'user'
  AND index_name = 'primary';

-- 查看某一个库下某一个表的注释
SELECT table_comment comments
FROM information_schema.tables
WHERE table_schema = 'demo'
  AND table_name = 'user';

-- 查看某一个库下某一个表的列的注释
SELECT lower(column_name) column_name,
       column_comment     comments
FROM information_schema.columns
WHERE table_schema = 'demo'
  AND table_name = 'user';