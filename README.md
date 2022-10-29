# QuickBoot
<p align="center">	
	<a target="_blank" href="https://search.maven.org/search?q=g:%22com.github.mengweijin%22%20AND%20a:%22quickboot-spring-boot-starter%22">
		<img src="https://img.shields.io/maven-central/v/com.github.mengweijin/quickboot-spring-boot-starter" />
	</a>
	<a target="_blank" href="https://github.com/mengweijin/quickboot/blob/master/LICENSE">
		<img src="https://img.shields.io/badge/license-Apache2.0-blue.svg" />
	</a>
	<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
		<img src="https://img.shields.io/badge/JDK-8-green.svg" />
	</a>
	<a target="_blank" href="https://gitee.com/mengweijin/quickboot/stargazers">
		<img src="https://gitee.com/mengweijin/quickboot/badge/star.svg?theme=dark" alt='gitee star'/>
	</a>
	<a target="_blank" href='https://github.com/mengweijin/quickboot'>
		<img src="https://img.shields.io/github/stars/mengweijin/quickboot.svg?style=social" alt="github star"/>
	</a>
</p>

## QuickBoot 版本
| 模块                            | 最新版本                                                                                                                                                                                                                                                |
|:------------------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| quickboot-spring-boot-starter | <a target="_blank" href="https://search.maven.org/search?q=g:%22com.github.mengweijin%22%20AND%20a:%22quickboot-spring-boot-starter%22"><img src="https://img.shields.io/maven-central/v/com.github.mengweijin/quickboot-spring-boot-starter"/></a> |
| quickboot-layui               | <a target="_blank" href="https://search.maven.org/search?q=g:%22com.github.mengweijin%22%20AND%20a:%22quickboot-layui%22"><img src="https://img.shields.io/maven-central/v/com.github.mengweijin/quickboot-layui"/></a>                             |

## 介绍
快速搭建 SpringBoot 项目，整合和配置常用的模块功能，节省搭建项目工程的时间。

- 接口统一返回格式：R.java
- 全局异常处理
- 可重复从 request 获取 body 的过滤器
- JdbcTemplate ColumnMapRowMapper
- 接口请求和返回值详情的 debug 日志记录。
- cors 跨域请求和 XSS 过滤
- mybatis plus
- orika 对象映射
- redis 重复提交拦截器、限流拦截器
- 各种 util 工具类
- spring doc

### quickboot-spring-boot-starter
```xml
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>quickboot-spring-boot-starter</artifactId>
    <version>${quickboot.version}</version>
</dependency>
```

#### orika
Bean 类型转换。扩展了 LocalDate, LocalDateTime 的默认转换。

使用示例：
```java
// 注册到 Spring 容器
@Component
public class BeanAToBConverter extends BeanConverter<BeanA, BeanB> {

    @Override
    public void fieldMapping(ClassMapBuilder<BeanA, BeanB> classMapBuilder) {
        // 字段映射。使用默认转换器
        classMapBuilder.field("nameA", "nameB")
                // 显示指定转换器
                .fieldMap("stringToDate", "stringToDate").converter(OrikaConverter.DATE_NORM_DATE.name()).add()
                .field("date", LambdaUtil.getFieldName(BeanB::getLocalDate))
                .field(LambdaUtil.getFieldName(BeanA::getDate), LambdaUtil.getFieldName(BeanB::getLocalDateTime));
    }
}

class BeanConverterTest {
  @Autowired
  private MapperFacade mapperFacade;
  public void test() {
    BeanA beanA = new BeanA();
    BeanB beanB = mapperFacade.map(beanA, BeanB.class);
  }
}
```

#### 配置加密
SafetyEncryptEnvironmentPostProcessor 可以实现配置文件敏感信息的加密配置。比如：数据库密码等信息。 使用方式：

