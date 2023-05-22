package mwe.validatorfail.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {})
@SupportedValidationTarget(ValidationTarget.ANNOTATED_ELEMENT)
@ReportAsSingleViolation
public @interface AnyUuid {

    String message() default "must be any variant of UUID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    AllowedRepresentation allowedRepresentation() default AllowedRepresentation.BOTH;

    enum AllowedRepresentation {
        LOWER_CASE(true, false, false),
        UPPER_CASE(false, true, false),
        BOTH(true, true, false),

        //not adding uppercase to simplify matching.
        IGNORE_CASE(true, false, true);

        private static final String UUID_FORMAT = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
        private final Pattern regexPattern;

        AllowedRepresentation(boolean lower, boolean upper, boolean ignoreCase) {
            regexPattern = createRegexPattern(lower, upper, ignoreCase);
        }

        public Pattern getRegexPattern() {
            return regexPattern;
        }

        private Pattern createRegexPattern(boolean lower, boolean upper, boolean ignoreCase) {
            int noFlags = 0;
            if (lower && upper) {
                return Pattern.compile(String.format("^%s|%s$", UUID_FORMAT, UUID_FORMAT.toUpperCase()), noFlags);
            }

            if (lower || ignoreCase) {
                int flags = ignoreCase ? Pattern.CASE_INSENSITIVE : noFlags;
                return Pattern.compile(String.format("^%s$", UUID_FORMAT), flags);
            }

            if (upper) {
                return Pattern.compile(String.format("^%s$", UUID_FORMAT.toUpperCase()), noFlags);
            }

            throw new IllegalStateException("coding error, unsupported combination.");
        }
    }
}
