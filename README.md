# satellite-tracker
The satellite tracker project is an attempt at operating on data in order to track satellite movements and to
predict future or past positions. The main purpose of finding future positions is to plan satellite passes over 
a distinct area for viewing.

There are currently two programs built, one a console app where you type input into the console, and the other
is a GUI, which is more front-end friendly. Probably 98% of the effort building the GUI is in the back-end, so
although it is much prettier looking than the console version, it is not as fully functional as the console version.
The biggest example here is when choosing a satellite to track. The HTTP request may return multiple satellites if
the search matches multiple satellite names. Right now the GUI version only selects the first one automatically, whereas
the console version allows the user to choose which one they wish to select. 

# to run
If running on Windows, two batch files have been created to run as is (as long as Java is installed on the machine).
To run the console app, double-click satellite-tracker-console.bat. To run the GUI app, double-click satellite-tracker-gui.bat.
Either of these files can also be run directly from the command line as well.
If running on a Unix like system (Mac, Linux) the user will need to understand how to run the Java command from the console
(this can also be done on Windows, without the batch files). The command will be like:
Java -classpath CLASSPATH PROGRAM-NAME
where CLASSPATH is equal to the string in classpath.txt (in quotes) which should be:
"out\production\satellite-tracker;external\json-simple\json-simple-1.1.1.jar"
and PROGRAM-NAME is equal to SatelliteTracker for the GUI and com.qbizzle.Main for the console app.
NOTE: don't forget to use the appropriate slash for your filesystem ('\' vs '/').