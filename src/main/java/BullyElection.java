// try to solve the task by different way

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BullyElection {
    private static int instanceId;
    private static boolean isCoordinator = false;
    private static final boolean isRunning = true;
    private static final List<Integer> processes = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length > 0) {
            instanceId = Integer.parseInt(args[0]);
        } else {
            instanceId = new Random().nextInt(100) + 1;
        }

        processes.add(instanceId);

        System.out.println("Instance " + instanceId + " started.");

        Thread messageReceiver = new Thread(() -> {
            while (isRunning) {
                for (int process : processes) {
                    if (process == instanceId) {
                        continue;
                    }
                    // message receiving
                    if (new Random().nextInt(10) > 7) {
                        System.out.println(new Date() + " - " + instanceId + " received message from " + process);
                        bullyElection(process);
                    }
                }
            }
        });

        messageReceiver.start();

        while (isRunning) {
            try {
                TimeUnit.SECONDS.sleep(3);
                detectCoordinator();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void detectCoordinator() {
        boolean coordinatorFound = false;
        for (int process : processes) {
            // coordinator detection
            if (new Random().nextInt(10) > 7) {
                System.out.println(new Date() + " - " + instanceId + " detected coordinator " + process);
                coordinatorFound = true;
                break;
            }
        }

        if (!coordinatorFound) {
            // Start election
            bullyElection(instanceId);
        }
    }

    private static void bullyElection(int otherProcess) {
        if (instanceId < otherProcess) {
            System.out.println(new Date() + " - " + instanceId + " received election message from " + otherProcess);
            // Send Ok message
            System.out.println(new Date() + " - " + instanceId + " sending OK message to " + otherProcess);
            //  message sending
            if (new Random().nextInt(10) > 7) {
                processes.remove(otherProcess);
                System.out.println(new Date() + " - " + instanceId + " removed process " + otherProcess);
            }
        } else if (!isCoordinator) {
            // Start election
            System.out.println(new Date() + " - " + instanceId + " sending election message to " + otherProcess);
            //  message sending
            if (new Random().nextInt(10) > 7) {
                processes.remove(otherProcess);
                System.out.println(new Date() + " - " + instanceId + " removed process " + otherProcess);
            }
        }
        // Check if we are the coordinator
        checkCoordinator();
    }

    private static void checkCoordinator() {
        if (!isCoordinator && processes.size() == 1 && processes.get(0) == instanceId) {
            // Declare as coordinator
            System.out.println(new Date() + " - " + instanceId + " declared itself as coordinator.");
            isCoordinator = true;
        }
    }
}
