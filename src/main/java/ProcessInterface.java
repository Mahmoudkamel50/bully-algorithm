// try to solve the task by different way

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalTime;

public interface ProcessInterface extends Remote {
   int getId() throws RemoteException;
   void setId(int id) throws RemoteException ;
   boolean isCoordinator() throws RemoteException;
   void setCoordinator(boolean isCoordinator) throws RemoteException;
   boolean isAlive() throws RemoteException;
   void setAlive(boolean isAlive) throws RemoteException;
   LocalTime getMessageTime() throws RemoteException;
   void setMessageTime(LocalTime localTime) throws RemoteException;
   String getMessage() throws RemoteException;
   void setMessage(String message) throws RemoteException;


   void addMessage(String message) throws RemoteException;
}
