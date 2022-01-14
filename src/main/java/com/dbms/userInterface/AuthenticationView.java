package com.dbms.userInterface;

public class AuthenticationView extends UserInterfaceHelper {

    public void showEnterUserName(){
        System.out.println("Enter your email:");
    }

    public void showEnterPassword(){
        System.out.println("Enter your password:");
    }


    public void answerSecurityQuestion(String question){
        System.out.println("Please answer the sec question:");
        System.out.println(question);
    }

    public void checkCredentials(){
        System.out.println("Please check your credentials!");
        System.out.println("0. Try again");
        System.out.println("1. Go to previous menu");
    }
}
