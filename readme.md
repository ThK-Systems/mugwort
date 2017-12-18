### mugwort

provides some utilities for enterprise software development using spring, hibernate, servlets, xstream, java-bean-validation, ... 

## Summary

### ... for using Hibernate and Spring

*   **BaseRepository** \- A very mightfull basic repository to be used with spring and hibernate. (Can also be used with spring-data-jpa for custom repositories.)
*   **IdentifiedEntity** \- A basic class for persisted entities.
*   **BaseService** \- A spring base service, with support for manual transactions (and more to come)
*   **SecureEnvironment** \- Supports encrypted property values.
*   **ImmutableCheckInterceptor** - To make entities really immutable (and log and suppress attempts to modify them)

### ... for working with Servlets

*   **ServletUtils** \- Gets headers/cookies/parameters as string lists, gets dump, ...
*   **DumpFilter** \- Used to dump the servlet request.

### ... for extending XStream

*   **MapToAttributesConverter** \- Converts a map to a list of xml-attributes. The key of the Map becomes the name of the xml-attribute, and the value of the Map becomes the value of the xml-attribute.

###... for validating beans

*   **Occurence** \- The annotated list must not have more than one elements, where the specified field (getter) has the specified value
*   **OneNotEmpty** \- At least one of the given fields must be non-empty (for strings, collections and arrays) or non-null (for anything else).
*   **DependantNotEmpty** \- The value of the field 'fieldname' must not be empty, if the value of the file 'dependantField' is equal to the 'dependantValue'.
*   **Country** \- The annotated element must be a valid country code (ISO 3166-1 ALPHA-2 code)
*   **Currency** \- The annotated element must be a valid currency code (ISO 4217 ALPHA)
*   **Locale** \- The annotated element must be a valid locale code

... more to come ... 


## Documentation

Browse the [Javadoc of the latest version](https://www.thk-systems.de/content/oss/javadoc/mugwort/current/index.html){:target="_blank"}. 


## Installation

```xml
<dependency>
  <groupId>net.thk-systems.commons</groupId>
  <artifactId>mugwort</artifactId>
  <version>3.7.4</version>
</dependency>
```

You can browse [maven-central](http://search.maven.org/#artifactdetails|net.thk-systems.commons|mugwort|3.7.4|jar){:target="_blank"} to download the jar or other versions. 

_Note: mugwort comes with no maven dependencies to spring, hibernate, servlet-api, ... ._ 


## Changelog

**3.8.0 (unreleased)**
*   Updated dependencies

**3.7.0 / 3.7.1 / 3.7.2 / 3.7.3 / 3.7.4**

*   Updated dependencies
*   Bugfixes

**3.5.0 / 3.6.2**

*   Removed ValidatedByValidator
*   Added LocaleValidator

**3.4.0**

*   Added ValidatedByValidator

**3.3.0**

*   Added CurrencyValidator

**3.2.0**

*   Added CountryValidator

**3.1.0**

*   Added BaseService.runInTransaction

**3.0.0**

*   Added ConditionalNotEmptyValidator
*   Move to Java 8 (first without changing anything

**2.3.0**

*   Added OneNotEmptyValidator

**2.2.0**

*   Added ImmutableCheckInterceptor and @Immutable and @Mutable annotations
*   Added OccurenceValidator
*   Renamed ExtendedEnvironment to SecureEnvironment and added a 'merging' with 'default' environment

**2.1.0**

*   Added ExtendedEnvironment and JasyptTool

**2.0.0**

*   Released as 2.0.0 under new maven group id
