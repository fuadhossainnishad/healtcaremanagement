package com.healthcaremanagement.patient.service;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public void createPatientProfile(AuthEntity auth) {
        PatientEntity patient = PatientEntity.builder()
                .auth(auth)
                .build();
        patientRepository.save(patient);
    }

    @Override
    public PatientEntity getPatientByAuthId(String authId) {
        return patientRepository.findByAuthId(authId)
                .orElseThrow(() -> new BusinessException("Patient profile not found"));
    }

    @Override
    @Transactional
    public void savePatient(PatientEntity patient) {
        patientRepository.save(patient);
    }
}