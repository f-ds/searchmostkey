package it.fds.searchmostkey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.stream.IntStream;

@Service
public class ScoreKeywordService {

    @Autowired
    AmazonClient amazonClient;

    /**
     * Assigns a score representing the search frequency on amazon to a given input keyword.
     *
     * The algorithm steps are the following:
     * <ul>
         * <li>invokes the Amazon autocomplete API on each substring with start index equals to 0</li>
         * <li>searches the input keyword in all of the returned suggestions set</li>
         * <li>calculate the index with the formula max(100 / substring[i].lenght)</li>
     * </ul>
     *
     * The calls to the Amazon autocomplete service happens in parallel,
     * the number of parallel executions depends on the JDK version and HW configuration
     *
     * @param keyword the input keyword on which to calculate the score
     * @return the score for the input keyword
     */
    public int scoreKeyword(@RequestParam String keyword){
        return IntStream.range(1, keyword.length()+1)
                .mapToObj(i -> keyword.substring(0,i))
                .parallel()
                .map(k -> isKeywordSuggested(k, keyword) ? 100/k.length() : 0)
                .max(Integer::compareTo).orElse(0);
    }

    /**
     * Check if a given keyword is present in the suggestion list of a given partial keyword
     *
     * @param partialKeyword a substring of a keyword with start index equals to 0
     * @param keyword the input keyword on which to calculate the score
     * @return true if present, else otherwise
     */
    private boolean isKeywordSuggested(String partialKeyword, String keyword){
        return amazonClient.getAutocompleteSuggestion(partialKeyword)
                .contains(keyword);
    }
}
