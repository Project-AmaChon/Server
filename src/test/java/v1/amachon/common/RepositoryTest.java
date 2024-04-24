package v1.amachon.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import v1.amachon.common.config.JpaAuditingConfig;
import v1.amachon.common.config.querydsl.QuerydslConfig;
import v1.amachon.common.config.security.SecurityConfig;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest(includeFilters = @Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@Import({JpaAuditingConfig.class, QuerydslConfig.class, SecurityConfig.class})
public @interface RepositoryTest {
}