package com.example.homeservicespringmvc.entity.users;

import com.example.homeservicespringmvc.entity.BaseEntity;
import com.example.homeservicespringmvc.entity.enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.Objects;


@MappedSuperclass
@Data
@NoArgsConstructor
public class Person extends BaseEntity<Long> {

    @Column(nullable = false)
    @NotBlank(message = "firstname should not be empty or contains space")
    private String firstname;
    @Column(nullable = false)
    @NotBlank(message = "lastname should not be empty or contains space or null value")
    private String lastname;

    @Column(unique = true, nullable = false)
    @Email(message = "Email Pattern not valid , Please provide a valid email address")
    private String email;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "username should not be empty or contains space or null value")
    @Pattern(regexp = "^[A-Za-z0-9._]+$")
    private String username;
    @Column(nullable = false)
    @NotBlank(message = "username should not be empty or contains space or null value ")
    @Pattern(regexp = "^[A-Za-z0-9._$%^&*#!@\\-/\\\\]{8,}+$")
    private String password;

    @CreationTimestamp
    private LocalDateTime dateOfSignup;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Builder
    public Person(String firstname, String lastname, String email, String username, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Person person = (Person) o;
        return Objects.equals(firstname, person.firstname) && Objects.equals(lastname, person.lastname) && Objects.equals(email, person.email) && Objects.equals(username, person.username) && Objects.equals(password, person.password) && Objects.equals(dateOfSignup, person.dateOfSignup) && userType == person.userType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstname, lastname, email, username, password, dateOfSignup, userType);
    }
}
