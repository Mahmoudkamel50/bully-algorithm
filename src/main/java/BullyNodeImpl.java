import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

class BullyNodeImpl extends UnicastRemoteObject implements BullyNode {
    private static final long serialVersionUID = 1L;
    private int id;
    private int coordinatorId;
    private Map<Integer, BullyNode> nodes;
    private boolean isCoordinator;
    static BullyNode stub1;

    public void BullyNode(int id, Map<Integer, BullyNode> nodes) throws RemoteException {
        this.id = id;
        this.nodes = nodes;
        this.isCoordinator = false;
    }

    protected BullyNodeImpl() throws RemoteException {
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

    public void sendMessage(String message) throws RemoteException {
        System.out.printf("[%d] Sent message: %s%n", this.id, message);
        for (BullyNode node : this.nodes.values()) {
            if (node.getId() != this.id) {
                node.receiveMessage(this.id, message);
            }
        }
    }

    public void receiveMessage(int senderId, String message) throws RemoteException {
        System.out.printf("[%d] Received message: %s from %d%n", this.id, message, senderId);
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
        this.sendMessage(String.format("Coordinator elected: %d", this.id));
        for (int nodeId : this.nodes.keySet()) {
            if (nodeId != this.id) {
                this.nodes.get(nodeId).setCoordinator(this.id);
            }
        }
    }

    public void setCoordinator(int coordinatorId) throws RemoteException {
        System.out.printf("[%d] Received coordinator update: %d%n", this.id, coordinatorId);
        this.coordinatorId = coordinatorId;
        this.isCoordinator = false;
    }

    public static void main(String[] args) throws RemoteException {
            BullyNodeImpl node = new BullyNodeImpl();
            try {
                stub1 = (BullyNode) UnicastRemoteObject.exportObject(node, 0);
                Registry registry= LocateRegistry.getRegistry();
                registry.bind("stub1", stub1);

                System.err.println("Node stub1 is ready");
                stub1.startElection();
            } catch (AlreadyBoundException e) {
                e.printStackTrace();
            }
    }
}