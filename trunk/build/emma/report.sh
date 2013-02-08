#!/bin/bash
set -eu
java -cp tools/emma-2.1.5320-lib/emma.jar emma report -r html -in $1
