package com.booking.service;

import java.util.regex.Pattern;

public class ValidationService {
    public static boolean validateInput(String input) {
        // Validasi input tidak boleh kosong
        return !input.isEmpty();
    }

    public static boolean validateCustomerId(String customerId) {
        // Validasi customer ID harus diawali dengan "Cust-" dan diikuti oleh dua digit angka
        return Pattern.matches("^Cust-\\d{2}$", customerId);
    }
    }    
