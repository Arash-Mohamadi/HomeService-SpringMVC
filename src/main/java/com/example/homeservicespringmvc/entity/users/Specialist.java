package com.example.homeservicespringmvc.entity.users;

import com.example.homeservicespringmvc.entity.capability.*;
import com.example.homeservicespringmvc.entity.enums.SpecialistStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
public class Specialist extends Person {

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer Avg;

    @Enumerated(EnumType.STRING)
    private SpecialistStatus status;

    private byte[] photo;

    @ToString.Exclude
    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}) // unidirectional
    private Credit credit;

    @ToString.Exclude
    @OneToMany(mappedBy = "specialist") // bidirectional
    private List<Opinion> opinions;

    @ToString.Exclude
    @OneToMany(mappedBy = "specialist") // bidirectional
    private List<Suggestion> suggestions;

    @ToString.Exclude
    @ManyToMany // bidirectional
    private Set<MainServices> servicesSet;

    @ManyToMany // bidirectional
    @ToString.Exclude
    private Set<SubServices> subServicesSet;

    @ToString.Exclude
    @OneToMany(mappedBy = "specialist") // bidirectional
    private List<Order> orders;

    public void addSubService(SubServices subServices) {
        this.getSubServicesSet().add(subServices);
        subServices.getSpecialistSet().add(this);
    }

    @Builder
    public Specialist(String firstname, String lastname, String email, String username, String password) {
        super(firstname, lastname, email, username, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Specialist that = (Specialist) o;
        return Objects.equals(Avg, that.Avg) && status == that.status &&
                Arrays.equals(photo, that.photo) && Objects.equals(credit, that.credit)
                && Objects.equals(opinions, that.opinions)
                && Objects.equals(suggestions, that.suggestions)
                && Objects.equals(servicesSet, that.servicesSet)
                && Objects.equals(subServicesSet, that.subServicesSet)
                && Objects.equals(orders, that.orders);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), Avg, status, credit, opinions,
                suggestions, servicesSet, subServicesSet, orders);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }
}
