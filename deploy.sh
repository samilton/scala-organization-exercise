#!/bin/bash

# need maven three point three
if [ -z ${MAVEN_HOME} ]; then
  echo "Please set MAVEN_HOME and try again"
  exit
fi

maven=${MAVEN_HOME}/bin/mvn

if [ -e runner.jar ]; then
  rm runner.jar
fi

${maven} clean
${maven} package

(echo 'exec java -jar "$0" "$@"'; cat target/runner.jar ) > runner
chmod +x runner.jar

