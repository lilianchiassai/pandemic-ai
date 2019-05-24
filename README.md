# pandemic-ai
The project aims to create self learning artificial intelligence capable to beat the board game Pandemic.
As i know nothing about neural network and deep learning, I will start from scratch : no library will be used to manage neural network.

Current state of development : after a few months pause I have time to start on thep project again.
The following actions will be taken before advancing further in the project :
- proper logging
- maven migration to manage external libraries
- analysis on the impact of an external library to manage Tree/Nodes
- isolation of the game engine from the players (human or AI)
- clean and comment code


The steps
I Building a decent Pandemic Board game
1. Initializing the game (create architecture classes, useful methods) is its simplest way : no event card, no card limit, no character player
2. Add log4j to track
3. build the game mechanics : what the game do , effect of each action etc.
4. Create the game engine : waiting for input to perform player actions and automatically performing game actions 
5. Add eventhandling to throw event when : player hand is full or when it must make an action -> DONE
6. Build an interface to play pandemic and check everything runs smoothly

II Build a Monte Carlo Tree Search based AI and check if it works
1. Undertsanding MCTS algorithm https://int8.io/monte-carlo-tree-search-beginners-guide/
2. Implementing the algorithm
3. Testing & debugging
4. Optimization

III Build a neural network learning from the Monte Carlo Tree Search, Alphagozero's way

IV Standardize AI entry points and API to make it "game" independant

V Add events and characters role

