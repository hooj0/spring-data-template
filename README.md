# Spring Data Template Framework

`Spring Data Framework`The extension or use of template scaffolding, which is a secondary development tool. According to this template, you can easily extract the files in the template and develop your own `Spring Data`framework.*This case is intended for learning and communication and cannot be used for actual production.*

[中文版](readme_zh.md), [Swith English version](README.md)

# Use spring-data-template

Briefly describe how to apply Spring data templates to develop your own `Spring Data`framework.

## Create project

First, you need to create a meaningful project name, which is `SpringData`the name of the current frame. For example, to create a cached `SpringData`frame, just name it `spring-data-cache`. If you want to create a `redis`'s `SpringData`, it is named `spring-data-redis`.

## Create Packages

Creating suitable `package`the top `pkg`, for example, with a `spring-data-cache`frame, for example, it `package`should be `org.springframework.data.cache`, and end is the name of the frame.

## `Package`And implementation class introduction

In general, the top `package`Here are the main core of the package to achieve spring-data, which usually are:

- **annotations**: annotations, `Query、Repository、Entity、Config`annotation classes for configuration
  - `Query` Query annotations, you can configure this annotation in the interface method of repo to complete the custom conditional query.
  - `CountQuery` Inherit Query to complete count query
  - `Model` Entity model annotation, after annotation, you can read the information in the entity annotation to facilitate the persistence operation
  - `Setting` Entity model annotations, such as the configuration path of the current entity
  - `Field` Entity model attribute annotation, you can map attributes
  - `Score` Entity attribute annotation, attribute additional configuration information

- **config**: configuration type, based on`SpringData`the configuration base class, `Entity` scan class, etc.

  - `AbstractTemplateConfiguration` The template configuration base class, some common configuration information can be preferentially configured in it
  - `TemplateEntityClassScanner`Scanning `Repository`used `EntityClass`, in `AbstractTemplateConfiguration.getInitialEntitySet`dependence of the type of process
  - `TemplateNamespaceHandler` Configuration injection of namespaces, you don't need

- **core** : Core business is done `repository` to achieve the underlying business CRUD operations

  - **converter**: underlying interface entity and `repository`  interaction time, object-type field converter

    - `DateTimeConverter` Date time and string conversion, there can also be conversion implementations of other classes
    - `MappingTemplateConverter` Map converter to complete the mapping between the underlying and the entity
    - `TemplateConverter` Map converter interface definition

  - **mapping**: **mapping** between entities and underlying interface objects, field types

    - `TemplatePersistentEntity` Persistent entity object interface, you can define additional information necessary for a number of persistent entities, to facilitate the realization of `repository`the use of time
    - `TemplatePersistentProperty` Persistent entity object attribute interface, additional necessary information to define persistent attributes
    - `SimpleTemplateMappingContext` Mapping context, providing methods for building persistent entities and attributes
    - `SimpleTemplatePersistentEntity` Persistent entity object interface implementation
    - `SimpleTemplatePersistentProperty` Persistent entity object attribute interface implementation

  - **query** : query string, annotation inquiry, CRUD operation **conditions of** the core implementation

  - `MyTplTemplate`Template class, CRUD operations is completed, provided to the `repository`underlying implementation

  - `TemplateOperations` Template class interface, complete the definition of CRUD operations

- **enums** : enum class configuration

- **repository**: `repository` interface rules and main implementation

  - **cdi**: CDI implemented using dependency injection, without depending on `spring` the case of injection, the injection may be used
    
    - `TemplateRepositoryBean`Use CdiRepositoryBean Creating `Repository`object instances
    - `TemplateRepositoryExtension`A portable CDI extensions, it is `Repository`registered bean, provided `CdiRepositoryBean`to achieve the required

  - **config**: based on`SpringData` the configuration class, open the current framework or some basic configuration
    
    - `EnableTemplateRepositories`Core configuration, whether open `spring-data-template`framework and loaded configuration
    - `TemplateRepositoriesRegistrar`Set `RepositoriesRegistrar`registration implementation
    - `TemplateRepositoryConfigurationExtension`Provide `RepositoryConfiguration`configuration information to achieve

  - **query**: Based on `SpringData` the query implementation, basic needs `SpringCommon` some interfaces and classes to complete
    
    - **simple** : Provides a simple implementation of the `repository`interface annotation SQL string query and `PartTree`query
    - **complex** : Provides a complex implementation of `repository`interface annotation SQL string queries and `PartTree`queries
    - `TemplateQueryMethod`Provide `repository`basic information about the interface query method
    - `SimpleTemplateQueryCreator``repository`Dynamically generate SQL query statements through interface query methods
    - `TemplateEntityMetadata`Provide `repository`metadata information for persistent entity objects

  - **support**: current `repository` core implementation
    
    - `TemplateEntityInformation`Expand `EntityMetadata`to add the ability to query entity instance information
    - `MappingTemplateEntityInformation`Implement EntityInformation to populate Entity related information. And you can take advantage of `Converter/PersistentEntity`data conversion and the filling.
    - `TemplateEntityInformationCreator`Entity information `TemplateEntityInformation`Object creator Interface
    - `TemplateEntityInformationCreatorImpl`Entity information `TemplateEntityInformation`Object creator interface
    - `TemplateRepositoryFactory`By `TemplateRepositoryFactory`Creating `TemplateRepository`instances
    - `TemplateRepositoryFactoryBean`Core class, create an `TemplateRepositoryFactory`instance
    - `AbstractTemplateRepository` Abstract implementation of **repo** , easy to expand
    - `SimpleTemplateRepository`Simple implementation `TemplateRepository`, supporting entity and string generics, string type Id
    - `NumberKeyedRepository`Simple implementation `TemplateRepository`, `Number`type Id
    - `UUIDTemplateRepository`Simple implementation `TemplateRepository`, `UUID`type Id

  - `TemplateRepository`Top `repository`interfaces, all interfaces inherit the completion `CRUD`of `repo`the operation

- **util** : some common tools `core`related to other related businesses`package`

## summary

`SpringData`And can `SpringBoot、SpringCloud、SpringFramework`seamlessly integrate, develop their own `SpringData`framework for ease of use and expand the system. This framework is just a template to help you understand and learn the underlying interface knowledge of SpringData.