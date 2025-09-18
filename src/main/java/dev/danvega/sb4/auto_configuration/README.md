# Modular Auto-Configuration 

Modular Auto-Configuration: A major under-the-hood change in Boot 4 is the refactoring of its auto-configuration c
odebase into a more modular structure. Many auto-configurations have been split into separate modules 
(to avoid pulling in unwanted transitive dependencies). For those who prefer the simpler approach of Boot 3.x, 
a new spring-boot-autoconfigure-classic module bundles all auto-configurations without the new modular dependency 
filtering

This “classic” module provides compatibility by aggregating auto-config classes, while still allowing advanced 
users to pick and choose modules in a more fine-grained way.


```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-autoconfigure-classic</artifactId>
</dependency>
```

For example, to have the RestClient auto configured you have to pull this dependency in

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-restclient</artifactId>
</dependency>
```