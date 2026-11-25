package com.tradeshield.util;

import com.tradeshield.model.Invoice;
public class InvoicePrinter {
    public static void print(Invoice invoice){TablePrinter.heading("TRADESHIELD BILL");System.out.println(invoice);System.out.println("Thank you for shopping!");}
}
