package tools;

import model.User;

public class CurrentLoginUser {
    private static User currentUser;

    public static void setCurrentUser(User currentUser) {
        CurrentLoginUser.currentUser = currentUser;
    }

    public static String getUsername() {
        return currentUser.getUsername();
    }

    public static String getPassword() {
        return currentUser.getPassword();
    }

    public static String getRole() {
        return currentUser.getRole();
    }

    public static int getId() {
        return currentUser.getId();
    }

}
