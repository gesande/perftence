#!/bin/bash
set -eu

show-svn-status() {
  local ST=$(svn st)
  clear
  if [ "$ST" = "" ]; then
    echo "No changes to be committed."
  else
    svn st
  fi
}

show-svn-status
