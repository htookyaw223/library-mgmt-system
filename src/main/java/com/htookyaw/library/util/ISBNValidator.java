package com.htookyaw.library.util;

public class ISBNValidator {

    public static boolean isValidISBN(String isbn) {
        isbn = isbn.replaceAll("-", "").toUpperCase();

        if (isbn.length() == 10) {
            return isValidISBN10(isbn);
        } else if (isbn.length() == 13) {
            return isValidISBN13(isbn);
        }
        return false;
    }

    private static boolean isValidISBN10(String isbn) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            if (!Character.isDigit(isbn.charAt(i))) return false;
            sum += (isbn.charAt(i) - '0') * (10 - i);
        }

        char last = isbn.charAt(9);
        sum += (last == 'X') ? 10 : (Character.isDigit(last) ? last - '0' : 0);

        return sum % 11 == 0;
    }

    private static boolean isValidISBN13(String isbn) {
        if (!isbn.matches("\\d{13}")) return false;

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = isbn.charAt(i) - '0';
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int check = (10 - (sum % 10)) % 10;
        return check == (isbn.charAt(12) - '0');
    }
}
