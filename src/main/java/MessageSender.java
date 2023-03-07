import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalTime;

public interface MessageSender extends Remote {
     void register(MessageListener listener) throws RemoteException;
     void electionMessage(int id) throws RemoteException;
     void coordinatorMessage(int id) throws RemoteException;
}