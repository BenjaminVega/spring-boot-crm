[![Build Status](https://travis-ci.org/BenjaminVega/spring-boot-crm.svg?branch=master)](https://travis-ci.org/BenjaminVega/spring-boot-crm)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.benjaminvega%3Acrm&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.benjaminvega%3Acrm)
# spring-boot-crm

## Description
This app is a CRM based on SpringBoot. The goal is to create a REST-API to manage customer data

## Technical specs
This app uses:

- Database: PostgreSQL
- Non-Relational DB: MongoDB

## Building the app
The project is configured in such a way that just by running "mvn clean install" it will run:
- the building process
- the UnitTests
- The Integration Tests (bringing up the docker containers)
- the Sonar analysis (Reliability, Security, Maintainability and Coverage)

## How to run the app
Before running the spring app we need to startup the environment (database and other services). 
- Open a terminal and change the directory to src/main/resources/docker
- Run the command ```docker-compose up```

## Setting up the env 
To be able to run the app, we need to startup the docker containers. You might need to:
-  Install docker: https://docs.docker.com

