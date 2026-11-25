package com.tradeshield.util;

import java.util.Scanner;
public class Input {
    private final Scanner scanner = new Scanner(System.in);
    public String text(String prompt){System.out.print(prompt);return scanner.nextLine().trim();}
    public int number(String prompt){while(true){try{return Integer.parseInt(text(prompt));}catch(NumberFormatException e){System.out.println("Enter a valid whole number.");}}}
    public double decimal(String prompt){while(true){try{return Double.parseDouble(text(prompt));}catch(NumberFormatException e){System.out.println("Enter a valid price.");}}}
}
