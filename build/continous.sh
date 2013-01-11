#!/bin/bash
set -eu
gradle --info perftence:clean perftence:test perftence:copy-pmd-settings perftence:findbugsMain perftence:pmdMain

