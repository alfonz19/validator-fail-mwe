package mwe.validatorfail.config;

import mwe.validatorfail.validation.AnyUuid;

import javax.validation.ConstraintValidator;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.ServiceLoader;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.cfg.GenericConstraintDef;
import org.hibernate.validator.cfg.defs.NotNullDef;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;


@Configuration
public class Config {

    @Bean
    public static LocalValidatorFactoryBean defaultValidator(ApplicationContext applicationContext) {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean() {
            @Override
            protected void postProcessConfiguration(javax.validation.Configuration<?> configuration) {
                super.postProcessConfiguration(configuration);

                if (configuration instanceof HibernateValidatorConfiguration hvc) {
                    ConstraintMapping constraintMapping = hvc.createConstraintMapping();

                    ServiceLoader.load(ConstraintValidator.class).forEach(cv->{
                        Arrays.stream(cv.getClass().getGenericInterfaces())
                              .filter(e -> e instanceof ParameterizedType)
                              .map(e -> (ParameterizedType) e)
                              .filter(e -> e.getRawType().equals(ConstraintValidator.class))
                              .map(ParameterizedType::getActualTypeArguments)
                              .forEach(aa -> {
                                  Type annotationType = aa[0];
                                  Class annotationClass = (Class<?>) annotationType;
                                  Class constraintValidatorClass = cv.getClass();

                                  System.out.println(String.format("registering %s", constraintValidatorClass));
                                    registerConstraint(constraintMapping, annotationClass, constraintValidatorClass);
                                  System.out.println(String.format("%s registered", constraintValidatorClass));


                    });

                    });


                    registerValidationsDefinitions(constraintMapping);

                    hvc.addMapping(constraintMapping);
                } else {
                    throw new IllegalStateException("Should not happen, we explicitly depend on HibernateValidator ValidationProvider");
                }
            }
        };
        factoryBean.setProviderClass(HibernateValidator.class);
        MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory(applicationContext);
        factoryBean.setMessageInterpolator(interpolatorFactory.getObject());
        return factoryBean;
    }

    private static void registerValidationsDefinitions(ConstraintMapping constraintMapping) {
        constraintMapping.type(mwe.validatorfail.dto.SampleDTO.class )
                         .getter( "uuid" )
                         .constraint( new GenericConstraintDef<>(AnyUuid.class) )
                         .constraint( new NotNullDef() );
    }

    public static <A extends Annotation> void registerConstraint(ConstraintMapping constraintMapping, Class<A> annotation,
                  Class<? extends ConstraintValidator<A, ?>> validator) {
        constraintMapping.constraintDefinition(annotation)
                         .includeExistingValidators(false)
                         .validatedBy(validator);
    }

}
