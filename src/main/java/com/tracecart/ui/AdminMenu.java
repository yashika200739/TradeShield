package com.tracecart.ui;

import com.tracecart.service.*;import com.tracecart.util.*;
public class AdminMenu {
    private final Input in;private final ProductService products=new ProductService();private final InventoryService stock=new InventoryService();
    private final RecallService recalls=new RecallService();private final ReturnService claims=new ReturnService();
    private final BatchTraceService batchTrace=new BatchTraceService();
    public AdminMenu(Input in){this.in=in;}
    public void start(){while(true){try{
        TablePrinter.heading("ADMIN MENU");
        System.out.println("1. Products\n2. Add product\n3. View batches\n4. Add batch\n5. Restock batch\n6. Recall defective batch\n7. Affected customers/orders\n8. List recalls\n9. Review return/replacement\n0. Logout");
        switch(in.number("Choose: ")){
            case 1 -> TablePrinter.show(products.list());
            case 2 -> {products.add(in.text("Name: "),in.text("Category: "),in.decimal("Price: "));System.out.println("Product added.");}
            case 3 -> TablePrinter.show(stock.list());
            case 4 -> {stock.add(in.number("Product ID: "),in.text("Batch code: "),in.number("Initial stock: "));System.out.println("Batch added.");}
            case 5 -> {stock.restock(in.number("Batch ID: "),in.number("Quantity to add: "));System.out.println("Restocked.");}
            case 6 -> {int batch=in.number("Batch ID: ");recalls.recall(batch,in.text("Defect reason: "));System.out.println("Batch recalled. Future orders blocked.");batchTrace.showAffected(batch);}
            case 7 -> batchTrace.showAffected(in.number("Batch ID: "));
            case 8 -> TablePrinter.show(recalls.list());
            case 9 -> {TablePrinter.show(claims.all());int id=in.number("Claim ID (0 to skip): ");if(id>0){int ans=in.number("1 Approve / 2 Reject: ");if(ans==1||ans==2){claims.review(id,ans==1);System.out.println("Updated.");}else System.out.println("Invalid choice.");}}
            case 0 -> {return;}
            default -> System.out.println("Invalid option.");
        }
    }catch(Exception e){System.out.println("Error: "+e.getMessage());}}}
}
