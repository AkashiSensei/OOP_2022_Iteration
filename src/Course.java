import java.util.TreeMap;

public class Course {
    public static final String ID_NO_EXIST = "course id not exist";
    public static final String NO_SELECTED = "no course selected";
    public static final int STATUS_UNLOGIN = 0, STATUS_STUDENT = 1, STATUS_ASSISTANT = 2, STATUS_TEACHER = 4;


    private static TreeMap<String, Course> allCourses = new TreeMap<>();
    //private static ArrayList<Course> courses = new ArrayList<>();
    private static Course courseNow = null;
    private static int status = STATUS_UNLOGIN;
    private static TreeMap<String, Course> coursesOfUserNow = null;

    private String id;
    private String name;
    private TreeMap<String, User> admins = new TreeMap<>();
    private TreeMap<String, User> students = new TreeMap<>();
    private TreeMap<String, Ware> wares = new TreeMap<>();
    private TreeMap<String, Task> tasks = new TreeMap<>();



    public Course(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static void addCourse(String[] para) {
        if(para.length != 3) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isTeacher()) {
            return;
        }

        if(!isIdLegal(para[1])) {
            return;
        }

        if(allCourses.containsKey(para[1])) {
            System.out.println("course id duplication");
            return;
        }

        if(!isNameLegal(para[2])) {
            System.out.println("course name illegal");
            return;
        }

        Course courseAdding = new Course(para[1], para[2]);
        courseAdding.admins.put(User.getUserNow().getId(), User.getUserNow());
        allCourses.put(courseAdding.id, courseAdding);
        coursesOfUserNow.put(courseAdding.id, courseAdding);
        System.out.println("add course success");
    }

    public static void removeCourse(String[] para) {
        if(para.length != 2) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isTeacher()) {
            return;
        }

        if(!isIdLegal(para[1])) {
            return;
        }

        if(!coursesOfUserNow.containsKey(para[1])) {
            System.out.println(ID_NO_EXIST);
            return;
        }



        Course courseRemoving = coursesOfUserNow.get(para[1]);
        if(courseNow == courseRemoving) {
            courseNow = null;
        }

