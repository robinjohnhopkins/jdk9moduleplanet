## JDK 9 

https://www.youtube.com/watch?v=C5yX-elG4w0

javac -d classes $(find src -name '*.java')
mkdir lib
jar --create --file lib/hello.jar -C classes .
java -cp lib/hello.jar org.openjdk.hello.hello wide
                                                                       Hello, wide world!

## non-modularised

```
tree
.
├── README.md
├── classes
│   └── org
│       └── openjdk
│           └── hello
│               └── hello.class
├── lib
│   └── hello.jar
└── src
    └── org
        └── openjdk
            └── hello
                └── hello.java

mv src hello
mkdir src
mv hello src

rm -rf classes
javac -d classes $(find src -name '*.java')

javap classes/module-info.class 
Compiled from "module-info.java"
module hello {
  requires java.base;
}

# javap command disassembles one or more class files.
javap -v classes/module-info.class      # verbose. 

jar --create --file lib/hello.jar -C classes .

java --module-path lib -m hello/org.openjdk.hello.hello foo
                                                                        Hello, foo world!

# specify main class in module
jar --create --file lib/hello.jar --main-class org.openjdk.hello.hello   -C classes .
java --module-path lib -m hello foo

# works with classpath (old school)
java -cp lib/* org.openjdk.hello.hello wonderful
                                                                   Hello, wonderful world!
```

## split out into separate module

```
tree src
src
├── hello
│   ├── module-info.java
│   └── org
│       └── openjdk
│           └── hello
│               └── hello.java
└── text
    ├── module-info.java
    └── org
        └── openjdk
            └── text
                └── Padder.java


rm -rf classes/
javac -d classes $(find src/text -name '*.java')
jar --create --file lib/text.jar -C classes .

rm -rf classes/
javac --module-path lib -d classes $(find src/hello -name '*.java')
jar --create --file lib/hello.jar --main-class org.openjdk.hello.hello   -C classes .
java --module-path lib -m hello foo
                                                                         Hello, foo world!
```

## info

jdk is built using jlink

Here are all the jmods used to build the jdk

/Library/Java/JavaVirtualMachines/jdk-10.jdk/Contents/Home/jmods/

```
the following builds a version of java that only includes the specified modules!

jlink --module-path /Library/Java/JavaVirtualMachines/jdk-10.jdk/Contents/Home/jmods/ --add-modules java.base --output jre 
 
du -sh jre
 37M	jre

./jre/bin/java --module-path lib -m hello foo
                                                                         Hello, foo world!

./jre/bin/java  --show-module-resolution   --module-path lib -m hello foo
root hello file:///Users/robinjohnhopkins/workspace/jdk9workshop/lib/hello.jar
hello requires text file:///Users/robinjohnhopkins/workspace/jdk9workshop/lib/text.jar
                                                                         Hello, foo world!


redirect stderr to stdout
./jre/bin/java -help 2>&1 | grep res                                               
```

## compile into jre including your modules

very cool

```
jlink --module-path $JAVA_HOME/jmods:lib  --add-modules hello --output jre2

jre2/bin/java --list-modules

hello
java.base@10
text

jre2/bin/java -m hello
                                                                             Hello, world!

create a launcher:

rm -rf jre2
jlink --module-path $JAVA_HOME/jmods:lib  --add-modules hello --launcher hello=hello  --output jre2
jre2/bin/hello 
                                                                             Hello, world!
ll jre2/bin/hello 
-rwxr-xr-x  1 robinjohnhopkins  staff  109 14 Apr 12:56 jre2/bin/hello

du -sh jre2/
 37M	jre2/

rm -rf jre2/
jlink --module-path $JAVA_HOME/jmods:lib  --add-modules hello --launcher hello=hello --compress 2 --strip-debug    --output jre2

jre2/bin/hello 
                                                                             Hello, world!

du -sh jre2/
 23M	jre2/

MUCH SMALLER - VERY COOL for DOCKER!
```

## Migrating to Modules by Mark Reinhold
https://www.youtube.com/watch?v=czhSo8rotC4

https://github.com/marcosnasp/java-9-labs

cloned into migrating-to-modules

cd migrating-to-modules

### step 1 check it works sans modules

javac -d classes -classpath lib/jackson-core-2.9.0.jar:lib/jackson-databind-2.9.0.jar:lib/jackson-annotations-2.9.0.jar $(find src/ -name '*.java'|grep -v module-info.java)

