package it.fds.searchmostkey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This invokes autocomplete services from the endpoint https://completion.amazon.com/search/complete
 */
public class AmazonClientComplete extends AmazonClient{

    private static Log LOGGER = LogFactory.getLog(AmazonClientComplete.class);

    @Value("${it.fds.searchmostkey.amazon.api.complete}")
    String urlTemplate;

    @Autowired
    ObjectMapper mapper;

    @Override
    public Set<String> getAutocompleteSuggestion(String partialKeyword) {
        String searchurl = String.format(urlTemplate, partialKeyword);
        ResponseEntity<String> response = get(searchurl);

        Set<String> output = new HashSet<>();
        try {
            JsonNode suggestions = mapper.readTree(response.getBody()).get(1);
            for(Iterator<JsonNode> suggIter = suggestions.iterator(); suggIter.hasNext();){
                output.add(suggIter.next().asText());
            }
        } catch (JsonProcessingException e) {
            LOGGER.error(e);
            throw new InternalError("An error occurred while parsing Amazon API response", e);
        }
        return output;
    }
}
