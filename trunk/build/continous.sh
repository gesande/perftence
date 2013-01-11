#!/bin/bash
set -eu
gradle --info perftence:clean perftence:test perftence:findbugsMain perftence:pmdMain

