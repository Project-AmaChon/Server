package v1.amachon.tags.entity.techtag;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import v1.amachon.tags.entity.regiontag.RegionTag;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> getTagNamesWithChildrenTags() {
        List<String> tagNames = List.of(name);
        tagNames.addAll(children.stream().map(TechTag::getName).collect(Collectors.toList()));
        return tagNames;
    }
}
