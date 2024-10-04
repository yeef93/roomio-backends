package com.finpro.roomio_backends.image.entity;

import com.finpro.roomio_backends.users.entity.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "image_avatar")
public class ImageUserAvatar {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_id_gen")
  @SequenceGenerator(name = "image_id_gen", sequenceName = "image_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private Users user;

  @NotNull
  @Column(name = "image_name", nullable = false, length = Integer.MAX_VALUE)
  private String imageName;

  @NotNull
  @Column(name = "image_url", nullable = false, length = Integer.MAX_VALUE)
  private String imageUrl;
}