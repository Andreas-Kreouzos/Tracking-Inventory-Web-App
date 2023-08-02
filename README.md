# Tracking Inventory

Write a program that tracks your personal inventory. The program should allow you to enter an item, a serial number, and estimated value. The program should then be able to print out a tabular report in both HTML and CSV formats that looks like this:

| **Name**   | **Serial Number** | **Value** |
| ---------- | ----------------- | --------- |
| Xbox One   | AXB124AXY         | $399.00   |
| Samsung TV | S40AZBDE4         | $599.00   |

## Table of Contents
* [Project Description](#project-description)
* [Technologies Used](#technologies-used)
* [Installation](#installation)
* [Execution](#execution)
* [Testing](#testing)
* [Project Status](#project-status)


## Project Description

The consumer of the application can create a POST request by providing the name, the serial number and the value
of an Item. The Tracking Inventory App is responsible for creating, deleting and getting the Items using JSON, HTML:
and CSV file types.

## Technologies Used

Backend Technologies:
- Java 17
- Jakarta 9.1.0
- Groovy 4.0.6

Server and frameworks:

- OpenLiberty 23.0.0.4
- Microprofile 5.0

## Installation

### Properties to be placed in gradle.properties file
In order to setup the project, the following properties should be placed inside
our local gradle.properties file:

- libertyInstallDir
    - variable that points to the local Liberty installation directory (example C:/path/wlp)

The libertyInstallDir is used to speed up the development of the
application. To do so, the following steps should be followed:
- Download the OpenLiberty Server files from [this](https://www.ibm.com/support/pages/20004-websphere-application-server-liberty-20004) URL.
- Unzip the file to a folder of your choice
- The unzipped folder should contain a subfolder under the name "wlp"
- The libertyInstallDir should point to that subfolder i.e. libertyInstallDir=C:\\<chosen_folder>\\openliberty-23.0.0.4\\wlp

**Note**: The usage of libertyInstallDir is not mandatory. But it's advised in order to
speed up the development of the application. If you do not want to make use of this feature
simply leave the value of the libertyInstallDir variable blank.

### Dependencies

- Java 17 needs to be installed in your local machine.

## Execution

To run the project locally after completing all the [Installation](#installation) steps you need to:

- Open a Git Bash inside the repository folder and execute the following command: `./gradlew libertyDev`

At this point the Tracking Inventory application is live on your localhost:9080. To ensure that it is working you can visit
the application's [OpenAPI interface](http://localhost:9080/openapi/ui/) where its API is documented and run some test requests against
its endpoint.

## Testing
The application contains two kinds of tests.

- Tests found under the `gr.iag.dgtl.inventory` package are unit tests. They can be executed without running the application.
- Tests found under the `integration.gr.iag.dgtl.inventory` package are integration tests. In order for them to be executed
  both the [Installation](#installation) and the [Execution](#execution) of the application must have been completed.

To run all the tests of the application use the following command inside a terminal: `./gradlew test`.
To run only the unit tests add the `--tests "gr.iag.dgtl.ip.motorclaims"` option. To run only integration tests add the
`--tests "integration.gr.iag.dgtl.ip.motorclaims"` option.

## Project Status
Project is: _in progress_\
Features awaiting implementation are:
- Integration tests