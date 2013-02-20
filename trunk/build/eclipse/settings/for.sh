#!/bin/bash
set -eu

settings-for() {
  local PROJECT=$1
  gradle --info :$PROJECT:cleanEclipse :$PROJECT:eclipse
} 

settings-for $1
