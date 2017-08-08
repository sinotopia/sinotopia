Plugin Documentation
Goals available for this plugin:

Goal	Report?	Description

migrations:bootstrap	No	Goal which execute the ibatis migration bootstrap command.
migrations:check	No	Goal which check the presence of pending migration.
migrations:down	No	Goal which execute the ibatis migration status command.
migrations:help	No	Display help information on migrations-maven-plugin.
Call mvn migration:help -Ddetail=true -Dgoal=<goal-name> to display parameter details.
migrations:init	No	Goal which executes the MyBatis migration init command. Init command creates a new migrate repository into 'repository' location.
migrations:new	No	Goal which executes the ibatis migration new command.
migrations:pending	No	Goal which execute the ibatis migration pending command.
migrations:script	No	Goal which executes the ibatis migration script command.
migrations:status	No	Goal which execute the ibatis migration status command.
migrations:status-report	Yes	Extends AbstractMavenReport. Class to generate a Maven report.
migrations:up	No	Goal which execute the ibatis migration status command.
migrations:version	No	Goal which execute the ibatis migration version command.
System Requirements
The following specifies the minimum requirements to run this Maven plugin:

Maven	2.0
JDK	1.6
Memory	No minimum requirement.
Disk Space	No minimum requirement.
Usage
You should specify the version in your project's plugin configuration:

<project>
  ...
  <build>
    <!-- To define the plugin version in your parent POM -->
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>org.mybatis.maven</groupId>
          <artifactId>migrations-maven-plugin</artifactId>
          <version>1.1.3</version>
        </plugin>
        ...
      </plugins>
    </pluginManagement>
    <!-- To use the plugin goals in your POM or parent POM -->
    <plugins>
      <plugin>
        <groupId>org.mybatis.maven</groupId>
        <artifactId>migrations-maven-plugin</artifactId>
        <version>1.1.3</version>
      </plugin>
      ...
    </plugins>
  </build>
  ...
  <!-- To use the report goals in your POM or parent POM -->
  <reporting>
    <plugins>
      <plugin>
        <groupId>org.mybatis.maven</groupId>
        <artifactId>migrations-maven-plugin</artifactId>
        <version>1.1.3</version>
      </plugin>
      ...
    </plugins>
  </reporting>
  ...
</project>
For more information, see "Guide to Configuring Plug-ins"