# This is a very simple Makefile that calls 'gradlew' to do the heavy lifting.

default: dist

dist:
	./gradlew jar

clean:
	rm -rf .gradle build
