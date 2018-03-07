#!/bin/bash

set -eu

[ $# == 1 ] || {
    echo "Usage: $0 AS_SOMEONE"
    echo "e.g. $0 as-iwant-user"
    exit 1
}

MYNAME=$(basename "$0")
AS_SOMEONE=$(readlink -f "$1")
WSROOT=$(readlink -f "$AS_SOMEONE/..")

cd "$WSROOT"

without-caches-etc() {
    cat |
	grep -v '/.i-cached/' |
	grep -v "$MYNAME" |
	cat
}

must-be-skipped() {
    local FILE=$1
    [ -L "$FILE" ] && return 0
    [ -d "$FILE" ] && return 0
    return 1
}

find . -name '*.java' -o -name '*.sh' | without-caches-etc | while read F; do
    must-be-skipped "$F" && continue || true
    echo "sed -i 's/net\.sf\.iwant/org.fluentjava.iwant/g' $F"
    echo "sed -i 's:net/sf/iwant:org/fluentjava/iwant:g' $F"
done

echo "echo Remember to delete the obsolete $AS_SOMEONE/with/java/net"
echo "echo Also, ExternalSource constructor no more throws checked so you will have to stop catching/throwing"

