package v1.amachon.domain.tags.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import v1.amachon.domain.tags.entity.regiontag.RegionTag;

public interface RegionTagRepository extends JpaRepository<RegionTag, Long> {
}
