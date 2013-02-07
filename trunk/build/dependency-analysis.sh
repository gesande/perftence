#!/bin/bash
set -eu
gradle clean jar
cp **/build/libs/*.jar tmp
cp **/lib/*.jar tmp
java -Xmx1024m -jar tools/tattletale-1.1.2.Final/tattletale.jar tmp/ out

