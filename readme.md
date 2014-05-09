Neo4JSystemDependencyApplication
================================

Simple implementation of a system dependency map, used to demonstrate Neo4J and Spring Data usage.

Technology
----------

Technology demonstrated:
* Annotation based Neo4J usage.
* Event based REST service design, exposing calls to Neo4J.
* Use of HATEOAS principles.
* Integration testing of REST calls using RestAssuredMockMVC.
* General Mockito usage.
* Domain integration testing of property files and system properties using
AbstractTestNGSpringContextTests and an implementation of ApplicationContextInitializer to mock
system properties.
* Pure java Spring configuration of MVC and Neo4J.
* Use of Spring Profiles to switch between configuration options.
* Use of Swagger for documentation of REST controller.

How to run
---------

* Neo4J running locally. Set correct host in test.properties at neo4j.host.
* gradle jettyRun to start application in root context. Application use [Gretty](https://github.com/akhikhl/gretty) to
launch jetty8, since the default Gradle jetty plugin is too old.
* The environment property spring.active.profiles needs to be set with the value "test" if running without gradle.



Neo4J
-----

The choice of using a graph database felt natural, since the whole concept is about modelling relationships.
Graph databases are very useful when dealing with large amounts of highly connected data, like social networks,
but here are a few other examples:

* Collaboration programs
* Configuration Management
* Geo-Spatial applications
* Impact Analysis
* Master Data Management
* Network Management
* Product Line Management
* Recommendation Engines

You usually start by whiteboarding the data in the domain to be modelled and what you draw ends up being what you graph.
Relationship types are completely defined by the application and can be created and modified as appropriate.

This application ended up with a very simple model:
(resourceA) - [:DEPENDS_ON] -> (resourceB)
With the relationship having different types, currently defined in [DependencyType.java](https://github.com/Lorkster/work-find-out/blob/master/Neo4JSystemDependencyApplication/src/main/java/com/findout/dependency/domain/DependencyType.java).

General design
--------------

The general design is broken into several layers, although all kept within a single project for simplicity.

The outmost layer is composed of two controllers:
* [ResourceQueryController.java](https://github.com/Lorkster/work-find-out/blob/master/Neo4JSystemDependencyApplication/src/main/java/com/findout/dependency/rest/controller/ResourceQueryController.java)
* [ResourceCommandController.java](https://github.com/Lorkster/work-find-out/blob/master/Neo4JSystemDependencyApplication/src/main/java/com/findout/dependency/rest/controller/ResourceCommandController.java)

These two expose the REST interface of the application, with the query controller exposing only read methods. The controllers
communicate with an event based design with [ResourceRestService.java](https://github.com/Lorkster/work-find-out/blob/master/Neo4JSystemDependencyApplication/src/main/java/com/findout/dependency/service/ResourceRestService.java)
, the events are there to decouple the REST interface from actual implementation logic.

The two controllers translate between the external domain and the relevant events. The external domain contains,
in addition to domain data, relevant HATEOAS information. As an example a specific resource contains links to any other
resources it has a direct dependency on. This is facilitated by extending the ResourceSupport, defined in
 [spring-hateoas](http://projects.spring.io/spring-hateoas/), class in [RestResource.java](https://github.com/Lorkster/work-find-out/blob/master/Neo4JSystemDependencyApplication/src/main/java/com/findout/dependency/rest/domain/RestResource.java).

The ResourceRestService in its turn communicates with [ResourceService.java](https://github.com/Lorkster/work-find-out/blob/master/Neo4JSystemDependencyApplication/src/main/java/com/findout/dependency/service/ResourceService.java)
which contains the actual logic for interacting with Neo4J. This last service might be unnecessary and could have been folded
into the ResourceRestService, but my idea was that one might want to break it out into, for example, a separate Hessian
service for performance reasons.

Currently the ResourceRestService basically maps from events to internal domain objects, calls the ResourceService, and
wraps and response in the correct events.

I also need to mention [RestResourceList.java](https://github.com/Lorkster/work-find-out/blob/master/Neo4JSystemDependencyApplication/src/main/java/com/findout/dependency/rest/domain/RestResourceList.java).
The sole purpose of this class is to handle the behaviour of jackson serializing Java POJOs to XML. Wrapping the List<RestResource>
in a base object makes sure that both XML and JSON serialization works as expected.


Spring Data
-----------

The domain modelling is completely done in Java using annotations from [spring-data-neo4j](http://projects.spring.io/spring-data-neo4j/).
[Resource.java](https://github.com/Lorkster/work-find-out/blob/master/Neo4JSystemDependencyApplication/src/main/java/com/findout/dependency/domain/Resource.java)
contains the base entity node model and [Dependency.java](https://github.com/Lorkster/work-find-out/blob/master/Neo4JSystemDependencyApplication/src/main/java/com/findout/dependency/domain/Dependency.java)
the relationship.

As illustrated by the classes the definition of nodes, relationships and their properties is very simple using annotations.
In Resource.java @NodeEntity defines an entity node, @GraphId defines a default unique id and @Index any fields that need to be indexed
due to search use cases. @RelatedToVia(type = "DEPENDS_ON") defines this entitys out going relationships and is matched by
the type defined in Dependency.java.

In Dependency.java @RelationshipEntity(type = "DEPENDS_ON") defines a relationship, once again
@GraphId is used for a unique key and @StartNode and @EndNode to define the entities involved in the relationship.

Currently only a few basic fields are defined to illustrate the functionality, but if any new fields are needed you
simply add them to the respective classes.

Spring Profiles and configuration
---------------------------------

The application is runnable without any xml configuration and only keeps a couple of properties externally, in test.properties,
for demonstration purposes. Instead it makes use of Spring profiles, @Configuration annotations and an implementation of
WebApplicationInitializer for launching of the web application. Having the environment property spring.active.profiles
set to "test" or "dev" is needed when running.

The configuration is split into two main classes, with a third one to demonstrate profile switching and running Neo4J
in memory:
* [MVCConfig.java](https://github.com/Lorkster/work-find-out/blob/master/Neo4JSystemDependencyApplication/src/main/java/com/findout/dependency/config/MVCConfig.java)
* [Neo4JTestConfig.java](https://github.com/Lorkster/work-find-out/blob/master/Neo4JSystemDependencyApplication/src/main/java/com/findout/dependency/config/Neo4jTestConfig.java)
* [Neo4JDevConfig.java](https://github.com/Lorkster/work-find-out/blob/master/Neo4JSystemDependencyApplication/src/test/java/com/findout/dependency/config/Neo4JDevConfig.java)

MVCConfig.java enables basic Spring MVC functionality as well as component scanning for annotations. Neo4jTestConfig.java
enables everything necessary for communicating with Neo4J. It's specifically connected to the Spring profile "test" and
reads the Neo4J host from ${spring.active.profiles}.properties.

The Spring profile "test" uses a Neo4J instance running on localhost, while "dev" instead initializes an in memory instance.

Tests
-----

I've put my focus on integration testing using Springs MockMVC for domain integration and functional tests (since loading
the complete Spring container was important for those) and RestAssuredMockMVC for the others.

[RestAssured](https://code.google.com/p/rest-assured/) is in my opinion an easier way to test and validate REST services,
although not covering all the functionality present in MockMvc.

The tests are broken down into the different operations exposed. For example [ViewResourceIntegrationTest.java](https://github.com/Lorkster/work-find-out/blob/master/Neo4JSystemDependencyApplication/src/test/java/com/findout/dependency/rest/controller/ViewResourceIntegrationTest.java)
which contains tests of all the read operations and validates generated JSON. [ViewResourceXmlIntegrationTest.java](https://github.com/Lorkster/work-find-out/blob/master/Neo4JSystemDependencyApplication/src/test/java/com/findout/dependency/rest/controller/ViewResourceXmlIntegrationTest.java)
tests and validates XML responses. Mockito is used to handle all calls backwards to the ResourceRestService and the needed
responses.

[RestDomainIntegrationTest.java](https://github.com/Lorkster/work-find-out/blob/master/Neo4JSystemDependencyApplication/src/test/java/com/findout/dependency/rest/RestDomainIntegrationTest.java)
tests the Spring context correctly loads property classes. It also demonstrates how to mock environment properties using
initializers and ApplicationContextInitializer. It simply initializes a complete Spring context and validates that a call
to get all resources returns ok.

[RestFunctionalTest.java](https://github.com/Lorkster/work-find-out/blob/master/Neo4JSystemDependencyApplication/src/test/java/com/findout/dependency/rest/RestFunctionalTest.java)
assumes that the application is running at the root of http://localhost:8080 and that some resources with relations is
present in Neo4J.

No pure unit tests are written at this time.

Swagger
-------

Documentation of the REST controllers make use of [Spring Swagger MVC](https://github.com/martypitt/swagger-springmvc) annotations, with a full annotation example used on getResourceDetails in
[ResourceQueryController.java](https://github.com/Lorkster/work-find-out/blob/master/Neo4JSystemDependencyApplication/src/main/java/com/findout/dependency/rest/controller/ResourceQueryController.java).
The Swagger documentation can be accessed by:
* gradle jettyRun
* Browse to [http://localhost:8080/index.html](http://localhost:8080/index.html)
