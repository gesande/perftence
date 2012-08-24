#!/bin/bash
set -eu

title() {
  echo "Backlog for pertence test tool:"
  echo 
}

tag() {
  local TAG=$1
  echo -n "#$TAG"
}

task-done() {
  local TASK=$1
  local TAGS=$2
  echo -e "\033[32m +++ $TASK +++ $TAGS"
}

done-title() {
  echo 
  echo -e "\033[32m DONE:"
  echo
}

in-progress-title() {
  echo
  echo -e "\033[33m IN PROGRESS:"
  echo
}

in-progress() {
  local TASK=$1
  local TAGS=$2
  echo -e "\033[33m - $TASK $TAGS -"
}

waiting-title() {
  echo
  echo -e "\033[31mWAITING:"
  echo
}

intended-comment() {
  local COMMENT=$1
  echo "  * $COMMENT"
}

waiting() {
  local TASK=$1
  local TAGS=$2
  echo -e "\033[31m --- $TASK --- $TAGS"
}

