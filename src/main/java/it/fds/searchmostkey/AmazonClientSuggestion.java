package it.fds.searchmostkey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This invokes autocomplete services from the endpoint https://completion.amazon.com/api/2017/suggestions
 */
public class AmazonClientSuggestion extends AmazonClient {

    private static Log LOGGER = LogFactory.getLog(AmazonClientSuggestion.class);

    @Value("${it.fds.searchmostkey.amazon.api.suggestions}")
    String urlTemplate;

    @Override
    public Set<String> getAutocompleteSuggestion(String partialKeyword) {
        String searchurl = String.format(urlTemplate, partialKeyword);
        ResponseEntity<String> response = get(searchurl);
        Set set = new HashSet();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode suggestions = root.path("suggestions");
            for(Iterator<JsonNode> suggIter = suggestions.iterator(); suggIter.hasNext();){
                set.add(suggIter.next().get("value").asText());
            }
        } catch (JsonProcessingException e) {
            LOGGER.error(e);
            throw new InternalError("An error occurred while parsing Amazon API response", e);
        }
        return set;
    }
}
