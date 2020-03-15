# This is a very simple Makefile that calls 'gradlew' to do the heavy lifting.

default: all

all:
	./gradlew jar

run: all
	java -jar build/libs/smil.jar

clean:
	rm -rf .gradle build
