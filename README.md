# ColorFight! API for Java

This is an ColorFight! client written in Java.

# Dependence

This package is dependent with `org.json`. You need to include `org.json` in your compiler. You can find `json-20180130.jar` in this repository.

* If you put the jar file in the same directory with your codes, use the following command:
  * compile: `javac -cp ".:*"  Main.java`
  * run program: `java -cp ".:*"  Main`
* If you put the jar file in a different directory, use the following comand(fill in `<path to jar>`):
  * compile: `javac -cp "<path to jar>"  Main.java`
  * run program: `java -cp "<path to jar>"  Main`
* If you are using an IDE, add the jar file to project dependencies(For example, for IntelliJ, `File >> Project Structure >> Project Settings >> Modules >> Denpendencies >> <the plus icon> >> JARs or directories`).

# Logging

* Use `enableDebugMode` to save detailed logs (**note that this may fill up your disk quickly!**).
* Use `disableLogging` to turn off logging.

If you open the log file in Firefox, it will be showen in a more user-friendly way. Chrome is not supported (it won't load `logging.xsl`).

# Documentation

To be continued...
