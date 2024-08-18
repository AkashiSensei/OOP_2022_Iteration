public class Graduate extends User{
    public Graduate(String id, String firstName, String lastName, String email, String password) {
        super(USER_TYPE_GRADUATE, id, firstName, lastName, email, password);
    }
}
