# pandemic-ai
The project aims to create self learning artificial intelligence capable to beat the board game Pandemic.
As i know nothing about neural network and deep learning, I will start from scratch : no library will be used to manage neural network.

Future releases
- proper logging
- comment code
- provide use instruction
- update README with mardown syntax
- check MCTS algorithm implementation
- write JMH benchmark for critical methods
- optimize deck creation/duplication
- optimize gameState duplication
- junit tests for Pandemic.getMoves method
- provide a more optimized Pandemic.getMoves method

Changelog
2019.06.28
- Junit tests cover all pandemic package except material. JUnit tests to be added before any refactoring of the material package
- Deck, PlayerDeck, PropagationDeck refactored
- Benchmark deck critical operations (draw, duplicate)
- add equivalent method to test logical equality between objects

2019.06.20
 - Deprecated observer pattern migrated to PropertyChangeListener
 - JUnit tests in progress

2019.06.18
 - Refactored project architecture
 - JUnit tests in progress
 - Add maven build process
 - Improved algorithm to find all valuable combinations of 4 actions
 - JMH benchmark
 

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

