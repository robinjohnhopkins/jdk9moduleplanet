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
