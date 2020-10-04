package com.huijiewei.agile.core.constraint;

import com.huijiewei.agile.core.application.port.inbound.ExistsUseCase;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.*;

/**
 * @author huijiewei
 */
public class ExistsValidator implements ConstraintValidator<Exists, Object> {
    @Autowired
    private ApplicationContext applicationContext;

    private ExistsUseCase existsUseCase;

    private String targetProperty;
    private String[] allowValues;

    @Override
    public void initialize(Exists annotation) {
        this.existsUseCase = this.applicationContext.getBean(annotation.existService());
        this.targetProperty = annotation.targetProperty();
        this.allowValues = annotation.allowValues();
    }

    @SneakyThrows
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        if (object == null) {
            return true;
        }

        List<String> allowValueList = Arrays.asList(this.allowValues);

        List<String> search = new ArrayList<>();

        if (object instanceof Collection) {
            for (Object item : (Collection<?>) object) {
                String str = item.toString();

                if (allowValueList.size() == 0 || !allowValueList.contains(str)) {
                    search.add(str);
                }
            }
        } else {
            String valueString = object.toString();

            if (this.allowValues.length > 0 && Arrays.asList(this.allowValues).contains(valueString)) {
                return true;
            }

            if (StringUtils.isEmpty(valueString)) {
                return true;
            }

            search.add(valueString);
        }

        if (search.size() == 0) {
            return true;
        }

        return this.existsUseCase.exists(targetProperty, search);
    }
}
