package com.example.homeservicespringmvc.validation;

import com.example.homeservicespringmvc.entity.BaseEntity;
import com.example.homeservicespringmvc.exception.CustomizedIllegalArgumentException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

public class HibernateValidatorProvider {

    private static final ValidatorFactory validatorFactory;

    static {
        validatorFactory = jakarta.validation.Validation.buildDefaultValidatorFactory();
    }

    public static ValidatorFactory getValidatorFactory() {
        return validatorFactory;
    }

    public static void checkEntity(BaseEntity<Long> entity) {
        Set<ConstraintViolation<BaseEntity<Long>>> constraintViolations = getValidatorFactory()
                .getValidator()
                .validate(entity);
        if (!constraintViolations.isEmpty())
            throw new CustomizedIllegalArgumentException(constraintViolations.toString());
    }
}
