public import java.util.*;

// Booking Request Model
class BookingRequest {
    String customerName;
    String roomType;

    public BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// Booking Service
class BookingService {

    private Queue<BookingRequest> requestQueue = new LinkedList<>();

    // Set to store unique room IDs
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map roomType -> allocated rooms
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    private InventoryService inventoryService;

    public BookingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void addRequest(BookingRequest request) {
        requestQueue.add(request);
    }

    public void processBookings() {
        while (!requestQueue.isEmpty()) {

            // FIFO dequeue
            BookingRequest request = requestQueue.poll();

            System.out.println("\nProcessing booking for: " + request.customerName);

            // Check availability
            if (!inventoryService.isAvailable(request.roomType)) {
                System.out.println("No rooms available for type: " + request.roomType);
                continue;
            }

            // Generate unique room ID
            String roomId = generateRoomId(request.roomType);

            // Ensure uniqueness using Set
            if (allocatedRoomIds.contains(roomId)) {
                System.out.println("Duplicate Room ID detected! Skipping...");
                continue;
            }

            // Atomic operation: allocate + update inventory
            allocatedRoomIds.add(roomId);

            roomAllocations
                .computeIfAbsent(request.roomType, k -> new HashSet<>())
                .add(roomId);

            inventoryService.decrement(request.roomType);

            // Confirm booking
            System.out.println("Booking Confirmed!");
            System.out.println("Customer: " + request.customerName);
            System.out.println("Room Type: " + request.roomType);
            System.out.println("Room ID: " + roomId);

            inventoryService.displayInventory();
        }
    }

    private String generateRoomId(String roomType) {
        return roomType.substring(0, 1).toUpperCase() + UUID.randomUUID().toString().substring(0, 5);
    }

    public void displayAllocations() {
        System.out.println("\nFinal Room Allocations:");
        for (String type : roomAllocations.keySet()) {
            System.out.println(type + " -> " + roomAllocations.get(type));
        }
    }
}

// Main Class
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        InventoryService inventoryService = new InventoryService();
        BookingService bookingService = new BookingService(inventoryService);

        // Add booking requests (FIFO Queue)
        bookingService.addRequest(new BookingRequest("Aditya", "Single"));
        bookingService.addRequest(new BookingRequest("Rahul", "Double"));
        bookingService.addRequest(new BookingRequest("Priya", "Single"));
        bookingService.addRequest(new BookingRequest("Kiran", "Single")); // Might fail if no inventory

        // Process bookings
        bookingService.processBookings();

        // Show final allocation
        bookingService.displayAllocations();
    }
} {
    
}
