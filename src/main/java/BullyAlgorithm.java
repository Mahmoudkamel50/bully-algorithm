import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class BullyAlgorithm extends HandelMessage {
    private int id;
    private int max;

    public BullyAlgorithm(int id, int max) {
        this.id = id;
        this.max = max;
    }

    void start(){
        try {
            Registry registry = LocateRegistry.getRegistry(1000+1);
            ProcessInterface stub = (ProcessInterface) registry.lookup("reg");
            while (true){
                if (stub.isCoordinator()){
                    coordinatorToProcess(id, max);
                }else {
                    processToProcess(id, max);
                    updateCoordinatorMessageTime(stub, recieveMessage(stub));
                    if(checkCoordinatorDead(stub.getMessageTime())){
                        if (electionStart(id, max, stub)){
                            stub.setCoordinator(true);
                            winingElection(id, max);
                        }else {
                            waitF(5000L);
                        }
                    }
                }
                waitF(1000L);
            }
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void waitF(Long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean electionStart(int id, int max, ProcessInterface stub) {
        int higherId = 0;
        while (true){
            election(id, max);
            String[] message = recieveMessage(stub);
            if (checkHigherId(id, message)){
                return false;
            }else {
                higherId++;
            }
            if (higherId == 5){
                return true;
            }
            waitF(1000L);
        }
    }

    boolean checkHigherId(int id, String[] message) {
        for (int i =0; i < message.length; i++){
            if (Integer.parseInt((message[i].split(""))[1]) > id){
                return true;
            }
        }
        return false;
    }

    private boolean checkCoordinatorDead(LocalTime messageTime) {
        if (messageTime.until(LocalTime.now(),ChronoUnit.SECONDS) > 10){
            return true;
        }else {
            return false;
        }
    }

    void updateCoordinatorMessageTime(ProcessInterface stub, String[] message) throws RemoteException {
        for (int i =0; i < message.length; i++){
            if (message[i].contains(coordinatorAlive)){
                stub.setMessageTime(LocalTime.now());
            }
        }
    }
}
