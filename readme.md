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

## Future ##
- Faction auto-selection
  - Tried to use PieceWindow, but ran into a Java/VASSAL problem and couldn't fix it. Posted on forum, have a branch with prototype code, not hopeful it will work.
  - Having GK add faction as a Layer Marker on each ship would resolve this really easily.
- Player auto-selection of Shrimpbot spawned fleets
  - Not likely to be really easy to solve with current tech.
  - in-VASSAL fleet spawner would solve this
- Mirror matches and squadron counting
  - Presently the algorithm gets all your cards, then counts all the squadron models on the table and assigns them as additional squadrons to the first player who has that squadron.
  - This doesn't work for mirror matches, as both players may have the same squadron.
  - I don't have a good solution for this right now.
  - Use the mirror match markers somehow?
  - in-VASSAL fleet spawner would solve this, as each fleet data could be stored as a unit
- Auto-calculate fleet points
  - Presently there is no facility to calculate fleet points, as VASSAL/module doesn't have this information.
  - Build a library of card stats? I don't want to maintain such a thing in this app.
  - Parse card images? Not a great plan.
  - Screenscrape another fleet builder or wiki? Also not a great plan. 
  - Build an external API of card stats? Possible, consider collaborating with other community developers on this.
  - in-VASSAL fleet spawner could solve this
- Add points cost to fleet display and what we sent to API "Acclamator II-class Assault Ship (71)"
- Auto-calculate final score
  - This isn't really hard, but with no fleet points it doesn't seem worthwhile.
  - Have a library of objectives and what their victory tokens are worth
  - Tokens above the card count towards the top player, tokens below the card count towards the bottom player.
  - Count dead ships/squadrons as ships not in the play area
- Fix how the dialog is laid out
  - Using a static "buffer" with a BoxLayout isn't great
  - Dialog feels a little "claustrophobic" to me.