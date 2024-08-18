public class Ware extends CourseSrc{
    public static final String ID_ILLEGAL = "ware id illegal";
    public static final String NAME_ILLEGAL = "ware name illegal";
    public static final String ID_DUPLICATE = "ware id duplication";
    public static final String ID_NO_EXIST = "ware id not exist";


    public Ware(String id, String name) {
        super(id, name);
    }

    public static boolean isIdLegal(String id, String courseId) {
        if(!id.matches("^W\\d{6}$")) {
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

    @Override
    public String toString() {
        return ("[ID:" + this.getId() + "] [Name:" + this.getName() + "]");
    }
}
