#!/bin/bash
set -eu
for i in $(svn st | grep ? | sed 's/?//'); do svn add $i ; done;
