/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Management;

import Model.Fruit;
import Model.Order;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

/**
 *
 * @author T495s
 */
public class Management {

    Scanner in;
    Validation validation;
    ArrayList<Fruit> lf;
    ArrayList<Order> lo;
    Hashtable<String, ArrayList<Order>> ht;

    public Management() {
        in = new Scanner(System.in);
        validation = new Validation();
        lf = new ArrayList<>();
        lo = new ArrayList<>();
        ht = new Hashtable<>();
    }

    //display menu
    public void menu() {
        int option;
        System.out.println("==========Fruit Shop System==========");
        while (true) {
            System.out.println("1. Create Fruit");
            System.out.println("2. View orders");
            System.out.println("3. Shopping(for buyer)");
            System.out.println("4. Exit");
            System.out.println("============================");
            System.out.print("Enter your choice: ");
            option = validation.checkInputIntLimit(1, 4);
            switch (option) {
                case 1:
                    createFruit();
                    break;
                case 2:
                    viewOrder();
                    break;
                case 3:
                    shopping();
                    break;
                case 4:
                    return;
            }
        }
    }

    //allow user create fruit
    public void createFruit() {
        //loop until user don't want to create fruit
        while (true) {
            System.out.print("Enter fruit id: ");
            String fruitId = validation.checkInputString();
            //check id exist
            if (!validation.checkIdExist(lf, fruitId)) {
                System.out.println("Id exist");
                return;
            }
            System.out.print("Enter fruit name: ");
            String fruitName = validation.checkInputString();
            System.out.print("Enter price: ");
            double price = validation.checkInputDouble();
            System.out.print("Enter quantity: ");
            int quantity = validation.checkInputInt();
            System.out.print("Enter origin: ");
            String origin = validation.checkInputString();
            lf.add(new Fruit(fruitId, fruitName, price, quantity, origin));
            //check user want to continue or not
            if (!validation.checkInputYN()) {
                return;
            }
        }
    }

    //allow user show view order
    public void viewOrder() {
        for (String name : ht.keySet()) {
            System.out.println("Customer: " + name);
            lo = ht.get(name);
            displayListOrder();
        }
    }

    //display list order
    public void displayListOrder() {
        double total = 0;
        System.out.printf("%-15s%-15s%-15s%-15s\n", "Product", "Quantity", "Price", "Amount");
        for (Order order : lo) {
            System.out.printf("%-15s%-15d$%-15.0f$%-15.0f\n", order.getFruitName(),
                    order.getQuantity(), order.getPrice(),
                    order.getPrice() * order.getQuantity());
            total += order.getPrice() * order.getQuantity();
        }
        System.out.println("Total: $" + total);
    }

    //allow user buy items
    public void shopping() {
        int orderId = 0;
        if (lf.isEmpty()) {
            System.out.println("Out of stock.");
            return;
        }
        //loop until user don't want to buy continue       
        while (true) {
            //check list empty user can't buy
            if (lf.isEmpty()) {
                System.out.println("Out of stock.");
                break;
            }
            displayListFruit();
            orderId++;           
            System.out.print("Enter item: ");
            int item = validation.checkInputIntLimit(1, lf.size());
            Fruit fruit = getFruitByItem(item);
            System.out.print("Enter quantity: ");
            int quantity = validation.checkInputIntLimit(0, fruit.getQuantity());           
            fruit.setQuantity(fruit.getQuantity() - quantity);
            if (fruit.getQuantity() == 0) {
                lf.remove(fruit);
            }
            //check item exist or not
            if (!validation.checkItemExist(lo, fruit.getFruitId())) {
                updateOrder(fruit.getFruitId(), quantity,orderId);
            } else {
                lo.add(new Order(orderId, fruit.getFruitId(), fruit.getFruitName(),
                        quantity, fruit.getPrice()));
            }
            if (!validation.checkInputYN()) {
                break;
            } else {
                continue;
            }
        }

        displayListOrder();
        System.out.print("Enter name: ");
        String name = validation.checkInputString();
        ht.put(name, lo);
        System.out.println("Add successful");
    }

    //display list fruit in shop
    public void displayListFruit() {
        int countItem = 1;
        System.out.printf("%-10s%-20s%-20s%-15s%-15s\n", "Item", "Fruit name", "Origin", "Price", "Quantity");
        for (Fruit fruit : lf) {
            if (fruit.getQuantity() != 0) {
                System.out.printf("%-10d%-20s%-20s$%-15.0f%-15d\n", countItem++,
                        fruit.getFruitName(), fruit.getOrigin(), fruit.getPrice(), fruit.getQuantity());
            }
        }
    }

    //get fruit user want to buy
    public Fruit getFruitByItem(int item) {
        int countItem = 1;
        for (Fruit fruit : lf) {
            //check shop have item or not 
            if (fruit.getQuantity() != 0) {
                countItem++;
            }
            if (countItem - 1 == item) {
                return fruit;
            }
        }
        return null;
    }

    //if order exist then update order
    public void updateOrder(String id, int quantity,int orderId) {
        for (Order order : lo) {
            if (order.getFruitId().equalsIgnoreCase(id)&&order.getOrderId()==orderId) {
                order.setQuantity(order.getQuantity() + quantity);
                order.setOrderId(orderId);
                return;
            }
        }
    }
}
