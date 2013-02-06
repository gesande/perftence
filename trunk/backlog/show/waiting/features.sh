#!/bin/bash
set -eu
HERE=$(dirname $0)
$HERE/../../show.sh  | grep " \-\-\-" | grep "#feature"
