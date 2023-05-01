package v1.amachon.domain.tags.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import v1.amachon.domain.tags.entity.techtag.MemberTechTag;

public interface MemberTechTagRepository extends JpaRepository<MemberTechTag, Long> {
}
