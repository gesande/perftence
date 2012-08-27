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
  echo -e "\033[32m +++ $TASK +++ $TAGS \e[m"
}

done-title() {
  echo 
  echo -e "\033[32m DONE:"
  echo
}

in-progress-title() {
  echo
  echo -e "\033[33m IN PROGRESS: \e[m"
  echo
}

in-progress() {
  local TASK=$1
  local TAGS=$2
  echo -e "\033[33m - $TASK $TAGS - \e[m"
}

waiting-title() {
  echo
  echo -e "\033[31mWAITING: \e[m"
  echo
}

intended-comment() {
  local COMMENT=$1
  echo -e "\033[31m  * $COMMENT  \e[m"
}

waiting() {
  local TASK=$1
  local TAGS=$2
  echo -e "\033[31m --- $TASK --- $TAGS \e[m"
}

