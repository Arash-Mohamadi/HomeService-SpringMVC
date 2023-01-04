package com.example.homeservicespringmvc.entity.capability;

import com.example.homeservicespringmvc.entity.users.Customer;
import com.example.homeservicespringmvc.entity.users.Specialist;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
public class Opinion extends BaseAbility {
    @Column(nullable = false)
    @Positive(message = "score should positive")
    @Max(value = 5, message = "only 1 to 5 is allowed")
    @NotNull
    private Integer score;
    private String comment;

    @ToString.Exclude
    @ManyToOne // bidirectional
    private Customer customer;

    @ToString.Exclude
    @ManyToOne // bidirectional
    private Specialist specialist;

    @ToString.Exclude
    @OneToOne(mappedBy = "opinion")
    private Order order;


    @Builder
    public Opinion(Integer score, String comment) {
        this.score = score;
        this.comment = comment;
    }
}
