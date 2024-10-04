package com.finpro.roomio_backends.properties.entity;

import com.finpro.roomio_backends.image.entity.ImageRoom;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rooms")
public class Rooms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "rooms_id_gen")
    @SequenceGenerator(name = "rooms_id_gen", sequenceName = "rooms_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Properties properties ;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @NotNull
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @NotNull
    @Column(name = "size")
    private Integer size;

    @Column(name = "bed_type", length = 100)
    private String bedType;

    @NotNull
    @Column(name = "total_bed")
    private Integer totalBed;

    @NotNull
    @Column(name = "total_bathroom")
    private Integer totalBathroom;

    @NotNull
    @Column(name = "qty")
    private Integer qty;

    @NotNull
    @Column(name = "base_price")
    private BigDecimal basePrice;

    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive = true;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "deleted_at")
    private Instant deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    @OneToMany(mappedBy = "rooms", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImageRoom> images = new ArrayList<>(); // Initialize with an empty list

    @OneToMany(mappedBy = "rooms", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoomPeakRate> peakRates;

    public BigDecimal getActualPrice() {
        if (peakRates == null || peakRates.isEmpty()) {
            return basePrice; // No peak rates, so the actual price is the base price
        }

        // Get the maximum peak rate based on the rate type
        Optional<RoomPeakRate> maxPeakRate = peakRates.stream()
                .max((r1, r2) -> r1.getRateValue().compareTo(r2.getRateValue()));

        if (maxPeakRate.isEmpty()) {
            return basePrice;
        }

        RoomPeakRate peakRate = maxPeakRate.get();
        BigDecimal peakRateValue = peakRate.getRateValue();
        String rateType = peakRate.getRateType();

        if ("percentage".equalsIgnoreCase(rateType)) {
            // Apply the peak rate as a percentage
            BigDecimal percentage = peakRateValue.divide(BigDecimal.valueOf(100));
            return basePrice.add(basePrice.multiply(percentage));
        } else if ("nominal".equalsIgnoreCase(rateType)) {
            // Apply the peak rate as a nominal value
            return basePrice.add(peakRateValue);
        } else {
            // Default case if the rate type is unknown or not handled
            return basePrice;
        }
    }
}
