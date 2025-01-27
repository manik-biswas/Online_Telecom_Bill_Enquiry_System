package com.otbs.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int planId;

    @NotBlank(message = "Plan name is mandatory")
    @Column(nullable = false, unique = true)
    private String planName;

    @Positive(message = "Fixed rate must be positive")
    @Column(nullable = false)
    private double fixedRate;

    @NotBlank(message = "Data limit is mandatory")
    @Column(nullable = false)
    private String dataLimit;

    @NotBlank(message = "Call limit is mandatory")
    @Column(nullable = false)
    private String callLimit;

    @NotBlank(message = "SMS limit is mandatory")
    @Column(nullable = false)
    private String smsLimit;

    @NotBlank(message = "Plan group is mandatory")
    @Column(nullable = false)
    private String planGroup;

    @Positive(message = "Number of days must be positive")
    @Column(nullable = false)
    private int numberOfDay;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Plan status is mandatory")
    @Column(nullable = false)
    private PlanStatus status;

    // Additional charge attributes (optional)
    @PositiveOrZero(message = "Additional charge per MB must be non-negative")
    private float extraChargePerMB;

    @PositiveOrZero(message = "Additional charge per call must be non-negative")
    private float extraChargePerCall;

    @PositiveOrZero(message = "Additional charge per SMS must be non-negative")
    private float extraChargePerSMS;
    
    
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Connection> connections;
    

    
    public enum PlanStatus {
        ACTIVE,
        INACTIVE
    }
    
}

