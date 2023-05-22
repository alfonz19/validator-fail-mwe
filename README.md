# MWE demonstrating initialization issues of Hibernate Validator
(or at least Hibernate Validator as it's used by default in springboot)

## Problem

When app starts and first request is made to endpoint accepting annotated dto, it will succeed and from that point on, validation on both endpoints works.

When app starts and first request is made to endpoint accepting dto configured via XML, it will FAIL and from that point on, validation on both endpoints FAILS.

When app reaches defective state, it can be only killed and restarted(as expected).

## Description

There are 2 modules: 

- module `validator-fail-api`, which has just custom Validator annotation definition
- module `validator-fail-app`, which has all the logic. 

This simulates having multi-module app with _api_ module, which does not contain `ConstraintValidator` implementations.

Module `validator-fail-app` has 1 controller(`mwe.validatorfail.controller.TestController`), which accepts 2 DTOs:

- `mwe.validatorfail.dto.AnnotatedSampleDTO`
- `mwe.validatorfail.dto.SampleDTO`

These are 'same', just one has validations expressed via annotations, another one has them expressed via XML(`validation-constraints.xml`).

Root `pom.xml` is here used just so that you can easily build it, otherwise both app has same spring parent to 'imitate' that these modules are unrelated, without same parent.

## How to invoke

in root directory:

    1. mvn clean install
    2. java -jar validator-fail-app/target/validator-fail-app-0.0.1-SNAPSHOT-repackaged.jar

and then invoke scripts from root, based on what endpoint you want to call:

* callAnnotatedEndpoint.sh
* callXmlDefinedEndpoint.sh



