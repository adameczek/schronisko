package pl.inzynierka.schronisko.imagescaler;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.*;
import java.awt.image.BufferedImage;


class ImageToolsTest {
    
    @ParameterizedTest
    @CsvSource({"3000, 3000, HIGH", "390, 844, MEDIUM", "500, 500, MINI", "1000,3000,MEDIUM", "3000, 1000, MEDIUM"})
    void getNearestLowestShouldCropTo(int x, int y, String desiredRes) {
        Resolution resolution = new Resolution(x, y);
        
        Assertions.assertThat(ImageTools.getNearestLowestResolution(resolution)).isEqualTo(ResolutionEnum.valueOf(
                desiredRes));
    }
    
    @ParameterizedTest
    @CsvSource({"0, 6, 3", "0, 5, 2", "6, 0, 3", "4, 9, 6"})
    void shouldReturnMiddle(int x, int y, int expectedValue) {
        Assertions.assertThat(ImageTools.getMiddle(x, y)).isEqualTo(expectedValue);
    }
    
    @ParameterizedTest
    @CsvSource({"1920,1080,1000,1000,460,40", "10,10, 5,5, 3, 3"})
    void shouldCorrectlyCropToMiddle(
            int sourceX, int sourceY, int desiredX, int desiredY, int expectedX, int expectedY) {
        Resolution source  = new Resolution(sourceX, sourceY);
        Resolution desired = new Resolution(desiredX, desiredY);
        
        Assertions.assertThat(ImageTools.getCroppedImageXYstart(source, desired)).isEqualTo(new Point(expectedX,
                                                                                                      expectedY));
    }
    
    @Test
    void shouldThrowExceptionOnTooBigDesiredResolution() {
        Resolution source  = new Resolution(1, 1);
        Resolution desired = new Resolution(2, 2);
        
        Assertions.assertThatThrownBy(() -> ImageTools.getCroppedImageXYstart(source, desired)).isInstanceOf(
                ArithmeticException.class).hasMessageContaining("Desired resolution: ");
    }
    
    @Test
    void shouldCropImageToHighResolution() {
        BufferedImage bufferedImage = new BufferedImage(3000, 3000, BufferedImage.TYPE_INT_RGB);
        
        CroppedImage result = ImageTools.cropImageToNearestResolution(bufferedImage);
        Condition<BufferedImage>
                croppedToHighResolution
                = new Condition<>(image -> new Resolution(image.getWidth(), image.getHeight()).equals(
                ResolutionEnum.HIGH.resolution), "high resolution");
        
        Assertions.assertThat(result.image()).is(croppedToHighResolution);
    }
    
    @Test
    void shouldBeCroppedFromCenter() {
        BufferedImage bufferedImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 500, 500);
        g.setColor(Color.black);
        g.fillRect(185, 109, ResolutionEnum.MINI.resolution.width(), ResolutionEnum.MINI.resolution.height());
        
        BufferedImage result = ImageTools.cropImageToNearestResolution(bufferedImage).image();
        
        Condition<BufferedImage> allBlack = new Condition<>(image -> {
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    int rgbColor = image.getRGB(x, y);
                    if (rgbColor != Color.BLACK.getRGB()) {
                        return false;
                    }
                }
            }
            
            return true;
        }, "all black");
        
        Assertions.assertThat(result).is(allBlack);
    }
}