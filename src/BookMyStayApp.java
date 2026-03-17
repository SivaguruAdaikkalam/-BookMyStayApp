import java.util.*;

// ---------------- Reservation Class ----------------
class Reservation {
    String reservationId;
    String roomType;
    String roomId;

    Reservation(String reservationId, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.roomType = roomType;
        this.roomId = roomId;
    }
}

// ---------------- Inventory Class ----------------
class RoomInventory {

    private Map<String, Integer> rooms = new HashMap<>();

    RoomInventory() {
        rooms.put("Single", 2);
        rooms.put("Double", 2);
        rooms.put("Suite", 1);
    }

    // Allocate room
    public void allocateRoom(String roomType) {
        rooms.put(roomType, rooms.get(roomType) - 1);
    }

    // Restore room
    public void restoreRoom(String roomType) {
        rooms.put(roomType, rooms.get(roomType) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + " : " + rooms.get(type));
        }
    }
}

// ---------------- Booking History ----------------
class BookingHistory {
    private Map<String, Reservation> bookings = new HashMap<>();

    public void addBooking(Reservation r) {
        bookings.put(r.reservationId, r);
    }

    public Reservation getBooking(String id) {
        return bookings.get(id);
    }

    public void removeBooking(String id) {
        bookings.remove(id);
    }

    public boolean exists(String id) {
        return bookings.containsKey(id);
    }
}

// ---------------- Cancellation Service ----------------
class CancellationService {

    // Stack for rollback (LIFO)
    private Stack<String> rollbackStack = new Stack<>();

    public void cancelBooking(String reservationId,
                              BookingHistory history,
                              RoomInventory inventory) {

        // Validate booking exists
        if (!history.exists(reservationId)) {
            System.out.println("Cancellation Failed: Booking does not exist.");
            return;
        }

        // Get reservation
        Reservation r = history.getBooking(reservationId);

        // Push room ID to stack (for rollback tracking)
        rollbackStack.push(r.roomId);

        // Restore inventory
        inventory.restoreRoom(r.roomType);

        // Remove booking
        history.removeBooking(reservationId);

        System.out.println("Booking Cancelled Successfully!");
        System.out.println("Released Room ID: " + rollbackStack.peek());
    }
}

// ---------------- Main Class ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancelService = new CancellationService();

        // Simulate a confirmed booking
        Reservation r1 = new Reservation("R101", "Single", "S1");
        history.addBooking(r1);
        inventory.allocateRoom("Single");

        System.out.println("Sample booking created with ID: R101");

        // Cancellation input
        System.out.print("Enter Reservation ID to cancel: ");
        String id = sc.nextLine();

        // Perform cancellation
        cancelService.cancelBooking(id, history, inventory);

        // Show updated inventory
        inventory.displayInventory();

        sc.close();
    }
}