Always code as if the guy who ends up maintaining your code will be a violent psychopath who knows where you live.

# Paintbot
A game of wits between programmers where they develop bots to compete against each other in painting a canvas in their
bot's color. The webpage for the game can be viewed at https://paintbot.cygni.se/

Originally based on [Cygni's Snakebot](https://github.com/cygni/snakebot)

This is the server and api repository, other parts:

* [Paintbot webapp](https://github.com/cygni/paintbot-webapp)
* [Paintbot Java Client](https://github.com/cygni/paintbot-client-java)

## Want to create your own client?
If you want to create your own client, please take a look at [Client info](documentation/client_info.md)

## Getting started with intellij
  - Make sure you have enabled the gradle plugin in intellij
  - When creating the project, be sure to import the build.gradle file, otherwise things go wrong

## To do test-runs:
  - Start up the server with 
```
> ./gradlew clean build
> ./gradlew bootRun
```
  - Start up the [webapp](https://github.com/cygni/paintbot-webapp) as described in the README. You have to change some configuration for it to go towards your local server.
```
const location = { origin: "http://localhost" };
```
  - Execute the main-method of one of the clients in [the clients folder](https://github.com/cygni/paintbot/tree/develop/client/src/main/java/se/cygni/paintbot)
  - Check the console for a link to watch the finished game, it should be in the format of 
```
http://localhost:3000/game/{gameId}
```

## New Release
To create a new release using a git repo as the artefactory simply ensure the build.gradle file uses a local folder to "publish" the artifacts, which should be default. Then run `./gradlew api:publish`, `./gradlew client:publish` and `./gradlew client-util:publish`. This should build the artifacts under releases/. This folder should then be copied into the paintbot-maven-repo projects and pushed to the main branch.
