#!/bin/bash
set -eu
clear && svn st && echo "Revision: $(svnversion)"
