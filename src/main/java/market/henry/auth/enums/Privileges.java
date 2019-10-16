package market.henry.auth.enums;

public enum Privileges {


    ACCESS_GRANT("Grants privilege to access endpoint", UserType.ADMIN,UserType.USER,UserType.SERVICE);




    Privileges(String description, UserType... userType) {
        this.userType = userType;
        this.description = description;
    }

    private UserType[] userType;
    private String description;
    private int operation;

    public String getDescription() {
        return description;
    }

    public UserType[] getUserTypes() {
        return userType;
    }

    public int operation() {
        return operation;
    }
}
