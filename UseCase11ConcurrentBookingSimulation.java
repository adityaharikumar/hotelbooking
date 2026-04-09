import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

// Booking Request Class
class BookingRequest {
    String guestName;
    String roomType;
    int count;

    public BookingRequest(String guestName, String roomType, int count) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.count = count;
    }
}

// Thread-Safe Room Inventory
class RoomInventory {
    private Map<String, Integer> rooms;

    public RoomInventory() {
        rooms = new HashMap<>();
        rooms.put("DELUXE", 3);
        rooms.put("STANDARD", 5);
        rooms.put("SUITE", 2);
    }

    // Critical Section (Synchronized)
    public synchronized boolean bookRoom(String type, int count, String guestName) {
        int available = rooms.getOrDefault(type, 0);

        System.out.println(guestName + " trying to book " + count + " " + type + " room(s)");

        if (available >= count) {
            // Simulate delay (to expose race condition if not synchronized)
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}

            rooms.put(type, available - count);
            System.out.println("Booking SUCCESS for " + guestName);
            return true;
        } else {
            System.out.println("Booking FAILED for " + guestName + " (Not enough rooms)");
            return false;
        }
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + " : " + rooms.get(type));
        }
    }
}

// Shared Booking Queue
class BookingQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
    }

    public synchronized BookingRequest getRequest() {
        return queue.poll();
    }
}

// Worker Thread
class BookingProcessor extends Thread {
    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {
        while (true) {
            BookingRequest request;

            // Synchronized retrieval (shared mutable state protection)
            synchronized (queue) {
                request = queue.getRequest();
            }

            if (request == null) {
                break;
            }

            // Critical Section handled inside inventory
            inventory.bookRoom(request.roomType, request.count, request.guestName);
        }
    }
}

// Main Class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Simulate multiple concurrent booking requests
        queue.addRequest(new BookingRequest("Guest1", "DELUXE", 2));
        queue.addRequest(new BookingRequest("Guest2", "DELUXE", 2));
        queue.addRequest(new BookingRequest("Guest3", "STANDARD", 3));
        queue.addRequest(new BookingRequest("Guest4", "STANDARD", 3));
        queue.addRequest(new BookingRequest("Guest5", "SUITE", 1));

        // Multiple threads (simulating concurrent users)
        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);
        BookingProcessor t3 = new BookingProcessor(queue, inventory);

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {}

        // Final consistent state
        inventory.displayInventory();
    }
}