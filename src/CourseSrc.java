public abstract class CourseSrc {
    public static final String OVERRIDE_ERROR = "ERROR : this method should be override";
    private String id;
    private String name;



    public CourseSrc(String id, String name) {
        this.id = id;
        this.name = name;
    }


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
