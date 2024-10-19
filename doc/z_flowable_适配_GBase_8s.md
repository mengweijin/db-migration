# 版本 flowable 7.0.1

参考：[版本 flowable 6.4.2适配gbase8s](https://www.gbase.cn/community/post/4540)

## 修改方案

### 增加gbase支持

```text
org.flowable.common.engine.impl.AbstractEngineConfiguration.java
getDefaultDatabaseTypeMappings()方法
添加
databaseTypeMappings.setProperty("GBase 8s Server","gbase8s");
```

在org.flowable.common.db.properties 包下，添加 gbase8s.properties

内容将此路径下 oracle.properties 内容复制即可。

### 新增sql文件

```text
org.flowable.db.create
org.flowable.db.drop
org.flowable.common.db.create
org.flowable.common.db.drop
org.flowable.entitylink.service.db.create
org.flowable.entitylink.service.db.drop
org.flowable.eventsubscription.service.db.create
org.flowable.eventsubscription.service.db.drop
org.flowable.identitylink.service.db.create
org.flowable.identitylink.service.db.drop
org.flowable.idm.db.create
org.flowable.idm.db.drop
org.flowable.job.service.db.create
org.flowable.job.service.db.drop
org.flowable.task.service.db.create
org.flowable.task.service.db.drop
org.flowable.variable.service.db.create
org.flowable.variable.service.db.drop

# 7.0.1 多了下面脚本
org.flowable.batch.service.db.create
org.flowable.batch.service.db.drop
```

在上述的包下新增 sql 文件。将 oracle 的 sql 文件复制出来，将名字中的oracle 替换成 gbase8s

### 修改sql语句

```text
1. 将 NVARCHAR2 替换成 VARCHAR
2. 将 NUMBER 替换成 DECIMAL
3. 将 NUMBER(*,10) 中的 * 替换成 28。比如：NUMBER(*,10) 替换后为：DECIMAL(28, 10)
4. （实测可以不用替换这个）将 TIMESTAMP(6) 替换成 TIMESTAMP(5)

5. 修改 ALTER 添加外键的语句支持 gbase8s。将语句中的 constraint 后边的名称去掉即可。
# 例如：
alter table ACT_ID_PRIV_MAPPING add constraint ACT_FK_PRIV_MAPPING foreign key (PRIV_ID_) references ACT_ID_PRIV (ID_);
# 修改为：
alter table ACT_ID_PRIV_MAPPING add constraint foreign key (PRIV_ID_) references ACT_ID_PRIV (ID_);

6. 修改 ALTER 添加唯一约束的语法支持 gbase8s。将语句中的 constraint 后边的名称去掉，在最后面加上：constraint 名称；
# 例如：
alter table ACT_ID_PRIV add constraint ACT_UNIQ_PRIV_NAME unique (NAME_);
# 修改后
alter table ACT_ID_PRIV add constraint unique (NAME_) constraint ACT_UNIQ_PRIV_NAME;

7、以下两条语句，需要特别调整合并为一条语句。
# 修改前
create index ACT_IDX_PROCDEF_INFO_PROC on ACT_PROCDEF_INFO(PROC_DEF_ID_);

alter table ACT_PROCDEF_INFO add constraint ACT_UNIQ_INFO_PROCDEF unique (PROC_DEF_ID_);
# 修改后
Create unique index ACT_IDX_PROCDEF_INFO_PROC on ACT_PROCDEF_INFO(PROC_DEF_ID_);
```
