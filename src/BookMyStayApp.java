import java.util.*;

// ---------------- Custom Exception ----------------
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// ---------------- Reservation Class ----------------
class Reservation {
    String reservationId;
    String guestName;
    String roomType;

    Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// ---------------- Validator Class ----------------
class InvalidBookingValidator {

    // Allowed room types
    private static final Set<String> validRoomTypes =
            new HashSet<>(Arrays.asList("Single", "Double", "Suite"));

    // Validate booking input
    public static void validate(String reservationId, String guestName, String roomType)
            throws InvalidBookingException {

        // Fail-fast validation
        if (reservationId == null || reservationId.isEmpty()) {
            throw new InvalidBookingException("Reservation ID cannot be empty.");
        }

        if (guestName == null || guestName.isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (!validRoomTypes.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type. Allowed: Single, Double, Suite.");
        }
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

    public void allocateRoom(String roomType) throws InvalidBookingException {
        int available = rooms.getOrDefault(roomType, 0);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }

        rooms.put(roomType, available - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Room Availability:");
        for (String type : rooms.keySet()) {
            System.out.println(type + " : " + rooms.get(type));
        }
    }
}

// ---------------- Main Class ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        RoomInventory inventory = new RoomInventory();

        try {
            System.out.print("Enter Reservation ID: ");
            String id = sc.nextLine();

            System.out.print("Enter Guest Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Room Type (Single/Double/Suite): ");
            String room = sc.nextLine();

            // Step 1: Validate input (Fail Fast)
            InvalidBookingValidator.validate(id, name, room);

            // Step 2: Allocate room (State Protection)
            inventory.allocateRoom(room);

            // Step 3: Create reservation
            Reservation r = new Reservation(id, name, room);

            System.out.println("\nBooking Successful!");
            System.out.println("Reservation ID: " + r.reservationId);

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("\nBooking Failed: " + e.getMessage());
        }

        // System continues safely
        inventory.displayInventory();

        sc.close();
    }
}