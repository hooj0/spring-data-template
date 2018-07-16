# Spring Data Template Framework
`Spring Data Framework` 的扩展或使用模板脚手架，这是一种二次开发工具。按照本模板可以简单的套取模板中的文件，开发自己的`Spring Data` 框架。

切换中文版, Swith English

# 使用 spring-data-template
简要描述如何应用Spring数据模板来开发自己的`Spring Data`框架。

## 创建工程

首先，需要创建有意义的工程名，也就是当前`SpringData`框架的名称。例如：要创建一个缓存的`SpringData`框架，就取名 `spring-data-cache`。如果要创建一个`redis`的`SpringData`,那就取名 `spring-data-redis`。

## 创建Packages
创建合适的`package` 这个顶级的 `pkg`, 例如，用 `spring-data-cache`框架为例，那么`package` 应该为 `org.springframework.data.cache`,最后结束的是框架的名称。

## `Package`和实现类介绍

一般情况在顶级 `package` 下面是实现 spring-data 的主要核心包，它们一般都会有：
+ **annotations**：注解，用来配置`Query、Repository、Entity、Config`的注解类 
	+ `Query` 查询注解，可以在repo的接口方法中配置该注解，完成自定义的条件查询
    + `CountQuery` 继承 Query 完成 count 查询
    + `Model` 实体模型注解，注解后可以读取实体注解中的信息，方便持久化操作
    + `Setting` 实体模型注解，如 当前实体的配置路径
    + `Field` 实体模型属性注解，可以对属性进行映射操作
    + `Score` 实体属性注解，属性额外的配置信息
+ **config**：配置类型，基于`SpringData`的配置基类、`Entity`扫描类等
	- `AbstractTemplateConfiguration` 模板配置基类，一些通用的配置信息可以优先配置在其中
	- `TemplateEntityClassScanner` 扫描 `Repository` 中使用的 `EntityClass`，在`AbstractTemplateConfiguration.getInitialEntitySet` 方法中依赖该类
	- `TemplateNamespaceHandler` 命名空间的配置注入，可以不需要
+ **core**：核心业务，是完成 `repository` 底层业务增删改查操作的实现
	- **converter**： 在实体和底层接口`repository`交互的时候，进行对象字段类型转换
		+ `DateTimeConverter` 日期时间和字符串的转换，还可以有其他类的转换实现
		+ `MappingTemplateConverter` 映射转换器，完成底层和实体之间的映射转换
		+ `TemplateConverter` 映射转换器接口定义
	- **mapping**：在实体和底层接口对象、字段类型进行映射
		+ `TemplatePersistentEntity` 持久化实体对象接口，可以定义一些持久化实体的额外必要的信息，方便在实现 `repository` 的时候使用
		+ `TemplatePersistentProperty` 持久化实体对象属性接口，定义持久化属性的额外必要信息
		+ `SimpleTemplateMappingContext` 映射上下文，提供持久化实体和属性的构建方法
		+ `SimpleTemplatePersistentEntity` 持久化实体对象接口实现
		+ `SimpleTemplatePersistentProperty` 持久化实体对象属性接口实现
	- **query**：字符串查询、注解查询、CRUD操作的**条件**的核心实现		
	- `MyTplTemplate` 模板类，完成CRUD操作，提供给 `repository` 的底层实现
	- `TemplateOperations` 模板类接口，完成CRUD操作的定义
+ **enums**：枚举配置类
+ **repository**：` repository` 接口规则和主要实现
  - **cid**：使用CDI依赖注入的实现，在不依赖 `spring` 注入的情况下，也可以使用注入
  	+ `TemplateRepositoryBean` 使用 CdiRepositoryBean 创建 `Repository` 对象实例
  	+ `TemplateRepositoryExtension` 一个便携式CDI扩展，它为`Repository`注册bean，提供 `CdiRepositoryBean` 需要的实现
  - **config**：基于`SpringData`的配置类，开启当前框架或一些基础配置
  	+ `EnableTemplateRepositories` 核心配置，是否开启 `spring-data-template` 框架和一些载入的配置方式
  	+ `TemplateRepositoriesRegistrar` 设置`RepositoriesRegistrar`的注册实现方式
  	+ `TemplateRepositoryConfigurationExtension` 提供 `RepositoryConfiguration` 配置信息实现
  - **query**：基于`SpringData`的查询实现，需要基础`SpringCommon`的一些接口和类去完成
  	+ **simple**：提供`repository`接口注解SQL字符串查询和`PartTree`查询的简单实现
  	+ **complex**：提供`repository`接口注解SQL字符串查询和`PartTree`查询的复杂实现
  	+ `TemplateQueryMethod` 提供`repository`接口查询方法的基本信息
  	+ `SimpleTemplateQueryCreator` 通过`repository`接口查询方法动态生成SQL查询语句
  	+ `TemplateEntityMetadata` 提供`repository` 持久化实体对象的元数据信息
  - **support**：当前` repository` 的核心实现
  	+ `TemplateEntityInformation` 扩展`EntityMetadata`以添加查询实体实例信息的功能
  	+ `MappingTemplateEntityInformation` 实现 EntityInformation 填充 Entity 相关信息。并且可以充分利用 `Converter/PersistentEntity` 进行数据转换与填充。
  	+ `TemplateEntityInformationCreator` 实体信息 `TemplateEntityInformation` 对象创造者接口
  	+ `TemplateEntityInformationCreatorImpl` 实体信息 `TemplateEntityInformation` 对象创造者接口实现
  	+ `TemplateRepositoryFactory` 通过 `TemplateRepositoryFactory` 创建`TemplateRepository`实例
  	+ `TemplateRepositoryFactoryBean` 核心类，创建`TemplateRepositoryFactory`实例
  	+ `AbstractTemplateRepository` **repo** 的抽象实现，方便扩展
  	+ `SimpleTemplateRepository` 简单实现 `TemplateRepository`，支持 实体和字符串泛型，字符串类型的Id
  	+ `NumberKeyedRepository` 简单实现 `TemplateRepository`，`Number` 类型的Id
  	+ `UUIDTemplateRepository` 简单实现 `TemplateRepository`，`UUID` 类型的Id
  - `TemplateRepository` 顶级 `repository`接口，所有接口继承完成`CRUD`的`repo`操作
+ **util**：一些常用工具类
   其他与`core`相关的业务`package`

## 总结
`SpringData`可以和 `SpringBoot、SpringCloud、SpringFramework` 无缝集成，开发自己的 `SpringData` 框架方便使用和扩展系统。本框架只是一个模板，可以帮助你了解和学习SpringData底层的接口知识。