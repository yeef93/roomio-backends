package com.finpro.roomio_backends.bookings.entity;

import com.finpro.roomio_backends.properties.entity.Rooms;
import com.finpro.roomio_backends.users.entity.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookings")
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "bookings_id_gen")
    @SequenceGenerator(name = "bookings_id_gen", sequenceName = "bookings_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Rooms room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "check_in_date")
    private Instant checkInDate;

    @Column(name = "check_out_date")
    private Instant checkOutDate;

    @NotNull
    @Column(name = "number_of_people")
    private Integer numberOfPeople;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "payment_proof")
    private String paymentProof;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "qty")
    private Integer qty;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "priceexcludefees")
    private Integer priceexcludefees;

    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "payment_deadline")
    private Instant paymentDeadline;

    @Column(name = "snap_token")
    private String snapToken;
}
