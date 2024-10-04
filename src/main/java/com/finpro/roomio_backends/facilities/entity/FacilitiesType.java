package com.finpro.roomio_backends.facilities.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "facilities_type")
public class FacilitiesType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "facilities_type_id_gen")
    @SequenceGenerator(name = "facilities_type_id_gen", sequenceName = "facilities_type_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

}
