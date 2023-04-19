package pl.inzynierka.schronisko.imagescaler;

import org.imgscalr.Scalr;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageTools {
    public static BufferedImage resize(BufferedImage image, ResolutionEnum resolution) {
        return Scalr.resize(image, resolution.resolution.width(), resolution.resolution.height());
    }

    public static BufferedImage cropImageToNearestResolution(BufferedImage image) {
        final Resolution imageResolution = new Resolution(image.getWidth(), image.getHeight());
        final ResolutionEnum resolutionEnum = getNearestLowestResolution(imageResolution);

        Point croppedImageXYstart = getCroppedImageXYstart(imageResolution, resolutionEnum.resolution);

        BufferedImage subimage = image.getSubimage(croppedImageXYstart.x, croppedImageXYstart.y, resolutionEnum.resolution.width(), resolutionEnum.resolution.height());
        BufferedImage copy = new BufferedImage(subimage.getWidth(), subimage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = copy.createGraphics();
        g.drawImage(subimage, 0, 0, null);

        return copy;
    }

    public static Point getCroppedImageXYstart(Resolution imageResolution, Resolution desiredResolution) {
        if (imageResolution.height() + imageResolution.width() < desiredResolution.width() + desiredResolution.height())
            throw new ArithmeticException(String.format("Desired resolution: %s is bigger than provided resolution: %s", desiredResolution, imageResolution));

        int x = getMiddle(0, imageResolution.width()) - getMiddle(0, desiredResolution.width());
        int y = getMiddle(0, imageResolution.height()) - getMiddle(0, desiredResolution.height());

        return new Point(x, y);
    }

    public static int getMiddle(int x, int y) {
        return (int) Math.abs(Math.floor((double) (x + y) / 2));
    }

    public static ResolutionEnum getNearestLowestResolution(Resolution resolution) {
        ResolutionEnum nearest = ResolutionEnum.MINI;
        
        for (ResolutionEnum resolutionEnum : ResolutionEnum.fromHighestToLowest()) {
            Resolution resFromEnum = resolutionEnum.resolution;
            if (resFromEnum.compareTo(resolution) <= 0 && resFromEnum.compareTo(nearest.resolution) >= 0) {
                nearest = resolutionEnum;
            }
        }

        return nearest;
    }
    
}
