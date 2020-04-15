## JDK 9 using intellij


1. intellij help with modules guide
https://mydeveloperplanet.com/2018/01/24/java-9-modules-with-intellij-and-maven-part-2/

https://github.com/mydeveloperplanet/mymodulesplanet

Java Platform Module System (JPMS)


### parent.pom.one.child.module.pom

[1] as at 

Our next step is to move the HelloModules.java file and the module-info.java file to the newly created com.mydeveloperplanet.jpms module. After doing this, remove the root src directory.

is tagged as:

git tag parent.pom.one.child.module.pom

The only difference being that my naming is different and my Doobry class is partly 
content from a you tube module tutorial/presentation:

https://www.youtube.com/watch?v=C5yX-elG4w0

(which I might move into this git to have it in one place later)

run:

```
java --module-path jpmshello/target/jpmshello-1.0-SNAPSHOT.jar --module com.test.jdk9moduleplanet/com.test.jpmshi.Doobry wide
                                                                        Hello, wide world!
The XML namespace prefix is: xml
```


### maven-compiler-plugin 3.7.0 fails for modules

IN THEORY You need at least version 3.7.0 of the Maven compiler plugin to properly handle modules

IN PRACTICE I needed to specify 3.6.2!

```
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.6.2</version><!-- NB 3.7.0 errored here! -->
```

### Add second module

After adding second module and 

```
java --module-path jpmshello/target/jpmshello-1.0-SNAPSHOT.jar:jpmshi/target/jpmshi-1.0-SNAPSHOT.jar --module com.test.jdk9moduleplanet/com.test.jpmsdoobry.Doobry wide
                                                                        Hello, wide world!
The XML namespace prefix is: xml
Hi Modules!
```

### Java 9 Modules directives (part 3)
https://mydeveloperplanet.com/2018/02/07/java-9-modules-directives-part-3/

#### requires
The requires directive indicates that this module depends on another module. 

requires com.mydeveloperplanet.jpmshi;

The requires directive also knows two variants:

requires transitive <module name>: this means that any module that reads your module, 
implicitly also reads the transitive module. E.g. if your module contains a method 
which is public available and returns a type of another module. When transitive is not used, 
any module reading your module, would explicitly have to add the dependent module.

requires static <module name>: this is an optional dependency. The module is needed at 
compile time, but not at run time.


#### exports
The exports directive indicates which public types of the module’s package are accessible 
to other modules. 

exports com.mydeveloperplanet.jpmshi;

#### exports ... to
Also an exports…to directive exists. After the to keyword, it is possible to set the 
list of packages which are allowed to use the exported packages.

#### opens to allow reflection
The opens directive also indicates which public types of the module’s package are 
accessible to other modules. The difference with exports is that opens is not during 
compile time but only during run time, while exports is during compile time and during 
run time. The opens directive can typically be used when you want to allow other modules 
to use reflection for the types in the specified packages.


workspace/jdk9moduleplanet/jpmshi/src/main/java/module-info.java
```
module jpmshi {
    exports com.test.jpmshi;
    opens com.test.jpmsopens;
}
```


run showing use of reflection with open directive
```
java --module-path jpmshello/target/jpmshello-1.0-SNAPSHOT.jar:jpmshi/target/jpmshi-1.0-SNAPSHOT.jar --module com.test.jdk9moduleplanet/com.test.jpmsdoobry.Doobry wide
                                                                        Hello, wide world!
The XML namespace prefix is: xml
Hi Modules!
Hi Modules!
public java.lang.String com.test.jpmshi.HiModules.getHi() is method: getHi
Hi Opens Directive!
```

