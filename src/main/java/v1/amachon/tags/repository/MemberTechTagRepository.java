package v1.amachon.tags.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import v1.amachon.tags.entity.techtag.MemberTechTag;

public interface MemberTechTagRepository extends JpaRepository<MemberTechTag, Long> {
}
