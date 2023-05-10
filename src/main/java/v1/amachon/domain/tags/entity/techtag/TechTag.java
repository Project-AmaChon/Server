package v1.amachon.domain.tags.entity.techtag;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechTag {

    @Id
    @GeneratedValue
    @Column(name = "tech_tag_id")
    private Long id;

    private String name;

    private int depth;

    @ManyToOne(fetch = FetchType.LAZY)
    private TechTag parent;

    @OneToMany(mappedBy = "parent")
    private List<TechTag> children = new ArrayList<>();

    @Builder
    public TechTag(String name, int depth, TechTag parent) {
        this.name = name;
        this.depth = depth;
        if (parent != null) {
            parent.children.add(this);
        }
        this.parent = parent;
        this.children = new ArrayList<>();
    }
}
