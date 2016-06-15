# Swimmy Fish
###Specifications:
Develop a Swimmy Fish program controlled by a single user through a Graphical User Interface. 
The object of the game is to navigate a fish named Nemo through pairs of coral obstacles protruding from the top and bottom of the screen. 
As Nemo moves through each pair of coral obstacles, the player’s score should increase. 

###Graphical User Interface:  
The display shall consist of a ground scene along the bottom of the screen and an underwater ocean scene should make up the rest of the background. 
The home screen shall have a “Press ‘space’ to play” sign, a high score display and a “How to play” button.
When the “How to play” button is clicked, a short description of the gameplay shall be displayed as well as a “back” button to return to the home screen. (This display shall inform the player of the game rules.)
When the spacebar is pressed from the home screen, the game shall begin.
Once the game has began, pressing “p” shall pause or un-pause the game .
Coral obstacle pairs shall be of random heights and generate from the right side every [#] seconds. (Regular generation of obstacles shall ensure an invariant level of difficulty.)
The gap between coral pairs shall be constant. (Constant gap between obstacle pairs shall ensure an invariant level of difficulty.)
Nemo the fish shall not move forward of backward; only the background and obstacles shall scroll throughout the game. (A stationary character shall let the user see upcoming obstacles and recently passed obstacles at the same time.)
Nemo shall move up or down depending on user actions. (The player shall have full control over the character’s Y coordinate in order to avoid obstacles.)
There should be a score display that is updated real time as the Nemo passes through the coral obstacles.



###How to Move: 
Nemo shall continuously swim to the right. (The character shall swim at a constant speed to be predictable and reliable for the user.) 
When the player taps the spacebar, Nemo shall increase his sea level. (Clicking the [spacebar] should simulate the nemo flapping his fins.)
When the spacebar is not tapped, the Nemo shall sink deeper into the water. (When Nemo is not flapping, he shall fall due to gravity.)

###Features: 
When each game is over, the system should display the player’s score for that game.  
The system should keep track of the player’s highest score and display a congratulatory message whenever the player achieves a new high score.

###How to play the game:
When the player presses the spacebar from the home screen, a new game shall begin. (Nemo shall be at his starting location, and there shall be one pair of coral obstacles to the far right of the game.)
The background shall begin to scroll to the left while new obstacles generate from the right side of the screen. (This shall give the appearance of the character swimming to the right.)
At anytime during a game, when the player presses “p”, the game shall pause. (If the game is already paused, pressing “p” shall un-pause the game.)
The player should guide Nemo as far as they can without hitting the top of the screen, the ground, or an obstacle. (Hitting anything shall end the game.)

###Object of the game:
The object of the game should be to swim as far as possible before the game ends. (There shall be no way to win the game; the player should attempt to get as high a score as possible.)

###How the game ends:
The game shall end when Nemo collides with a coral obstacle, the ground display, or the ceiling.
