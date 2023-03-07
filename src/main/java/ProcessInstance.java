import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

class ProcessInstance extends UnicastRemoteObject implements MessageListener {
    private int id;
    private int coordinatorId;
    private List<Integer> runningInstances;
    private boolean isCoordinator;

    public ProcessInstance(int id) throws RemoteException {
        super();
        this.id = id;
        this.isCoordinator = false;
        this.runningInstances = new ArrayList<>();
        this.runningInstances.add(id);
    }

    public ProcessInstance() throws RemoteException {
        this((int) (Math.random() * 100));
    }

    public void run() throws RemoteException, MalformedURLException, NotBoundException {
        printLog("Starting process with ID " + this.id);
        lookupOtherProcesses();
        initiateElection();
    }

    private void lookupOtherProcesses() throws RemoteException {
        for (int i = 0; i < 100; i++) {
            try {
                if (i != id) {
                    String name = "ProcessInstance_" + i;
                    MessageSender receiver = (MessageSender) Naming.lookup(name);
                    receiver.register(this);
                }
            } catch (Exception e) {
                // ignore
            }
        }
    }

    private void initiateElection() throws RemoteException, MalformedURLException, NotBoundException {
        printLog("Starting election...");
        int maxId = Collections.max(runningInstances);
        if (maxId == id) {
            becomeCoordinator();
            broadcastCoordinator();
        } else {
            MessageSender receiver = (MessageSender) Naming.lookup("ProcessInstance_" + maxId);
            receiver.electionMessage(this.id);
        }
    }

    private void becomeCoordinator() {
        this.isCoordinator = true;
        this.coordinatorId = this.id;
        printLog("Becoming coordinator.");
    }

    private void broadcastCoordinator() throws RemoteException, MalformedURLException, NotBoundException {
        for (int i : runningInstances) {
            if (i != this.id) {
                MessageSender receiver = (MessageSender) Naming.lookup("ProcessInstance_" + i);
                receiver.coordinatorMessage(this.coordinatorId);
            }
        }
    }

    public void electionMessage(int id) throws RemoteException, MalformedURLException, NotBoundException {
        if (id > this.id) {
            MessageSender receiver = (MessageSender) Naming.lookup("ProcessInstance_" + id);
            receiver.electionMessage(this.id);
        } else {
            initiateElection();
        }
    }

    public void coordinatorMessage(int id) throws RemoteException {
        if (this.coordinatorId != id) {
            this.coordinatorId = id;
            printLog("New coordinator detected: " + this.coordinatorId);
        }
    }

    public void register(MessageListener listener) {
        // Do nothing
    }

    public void receiveMessage(String message) {
        printLog("Received message: " + message);
    }

    private void printLog(String message) {
        System.out.println(new Date() + " Process " + id + ": " + message);
    }

    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
        int numProcesses = Integer.parseInt(args[0]);
        for (int i = 0; i < numProcesses; i++) {
            ProcessInstance instance = new ProcessInstance();
            Naming.rebind("ProcessInstance_" + instance.id, instance);
            instance.run();
        }
    }
}