swagger-jaxb
============

JAXB XJC Plugin for automatically adding annotations from Swagger to generated classes from an XSD

Tests run in separate project, see here for the code https://github.com/redlab/swagger-jaxb-tck

Usage
============
* REQUIRE Java 8 or higher! 
* build the plugin with maven
* install it in your local repo
* add the plugin to your classpath and use -swaggify on your jaxb command line or configure it i your pom
or
* add sonatype snapshot repository to your repo manager. ( post an issue if you really want dev version in Maven Central )
 
 
 use with org.jvnet.jaxb2.maven2 maven-jaxb2-plugin
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
							<version>1.5</version>
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
=========== 
use with org.codehaus.mojo   jaxb2-maven-plugin 

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
                        <version>1.5</version>
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

