package v1.amachon.tags.entity.regiontag;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> getTagNamesWithChildrenTags() {
        List<String> tagNames = List.of(name);
        tagNames.addAll(children.stream().map(RegionTag::getName).collect(Collectors.toList()));
        return tagNames;
    }
}
