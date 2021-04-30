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

## TODO ##
- I recommend posting in TTS discord "worked with Valadian to make a plugin to upload to TTS Armada score cloud", "for when you have to play in Vassal but still want to get ranked credit for the game"
- put out to public
- re-write Chatter messages from the POV that both players will see them.
- multi-counting several squadrons
- bug from James
- catch all exceptions and write to Chatter
  - have them send a vsav to me for diagnosis

New release
- auto-selection of first player
- update wiki

## Future ##
- Consider how to calculate fleet points for players
    - Build library of card/ship stats in this app? Means maintaining that forever.
    - Screenscrape existing fleet builder? (AFD has them all in an SPA) Need to ask that person about this, not the best plan for future-proofing.
    - Build a common API? Difficult, again needs a maintainer.
- Consider how to calculate score for players
    - Count dead ships by "ships not in play area"
    - Count objective points by "objective with tokens", rebel vs imperial tokens
    - TTS puts the calculation in for players, but allows them to override it
- How to count better squadrons?
  - Presently counting squadrons on the table and comparing to who has what card
  - Doesn't properly model "card", since it's acting like we have 1-card-per-squadron (incorrect)
  - Doesn't account for mirror matches at all.
- Add points cost to fleet display "Acclamator II-class Assault Ship (71)"
- Add "Ranked/Casual" toggle and update API post to send it
- figure out how to use IntelliJ variables in project to make it easier for others to start working on it
- determine first player by where the initiative token is
  - can determine if top or bottom is first player, but with current UI can't pick one of them because I don't have a name!!
  - try to go back to radio button technique? :(
- trying to do faction selection but can't get piecewindow
  - got it using a certain combination of things ("single window" option in Preferences+cutting/pasting other Game Piece Palettes below the main one)
  - check to see if this actually allows me to get access to the piece tree to determine faction
```
        for (final PieceWindow window : GameModule.getGameModule().getAllDescendantComponentsOf(PieceWindow.class)) {
            if("Game pieces".equals(window.getConfigureName())){
                var i = 0;
            }
        }
 ```

## Random Stuff ##

How to get Vassal CLI arguments

`c:\users\craig\.jdks\openjdk-11\bin\java -classpath "c:\program files\vassal-3.5.5\lib\vengine.jar" VASSAL.launch.Player -h`

Some code from TTS

`https://github.com/Valadian/TabletopSimulatorIncludeDir/tree/master/TTS_armada`