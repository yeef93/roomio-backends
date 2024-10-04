package com.finpro.roomio_backends.properties.entity;

import com.finpro.roomio_backends.categories.entity.Categories;
import com.finpro.roomio_backends.image.entity.ImageProperties;
import com.finpro.roomio_backends.users.entity.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "properties")
public class Properties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "properties_id_gen")
    @SequenceGenerator(name = "properties_id_gen", sequenceName = "properties_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Users user;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Categories categories;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "location", length = 500)
    private String location;

    @Column(name = "city", length = 100)
    private String city;

    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive = true;

    @ColumnDefault("false")
    @Column(name = "is_publish")
    private Boolean isPublish = false;

    @Column(name = "map", length = 400)
    private String map;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;


    @OneToMany(mappedBy = "properties", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImageProperties> images = new ArrayList<>(); // Initialize with an empty list


    @OneToMany(mappedBy = "properties", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rooms> rooms;

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
