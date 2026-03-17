import java.io.*;
import java.util.*;

// ---------------- Reservation Class ----------------
class Reservation implements Serializable {
    String reservationId;
    String guestName;
    String roomType;

    Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// ---------------- Inventory Class ----------------
class RoomInventory implements Serializable {
    Map<String, Integer> rooms = new HashMap<>();

    RoomInventory() {
        rooms.put("Single", 2);
        rooms.put("Double", 2);
        rooms.put("Suite", 1);
    }

    public void displayInventory() {
        System.out.println("\nInventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + " : " + rooms.get(type));
        }
    }
}

// ---------------- System State ----------------
class SystemState implements Serializable {
    List<Reservation> bookings;
    RoomInventory inventory;

    SystemState(List<Reservation> bookings, RoomInventory inventory) {
        this.bookings = bookings;
        this.inventory = inventory;
    }
}

// ---------------- Persistence Service ----------------
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state to file
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load state from file
    public static SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("System state loaded successfully.");
            return (SystemState) ois.readObject();

        } catch (Exception e) {
            System.out.println("No previous data found. Starting fresh.");
            return null;
        }
    }
}

// ---------------- Main Class ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        // Try loading previous state
        SystemState state = PersistenceService.load();

        List<Reservation> bookings;
        RoomInventory inventory;

        if (state != null) {
            bookings = state.bookings;
            inventory = state.inventory;
        } else {
            bookings = new ArrayList<>();
            inventory = new RoomInventory();
        }

        Scanner sc = new Scanner(System.in);

        System.out.print("Add new booking? (yes/no): ");
        String choice = sc.nextLine();

        if (choice.equalsIgnoreCase("yes")) {

            System.out.print("Enter Reservation ID: ");
            String id = sc.nextLine();

            System.out.print("Enter Guest Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Room Type: ");
            String room = sc.nextLine();

            bookings.add(new Reservation(id, name, room));

            // Reduce inventory
            inventory.rooms.put(room, inventory.rooms.get(room) - 1);

            System.out.println("Booking added.");
        }

        // Display current state
        System.out.println("\n--- Current Bookings ---");
        for (Reservation r : bookings) {
            System.out.println(r.reservationId + " | " + r.guestName + " | " + r.roomType);
        }

        inventory.displayInventory();

        // Save state before exit
        PersistenceService.save(new SystemState(bookings, inventory));

        sc.close();
    }
}