The Dragon's Treasure is an "endless" game composed of two distinct locations - the Dragon's Cave and the Treasure Room. The best way to understand this code is to look at it using the Model, View, Controller design pattern. The main method is in Controller.java

# Basic Overview:
This game supports:
1. The ability to bring up an instruction menu with clear and concise instructions on how to play.
2. The ability to pause the game.
3. The ability to resume the game.
4. The ability to save the game (saves hero health, dragon health, level, and damage)
5. The ability to load the game (loads hero health, dragon health, level, and damage)
4/5. Load/save is done through an unencrypted .txt file; it can be manipulated by opening the .txt        file created and editing the data.
6. Ability to reset your save.
7. Ability to exit the game.

# The Dragon's Cave:
![Imgur](https://i.imgur.com/QoM7A7U.png)
In the dragon's cave, the hero will face progressively harder dragons (health increases; as well as the rate "fireballs" are produced.) The hero is able to damage the dragon using arrows and the dragon is able to damage the hero using its fireballs. 

Once (or if) the hero kills the dragon, a "portal" opens to the treasure room:
![Imgur](https://i.imgur.com/UjOmc9c.png)
Upon moving the hero to the portal, the hero is transported to the treasure room, where he/she can dig for buried treasure that's used to pay for a more potent poison (increasing the damage of the hero's arrows.)

# The Treasure Room: 
![Imgur](https://i.imgur.com/mHRceTu.png)
In the treasure room, the hero has 10 opportunities to "dig" for buried treasure. If the buried treasure is found, the hero has the option to use one to pay for upgraded weaponry. After the treasure room phase, the hero travels to the next dragon's cave to face a more difficult challenge.
