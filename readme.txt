perftence - a fluent language to write perftences 

can be used for:
  - finding threading issues of java components
  - writing performance tests
  - writing use cases based tests (called test agents)

Performance test engine written in Java and exposed as a fluent API for writing JUnit tests. 

Perftence can also be used without JUnit by using perftence-api distribution package.

Full test results are provided in html, which can published directly with different CI's (e.g. Jenkins/Hudson).

Runtime test statistics are also provided through logging (e.g. using slf4j-log4j and log4j).

Pertence tool can also be used for finding out threading issues. Just write a test that uses the component you need to test for threading issues and assign load of threads to the test.

Thanks to my employer (Nokia) for letting me publish the initial version of this tool as open-source!


Developing perftence:

import eclipse formatting settings from ./build/eclipse/formatting/settings.xml

some eclipse plugins you might consider useful (not required for developing perftence):

eclipse-groovy plugin
    http://groovy.codehaus.org/Eclipse+Plugin

eclipse-gradle plugin
  http://kaczanowscy.pl/tomek/2010-03/gradle-ide-integration-eclipse-plugin
  https://github.com/spring-projects/eclipse-integration-gradle/

Developing perftence:

install git
checkout the code

see tool installation scripts (mainly for ubuntu) in install project:
./install/groovy/for/ubuntu.sh 
./install/gradle/for/ubuntu.sh
or install them manually

setup iwant
as-perftence-developer/with/bash/iwant/help.sh

build your eclipse settings:
gradle eclipseSettings (applies to buildSrc and backlog projects)
as-perftence-developer/with/bash/iwant/side-effect/eclipse-settings/effective

after that just import the projects to eclipse
