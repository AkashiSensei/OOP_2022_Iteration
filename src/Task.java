import com.sun.source.tree.Tree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Task extends CourseSrc{
    public static final String ID_ILLEGAL = "task id illegal";
    public static final String NAME_ILLEGAL = "task name illegal";
    public static final String ID_DUPLICATE = "task id duplication";
    public static final String ID_NO_EXIST = "task not found";
    public static final String TIME_ILLEGAL = "task time illegal";

    private final String startTime;
    private final String deadTime;
    private final String ofCourseId;
    private int receiveNum;

    public class Assignment {
        User student;
        double maxScore;

        public Assignment(User student, double maxScore) {
            this.student = student;
            this.maxScore = maxScore;
        }

        private void judge() {
            assert hasAnswer = true;
            BufferedReader paperBuf = null;
            BufferedReader answerBuf = null;
            String answerLine;
            String paperLine;
            double questionCnt = 0;
            double correctCnt = 0;
            double score;

            try{
                paperBuf = new BufferedReader(new FileReader(Task.this.getTaskFolderPath() + this.student.getId() + ".task"));
                answerBuf = new BufferedReader(new FileReader(Task.this.getAnswerFilePath()));

                while((answerLine = answerBuf.readLine()) != null) {
                    paperLine = paperBuf.readLine();
                    questionCnt += 1;

                    if(answerLine.equalsIgnoreCase(paperLine)) {
                        correctCnt += 1;
                    }
                }
            }catch (Exception e) {
                System.out.println("!!! judge assignment error");
            }finally {
                try {
                    if(paperBuf != null) {
                        paperBuf.close();
                    }
                    if(answerBuf != null) {

                    }
                }catch (Exception e) {
                    System.out.println("!!! judge assignment files close error");
                    return;
                }
            }

            score = 100 * correctCnt / questionCnt;
            if(score > maxScore) {
                maxScore = score;
            }
        }

        @Override
        public String toString() {
            return ("[ID:" + student.getId() + "] [Name:" + student.getLastName() + " " + student.getFirstName() +
                    "] [Task_ID:" + Task.this.getId() + "] [Score:" + ((this.maxScore < 0) ?"None" : String.format("%.1f", this.maxScore)) + "]");
        }
    }
    private final TreeMap<String, Assignment> assignments = new TreeMap<>();
    boolean hasAnswer;

    public Task(String id, String name, String startTime, String deadTime, String ofCourseId) {
        super(id, name);
        this.startTime = startTime;
        this.deadTime = deadTime;
        this.ofCourseId = ofCourseId;
        receiveNum = 0;
    }




    public boolean isStudentSubmitAssignment(String studentId) {
        return this.assignments.containsKey(studentId);
    }

    public void saveAssignment(String filePath, User student) {
        String studentId = student.getId();
        File srcFile = new File(filePath);
        if(!srcFile.exists()) {
            System.out.println("file operation failed");
            return;
        }

        File dstFile = new File(getTaskFolderPath() + studentId + ".task");
        boolean isAlreadyHave = this.assignments.containsKey(studentId);
        if(isAlreadyHave) {
            System.gc();
            System.out.println("task already exists, do you want to overwrite it? (y/n)");
            Scanner scanner = Test.getScanner();
            char input = scanner.nextLine().charAt(0);
            if(input != 'Y' && input != 'y') {
                System.out.println("submit canceled");
                return;
            }
        }


        try{

            if(dstFile.getParentFile() != null && !dstFile.getParentFile().exists()) {
                dstFile.getParentFile().mkdirs();
            }
            Files.copy(Path.of(filePath), dstFile.toPath(), REPLACE_EXISTING);
        }catch (Exception e) {
            System.out.println(e);
            System.out.println("file operation failed");
            return;
        }

        if(!isAlreadyHave) {
            this.assignments.put(studentId, new Assignment(student, -1));
            receiveNum++;
        }

        Assignment assignmentNow = this.assignments.get(studentId);
        if(this.hasAnswer) {
            assignmentNow.judge();
        }


        System.out.println("submit success");
        if(assignmentNow.maxScore < 0) {
            System.out.println("your score is: None");
        }else {
            System.out.println("your score is: " + String.format("%.1f", assignmentNow.maxScore));
        }
    }

    public void saveAnswer(String filePath) {
        try {
            File answerFile = new File(getAnswerFilePath());
            if(answerFile.getParentFile() != null && !answerFile.getParentFile().exists()) {
                answerFile.getParentFile().mkdirs();
            }
            Files.copy(Path.of(filePath), Path.of(getAnswerFilePath()), REPLACE_EXISTING);
        }catch (Exception e) {
            System.out.println(e);
            System.out.println("file operation failed");
            return;
        }
        System.out.println("add answer success");
        hasAnswer = true;
    }

    public static void addOutputAllTasksScoreForAllStu(ArrayList<String> outputList, TreeMap<String, Task> tasks) {
        for(Task task : tasks.values()) {
            task.addOutputAllStuScore(outputList);
        }
    }

    public static void addOutputAllTasksScoreForSingle(ArrayList<String> outputList, TreeMap<String, Task> tasks, String studentID) {
        for(Task task : tasks.values()) {
            task.addOutputSingleScore(outputList, studentID);
        }
    }

    public void addOutputAllStuScore(ArrayList<String> outputList) {
        ArrayList<Assignment> tempList = new ArrayList<>();

        for(Assignment assignment : this.assignments.values()) {
            if(assignment.maxScore < 0 && this.hasAnswer) {
                assignment.judge();
            }

            tempList.add(assignment);
        }

        Collections.sort(tempList, new Comparator<Assignment>() {
            @Override
            public int compare(Assignment o1, Assignment o2) {
                if(o1.maxScore < o2.maxScore) {
                    return 1;
                }else if(o1.maxScore > o2.maxScore) {
                    return -1;
                }else {
                    return o1.student.getId().compareTo(o2.student.getId());
                }
            }
        });

        for(Assignment assignment : tempList) {
            outputList.add(assignment.toString());
        }
    }

    public void addOutputSingleScore(ArrayList<String> outputList, String studentId) {
        if(!this.assignments.containsKey(studentId)) {
            return;
        }

        Assignment assignment = this.assignments.get(studentId);
        this.addOutputScore(outputList, assignment);
    }

    public void addOutputScore(ArrayList<String> outputList, Assignment assignment) {
        if(assignment.maxScore < 0 && this.hasAnswer) {
            assignment.judge();
        }

        outputList.add(assignment.toString());
    }

    public static void showOutputList(ArrayList<String> outputList) {
        int resultCnt = outputList.size();
        if(resultCnt < 2) {
            System.out.println("total " + resultCnt + " result");
        }else {
            System.out.println("total " + resultCnt + " results");
        }

        int resultCnx = 1;
        for(String outputLine : outputList) {
            System.out.println("[" + resultCnx + "] " + outputLine);
            resultCnx++;
        }
    }

    public static boolean isIdLegal(String id, String courseId) {
        if(!id.matches("^T\\d{6}$")) {
            System.out.println(ID_ILLEGAL);
            return false;
        }

        if(Integer.parseInt(id.substring(5, 7)) >= 1 && id.substring(1, 5).equals(courseId.substring(1, 5))) {
            return true;
        }else {
            System.out.println(ID_ILLEGAL);
            return false;
        }
    }

    public static boolean isNameLegal(String name) {
        if(name.matches("^\\w+\\.[a-zA-Z0-9]+$")) {
            return true;
        }
        System.out.println(NAME_ILLEGAL);
        return false;
    }

    public static boolean isTimeLegal(String dateAndTime) {
        if(!dateAndTime.matches("^\\d{4}-\\d{2}-\\d{2}-\\d{2}:\\d{2}:\\d{2}$")) {
            return false;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.setLenient(false);
            Date date = format.parse(dateAndTime.substring(0, 10));
        }catch (Exception e) {
            return false;
        }

        int year = Integer.parseInt(dateAndTime.substring(0, 4));
        int HH = Integer.parseInt(dateAndTime.substring(11, 13));
        int mm = Integer.parseInt(dateAndTime.substring(14, 16));
        int ss = Integer.parseInt(dateAndTime.substring(17, 19));
        //System.out.println(year + HH + mm + ss);

        return year >= 1900 && year <= 9999 && HH <= 23 && mm <= 59 && ss <= 59;
    }

    public static boolean isStartEarly(String timeFirst, String timeDead) {
        return timeFirst.compareTo(timeDead) < 0;
    }


    @Override
    public String getSavePathAfterCourseId() {
        return ("tasks\\" + this.getId() + "\\" + this.getName());
    }

    public String getTaskFolderPath() {
        return (".\\data\\" + this.ofCourseId + "\\tasks\\" + this.getId() + "\\");
    }

    public String getAnswerFilePath() {
        return (".\\data\\" + this.ofCourseId + "\\answers\\" + this.getId() + ".ans");
    }

    @Override
    //should not be used
    public String toString() {
        return ("[ID:" + this.getId() + "] [Name:" + this.getName() + "] [ReceiveNum:" + this.receiveNum +
                 "] [StartTime:" + this.startTime + "] [EndTime:" + this.deadTime + "]");
    }

    public String getStartTime() {
        return startTime;
    }

    public String getDeadTime() {
        return deadTime;
    }

    public int getReceiveNum() {
        return receiveNum;
    }
}
