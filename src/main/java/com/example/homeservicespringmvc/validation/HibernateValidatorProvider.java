package com.example.homeservicespringmvc.validation;

import com.example.homeservicespringmvc.entity.BaseEntity;
import com.example.homeservicespringmvc.exception.CustomizedHibernateValidatorProviderException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

public class HibernateValidatorProvider {

    private static final ValidatorFactory validatorFactory;

    static {
        validatorFactory = Validation.buildDefaultValidatorFactory();
    }

    public static void checkEntity(BaseEntity<Long> entity) {
        Set<ConstraintViolation<BaseEntity<Long>>> constraintViolations = validatorFactory
                .getValidator()
                .validate(entity);
        if (!constraintViolations.isEmpty())
            throw new CustomizedHibernateValidatorProviderException(constraintViolations.toString());
    }
}
