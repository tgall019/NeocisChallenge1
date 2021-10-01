# Neocis Software Challenge Documentation

## Position: Internship - Software Engineering 

### Tyrone Gallardo

# Overview
The software assessment was divided into two parts. For the sake of simplicity, each part was made into its own individual project. 
The challenge was coded using Java16 in its entirety. This repository contains the first part of the project. Here, you can find the
source code, as well as a .bat and a runnable .jar file for easy execution.

Upon execution, a window will show displaying a 20x20 grid of `MyButton` squared buttons. The user can click a button and drag the 
cursor to display a dynamically-sized circle. Upon releasing the mouse click, an algorithm will map the circle to the squared buttons 
beneath it, creating a replica of it using said buttons in blue.

Additionally, two additional circles, sharing the same origin as the blue circle, will display in red when the mouse click is released. 
The outer red circle represents the smallest possible circle that **completely** **encapsulate*s*** all the squares marked in blue. The 
inner red circle represents the largest possible circle that is **completely encapsulat*ed*** by the squares marked in blue. The origin 
of these three circles will be marked in yellow upon release as well.

Parts of the source code will be commented and labeled as **IGNORE**; these areas of the code are not relevant to the implementation, 
but served as valuable experiences.

## Algorithm
A public static variable named `MoE` (Margin of Error) determines which squares will be marked blue. Each square has an object attribute 
named `center`, which helps determine the square’s relative position on the window. By knowing each square’s position, the distance 
between each square and the circle’s origin (another square) can be found using a simple method; the `distanceFrom()` method found in 
the `Coordinate` class serves this function. Only squares for which 
![img](file:///C:/Users/Owner/AppData/Local/Temp/msohtmlclip1/01/clip_image002.png), where 
![img](file:///C:/Users/Owner/AppData/Local/Temp/msohtmlclip1/01/clip_image004.png) is the distance between the square and the origin, 
and ![img](file:///C:/Users/Owner/AppData/Local/Temp/msohtmlclip1/01/clip_image006.png) is the circle’s radius, were set to blue. The 
`MoE` variable can be changed in-code, but a balance between effectiveness and precision is preferred: `8px`.

To make the red circle implementation work, additional attributes had to be added to each square. Rather than just knowing the 
coordinates for their center, knowing the coordinates for each corner of the square would be useful. For this purpose, four additional 
attributes `tl`, `bl`, `tr`, `br` (for top-left, bottom-left, top-right, bottom-right, respectively) were added. Next, all four corners 
of each blue square was measured against the origin, and the shortest and longest distances were recorded; these distances would become 
the radii of the inner and outer red circle, respectively

## Synopsis

When the project is executed, the following steps carry out in order:

1. The `main()` method is executed, which runs the `gui()` method in Line 90.
2. The `gui()` method will create a new frame instance, and call its class constructor in line 97.
   1. The constructor will set up every square button that will be displayed later, setting things like bounds, and backgrounds.
   2. The constructor will add a `MouseListener` and a `MouseMotionListener` using instances of the `Part1Listener` inner class.
   3. The constructor will then add each square button to the frame in line 55
3. The `gui()` method initiates the frame to be displayed, setting background, window size, and more.
   1. The `CirclePanel1()` panel is added to the frame in line 101, setting up a `Graphics` object that will act as the brush in 
   the frame.
   2. The `Graphics` object will immediately draw the three circles (1 blue and 2 red), but since the radii variables they are using 
   are momentarily set to 0, the circles will not be visible. The circles will display when these variables are given a value in a 
   future step.
   3. The frame will be set visible in line 103.
4. As soon as the window is visible and interactable, the listeners in each of the 400 square buttons will be awaiting events.
5. The user will proceed to click and drag from one of the squares, where a `MousePressed()` and a `MouseDragged`() method will be 
called through the `MouseListener` and the `MouseMotionListener`. 
   1. The `MousePressed()` method will store the position of the origin button that was first pressed.
   2. The `MouseDragged()` method will dynamically keep track of the mouse cursor's relative position to the circle's origin, and 
   store this distance in the `currentRadius1` variable. The `Graphics` object, activated by the `frame.update()` method, will use 
   this newly set distance $d \neq 0$ and change the radius of the blue circle.
   3. The user can see the blue circle being expanded dynamically as the mouse click is being dragged.
6. When the user releases the mouse click, a `MouseEvent` will trigger the `mouseReleased()` method, triggering three main additional methods:
   1. The `setBtnBlue()` method will be called in line 190, turning squares blue as indicated in the algorithm explanation above.
   2. The `findRedCircles()` method will be called in line 191, assigning values to the `farthestRadius1` and `closestRadius1` variables as 
   indicated in the algorithm explanation above. These radii are then used by the `Graphics` object to display the two red circles.
   3. The `frame.update()` method will update the graphics to the newly set radii.
