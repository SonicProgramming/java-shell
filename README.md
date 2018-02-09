# java-shell
A command processor written on java (for fun). It can be easily extended with functionality with the help of a plugin loading system that provides plenty of space for adding useful features.
Its original purpose was to be a joke-style \*NIX shell.

Usage (console):
  java -jar JavaShell.jar

To enable plugin loading use:
  java -jar JavaShell.jar --plugins
  
All plugins must be in /plugins directory next to the jarfile. Each plugin should have a \[JARFILE_NAME\].txt file with full classpath of the main class of your plugin. Example: you made it so that your main class named **Myclass** lies in **main** package which is in **plugin** package. Your jarfile is named **MyTestPlugin.jar**. Then you need to place **MyTestPlugin.txt** next to the jarfile in the /plugins folder and write **plugin.main.Myclass** in **MyTestPlugin.txt**.
