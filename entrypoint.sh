#!/bin/sh
mvn clean test -Dmaven.test.skip=false -Dtest="Authorization#Authorizations"
allure generate target/allure-results --clean