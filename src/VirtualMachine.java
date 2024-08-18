import java.io.Serializable;
import java.util.ArrayList;

abstract public class VirtualMachine implements Serializable {
    private int numInCourse;
    private final ArrayList<String> commands = new ArrayList<>();


    public int getNumInCourse() {
        return numInCourse;
    }
    public void setNumInCourse(int numInCourse) {
        this.numInCourse = numInCourse;
    }

    public ArrayList<String> getCommands() {
        return commands;
    }

    public void clearCommands() {
        this.commands.clear();
    }

    abstract public String getSysType();
}
