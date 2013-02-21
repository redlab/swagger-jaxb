swagger-jaxb
============

JAXB XJC Plugin for automatically adding annotations from Swagger to generated classes from an XSD

Please note this plugin is not fully finished and is in development phase !! Feel free to help out and send pull requests.

[![Build Status](https://redlab.ci.cloudbees.com/job/swagger-jaxb/badge/icon)](https://redlab.ci.cloudbees.com/job/swagger-jaxb/)
Tests run in separate project [![Build Status](https://redlab.ci.cloudbees.com/job/swagger-jaxb-tck/badge/icon)](https://redlab.ci.cloudbees.com/job/swagger-jaxb-tck/) see here for the code [https://github.com/redlab/swagger-jaxb-tck swagger-jaxb-tck]

Usage
============
1.
* build the plugin with maven
* install it in your local repo
* add the plugin to your classpath and use -swaggify on your jaxb command line or configure it i your pom

 
Use with jaxb2-maven-plugin

```
    <plugin>
        <groupId>org.codehaus.mojo</groupId>
		<artifactId>jaxb2-maven-plugin</artifactId>
		<version>1.5</version>
	  	<executions>
	     		<execution>
				<id>internal.generate</id>
				<goals>
					<goal>xjc</goal>
				</goals>
				<configuration>
				 	<arguments>-swaggerify</arguments>
					<clearOutputDir>true</clearOutputDir>
					<schemaDirectory>${project.basedir}/src/main/resources/xsd//api</schemaDirectory>
					<packageName>com.example.api.model</packageName>
					<staleFile>${project.build.directory}/generated-sources/jaxb/.api.internal</staleFile>
				</configuration>
			</execution>
		</executions>
		<dependencies>
			<dependency>
				<groupId>javax.xml.parsers</groupId>
				<artifactId>jaxp-api</artifactId>
				<version>1.4.5</version>
			</dependency>
			<dependency>
				<groupId>com.sun.xml.parsers</groupId>
				<artifactId>jaxp-ri</artifactId>
				<version>1.4.5</version>
			</dependency>
			<dependency>
				<groupId>com.sun.xml.bind</groupId>
				<artifactId>jaxb-xjc</artifactId>
				<version>2.2.7-b53</version>
			</dependency>
			<dependency>
				<groupId>com.sun.xml.bind</groupId>
				<artifactId>jaxb-impl</artifactId>
				<version>2.2.7-b53</version>
			</dependency>
			<dependency>
				<groupId>be.redlab.jaxb</groupId>
				<artifactId>swagger-jaxb</artifactId>
				<version>0.0.1-SNAPSHOT</version>
			</dependency>
		</dependencies>
	</plugin>
```

The plugin dependencies are needed until the JAXB2 plugin is updated to use the latest version of jaxb-xjc and jaxp. Otherwise the code generation will fail due to missing methods.
