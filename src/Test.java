import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;

public class Test {
    private static final Scanner scanner = new Scanner(System.in);
    public static final String PARA_ILLEGAL = "arguments illegal";

    public static void main(String[] args) {
        String inputLine;
        String[] paras;
        Boolean canDebug = args.length != 0;

        if(canDebug) {
            debug(args);
        }

        while (true) {
            inputLine = scanner.nextLine();
            paras = inputLine.split("\\s+");

            runComm(paras, canDebug);
        }
    }

    public static void runComm(String[] paras, boolean canDebug) {
        switch (paras[0]) {
            case "" -> {
                //System.out.println(paras[1]);
            }

            case "QUIT" -> {
                try {
                    deleteData();
                } catch (IOException e) {
                    System.out.println("delete data error" + e);
                }
                System.out.println("----- Good Bye! -----");
                System.exit(0);
            }

            case "register" -> User.register(paras);
            case "login" -> User.login(paras);
            case "printInfo" -> User.printInfo(paras);
            case "logout" -> User.logout(paras);

            case "debug" -> {
                if(canDebug)
                    Debug.debugs(paras);
                else
                    System.out.println("command '" + paras[0] + "' not found");
            }




            case "addCourse" -> Course.addCourse(paras);

            case "removeCourse" -> Course.removeCourse(paras);

            case "listCourse" -> Course.listCourse(paras);

            case "selectCourse" -> Course.selectCourse(paras);

            case "addAdmin" -> Course.addAdmin(paras);

            case "removeAdmin" -> Course.removeAdmin(paras);

            case "listAdmin" -> Course.listAdmin(paras);

            case "changeRole" -> Course.changeRole(paras);

            case "addWare" -> Course.addWare(paras);

            case "removeWare" -> Course.removeWare(paras);

            case "listWare" -> Course.listWare(paras);

            case "addTask" -> Course.addTask(paras);

            case "removeTask" -> Course.removeTask(paras);

            case "listTask" -> Course.listTask(paras);

            case "addStudent" -> Course.addStudent(paras);

            case "removeStudent" -> Course.removeStudent(paras);

            case "listStudent" -> Course.listStudent(paras);




            case "downloadFile" -> Course.downloadFile(paras);

            case "openFile" -> Course.openFile(paras);

            case "submitTask", "addAnswer" -> Course.submitTaskOrAnswer(paras);

            case "queryScore" -> Course.queryScore(paras);



            case "requestVM" -> Course.requestVM(paras);

            case "startVM" -> Course.startVM(paras);

            case "clearVM" -> Course.clearVM(paras);
            case "logVM" -> Course.logVM(paras);
            case "uploadVM" -> Course.upLoadVM(paras);
            case "downloadVM" -> Course.downloadVM(paras);

            default -> System.out.println("command '" + paras[0] + "' not found");
        }
    }

    public static void debug(String[] args) {
        for(String arg : args) {
            Debug.debug(Integer.parseInt(arg));
        }
    }

    public static void deleteData() throws IOException {
        System.gc();
        if(!new File("./data").exists()) {
            return;
        }

        Files.walkFileTree(Path.of("./data"),
                new SimpleFileVisitor<>() {
                    // 先去遍历删除文件
                    @Override
                    public FileVisitResult visitFile(Path file,
                                                     BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        //System.out.printf("文件被删除 : %s%n", file);
                        return FileVisitResult.CONTINUE;
                    }

                    // 再去遍历删除目录
                    @Override
                    public FileVisitResult postVisitDirectory(Path dir,
                                                              IOException exc) throws IOException {
                        Files.delete(dir);
                        //System.out.printf("文件夹被删除: %s%n", dir);
                        return FileVisitResult.CONTINUE;
                    }

                }
        );

    }

    public static Scanner getScanner() {
        return scanner;
    }

}
