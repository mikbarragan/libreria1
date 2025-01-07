# ls-java-50059982-core: Core de los microservicios
[![build status](https://sonar.alm.europe.cloudcenter.corp/sonar/api/project_badges/measure?project=SANES%3Asanes-adn360%3ASUA%3Als-java-50059982-core&metric=alert_status)](https://sonar.alm.europe.cloudcenter.corp/sonar/dashboard?id=SANES%3Asanes-adn360%3ASUA:ls-java-50059982-core)
[![coverage report](https://sonar.alm.europe.cloudcenter.corp/sonar/api/project_badges/measure?project=SANES%3Asanes-adn360%3ASUA%3Als-java-50059982-core&metric=coverage)](https://sonar.alm.europe.cloudcenter.corp/sonar/dashboard?id=SANES%3Asanes-adn360%3ASUA:ls-java-50059982-core)

Core library of all ADN 360 microservices.
* Logger aspect.
* Orika converter.
* Default exception controller.
* Common models.
* Confidentiality services.

## Getting Started
### Prerequisites

To config a development environment, we must follow these [steps](https://confluence.ci.gsnet.corp/display/ADN360PORTAL/%5BMS-BACK%5D+Entorno+de+desarrollo)

```
[MS-BACK] Entorno de desarrollo
```

### Installing

To use the library, just add the dependency in the file pom

```
    <dependencies>
        <dependency>
            <groupId>es.santander.adn360</groupId>
            <artifactId>core</artifactId>
            <version>${core.version}</version>
        </dependency>
        ...
    </dependencies>
```

## Built With

* [Nexus Repository Manager](https://nexus.alm.gsnetcloud.corp/) - Maven Dependency Management

## Versioning

For the versions available, see the [tags on this repository](https://github.alm.europe.cloudcenter.corp/sanes-adn360/ls-java-50059982-core/tags). 

## Authors

* **Damian Junquera** - [@XI319166](https://gitlab.alm.gsnetcloud.corp/XI319166)
* **David Palomar** - [@XI330240](https://gitlab.alm.gsnetcloud.corp/XI330240)
* **Diego Casanova** - [@XI330112](https://gitlab.alm.gsnetcloud.corp/XI330112)
* **Javier Moreno** - [@XI321781](https://gitlab.alm.gsnetcloud.corp/XI321781)
* **Luis Ruzafa** - [@XI312225](https://gitlab.alm.gsnetcloud.corp/XI312225)
* **Misael Suriel** - [@XI330281](https://gitlab.alm.gsnetcloud.corp/XI330281)

See also the list of [contributors](https://github.alm.europe.cloudcenter.corp/sanes-adn360/ls-java-50059982-core/graphs/development) who participated in this project.
