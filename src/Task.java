import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task extends CourseSrc{
    public static final String ID_ILLEGAL = "task id illegal";
    public static final String NAME_ILLEGAL = "task name illegal";
    public static final String ID_DUPLICATE = "task id duplication";
    public static final String ID_NO_EXIST = "task id not exist";
    public static final String TIME_ILLEGAL = "task time illegal";

    private String startTime;
    private String deadTime;
    private int receiveNum;


    public Task(String id, String name, String startTime, String deadTime) {
        super(id, name);
        this.startTime = startTime;
        this.deadTime = deadTime;
        receiveNum = 0;
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
    public String toString() {
        return ("[ID:" + this.getId() + "] [Name:" + this.getName() + "] [ReceiveNum:" + this.receiveNum +
                 "] [StartTime:" + this.startTime + "] [EndTime:" + this.deadTime + "]");
    }
}
