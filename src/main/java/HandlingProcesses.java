import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;

public class HandlingProcesses extends ProcessInstanceImpl {
    int max = 1000;
    int numberOfProcess = 0;
    ProcessInstanceImpl[] list= new ProcessInstanceImpl[max];

    ProcessBuilder[] processBuilders = new ProcessBuilder[max];
    ProcessInterface[] processInterfacesStub = new ProcessInterface[max];
    Process[] processes = new Process[max];

    public HandlingProcesses(){
        init();
    }

    void init() {
        try {
            for (int i =0; i < max; i++){
                processBuilders[i] = new ProcessBuilder("java.exe", "-ba", "./*", "bullyAlgorithm.start",
                        ((Integer) i ).toString(), ((Integer)max).toString());
                processBuilders[i].redirectOutput(ProcessBuilder.Redirect.INHERIT);
                processBuilders[i].redirectError(ProcessBuilder.Redirect.INHERIT);
                list[i] = new ProcessInstanceImpl();
                processInterfacesStub[i] = (ProcessInterface) UnicastRemoteObject.exportObject(list[i],0);
                Registry registry = LocateRegistry.createRegistry(1000+1);
                registry.bind("Reg", processInterfacesStub[i]);
                processInterfacesStub[i].setId(i);
                processInterfacesStub[i].setAlive(false);
                processInterfacesStub[i].setCoordinator(false);
                processInterfacesStub[i].setMessageTime(LocalTime.now());
                processInterfacesStub[i].setMessage("");
            }
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean startProcess(int id) {
        try {
            if (processes[id] == null || !processes[id].isAlive()) {

                processes[id] = processBuilders[id].start();
                processInterfacesStub[id].setCoordinator(false);
                processInterfacesStub[id].setAlive(false);
                processInterfacesStub[id].setId(id);
                processInterfacesStub[id].setMessageTime(LocalTime.now().minusSeconds(10));
                processInterfacesStub[id].setMessage("");
                int currentCoordinator = getCurrentCoordinator();
                if (currentCoordinator != -1) {
                    processInterfacesStub[getCurrentCoordinator()].setCoordinator(false);
                }
                numberOfProcess++;
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean deleteProcess(int id){
        if (processes != null && processes[id].isAlive()){
            processes[id].destroy();
            numberOfProcess--;
            return true;
        }
        return false;
    }

    private int getCurrentCoordinator() {
        try {
            for (int i = 0; i < max ; i++){
                if (processes[i] != null && processes[i].isAlive()){
                    if (processInterfacesStub[i].isCoordinator()){
                        return i;
                    }
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public int[] getIds(){
        int[] aliveProcesses= new int[numberOfProcess];
        int id = 0;
        for (int i =0; i < max; i++){
            if (processes[i] != null && processes[i].isAlive()){
                aliveProcesses[id++] = i;
            }
        }
        return aliveProcesses;
    }

    public int getNumberOfProcess() {
        return numberOfProcess;
    }

    public void setNumberOfProcess(int numberOfProcess) {
        this.numberOfProcess = numberOfProcess;
    }

    public ProcessInterface[] getProcessInterfacesStub() {
        return processInterfacesStub;
    }

    public void setProcessInterfacesStub(ProcessInterface[] processInterfacesStub) {
        this.processInterfacesStub = processInterfacesStub;
    }
}
