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

#### provides…with
The provides…with directive indicates that a module provides a service implementation. 
The module is therefore a service provider. After the provides part, the interface or 
abstract class is listed, after the with part, the implementation class is listed.

Before continue reading this paragraph, it is a good thing to revise the concept of services.

articles:

Java 9 Module Services
https://dzone.com/articles/java-9-module-services

The Oracle Java trail: The extension mechanism
https://docs.oracle.com/javase/tutorial/ext/index.html


### Create the Service Provider Interface


workspace/jdk9moduleplanet/serviceprovider1/src/main/java/module-info.java
```
module serviceprovider1 {
    requires serviceproviderinterface;
    provides com.test.serviceproviderinterface.spi.ServiceProviderInterface with com.test.serviceprovider1.ServiceProvider1;
}
```

workspace/jdk9moduleplanet/serviceproviderinterface/src/main/java/module-info.java
```
module serviceproviderinterface {
    exports com.test.serviceproviderinterface.spi;
    exports com.test.serviceproviderinterface;
    uses com.test.serviceproviderinterface.spi.ServiceProviderInterface;
}
```

workspace/jdk9moduleplanet/jpmshello/src/main/java/module-info.java
```
module com.test.jdk9moduleplanet {
    requires java.xml;
    requires jpmshi;
    requires serviceproviderinterface;
}
```

run
```
java --module-path jpmshello/target/jpmshello-1.0-SNAPSHOT.jar:jpmshi/target/jpmshi-1.0-SNAPSHOT.jar:./serviceproviderinterface/target/serviceproviderinterface-1.0-SNAPSHOT.jar:./serviceprovider1/target/serviceprovider1-1.0-SNAPSHOT.jar --module com.test.jdk9moduleplanet/com.test.jpmsdoobry.Doobry wide
                                                                        Hello, wide world!
The XML namespace prefix is: xml
Hi Modules!
Hi Modules!
public java.lang.String com.test.jpmshi.HiModules.getHi() is method: getHi
Hi Opens Directive!
This is Service Provider 1
```



### Add second Service Provider

To conclude with, we add a second Service Provider ServiceProvider2 
identical to ServiceProvider1. The only thing we will change, is to 
make the module-info.java of ServiceProvider2 a bit more readable by 
using import statements.

workspace/jdk9moduleplanet/serviceproviderinterface/src/main/java/module-info.java
```
import com.test.serviceproviderinterface.spi.ServiceProviderInterface;
import com.test.serviceprovider2.ServiceProvider2;

module serviceprovider2 {
    requires serviceproviderinterface;
    provides ServiceProviderInterface with ServiceProvider2;
}
```

run - IMPORTANT - the only way the second provider is picked up
is by ADDING to --module-path in following invocation.
It is NOT NEEDED as a dependency of Doobry POM!
i.e. The service implementation is picked up and used purely through interface
```
java --module-path jpmshello/target/jpmshello-1.0-SNAPSHOT.jar:jpmshi/target/jpmshi-1.0-SNAPSHOT.jar:./serviceproviderinterface/target/serviceproviderinterface-1.0-SNAPSHOT.jar:./serviceprovider1/target/serviceprovider1-1.0-SNAPSHOT.jar:./serviceprovider2/target/serviceprovider2-1.0-SNAPSHOT.jar  --module com.test.jdk9moduleplanet/com.test.jpmsdoobry.Doobry wide
                                                                        Hello, wide world!
The XML namespace prefix is: xml
Hi Modules!
Hi Modules!
public java.lang.String com.test.jpmshi.HiModules.getHi() is method: getHi
Hi Opens Directive!
This is Service Provider 2
This is Service Provider 1
```

### Summary
In this post the module directives were explained and used in several examples.


### POST ADDITION

https://github.com/robinjohnhopkins/jdk9moduleplanet/tree/master/jdk9workshop

jdk9workshop/README.md

contains additional jdk 9 modules work done prior to this one 
with some additional cool stuff like creating small
java deployments with only specific modules included
- AWESOME for docker and cloud deployments!


## JDK 9 ProcessHandle identifies and provides control of native processes.

### start process and kill it

```
java -cp ./process-api-httpclient/target/process-api-httpclient-1.0-SNAPSHOT.jar com/process/api/KillChildProcess
KillChildProcess demo - Press enter to continue [user: Optional[<name>], cmd: /bin/bash, args: [-c, while true; do sleep 1; done], startTime: Optional[2020-04-15T11:37:40.978Z]]

Killing process
Press enter to finish
Process 94301 was killed
```


### search all processes for a string param and kill matches - java pkill

```
java -cp ./process-api-httpclient/target/process-api-httpclient-1.0-SNAPSHOT.jar com/process/api/KillOtherProcess Meld
searching process to kill containing Meld
[user: Optional[robinjohnhopkins], cmd: /Applications/Meld.app/Contents/MacOS/Meld, startTime: Optional[2020-04-15T12:08:23.073Z]]
Meld was killed by Java!
Shutdown: true
```

### list processes

```
java -cp ./process-api-httpclient/target/process-api-httpclient-1.0-SNAPSHOT.jar com/process/api/ListProcesses
Started at: 2020-04-02T08:09:15.414Z, Command: /System/Library/CoreServices/loginwindow.app/Contents/MacOS/loginwindow
Started at: 2020-04-02T08:09:42.125Z, Command: /System/Library/Frameworks/LocalAuthentication.framework/Support/coreauthd
Started at: 2020-04-02T08:09:42.142Z, Command: /usr/sbin/cfprefsd
Started at: 2020-04-02T08:09:42.430Z, Command: /usr/libexec/UserEventAgent
```

### new way to access process info - parent pid, this pid
java -cp ./process-api-httpclient/target/process-api-httpclient-1.0-SNAPSHOT.jar com/process/api/ProcessId
{ pidOld: 15085, pidNew: 15085 }
parentPid (IDE): 54518

### jdk.incubator.httpclient - forget this - see java 11
https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html

### stack walker 

```
java -cp ./process-api-httpclient/target/process-api-httpclient-1.0-SNAPSHOT.jar com/stack/StackWalkerDemo
com.stack.StackWalkerDemo.method4(StackWalkerDemo.java:27)
com.stack.StackWalkerDemo.method3(StackWalkerDemo.java:21)
com.stack.StackWalkerDemo.method2(StackWalkerDemo.java:17)
com.stack.StackWalkerDemo.method1(StackWalkerDemo.java:13)
com.stack.StackWalkerDemo.main(StackWalkerDemo.java:9)
29
21
17
13
9
```

### other demos

```
java -cp ./process-api-httpclient/target/process-api-httpclient-1.0-SNAPSHOT.jar com/langandlib/Testrun
```

