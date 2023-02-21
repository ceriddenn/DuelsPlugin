# DuelsPlugin

This plugin will feature multiple kit types. Includes
 - Gapple
 - BuildUHC
 - Bridge Duels (basically seperate arena and management systems)
 - Classic
 - And much more...

The plugin will also feature multiple menus / guis such as
  - Queue Menu
  - Player Settings Menu
  - /duel cmd shows a menu
  - Possibly an arena management menu.
  
Note: Legit everything is stored in mongodb.... kits, arenas, locs, legit everything. Only one config file with one field which is uri for connecting to db.

Don't worry about connection issues / bandwidth with mongo though as it only saves player data such as wins, losses, deaths, kills on leave and plugin disable so the db
isn't constantly in use.

This plugin also features multiple intelligent systems which include...
  - Queue System for all kit types and the ability to choose a specific queue type and (soon the ability to go into queue for more than 1 kit type)
  - Interal Player management system (manages player stats, different states, scoreboards etc)
  - Scoreboard system (disabled and going to be recoded as I had many issues with the spigot scoreboard api before)
  - Inventory Items for easy navigation and action taking
 
 This plugin is just a side project for me and was started on 2/8/23 so it's very new and not even close to done. Some things don't work as expected due to them not being complete
 or things being hard coded for testing and debuging. If you wish to help me or want to help contact me on discord at **ceridev#8986** or at **ceriddenn@gmail.com**
 
 My main focus right now is to get all of the main game mechanics finished, sort out all bugs, then pretty it up and add final features ie more guis better efficiency and functionality.
 
 **I am 1-2 months into learning java and spigot api so I apologize if this code is messy, un-optimized, or doesn't run efficiently. Please contact me on discord or via email above if you want to help me or give me some pointers on this project. I am always open for help. In the future I will setup this repo for prs and bug reporting.**
 
 ![image](https://user-images.githubusercontent.com/90457772/220230812-e4d66f9e-ba47-4b25-833d-945ef8908675.png)
![image](https://user-images.githubusercontent.com/90457772/220230839-afc76174-ffec-4b21-b115-0aa435ffeb3a.png)

 
 
