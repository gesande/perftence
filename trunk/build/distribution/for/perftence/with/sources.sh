#!/bin/bash
set -eu
gradle clean perftence:build perftence:dist perftence:sourcesJar

