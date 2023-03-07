import java.rmi.RemoteException;

public interface MessageListener {
    void receiveMessage(String message) throws RemoteException;
}
