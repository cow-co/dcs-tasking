package cowco.tasking.taskingbackend.db;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import java.util.Arrays;

/**
 * Can't store a Set directly into the DB, so we convert it to a single String
 * prior to persisting.
 */
@Converter
public class StringSetConverter implements AttributeConverter<Set<String>, String> {
    private static final String DELIMITER = "¬"; // TODO Use a character that is forbidden in DCS usernames

    @Override
    public String convertToDatabaseColumn(Set<String> stringSet) {
        return (stringSet != null && !stringSet.isEmpty()) ? String.join(DELIMITER, stringSet) : "";
    }

    @Override
    public Set<String> convertToEntityAttribute(String string) {
        return (string != null && !string.isBlank()) ? new HashSet<>(Arrays.asList(string.split(DELIMITER)))
                : new HashSet<>();
    }
}
