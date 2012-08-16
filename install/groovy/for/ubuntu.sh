#!/bin/bash
set -eu
HERE=$(dirname "$0")

rm -rf /opt/groovy-1.8.6
unzip -q $HERE/groovy-binary-1.8.6.zip -d /opt/
echo "unzip successful"
rm -rf /opt/groovy
ln -s /opt/groovy-1.8.6 /opt/groovy
echo "symbolic link created"
cp $HERE/groovy.sh /etc/profile.d/
echo "copied groovy.sh to /etc/profile.d/"
groovy -v
echo "Done"

