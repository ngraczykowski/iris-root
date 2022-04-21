# Iris Platform <!-- omit in toc -->

Welcome to the Iris Platform project, where all the ends meet and all suffering ends.

This project contains all Iris-based product components.

#### Table of Contents <!-- omit in toc -->

[[_TOC_]]

## Prerequisites

Before you jump in and start furiously contributing, you might need to have a few prerequisites on
your system.

|  Prerequisite  | Necessity | Remarks                                                                                                                                                                                                                                               |
|:--------------:|:---------:|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|  **Java
17**   | Mandatory | Iris uses Java 17, you can  [download easy to install packages for your system from Azul Systems, Inc.](https://www.azul.com/downloads/zulu-community/?&version=java-17-lts#download-openjdk); make sure you install full Java Development Kit (JDK). |
|   **
Docker**   | Mandatory | Running integration tests in SERP requires Docker; infrastructural services also can use Docker, which greatly simplifies development setup.                                                                                                          |
|  **
IntelliJ**  | Mandatory | Project is prepared for being worked on in IntelliJ IDEA, so you have to have a license for yours.                                                                                                                                                    |
|    **
Make**    | Mandatory | Yes, there is a Makefile in the root of this repository. It makes running scripts easier to document, and allows for setting dependencies between scripts, so go and grab the nearest Make package for your system.                                   |
| **
Pre-commit** | Mandatory | [Pre-commit](https://pre-commit.com) keeps commits clean, running various hooks. To install pre-commit, [follow these instructions](docs/development/installing-pre-commit.md).                                                                       |
|    **
gdub**    | Optional  | That is a helper script for running nearest gradle wrapper. [Download it from GitHub](https://github.com/dougborg/gdub) and follow instructions for installing.                                                                                       |
|    **
jEnv**    | Optional  | jEnv helps with managing Java versions. [Download it from jEnv site](https://www.jenv.be/) and follow instructions for installing.                                                                                                                    |  
|   **
Gradle**   | Optional  | Iris comes with Gradle Wrapper that you should use for building the project. Nevertheless you might want to have Gradle installed.                                                                                                                    |

## Development setup

### After cloning

Right after cloning this repository, please initialize the pre-commit hook:

```bash
pre-commit install
```

> **NOTE**
>
> If you have not done so
> yet, [please install Pre-commit following the instructions](docs/development/installing-pre-commit.md)
> before running the command.

### IntelliJ IDEA configuration

> **WORK IN PROGRESS**

The recommended method for running Iris components is to them from IntelliJ directly, while all the
third-party dependencies as Docker Compose managed Docker containers.

## Gradle plugins

`com.silenteight.sear.build.spring-boot-application`

`com.silenteight.sear.build.java-library`

`com.silenteight.sear.build.java-base`
