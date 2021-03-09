package com.huijiewei.agile.core.constraint;

import com.huijiewei.agile.core.application.port.inbound.ExistsUseCase;
import com.huijiewei.agile.core.until.StringUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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

        var allowValueList = Arrays.asList(this.allowValues);

        var search = new ArrayList<String>();

        if (object instanceof Collection) {
            for (var item : (Collection<?>) object) {
                String str = item.toString();

                if (allowValueList.size() == 0 || !allowValueList.contains(str)) {
                    search.add(str);
                }
            }
        } else {
            var valueString = object.toString();

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
