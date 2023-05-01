package v1.amachon.domain.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import v1.amachon.domain.project.entity.TeamMember;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
}
