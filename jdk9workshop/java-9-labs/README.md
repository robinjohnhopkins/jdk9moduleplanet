# java-9-labs

Sequence of tasks to do after the code update:

```console
sudo rm -R classes/*
```

```console
javac -d classes -classpath lib/jackson-core-2.9.5.jar:lib/jackson-databind-2.9.5.jar:lib/jackson-annotations-2.9.0.jar $(find src/ -name '*.java')
```

```console
jar --create --file lib/org.tweetsum.jar -C classes .
```

```console
jar tvf lib/org.tweetsum.jar
```

```console
java -classpath lib/jackson-core-2.9.5.jar:lib/jackson-databind-2.9.5.jar:lib/jackson-annotations-2.9.0.jar:lib/org.tweetsum.jar org.tweetsum.Main < tweets.json
```

```console
jdeps --jdk-internals lib/tweetsum.jar
```

```console
jdeps -s -classpath lib/jackson-core-2.9.5.jar:lib/jackson-databind-2.9.5.jar:lib/jackson-annotations-2.9.0.jar lib/org.tweetsum.jar
```

```console
javac -d mods --module-path lib --module-source-path src -m org.tweetsum
```

```console
jar --create --file lib/org.tweetsum.jar --main-class org.tweetsum.Main -C mods/org.tweetsum .
```

```console
jar tvf lib/org.tweetsum.jar
```
