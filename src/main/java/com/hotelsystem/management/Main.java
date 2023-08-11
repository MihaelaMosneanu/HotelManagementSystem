package com.hotelsystem.management;

import com.hotelsystem.management.model.*;
import com.hotelsystem.management.repository.*;
import com.hotelsystem.management.util.Utils;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class Main {
    static EmployeeRepository employeeRepository;
    static GuestRepository guestRepository;
    static HotelRepository hotelRepository;
    static PaymentRepository paymentRepository;
    static ReservationRepository reservationRepository;
    static RoomRepository roomRepository;


    static Scanner scanner = new Scanner(System.in);
    static int selectedHotelIndex;

    static List<Hotel> hotelList;

    public static void main(String[] args) {
        initiateDatabase();
        populateHotelsDb(hotelRepository);
        hotelList = hotelRepository.findAll();
        welcomeMessage(hotelList);

        Hotel selectedHotel = hotelList.get(selectedHotelIndex);
        selectedHotel.setRooms(roomRepository.findAllByHotelId(selectedHotel.getId()));
        processHotelManagement(selectedHotel);
    }

    private static void initiateDatabase() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection dbConnection = databaseConnection.connectToDB();
        employeeRepository = new EmployeeRepositoryImpl(dbConnection);
        guestRepository = new GuestRepositoryImpl(dbConnection);
        hotelRepository = new HotelRepositoryImpl(dbConnection);
        paymentRepository = new PaymentRepositoryImpl(dbConnection);
        reservationRepository = new ReservationRepositoryImpl(dbConnection);
        roomRepository = new RoomRepositoryImpl(dbConnection);
    }

    private static void welcomeMessage(List<Hotel> hotelList) {
        System.out.println("Welcome!");
        System.out.println("Please choose your hotel to manage:");

        int i = 1;
        for (Hotel hotel : hotelList) {
            System.out.println(i + ". " + hotel.getName());
            i++;
        }
        String input = scanner.next();
        if (!validHotelInput(input, hotelList)) {
            welcomeMessage(hotelList);
        }

    }

    private static boolean isValidChoice(String input) {
        if (!Utils.isNumericAndPositive(input)) {
            return false;
        }
        try {
            int choice = Integer.parseInt(input);
            return choice >= 1 && choice <= 8;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private static void processHotelManagement(Hotel selectedHotel) {
        System.out.println();
        System.out.println("==== " + selectedHotel.getName() + " Hotel Management ====");
        System.out.println("1. Check room availability");
        System.out.println("2. Search for a guest");
        System.out.println("3. Make a reservation");
        System.out.println("4. Check-in a guest");
        System.out.println("5. Check-out a guest");
        System.out.println("6. Generate guest bill");
        System.out.println("7. Generate occupancy report for current day");
        System.out.println("8. Return to choose a different hotel");
        System.out.println("9. Exit");
        System.out.println("Enter your choice: ");
        String choice = scanner.next();

        if (!isValidChoice(choice)) {
            System.out.println("Invalid input. Try again.");
            processHotelManagement(selectedHotel);
        }

        switch (Integer.parseInt(choice)) {
            case 1:
                checkRoomAvailability(selectedHotel);
                break;
            case 2:
                searchForGuest(selectedHotel);
                break;
            case 3:
                makeReservation(selectedHotel);
                break;
            case 4:
                checkInGuest(selectedHotel);
                break;
            case 5:
                checkOutGuest(selectedHotel);
                break;
            case 6:
                generateGuestBill(selectedHotel);
                break;
            case 7:
                generateOccupancyReport(selectedHotel);
                break;
            case 8:
                welcomeMessage(hotelList);
                break;
            case 9:
                return;

            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }


    private static boolean validHotelInput(String input, List<Hotel> hotelList) {
        if (Utils.isNumericAndPositive(input)) {
            selectedHotelIndex = Integer.parseInt(input) - 1;
            if (selectedHotelIndex >= 0 && selectedHotelIndex < hotelList.size()) {
                return true;
            } else {
                System.out.println("Invalid hotel selection.");
            }
        } else {
            System.out.println("Invalid input. Please enter a valid number.");
        }
        return false;
    }

    private static void populateHotelsDb(HotelRepository hotelRepository) {
        Hotel ramadaParc = new Hotel("Ramada Parc", "Poligrafiei str 3 ");
        Hotel ramadaPlaza = new Hotel("Ramada Plaza", "Poligrafiei str 3");
        List<Hotel> hotelsToAdd = Arrays.asList(ramadaParc, ramadaPlaza);
        List<Hotel> existingHotels = hotelRepository.findAll();
        boolean exists = existingHotels.stream().map(Hotel::getName).allMatch(hotelsToAdd.stream().map(Hotel::getName).collect(Collectors.toSet())::contains);
        if (!exists) {
            hotelRepository.addHotel(ramadaParc);
            hotelRepository.addHotel(ramadaPlaza);
        }
    }

    private static void checkRoomAvailability(Hotel selectedHotel) {
        System.out.println("Press 'x' for cancel at any time.");
        LocalDate checkInDate = null;
        LocalDate checkOutDate = null;
        try {
            System.out.println("Enter the check-in date (yyyy-MM-dd): ");
            String checkin = scanner.next();
            if (checkin.equalsIgnoreCase("x")) {
                processHotelManagement(selectedHotel);
            } else {
                checkInDate = LocalDate.parse(checkin);
            }

            System.out.println("Enter the check-out date (yyyy-MM-dd): ");
            String checkout = scanner.next();
            if (checkout.equalsIgnoreCase("x")) {
                processHotelManagement(selectedHotel);
            } else {
                checkOutDate = LocalDate.parse(checkout);
            }

            List<Room> availableRooms = new ArrayList<>();
            List<Reservation> reservations = reservationRepository.findAll();

            // Loop through each room in the hotel and check availability
            for (Room room : selectedHotel.getRooms()) {
                boolean isAvailable = true;

                // Check if the room is already reserved for any dates in the specified range
                for (Reservation reservation : reservations) {
                    if (reservation.getAllocatedRoom() == room.getRoomNumber() && !(checkOutDate.isBefore(reservation.getCheckInDate()) || checkInDate.isAfter(reservation.getCheckOutDate()))) {
                        isAvailable = false;
                        break;
                    }
                }

                if (isAvailable) {
                    availableRooms.add(room);
                }
            }

            if (!availableRooms.isEmpty()) {
                System.out.println("Available Rooms for " + selectedHotel.getName() + ":");
                for (Room room : availableRooms) {
                    System.out.println("Room Number: " + room.getRoomNumber());
                    System.out.println("Room Type: " + room.getRoomType());
                    System.out.println("Room Price: " + room.getPricePerNight());
                    System.out.println();
                }
            } else {
                System.out.println("All rooms are booked for the specified dates.");
            }

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date.");
            checkRoomAvailability(selectedHotel);
        }
        processHotelManagement(selectedHotel);
    }

    private static void searchForGuest(Hotel selectedHotel) {
        System.out.println("Press 'x' for cancel at any time.");
        System.out.println();
        System.out.println("Enter the guest's name: (Be sure to type like this: Surname-First Name)");
        String guestName = validateGuestName(selectedHotel, "searchGuest");

        List<Guest> guests = guestRepository.findAllByName(guestName);

        if (!guests.isEmpty()) {
            System.out.println("Guests with the name '" + guestName + "':");
            for (Guest guest : guests) {
                System.out.println("Guest Name: " + guest.getName());
                System.out.println("Guest Contact: " + guest.getEmail());
                if (guest.getStatus() == null) {
                    System.out.println("Status: Guest not yet checked in.");
                } else {
                    System.out.println("Status: " + guest.getStatus());
                }
            }
        } else {
            System.out.println("No guests found with the name: " + guestName);
        }
        processHotelManagement(selectedHotel);
    }

    private static void makeReservation(Hotel selectedHotel) {
        System.out.println("Enter the guest's name: (Be sure to type like this: Surname-First Name)");
        String guestName = validateGuestName(selectedHotel, "reservation");
        System.out.println("Enter the guest's email: ");
        String guestEmail = validateEmail(selectedHotel);
        Guest guest = new Guest(guestName, guestEmail);
        guestRepository.addGuest(guest);

        System.out.println("Enter the room type (Choose from: Standard, Deluxe, Suite): ");
        String roomType = scanner.next();
        validateRoomTypeChosen(roomType, selectedHotel);

        System.out.println("Enter the check-in date (yyyy-MM-dd): ");
        LocalDate checkInDate = validateCheckInDate(selectedHotel);

        System.out.println("Enter the check-out date (yyyy-MM-dd): ");
        LocalDate checkOutDate = validateCheckOutDate(selectedHotel, checkInDate);

        // Retrieve the list of available rooms for the selected dates and room type
        List<Room> availableRooms = new ArrayList<>();
        List<Reservation> reservations = reservationRepository.findAll();

        for (Room room : selectedHotel.getRooms()) {
            boolean isAvailable = true;

            // Check if the room is already reserved for any dates in the specified range
            for (Reservation reservation : reservations) {
                if (reservation.getAllocatedRoom() == room.getRoomNumber() &&
                        !(checkOutDate.isBefore(reservation.getCheckInDate()) ||
                                checkInDate.isAfter(reservation.getCheckOutDate()))) {
                    isAvailable = false;
                    break;
                }
            }

            // Check if the room matches the desired room type and is available
            if (isAvailable && room.getRoomType().equalsIgnoreCase(roomType)) {
                availableRooms.add(room);
            }

        }

        if (!availableRooms.isEmpty()) {
            // Display available rooms and let the user select a room for the reservation
            System.out.println("Available Rooms for " + selectedHotel.getName() + ":");
            for (Room room : availableRooms) {
                System.out.println("Room Number: " + room.getRoomNumber());
                System.out.println("Room Type: " + room.getRoomType());
                System.out.println("Room Price: " + room.getPricePerNight());
                System.out.println();
            }
            System.out.println("Enter the room number for the reservation: ");
            int selectedRoomNumber = validateSelectedRoomNumber(selectedHotel, availableRooms);

            // Check if the selected room number exists and is available
            Room selectedRoom = availableRooms.stream()
                    .filter(room -> room.getRoomNumber() == selectedRoomNumber)
                    .findFirst()
                    .orElse(null);

            if (selectedRoom != null) {
                int reservationNumber = generateReservationNumber();
                int room_id = selectedRoom.getId();

                long noOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
                double amountForInvoice = noOfNights * selectedRoom.getPricePerNight();

                System.out.println("Please choose a payment method. Type 'card' or 'cash'");
                String paymentMethod = validatePaymentMethod(selectedHotel);
                Payment payment = new Payment(amountForInvoice, paymentMethod);
                paymentRepository.addPayment(payment);

                Reservation reservation = new Reservation(reservationNumber, checkInDate, checkOutDate, selectedRoom.getRoomNumber(), room_id, payment.getId(), guest.getId());
                reservationRepository.addReservation(reservation);

                System.out.println("Reservation made successfully for " + selectedHotel.getName() + "!");
                System.out.println("Reservation number: " + reservation.getReservationId());
                processHotelManagement(selectedHotel);
            } else {
                System.out.println("Invalid room selection. Please try again.");
                makeReservation(selectedHotel);
            }
        } else {
            System.out.println("No available rooms for the specified dates and room type.");
            processHotelManagement(selectedHotel);
        }
    }

    private static int validateSelectedRoomNumber(Hotel selectedHotel, List<Room> availableRooms) {
        String input = scanner.next();
        if (input.equalsIgnoreCase("x")) {
            processHotelManagement(selectedHotel);
        }
        if (Utils.isNumericAndPositive(input)) {
            try {
                int choice = Integer.parseInt(input);
                boolean result = availableRooms.stream().anyMatch(room -> room.getRoomNumber() == choice);
                if (result) {
                    return choice;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid room selection. Try again.");
                makeReservation(selectedHotel);
            }
        } else {
            System.out.println("Invalid room selection. Try again.");
            makeReservation(selectedHotel);

        }
        return 0;
    }

    private static String validatePaymentMethod(Hotel selectedHotel) {
        String input = scanner.next();
        if (input.equalsIgnoreCase("x")) {
            processHotelManagement(selectedHotel);
        }
        List<String> paymentMethods = Arrays.asList("card", "cash");
        boolean result = paymentMethods.stream().anyMatch(input::equalsIgnoreCase);
        if (result) {
            return input;
        } else {
            System.out.println("Payment method not valid. Try again.");
            makeReservation(selectedHotel);
        }
        return null;
    }

    private static LocalDate validateCheckInDate(Hotel selectedHotel) {
        try {
            LocalDate checkIn = null;
            String checkin = scanner.next();
            if (checkin.equalsIgnoreCase("x")) {
                processHotelManagement(selectedHotel);
            } else {
                checkIn = LocalDate.parse(checkin);
            }

            if (checkIn.isEqual(LocalDate.now()) || checkIn.isAfter(LocalDate.now())) {
                return checkIn;
            } else {
                System.out.println("Check in date has to be higher than today.");
                makeReservation(selectedHotel);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Check in date does not have proper value.");
            makeReservation(selectedHotel);
        }
        return null;
    }

    private static LocalDate validateCheckOutDate(Hotel selectedHotel, LocalDate checkIn) {
        try {
            String checkout = scanner.next();
            LocalDate checkOut = null;
            if (checkout.equalsIgnoreCase("x")) {
                processHotelManagement(selectedHotel);
            } else {
                checkOut = LocalDate.parse(checkout);

            }
            if (checkOut.isAfter(checkIn)) {
                return checkOut;
            } else {
                System.out.println("Check out date has to be higher than check in date.");
                makeReservation(selectedHotel);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Check out date does not have proper value.");
            makeReservation(selectedHotel);
        }
        return null;
    }

    private static String validateEmail(Hotel selectedHotel) {
        String emailAddress = scanner.next();
        if (emailAddress.equalsIgnoreCase("x")) {
            processHotelManagement(selectedHotel);
        }
        String regexPattern = "^(.+)@(\\S+)$";

        boolean matches = Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();

        if (matches) {
            return emailAddress;
        } else {
            System.out.println("Email address does not seem correct.");
            makeReservation(selectedHotel);
        }
        return null;
    }

    private static int generateReservationNumber() {
        return Math.abs(new Random().nextInt()); // generating unique reservation numbers
    }


    private static void checkInGuest(Hotel selectedHotel) {
        System.out.println("Press 'x' for cancel at any time.");
        System.out.println();
        System.out.println("Enter the guest's name: (Be sure to type like this: Surname-First Name)");
        String guestName = validateGuestName(selectedHotel, "checkin");

        // Search for guests with the matching name
        List<Guest> guests = guestRepository.findAllByName(guestName);

        if (!guests.isEmpty()) {
            // Display guests with the matching name
            System.out.println("Guests with the name '" + guestName + "':");
            for (int i = 0; i < guests.size(); i++) {
                System.out.println((i + 1) + ". " + guests.get(i).getName() + " (" + guests.get(i).getEmail() + ")");
            }

            System.out.println("Enter the number corresponding to the guest for check-in: ");
            int selectedGuestNumber = validateSelectedGuestNumber(selectedHotel, guests, "checkin");

            // Check if the selected guest number is valid
            if (selectedGuestNumber >= 1 && selectedGuestNumber <= guests.size()) {
                Guest selectedGuest = guests.get(selectedGuestNumber - 1);

                // Get the reservation details for the selected guest
                List<Reservation> reservationsOfGuest = reservationRepository.findReservationsForGuest(selectedGuest.getId());
                boolean result = reservationsOfGuest.stream().anyMatch(reservation -> reservation.getCheckInDate().isEqual(LocalDate.now()));

                if (result) {
                    selectedGuest.setStatus("checked in");
                    List<Reservation> res = reservationsOfGuest.stream().filter(reservation -> reservation.getCheckInDate().isEqual(LocalDate.now())).toList();
                    if (selectedGuest.getStatus().equals("checked in")) {
                        System.out.println("Guest has already been checked in.");
                        processHotelManagement(selectedHotel);
                    } else {
                        System.out.println(selectedGuest.getName() + " has been checked-in to room number " + res.get(0).getAllocatedRoom());
                        processHotelManagement(selectedHotel);
                    }

                } else {
                    System.out.println("This guest does not have a reservation for today");
                    processHotelManagement(selectedHotel);
                }


            } else {
                System.out.println("Invalid guest selection. Please try again.");
                checkInGuest(selectedHotel);
            }
        } else {
            System.out.println("No guests found with the name: " + guestName);
            processHotelManagement(selectedHotel);
        }
    }

    private static int validateSelectedGuestNumber(Hotel selectedHotel, List<Guest> guests, String action) {
        String input = scanner.next();
        if (input.equalsIgnoreCase("x")) {
            processHotelManagement(selectedHotel);
        }
        if (Utils.isNumericAndPositive(input)) {
            try {
                int choice = Integer.parseInt(input);
                return choice;
            } catch (NumberFormatException e) {
                System.out.println("Invalid guest selection. Try again.");
                switch (action) {
                    case "checkin":
                        checkInGuest(selectedHotel);
                        break;
                    case "checkout":
                        checkOutGuest(selectedHotel);
                        break;
                    case "generateBill":
                        generateGuestBill(selectedHotel);
                        break;
                    default:
                        processHotelManagement(selectedHotel);
                        break;
                }
            }
        }
        System.out.println("Invalid guest selection. Try again.");
        checkInGuest(selectedHotel);
        return 0;
    }

    private static void checkOutGuest(Hotel selectedHotel) {
        System.out.println("Press 'x' for cancel at any time.");
        System.out.println();
        System.out.println("Enter the guest's name:  (Be sure to type like this: Surname-First Name)");
        String guestName = validateGuestName(selectedHotel, "checkout");

        // Search for guests with the matching name
        List<Guest> guests = guestRepository.findAllByName(guestName);

        if (!guests.isEmpty()) {
            // Display guests with the matching name
            System.out.println("Guests with the name '" + guestName + "':");
            for (int i = 0; i < guests.size(); i++) {
                System.out.println((i + 1) + ". " + guests.get(i).getName() + " (" + guests.get(i).getEmail() + ")");
            }

            System.out.println("Enter the number corresponding to the guest for check-out: ");
            int selectedGuestNumber = validateSelectedGuestNumber(selectedHotel, guests, "checkout");

            // Check if the selected guest number is valid
            if (selectedGuestNumber >= 1 && selectedGuestNumber <= guests.size()) {
                Guest selectedGuest = guests.get(selectedGuestNumber - 1);

                // Get the reservation details for the selected guest
                List<Reservation> reservationsOfGuest = reservationRepository.findReservationsForGuest(selectedGuest.getId());
                boolean result = reservationsOfGuest.stream().anyMatch(reservation -> reservation.getCheckOutDate().isEqual(LocalDate.now()));

                if (result) {
                    selectedGuest.setStatus("checked out");
                    guestRepository.updateGuest(selectedGuest);
                    List<Reservation> res = reservationsOfGuest.stream().filter(reservation -> reservation.getCheckOutDate().isEqual(LocalDate.now())).toList();
                    System.out.println(selectedGuest.getName() + " has been checked-out from room number " + res.get(0).getAllocatedRoom());
                } else {
                    System.out.println("This guest does not have a reservation with check-out today");
                }
                processHotelManagement(selectedHotel);
            } else {
                System.out.println("Invalid guest selection. Please try again.");
                checkOutGuest(selectedHotel);
            }
        } else {
            System.out.println("No guests found with the name: " + guestName);
            processHotelManagement(selectedHotel);
        }
    }

    private static void generateGuestBill(Hotel selectedHotel) {
        System.out.println("Enter the guest's name: (Be sure to type like this: Surname-First Name) ");
        String guestName = validateGuestName(selectedHotel, "generateBill");

        // Search for guests with the matching name
        List<Guest> guests = guestRepository.findAllByName(guestName);

        if (!guests.isEmpty()) {
            // Display guests with the matching name
            System.out.println("Guests with the name '" + guestName + "':");
            for (int i = 0; i < guests.size(); i++) {
                System.out.println((i + 1) + ". " + guests.get(i).getName() + " (" + guests.get(i).getEmail() + ")");
            }

            System.out.println("Enter the number corresponding to the guest for generating the bill: ");
            int selectedGuestNumber = validateSelectedGuestNumber(selectedHotel, guests, "generateBill");

            // Check if the selected guest number is valid
            if (selectedGuestNumber >= 1 && selectedGuestNumber <= guests.size()) {
                Guest selectedGuest = guests.get(selectedGuestNumber - 1);

                // Get the reservation details for the selected guest
                List<Reservation> reservationsForGuest = reservationRepository.findReservationsForGuest(selectedGuest.getId());

                for (Reservation reservation : reservationsForGuest) {
                    System.out.println("Reservation number: " + reservation.getReservationId());
                    System.out.println("Check-in date: " + reservation.getCheckInDate());
                    System.out.println("Check-out date: " + reservation.getCheckOutDate());
                    Payment payment = paymentRepository.findPaymentById(reservation.getPaymentId());
                    System.out.println("Total amount: " + payment.getAmount());
                }

                System.out.println("Enter the reservation number corresponding to the payment for generating the bill: ");
                int reservationNumber = validateReservationNumberForPayment(selectedHotel, reservationsForGuest);

                Reservation selectedReservation = reservationRepository.findReservationByReservationNumber(reservationNumber);

                // Display the guest's bill
                System.out.println("----- INVOICE -----");
                System.out.println("Guest Name: " + selectedGuest.getName());
                System.out.println("Room Number: " + selectedReservation.getAllocatedRoom());
                System.out.println("Check-In Date: " + selectedReservation.getCheckInDate());
                System.out.println("Check-Out Date: " + selectedReservation.getCheckOutDate());
                Payment payment = paymentRepository.findPaymentById(selectedReservation.getPaymentId());

                System.out.println("Total Amount: $" + payment.getAmount());
            } else {
                System.out.println("Invalid guest selection. Please try again.");
                generateGuestBill(selectedHotel);
            }
        } else {
            System.out.println("No guests found with the name: " + guestName);
        }
        processHotelManagement(selectedHotel);
    }

    private static int validateReservationNumberForPayment(Hotel selectedHotel, List<Reservation> reservationsForGuest) {
        String inputStr = scanner.next();
        if (inputStr.equalsIgnoreCase("x")) {
            processHotelManagement(selectedHotel);
        }
        try {
            int input = Integer.parseInt(inputStr);
            boolean result = reservationsForGuest.stream().anyMatch(reservation -> reservation.getReservationId() == input);
            if (result) {
                return input;
            } else {
                System.out.println("Reservation number not found. Try again");
                generateGuestBill(selectedHotel);
            }
        } catch (NumberFormatException e) {
            System.out.println("Reservation number format incorrect.");
            generateGuestBill(selectedHotel);
        }
        return 0;
    }

    private static void generateOccupancyReport(Hotel selectedHotel) {
        // Get the total number of rooms in the selected hotel
        int totalRooms = selectedHotel.getRooms().size();

        // Get the list of all reservations for the selected hotel
        List<Reservation> reservations = reservationRepository.findAll();

        List<Reservation> reservationsForCurrentDay = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getCheckInDate().isEqual(LocalDate.now()) || reservation.getCheckInDate().isBefore(LocalDate.now()) && reservation.getCheckOutDate().isAfter(LocalDate.now())) {
                reservationsForCurrentDay.add(reservation);
            }
        }

        // Count the number of occupied rooms
        int occupiedRooms = reservationsForCurrentDay.size();

        // Calculate the occupancy rate
        double occupancyRate = (double) occupiedRooms / totalRooms * 100;

        // Display the occupancy report
        System.out.println("Occupancy Report for " + selectedHotel.getName());
        System.out.println("Total Rooms: " + totalRooms);
        System.out.println("Occupied Rooms: " + occupiedRooms);
        System.out.println("Occupancy Rate: " + occupancyRate + "%");

        processHotelManagement(selectedHotel);
    }

    private static String validateGuestName(Hotel selectedHotel, String action) {
        String guestName = null;
        String input = scanner.next();
        if (input.equalsIgnoreCase("x")) {
            processHotelManagement(selectedHotel);
        }
        if (input.contains("-")) {
            guestName = input.replace("-", " ");
        } else {
            System.out.println("Name not entered as specified");
            switch (action) {
                case "searchGuest":
                    searchForGuest(selectedHotel);
                    break;
                case "reservation":
                    makeReservation(selectedHotel);
                    break;
                case "checkin":
                    checkInGuest(selectedHotel);
                    break;
                case "checkout":
                    checkOutGuest(selectedHotel);
                    break;
                case "generateBill":
                    generateGuestBill(selectedHotel);
                default:
                    break;

            }


        }
        return guestName;
    }

    private static String validateRoomTypeChosen(String input, Hotel selectedHotel) {
        if (input.equalsIgnoreCase("x")) {
            processHotelManagement(selectedHotel);
        }
        List<String> roomTypes = List.of("Standard", "Deluxe", "Suite");
        boolean result = roomTypes.stream().anyMatch(input::equalsIgnoreCase);
        if (result) {
            return input;
        } else {
            System.out.println("The chosen room type does not exist. Retry to make the reservation.");
            makeReservation(selectedHotel);
        }
        return null;
    }
}