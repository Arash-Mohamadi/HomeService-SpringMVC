package com.example.homeservicespringmvc.entity.capability;

import com.example.homeservicespringmvc.entity.enums.TransactionStatus;
import com.example.homeservicespringmvc.entity.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Transaction extends BaseAbility {
    @CreationTimestamp
    private LocalDateTime creationDate;
    @Positive(message = "amount should positive")
    @NotNull(message = "amount should specified")
    private double amount;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @ManyToOne // unidirectional
    private Credit toCredit;
    @ManyToOne  // unidirectional
    private Credit fromCredit;
    @OneToOne // unidirectional
    private Order order;

    @Builder
    public Transaction(double amount,
                       TransactionType type,
                       TransactionStatus status,
                       Credit toCredit,
                       Credit fromCredit,
                       Order order) {
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.toCredit = toCredit;
        this.fromCredit = fromCredit;
        this.order = order;
    }
}
