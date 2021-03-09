package com.huijiewei.agile.core.constraint;

import com.huijiewei.agile.core.application.port.inbound.UniqueUseCase;
import com.huijiewei.agile.core.until.StringUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;

/**
 * @author huijiewei
 */

public class UniqueValidator implements ConstraintValidator<Unique, Object> {
    private final ApplicationContext applicationContext;

    private UniqueUseCase uniqueUseCase;

    private String primaryKey;
    private String[] fields;
    private String message;

    @Autowired
    public UniqueValidator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize(Unique constraintAnnotation) {
        this.uniqueUseCase = this.applicationContext.getBean(constraintAnnotation.uniqueService());

        this.primaryKey = constraintAnnotation.primaryKey();
        this.fields = constraintAnnotation.fields();
        this.message = constraintAnnotation.message();
    }

    @SneakyThrows
    private String getObjectFieldValue(Object object, String field) {
        var clazz = object.getClass();

        var objectField = ReflectionUtils.findField(clazz, field);

        if (objectField == null) {
            return "";
        }

        objectField.setAccessible(true);

        var value = objectField.get(object);

        if (value == null) {
            return "";
        }

        return value.toString();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        var values = new HashMap<String, String>();

        for (var field : this.fields) {
            var value = this.getObjectFieldValue(object, field);

            if (StringUtils.isNotBlank(value)) {
                values.put(field, value);
            }
        }

        if (values.size() == 0) {
            return true;
        }

        var isValid = this.uniqueUseCase.unique(values, this.primaryKey, this.getObjectFieldValue(object, this.primaryKey));

        if (!isValid) {
            context.disableDefaultConstraintViolation();

            for (var field : fields) {
                context
                        .buildConstraintViolationWithTemplate(String.format(message, String.join(",", fields)))
                        .addPropertyNode(field)
                        .addConstraintViolation();
            }
        }

        return isValid;
    }
}
