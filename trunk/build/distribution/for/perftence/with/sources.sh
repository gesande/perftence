#!/bin/bash
set -eu
set -eu
HERE=$(dirname $0)
. $HERE/../../lib.sh

build-distribution-artifact "perftence"

