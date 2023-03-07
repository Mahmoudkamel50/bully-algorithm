import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalTime;

public interface MessageSender extends Remote {
     void register(MessageListener listener) throws RemoteException;
     void electionMessage(int id) throws RemoteException, MalformedURLException, NotBoundException;
     void coordinatorMessage(int id) throws RemoteException;
}