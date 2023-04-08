package edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.ArrayList;

public class HouseholdParking extends CalgaryProperty {

    private String residentLicence;
    private VisitorParking visitors;

    public HouseholdParking(int taxRollNumber, String zoning, String streetName, int buildingNumber, String postCode, String buildingAnnex) throws IllegalArgumentException {
        super(taxRollNumber, zoning, streetName, buildingNumber, postCode, buildingAnnex);
        this.visitors = new VisitorParking();
    }

    public HouseholdParking(int taxRollNumber, String zoning, String streetName, int buildingNumber, String postCode) throws IllegalArgumentException {
        super(taxRollNumber, zoning, streetName, buildingNumber, postCode);
        this.visitors = new VisitorParking();
    }

    public String getResidentLicence() {
        return residentLicence;
    }

    public void addOrReplaceResidentLicence(String licence) throws IllegalArgumentException {
        String standardizedLicence = Parking.standardizeAndValidateLicence(licence);
        this.residentLicence = standardizedLicence;
    }

    public void removeResidentLicence() {
        this.residentLicence = "";
    }

    public VisitorParking getVisitors() {
        return visitors;
    }

    public void addVisitorReservation(String licence) {
        visitors.addVisitorReservation(licence);
    }

    public void addVisitorReservation(String licence, LocalDate date) {
        visitors.addVisitorReservation(licence, date);
    }

    public boolean licenceIsRegisteredForDate(String licence) {
        return visitors.licenceIsRegisteredForDate(licence);
    }

    public boolean licenceIsRegisteredForDate(String licence, LocalDate date) {
        return visitors.licenceIsRegisteredForDate(licence, date);
    }

    public ArrayList<String> getLicencesRegisteredForDate() {
        return visitors.getLicencesRegisteredForDate();
    }

    public ArrayList<String> getLicencesRegisteredForDate(LocalDate date) {
        return visitors.getLicencesRegisteredForDate(date);
    }

    public ArrayList<LocalDate> getAllDaysLicenceIsRegistered(String licence) {
        return visitors.getAllDaysLicenceIsRegistered(licence);
    }

    public ArrayList<LocalDate> getStartDaysLicenceIsRegistered(String licence) {
        return visitors.getStartDaysLicenceIsRegistered(licence);
    }
}