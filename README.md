# WCM Filter Override

This will override the WCM Filter in the JVM and allow for the EDIT field to be enabled preventing the 404.

## Modules

The main parts of the template are:

* core: Java bundle containing all core functionality like OSGi services, listeners or schedulers, as well as component-related Java code such as servlets or request filters.
* ui.config: JCR package containing all the OSGi configs needed

## How to build

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install


## Maven settings

The project comes with the auto-public repository configured. To setup the repository in your Maven settings, refer to:

    http://helpx.adobe.com/experience-manager/kb/SetUpTheAdobeMavenRepository.html
