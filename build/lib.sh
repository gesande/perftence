#!/bin/bash
set -eu

build() {
  local ARGS=$1
  gradle  $ARGS
}
