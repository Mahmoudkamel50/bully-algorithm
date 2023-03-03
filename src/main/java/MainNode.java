import java.rmi.RemoteException;
import java.util.Scanner;

public class MainNode {
    public static void main(String[] args) {
        Scanner se = new Scanner(System.in);
        int numberOfProcess = se.nextInt();

        BullyNode[] nodes = new BullyNode[numberOfProcess];
        for (int i =0; i < numberOfProcess; i++){
            try {
                nodes[i].start();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
