import java.util.ArrayList;
import java.util.TreeMap;

public class User {
    public static final String ALREADY_LOGIN = "already logged in";
    public static final String ID_NO_EXIST = "user id not exist", ID_ILLEGAL = "user id illegal";
    public static final String NO_LOGIN = "not logged in";
    public static final String RIGHT_ERROR = "permission denied";
    public static final int USER_TYPE_UNDER = 0, USER_TYPE_GRADUATE = 1, USER_TYPE_TEACHER = 2;


    private static ArrayList<User> users = new ArrayList<>();

    private static User userNow = null;


    private final int type;
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final TreeMap<String, Course> adminCourses = new TreeMap<>();
    private final TreeMap<String, Course> learnCourses = new TreeMap<>();
    private final TreeMap<String, VirtualMachine> vms = new TreeMap<>();

    public User(int type, String id, String firstName, String lastName, String email, String password) {
        this.type = type;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }


    public static void register(String [] para) {

        if(para.length != 7) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(userNow != null) {
            System.out.println(ALREADY_LOGIN);
            return;
        }

        if(!isIdLegal(para[1])) {
            return;
        }
        if(isIdDuplicate(para[1])) {
            return;
        }

        if(!isNameLegal(para[2])) {
            return;
        }
        if(!isNameLegal(para[3])) {
            return;
        }

        if(!isEmailLegal(para[4])) {
            return;
        }

        if(!isPasswordLegal(para[5], para[6])) {
            return;
        }

        switch (para[1].length()) {
            case 8 -> {
                users.add(new UnderGraduate(para[1], para[2], para[3], para[4], para[5]));
            }
            case 9 -> {
                users.add(new Graduate(para[1], para[2], para[3], para[4], para[5]));
            }
            case 5 -> {
                users.add(new Teacher(para[1], para[2], para[3], para[4], para[5]));
            }
        }
        System.out.println("register success");
    }







    public static void login(String[] para) {
        if(para.length != 3) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(userNow != null) {
            System.out.println(ALREADY_LOGIN);
            return;
        }


        if(!isIdLegal(para[1])) {
            return;
        }

        User user = findId(para[1]);
        if(user == null) {
            System.out.println(ID_NO_EXIST);
            return;
        }

        if(user.password.equals(para[2])) {
            userNow = user;
            Course.setAdminCoursesOfUserNow(userNow.adminCourses);
            Course.setLearnCoursesOfUserNow(userNow.learnCourses);
            if(userNow.type == USER_TYPE_TEACHER) {
                Course.setStatus(Course.STATUS_TEACHER);
                System.out.println("Hello Professor " + userNow.lastName + "~");
            }else {
                Course.setStatus(Course.STATUS_STUDENT);
                System.out.println("Hello " + userNow.firstName + "~");
            }
        }else {
            System.out.println("wrong password");
        }
    }





    public static void logout(String[] para) {
        if(para.length != 1) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(userNow == null) {
            System.out.println(NO_LOGIN);
            return;
        }

        Course.setAdminCoursesOfUserNow(null);
        Course.setLearnCoursesOfUserNow(null);
        Course.setStatus(Course.STATUS_UNLOGIN);
        Course.setCourseNow(null);

        userNow = null;
        System.out.println("Bye~");
    }






    public static void printInfo(String[] para) {
        if(userNow == null) {
            System.out.println("login first");
            return;
        }

        if(para.length > 2) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(userNow.type == USER_TYPE_TEACHER) {
            //teacher
            if(para.length == 1) {
                System.out.println(userNow);
                return;
            }

            if(!isIdLegal(para[1])) {
                return;
            }

            User userCheck = findId(para[1]);
            if(userCheck == null) {
                System.out.println(ID_NO_EXIST);
                return;
            }

            System.out.println(userCheck);

        }else {
            //student
            if(para.length > 1) {
                System.out.println(RIGHT_ERROR);
                return;
            }

            System.out.println(userNow);
        }
    }


    static public boolean isUserNowTeacher() {
        if(userNow != null && userNow.type == USER_TYPE_TEACHER) {
            return true;
        }else {
            return false;
        }
    }

    static public User findId(String id) {
        for(User user : users) {
            if(user.id.equals(id)) {
                return user;
            }
        }
        return null;
    }







    public static boolean isIdLegal(String id) {
        int year, college, classNum, number;
        boolean isLegal = false;

        switch (id.length()) {
            case 8 -> {
                if(id.matches("\\d+")) {
                    year = Integer.parseInt(id.substring(0, 2));
                    college = Integer.parseInt(id.substring(2, 4));
                    classNum = Integer.parseInt(id.substring(4, 5));
                    number = Integer.parseInt(id.substring(5, 8));
                    isLegal = year >= 17 && year <= 22 &&
                            college >= 1 && college <= 43 &&
                            classNum >= 1 && classNum <= 6 &&
                            number >= 1;
                }
            }
            case 9 -> {
                if (id.matches("[SZB]Y\\d+")) {
                    year = Integer.parseInt(id.substring(2, 4));
                    college = Integer.parseInt(id.substring(4, 6));
                    classNum = Integer.parseInt(id.substring(6, 7));
                    number = Integer.parseInt(id.substring(7, 9));
                    isLegal = year >= (id.charAt(0) == 'B' ? 17 : 19) && year <= 22 &&
                            college >= 1 && college <= 43 &&
                            classNum >= 1 && classNum <= 6 &&
                            number >= 1;
                }
            }
            case 5 -> {
                if(id.matches("\\d+")) {
                    number = Integer.parseInt(id);
                    isLegal = number >= 1;
                }
            }
        }

        if(!isLegal) {
            System.out.println(ID_ILLEGAL);
            return false;
        }

        return true;
    }

    private static boolean isIdDuplicate(String id) {
        for(User user : users) {
            if(user.id.equals(id)) {
                System.out.println("user id duplication");
                return true;
            }
        }
        return false;
    }

    private static boolean isNameLegal(String name) {
        if(name.length() <= 20 && name.matches("^[A-Z]{1}[a-z]*$")) {
            return true;
        }
        System.out.println("user name illegal");
        return false;
    }

    private static boolean isEmailLegal(String email) {
        if(email.matches("^\\w+@\\w+(\\.\\w+)+$")) {
            return true;
        }
        System.out.println("email address illegal");
        return false;
    }

    private static boolean isPasswordLegal(String password, String passwordAgain) {
        if(!password.matches("^[a-zA-Z]{1}\\w{7,15}$")) {
            System.out.println("password illegal");
            return false;
        }
        if(!password.equals(passwordAgain)) {
            System.out.println("passwords inconsistent");
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ("Name: " + firstName + " " + lastName +
                "\nID: " + id +
                "\nType: " + ((type == USER_TYPE_TEACHER) ? "Professor" : "Student") +
                "\nEmail: " + email);
    }

    public static User getUserNow() {
        if(userNow == null) {
            System.out.println(NO_LOGIN);
        }
        return userNow;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public int getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public TreeMap<String, Course> getAdminCourses() {
        return adminCourses;
    }

    public TreeMap<String, Course> getLearnCourses() {
        return learnCourses;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public TreeMap<String, VirtualMachine> getVms() {
        return vms;
    }
}
