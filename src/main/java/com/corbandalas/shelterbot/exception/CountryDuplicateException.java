package com.corbandalas.shelterbot.exception;

public class CountryDuplicateException extends RuntimeException{

    public CountryDuplicateException(String name) {
        super("Country '" + name + "' already exists.");
    }
}