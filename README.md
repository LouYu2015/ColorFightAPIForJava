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

## Project Structure

### Code Files

* `Main.java`: example AI.
* `ColorFightClient.java`: game client.
* `ColorFightPlayer.java`: a class that provide player information.
* `ColorFightCell.java`: a class that provide cell information.
* `BlastDirection.java`: a class that is used to specify a direction in blast action.

### Other Files

* `README.md`: documentation.
* `LICESE.txt`: license.
* `json-20180130.jar`: compiled code for org.json package.
* `logging.xsl`: style for log file.
* `logging.xml`: log file.
* `configuration.txt`: player's token.


## Join Game

Use `ColorFightClient game = new ColorFightClient()` to creat a new client. If you want to change server URL, pass the URL as a parameter.

Use `game.joinGame(username)` to join the game. Please choose an unique name as your user name.

After you joined the game, you will get a token as your identification. It will be stored in `configuration.txt`. If your program is aborted during the game, `joinGame()` will automatically use this token to restore previous game status from server when you start the program again. If you want to join as a new player instead, please use `game.clearToken()` or delete `configuration.txt`.

## Logging

We provide a logger that helps you to manage logs. It can show logs in the console and and save them to disk at the same time.

Logs will be save in `logging.xml`. If you open the log file in Firefox, it will be showen in a more user-friendly way. Chrome is not supported (it won't load `logging.xsl`).

**Note: when you start the program again, previous logs will be overrided and lost.**

### Adding to Log

Use the following method to add your own log:

* `game.LOGGER.severe(message)`: log a error message
* `game.LOGGER.warining(message)`: log a waring
* `game.LOGGER.info(message)`: log some information
* `game.LOGGER.fine(message)`: log some information that can be ignored

### Turn On or Turn Off Logging

You can easily controll what level of details to keep without commenting out any codes:

* Use `game.enableDebugMode()` to show and save detailed logs for the client. After that, `game.LOGGER.fine` won't be ignored.  (**note that this may fill up your disk quickly!**).
* Use `game.disableLogging()` to turn off logging for the client. After that, all logs will be ignored.

## Reading Game Status

* `game.refresh()`: get latest information from server. **Remember to call this after each action, or your will get outdated information!**

### Reading General Information

* `game.uid`: your user ID.
* `game.getGameID()`: ID for this game.
* `game.getEndTime()`: timestamp for the time when the game will end. 0 means no limit.
* `game.getJoinEndTime()`: timestamp for the time when the game will stop accepting new player. 0 means no limit.
* `game.getWidth()`: width of game board.
* `game.getHeight()`: height of game borad.
* `game.getLastUpdateTime()`: timestamp for the last time that the client fetch information from server.

### Reading the Game Board

* `game.cellInfo`: an array of all cells on the board. See also: `ColorFightCell`.
* `game.getCell(x, y)`: get the cell at coordinate (x, y). Origin is at upper-left corner of the board. See also: `ColorFightCell`.
* `game.canAttack(x, y)`: return whether you can attack this cell.

###  Reading Player's Information
 
* `game.myInfo`: get my information. See also: `ColorFightPlayer`.
* `game.otherPlayerInfo`: an array of other players. See also: `ColorFightPlayer`.
 
### `ColorFightCell`

* `cell.getOwnerID()`: user ID of owner.
* `cell.getCellType()`: name of cell's type. May contain the following values:
    * `"normal"`: this is an ordinary cell.
    * `"gold"`: this is golden cell.
    * `"energy"`: this is energy cell.
* `cell.isEmpty()`: return true if this cell has owner.
* `cell.isEdge()`: return true if this cell is out of the boundary.
* `cell.isBuilding()`: return true if there is a building on this cell.
* `cell.isBase()`: return true if this building is a base.
* `cell.getBuildType()`: name of building's type.
* `cell.getBuildTime()`: timestamp when building on this cell was built.
* `cell.isTaking()`: return true if this cell is under attacked.
* `cell.getFinishTime()`: timestamp when attack will finish.  ** Don't use if the cell is not under attack**.
* `cell.getAttackerUID()`: user id of attacker. ** Don't use if the cell is not under attack**.
* `cell.getOccupyTime()`: timestamp when cell is occupied by current owner.
* `cell.getTakeTime()`: time to spend if you attack this cell. Unit: second.

### `ColorFightPlayer`

To be continued...
