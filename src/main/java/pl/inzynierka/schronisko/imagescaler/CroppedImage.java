package pl.inzynierka.schronisko.imagescaler;

import java.awt.image.BufferedImage;

public record CroppedImage(ResolutionEnum resolution, BufferedImage image) {}
