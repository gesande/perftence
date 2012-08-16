#!/bin/bash
set -eu
HERE=$(dirname $0)

svn-ignore-using-file-settings() {
  svn propset svn:ignore -F $HERE/svn-ignore $1
}

svn-ignore-using-file-settings $1
