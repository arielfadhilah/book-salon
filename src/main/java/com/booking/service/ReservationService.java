package com.booking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import com.booking.models.Customer;
import com.booking.models.Employee;
import com.booking.models.Person;
import com.booking.models.Reservation;
import com.booking.models.Service;
import com.booking.repositories.PersonRepository;
import com.booking.repositories.ServiceRepository;

public class ReservationService {
    private static List<Reservation> reservationList = new ArrayList<>();
    private static List<Person> customerList = PersonRepository.getAllPerson();
    private static List<Person> employeeList = PersonRepository.getAllPerson();
    private static List<Service> serviceList = ServiceRepository.getAllService();

    public static List<Reservation> getReservationList() {
        return reservationList;
    }

    public static void createReservation(Scanner input){
        // Memilih customer
        Customer customer = selectCustomer(input);
        if (customer == null) {
            System.out.println("Customer yang dicari tidak tersedia.");
            return;
        }

        // Memeriksa validitas customer ID
        if (!ValidationService.validateCustomerId(customer.getId())) {
            System.out.println("Customer ID tidak valid.");
            return;
    }
        

        // Memilih employee
        Employee employee = selectEmployee(input);
        if (employee == null) {
            System.out.println("Employee yang dicari tidak tersedia.");
            return;
        }
        // Memilih service
        List<Service> selectedServices = selectServices(input);

        // Membuat reservation id
        String reservationId = generateReservationId();

        // Menghitung reservationPrice berdasarkan customer
        double reservationPrice = calculateReservationPrice(selectedServices, customer);

        // Membuat reservation
        Reservation reservation = new Reservation(reservationId, customer, employee, selectedServices, reservationPrice, "In Process");
        reservationList.add(reservation);

        System.out.println("Reservation created with ID: " + reservationId);
    }

    private static Customer selectCustomer(Scanner scanner) {
        System.out.println("Available Customer IDs:");
        for (Person person : customerList) {
            if (person instanceof Customer) {
            Customer customer = (Customer) person;
            System.out.println(customer.getId() + ". " + customer.getName());
        }
    }

        System.out.print("Masukkan customer id: ");
        String customerId = scanner.nextLine().trim();
        for (Person person : customerList) {
            if (person instanceof Customer && ((Customer) person).getId().equals(customerId)) {
                return (Customer) person;
            }
        }
        return null;
    }

    private static Employee selectEmployee(Scanner scanner) {
        System.out.println("Available Employee IDs:");
        for (Person person : employeeList) {
            if (person instanceof Employee) {
                Employee employee = (Employee) person;
                System.out.println(employee.getId() + ". " + employee.getName());
            }
        }

        System.out.print("Masukkan employee id: ");
        String employeeId = scanner.nextLine().trim();
            for (Person person : employeeList) {
                if (person.getId().equals(employeeId) && person instanceof Employee) {
                    return (Employee) person;
                }
            }
        return null;
    }

    private static List<Service> selectServices(Scanner scanner) {
        System.out.println("Available Service IDs:");
    for (Service service : serviceList) {
        System.out.println(service.getServiceId() + ". " + service.getServiceName());
    }

    List<Service> selectedServices = new ArrayList<>();
    boolean stopSelection = false;
    while (!stopSelection) {
        System.out.print("Masukkan service id (atau ketik 'stop' untuk menghentikan pemilihan): ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("stop")) {
            stopSelection = true;
        } else {
            Service selectedService = getServiceById(input);
            if (selectedService == null) {
                System.out.println("Service yang dicari tidak tersedia.");
            } else if (selectedServices.contains(selectedService)) {
                System.out.println("Service sudah dipilih.");
            } else {
                selectedServices.add(selectedService);
                System.out.println("Service '" + selectedService.getServiceName() + "' telah dipilih.");
            }
        }
    }
    return selectedServices;
}

    private static Service getServiceById(String serviceId) {
        for (Service service : ServiceRepository.getAllService()) {
            if (service.getServiceId().equals(serviceId)) {
                return service;
            }
        }
        return null;
    }

    private static String generateReservationId() {
        return UUID.randomUUID().toString();
    }


    // Function untuk mengedit workstage dari reservation
    public static void editReservationWorkstage(String reservationId, String newWorkstage){
        // Implementasi untuk mengedit workstage dari reservation berdasarkan reservation id
    }

    private static double calculateReservationPrice(List<Service> selectedServices, Customer customer) {
    double totalPrice = 0.0;
    for (Service service : selectedServices) {
        totalPrice += service.getPrice();
    }

    double discount = 0.0;
    if (customer.getMember().getType().equalsIgnoreCase("Silver")) {
        discount = 0.05;
    } else if (customer.getMember().getType().equalsIgnoreCase("Gold")) {
        discount = 0.1;
    }

    return totalPrice * (1 - discount);
}

    public static void viewInProcessReservations() {
        System.out.println("In Process Reservations:");
        for (Reservation reservation : reservationList) {
            if (reservation.getWorkstage().equalsIgnoreCase("In Process")) {
                System.out.println("ID: " + reservation.getReservationId() + ", Customer: " + reservation.getCustomer().getName() + ", Employee: " + reservation.getEmployee().getName());
            }
        }
    }

    public static void completeOrCancelReservation(Scanner input) {
    // Meminta input reservation ID
    System.out.print("Enter reservation ID: ");
    String reservationId = input.nextLine().trim();

    // Mencari reservation berdasarkan reservation ID
    Reservation reservation = getReservationById(reservationId);
    if (reservation == null) {
        System.out.println("Reservation not found.");
        return;
    }

    // Memeriksa status reservation
    if (reservation.getWorkstage().equalsIgnoreCase("In Process")) {
        System.out.println("Do you want to complete or cancel this reservation?");
        System.out.println("1. Complete");
        System.out.println("2. Cancel");
        int choice = Integer.valueOf(input.nextLine());

        switch (choice) {
            case 1:
                reservation.setWorkstage("Finish");
                System.out.println("Reservation marked as finished.");

                // Mengurangi jumlah wallet pelanggan
                Customer customer = (Customer) reservation.getCustomer();
                double totalReservationPrice = reservation.getReservationPrice();
                customer.setWallet(customer.getWallet() - totalReservationPrice);

                // Menampilkan info wallet terbaru pelanggan
                System.out.println("Wallet updated. Current balance: " + customer.getWallet());
                break;
            case 2:
                reservation.setWorkstage("Canceled");
                System.out.println("Reservation canceled.");
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }

        // Menampilkan history seluruh reservation setelah complete/cancel reservation
        PrintService printService = new PrintService();
        printService.showHistoryReservation(reservationList);

        // Menghitung total harga dari reservasi dengan status "Finish"
        double totalFinishPrice = 0;
        for (Reservation r : reservationList) {
            if (r.getWorkstage().equalsIgnoreCase("Finish")) {
                totalFinishPrice += r.getReservationPrice();
            }
        }
        System.out.println("Pendapatan dari status pembayaran berhasil: " + totalFinishPrice);
    } else {
        System.out.println("Reservation has already been completed or canceled.");
    }
}

    public static Reservation getReservationById(String reservationId) {
        for (Reservation reservation : reservationList) {
            if (reservation.getReservationId().equals(reservationId)) {
                return reservation;
            }
        }
        return null;
    }

    


    // Silahkan tambahkan function lain, dan ubah function diatas sesuai kebutuhan
}
