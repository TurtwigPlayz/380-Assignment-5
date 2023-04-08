package edu.ucalgary.oop;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class VisitorParking {

    private HashMap<String, TreeSet<LocalDate>> parkingRecord;
    private HashMap<LocalDate, Integer> reservationCountPerDate = new HashMap<>();
    private HashMap<String, TreeSet<LocalDate>> completeDates = new HashMap<>();


    public VisitorParking() {
        this.parkingRecord = new HashMap<>();
    }
    
    public VisitorParking(String license, LocalDate date) throws IllegalArgumentException {
        this();
        addVisitorReservation(license, date);
       
    }
    
    public VisitorParking(String license) throws IllegalArgumentException {
        this();
        String standardizedLicense = Parking.standardizeAndValidateLicence(license);
        this.addVisitorReservation(standardizedLicense);
    }
    public HashMap<String, TreeSet<LocalDate>> getParkingRecord() {
        return this.parkingRecord;
    }
    public void addVisitorReservation(String license) throws IllegalArgumentException {
        LocalDate currentDate = LocalDate.now();
        try {
            addVisitorReservation(license, currentDate);
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }
    
    public void addVisitorReservation(String license, LocalDate date) throws IllegalArgumentException {
        String standardizedLicense = Parking.standardizeAndValidateLicence(license);
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot reserve a parking spot for a date in the past.");
        }

        if (licenceIsRegisteredForDate(standardizedLicense, date)) {
            throw new IllegalArgumentException("License plate is already registered for the requested date.");
        }

        if (reservationCountPerDate.getOrDefault(date, 0) >= 2) {
            throw new IllegalArgumentException("Cannot have more than 2 reservations on the same date.");
        }

        if (completeDates.containsKey(standardizedLicense)) {
            TreeSet<LocalDate> reservationDates = completeDates.get(standardizedLicense);

            int reservationCount = 0;
            for (int i = -2; i <= 2; i++) {
                if (reservationDates.contains(date.plusDays(i))) {
                    reservationCount++;
                }
            }

            if (reservationCount >= 3 && !reservationDates.last().plusDays(1).equals(date)) {
                throw new IllegalArgumentException("Cannot have more than 2 reservations within a 3-day window.");
            }

            reservationDates.add(date.plusDays(2));
            reservationDates.add(date.plusDays(1));
            reservationDates.add(date);
        } else {
            TreeSet<LocalDate> reservationDates = new TreeSet<>(Collections.reverseOrder());
            reservationDates.add(date.plusDays(2));
            reservationDates.add(date.plusDays(1));
            reservationDates.add(date);
            completeDates.put(standardizedLicense, reservationDates);
        }

        for (int i = 0; i < 3; i++) {
            LocalDate currentDate = date.plusDays(i);
            reservationCountPerDate.put(currentDate, reservationCountPerDate.getOrDefault(currentDate, 0) + 1);
        }

        parkingRecord.putIfAbsent(standardizedLicense, new TreeSet<>(Collections.reverseOrder()));
        parkingRecord.get(standardizedLicense).add(date);
    }

    public boolean licenceIsRegisteredForDate(String license) {
        return licenceIsRegisteredForDate(license, LocalDate.now());
    }

    public boolean licenceIsRegisteredForDate(String license, LocalDate date) {
        String standardizedLicense = Parking.standardizeAndValidateLicence(license);
        TreeSet<LocalDate> dates = this.completeDates.get(standardizedLicense);
        if (dates == null) {
            return false;
        }
        return dates.contains(date);
    }
    public ArrayList<String> getLicencesRegisteredForDate() {
        LocalDate today = LocalDate.now();
        ArrayList<String> result = new ArrayList<>();
        for (String license : this.completeDates.keySet()) {
            TreeSet<LocalDate> dates = this.completeDates.get(license);
            if (dates.contains(today)) {
                result.add(license);
            }
        }
        return result;
    }
    public ArrayList<String> getLicencesRegisteredForDate(LocalDate date) {
        ArrayList<String> licences = new ArrayList<String>();
        for (String license : this.completeDates.keySet()) {
            TreeSet<LocalDate> dates = this.completeDates.get(license);
            if (dates.contains(date)) {
                licences.add(license);
            }
        }
        return licences;
    }
    public ArrayList<LocalDate> getAllDaysLicenceIsRegistered(String license) {
        String standardizedLicense = Parking.standardizeAndValidateLicence(license);
        TreeSet<LocalDate> dates = this.completeDates.get(standardizedLicense);
        if (dates == null) {
            return new ArrayList<LocalDate>();
        }
        ArrayList<LocalDate> result = new ArrayList<LocalDate>(dates);
        Collections.sort(result);
        return result;
    }

    public ArrayList<LocalDate> getStartDaysLicenceIsRegistered(String license) {
        ArrayList<LocalDate> startDates = new ArrayList<LocalDate>();

        String standardizedLicense = Parking.standardizeAndValidateLicence(license);
        TreeSet<LocalDate> reservationStartDates = this.parkingRecord.get(standardizedLicense);

        if (reservationStartDates != null) {
            startDates.addAll(reservationStartDates);

            LocalDate currentDate = LocalDate.now();
            Collections.sort(startDates, new Comparator<LocalDate>() {
                public int compare(LocalDate d1, LocalDate d2) {
                    return Math.abs((int) ChronoUnit.DAYS.between(d1, currentDate))
                            - Math.abs((int) ChronoUnit.DAYS.between(d2, currentDate));
                }
            });
        }
        return startDates;
    }
}