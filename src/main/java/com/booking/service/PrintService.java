package com.booking.service;

import java.util.List;

import com.booking.models.Customer;
import com.booking.models.Employee;
import com.booking.models.Person;
import com.booking.models.Reservation;
import com.booking.models.Service;

public class PrintService {
    public static void printMenu(String title, String[] menuArr){
        int num = 1;
        System.out.println(title);
        for (int i = 0; i < menuArr.length; i++) {
            if (i == (menuArr.length - 1)) {   
                num = 0;
            }
            System.out.println(num + ". " + menuArr[i]);   
            num++;
        }
    }

    public String printServices(List<Service> serviceList){
        String result = "";
        // Bisa disesuaikan kembali
        for (Service service : serviceList) {
            result += service.getServiceName() + ", ";
        }
        return result;
    }

    // Function yang dibuat hanya sebgai contoh bisa disesuaikan kembali
    public void showRecentReservation(List<Reservation> reservationList){
        int num = 1;
    double totalProfit = 0;
    System.out.printf("| %-4s | %-4s | %-11s | %-15s | %-15s | %-15s | %-10s |\n",
            "No.", "ID", "Nama Customer", "Service", "Biaya Service", "Pegawai", "Workstage");
    System.out.println("+========================================================================================+");
    for (Reservation reservation : reservationList) {
        if (reservation.getWorkstage().equalsIgnoreCase("Finish")) {
            System.out.printf("| %-4s | %-4s | %-11s | %-15s | %-15s | %-15s | %-10s |\n",
            num, reservation.getReservationId(), reservation.getCustomer().getName(), printServices(reservation.getServices()), reservation.getReservationPrice(), reservation.getEmployee().getName(), reservation.getWorkstage());
            num++;
            totalProfit += reservation.getReservationPrice();
        }
    }
    System.out.println("Keuntungan Total: " + totalProfit);
}

    public void showAllCustomer(List<Customer> customerList){
        System.out.println("List Customer:");
        for (Customer customer : customerList) {
            System.out.println(customer.getId() + ". " + customer.getName());
        }
    }

    public void showAllEmployee(List<Employee> employeeList){
        System.out.println("List Pegawai:");
        for (Employee employee : employeeList) {
            System.out.println(employee.getId() + ". " + employee.getName());
        }
    }

    public static void printCustomers(List<Person> personList) {
        System.out.println("List of Customers:");
        System.out.println("+----+-------------------+----------------------+------------+");
        System.out.println("| ID | Name              | Address              | Wallet     |");
        System.out.println("+----+-------------------+----------------------+------------+");
        for (Person person : personList) {
            if (person instanceof Customer) {
                Customer customer = (Customer) person;
                System.out.printf("| %-2s | %-17s | %-20s | %-10.2f |\n", customer.getId(), customer.getName(), customer.getAddress(), customer.getWallet());
            }
        }
        System.out.println("+----+-------------------+----------------------+------------+");
    }

    public static void printEmployees(List<Person> personList) {
        System.out.println("List of Employees:");
        System.out.println("+----+-------------------+----------------------+-----------------+");
        System.out.println("| ID | Name              | Address              | Experience (yrs) |");
        System.out.println("+----+-------------------+----------------------+-----------------+");
        for (Person person : personList) {
            if (person instanceof Employee) {
                Employee employee = (Employee) person;
                System.out.printf("| %-2s | %-17s | %-20s | %-15s |\n", employee.getId(), employee.getName(), employee.getAddress(), employee.getExperience());
            }
        }
        System.out.println("+----+-------------------+----------------------+-----------------+");
    }

    public void showHistoryReservation(List<Reservation> reservationList) {
        System.out.println("History of Reservations:");
        System.out.println("+----+-------------------+----------------------+------------+--------------+");
        System.out.println("| ID | Customer          | Employee             | Price      | Workstage    |");
        System.out.println("+----+-------------------+----------------------+------------+--------------+");
        for (Reservation reservation : reservationList) {
            System.out.printf("| %-2s | %-17s | %-20s | %-10.2f | %-12s |\n", reservation.getReservationId(), reservation.getCustomer().getName(), reservation.getEmployee().getName(), reservation.getReservationPrice(), reservation.getWorkstage());
        }
        System.out.println("+----+-------------------+----------------------+------------+--------------+");
    }

    public static void allHistoryReservation(List<Reservation> reservationList) {
        double totalProfit = 0.0;
        System.out.println("Reservation History:");
        for (Reservation reservation : reservationList) {
            if (reservation.getWorkstage().equalsIgnoreCase("Finish") || reservation.getWorkstage().equalsIgnoreCase("Canceled")) {
                System.out.println("ID: " + reservation.getReservationId() + ", Customer: " + reservation.getCustomer().getName() + ", Employee: " + reservation.getEmployee().getName() + ", Workstage: " + reservation.getWorkstage() + ", Price: " + reservation.getReservationPrice());
                totalProfit += reservation.getReservationPrice();
            }
        }
        System.out.println("Total Profit: " + totalProfit);
    }
}
