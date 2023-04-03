public class StartProcess {
    public static void main(String[] args) {
        int id = Integer.parseInt(args[0]);
        int max = Integer.parseInt(args[1]);
        BullyAlgorithm bullyAlgorithm = new BullyAlgorithm(id, max);
        bullyAlgorithm.start();
    }
}
