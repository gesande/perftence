perftence - a fluent language to write perftences 

can be used for:
  - finding threading issues of java components
  - writing performance tests
  - writing use cases based tests (called test agents)

import eclipse formatting settings from ./build/eclipse/formatting/settings.xml

some eclipse plugins you might consider useful (not required for developing perftence):

eclipse-groovy plugin
  http://dist.springsource.org/release/GRECLIPSE/e3.7/

eclipse-gradle plugin
  http://kaczanowscy.pl/tomek/2010-03/gradle-ide-integration-eclipse-plugin

Developing perftence:

install subversion
checkout the code

see tool installation scripts (mainly for ubuntu) in install project:
./install/groovy/for/ubuntu.sh 
./install/gradle/for/ubuntu.sh
or install them manually

build your eclipse settings:
./build/eclipse/settings.sh 

after that just import the projects to eclipse


