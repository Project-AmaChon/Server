package v1.amachon.domain.tags.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import v1.amachon.domain.tags.entity.techtag.TechTag;

public interface TechTagRepository extends JpaRepository<TechTag, Long> {
}
