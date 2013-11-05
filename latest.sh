#!/bin/bash

MSG=${1-mutyi}

LATEST=$(curl -s http://central.maven.org/maven2/com/github/lalyos/jfiglet/maven-metadata.xml|grep "<latest>"| sed "s:</.*::;s:.*>::")
curl -so jfiglet.jar http://central.maven.org/maven2/com/github/lalyos/jfiglet/$LATEST/jfiglet-$LATEST.jar
java -jar jfiglet.jar $MSG
rm jfiglet.jar