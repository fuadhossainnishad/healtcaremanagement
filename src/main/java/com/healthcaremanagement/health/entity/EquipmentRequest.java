package com.healthcaremanagement.health.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "equipment_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String patientId;
    private String equipmentName;      // e.g., "Digital blood pressure machine"
    private String description;
    @Enumerated(EnumType.STRING)
    private RequestType type;          // ORDER, BORROW
    private LocalDateTime requestedAt;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;      // PENDING, APPROVED, REJECTED, DELIVERED

    public enum RequestType { ORDER, BORROW }
    public enum RequestStatus { PENDING, APPROVED, REJECTED, DELIVERED }
}