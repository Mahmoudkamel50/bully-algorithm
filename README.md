# bully-algorithm-with-rmi

### This task is a programming exercise that requires implementing the Bully Election Algorithm to elect a coordinator from multiple instances of a program. The program ### instances must communicate with each other using Interprocess Communication (IPC) mechanisms.

### The task requires implementing the Bully Election Algorithm, which is a distributed algorithm for electing a coordinator from among a group of processes. 
### The algorithm works as follows:

### When a new process starts, it checks whether a coordinator exists by sending a message to all processes with a higher priority number.
### If it receives no response from a higher priority process, the process declares itself as the coordinator.
### If it receives a response from a higher priority process, it starts an election by sending a message to all processes with a higher priority number.
### If it receives no response from higher priority processes, it declares itself the coordinator and notifies all other processes.
### If it receives a response from a higher priority process during an election, it stops the election and waits for a new election to be started.

### The program communicate using one of the IPC mechanisms and print to the console a time-stamped message for any kind of communication either sent or received.
### Each new instance that runs should wait to detect the current existing coordinator at some pre-configured interval (3 seconds, for example). If the coordinator is ### not detected, then it should declare itself as coordinator.
