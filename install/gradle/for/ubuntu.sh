#!/bin/bash
set -eu
HERE=$(dirname "$0")

rm -rf /opt/gradle-1.0-rc-1
rm -rf /opt/gradle-1.3

unzip -q $HERE/gradle-1.3-all.zip -d /opt/
echo "unzip successful"
rm -rf /opt/gradle
ln -s /opt/gradle-1.3 /opt/gradle
echo "symbolic link created"
cp $HERE/gradle.sh /etc/profile.d/
echo "copied gradle.sh to /etc/profile.d/"
gradle -v
echo "Done"

