import java.util.*;

// ---------------- Booking Request ----------------
class BookingRequest {
    String guestName;
    String roomType;

    BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// ---------------- Shared Inventory ----------------
class RoomInventory {

    private Map<String, Integer> rooms = new HashMap<>();

    RoomInventory() {
        rooms.put("Single", 2);
        rooms.put("Double", 1);
    }

    // Critical Section (Thread Safe)
    public synchronized void allocateRoom(BookingRequest request) {

        int available = rooms.getOrDefault(request.roomType, 0);

        if (available > 0) {
            rooms.put(request.roomType, available - 1);

            System.out.println(
                    Thread.currentThread().getName()
                            + " booked "
                            + request.roomType
                            + " room for "
                            + request.guestName
            );

        } else {
            System.out.println(
                    Thread.currentThread().getName()
                            + " FAILED booking for "
                            + request.guestName
                            + " (No rooms available)"
            );
        }
    }

    public void displayInventory() {
        System.out.println("\nRemaining Inventory:");
        for (String type : rooms.keySet()) {
            System.out.println(type + " : " + rooms.get(type));
        }
    }
}

// ---------------- Booking Processor (Thread) ----------------
class BookingProcessor extends Thread {

    private Queue<BookingRequest> queue;
    private RoomInventory inventory;

    BookingProcessor(Queue<BookingRequest> queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {

        while (true) {

            BookingRequest request;

            synchronized (queue) {
                if (queue.isEmpty())
                    break;

                request = queue.poll();
            }

            inventory.allocateRoom(request);
        }
    }
}

// ---------------- Main Class ----------------
public class BookMyStayApp {

    public static void main(String[] args) {

        Queue<BookingRequest> bookingQueue = new LinkedList<>();
        RoomInventory inventory = new RoomInventory();

        // Simulated concurrent booking requests
        bookingQueue.add(new BookingRequest("Siva", "Single"));
        bookingQueue.add(new BookingRequest("Arun", "Single"));
        bookingQueue.add(new BookingRequest("Priya", "Single"));
        bookingQueue.add(new BookingRequest("Rahul", "Double"));
        bookingQueue.add(new BookingRequest("Meena", "Double"));

        // Create multiple threads
        BookingProcessor t1 = new BookingProcessor(bookingQueue, inventory);
        BookingProcessor t2 = new BookingProcessor(bookingQueue, inventory);
        BookingProcessor t3 = new BookingProcessor(bookingQueue, inventory);

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.displayInventory();
    }
}