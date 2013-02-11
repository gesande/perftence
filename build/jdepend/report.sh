#!/bin/bash
set -eu
mkdir -p tmp/jars
cp **/build/libs/*.jar tmp/jars
cp **/lib/*.jar tmp/jars
mkdir -p tmp/jdepend
echo "Creating JDepend report..."
java -Xmx1024m -jar tools/tattletale-1.1.2.Final/tattletale.jar tmp/jars tmp/jdepend
echo "JDepend report: file://$(realpath tmp/jdepend/index.html)"
