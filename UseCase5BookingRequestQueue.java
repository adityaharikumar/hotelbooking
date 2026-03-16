package hotelbooking;

import java.util.LinkedList;
import java.util.Queue;

// Reservation class representing a guest booking request
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// Booking Request Queue class
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request to queue
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }

    // Display all queued requests
    public void displayRequests() {
        System.out.println("Booking Requests in Queue:");

        for (Reservation r : requestQueue) {
            r.displayReservation();
        }
    }
}

// Main application
public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay");
        System.out.println("Hotel Booking System v5.0");
        System.out.println();

        BookingRequestQueue queue = new BookingRequestQueue();

        // Guests submit booking requests
        queue.addRequest(new Reservation("Rahul", "Single Room"));
        queue.addRequest(new Reservation("Anita", "Double Room"));
        queue.addRequest(new Reservation("Vikram", "Suite Room"));

        // Display queued requests (arrival order preserved)
        queue.displayRequests();
    }
}
