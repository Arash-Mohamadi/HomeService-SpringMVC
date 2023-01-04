package com.example.homeservicespringmvc.entity.capability;

import com.example.homeservicespringmvc.entity.BaseEntity;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Setter
@Getter
public class BaseAbility extends BaseEntity<Long> {


}
