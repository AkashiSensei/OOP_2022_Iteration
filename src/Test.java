import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        String inputLine;
        String[] paras;
        Boolean canDebug = args.length != 0;

        if(canDebug) {
            debug(args);
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            inputLine = scanner.nextLine();
            paras = inputLine.split("\\s+");

            runComm(paras, canDebug);
        }
    }

    public static void runComm(String[] paras, boolean canDebug) {
        switch (paras[0]) {

            case "QUIT" -> {
                System.out.println("----- Good Bye! -----");
                System.exit(0);
            }

            case "register" -> {
                User.register(paras);
            }
            case "login" -> {
                User.login(paras);
            }
            case "printInfo" -> {
                User.printInfo(paras);
            }
            case "logout" -> {
                User.logout(paras);
            }

            case "debug" -> {
                if(canDebug)
                    Debug.debugs(paras);
                else
                    System.out.println("command '" + paras[0] + "' not found");
            }

            default -> {
                System.out.println("command '" + paras[0] + "' not found");
            }
        }
    }

    public static void debug(String[] args) {
        for(String arg : args) {
            Debug.debug(Integer.parseInt(arg));
        }
    }
}
