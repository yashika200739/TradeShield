package com.tracecart.ui;

import com.tracecart.model.User;import com.tracecart.service.AuthService;import com.tracecart.util.Input;import com.tracecart.util.TablePrinter;
public class MainMenu {
    private final Input in=new Input();private final AuthService auth=new AuthService();
    public void start(){while(true){try{
        TablePrinter.heading("WELCOME TO TRADESHIELD");System.out.println("1. Register customer\n2. Login\n0. Exit");
        switch(in.number("Choose: ")){
            case 1 -> {auth.register(in.text("Name: "),in.text("Email: "),in.text("Password: "));System.out.println("Registered!");}
            case 2 -> {User u=auth.login(in.text("Email: "),in.text("Password: "));
                if(u==null)System.out.println("Incorrect credentials.");
                else if(u.getRole().equals("ADMIN"))new AdminMenu(in).start();
                else new CustomerMenu(in,u).start();}
            case 0 -> {return;}
            default -> System.out.println("Invalid option.");
        }
    }catch(Exception e){System.out.println("Error: "+e.getMessage());}}}
}
