package com.tradeshield.ui;

import com.tradeshield.model.User;import com.tradeshield.service.*;import com.tradeshield.util.*;
public class CustomerMenu {
    private final Input in;private final User user;
    private final ProductService products=new ProductService();private final InventoryService stock=new InventoryService();
    private final OrderService orders=new OrderService();private final BillService bills=new BillService();
    private final ReturnService claims=new ReturnService();
    public CustomerMenu(Input in,User user){this.in=in;this.user=user;}
    public void start(){while(true){try{
        TablePrinter.heading("CUSTOMER: "+user.getName());
        System.out.println("1. Browse products\n2. View batches\n3. Buy product from batch\n4. My orders\n5. View bill\n6. Request return/replacement\n7. My claims\n0. Logout");
        switch(in.number("Choose: ")){
            case 1 -> TablePrinter.show(products.list());
            case 2 -> TablePrinter.show(stock.list());
            case 3 -> {TablePrinter.show(stock.list());int id=in.number("Batch ID: ");int qty=in.number("Quantity: ");
                System.out.println("Order placed! Order #"+orders.buy(user.getId(),id,qty));}
            case 4 -> {TablePrinter.show(orders.orders(user.getId()));TablePrinter.heading("YOUR ORDER ITEMS");TablePrinter.show(orders.items(user.getId()));}
            case 5 -> bills.print(in.number("Order ID: "),user.getId());
            case 6 -> {TablePrinter.show(orders.items(user.getId()));int item=in.number("Order item ID: ");
                int kind=in.number("1 Return / 2 Replacement: ");if(kind!=1&&kind!=2){System.out.println("Invalid type.");break;}
                claims.request(item,user.getId(),kind==1?"RETURN":"REPLACEMENT",in.text("Reason: "));System.out.println("Claim submitted.");}
            case 7 -> TablePrinter.show(claims.mine(user.getId()));
            case 0 -> {return;}
            default -> System.out.println("Invalid option.");
        }
    }catch(Exception e){System.out.println("Error: "+e.getMessage());}}}
}
