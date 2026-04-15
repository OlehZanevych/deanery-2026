package org.lnu.teaching.web.application.design.deanery.entity.user;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@DynamicUpdate
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String passwordHash;
    @Column(nullable = false)
    private boolean isAdmin;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String middleName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String email;
    private String info;
}
