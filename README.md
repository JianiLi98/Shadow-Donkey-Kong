# Shadow Donkey Kong - Project 2B

## Assumptions & Design Decisions

This section documents reasonable assumptions made during the implementation of Project 2B, in accordance with the specification guidelines.
### 1. Monkey Edge Detection and Turnaround
Monkeys reverse direction when the entities' right / left edge has reached the right / left edge of the **platform** they are currently walking on.

This ensures that monkeys never fall off platforms and their route resets appropriately, matching the example behaviour described in the specification.

### 2. Hammer Initialization
Each level is assumed to contain **only one hammer** and hammer has been stored as one single instance.

### 3. Intelligent Monkey and Banana Interaction
When an **intelligent monkey** is destroyed, any existing **banana projectiles** fired by that monkey remain on-screen and behave normally until their travel distance is exceeded. 

This aligns with the specification, which permits either behaviour as long as it is documented.

### 4. Score Display Transition Between Levels
At the start of **Level 2**, the on-screen score display resets to 0. Internally, Level 1’s score is preserved. When the player proceeds to win Level 2:
- The final score displayed on the **Game Over** screen includes:
    - Level 1 score
    - Level 2 score
    - Time bonus from Level 2 only  

### 5. Gravity and Fall Speed
The **maximum fall speed is 10 pixels/frame** for all gravity-affected entities. 
This is a global value ensuring consistent gameplay physics across all entities.

### 6. Reasoning for use of Interface for GameScreen
The **GameScreen** type is implemented as an **interface**, because:
- It represents **shared behaviour** (via `update()` and `showScreenText()` methods),
- But not **shared state** or fields.
  This design promotes loose coupling and ensures that each screen (home, gameplay, game over) implements its own logic independently, following best OOSD practices.

---
