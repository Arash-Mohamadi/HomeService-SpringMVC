package com.example.homeservicespringmvc.entity.capability;

import com.example.homeservicespringmvc.entity.users.Specialist;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;


@Entity
@ToString
@Setter
@Getter
@NoArgsConstructor
public class Suggestion extends BaseAbility {
    @CreationTimestamp
    private LocalDateTime creationDate;
    @NotNull
    private LocalDateTime startWork;
    @NotNull
    private LocalDateTime endWork;
    @Positive(message = "price should positive")
    @NotNull
    private double price;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.MERGE)  // bidirectional
    private Specialist specialist;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.MERGE) // bidirectional
    private Order order;

    @Builder
    public Suggestion(LocalDateTime startWork, LocalDateTime endWork, double price) {
        this.startWork = startWork;
        this.endWork = endWork;
        this.price = price;
    }
}
