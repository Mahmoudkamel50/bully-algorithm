import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class BullyNodeImpl extends UnicastRemoteObject implements BullyNode {
    private static final long serialVersionUID = 1L;
    private static final int ELECTION_MESSAGE = 1;
    private static final int COORDINATOR_MESSAGE = 2;

    private List<Integer> higherProcesses = new ArrayList<>();
    private List<Integer> lowerProcesses = new ArrayList<>();
    private int id;
    private int coordinatorId;
    private Map<Integer, BullyNode> nodes;
    private boolean isCoordinator;

    public void BullyNode(int id, Map<Integer, BullyNode> nodes) {
        this.id = id;
        this.nodes = nodes;
        this.isCoordinator = false;
    }

    protected BullyNodeImpl(int processId) throws RemoteException {
    }

    public int getId() throws RemoteException {
        return this.id;
    }

    @Override
    public void start() throws RemoteException {
        System.out.printf("[%d] Starting node%n", this.id);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isCoordinator) {
                    BullyNode coordinator = nodes.get(coordinatorId);
                }
            }
        }, 3000);
    }

    public void sendMessage(int senderId, int messageType) throws RemoteException {
        LocalDateTime currentTime = LocalDateTime.now();
        String message = getMessageTypeString(messageType);
        System.out.println(getCurrentTime() + " Process " + id + " sends " + message + " message to Process " + senderId);
        receiveMessage(senderId, messageType);
    }


    public void startElection() throws RemoteException {
        System.out.printf("[%d] Starting election%n", this.id);
        boolean higherExists = false;
        for (int nodeId : this.nodes.keySet()) {
            if (nodeId > this.id) {
                BullyNode node = this.nodes.get(nodeId);
                higherExists = true;
                break;
            }
        }
        if (!higherExists) {
            this.declareCoordinator();
        }
    }

    public void declareCoordinator() throws RemoteException {
        System.out.printf("[%d] Declaring self as coordinator%n", this.id);
        this.isCoordinator = true;
        this.coordinatorId = this.id;
        this.sendMessage(id,COORDINATOR_MESSAGE);
        for (int nodeId : this.nodes.keySet()) {
            if (nodeId != this.id) {
                this.nodes.get(nodeId).setCoordinator(this.id);
            }
        }
    }

    public static String getCurrentTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return formatter.format(currentTime);
    }

    @Override
    public void receiveMessage(int senderId, int messageType) {
        LocalDateTime currentTime = LocalDateTime.now();
        String message = getMessageTypeString(messageType);
        System.out.println(getCurrentTime() + " Process " + id + " receives " + message + " message from Process " + senderId);
        if (messageType == ELECTION_MESSAGE) {
            try {
                sendMessage(senderId, COORDINATOR_MESSAGE);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            higherProcesses.add(senderId);
        } else if (messageType == COORDINATOR_MESSAGE) {
            higherProcesses.remove(senderId);
            lowerProcesses.add(senderId);
        }

    }

    public void setCoordinator(int coordinatorId) throws RemoteException {
        System.out.printf("[%d] Received coordinator update: %d%n", this.id, coordinatorId);
        this.coordinatorId = coordinatorId;
        this.isCoordinator = false;
    }

    public String getMessageTypeString(int messageType) {
        switch (messageType) {
            case ELECTION_MESSAGE:
                return "ELECTION";
            case COORDINATOR_MESSAGE:
                return "COORDINATOR";
            default:
                return "";
        }
    }

    public static void main(String[] args) throws RemoteException {
        int processId;
        if (args.length > 0) {
            processId = Integer.parseInt(args[0]);
        } else {
            processId = new Random().nextInt(100) + 1;
        }
        System.out.println(getCurrentTime() + " Process " + processId + " started");

        BullyNodeImpl bullyNode = new BullyNodeImpl(processId);
        bullyNode.start();
    }
}