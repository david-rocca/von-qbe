#!/bin/bash

VONQBEPORT=${1:-8080}
echo "Spring port: $VONQBEPORT"
export VONQBEPORT

mvn spring-boot:run -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true
