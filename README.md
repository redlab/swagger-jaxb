swagger-jaxb
============

JAXB XJC Plugin for automatically adding annotations from Swagger to generated classes from an XSD

[![Develop  Build Status](https://travis-ci.com/redlab/swagger-jaxb.svg?branch=develop)](https://travis-ci.com/redlab/swagger-jaxb)
[![Master  Build Status](https://travis-ci.com/redlab/swagger-jaxb.svg?branch=master)](https://travis-ci.com/redlab/swagger-jaxb)

Tests run in separate project, see here for the code https://github.com/redlab/swagger-jaxb-tck

Usage
============

* REQUIRE Java 8 (or higher!? not yet tested)
* Get it from maven central.
* add the plugin to your classpath and use -swaggify on your jaxb command line or configure it in your pom.

for the dev version:

* build the plugin with maven3
* install it in your local repo
* or add sonatype snapshot repository to your repo manager.
 
 
#### use with org.jvnet.jaxb2.maven2 maven-jaxb2-plugin
 ```
 	<build>
		<plugins>
			<plugin>
				<groupId>org.jvnet.jaxb2.maven2</groupId>
				<artifactId>maven-jaxb2-plugin</artifactId>
				<version>0.13.2</version>
				<executions>
					<execution>
						<goals>
							<goal>generate</goal>
						</goals>
					</execution>
				</executions>
				<configuration>
					<schemaIncludes>
						<schemaInclude>**/*.xsd</schemaInclude>
					</schemaIncludes>
					<strict>true</strict>
					<verbose>true</verbose>
					<extension>true</extension>
					<removeOldOutput>true</removeOldOutput>
					<args>
						<arguments>-swaggerify</arguments>
					</args>
					<plugins>
						<plugin>
							<groupId>be.redlab.jaxb</groupId>
							<artifactId>swagger-jaxb</artifactId>
							<version>1.6</version>
						</plugin>
					</plugins>
					<dependencies>
						<dependency>
							<groupId>io.swagger</groupId>
							<artifactId>swagger-annotations</artifactId>
							<version>1.5.12</version>
						</dependency>
					</dependencies>
				</configuration>
			</plugin>
		</plugins>
	</build>
	<dependencies>
		<dependency>
			<groupId>io.swagger</groupId>
			<artifactId>swagger-annotations</artifactId>
			<version>1.5.12</version>
		</dependency>
	</dependencies>
 
``` 

#### use with org.codehaus.mojo   jaxb2-maven-plugin 

```
    <build>
    <pluginManagement>
        <plugins>
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>jaxb2-maven-plugin</artifactId>
                <version>2.3</version>
                <dependencies>
                    <dependency>
                        <groupId>be.redlab.jaxb</groupId>
                        <artifactId>swagger-jaxb</artifactId>
                        <version>1.6</version>
                    </dependency>
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
                        <version>2.2.11</version>
                    </dependency>
                    <dependency>
                        <groupId>com.sun.xml.bind</groupId>
                        <artifactId>jaxb-core</artifactId>
                        <version>2.2.11</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </pluginManagement>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>jaxb2-maven-plugin</artifactId>
            <version>2.3</version>
            <executions>
                    <execution>
                        <id>internal.generate</id>
                        <goals>
                            <goal>xjc</goal>
                        </goals>
                        <configuration>
                            <arguments>-swaggerify</arguments>
                            <clearOutputDir>true</clearOutputDir>
                            <packageName>be.redlab.jaxb.swagger.generated.model</packageName>
                            <sources>
                                <source>${project.basedir}/src/main/xsd/schema</source>
                            </sources>
                        </configuration>
                    </execution>
            </executions>
        </plugin>
	</plugins>

    <dependencies>
        <dependency>
            <groupId>io.swagger</groupId>
            <artifactId>swagger-annotations</artifactId>
            <version>1.5.12</version>
        </dependency>
    </dependencies>
```
Also you can use plugin with Gradle
```
configurations {
    xjcConf
}

    xjcConf 'com.sun.xml.bind:jaxb-xjc:2.2.6'
    xjcConf 'com.sun.xml.bind:jaxb-impl:2.2.6'
    xjcConf 'javax.xml.bind:jaxb-api:2.2.6'
    xjcConf 'org.jvnet.jaxb2_commons:jaxb2-basics:1.11.1'
    xjcConf 'org.jvnet.jaxb2_commons:jaxb2-basics-runtime:1.11.1'
    xjcConf 'org.jvnet.jaxb2_commons:jaxb2-basics-tools:1.11.1'
    xjcConf 'org.jvnet.jaxb2_commons:jaxb2-basics-ant:1.11.1'
    xjcConf 'org.jvnet.jaxb2_commons:jaxb2-basics-annotate:1.0.3'    
    
    xjcConf('be.redlab.jaxb:swagger-jaxb:1.5') {
        exclude group: 'com.sun.xml.bind'
        exclude group: 'javax.xml.bind'
    }
}

task generateClassesFromXsd {
    doLast {
        ant.taskdef(
                name: 'antXjc',
                classname: 'org.jvnet.jaxb2_commons.xjc.XJC2Task',
                classpath: configurations.xjcConf.asPath
        )

        System.setProperty('javax.xml.accessExternalSchema', 'file')

        ant.antXjc(
                destdir: 'src/main/java',
                binding: 'src/main/resources/xsd/binding.xjb',
                extension: 'true') {
            arg(value: '-swaggerify')
            schema(dir: 'src/main/resources/xsd') {
                include(name: '*.xsd')
            }
        }
    }
}
```

Kudos to other committers: 

* [JohneDoe](https://github.com/JohneDoe) 
* [Andrey Grigorov](https://github.com/peneksglazami)
