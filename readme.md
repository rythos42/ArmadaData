## Goals ##
1. Allow players to submit match results in order to gain better global game data.
2. Attempt to figure out inputs based on board state.  
3. Avoid requiring "unreasonable" playing-conventions ("put this here, and that there")
4. Avoid requiring changes to main Vassal module
    4. Though this may become useful if this sees use.

## To get started developing ##
1. Update project values to point vengine.jar to your vassal installation
2. Update project run configuration to point to the Vassal module you'll be running it with
3. Build the project 
4. In the Vassal module select screen, add the extension from {src}\out\artifacts\CreateVMDX\ArmadaData.vmdx

## Future ##
- Consider how to calculate fleet points for players
    - Build library of card/ship stats in this app? Means maintaining that forever.
    - Screenscrape existing fleet builder? (AFD has them all in an SPA) Need to ask that person about this, not the best plan for future-proofing.
    - Build a common API? Difficult, again needs a maintainer.
- Consider how to calculate score for players
    - Count dead ships by "ships not in play area"
    - Count objective points by "objective with tokens", rebel vs imperial tokens
    - TTS puts the calculation in for players, but allows them to override it
- Toggle to select "ranked" or not, when there is a vassal ranking
- Better way of associating fleets with players
  - Ships are marked by which player spawns them, but anyone can spawn anything they like
  - Position (bottom player == player 1) doesn't always work
- How to count better squadrons?
  - Presently counting squadrons on the table and comparing to who has what card
  - Doesn't properly model "card", since it's acting like we have 1-card-per-squadron (incorrect)
  - Doesn't account for mirror matches at all.
- Attempt to auto-select objective played
- Add points cost to fleet display "Acclamator II-class Assault Ship (71)"
- Add "Ranked/Casual" toggle and update API post to send it
- Attempt to auto-select player factions
- Attempt to auto-select objectives played
- Took validation off of objectives entry in case opponent has left and you can't get them anymore! Something else to do here?
- Better way to get objectives? allow pasting from fleet builders?
- add to help guide playing-conventions that will aid the submission process
- can't tab to right First Player radio button, possibly because of this bug https://bugs.openjdk.java.net/browse/JDK-8154043
- figure out how to use IntelliJ variables in project to make it easier for others to start working on it

## Random Stuff ##

How to get Vassal CLI arguments

`c:\users\craig\.jdks\openjdk-11\bin\java -classpath "c:\program files\vassal-3.5.5\lib\vengine.jar" VASSAL.launch.Player -h`

Some code from TTS

`https://github.com/Valadian/TabletopSimulatorIncludeDir/tree/master/TTS_armada`