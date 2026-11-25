import java.io.*;
import java.util.*;

/**
 * Restaurant Order Queue
 * Concepts: Queue, LinkedList, Methods, File Handling
 * Orders are stored in orders.txt and served in FIFO order.
 */
public class RestaurantOrderQueue {

    // Simple class to hold one order
    static class Order {
        String id, customer, dish;
        int quantity;

        Order(String id, String customer, String dish, int quantity) {
            this.id = id;
            this.customer = customer;
            this.dish = dish;
            this.quantity = quantity;
        }

        public String toString() {
            return id + " | " + customer + " | " + dish + " x" + quantity;
        }
    }

    static final String FILE_NAME = "orders.txt";
    static Queue<Order> queue = new LinkedList<>();   
    static int nextId = 101;                   
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadFromFile();
        int choice;
        do {
            System.out.println("\n--- Restaurant Order Queue ---");
            System.out.println("1. Place Order");
            System.out.println("2. Serve Next Order");
            System.out.println("3. View Pending Orders");
            System.out.println("4. Cancel Order by ID");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                choice = 0;
            }
            switch (choice) {
                case 1: placeOrder(); break;
                case 2: serveNext(); break;
                case 3: viewPending(); break;
                case 4: cancelOrder(); break;
                case 5: saveToFile(); System.out.println("Orders saved. Goodbye!"); break;
                default: System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 5);
    }
    static void placeOrder() {
        System.out.print("Customer: ");
        String customer = sc.nextLine().trim();
        System.out.print("Dish: ");
        String dish = sc.nextLine().trim();
        System.out.print("Quantity: ");
        int qty;
        try {
            qty = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            qty = 0;
        }
        if (qty < 1) {
            System.out.println("Invalid quantity. Quantity must be at least 1.");
            return;
        }
        Order o = new Order("O" + nextId++, customer, dish, qty);
        queue.add(o);
        System.out.println("Order placed. ID: " + o + " | Position: " + queue.size());
    }

    // 2. Remove the first order (FIFO)
    static void serveNext() {
        Order o = queue.poll();
        if (o == null) {
            System.out.println("No pending orders.");
            return;
        }
        System.out.println("Order " + o.id + " served. Pending orders: " + queue.size());
    }

    static void viewPending() {
        System.out.println("Pending: " + queue.size());
        int i = 1;
        for (Order o : queue) {
            System.out.println("  " + i++ + ". " + o);
        }
    }

    static void cancelOrder() {
        System.out.print("Enter Order ID to cancel: ");
        String id = sc.nextLine().trim();
        Iterator<Order> it = queue.iterator();
        while (it.hasNext()) {
            if (it.next().id.equalsIgnoreCase(id)) {
                it.remove();
                System.out.println("Order " + id.toUpperCase() + " cancelled. Pending orders: " + queue.size());
                return;
            }
        }
        System.out.println("Order ID not found.");
    }

    static void loadFromFile() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 4) {
                    queue.add(new Order(p[0], p[1], p[2], Integer.parseInt(p[3])));
                    int num = Integer.parseInt(p[0].substring(1));
                    if (num >= nextId) nextId = num + 1;
                }
            }
        } catch (Exception e) {
            System.out.println("Could not read file: " + e.getMessage());
        }
    }
    static void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Order o : queue) {
                pw.println(o.id + "," + o.customer + "," + o.dish + "," + o.quantity);
            }
        } catch (IOException e) {
            System.out.println("Could not save file: " + e.getMessage());
        }
    }
}

/*--- Restaurant Order Queue ---
1. Place Order
2. Serve Next Order
3. View Pending Orders
4. Cancel Order by ID
5. Exit
Enter choice: 1
Customer: Aarav
Dish: Paneer Butter Masala
Quantity: 2
Order placed. ID: O101 | Aarav | Paneer Butter Masala x2 | Position: 1

--- Restaurant Order Queue ---
1. Place Order
2. Serve Next Order
3. View Pending Orders
4. Cancel Order by ID
5. Exit
Enter choice: 1
Customer: Priya
Dish: Masala Dosa
Quantity: 3
Order placed. ID: O102 | Priya | Masala Dosa x3 | Position: 2

--- Restaurant Order Queue ---
1. Place Order
2. Serve Next Order
3. View Pending Orders
4. Cancel Order by ID
5. Exit
Enter choice: 1
Customer: Rohan
Dish: Veg Biryani
Quantity: 1
Order placed. ID: O103 | Rohan | Veg Biryani x1 | Position: 3

--- Restaurant Order Queue ---
1. Place Order
2. Serve Next Order
3. View Pending Orders
4. Cancel Order by ID
5. Exit
Enter choice: 3
Pending: 3
  1. O101 | Aarav | Paneer Butter Masala x2
  2. O102 | Priya | Masala Dosa x3
  3. O103 | Rohan | Veg Biryani x1

--- Restaurant Order Queue ---
1. Place Order
2. Serve Next Order
3. View Pending Orders
4. Cancel Order by ID
5. Exit
Enter choice: 2
Order O101 served. Pending orders: 2

--- Restaurant Order Queue ---
1. Place Order
2. Serve Next Order
3. View Pending Orders
4. Cancel Order by ID
5. Exit
Enter choice: 4
Enter Order ID to cancel: O103
Order O103 cancelled. Pending orders: 1

--- Restaurant Order Queue ---
1. Place Order
2. Serve Next Order
3. View Pending Orders
4. Cancel Order by ID
5. Exit
Enter choice: 3
Pending: 1
  1. O102 | Priya | Masala Dosa x3

--- Restaurant Order Queue ---
1. Place Order
2. Serve Next Order
3. View Pending Orders
4. Cancel Order by ID
5. Exit
Enter choice: 5
Orders saved. Goodbye!*/
