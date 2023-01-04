package com.example.homeservicespringmvc.entity.capability;

import com.example.homeservicespringmvc.entity.users.Specialist;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Duration;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startWork;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endWork;

    @Transient
    private Duration duration;
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
    public Suggestion(LocalDateTime startWork, LocalDateTime endWork, Duration duration, double price) {
        this.startWork = startWork;
        this.endWork = endWork;
        this.duration = duration;
        this.price = price;
    }
}
