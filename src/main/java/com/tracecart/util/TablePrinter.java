package com.tracecart.util;

import java.util.List;
public class TablePrinter {
    public static void heading(String h){System.out.println("\n========== "+h+" ==========");}
    public static <T> void show(List<T> rows){if(rows.isEmpty())System.out.println("No records found."); else for(T row:rows)System.out.println(row);}
}
