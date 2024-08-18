public class Teacher extends User{
    public Teacher(String id, String firstName, String lastName, String email, String password) {
        super(USER_TYPE_TEACHER, id, firstName, lastName, email, password);
    }
}
