#/bin/sh

CODEGEN_JAR=~/wutsi-codegen/wutsi-codegen.jar

API_NAME=marketplace-manager
API_URL=https://raw.githubusercontent.com/wutsi/wutsi-openapi/master/src/openapi/v2/marketplace-manager.yaml
GITHUB_USER=wutsi

echo "Generating code from ${API_URL}"
java -jar ${CODEGEN_JAR} server \
    -in ${API_URL} \
    -out . \
    -name ${API_NAME} \
    -package com.wutsi.marketplace.manager \
    -jdk 11 \
    -github_user ${GITHUB_USER} \
    -github_project ${API_NAME}-server \
    -heroku ${API_NAME} \
    -service_logger \
    -service_messaging \
    -service_mqueue \
    -service_slack \
    -service_swagger

if [ $? -eq 0 ]
then
    echo Code Cleanup...
    mvn antrun:run@ktlint-format
    mvn antrun:run@ktlint-format

else
    echo "FAILED"
    exit -1
fi
