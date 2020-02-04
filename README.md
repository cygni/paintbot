Always code as if the guy who ends up maintaining your code will be a violent psychopath who knows where you live.

## Getting started with intellij
  - Make sure you have enabled the gradle plugin in intellij
  - When creating the project, be sure to select the build.gradle file, otherwise things go wrong

## To do test-runs:
  - Start up the server with 
```
> ./gradlew clean build
> ./gradlew bootRun
```
  - Start up the [webapp](https://github.com/cygni/paintbot-webapp) as described in the README. You may have to add the following line to [src/Routes.tsx](https://github.com/cygni/paintbot-webapp/blob/develop/src/Routes.tsx) in the beginning of the document:
```
const location = { origin: "http://localhost" };
```
  - Execute the main-method of one of the clients in [the clients folder](https://github.com/cygni/paintbot/tree/develop/client/src/main/java/se/cygni/paintbot)
  - Check the console for a link to watch the finished game, it should be in the format of 
```
http://localhost:3000/game/{gameId}
```
