import com.sun.source.tree.Tree;

import java.util.TreeMap;

public abstract class CourseSrc {
    public static final String OVERRIDE_ERROR = "ERROR : this method should be override";
    private String id;
    private String name;



    public CourseSrc(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static <T extends CourseSrc> void list(String type, TreeMap<String, T> courseSrcs) {
        if(courseSrcs.size() == 0) {
            System.out.println("total 0 " + type);
            return;
        }
        for(CourseSrc courseSrc : courseSrcs.values()) {
            System.out.println(courseSrc);
        }
    }


    abstract public String getSavePathAfterCourseId();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