java -classpath lib/jackson-core-2.9.0.jar:lib/jackson-databind-2.9.0.jar:lib/jackson-annotations-2.9.0.jar:lib/org.tweetsum.jar org.tweetsum.Main < tweets.json

2011-04-04 00:48:36.0: RT @PostGradProblem: In preparation for the NFL lockout, I will be spending twice as much time analyzing my fantasy baseball team during ...


### step 2 

add java-9-labs/migrating-to-modules/src/org.tweetsum/module-info.java

```
module org.tweetsum {
    requires java.base;
    requires jackson.core;                  // refers to jackson-core-2.9.0.jar
    requires jackson.databind;
    requires jackson.annotations;           // this is using automatic modules
    requires java.sql;

    opens org.tweetsum to jackson.databind;
}
```

automatic modules take a non-conflicting jar on the classpath and adds each as a module.

jdeps can check if jar uses internal APIs

```
jdeps --jdk-internals lib/org.tweetsum.jar
```

phew - would have printed warnings


```
jdeps  lib/org.tweetsum.jar             # get summary

org.tweetsum.jar -> java.base
org.tweetsum.jar -> java.sql
org.tweetsum.jar -> not found
   org.tweetsum                                       -> com.fasterxml.jackson.annotation                   not found
   org.tweetsum                                       -> com.fasterxml.jackson.core.type                    not found
   org.tweetsum                                       -> com.fasterxml.jackson.databind                     not found
   org.tweetsum                                       -> java.io                                            java.base
   org.tweetsum                                       -> java.lang                                          java.base
   org.tweetsum                                       -> java.lang.invoke                                   java.base
   org.tweetsum                                       -> java.sql                                           java.sql
   org.tweetsum                                       -> java.util                                          java.base
   org.tweetsum                                       -> java.util.function                                 java.base
```

if requires java.sql; was missing - jdeps would have told us!
the not found is due to the jars not being found.

```
jdeps -classpath lib/jackson-core-2.9.0.jar:lib/jackson-databind-2.9.0.jar:lib/jackson-annotations-2.9.0.jar  lib/org.tweetsum.jar 
org.tweetsum.jar -> lib/jackson-annotations-2.9.0.jar
org.tweetsum.jar -> lib/jackson-core-2.9.0.jar
org.tweetsum.jar -> lib/jackson-databind-2.9.0.jar
org.tweetsum.jar -> java.base
org.tweetsum.jar -> java.sql
   org.tweetsum                                       -> com.fasterxml.jackson.annotation                   jackson-annotations-2.9.0.jar
   org.tweetsum                                       -> com.fasterxml.jackson.core.type                    jackson-core-2.9.0.jar
   org.tweetsum                                       -> com.fasterxml.jackson.databind                     jackson-databind-2.9.0.jar
   org.tweetsum                                       -> java.io                                            java.base
   org.tweetsum                                       -> java.lang                                          java.base
   org.tweetsum                                       -> java.lang.invoke                                   java.base
   org.tweetsum                                       -> java.sql                                           java.sql
   org.tweetsum                                       -> java.util                                          java.base
   org.tweetsum                                       -> java.util.function                                 java.base
```

compile

```
javac -d mods --module-path lib --module-source-path src -m org.tweetsum
```

get some info
```
javap mods/org.tweetsum/module-info.class 
Compiled from "module-info.java"
module org.tweetsum {
  requires java.base;
  requires jackson.core;
  requires jackson.databind;
  requires jackson.annotations;
  requires java.sql;
  opens org.tweetsum to jackson.databind;  // << NB
}
```

NB 'opens org.tweetsum to jackson.databind;'  is required to allow reflection to be used within jackson.databind jar.
NB you can alternatively put opens prefix before 'module' line 1.


create jar

```
jar --create --file lib/tweetsum.jar -C mods/org.tweetsum .
```

run:

```
java --module-path lib -m org.tweetsum/org.tweetsum.Main <tweets.json 

2011-04-04 00:48:36.0: RT @PostGradProblem: In preparation for the NFL lockout, I will be spending twice as much time analyzing my fantasy baseball team during ...
```

create small jre:

