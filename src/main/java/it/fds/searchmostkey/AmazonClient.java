package it.fds.searchmostkey;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

/**
 *
 */
public abstract class AmazonClient {

    /**
     * Calls a URL with GET endpoint
     *
     * @param url
     * @return the response of the service as a String
     */
    ResponseEntity<String> get(String url){ ;
        return new RestTemplate().getForEntity(url, String.class);
    }

    /**
     * Invokes Amazon autocomplete API and return all the suggestions obtained
     *
     * @param partialKeyword a string representing a substring with start index equals to 0 of a target keyword
     * @return a set of suggestions
     */
    abstract Set<String> getAutocompleteSuggestion(String partialKeyword);
}
