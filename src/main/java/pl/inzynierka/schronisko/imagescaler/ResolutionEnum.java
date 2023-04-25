package pl.inzynierka.schronisko.imagescaler;

import java.util.*;

public enum ResolutionEnum {

    HIGH(1170,2532),
    MEDIUM(390, 844),
    MINI(130, 282);

    public final Resolution resolution;

    ResolutionEnum(int width, int height) {
       this.resolution = new Resolution(width, height);
    }

    public static List<ResolutionEnum> fromHighestToLowest() {
        List<ResolutionEnum> list = Arrays.asList(ResolutionEnum.values());

        list.sort(new ResolutionEnumComparator());

        return list;
    }

    private static class ResolutionEnumComparator implements Comparator<ResolutionEnum> {
        @Override
        public int compare(ResolutionEnum o1, ResolutionEnum o2) {
            return o1.compareTo(o2);
        }
    }
}
