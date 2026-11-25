package com.tracecart.app;

import com.tracecart.db.Schema;import com.tracecart.ui.MainMenu;
public class Main {
    public static void main(String[] args){
        try {Schema.create();new MainMenu().start();}
        catch(Exception e){System.out.println("Could not start TradeShield: "+e.getMessage());e.printStackTrace();}
    }
}