1. 生成 16 位随机 AES 密钥：String randomKey = AESUtils.generateRandomKey();
2. 在启动 jar 时把下面生成的 key 通过命令行参数 -Dquickboot.cipher=${randomKey} 或者配置到 application.yml 中传递到应用程序中。Jar 启动参数（idea 设置 JVM arguments）
   - 命令行参数配置示例：-Dquickboot.cipher=abcd1234
   - application.properties 示例：quickboot.cipher=abcd1234
3. 密钥加密：配置在 application.yaml 中的加密值：String encrypt = AESUtils.encryptByKey(randomKey, password);
4. YML 配置：加密配置 {cipher} 开头紧接加密内容（ 非数据库配置专用， YML 中其它配置也是可以使用的 ）
   - spring.datasource.username='{cipher}Xb+EgsyuYRXw7U7sBJjBpA=='
   - spring.datasource.password='{cipher}Hzy5iliJbwDHhjLs1L0j6w=='
5. 为什么以 {cipher} 作为前缀？目的是和 Spring cloud config 加密前缀保持一直

#### cors 跨域
跨域配置。根据规则在application.yml中配置：
~~~~yaml
quickboot:
  cors:
    # 是否启用自动配置，默认 false。如果为 false, 则默认采用 SpringBoot 规则（不能跨域请求）；
    enabled: true
~~~~

#### xss
XSS 过滤配置。根据规则在application.yml中配置：依赖 Jsoup。
~~~~yaml
quickboot:
  xss:
    # 是否启用 xss 过滤，默认 false
    enabled: true
    # 不需要xss校验的链接（配置示例：/system/*,/tool/*）
    excludes: /druid/*,/actuator/*
~~~~
```java
@RestController
@RequestMapping("/user")
public class UserController {
  /**
   * 访问 http://localhost:8080/user/xss?html=<script>aaaa</script>bbbb
   * @return 返回 bbbb，说明过滤了 <script>aaaa</script>
   */
  @GetMapping("/xss")
  public String xss(String html){
    log.info("Entered xss method.");
    return html;
  }
}
```

#### p6spy 数据库查询日志记录
自动记录每一条真实查询的 SQL 记录到 debug 日志中。

#### SpringDoc OpenAPI（Swagger）
```xml
<dependency> 
    <groupId>org.springdoc</groupId>  
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>${springdoc.version}</version>
</dependency> 
```
启动服务后访问 url: http://localhost:8080/swagger-ui/index.html

#### AOP debug 级别，请求、响应参数详情等日志记录
仅当 spring.profiles.active 中配置了 !prod 才启用。如：spring.profiles.active=quickboot,dev 不包含 prod, 就会启用

#### flyway
~~~yaml
spring:
  # flyway在spring boot中默认配置位置为：classpath:db/migration
  # flyway命名规则为：V<VERSION>__<NAME>.sql (with <VERSION> an underscore-separated version, such as ‘1’ or ‘2_1’)
  flyway:
    # 默认不启用
    enabled: true
    baseline-on-migrate: true
    locations:
      - classpath:db/migration/h2
      # - classpath:db/migration/mysql
      # - classpath:db/migration/oracle
~~~

#### 分页 IPage & Pager
~~~java
@GetMapping("/page")
public IPage<User> getPage(Page<User> page){
    return userService.page(page);
}

@GetMapping("/pager")
public Pager<User> getPager(Pager<User> pager) {
final IPage<User> page = userService.page(pager.toPage());
    return pager.toPager(page);
}
~~~


#### CacheManager 的使用
* 放入缓存：cache.put(Object key, @Nullable Object value);
* 取出缓存：cache.get(Object key);
```java
@Autowired
private CacheManager cacheManager;
Cache cache=cacheManager.getCache("cacheName");
```

### quickboot-layui
- 集成 Thymeleaf，Layui，扩展了一个 Layui 的 quickboot 模块。
- 全局 jquery 配置。
- 添加 Ajax 的 put，delete 方法到 jquery。

```xml
<dependency>
    <groupId>com.github.mengweijin</groupId>
    <artifactId>quickboot-layui</artifactId>
    <version>${quickboot-layui.version}</version>
</dependency>
```
