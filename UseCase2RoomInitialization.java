package hotelbooking;


abstract class Room {
    protected String roomType;
    protected int beds;
    protected int size;
    protected double price;

    public Room(String roomType, int beds, int size, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq ft");
        System.out.println("Price: $" + price);
    }
}

// Single Room class
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 200, 100);
    }
}

// Double Room class
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 350, 180);
    }
}

// Suite Room class
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 500, 300);
    }
}

// Main Application Class
public class UseCase2RoomInitialization {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay");
        System.out.println("Hotel Booking System v2.1");
        System.out.println();

        // Create room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability variables
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Display room details and availability
        single.displayRoomDetails();
        System.out.println("Available Rooms: " + singleAvailable);
        System.out.println();

        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + doubleAvailable);
        System.out.println();

        suite.displayRoomDetails();
        System.out.println("Available Rooms: " + suiteAvailable);
    }
}
