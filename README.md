# WhoBrokeIt - detect the change that broke a JUnit Test #

## Introduction ##
WhoBrokeIt is a simple library to detect which VCS commit broke a particular unit test. Basically it hooks into the
JVM as a JavaAgent and logs class loading and execution during unit tests.

The following usecase describes its key features:

During JUnit test execution, for every successful unit test a log file is written as follows:

+ a.b.c.LoadedClass1;Rev122;user1@example.com
+ a.b.c.LoadedClass2;Rev15;user1@example.com

This informs us that the unit test touched the classes LoadedClass1 and LoadedClass2 in some way. Now, WhoBrokeIt
also logs the current revision of the Java source file and the user who did the last change on that file. This
is done by using a plugable VCS adapter. Currently, Git and SVN are supported.

Now, what happens if the test fails?

Well, then WhoBrokeIt checks the currently touched classes and the already written log file, which gives the
status of the test while it was ok. Then it simply logs the diff of the two logs, and we will see which change
and who broke the test.

## Configuration ##
As stated before, WhoBrokeIt is a simple JavaAgent. This Agent is configured using Java VM parameters the following way:

+ -javaagent:fullpathtoagent.jar
+ -Dwhobrokeit.projectDir=the directory where your maven pom.xml is located
+ -Dwhobrokeit.logDir=the directory where the log files should be written

WhoBrokeIt comes in different flavors. There is an agent.jar file for Maven and Git support, and there is another
agent.jar file for Maven and SVN support. You have to choose the right one for your project.

## Maven project structure ##
Currently, WhoBrokeIt just supports the standard maven project structure, such as src/main/java and src/test/java for
source file detection. It does not analyze the pom.xml or plugin configuration and generated source files such as JAXB artifacts.
It does also not support Multi-Module/Reactor projects at the moment. This will be implemented in a future version.

## SVN support ##
WhoBrokeIt uses SVNKit for retrieving version information. No native client is required.

## GIT support ##
WhoBrokeIt uses JGIT for retrieving version information. No native client is required.




