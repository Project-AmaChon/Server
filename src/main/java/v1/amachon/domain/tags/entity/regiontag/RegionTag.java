package v1.amachon.domain.tags.entity.regiontag;

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
public class RegionTag {

    @Id @GeneratedValue
    @Column(name = "region_tag_id")
    private Long id;

    private String name;

    private int depth;

    @ManyToOne(fetch = FetchType.LAZY)
    private RegionTag parent;

    @OneToMany(mappedBy = "parent")
    private List<RegionTag> children = new ArrayList<>();

    @Builder
    public RegionTag(String name, int depth, RegionTag parent) {
        this.name = name;
        this.depth = depth;
        if (parent != null) {
            parent.children.add(this);
        }
        this.parent = parent;
    }
}
