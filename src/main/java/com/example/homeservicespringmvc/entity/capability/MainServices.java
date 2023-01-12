package com.example.homeservicespringmvc.entity.capability;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.List;



@Entity
@ToString
@Setter
@Getter
@NoArgsConstructor
public class MainServices extends BaseAbility {

    @Column(unique = true)
    @NotBlank(message = "name of service should not contains null value or space . ")
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "services", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SubServices> subList;





    public void addSubServices(SubServices subServices) {
        this.getSubList().add(subServices);
        subServices.setServices(this);
    }

    @Builder
    public MainServices(String name) {
        this.name = name;
    }
}
