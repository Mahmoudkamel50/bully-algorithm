import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HandelMessage {
    String coordinatorAlive = "Coordinator Alive";
    String processAlive = "Process Alive";
    String election = "Election";
    String electionWon = "Election Won";

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH : mm : ss");
    void processToProcess(int id, int max){
        sendMessage(createMessage(processAlive, id), id, max);
    }
    void coordinatorToProcess(int id, int max){
        sendMessage(createMessage(coordinatorAlive, id), id, max);
    }void election(int id, int max){
        sendMessage(createMessage(election, id), id, max);
    }void winingElection(int id, int max){
        sendMessage(createMessage(electionWon, id), id, max);
    }


    void sendMessage(String message, int id, int max){
        try {
            Registry registry;
            ProcessInterface stub;
            for (int i = 0; i < max; i++){
                if (i != id){
                    registry = LocateRegistry.getRegistry(1000+1);
                    stub = (ProcessInterface) registry.lookup("reg");
                    stub.addMessage(message);
                }
            }
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    String[] recieveMessage(ProcessInterface stub){
        String[] message = new String[0];
        try {
            if (stub.getMessage().length() != 0){
                message= stub.getMessage().split("\n");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

     String createMessage(String message, int id) {
        return LocalTime.now().format(dateTimeFormatter) + " " + ((Integer) id) + message + "\n" ;
    }
}