        for(User admins : courseRemoving.admins.values()) {
            //contains "coursesOfUserNow"
            admins.getCourses().remove(para[1]);
        }
        allCourses.remove(para[1]);
        System.out.println("remove course success");
    }

    public static void listCourse(String[] para) {
        if(para.length != 1) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isTeacher()) {
            return;
        }

        if(coursesOfUserNow.size() == 0) {
            System.out.println("course not exist");
        }

        for(Course course : coursesOfUserNow.values()) {
            System.out.println(course);
        }
    }

    public static void selectCourse(String[] para) {
        if(para.length != 2) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isAdmin()) {
            return;
        }

        if(!isIdLegal(para[1])) {
            return;
        }

        if(!coursesOfUserNow.containsKey(para[1])) {
            System.out.println(ID_NO_EXIST);
            return;
        }

        courseNow = coursesOfUserNow.get(para[1]);
        System.out.println("select course success");
    }

    public static void addAdmin(String[] para) {
        if(para.length == 1) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isTeacherAndSelected()) {
            return;
        }

        for(int i = 1; i < para.length; i++) {
            if(!User.isIdLegal(para[i])) {
                return;
            }

            if(User.findId(para[i]) == null) {
                System.out.println(User.ID_NO_EXIST);
                return;
            }
        }

        User userAdding;
        for(int i = 1; i < para.length; i++) {
            if(courseNow.admins.containsKey(para[i])) {
                continue;
            }
            userAdding = User.findId(para[i]);
            assert userAdding != null;

            courseNow.admins.put(userAdding.getId(), userAdding);
            userAdding.getCourses().put(courseNow.id, courseNow);
        }
        System.out.println("add admin success");
    }

    public static void removeAdmin(String[] para) {
        if(para.length != 2) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isTeacherAndSelected()) {
            return;
        }

        if(!User.isIdLegal(para[1])) {
            return;
        }

        if(!courseNow.admins.containsKey(para[1])) {
            System.out.println(User.ID_NO_EXIST);
            return;
        }

        courseNow.admins.get(para[1]).getCourses().remove(courseNow.id);
        courseNow.admins.remove(para[1]);
        System.out.println("remove admin success");
    }

    public static void listAdmin(String[] para) {
        if(para.length != 1) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isAdminAndSelected()) {
            return;
        }

        for(User admin : courseNow.admins.values()) {
            System.out.println("[ID:" + admin.getId() + "] [Name:" + admin.getLastName() + " " + admin.getFirstName() +
                    "] [Type:" + (admin.getType() == User.USER_TYPE_TEACHER ? "Professor" : "Assistant") +
                    "] [Email:" + admin.getEmail() + "]");
        }
    }

    public static void changeRole(String[] para) {
        if(para.length != 1) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        switch (status) {
            case STATUS_UNLOGIN -> {
                System.out.println(User.NO_LOGIN);
            }
            case STATUS_ASSISTANT -> {
                status = STATUS_STUDENT;
                System.out.println("change into Student success");
            }
            case STATUS_TEACHER -> {
                System.out.println("permission denied");
            }
            case STATUS_STUDENT -> {
                if(coursesOfUserNow.size() == 0) {
                    System.out.println("permission denied");
                    return;
                }
                status = STATUS_ASSISTANT;
                System.out.println("change into Assistant success");
            }
        }
    }

    public static void addWare(String[] para) {
        if(para.length != 3) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isTeacherAndSelected()) {
            return;
        }

        if(!Ware.isIdLegal(para[1], courseNow.id)) {
            return;
        }

        if(courseNow.wares.containsKey(para[1])) {
            System.out.println(Ware.ID_DUPLICATE);
            return;
        }

        if(!Ware.isNameLegal(para[2])) {
            return;
        }

        courseNow.wares.put(para[1], new Ware(para[1], para[2]));
        System.out.println("add ware success");
    }

    public static void removeWare(String[] para) {
        if(para.length != 2) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isTeacherAndSelected()) {
            return;
        }

        if(!Ware.isIdLegal(para[1], courseNow.id)) {
            return;
        }

        if(!courseNow.wares.containsKey(para[1])) {
            System.out.println(Ware.ID_NO_EXIST);
            return;
        }

        courseNow.wares.remove(para[1]);
        System.out.println("remove ware success");
    }

    public static void listWare(String[] para) {
        if(para.length != 1) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isAdminAndSelected()) {
            return;
        }

        for(Ware ware : courseNow.wares.values()) {
            System.out.println(ware);
        }
    }

    public static void addTask(String[] para) {
        if(para.length != 5) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isAdminAndSelected()) {
            return;
        }

        if(!Task.isIdLegal(para[1], courseNow.id)) {
            return;
        }

        if(courseNow.tasks.containsKey(para[1])) {
            System.out.println(Task.ID_DUPLICATE);
            return;
        }

        if(!Task.isNameLegal(para[2])) {
            return;
        }

        if(!(Task.isTimeLegal(para[3]) && Task.isTimeLegal(para[4]) && Task.isStartEarly(para[3], para[4]))) {
            System.out.println(Task.TIME_ILLEGAL);
            return;
        }

        courseNow.tasks.put(para[1], new Task(para[1], para[2], para[3], para[4]));
        System.out.println("add task success");
    }

    public static void removeTask(String[] para) {
        if(para.length != 2) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isAdminAndSelected()) {
            return;
        }

        if(!Task.isIdLegal(para[1], courseNow.id)) {
            return;
        }

        if(!courseNow.tasks.containsKey(para[1])) {
            System.out.println(Task.ID_NO_EXIST);
            return;
        }

        courseNow.tasks.remove(para[1]);
        System.out.println("remove task success");
    }

    public static void listTask(String[] para) {
        if(para.length != 1) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isAdminAndSelected()) {
            return;
        }

        for(Task task : courseNow.tasks.values()) {
            System.out.println(task);
        }
    }

    public static void addStudent(String[] para) {
        if(para.length == 1) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isAdminAndSelected()) {
            return;
        }


        User userAdding;
        for(int i = 1; i < para.length; i++) {
            if(!User.isIdLegal(para[i])) {
                return;
            }

            userAdding = User.findId(para[i]);
            if(userAdding == null) {
                System.out.println(User.ID_NO_EXIST);
                return;
            }

            if(userAdding.getType() == User.USER_TYPE_TEACHER) {
                System.out.println("I'm professor rather than student!");
                return;
            }
        }


        for(int i = 1; i < para.length; i++) {
            if(courseNow.students.containsKey(para[i])) {
                continue;
            }

            userAdding = User.findId(para[i]);
            assert userAdding != null;

            courseNow.students.put(userAdding.getId(), userAdding);
        }
        System.out.println("add student success");
    }

    public static void removeStudent(String[] para) {
        if(para.length != 2) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isAdminAndSelected()) {
            return;
        }

        if(!User.isIdLegal(para[1])) {
            return;
        }

        if(!courseNow.students.containsKey(para[1])) {
            System.out.println(User.ID_NO_EXIST);
            return;
        }

        courseNow.students.remove(para[1]);
        System.out.println("remove student success");
    }

    public static void listStudent(String[] para) {
        if(para.length != 1) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isAdminAndSelected()) {
            return;
        }

        for(User student : courseNow.students.values()) {
            System.out.println("[ID:" + student.getId() + "] [Name:" + student.getLastName() + " " + student.getFirstName() +
                    "] [Email:" + student.getEmail() + "]");
        }
    }













    private static boolean isIdLegal(String id) {
        int year, num;
        boolean isLegal;
        if(!id.matches("^C\\d{4}$")) {
            System.out.println("course id illegal");
            return false;
        }

        year = Integer.parseInt(id.substring(1, 3));
        num = Integer.parseInt(id.substring(3, 5));

        isLegal = year >= 17 && year <= 22 &&
                num >= 1 && num <= 99;

        if (isLegal) {
            return true;
        }else {
            System.out.println("course id illegal");
            return false;
        }
    }


    private static boolean isTeacher() {
        if(status == STATUS_UNLOGIN) {
            System.out.println(User.NO_LOGIN);
            return false;
        }

        if(status != STATUS_TEACHER) {
            System.out.println(User.RIGHT_ERROR);
            return false;
        }

        return true;
    }

    private static boolean isTeacherAndSelected() {
        if(!isTeacher()) {
            return false;
        }

        if(courseNow == null) {
            System.out.println(NO_SELECTED);
            return false;
        }

        return true;
    }

    private static boolean isAdmin() {
        if(status == STATUS_UNLOGIN) {
            System.out.println(User.NO_LOGIN);
            return false;
        }

        if(status == STATUS_STUDENT) {
            System.out.println(User.RIGHT_ERROR);
            return false;
        }

        return true;
    }

    private static boolean isAdminAndSelected() {
        if(!isAdmin()) {
            return false;
        }

        if(courseNow == null) {
            System.out.println(NO_SELECTED);
            return false;
        }

        return true;
    }

    private static boolean isNameLegal(String name) {
        return name.matches("^\\w{6,16}$");
    }

    public static void setStatus(int status) {
        Course.status = status;
    }

    public static void setCoursesOfUserNow(TreeMap<String, Course> coursesOfUserNow) {
        Course.coursesOfUserNow = coursesOfUserNow;
    }

    public static void setCourseNow(Course courseNow) {
        Course.courseNow = courseNow;
    }

    @Override
    public String toString() {
        int teacher = 0, assistant = 0;
        for(User user : this.admins.values()) {
            if(user.getType() == User.USER_TYPE_TEACHER) {
                teacher++;
            }else {
                assistant++;
            }
        }
        return ("[ID:" + this.id + "] [Name:" + this.name + "] [TeacherNum:" + teacher + "] [AssistantNum:" +
                assistant + "] [StudentNum:" + this.students.size() + "]");
    }
}