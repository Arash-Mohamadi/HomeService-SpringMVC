package com.example.homeservicespringmvc.entity.capability;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Credit extends BaseAbility {
    @ColumnDefault("0")
    private double balance;

}
