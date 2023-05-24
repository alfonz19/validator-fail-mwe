package mwe.validatorfail.config;

import mwe.validatorfail.validation.AnyUuid;

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

                    constraintMapping.type( mwe.validatorfail.dto.SampleDTO.class ) // instead of an XML
                                     .getter( "uuid" )
                                     .constraint( new GenericConstraintDef<>(AnyUuid.class) )
                                     .constraint( new NotNullDef() );

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

}
