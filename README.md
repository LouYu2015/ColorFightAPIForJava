# ColorFight! API for Java

This is an ColorFight! client written in Java.

## Dependency

This package is dependent with `org.json`. You need to include `org.json` in your compiler. You can find `json-20180130.jar` in this repository.

* If you put the jar file in the same directory with your codes, use the following command:
    * compile: `javac -cp ".:*"  Main.java`
    * run program: `java -cp ".:*"  Main`
* If you put the jar file in a different directory, use the following comand(fill in `<path to jar>`):
    * compile: `javac -cp "<path to jar>"  Main.java`
    * run program: `java -cp "<path to jar>"  Main`
* If you are using an IDE, add the jar file to project dependencies(For example, for IntelliJ, `File >> Project Structure >> Project Settings >> Modules >> Denpendencies >> <the plus icon> >> JARs or directories`).

## Project structure

### Code files

* `Main.java`: example AI.
* `ColorFightClient.java`: game client.
* `ColorFightPlayer.java`: a class that provide player information.
* `ColorFightCell.java`: a class that provide cell information.
* `BlastDirection.java`: a class that is used to specify a direction in blast action.

### Other files

* `README.md`: documentation.
* `LICESE.txt`: license.
* `json-20180130.jar`: compiled code for org.json package.
* `logging.xsl`: style for log file.
* `logging.xml`: log file.
* `configuration.txt`: player's token.


## Join game

Use `ColorFightClient game = new ColorFightClient()` to creat a new client. If you want to change server URL, pass the URL as a parameter.

Use `game.joinGame(username)` to join the game. Please choose an unique name as your user name.

After you joined the game, you will get a token as your identification. It will be stored in `configuration.txt`. If your program is aborted during the game, `joinGame()` will automatically use this token to restore previous game status from server when you start the program again. If you want to join as a new player instead, please use `game.clearToken()` or delete `configuration.txt`.

## Logging

We provide a logger that helps you to manage logs. It can show logs in the console and and save them to disk at the same time.

Logs will be save in `logging.xml`. If you open the log file in Firefox, it will be showen in a more user-friendly way. Chrome is not supported (it won't load `logging.xsl`).

**Note: when you start the program again, previous logs will be overrided and lost.**

### Adding to log

Use the following method to add your own log:

* `game.LOGGER.severe(message)`: log a error message
* `game.LOGGER.warining(message)`: log a waring
* `game.LOGGER.info(message)`: log some information
* `game.LOGGER.fine(message)`: log some information that can be ignored

### Turn on or turn off logging

You can easily controll what level of details to keep without commenting out any codes:

* Use `game.enableDebugMode()` to show and save detailed logs for the client. After that, `game.LOGGER.fine` won't be ignored.  (**note that this may fill up your disk quickly!**).
* Use `game.disableLogging()` to turn off logging for the client. After that, all logs will be ignored.

## Basic actions

To be continued...
