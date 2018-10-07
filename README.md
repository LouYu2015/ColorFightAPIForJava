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
* If you are using an IDE, add the jar file to project dependencies:
    * For IntelliJ, `File >> Project Structure >> Project Settings >> Modules >> Denpendencies >> <the plus icon> >> JARs or directories`.
    * For Eclipse, right click on the jar file, select `Build Path >> Add To Builds Path`

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

## Notes

You can watch the game in web browser at the server's URL (by default, [https://colorfightuw.herokuapp.com/](https://colorfightuw.herokuapp.com/)).

Coordinate's origin is at upper-left corner of the board.

Unit of time is seconds in all places, except `game.sleep()`.

For game rules,  see [https://colorfight.cssauw.org/](https://colorfight.cssauw.org/).

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
* `game.getCell(x, y)`: get the cell at coordinate (x, y). See also: `ColorFightCell`.
* `game.canAttack(x, y)`: return whether you can attack this cell.

###  Reading Player's Information
 
* `game.myInfo`: get my information. See also: `ColorFightPlayer`.
* `game.otherPlayerInfo`: an array of other players. See also: `ColorFightPlayer`.
 
### `ColorFightCell`

* `cell.getX()`: x coordinate.
* `cell.getY()`: y coordinate.
* `cell.getOwnerUID()`: user ID of owner.
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
* `cell.toString()`: description of this cell.

### `ColorFightPlayer`

* `player.getID()`: user ID.
* `player.getName()`: user name.
* `player.getCDTime()`: timestamp when this player can take next action.
* `player.getCellNum()`: number of cells occupied by this player.
* `player.getBaseNum()`: number of bases that this player owns.
* `player.getEnergyCellNum()`: number of energy cells that this player occupies.
* `player.getGoldCellNum()`: number of gold cells that this player occupies.
* `player.getGold()`: amount of gold that this player holds.
* `player.getEnergy()`: amount of energy that this player has.
* `player.toString()`: discription of this player.

## Actions

* `game.attackCell(x, y)`: attack cell at coordinate (x, y). It must be next to a cell that you owns.
* `game.attackCell(x, y, boost)`: attack cell. if boost is true, use 15 energy to boost this attack. This attack's time cost will be `max(1, x/4)` after boost, where `x` is the time cost before boost.
* `game.buildBase(x, y)`: build a base on cell.
* `game.blast(x, y, direction)`: start an explosion centered at (x, y). An explosion takes 1 seconds and costs 30 energy. It will force every cell in the explosion area to lost its owner and become empty cell, except your own cells. `direction` can be:
    * `BlastDirection.SQUARE`: set explosion area to a 3 * 3 square
    * `BlastDirection.VERTICAL`: set explosion area to 9 cells in a vertical colomn.
    * `BlastDirection.HORIZENTAL`: set explosion area to 9 cells in a horizental row.
* `game.multiAttack(x, y)`: attack 4 cells directly next to (x, y)(vertically and horizentally). All attack will take place simultimiously, but only cells next to your cell can be attacked. You can take next action only after all attack finished.

## Other Methods

* `game.setPassword(password)`: tell the client to use password when joining the game. Should be called before `game.joinGame()`
* `game.sleep(time)`: wait some time. You can use it to wait for current action to end. ** Unit of `time` is milliseconds(0.001 second) instead of second. **
