package v1.amachon.tags.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import v1.amachon.tags.entity.regiontag.RegionTag;

import java.util.List;
import java.util.Optional;

public interface RegionTagRepository extends JpaRepository<RegionTag, Long> {
    List<RegionTag> findByDepth(int depth);
    Optional<RegionTag> findByName(String name);
}
