package com.finpro.roomio_backends.properties.entity;

import com.finpro.roomio_backends.facilities.entity.Facilities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "property_facility")
public class PropertyFacility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "property_facility_id_gen")
    @SequenceGenerator(name = "property_facility_id_gen", sequenceName = "property_facility_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Properties properties;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facilities facilities;
}
