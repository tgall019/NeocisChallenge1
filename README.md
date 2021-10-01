# Neocis Software Challenge Documentation

## Position: Internship - Software Engineering 

### Tyrone Gallardo

# Overview
The software assessment was divided into two parts. For the sake of simplicity, each part was made into its own individual project. 
The challenge was coded using Java16 in its entirety. This repository contains the first part of the project. Here, you can find the
source code, as well as a .bat and a runnable .jar file for easy execution. The rest of the project documentation, including an algorithm 
explanation and a system synopsis, can also be found in the repository.

Upon execution, a window will show displaying a 20x20 grid of `MyButton` squared buttons. The user can click a button and drag the 
cursor to display a dynamically-sized circle. Upon releasing the mouse click, an algorithm will map the circle to the squared buttons 
beneath it, creating a replica of it using said buttons in blue.

Additionally, two additional circles, sharing the same origin as the blue circle, will display in red when the mouse click is released. 
The outer red circle represents the smallest possible circle that **completely** **encapsulate*s*** all the squares marked in blue. The 
inner red circle represents the largest possible circle that is **completely encapsulat*ed*** by the squares marked in blue. The origin 
of these three circles will be marked in yellow upon release as well.

Parts of the source code will be commented and labeled as **IGNORE**; these areas of the code are not relevant to the implementation, 
but served as valuable experiences.
