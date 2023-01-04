package com.example.homeservicespringmvc.entity.users;

import com.example.homeservicespringmvc.entity.capability.Credit;
import com.example.homeservicespringmvc.entity.capability.Opinion;
import com.example.homeservicespringmvc.entity.capability.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
public class Customer extends Person{

    @ToString.Exclude
    @OneToOne(cascade = {CascadeType.REMOVE,CascadeType.PERSIST}) // unidirectional
    private Credit credit;

    @ToString.Exclude
    @OneToMany(mappedBy = "customer",cascade = CascadeType.REMOVE) // bidirectional
    private List<Opinion> opinions;

    @ToString.Exclude
    @OneToMany(mappedBy = "customer",cascade = CascadeType.REMOVE) // bidirectional
    private List<Order> orders;

    @Builder
    public Customer(String firstname, String lastname, String email, String username, String password) {
        super(firstname, lastname, email, username, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(credit, customer.credit) && Objects.equals(opinions, customer.opinions) && Objects.equals(orders, customer.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), credit, opinions, orders);
    }
}
