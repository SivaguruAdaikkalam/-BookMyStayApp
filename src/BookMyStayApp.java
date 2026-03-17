import java.util.*;

// ---------------- Add-On Service Class ----------------
class AddOnService {
    String serviceName;
    double price;

    AddOnService(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }
}

// ---------------- Add-On Service Manager ----------------
class AddOnServiceManager {

    // Map: Reservation ID → List of Services
    private HashMap<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add services to a reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total additional cost
    public double calculateTotalCost(String reservationId) {
        double total = 0;
        List<AddOnService> services = getServices(reservationId);

        for (AddOnService s : services) {
            total += s.price;
        }
        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Services for Reservation ID: " + reservationId);
        for (AddOnService s : services) {
            System.out.println("- " + s.serviceName + " : ₹" + s.price);
        }

        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
    }
}

// ---------------- Main Class (UC7 Execution) ----------------
public class BookMyStayApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AddOnServiceManager manager = new AddOnServiceManager();

        System.out.print("Enter Reservation ID: ");
        String reservationId = sc.nextLine();

        System.out.print("How many services do you want to add? ");
        int n = sc.nextInt();
        sc.nextLine(); // consume newline

        for (int i = 0; i < n; i++) {
            System.out.print("Enter Service Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Service Price: ");
            double price = sc.nextDouble();
            sc.nextLine();

            AddOnService service = new AddOnService(name, price);
            manager.addService(reservationId, service);
        }

        System.out.println("\n--- Selected Add-On Services ---");
        manager.displayServices(reservationId);

        sc.close();
    }
}