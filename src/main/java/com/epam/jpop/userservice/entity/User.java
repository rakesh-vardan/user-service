package com.epam.jpop.userservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "user", schema = "users")
@SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_seq", allocationSize = 1, initialValue = 1)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_generator")
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(nullable = false)
    private String role;

    @Column
    private String email;

    @Column(name = "phone_no")
    private String phoneNumber;
}
