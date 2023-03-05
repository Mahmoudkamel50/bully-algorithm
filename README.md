# bully-algorithm-with-rmi

This task is a programming exercise that requires implementing the Bully Election Algorithm to elect a coordinator from multiple instances of a program. The program instances must communicate with each other using Interprocess Communication (IPC) mechanisms.

The task requires implementing the Bully Election Algorithm, which is a distributed algorithm for electing a coordinator from among a group of processes. The algorithm works as follows:

When a new process starts, it checks whether a coordinator exists by sending a message to all processes with a higher priority number.
If it receives no response from a higher priority process, the process declares itself as the coordinator.
If it receives a response from a higher priority process, it starts an election by sending a message to all processes with a higher priority number.
If it receives no response from higher priority processes, it declares itself the coordinator and notifies all other processes.
If it receives a response from a higher priority process during an election, it stops the election and waits for a new election to be started.
To complete the task, you need to write a program that can communicate with other instances of the same program using IPC. The program should be able to run multiple instances, each representing a node in the Bully Election Algorithm. Each instance should have a unique number, which will be used to determine the instance's priority in the Bully Algorithm. If no argument is passed to the instance, it can use the current process ID as its number.

The program should communicate using one of the IPC mechanisms and print to the console a time-stamped message for any kind of communication either sent or received. Each new instance that runs should wait to detect the current existing coordinator at some pre-configured interval (3 seconds, for example). If the coordinator is not detected, then it should declare itself as coordinator.

The coordinator should distribute a task among the other processes. Each process will get a subtask and return the result to the coordinator. The coordinator will create a large array in memory that contains random numbers. The coordinator will divide the array into chunks, one chunk for each instance, and send a chunk to each instance. Each instance will look for the minimum value in the chunk it has and return the result to the coordinator. When the coordinator receives all the responses from all the processes, it will find the minimum value in all the responses and display it to the user.

you can use any IPC mechanism supported by your programming language, such as pipes, sockets, rmi, or shared memory. You should not use libraries like OpenMP or MPI to implement IPC. You should implement the Bully Algorithm as separate processes, not threads.
