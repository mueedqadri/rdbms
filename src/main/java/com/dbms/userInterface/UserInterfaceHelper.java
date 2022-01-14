package com.dbms.userInterface;

import java.util.Scanner;

public abstract class UserInterfaceHelper {
    private Scanner sc = new Scanner(System.in);
    public String getStringInputNoValidation() {
        while (!sc.hasNext()) {
            fieldCannotBeEmpty();
            sc.next();
        }
        return sc.next();
    }

    public String getLine() {
        while (!sc.hasNextLine()) {
            fieldCannotBeEmpty();
            sc.next();
        }
        return sc.nextLine();
    }
    public String getValidEmailInput() {
        while (!sc.hasNext("([A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6})")) {
            showInvalidEmail();
            sc.next();
        }
        return sc.next();
    }
    public void fieldCannotBeEmpty() {
        System.out.println("Field cannot be empty");
    }
    public void showInvalidEmail() {
        System.out.println("Please enter a valid email");
    }

    public int getIntegerInput() {
        while (!sc.hasNextInt() ) {
            showInvalidInputNumber();
            sc.next();
        }
        return sc.nextInt();
    }

    public void showInvalidInputNumber() {
        System.out.println("Invalid input");
    }
}