```
jlink --module-path $JAVA_HOME/jmods  --add-modules java.sql    --output jre
du -sh jre/
 48M	jre/
./jre/bin/java --list-modules
    java.base@10
    java.logging@10
    java.sql@10
    java.xml@10


now run but also show module resolution as you go

./jre/bin/java --show-module-resolution  --module-path lib -m org.tweetsum/org.tweetsum.Main <tweets.json 
root org.tweetsum file:///Users/robinjohnhopkins/workspace/jdk9workshop/java-9-labs/migrating-to-modules/lib/tweetsum.jar
org.tweetsum requires jackson.databind file:///Users/robinjohnhopkins/workspace/jdk9workshop/java-9-labs/migrating-to-modules/lib/jackson-databind-2.9.0.jar automatic
org.tweetsum requires jackson.annotations file:///Users/robinjohnhopkins/workspace/jdk9workshop/java-9-labs/migrating-to-modules/lib/jackson-annotations-2.9.0.jar automatic
org.tweetsum requires java.sql jrt:/java.sql
org.tweetsum requires jackson.core file:///Users/robinjohnhopkins/workspace/jdk9workshop/java-9-labs/migrating-to-modules/lib/jackson-core-2.9.0.jar automatic
java.sql requires java.xml jrt:/java.xml
java.sql requires java.logging jrt:/java.logging
java.base binds java.logging jrt:/java.logging
Apr 14, 2020 2:30:20 PM com.fasterxml.jackson.databind.ext.Java7Support <clinit>
WARNING: Unable to load JDK7 types (annotations, java.nio.file.Path): no Java7 support added

2011-04-04 00:48:36.0: RT @PostGradProblem: In preparation for the NFL lockout, I will be spending twice as much time analyzing my fantasy baseball team during ...
```

## migrate jackson jars

clean up

```
rm -rf classes/

compile as normal - non-modular

javac -d classes -classpath lib/jackson-core-2.9.0.jar:lib/jackson-databind-2.9.0.jar:lib/jackson-annotations-2.9.0.jar $(find src/ -name '*.java'|grep -v module-info.java)


tree classes/
classes/
└── org
    └── tweetsum
        ├── Main$1.class
        ├── Main.class
        └── Tweet.class
```

If you have src of the jackson jars and you wanted to migrate to modules:

possibly use jdeps to help with requires statements

```
jdeps -s lib/jackson-*
    jackson-annotations-2.9.0.jar -> java.base
    jackson-core-2.9.0.jar -> java.base
    jackson-databind-2.9.0.jar -> lib/jackson-annotations-2.9.0.jar
    jackson-databind-2.9.0.jar -> lib/jackson-core-2.9.0.jar
    jackson-databind-2.9.0.jar -> java.base
    jackson-databind-2.9.0.jar -> java.desktop
    jackson-databind-2.9.0.jar -> java.logging
    jackson-databind-2.9.0.jar -> java.sql
    jackson-databind-2.9.0.jar -> java.xml


get help writing module-info.java

jdeps --generate-module-info src  lib/jackson-core-2.9.0.jar 
writing to src/jackson.core/module-info.java

cat src/jackson.core/module-info.java

    module jackson.core {
        exports com.fasterxml.jackson.core;
        exports com.fasterxml.jackson.core.async;
        exports com.fasterxml.jackson.core.base;
        exports com.fasterxml.jackson.core.filter;
        exports com.fasterxml.jackson.core.format;
        exports com.fasterxml.jackson.core.io;
        exports com.fasterxml.jackson.core.json;
        exports com.fasterxml.jackson.core.json.async;
        exports com.fasterxml.jackson.core.sym;
        exports com.fasterxml.jackson.core.type;
        exports com.fasterxml.jackson.core.util;

        provides com.fasterxml.jackson.core.JsonFactory with
            com.fasterxml.jackson.core.JsonFactory;
    }
```

NB: provides bit shows that services are understood.

useful describe option:

```
jar --describe-module --file lib/tweetsum.jar 
org.tweetsum jar:file:///Users/robinjohnhopkins/workspace/jdk9workshop/java-9-labs/migrating-to-modules/lib/tweetsum.jar/!module-info.class
requires jackson.annotations
requires jackson.core
requires jackson.databind
requires java.base
requires java.sql
qualified opens org.tweetsum to jackson.databind
```

### transitive

When auto generating non-core annotations for some jackson jars

```
requires transitive jackson.annotations;
```

this establishes a one step shortcut to types in this route.

also      exports ...*.impl      were commented out

So, say you have created module jars for the jackson sw, then try to use with a non-modularised jar:

```
java --module-path mlib -cp org.tweetsum.jar < tweets.json

ERROR: A JNI error has occurred .... NoClassDefFoundError: com/fasterxml/jackson/core/type/TypeReference

NB missing --add-modules:

java --module-path mlib -cp org.tweetsum.jar --add-modules jackson.core,jackson.databind,jackson.annotations  < tweets.json
```

Every class/type is in a module.
There is a special 'unnamed-module' - effectively requires evry module and exports everything.

