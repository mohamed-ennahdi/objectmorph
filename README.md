# Introduction

To visualize a group of classes in a Eclipse like class diagram can be useful in terms of understanding the overall business logic.

This project was inspired by the eclipse plugin called ObjectAid, that is not being maintained anymore.

Sample video:

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/h0GwFqhiWfs/0.jpg)](https://www.youtube.com/watch?v=h0GwFqhiWfs)

# Purpose

This is a multi-module application that processes Java source code to render it as a class diagram.

![](https://a.fsdn.com/con/app/proj/objectmorph/screenshots/objectmorph-classdiagram-ef940eae.png/max/max/1)

# Modules

The 3 modules to achieve the purpose of this project are:
- objectmorph-logic
- objectmorph-renderer
- objectmorph-app

## objectmorph-logic

This modules takes advantage of JavaParser frameworks that retrieves Java class elements and allows a seamless manipulation.

## objectmorph-renderer

This module  is built upon [J2HTML](https://j2html.com/), [PlainDraggable](https://github.com/anseki/plain-draggable), and [LeaderLine](https://github.com/anseki/leader-line) frameworks, with an orchestration of the aforementionned module, with the aim to construct HTML pages based on Java source code elements.

## objectmorph-app

This module allows the consumption of objectmorph-renderer module through REST endpoints.

# CI / CD

Through Github Actions, there is a RELEASE workflow which is triggered manually. It creates a tag, and prepares the POM files to the next SNAPSHOT version.

The tag creation triggers another workflow whose duty is to create and publish, in [Docker hub](https://hub.docker.com/r/ennahdi/objectmorph-app/tags), a new Docker image with the latest tag and version number tag.

Render.com allows to deploy Spring Boot applications available as Docker images as Web Service.

# Deployed Instance

objectmorph-app is deployed under Render, and is accessible with the following url:

https://objectmorph-app.onrender.com/

# Conclusion

This project offers an easy way to deploy Spring Boot enabled application so that submitting Java source code files can be done easily through all kinds of interfaces.

Eventually, the returned HTML code can be viewed in a browser and displays the source code as a class diagram.
