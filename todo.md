# Tests
- ClevelandAlgo
- PartitionByBisimilarity
- getLastCommonBlock
- getDistinguishingFormula




# Known Bugs
- Try to reproduce with high numbers of nodes:
  - One time 50+ nodes with random edges -> colors were wrong


# Refactor
## Vertex Class and Creation Chain
- When are vertices created?
  - User clicks on canvas
  - User Asks for a number of vertices
  -> Only when users interact with the UI to deliberately create one

- What do we need to consider when we create new vertices?
  - Order of creation: View first or model first?
  -> Logical answer: Model first
    - Problem: When a user clicks on the canvas, the vertex needs to be created at that exact spot
    , but the vertex class in the model shouldn't contain coordinates.
    -> Coordinates need to be stored separately and are best given to the model as well
    -> model.addVertexAtPosition(ID,POS);
      - needs to call model.addVertex(ID)
      - needs to signal that the Canvas should draw the vertex
      - needs to signal the position on the canvas
      - -> Separation of concerns: We should be able to call Algorithms without drawing the graph
       
## Draw a graph
---
Explain the chain of commands when we need to draw a graph or parts of it
- todo: what happens when a draw request cannot be fulfilled?
---
        - Scenario 1: add single vertices/edges
          - view detects a request by user and notifies controller
          - controller tells model to add a vertex / an edge
          - model adds vertex / edge via business logic
            - if successful, viewmodel will receive updates via observation
            - if not successful
              - don't do anything OR
              - model gives feedback that it didn't work
                -> message viewmodel hands feedback to view
          - viewmodel creates the new visual object that needs to be added to the view
          -> QUESTION: Which component is responsible for handling communication of an impossible request?
          -> EXAMPLE: We want to add 
        - Scenario 2: have a whole graph and draw it
          - iterate through vertices, draw them
          - iterate through edges, draw them
          - Because if you iterate through edges midway, you'd have to consider visited vertices
    
## Why we use our own graph, not an existing library
- It was hard to find a graph that 