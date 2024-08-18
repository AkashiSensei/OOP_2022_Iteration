import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Course {
    public static final String ID_NO_EXIST = "course id not exist";
    public static final String NO_SELECTED = "no course selected";
    public static final int STATUS_UNLOGIN = 0, STATUS_STUDENT = 1, STATUS_ASSISTANT = 2, STATUS_TEACHER = 4;


    private static final TreeMap<String, Course> allCourses = new TreeMap<>();
    private static Course courseNow = null;
    private static int status = STATUS_UNLOGIN;
    private static TreeMap<String, Course> adminCoursesOfUserNow = null;
    private static TreeMap<String, Course> learnCoursesOfUserNow = null;

    private final String id;
    private final String name;
    private final TreeMap<String, User> admins = new TreeMap<>();
    private final TreeMap<String, User> students = new TreeMap<>();
    private final TreeMap<String, Ware> wares = new TreeMap<>();
    private final TreeMap<String, Task> tasks = new TreeMap<>();



    public Course(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static void addCourse(String[] para) {
        if(para.length != 3) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isLoginAndTeacher()) {
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
        adminCoursesOfUserNow.put(courseAdding.id, courseAdding);
        System.out.println("add course success");
    }

    public static void removeCourse(String[] para) {
        if(para.length != 2) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isLoginAndTeacher()) {
            return;
        }

        if(!isIdLegal(para[1])) {
            return;
        }

        if(!adminCoursesOfUserNow.containsKey(para[1])) {
            System.out.println(ID_NO_EXIST);
            return;
        }



        Course courseRemoving = adminCoursesOfUserNow.get(para[1]);
        if(courseNow == courseRemoving) {
            courseNow = null;
        }

        for(User admins : courseRemoving.admins.values()) {
            //contains "coursesOfUserNow"
            admins.getAdminCourses().remove(para[1]);
        }
        allCourses.remove(para[1]);
        System.out.println("remove course success");
    }

    public static void listCourse(String[] para) {
        if(para.length != 1) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isLogin()) {
            return;
        }

        switch (status) {
            case STATUS_ASSISTANT, STATUS_TEACHER -> {
                if(adminCoursesOfUserNow.size() == 0) {
                    System.out.println("course not exist");
                }

                for(Course course : adminCoursesOfUserNow.values()) {
                    System.out.println(course);
                }
            }
            case STATUS_STUDENT -> {
                if(learnCoursesOfUserNow.size() == 0) {
                    System.out.println("course not exist");
                }

                for(Course course : learnCoursesOfUserNow.values()) {
                    System.out.println(course);
                }
            }
            // should not be used
            default -> {
                System.out.println(User.NO_LOGIN);
            }
        }


    }

    public static void selectCourse(String[] para) {
        if(para.length != 2) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isLogin()) {
            return;
        }

        if(!isIdLegal(para[1])) {
            return;
        }

        switch (status) {
            case STATUS_ASSISTANT, STATUS_TEACHER -> {
                if(!adminCoursesOfUserNow.containsKey(para[1])) {
                    System.out.println(ID_NO_EXIST);
                    return;
                }

                courseNow = adminCoursesOfUserNow.get(para[1]);
                System.out.println("select course success");
            }
            case STATUS_STUDENT -> {
                if(!learnCoursesOfUserNow.containsKey(para[1])) {
                    System.out.println(ID_NO_EXIST);
                    return;
                }

                courseNow = learnCoursesOfUserNow.get(para[1]);
                System.out.println("select course success");
            }
            // should not be used
            default -> {
                System.out.println("Select course error");
            }
        }


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
            userAdding.getAdminCourses().put(courseNow.id, courseNow);
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

        courseNow.admins.get(para[1]).getAdminCourses().remove(courseNow.id);
        courseNow.admins.remove(para[1]);
        System.out.println("remove admin success");
    }

    public static void listAdmin(String[] para) {
        if(para.length != 1) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isLoginAndSelected()) {
            return;
        }

        switch (status) {
            case STATUS_ASSISTANT, STATUS_TEACHER ->  {
                for(User admin : courseNow.admins.values()) {
                    System.out.println("[ID:" + admin.getId() + "] [Name:" + admin.getLastName() + " " + admin.getFirstName() +
                            "] [Type:" + (admin.getType() == User.USER_TYPE_TEACHER ? "Professor" : "Assistant") +
                            "] [Email:" + admin.getEmail() + "]");
                }
            }
            case STATUS_STUDENT -> {
                for(User admin : courseNow.admins.values()) {
                    System.out.println("[Name:" + admin.getLastName() + " " + admin.getFirstName() +
                            "] [Type:" + (admin.getType() == User.USER_TYPE_TEACHER ? "Professor" : "Assistant") +
                            "] [Email:" + admin.getEmail() + "]");
                }
            }
            // should not be used
            default -> {
                System.out.println("listAdmin error");
            }
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
                courseNow = null;
                status = STATUS_STUDENT;
                System.out.println("change into Student success");
            }
            case STATUS_TEACHER -> {
                System.out.println("permission denied");
            }
            case STATUS_STUDENT -> {
                if(adminCoursesOfUserNow.size() == 0) {
                    System.out.println("permission denied");
                    return;
                }
                status = STATUS_ASSISTANT;
                courseNow = null;
                System.out.println("change into Assistant success");
            }
        }
    }

    public static void addWare(String[] para) {
        System.gc();
        if(para.length != 3) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isAdminAndSelected()) {
            return;
        }

        if(!Ware.isIdLegal(para[1], courseNow.id)) {
            return;
        }

        String wareName;
        try {
            String[] pathLevel = para[2].split("\\\\");
            wareName = pathLevel[pathLevel.length - 1];
        }catch (Exception e) {
            System.out.println("unexpected error");
            return;
        }

        if(!Ware.isNameLegal(wareName)) {
            return;
        }

        File wareSrc = new File(para[2]);
        if(!wareSrc.exists()) {
            System.out.println("ware file does not exist");
            return;
        }

        if(courseNow.wares.containsKey(para[1])) {
            new File(".\\data\\" + courseNow.id + "\\" + courseNow.wares.get(para[1]).getSavePathAfterCourseId()).delete();
        }
        try {
            File wareDstFolder = new File(".\\data\\" + courseNow.id + "\\wares\\");
            if(!wareDstFolder.exists()) {
                wareDstFolder.mkdirs();
            }
            Files.copy(Path.of(para[2]), Path.of(".\\data\\" + courseNow.id + "\\wares\\" + para[1] + "_" + wareName), REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("ware file operation failed");
            return;
        } catch (Exception e) {
            System.out.println("unexpected error");
            return;
        }

        courseNow.wares.put(para[1], new Ware(para[1], wareName));
        System.out.println("add ware success");
    }

    public static void removeWare(String[] para) {
        System.gc();
        if(para.length != 2) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isAdminAndSelected()) {
            return;
        }

        /*
        if(!Ware.isIdLegal(para[1], courseNow.id)) {
            return;
        }

         */


        if(!courseNow.wares.containsKey(para[1])) {
            System.out.println(Ware.ID_NO_EXIST);
            return;
        }

        Ware removingWare = courseNow.wares.get(para[1]);
        File removingWareFile = new File(".\\data\\" + courseNow.id + "\\wares\\" + removingWare.getId() + "_" + removingWare.getName());
        if(!removingWareFile.delete()) {
            System.out.println("delete file failed");
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

        if(!isLoginAndSelected()) {
            return;
        }

        CourseSrc.list("ware", courseNow.wares);
    }

    public static void addTask(String[] para) {
        System.gc();
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

        /*


         */
        String taskName;
        try {
            String[] pathLevel = para[2].split("\\\\");
            taskName = pathLevel[pathLevel.length - 1];
        }catch (Exception e) {
            System.out.println("unexpected error");
            return;
        }

        if(!Task.isNameLegal(taskName)) {
            return;
        }

        if(!(Task.isTimeLegal(para[3]) && Task.isTimeLegal(para[4]) && Task.isStartEarly(para[3], para[4]))) {
            System.out.println(Task.TIME_ILLEGAL);
            return;
        }


        File taskSrcFile = new File(para[2]);

        if(!taskSrcFile.exists()) {
            //System.out.println(para[2]);
            System.out.println("task file not found");
            return;
        }


        if(courseNow.tasks.containsKey(para[1])) {
            new File(".\\data\\" + courseNow.id + "\\" + courseNow.tasks.get(para[1]).getSavePathAfterCourseId()).delete();
        }

        try {
            File taskDstFolder = new File(".\\data\\" + courseNow.id + "\\tasks\\" + para[1] + "\\");
            if(!taskDstFolder.exists()) {
                taskDstFolder.mkdirs();
            }
            //System.out.println(".\\data\\" + courseNow.id + "\\tasks\\" + para[1] + "\\" + taskName);
            Files.copy(Path.of(para[2]), Path.of(".\\data\\" + courseNow.id + "\\tasks\\" + para[1] + "\\" + taskName), REPLACE_EXISTING);
            //copy(para[2], ".\\data\\" + courseNow.id + "\\tasks\\" + para[1] + "\\" + taskName);
        } catch (IOException e) {
            //System.out.println(e);
            System.out.println("task file operation failed");
            return;
        } catch (Exception e) {
            System.out.println("unexpected error");
            return;
        }

        courseNow.tasks.put(para[1], new Task(para[1], para[2], para[3], para[4], courseNow.id));
        System.out.println("add task success");
    }

    public static void copy(String srcPath, String dstPath) throws Exception{
        byte buffer[] = new byte[4096];
        int readNum;
        File dstFile = new File(dstPath);
        FileInputStream inputStream = new FileInputStream(srcPath);
        FileOutputStream outputStream = new FileOutputStream(dstFile);

        readNum = inputStream.read(buffer);
        outputStream.write(buffer, 0, readNum);
        outputStream.flush();
        outputStream.close();
    }

    public static void removeTask(String[] para) {
        if(para.length != 2) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isAdminAndSelected()) {
            return;
        }

        /*
        if(!Task.isIdLegal(para[1], courseNow.id)) {
            return;
        }

         */

        if(!courseNow.tasks.containsKey(para[1])) {
            System.out.println(Task.ID_NO_EXIST);
            return;
        }

        Task removingTask = courseNow.tasks.get(para[1]);
        File removingTaskFile = new File(".\\data\\" + courseNow.id + "\\tasks\\" + removingTask.getId() + "\\" + removingTask.getName());
        if(!removingTaskFile.delete()) {
            System.out.println("delete file failed");
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

        if(!isLoginAndSelected()) {
            return;
        }


        if(courseNow.tasks.size() == 0) {
            System.out.println("total 0 task");
            return;
        }
        switch (status) {
            case STATUS_ASSISTANT, STATUS_TEACHER -> {
                for(Task task : courseNow.tasks.values()) {
                    System.out.println("[ID:" + task.getId() + "] [Name:" + task.getName() + "] [SubmissionStatus:" +
                            task.getReceiveNum() + "/" + courseNow.students.size() +
                            "] [StartTime:" + task.getStartTime() + "] [EndTime:" + task.getDeadTime() + "]");
                }
            }
            case STATUS_STUDENT -> {
                for(Task task : courseNow.tasks.values()) {
                    System.out.println("[ID:" + task.getId() + "] [Name:" + task.getName() + "] [Status:" +
                            ((task.isStudentSubmitAssignment(User.getUserNow().getId()))? "done" : "undone") +
                            "] [StartTime:" + task.getStartTime() + "] [EndTime:" + task.getDeadTime() + "]");
                }
            }
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


        User studentAdding;
        for(int i = 1; i < para.length; i++) {
            if(!User.isIdLegal(para[i])) {
                return;
            }

            studentAdding = User.findId(para[i]);
            if(studentAdding == null) {
                System.out.println(User.ID_NO_EXIST);
                return;
            }

            if(studentAdding.getType() == User.USER_TYPE_TEACHER) {
                System.out.println("I'm professor rather than student!");
                return;
            }
        }


        for(int i = 1; i < para.length; i++) {
            if(courseNow.students.containsKey(para[i])) {
                continue;
            }

            studentAdding = User.findId(para[i]);
            assert studentAdding != null;

            courseNow.students.put(studentAdding.getId(), studentAdding);
            studentAdding.getLearnCourses().put(courseNow.id, courseNow);
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
        assert User.findId(para[1]) != null;
        User.findId(para[1]).getLearnCourses().remove(courseNow.id);
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

    public static void downloadFile(String[] para) {
        if(para.length == 1) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        String savePath = null;
        String outputPath = null;
        String fileId = para[1];
        boolean isAppend = false;




        for(int cnx = 1; cnx < para.length; cnx++) {
            if(para[cnx].matches(">+")) {
                if(cnx == para.length - 1) {
                    System.out.println("please input the path to redirect the file");
                    return;
                }

                //System.out.println(savePath + para[cnx + 1]);
                if(cnx != 1 && para[cnx + 1].equals(para[1])) {
                    System.out.println("input file is output file");
                    return;
                }

                outputPath = para[cnx + 1];
                if(para[cnx].equals(">")) {
                    isAppend = false;
                }else if(para[cnx].equals(">>")) {
                    isAppend = true;
                }else {
                    System.out.println("!!!redirect judge error");
                    return;
                }
                break;
            }
        }


        FileInputStream readingStream = null;
        FileOutputStream outputStream = null;
        FileWriter writer = null;
        CourseSrc downloadingSrc = null;
        File dstFile;
        if(outputPath != null) {
            try{
                dstFile = new File(outputPath);
                if(dstFile.getParentFile() != null && !dstFile.getParentFile().exists()) {
                    dstFile.getParentFile().mkdirs();
                }
                writer = new FileWriter(dstFile, isAppend);

                if(para.length > 5 || para.length < 4) {
                    writer.write(Test.PARA_ILLEGAL + "\n");
                    return;
                }

                if(para.length == 5) {
                    savePath = para[1];
                    fileId = para[2];
                }else {
                    fileId = para[1];
                }

                if(status == STATUS_UNLOGIN) {
                    writer.write(User.NO_LOGIN + "\n");
                    return;
                }

                if(courseNow == null) {
                    writer.write(NO_SELECTED + "\n");
                    return;
                }


                if(fileId.charAt(0) == 'T') {
                    if(!courseNow.tasks.containsKey(fileId)) {
                        writer.write("file not found" + "\n");
                        return;
                    }
                    downloadingSrc = courseNow.tasks.get(fileId);
                }else if(fileId.charAt(0) == 'W') {
                    if(!courseNow.wares.containsKey(fileId)) {
                        writer.write("file not found" + "\n");
                        return;
                    }
                    downloadingSrc = courseNow.wares.get(fileId);
                }else {
                    writer.write("file not found" + "\n");
                    return;
                }
            }catch (IOException e) {
                System.out.println(e);
            }finally {
                try {
                    if(writer != null) {
                        writer.flush();
                        writer.close();
                    }
                }catch (Exception e) {
                    System.out.println("stream close error");
                }
            }
        }else {
            if(para.length != 3) {
                System.out.println(Test.PARA_ILLEGAL);
                return;
            }

            if(!isLoginAndSelected()) {
                return;
            }

            fileId = para[2];
            if(fileId.charAt(0) == 'T') {
                if(!courseNow.tasks.containsKey(fileId)) {
                    System.out.println("file not found");
                    return;
                }
                downloadingSrc = courseNow.tasks.get(fileId);
            }else if(fileId.charAt(0) == 'W') {
                if(!courseNow.wares.containsKey(fileId)) {
                    System.out.println("file not found");
                    return;
                }
                downloadingSrc = courseNow.wares.get(fileId);
            }else {
                System.out.println("file not found");
                return;
            }
            savePath = para[1];
        }


        byte[] buffer = new byte[4096];
        int readNum = 0;


        try {
            File downloadingFile = new File(".\\data\\" + courseNow.id + "\\" + downloadingSrc.getSavePathAfterCourseId());
            readingStream = new FileInputStream(downloadingFile);
            readNum = readingStream.read(buffer);

            if(savePath != null) {
                dstFile = new File(savePath);
                if(dstFile.getParentFile() != null && !dstFile.getParentFile().exists()) {
                    dstFile.getParentFile().mkdirs();
                }
                outputStream = new FileOutputStream(dstFile, false);
                outputStream.write(buffer, 0, readNum);
                outputStream.flush();
            }

            String content = new String(buffer, 0, readNum);
            if(outputPath != null) {
                dstFile = new File(outputPath);
                if(dstFile.getParentFile() != null && !dstFile.getParentFile().exists()) {
                    dstFile.getParentFile().mkdirs();
                }
                writer = new FileWriter(dstFile, isAppend);
                writer.write(content);
                writer.flush();
            }else {
                System.out.println(content.trim());
            }

        } catch (IOException e) {
            System.out.println("file operation failed");
        } finally {
            try {
                if(writer != null) {
                    writer.close();
                }
                if(outputStream != null) {
                    outputStream.close();
                }
                if(readingStream != null) {
                    readingStream.close();
                }
            }catch (Exception e) {
                System.out.println("stream close error");
                return;
            }
        }
    }

    public static void openFile(String para[]) {
        if(para.length < 2) {
            System.out.println("please input the path to open the file");
            return;
        }

        String inputPath;
        int redCnx = 0;

        if((redCnx = findRedirectInCnx(para)) == -1) {
            return;
        }

        inputPath = para[redCnx + 1];

        switch (redCnx) {
            case 0 -> {
                if(para.length != 2) {
                    System.out.println(Test.PARA_ILLEGAL);
                    return;
                }
                inputPath = para[1];
            }
            case 1 -> {
                if ((para.length != 3)) {
                    System.out.println(Test.PARA_ILLEGAL);
                    return;
                }
            }
            case 2 -> {
                if(para.length != 4) {
                    System.out.println(Test.PARA_ILLEGAL);
                    return;
                }
                inputPath = para[1];
            }
            default -> {
                System.out.println(Test.PARA_ILLEGAL);
                return;
            }
        }

        byte[] buffer = new byte[4096];
        String content;
        int readNum;
        FileInputStream inputStream = null;
        try {
            File inputFile = new File(inputPath);

            inputStream = new FileInputStream(inputFile);
            readNum = inputStream.read(buffer);
            content = new String(buffer, 0, readNum);
            System.out.println(content.trim());
        }catch (IOException e) {
            //System.out.println(e);
            System.out.println("file open failed");
        }finally {
            try {
                if(inputStream != null) {
                    inputStream.close();
                }
            }catch (Exception e) {
                System.out.println("!!! openFile stream close failed");
            }
        }
    }

    public static void submitTaskOrAnswer(String[] para) {
        if(para.length < 3) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        String inputPath;
        int redCnx;

        if((redCnx = findRedirectInCnx(para)) == -1) {
            return;
        }
        String taskId;

        switch (redCnx) {
            case 0 -> {
                if(para.length != 3) {
                    System.out.println(Test.PARA_ILLEGAL);
                    return;
                }
                taskId = para[2];
                inputPath = para[1];
            }
            case 2 -> {
                if ((para.length != 4)) {
                    System.out.println(Test.PARA_ILLEGAL);
                    return;
                }
                taskId = para[1];
                inputPath = para[3];
            }
            case 3 -> {
                if(para.length != 5) {
                    System.out.println(Test.PARA_ILLEGAL);
                    return;
                }
                taskId = para[2];
                inputPath = para[1];
            }
            default -> {
                System.out.println(Test.PARA_ILLEGAL);
                return;
            }
        }

        if(!isLogin()) {
            return;
        }


        boolean isSubmitTask = para[0].equals("submitTask");
        if(isSubmitTask) {
            if(status != STATUS_STUDENT) {
                System.out.println("operation not allowed");
                return;
            }
        }else {
            if(status == STATUS_STUDENT) {
                System.out.println("permission denied");
                return;
            }
        }


        if(!isSelect()) {
            return;
        }


        if(!courseNow.tasks.containsKey(taskId)) {
            System.out.println(Task.ID_NO_EXIST);
            return;
        }

        if(isSubmitTask) {
            courseNow.tasks.get(taskId).saveAssignment(inputPath, User.getUserNow());
        }else {
            courseNow.tasks.get(taskId).saveAnswer(inputPath);
        }

    }

    public static void queryScore(String[] para) {
        if(para.length > 3) {
            System.out.println(Test.PARA_ILLEGAL);
            return;
        }

        if(!isLoginAndSelected()) {
            return;
        }

        ArrayList<String> outputList = new ArrayList<>();
        switch (status) {
            case STATUS_ASSISTANT, STATUS_TEACHER -> {
                if(para.length == 1) {
                    Task.addOutputAllTasksScoreForAllStu(outputList, courseNow.tasks);
                }else if(para[1].matches("^T\\d{6}$")) {
                    if(!courseNow.tasks.containsKey(para[1])) {
                        System.out.println("task not found");
                        return;
                    }

                    if(para.length == 2) {
                        courseNow.tasks.get(para[1]).addOutputAllStuScore(outputList);
                    }else {
                        if(User.findId(para[2]) == null) {
                            System.out.println("student not found");
                            return;
                        }

                        courseNow.tasks.get(para[1]).addOutputSingleScore(outputList, para[2]);
                    }
                }else {
                    if(para.length == 3) {
                        System.out.println("task not found");
                        return;
                    }

                    if(User.findId(para[1]) == null) {
                        System.out.println("student not found");
                        return;
                    }

                    Task.addOutputAllTasksScoreForSingle(outputList, courseNow.tasks, para[1]);
                }
            }
            case STATUS_STUDENT -> {
                if(para.length == 1) {
                    Task.addOutputAllTasksScoreForSingle(outputList, courseNow.tasks, User.getUserNow().getId());
                }else if(para.length == 2) {
                    if(!courseNow.tasks.containsKey(para[1])) {
                        System.out.println("task not found");
                        return;
                    }
                    courseNow.tasks.get(para[1]).addOutputSingleScore(outputList, User.getUserNow().getId());
                }else {
                    System.out.println("permission denied");
                    return;
                }
            }
        }

        Task.showOutputList(outputList);
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



    private static boolean isLogin() {
        if(status == STATUS_UNLOGIN) {
            System.out.println(User.NO_LOGIN);
            return false;
        }
        return true;
    }

    private static boolean isSelect() {
        if(courseNow == null) {
            System.out.println(NO_SELECTED);
            return false;
        }
        return true;
    }
    private static boolean isLoginAndTeacher() {
        if(!isLogin()) {
            return false;
        }

        if(status != STATUS_TEACHER) {
            System.out.println(User.RIGHT_ERROR);
            return false;
        }

        return true;
    }

    private static boolean isTeacherAndSelected() {
        if(!isLoginAndTeacher()) {
            return false;
        }

        return isSelect();
    }

    private static boolean isLoginAndAdmin() {
        if(!isLogin()) {
            return false;
        }

        if(status == STATUS_STUDENT) {
            System.out.println(User.RIGHT_ERROR);
            return false;
        }

        return true;
    }

    private static boolean isLoginAndSelected() {
        if(!isLogin()) {
            return false;
        }

        return isSelect();
    }

    private static boolean isAdminAndSelected() {
        if(!isLoginAndAdmin()) {
            return false;
        }

        return isSelect();
    }


    private static int findRedirectInCnx(String[] para) {
        assert para.length > 1;
        int cnx;
        for(cnx = 1; cnx < para.length; cnx++) {
            if(para[cnx].equals("<")) {
                if(cnx == para.length - 1) {
                    System.out.println("please input the path to redirect the file");
                    return -1;
                }
                return cnx;
            }
        }
        return 0;
    }

    private static boolean isNameLegal(String name) {
        return name.matches("^\\w{6,16}$");
    }

    public static void setStatus(int status) {
        Course.status = status;
    }

    public static void setAdminCoursesOfUserNow(TreeMap<String, Course> adminCoursesOfUserNow) {
        Course.adminCoursesOfUserNow = adminCoursesOfUserNow;
    }

    public static void setLearnCoursesOfUserNow(TreeMap<String, Course> learnCoursesOfUserNow) {
        Course.learnCoursesOfUserNow = learnCoursesOfUserNow;
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

    public TreeMap<String, User> getStudents() {
        return students;
    }
}