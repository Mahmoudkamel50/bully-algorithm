import java.rmi.RemoteException;
import java.time.LocalTime;

class ProcessInstanceImpl implements ProcessInterface {
    private  int id;
    private boolean isCoordinator;
    private boolean isAlive;
    private LocalTime localTime;
    private String message;


    @Override
    public int getId() throws RemoteException {
        return id;
    }

    @Override
    public void setId(int id) throws RemoteException {
        this.id = id;
    }

    @Override
    public boolean isCoordinator() throws RemoteException {
        return isCoordinator;
    }

    @Override
    public void setCoordinator(boolean isCoordinator) throws RemoteException {
        this.isCoordinator = isCoordinator;
    }

    @Override
    public boolean isAlive() throws RemoteException {
        return isAlive;
    }

    @Override
    public void setAlive(boolean isAlive) throws RemoteException {
        this.isAlive = isAlive;
    }

    @Override
    public LocalTime getMessageTime() throws RemoteException {
        return localTime;
    }

    @Override
    public void setMessageTime(LocalTime localTime) throws RemoteException {
        this.localTime = localTime;
    }

    @Override
    public String getMessage() throws RemoteException {
        return message;
    }

    @Override
    public void setMessage(String message) throws RemoteException {
        this.message = message;
    }

    @Override
    public void addMessage(String message) throws RemoteException {
        this.message += message;
    }
}