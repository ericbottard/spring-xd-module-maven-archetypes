spring-xd-processor-module-archetype
====================================

This is a maven archetype to create a Spring XD Processor Module project. Currently under development, this archetype is not available in any remote maven repositories. Additional archetypes are planned for source,sink, and job modules, or this may be enhanced to create any module type (further investigation is required here).

Installation
===
To install this in your local maven repository clone this repo, build it, and update the archetype catalog:

````
$ mvn clean install
$ mvn archetype:crawl
````

The last command updates `archetype-catalog.xml` in your local repository to make the archetype available.

Generate a Processor Module
===
Copy the following command, substituting your preferred `groupId`, `artifactId`, and `version`. This will create the Processor Module project in a directory named `$artifactId` under the directory from which you run this command.

````
$ mvn archetype:generate -DarchetypeGroupId=org.springframework.xd \
-DarchetypeArtifactId=spring-xd-processor-module-archetype \
-DarchetypeVersion=1.0.0.BUILD-SNAPSHOT \
-DgroupId=my.group -DartifactId=myModule -Dversion=1.0.0.BUILD-SNAPSHOT
````

The generated project is a working example processor module including configuration, unit tests, and unit integration tests. You can build and install, and test the module in Spring XD (requires the current 1.1.0.BUILD-SNAPSHOT or later).

````
$ mvn package
$ cp target/$artifactId.jar $XD_DIST_HOME/xd/modules/processor
````
where `$XD_DIST_HOME` is the root of your Spring XD installation. Start xd-singlenode and xd-shell in separate terminal windows. Use the shell to create a stream:

````
xd:> stream create test --definition "time | <your-module-name> --prefix=start --suffix=end | log" --deploy
````

You should see the output on the console log: 

````
15:09:01,839 1.1.0.SNAP  INFO task-scheduler-6 sink.test - start2014-10-31 15:09:01end
15:09:02,842 1.1.0.SNAP  INFO task-scheduler-6 sink.test - start2014-10-31 15:09:02end
15:09:03,848 1.1.0.SNAP  INFO task-scheduler-6 sink.test - start2014-10-31 15:09:03end
15:09:04,853 1.1.0.SNAP  INFO task-scheduler-6 sink.test - start2014-10-31 15:09:04end
15:09:05,855 1.1.0.SNAP  INFO task-scheduler-6 sink.test - start2014-10-31 15:09:05end
````

Use this project as a template to create a real processor. The generated processor does not use any custom spring beans or classes besides `ExampleModuleOptionsMetadata`. Any classes you add will be automatically included the module jar, actually it's a Spring Boot uberjar of sorts (not executable) which also packages any dependent libraries that you specify. The module project has a lot of Spring XD dependencies needed to run the tests, and to support module development. These are excluded from the uberjar since they are already included in the Spring XD classpath. Any dependent jars specific to your module must be added to the pom, as usual, but also explicitly declared in the project's assembly descriptor located in src/assembly to include it in the uberjar (and the module's class path). The assembly descriptor looks like [this](src/main/resources/archetype-resources/src/assembly/uber-jar.xml).

Good Luck!











