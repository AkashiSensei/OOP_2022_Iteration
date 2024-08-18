import java.util.Scanner;
import java.util.stream.StreamSupport;

public class Debug {

    public static void init() {
        System.out.println("---------- init ----------");

        //调试参数 0 预执行代码块




        System.out.println();
        System.out.println();
        System.out.println();
    }

    public static void debugs(String[] paras) {
        for(int i = 1; i < paras.length; i++) {
            debug(Integer.parseInt(paras[i]));
        }
    }

    public static void debug(int code) {
        switch (code) {
            case 1 -> registerTest0();
            case 2 -> logTest0();
            case 3 -> printInfoTest0();
        }
    }

    public static void registerTest0() {
        System.out.println("---------- register test 0 start ----------");
        String[] paras;


        paras = new String[]{"registeer", "20374249", "Yizhou", "Liu", "20374249@buaa.edu.cn", "b20011114"};
        testComm(paras, "wrong command", "arguments illegal");

        paras = new String[]{"register", "20374249", "Yizhou", "Liu", "20374249@buaa.edu.cn", "b20011114"};
        testComm(paras, "less arguments", "arguments illegal");

        paras = new String[]{"register", "203742", "Yizhou", "Liu", "20374249@buaa.edu.cn", "b20011114", "b20011114"};
        testComm(paras, "shorter ID", "user id illegal");

        paras = new String[]{"register", "20374249", "yizhou", "liu", "20374249@buaa.edu.cn", "b20011114", "b20011114"};
        testComm(paras, "name start with lower", "user name illegal");

        paras = new String[]{"register", "20374249", "Yizhou", "Liu", "20374249@buaa", "b20011114", "b20011114"};
        testComm(paras, "wrong email", "email address illegal");

        paras = new String[]{"register", "20374249", "Yizhou", "Liu", "20374249@buaa.edu.cn", "20011114", "20011114"};
        testComm(paras, "wrong password", "password illegal");

        paras = new String[]{"register", "20374249", "Yizhou", "Liu", "20374249@buaa.edu.cn", "b20011114", "20011114"};
        testComm(paras, "password disaccord", "passwords inconsistent");

        paras = new String[]{"register", "20374249", "Yizhou", "Liu", "20374249@buaa.edu.cn", "b20011114", "b20011114"};
        testComm(paras, "OK", "register success");

        paras = new String[]{"register", "20374249", "Pika", "Qiu", "20374249@buaa.edu.cn", "b20011114", "b20011114"};
        testComm(paras, "id duplication", "user id duplication");

        paras = new String[]{"register", "BY2021345", "Pika", "Qiu", "BY2021345@buaa.edu.cn", "BY2021345", "BY2021345"};
        testComm(paras, "register graduate", "register success");

        paras = new String[]{"register", "21345", "Lifang", "Liang", "Lianglifang@buaa.edu.cn", "fangfang", "fangfang"};
        testComm(paras, "register teacher", "register success");

        System.out.println();
        System.out.println();
        System.out.println();
    }

    public static void logTest0() {
        System.out.println("---------- login & logout test 0 start ----------");
        String[] paras;

        paras = new String[]{"logout", "21345", "fangfang"};
        testComm(paras, "", "");

        paras = new String[]{"logout"};
        testComm(paras, "", "");

        paras = new String[]{"login", "213456", "fangfang"};
        testComm(paras, "", "");

        paras = new String[]{"login", "21345", "fang"};
        testComm(paras, "", "");

        paras = new String[]{"login", "20374249", "b20011114"};
        testComm(paras, "OK", "");

        paras = new String[]{"logout"};
        testComm(paras, "OK", "");

        System.out.println();
        System.out.println();
        System.out.println();
    }

    public static void printInfoTest0() {
        System.out.println("---------- register test 0 start ----------");
        String[] paras;

        paras = new String[]{"logout"};
        testComm(paras, "OK", "");

        paras = new String[]{"login", "21345", "fangfang"};
        testComm(paras, "", "");

        paras = new String[]{"printInfo", "21345", "fang"};
        testComm(paras, "", "");

        paras = new String[]{"printInfo", "BY2021345"};
        testComm(paras, "", "");

        paras = new String[]{"printInfo", "BY202"};
        testComm(paras, "", "");

        paras = new String[]{"printInfo", "BY2021344"};
        testComm(paras, "", "");

        paras = new String[]{"printInfo"};
        testComm(paras, "", "");

        paras = new String[]{"logout"};
        testComm(paras, "OK", "");

        paras = new String[]{"login", "20374249", "b20011114"};
        testComm(paras, "OK", "");

        paras = new String[]{"printInfo", "BY2021345"};
        testComm(paras, "", "");

        paras = new String[]{"printInfo"};
        testComm(paras, "", "");

        System.out.println();
        System.out.println();
        System.out.println();
    }








    public static void showCommand(String[] paras, String info, String except) {
        System.out.println();
        System.out.print("INPUT <- ");
        for(String para : paras) {
            System.out.print(para + " ");
        }
        System.out.println("| " + info);
        System.out.print(except + " | ");
    }

    public static void testComm(String[] paras, String info, String except) {
        showCommand(paras, info, except);
        Test.runComm(paras, false);
    }

    public static void testComm(String[] paras) {
        testComm(paras, "", "");
    }

}
