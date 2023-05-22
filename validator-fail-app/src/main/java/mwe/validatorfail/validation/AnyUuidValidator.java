package mwe.validatorfail.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;
import java.util.regex.Pattern;

public class AnyUuidValidator implements ConstraintValidator<AnyUuid, Object> {

    private Pattern regexPattern;

    @Override
    public void initialize(AnyUuid constraint) {
        regexPattern = constraint.allowedRepresentation().getRegexPattern();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value instanceof Optional<?> opt) {
            if (opt.isPresent()) {
                value = opt.get();
            } else {
                return true;
            }
        }

        if (!(value instanceof String)) {
            return false;
        }

        return regexPattern.matcher((String)value).matches();
    }
}
