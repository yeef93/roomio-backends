package com.finpro.roomio_backends.properties.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "room_peakrate")
public class RoomPeakRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "room_peakrate_id_gen")
    @SequenceGenerator(name = "room_peakrate_id_gen", sequenceName = "room_peakrate_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Rooms rooms ;

    @NotNull
    @Column(name = "date", nullable = false)
    private String date;


    @NotNull
    @Column(name = "rate_type", nullable = false)
    private String rateType;

    @NotNull
    @Column(name = "rate_value", nullable = false)
    private BigDecimal rateValue;

    @Column(name = "description")
    private String description;

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
}
