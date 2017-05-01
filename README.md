# spring-mybatis
spring4.1.0+mybatis3.1+springmvc4.1.0配置

## spring 与spring mvc配置
看web.xml里面的说明即可，这里就不多说了

## mybatis与spring集成（一）
其实spring懂了之后，集成mybatis也应该很简单才对，但是在继承过程中却遇到很多问题。我也是醉了
先来了解一下mybatis
### 数据库链接

#### SqlSessionFactoryBuilder
这个类可以被实例化，使用和丢弃。一旦你创建了 SqlSessionFactory 后，这个类就不需 要存在了。  
**==只需要创建一次，然后就可以丢去了==**

#### SqlSessionFactory
一旦被创建，SqlSessionFactory 应该在你的应用执行期间都存在。没有理由来处理或重 新创建它。  
**==仅仅只能需要创建一次，不能创建多个SqlSessionFactory==**

#### SqlSession
==每个线程都应该有它自己的 SqlSession 实例==。SqlSession 的实例不能被共享，也是线程 不安全的。因此最佳的范围是请求或方法范围
==关闭 Session 很重要，你应该确保使 用 finally 块来关闭它==
```
String resource = "org/mybatis/example/Configuration.xml";
Reader reader = Resources.getResourceAsReader(resource);
SqlSessionFactory sqlSessionFactory  = new SqlSessionFactoryBuilder().build(reader);
SqlSession session = sqlSessionFactory.openSession(); 
try {
BlogMapper mapper = session.getMapper(BlogMapper.class);
Blog blog = mapper.selectBlog(101);
} finally {
  session.close(); 
}
```
看了很简单吧，就一个读取数据库配置文件，连接数据库，管理数据库连接工厂，然后就是session，没了，就简单三步。我们用spring来管理
这些bean不就完了嘛，但是但是。。。。。

## mybatis与spring集成（二）
现在我们来集成一下
### 第一步配置数据库源
```
<!-- 1. 数据源 : DriverManagerDataSource -->
	<bean id="dataSource"
		class="org.springframework.jdbc.datasource.DriverManagerDataSource">
		<property name="driverClassName" value="com.mysql.jdbc.Driver" />
		<property name="url" value="jdbc:mysql://localhost:3306/mybatis" />
		<property name="username" value="root" />
		<property name="password" value="root" />
	</bean>
```  
### 第二步定义连接工厂
```
<!--
		2. mybatis的SqlSession的工厂: SqlSessionFactoryBean dataSource:引用数据源

		MyBatis定义数据源,同意加载配置
	-->
	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<property name="dataSource" ref="dataSource"></property>
		<property name="configLocation" value="classpath:mybatis-config.xml" />
		<property name="mapperLocations" value="classpath*:com/test/spring/**/*Mapper.xml"/>
	</bean>
```
这里就需要多说几句了，坑爹的东西在这里。  
mybatis-config.xml这个文件我是放在资源文件夹下，所以加载方式如下  
```
<property name="configLocation" value="classpath:mybatis-config.xml" />
```
但是UserDaoMapper.xml文件我发到的是src/java/ com.test.spring.mapper文件夹下,
```
<property name="mapperLocations" value="classpath*:com/test/spring/**/*Mapper.xml"/>
```  
配置路径需要加上**classpath\*:**

### 创建sqlSession
```
<!--
		3. mybatis自动扫描加载Sql映射文件/接口 : MapperScannerConfigurer sqlSessionFactory

		basePackage:指定sql映射文件/接口所在的包（自动扫描）
	-->
	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
		<!-- 扫描com.test.spring这个包以及它的子包下的所有映射接口类 -->
		<property name="basePackage" value="com.test.spring.*.dao" />
		<property name="sqlSessionFactoryBeanName" value="sqlSessionFactory" />
	</bean>
``` 
>没有必要在 Spring 的 XML 配置文件中注册所有的映射器。相反,你可以使用一个 MapperScannerConfigurer , 它 将 会 查 找 类 路 径 下 的 映 射 器 并 自 动 将 它 们 创 建 成 MapperFactoryBean。
>要创建 MapperScannerConfigurer,可以在 Spring 的配置中添加如下代码:
```
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
  <property name="basePackage" value="org.mybatis.spring.sample.mapper" />
</bean>
```
>basePackage 属性是让你为映射器接口文件设置基本的包路径。 你可以使用分号或逗号 作为分隔符设置多于一个的包路径。每个映射器将会在指定的包路径中递归地被搜索到。
>MapperScannerConfigurer 属性不支持使用了 PropertyPlaceholderConfigurer 的属 性替换,因为会在 Spring 其中之前来它加载。但是,你可以使用 PropertiesFactoryBean 和 SpEL 表达式来作为替代。
>注 意 , 没 有 必 要 去 指 定 SqlSessionFactory 或 SqlSessionTemplate , 因 为 MapperScannerConfigurer 将会创建 MapperFactoryBean,之后自动装配。但是,如果你使 用了一个 以上的 DataSource ,那 么自动 装配可 能会失效 。这种 情况下 ,你可 以使用 sqlSessionFactoryBeanName 或 sqlSessionTemplateBeanName 属性来设置正确的 bean 名 称来使用。这就是它如何来配置的,注意 bean 的名称是必须的,而不是 bean 的引用,因 此,value 属性在这里替代通常的 ref:
><property name="sqlSessionFactoryBeanName" value="sqlSessionFactory" />
>MapperScannerConfigurer 支 持 过 滤 由 指 定 的 创 建 接 口 或 注 解 创 建 映 射 器 。 annotationClass 属性指定了要寻找的注解名称。 
>markerInterface 属性指定了要寻找的父 接口。如果两者都被指定了,加入到接口中的映射器会匹配两种标准。默认情况下,这两个 属性都是 null,所以在基中给定的所有接口可以作为映射器加载。

**坑爹的事来了**
---
```
<property name="basePackage" value="com.test.spring.*.dao" />
```
大家应该记得springmvc配置注解扫描包
```
<!-- 注解扫描包 -->
	<context:component-scan base-package="com.test.spring.*" />
```
com.test.spring.*表示com.test.spring下面所有的包，自动递归下去。  
但是mybatis的不是这样的，不会递归，我也是醉了一地的。  
我的xxmapper.xml在com.test.spring.xx.dao下面，xx表示各种文件夹，我的dao层是放在不同的实体下面的，userDao放在com.test.spring.user.dao下面，roleDao放在com.teset.spring.role.dao下面，所以刚开始配置的时候我是这样写的  
```
<property name="basePackage" value="com.test.spring.*" />
```
结果运行起来死活包错，找不到映射文件，害我折腾了一天。  
