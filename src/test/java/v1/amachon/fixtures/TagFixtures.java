package v1.amachon.fixtures;

import v1.amachon.tags.entity.regiontag.RegionTag;
import v1.amachon.tags.entity.techtag.TechTag;

public class TagFixtures {
    public static final String backend = "Backend";
    public static final String spring = "Spring";
    public static final String nodejs = "NodeJS";
    public static final String django = "Django";

    public static final String frontend = "Frontend";
    public static final String react = "React";
    public static final String flutter = "Flutter";
    public static final String android = "Android";
    public static final String ios = "ios";

    public static final String seoul = "서울";
    public static final String gangnam = "강남구";

    public static final String gyeonggi = "경기";
    public static final String hwaseong = "화성시";

    public static TechTag backend() {
        return TechTag.builder().depth(0).name(backend).build();
    }

    public static TechTag spring(TechTag parent) {
        return TechTag.builder().depth(1).name(spring).parent(parent).build();
    }

    public static TechTag nodejs(TechTag parent) {
        return TechTag.builder().depth(1).name(nodejs).parent(parent).build();
    }

    public static TechTag django(TechTag parent) {
        return TechTag.builder().depth(1).name(django).parent(parent).build();
    }

    public static TechTag frontend() {
        return TechTag.builder().depth(0).name(frontend).build();
    }

    public static TechTag react(TechTag parent) {
        return TechTag.builder().depth(1).name(react).parent(parent).build();
    }

    public static TechTag flutter(TechTag parent) {
        return TechTag.builder().depth(1).name(flutter).parent(parent).build();
    }

    public static TechTag android(TechTag parent) {
        return TechTag.builder().depth(1).name(android).parent(parent).build();
    }

    public static TechTag ios(TechTag parent) {
        return TechTag.builder().depth(1).name(ios).parent(parent).build();
    }

    public static RegionTag gyeonggi() {
        return RegionTag.builder().depth(0).name(gyeonggi).build();
    }

    public static RegionTag hwaseong(RegionTag parent) {
        return RegionTag.builder().depth(1).name(hwaseong).parent(parent).build();
    }

    public static RegionTag seoul() {
        return RegionTag.builder().depth(0).name(seoul).build();
    }

    public static RegionTag gangnam(RegionTag parent) {
        return RegionTag.builder().depth(1).name(gangnam).parent(parent).build();
    }

}
