# Flyway 对 PL/SQL 的支持

Oracle 的存储过程、触发器、函数等 PL/SQL 代码块需以 `/` 符号结尾，告知 SQL 引擎执行该代码块。

因此，在 Flyway 中执行 Oracle 存储过程脚本时，必须在 PL/SQL 块的末尾添加 `/` 符号，以明确表示代码块的结束。
这是 Oracle 数据库对 PL/SQL 语法解析的要求，Flyway 在执行脚本时同样需要遵循这一规则。

Flyway 默认使用普通 SQL 解析器执行脚本，而 Oracle 的 PL/SQL 块需要明确的分隔符。添加 / 符号能帮助 Flyway 识别代码块边界。

例如在创建存储过程或触发器时：

```sql
CREATE OR REPLACE PROCEDURE test_proc AS
BEGIN
    -- 存储过程逻辑。此处略。
END;
/
   
CREATE TRIGGER test_trig AFTER INSERT ON test_user
BEGIN
    UPDATE test_user SET name = CONCAT(name, 'triggered');
END;
/
```

与普通 SQL 脚本的区别：

普通的 DDL（如建表）或 DML（如插入数据）脚本无需 / 符号，因为它们不是多行代码块。 例如：

```sql
CREATE TABLE users (id NUMBER PRIMARY KEY);
INSERT INTO users VALUES (1);
```

但涉及 PL/SQL 结构的脚本（如存储过程、函数、包）必须添加 /。
