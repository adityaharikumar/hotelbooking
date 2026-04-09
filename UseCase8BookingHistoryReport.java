import java.util.ArrayList;
import java.util.List;

// Reservation class (Represents a confirmed booking)
class Reservation {
    private int bookingId;
    private String customerName;
    private String roomType;
    private int nights;

    public Reservation(int bookingId, String customerName, String roomType, int nights) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "BookingID: " + bookingId +
               ", Name: " + customerName +
               ", Room: " + roomType +
               ", Nights: " + nights;
    }
}

// Booking History (stores confirmed bookings)
class BookingHistory {
    private List<Reservation> historyList;

    public BookingHistory() {
        historyList = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation r) {
        historyList.add(r);
    }

    // Retrieve all bookings
    public List<Reservation> getAllReservations() {
        return historyList;
    }
}

// Reporting Service (generates reports)
class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<Reservation> reservations) {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> reservations) {
        System.out.println("\n--- Booking Summary Report ---");

        int totalBookings = reservations.size();
        int totalNights = 0;

        for (Reservation r : reservations) {
            totalNights += r.getNights();
        }

        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Nights Booked: " + totalNights);
    }
}

// Main Class
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        // Create booking history
        BookingHistory history = new BookingHistory();

        // Simulating confirmed bookings
        Reservation r1 = new Reservation(1, "Aditya", "Deluxe", 2);
        Reservation r2 = new Reservation(2, "Rahul", "Suite", 3);
        Reservation r3 = new Reservation(3, "Sneha", "Standard", 1);

        // Add to history (insertion order maintained)
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Reporting
        BookingReportService reportService = new BookingReportService();

        // Admin views booking history
        reportService.displayAllBookings(history.getAllReservations());

        // Admin views summary report
        reportService.generateSummary(history.getAllReservations());
    }
}