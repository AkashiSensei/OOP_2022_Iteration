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
            case 4 -> courseTest();
        }
    }

    public static void registerTest0() {
        System.out.println("---------- register test 0 start ----------");
        String[] paras;


        paras = new String[]{"registeer", "20374249", "Yizhou", "Liu", "20374249@buaa.edu.cn", "b20011114"};
        testComm(paras, "wrong command", "command 'registeer' not found");

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
        System.out.println("---------- printInfo test 0 start ----------");
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

    public static void courseTest() {
        System.out.println("---------- courseTest test 0 start ----------");
        String[] paras;

        paras = new String[]{"register", "20373252", "Yixiao", "Li", "20373252@buaa.edu.cn", "x12345678", "x12345678"};
        testComm(paras, "", "");

        paras = new String[]{"register", "19376054", "Hongxi", "Zhou", "19376054@buaa.edu.cn", "x12345678", "x12345678"};
        testComm(paras, "", "");

        paras = new String[]{"register", "10001","Xueping", "Shen", "10001@buaa.edu.cn", "x12345678", "x12345678"};
        testComm(paras, "", "");

        paras = new String[]{"login", "10001", "x12345678"};
        testComm(paras, "", "");

        paras = new String[]{"addCourse", "C2101", "oop_autumn"};
        testComm(paras, "", "");

        paras = new String[]{"listCourse"};
        testComm(paras, "", "");

        paras = new String[]{"selectCourse", "C2101"};
        testComm(paras, "", "");

        paras = new String[]{"addAdmin", "20373252"};
        testComm(paras, "", "");

        paras = new String[]{"listAdmin"};
        testComm(paras, "", "");

        paras = new String[]{"addTask", "T210101", "autumn_task1.txt", "2020-02-29-00:00:00", "2024-02-29-00:00:00"};
        testComm(paras, "", "");

        paras = new String[]{"listTask"};
        testComm(paras, "", "");

        paras = new String[]{"addWare", "W210101", "autumn_ware1.txt"};
        testComm(paras, "", "");

        paras = new String[]{"listWare"};
        testComm(paras, "", "");

        paras = new String[]{"addStudent", "19376054"};
        testComm(paras, "", "");

        paras = new String[]{"listStudent"};
        testComm(paras, "", "");

        paras = new String[]{"logout"};
        testComm(paras, "", "");

        paras = new String[]{"login", "20373252", "x12345678"};
        testComm(paras, "", "");

        paras = new String[]{"changeRole"};
        testComm(paras, "", "");

        paras = new String[]{"selectCourse", "C2101"};
        testComm(paras, "", "");

        paras = new String[]{"listAdmin"};
        testComm(paras, "", "");

        paras = new String[]{"listTask"};
        testComm(paras, "", "");

        paras = new String[]{"listWare"};
        testComm(paras, "", "");

        paras = new String[]{"listStudent"};
        testComm(paras, "", "");





        paras = new String[]{"logout"};
        testComm(paras, "", "");

        paras = new String[]{"login", "10001", "x12345678"};
        testComm(paras, "", "");

        paras = new String[]{"addCourse", "C2022", "nihao"};
        testComm(paras, "", "");

        paras = new String[]{"addCourse", "C2001", "pikaqiu"};
        testComm(paras, "", "");

        paras = new String[]{"listCourse"};
        testComm(paras, "", "");

        paras = new String[]{"removeCourse", "C2001"};
        testComm(paras, "", "");

        paras = new String[]{"listCourse"};
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
