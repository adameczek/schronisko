package pl.inzynierka.schronisko.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.modelmapper.ModelMapper;

public class RequestToObjectMapper {
    public static <T, D> D mapRequestToObjectForUpdate(JsonNode request,
                                                       D existingObject,
                                                       Class<T> requestClass,
                                                       Class<D> objectClass,
                                                       ModelMapper modelMapper,
                                                       String... skipAttribute) throws
            RequestToObjectMapperException {
        try {
            ObjectMapper ob = new ObjectMapper();
            ob.findAndRegisterModules();
            D copy = ob.readValue(ob.writeValueAsString(existingObject),
                                  objectClass);
            T objectMappedToRequest = modelMapper.map(existingObject,
                                                      requestClass);

            ObjectReader objectReader = ob.readerForUpdating(
                    objectMappedToRequest);

            for (String attribute : skipAttribute) {
                objectReader = objectReader.withoutAttribute(attribute);
            }

            T updatedRequest = objectReader.readValue(request, requestClass);

            modelMapper.map(updatedRequest, copy);

            return copy;
        } catch (Exception e) {
            throw new RequestToObjectMapperException(e);
        }
    }
}
