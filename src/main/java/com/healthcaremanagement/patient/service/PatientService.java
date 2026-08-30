package com.healthcaremanagement.patient.service;

public interface PatientService {
    void createPatientProfile(AuthEntity auth);
    PatientEntity getPatientByAuthId(String authId);
    void savePatient(PatientEntity patient);
}