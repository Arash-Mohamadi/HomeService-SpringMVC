package com.example.homeservicespringmvc.entity.capability;

import com.example.homeservicespringmvc.entity.users.Customer;
import com.example.homeservicespringmvc.entity.users.Manager;
import com.example.homeservicespringmvc.entity.users.Specialist;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String tokenValue;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmationAt;

    @ManyToOne
    @JoinColumn
    private Customer customer;


    @ManyToOne
    @JoinColumn
    private Specialist specialist;

    @ManyToOne
    @JoinColumn
    private Manager manager;

    public Token(String tokenValue,
                 LocalDateTime createdAt,
                 LocalDateTime expiresAt,
                 Customer customer,
                 Specialist specialist,
                 Manager manager) {
        this.tokenValue = tokenValue;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.customer = customer;
        this.specialist = specialist;
        this.manager = manager;
    }
}
