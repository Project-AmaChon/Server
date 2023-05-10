package v1.amachon.domain.tags.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import v1.amachon.domain.tags.entity.regiontag.RegionTag;
import v1.amachon.domain.tags.entity.techtag.TechTag;

import java.util.List;
import java.util.Optional;

public interface RegionTagRepository extends JpaRepository<RegionTag, Long> {
    List<RegionTag> findByDepth(int depth);
    Optional<RegionTag> findByName(String name);
}
