package de.hiqs.daybird.api.shared.domains.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import de.hiqs.daybird.api.shared.domains.error.ErrorCodeEnum;
import de.hiqs.daybird.api.shared.domains.error.exception.DaybirdApiException;

import java.io.IOException;

public class JsonUtils {

    private final ObjectMapper objectMapper;

    public JsonUtils() {
        /*!!! Don't set an option for pretty json formating. It has to be written as one line. Makes it better for Splunk !!!*/
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setDateFormat(new StdDateFormat().withColonInTimeZone(true));
    }

    /**
     * Converts an Object to json String. Throws an exception if it couldn't serialize the object.
     *
     * @param object Object which should be converted to json.
     * @return json as String
     */
    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new DaybirdApiException(ErrorCodeEnum.JSON_SERIALIZATION_ERROR, e);
        }
    }

    /**
     * Converts a json string into an Object. Throws an exception if it couldn't deserialize the string.
     *
     * @param json      Json as String
     * @param typeClass The class to which we wan't to convert the object.
     * @param <T>       The typeClass defines also the return value.
     * @return The deserialized json as Object.
     */
    public <T> T toObject(String json, Class<T> typeClass) {
        try {
            return objectMapper.readValue(json, typeClass);
        } catch (IOException e) {
            throw new DaybirdApiException(ErrorCodeEnum.JSON_DESERIALIZATION_ERROR, e);
        }
    }


    /**
     * remove newlines, linebreaks, whitespaces.....
     *
     * @param json
     * @return string without removals
     */
    public String minify(String json) {
        return toObject(json, JsonNode.class).toString();
    }
}
