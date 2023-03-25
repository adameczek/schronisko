package pl.inzynierka.schronisko.configurations.converters;

import org.modelmapper.AbstractConverter;
import pl.inzynierka.schronisko.animals.tags.AnimalTag;

import java.util.List;

public class TagListConverter extends AbstractConverter<List<AnimalTag>, List<String>> {
    @Override
    protected List<String> convert(List<AnimalTag> tags) {
        return tags.stream()
                .map(AnimalTag::getValue)
                .toList();
    }
}
