package pl.inzynierka.schronisko.imagescaler;

import org.jetbrains.annotations.NotNull;

public record Resolution (int width, int height) implements Comparable<Resolution> {
    @Override
    public int compareTo(@NotNull Resolution o) {
        if (this.width < o.width && this.height < o.height)
            return -1;
        if (this.width > o.width || this.height > o.height)
            return 1;
        else return 0;
    }
}
