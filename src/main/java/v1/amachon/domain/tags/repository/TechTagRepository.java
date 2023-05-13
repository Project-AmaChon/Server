package v1.amachon.domain.tags.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import v1.amachon.domain.tags.entity.techtag.TechTag;

import java.util.List;
import java.util.Optional;

public interface TechTagRepository extends JpaRepository<TechTag, Long> {
    List<TechTag> findByDepth(int depth);
    Optional<TechTag> findByName(String name);
}
