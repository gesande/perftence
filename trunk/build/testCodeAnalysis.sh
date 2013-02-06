#!/bin/bash
set -eu
gradle clean findbugsTest | tee ../out
echo Check reports:
echo $(cat ../out | grep 'FindBugs rule violations were found')
