#!/bin/bash
set -eu
HERE=$(dirname $0)
. $HERE/lib.sh

build-distribution-artifact $1
