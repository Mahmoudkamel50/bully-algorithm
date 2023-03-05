import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalTime;

public interface BullyNode extends Remote {
     int getId() throws RemoteException;
     void start() throws RemoteException;
     void sendMessage(int receiverId, int messageType) throws RemoteException;
     void receiveMessage(int senderId, int messageType);
     void startElection() throws RemoteException;
     void setCoordinator(int coordinatorId) throws RemoteException;
     void declareCoordinator() throws RemoteException;
     String getMessageTypeString(int messageType) throws RemoteException;
     public static LocalTime getCurrentTime() throws RemoteException {
          return LocalTime.now();
     }
}