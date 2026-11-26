package com.tradeshield.app;

import com.tradeshield.db.Schema;
import com.tradeshield.ui.MainMenu;
public class Main {
    public static void main(String[] args){
        try {Schema.create();new MainMenu().start();}
        catch(Exception e){System.out.println("Could not start TradeShield. Check your .env settings, MySQL server, and database schema.");}
    }
}
