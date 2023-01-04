package com.example.homeservicespringmvc.entity.capability;

import com.example.homeservicespringmvc.entity.enums.OrderStatus;
import com.example.homeservicespringmvc.entity.users.Customer;
import com.example.homeservicespringmvc.entity.users.Specialist;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order extends BaseAbility {
    @CreationTimestamp
    private LocalDateTime creationDate;
    @Positive(message = "price should be positive")
    @NotNull(message = "price should filled")
    private double price;
    @NotNull(message = "startWork should specified")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startWork;

    @NotBlank(message = "please write a correct description ")
    @Column(columnDefinition = "text")
    private String description;

    @NotBlank(message = "please write a correct address ")
    @Column(columnDefinition = "text")
    private String address;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ToString.Exclude
    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<Suggestion> suggestions;


    @Column(name = "selected_suggestion_id")
    private Long selectedSuggestionID;

    @ToString.Exclude
    @ManyToOne // bidirectional
    private Customer customer;

    @ToString.Exclude
    @ManyToOne // bidirectional
    private SubServices subServices;

    @ToString.Exclude
    @OneToOne // bidirectional
    private Opinion opinion;

    @ManyToOne(cascade = CascadeType.MERGE) // bidirectional
    @ToString.Exclude
    private Specialist specialist;

    public void addSuggestion(Suggestion suggestion) {
        this.getSuggestions().add(suggestion);
        suggestion.setOrder(this);
    }

    public void setOrderWithOpinion(Opinion opinion) {
        this.setOpinion(opinion);
        opinion.setOrder(this);
    }

    @Builder
    public Order(double price, LocalDateTime startWork, String description, String address) {
        this.price = price;
        this.startWork = startWork;
        this.description = description;
        this.address = address;
    }
}
