#!/bin/bash
set -eu
gradle $1:create-dirs $1:copy-pmd-settings $1:init-libs $1:eclipse
