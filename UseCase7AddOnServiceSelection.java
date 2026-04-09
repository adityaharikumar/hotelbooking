import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Add-On Service Model
class AddOnService {
    String serviceName;
    double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + cost + ")";
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<AddOnService>> reservationServices = new HashMap<>();

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {

        reservationServices
            .computeIfAbsent(reservationId, k -> new ArrayList<>())
            .add(service);

        System.out.println("Service added: " + service.serviceName + " to Reservation ID: " + reservationId);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return reservationServices.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total additional cost
    public double calculateTotalCost(String reservationId) {
        double total = 0;

        List<AddOnService> services = getServices(reservationId);

        for (AddOnService s : services) {
            total += s.cost;
        }

        return total;
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {

        List<AddOnService> services = getServices(reservationId);

        System.out.println("\nServices for Reservation ID: " + reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (AddOnService s : services) {
            System.out.println("- " + s);
        }

        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
    }
}

// Main Class
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        AddOnServiceManager manager = new AddOnServiceManager();

        // Assume reservation IDs (from Use Case 6)
        String reservation1 = "RES101";
        String reservation2 = "RES102";

        // Available services
        AddOnService breakfast = new AddOnService("Breakfast", 200);
        AddOnService wifi = new AddOnService("WiFi", 100);
        AddOnService spa = new AddOnService("Spa", 500);

        // Guest selects services
        manager.addService(reservation1, breakfast);
        manager.addService(reservation1, wifi);

        manager.addService(reservation2, spa);

        // Display services
        manager.displayServices(reservation1);
        manager.displayServices(reservation2);
    }
}