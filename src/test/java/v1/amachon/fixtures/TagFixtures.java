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

    public static final String seoul = "서울";
    public static final String gangnam_gu = "강남구";

    public static final String gyeonggi = "경기";
    public static final String hwaseong_si = "화성시";

    public static TechTag backend() {
        return TechTag.builder().depth(0).name(backend).build();
    }

    public static TechTag frontend() {
        return TechTag.builder().depth(0).name(frontend).build();
    }

    public static RegionTag seoul() {
        return RegionTag.builder().depth(0).name(seoul).build();
    }

    public static RegionTag gyeonggi() {
        return RegionTag.builder().depth(0).name(gyeonggi).build();
    }

}
