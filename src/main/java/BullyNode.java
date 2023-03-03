import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BullyNode extends Remote {
    public int getId() throws RemoteException;
    public void start() throws RemoteException;
    public void sendMessage(String message) throws RemoteException;
    public void receiveMessage(int senderId, String message) throws RemoteException;
    public void startElection() throws RemoteException;
    public void setCoordinator(int coordinatorId) throws RemoteException;
    public void declareCoordinator() throws RemoteException;
}