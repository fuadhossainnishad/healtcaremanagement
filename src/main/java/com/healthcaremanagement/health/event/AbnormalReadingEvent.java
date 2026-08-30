package com.healthcaremanagement.health.event;

package com.healthcaremanagement.health.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbnormalReadingEvent implements Serializable {
    private String patientId;
    private String conditionType;
    private String readingValue;
    private LocalDateTime recordedAt;
    // constructor from entity
    public AbnormalReadingEvent(HealthReading reading) {
        this.patientId = reading.getCondition().getPatientId();
        this.conditionType = reading.getCondition().getType().name();
        this.readingValue = reading.getDisplayValue(); // helper method
        this.recordedAt = reading.getRecordedAt();
    }
}