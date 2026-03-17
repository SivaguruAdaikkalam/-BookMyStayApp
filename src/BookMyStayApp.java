import java.util.*;

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

// ---------------- Booking History ----------------
class BookingHistory {

    // List to maintain order of confirmed bookings
    private List<Reservation> history = new ArrayList<>();

    // Add confirmed reservation
    public void addReservation(Reservation r) {
        history.add(r);
    }

    // Get all reservations
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// ---------------- Booking Report Service ----------------
class BookingReportService {

    // Display all bookings
    public void showAllBookings(List<Reservation> list) {
        if (list.isEmpty()) {
            System.out.println("No booking history available.");
            return;
        }

        System.out.println("\n--- Booking History ---");
        for (Reservation r : list) {
            System.out.println("ID: " + r.reservationId +
                    " | Guest: " + r.guestName +
                    " | Room: " + r.roomType);
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> list) {
        System.out.println("\n--- Booking Summary ---");
        System.out.println("Total Bookings: " + list.size());
    }
}

// ---------------- Main Class ----------------
public class BookMyStayApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        System.out.print("Enter number of confirmed bookings: ");
        int n = sc.nextInt();
        sc.nextLine();

        // Simulating confirmed bookings
        for (int i = 0; i < n; i++) {
            System.out.print("Enter Reservation ID: ");
            String id = sc.nextLine();

            System.out.print("Enter Guest Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Room Type: ");
            String room = sc.nextLine();

            Reservation r = new Reservation(id, name, room);
            history.addReservation(r);
        }

        // Admin viewing reports
        reportService.showAllBookings(history.getAllReservations());
        reportService.generateSummary(history.getAllReservations());

        sc.close();
    }
}