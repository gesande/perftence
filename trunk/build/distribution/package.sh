#!/bin/bash
set -eu

gradle perftence:dist perftence:sourcesJar responsecode-summaryappender:sourcesJar responsecode-summaryappender:dist perftence-junit-utils:sourcesJar perftence-junit-utils:dist

