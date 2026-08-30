package com.healthcaremanagement.profile.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdatedEvent implements Serializable {
    private String patientId;
    private String timestamp;
}