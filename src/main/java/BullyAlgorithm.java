// Another way to solve the task

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BullyAlgorithm {

    private static final int ELECTION_MESSAGE = 1;
    private static final int COORDINATOR_MESSAGE = 2;

    private int processId;
    private boolean isCoordinator;
    private List<Integer> higherProcesses = new ArrayList<>();
    private List<Integer> lowerProcesses = new ArrayList<>();

    public BullyAlgorithm(int processId) {
        this.processId = processId;
    }

    public void run() {
        // wait for other processes to start
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // send an election message to higher numbered processes
        for (Integer higherProcess : higherProcesses) {
            sendMessage(higherProcess, ELECTION_MESSAGE);
        }

        // wait for responses from higher numbered processes
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // if no response received from higher numbered processes, declare itself as coordinator
        if (!isCoordinator && higherProcesses.isEmpty()) {
            isCoordinator = true;
            System.out.println(getCurrentTime() + " Process " + processId + " is the new coordinator");
            for (Integer lowerProcess : lowerProcesses) {
                sendMessage(lowerProcess, COORDINATOR_MESSAGE);
            }
        }
    }

    private void sendMessage(int receiverId, int messageType) {
        // sending message to receiver process
        LocalDateTime currentTime = LocalDateTime.now();
        String message = getMessageTypeString(messageType);
        System.out.println(getCurrentTime() + " Process " + processId + " sends " + message + " message to Process " + receiverId);
        receiveMessage(receiverId, messageType);
    }

    private void receiveMessage(int senderId, int messageType) {
        // receiving message from sender process
        LocalDateTime currentTime = LocalDateTime.now();
        String message = getMessageTypeString(messageType);
        System.out.println(getCurrentTime() + " Process " + processId + " receives " + message + " message from Process " + senderId);
        if (messageType == ELECTION_MESSAGE) {
            sendMessage(senderId, COORDINATOR_MESSAGE);
            higherProcesses.add(senderId);
        } else if (messageType == COORDINATOR_MESSAGE) {
            higherProcesses.remove(senderId);
            lowerProcesses.add(senderId);
        }
    }

    private String getMessageTypeString(int messageType) {
        switch (messageType) {
            case ELECTION_MESSAGE:
                return "ELECTION";
            case COORDINATOR_MESSAGE:
                return "COORDINATOR";
            default:
                return "";
        }
    }

    private static String getCurrentTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return formatter.format(currentTime);
    }

    public static void main(String[] args) {
        int processId;
        if (args.length > 0) {
            processId = Integer.parseInt(args[0]);
        } else {
            processId = new Random().nextInt(100) + 1;
        }
        System.out.println(getCurrentTime() + " Process " + processId + " started");

        BullyAlgorithm bullyAlgorithm = new BullyAlgorithm(processId);
        bullyAlgorithm.run();
    }
}
