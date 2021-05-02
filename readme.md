# ArmadaData #
This is an extension to the [VASSAL](http://www.vassalengine.org/index.php) module for the tabletop wargame [Star Wars Armada](http://www.vassalengine.org/wiki/Module:Star_Wars:_Armada). It allows players to submit data to a common server, to help with determining global trends in the game.

## Project Goals ##
1. Allow players to submit match results in order to gain better global game data.
2. Attempt to figure out inputs based on board state.  
3. Avoid *requiring* unreasonable playing-conventions ("put this here, and that there")
4. Avoid *requiring* changes to main VASSAL Armada module
    4. Though this may be a useful collaboration in the future.

## To get started developing ##
1. (optional?) Install IntelliJ
2. Set project path variable `VASSAL_BASEDIR` to point to your VASSAL directory, like `C:\Program Files\Vassal-3.5.3`.
3. Set project path variable `VASSAL_MODULE_BASEDIR` to point to the directory where you keep your VASSAL modules, or the location of the Armada module. 
3. Build the project 
4. In the VASSAL module select screen, add the extension from {repo}\out\artifacts\CreateVMDX\ArmadaData.vmdx

## TODO ##
- Add "Ranked/Casual" toggle and update API post to send it
- got it using a certain combination of things ("single window" option in Preferences+cutting/pasting other Game Piece Palettes below the main one)
  - check to see if this actually allows me to get access to the piece tree to determine faction
```
        for (final PieceWindow window : GameModule.getGameModule().getAllDescendantComponentsOf(PieceWindow.class)) {
            if("Game pieces".equals(window.getConfigureName())){
                var i = 0;
            }
        }
 ```

- I recommend posting in TTS discord "worked with Valadian to make a plugin to upload to TTS Armada score cloud", "for when you have to play in Vassal but still want to get ranked credit for the game"
- put out to public

New release
- auto-selection of first player
- update wiki
- fixed bug with giant fleet text area
- fixed bug where if the player who spawned a fleet isn't in the game, an error would occur
- fixed bug with counting squadrons incredibly wrong 
- preventing errors from trying to go to the Vassal bugtracker, they won't help
- updated dev environment to be easier to get started developing

## Future ##
- Consider how to calculate fleet points for players
  - Build library of card/ship stats in this app? Means maintaining that forever.
  - Screenscrape existing fleet builder? (AFD has them all in an SPA) Need to ask that person about this, not the best plan for future-proofing.
  - Build a common API? Difficult, again needs a maintainer.
  - Add points cost to fleet display "Acclamator II-class Assault Ship (71)"
- After we have fleet points, consider how to calculate score for players
  - Count dead ships by "ships not in play area"
  - Count objective points by "objective with tokens", rebel vs imperial tokens
  - TTS puts the calculation in for players, but allows them to override it
- How to count better squadrons?
  - Presently counting squadrons on the table and comparing to who has what card
  - Doesn't account for mirror matches at all.
- fleets spawned by Shrimpbox have a side of "<observer>", so am unable to link them to a certain player.
  - more evidence we might want an in-vassal ship spawner

## Random Stuff ##

How to get Vassal CLI arguments

`c:\users\craig\.jdks\openjdk-11\bin\java -classpath "c:\program files\vassal-3.5.5\lib\vengine.jar" VASSAL.launch.Player -h`

Some code from TTS

`https://github.com/Valadian/TabletopSimulatorIncludeDir/tree/master/TTS_armada`