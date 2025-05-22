package com.atbuys.runmawi;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneNumberValidator {

    public static boolean isValidPhoneNumber(String countryCode, String phoneNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phoneNumber, countryCode);
            return phoneNumberUtil.isValidNumber(parsedNumber);


        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());

            return false;
        }
    }
}

