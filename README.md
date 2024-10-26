# Introduction

To visualize a group of classes in a UML like class diagram can be useful in terms of understanding the overall business logic.

# Purpose

The purpose of this project is to create an multi-module application that processes Java source code to render it as a class diagram.

# Modules

The 3 modules to achieve the purpose of this project are:
- objectmorph-logic
- objectmorph-renderer
- objectmorph-app

## objectmorph-logic

This modules takes advantage of JavaParser frameworks that retrieves Java class elements and allows a seamless manipulation.

## objectmorph-renderer

This module  is built upon J2HTML framework, with an orchestration of the aforementionned module, with the aim to construct HTML pages based on Java source code elements.

## objectmorph-app

This module allows the consumption of objectmorph-renderer module through REST endpoints.

# Conclusion

This project offers an easy to deploy Spring Boot enabled application so that submitting Java source code files can be done easily through all kinds of interfaces.
