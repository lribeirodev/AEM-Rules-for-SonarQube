[<img src="assets/vml-logo.png" width=50% height=50%>](http://www.vml.com/poland)


![Builds Status](https://github.com/wttech/AEM-Rules-for-SonarQube/actions/workflows/build.yml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/Cognifide/AEM-Rules-for-SonarQube/badge.svg?branch=master)](https://coveralls.io/github/Cognifide/AEM-Rules-for-SonarQube?branch=master)
[<img src="https://rules.sonarsource.com/images/logos/SonarLint-black.svg" height="28" alt="Available in SonarLint">](https://www.sonarlint.org/)
[<img src="https://rules.sonarsource.com/images/logos/SonarCloud-black.svg" height="28" alt="Available in SonarCloud">](https://sonarcloud.io)
[<img src="https://rules.sonarsource.com/images/logos/SonarQube-black.svg" height="28" alt="Available in SonarQube">](https://www.sonarqube.org/)
# About AEM Rules for SonarQube

![AEM Rules for SonarQube](https://raw.githubusercontent.com/wttech/AEM-Rules-for-SonarQube/master/assets/logo.png)

## Purpose

As we all know, SonarQube is a great tool that helps us increase quality of our codebase. However, it does apply mainly to general Java issues. As we know, we can hurt ourselves much more doing AEM. [Adobe Experience Manager](https://experienceleague.adobe.com/en/docs) is a comprehensive content management platform solution for building websites, mobile apps and forms. This tool is intended to find common bugs and bad smells specific for AEM development. Documentation of each rule is available from SonarQube interface after plugin installation.

## Prerequisites

Each release has its own prerequisites section, for more information please check [releases page](https://github.com/wttech/AEM-Rules-for-SonarQube/releases).

## Installation

### Local docker image

Check the plugin locally with the prepared ```docker-compose.yml```. Just run these commands from the repo root to build the latest package and fire up a container.

```
mvn clean package
cd local-docker-image
docker-compose up -d
```

### Update Center

Go to your SonarQube instance administration console and open Update Center. Find AEM Rules for SonarQube plugin and click install!

### Manual

1. [Download](https://github.com/wttech/AEM-Rules-for-SonarQube/releases) `aemrules-x.y.jar` or build **AEM Rules for SonarQube** plugin.
2. Paste it into `sonarqube/extensions/plugins` directory.
3. Restart SonarQube.
4. Go to rules section and activate AEM rules in your profile.

## Usage

Use of the plugin does not differ much from regular SonarQube analysis. However, as rules are often tied to a certain AEM version and its components (Felix, Sling), we've introduced the `aemVersion` analysis property.

Each rule defines supported AEM version or version range. Most of the rules are universal.

SonarLint supports only AEM-JAVA rules. Currently SonarLint does not support custom languages.
Please refer to this [article](https://community.sonarsource.com/t/sonarlint-for-custom-languages/69904).

It was tried to add `HTL` rules to `web` language, but [sonar-html-plugin](https://github.com/SonarSource/sonar-html) is final and can not be extended.

### Running analysis

When running analysis, pass `sonarRunner.aemVersion` property with your AEM version. The format is as follows:

`sonarRunner.aemVersion=<MAJOR_VERSION>.<MINOR_VERSION>`

To avoid quality profiles collisions, the additional execution param has been added.

`-Dsonar.html.file.suffixes=.notexistingsuffix`

Running with Maven
```
mvn clean verify sonar:sonar \
    -Dsonar.projectKey={sonar_project_key} \
    -Dsonar.projectName='{sonar_project_name}' \
    -Dsonar.host.url=http://localhost:9000 \
    -Dsonar.token={sonar_project_token} \
    -DsonarRunner.aemVersion=6.5 \
    -Dsonar.html.file.suffixes=.notexistingsuffix
```

# Rule set

Below you will find descriptions of all rules available in **AEM Rules for SonarQube** plugin.

## AEM Good practices

- **AEM-1** Use predefined constant in annotation instead of hardcoded value.
  - Use constants available in AEM instead of repeating inline literals.

- **AEM-2** Use predefined constant instead of hardcoded value.
  - Use constants available in AEM instead of repeating inline literals.
  
- **AEM-5** ``getContentResource()`` is not null checked
  - Always null check the return value of ``getContentResource()``. It is possible to get a null if a jcr:content node does not exist in the repository.

- **AEM-8** Prefer cleaner `@SlingServlet` annotation.
  - Prefer cleaner `@SlingServlet` annotation over `@Properties` approach. Do not mix up both approaches.

- **AEM-15** Usage of ``synchronized`` keyword should be avoided if possible.
  - Usage of ``synchronized`` keyword should be avoided if possible. Check if using ``synchronized`` can be replaced with more sophisticated solution.

- **AEM-17** No mutator methods invoked on ``ModifiableValueMap``
  - ``ModifiableValueMap`` should be replaced by ``ValueMap`` if no mutator methods are invoked.

- **AEM-19** Implicit search strategy used in Sling Query
  - `SearchStrategy` can have negative performance impact if mismatched.
  Therefore developer should always make informed decision and define strategy explicitly.

## HTL Good practices

- **HTL-1** Wrong placement of the HTL Attribute.
  - Always Place HTL Attributes After the Ones that are Part of the Markup.
  
- **HTL-2** HTL Templates should be placed in separate files.
  - HTL Templates should be placed in separate files. This helps to understand which code is meant to render a component and which code is re-used as a template.

- **HTL-3** Use Explicit Names in Loops
  - HTL provides implicit variables in `data-sly-list` and `data-sly-repeat` blocks.
    Try to avoid them and use explicit names clarifying the role of the objects instead.

- **HTL-4** Name and re-use Repeating Conditions
  - Consider caching data-sly-test conditions and reduce code duplication.

- **HTL-5** Usage of HTML comments should be avoided if possible
  - If you want to place comments regarding your code, make sure they don't display to the end users.

- **HTL-6** HTL automatically recognises the context for HTML output
  - HTL uses uri display context as default for src, poster, manifest, href, formaction, data, cite, action attributes
  
- **HTL-7** Style and script tags display context definition is mandatory
  
- **HTL-8** Event attribute attributes must have display context defined

- **HTL-9** Inline styles must have display context defined

- **HTL-10** Use sly tags over redundant markup.
  - HTL attributes should be wrapped in sly tags to avoid superfluous markup.

- **HTL-11** Use existing HTML elements instead of adding extra sly tags.
  - HTL attributes should be included in HTML markup without additional SLY tags. 

- **HTL-12** Use the most restrictive HTL context possible.
  - For data attributes HTL applies HTML escaping.  
  
- **HTL-13** Avoid using 'unsafe' display context. 
  - 'unsafe' display context disables XSS protection completely.
  
- **HTL-14** HTL expressions in HTML comments should have defined context.
    - HTML comments automatically implies 'comment' markup context. 
    
- **HTL-15** Use Camel Case in identifiers:
    - variable names
    - template names    

## Possible bugs

- **AEM-3** Non-thread safe object used as a field of Servlet / Filter etc.
  - It is not safe to keep session based object as a field in `Servlet` or `Filter`. Rule checks for the occurrence of any instance or static fields of following types:
    - `org.apache.sling.api.resource.ResourceResolver`
    - `javax.jcr.Session`
    - `com.day.cq.wcm.api.PageManager`
    - `com.day.cq.wcm.api.components.ComponentManager`
    - `com.day.cq.wcm.api.designer.Designer`
    - `com.day.cq.dam.api.AssetManager`
    - `com.day.cq.tagging.TagManager`
    - `com.day.cq.security.UserManager`
    - `org.apache.jackrabbit.api.security.user.Authorizable`
    - `org.apache.jackrabbit.api.security.user.User`
    - `org.apache.jackrabbit.api.security.user.UserManager`

- **AEM-6** ResourceResolver should be closed in finally block.
  - According to its [Javadoc](https://sling.apache.org/apidocs/sling6/org/apache/sling/api/resource/ResourceResolver.html), Resource Resolver has a life cycle which begins with the creation of the Resource Resolver using any of the factory methods and ends with calling the `close` method. It is very important to call the `close` method once the resource resolver is not used any more to ensure any system resources are properly clean up.

- **AEM-7** Session should be logged out in finally block.
  - Manually created `javax.jcr.Session` should be logged out after it is no longer needed. The `logout` method releases all resources associated with `Session`.

- **AEM-11** Do not use deprecated administrative access methods
  - Administrative access to the resource tree and JCR Repository by means of usage of ``ResourceResolverFactory.getAdministrativeResourceResolver`` and ``SlingRepository.loginAdministrative`` has been deprecated. Use ``ResourceResolverFactory.getServiceResourceResolver`` or ``SlingRepository.loginService`` respectively.

## [Sling Models](https://sling.apache.org/documentation/bundles/models.html) related

- **AEM-16** Optional is defined as ``DefaultInjectionStrategy``
  - Usage of ``@Optional`` annotation is redundant, when ``defaultInjectionStrategy`` is ``OPTIONAL``.

# Release notes

Release notes for each version can be found in [releases section](https://github.com/wttech/AEM-Rules-for-SonarQube/releases).

# License

Copyright 2015-2024 VML

Licensed under the Apache License, Version 2.0

# Commercial Support

Technical support can be made available if needed. Please [contact us](mailto:labs-support@wundermanthompson.com) for more details.

We can:

* prioritize your feature request,
* tailor the product to your needs,
* provide a training for your engineers,
* support your development teams.
