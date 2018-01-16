# AndroidGameEngine
The project was created to learn the basics of a 2D game engine creating a solid base 
for little 2D games so that developers can focus on game logic.

## Description and feature

This Engine is created around 2 main Object, GameObject and GameData.

The GameObject is an abstract class for all GameObjects implemented from users and holds generic data like: 
polygon shape, bounding box, speed, animation etc. 

This class also defines the method **update** , **onTouch**, **onCollide**, invoked respectively 
when the event occurs (update is called every game cycle loop).

For the implementation of a shape into the game, the class Polygon of the AWT java library was imported into this 
engine and modified with some other additional data, like android Rect for useful rapresentation of the bounding box and 
a android Point to save the center of the object.

The GameData object holds the data needed for a game activity to run like: a list with all the object in the view, 
the surface to draw, the fps data, etc.

Plus this class has a field **viewData** defined as a generic class, so if a user wants to add a 
custom data for the view he can add it there.

A abstract GameActivity and GameView is needed to extend in order to add an activity into the game.
This two objects holds the core elements described above and provides a start/stop gameLoop method and skip/not-skip 
updatePhase method. 

>For now the mechanism to switch activity is yet to be implement by the user. Future implementations will add a ViewManager to do this.

The Engine uses a ColliderManager with a AABB collision check. This implementation has obvious problems with 
fast moving objects or thin walls in collision check. Some of this problems are fixed with the **comeBack()** function called in the 
onCollide Object method. This function moves the object in the position before the collision, so we 
avoid to apply the collision logic inside the object collided in order to avoid being stuck inside.

>If this basic logic dosen't fix the problem, some other logic has to be implemneted in the onCollide method.

To draw the Animation the Engine works with sprite sheets and can hold more sprites for each object.


## Import the engine

To include this project into your app/game you need to add the following lines into the settings.gradle file :

	include ':gameEngine'
	project(':gameEngine').projectDir = new File(settingsDir , '../AndroidGameEngine/gameEngine')
	
and the following line into the build.gradle file in the dependencies:

	compile project(path: ':gameEngine')


## TODO improvment

- Implements collision check with Reycast instead of the AABB.

- Modify the structure for a multi-thread Engine architecture.



