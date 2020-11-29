# searchmostkey
Guess the hottest amazon searched keywords reverse engineering its autocomplete API

## build & run
The project requires Maven 3 for the build and java > 8 for run.

Assuming both are installed and configured and the project cloned in folder `/home/code/searchmostkey`
```
$/home/code/searchmostkey# mvn clean install 
$/home/code/searchmostkey# java -jar target/searchmostkey-0.0.1-SNAPSHOT.jar
```
Then open your browser and type the URL `http://localhost:8080/estimate?keyword=christmas tree` and you will get the estimation for the keyword `christmas tree`
```
{
    keyword: "christmas tree",
    score: 100
}
```

## How does it work
Amazon do not provide any information on the search-volume of keywords but we can reverse-engineer and estimate it using the Amazon search box autocomplete service.

We assume that the autocomplete service returns the 10 most frequent searches for a given prefix.
This means that a keyword found when searching for a prefix with length *n*  is more frequent than one appearing only when the prefix with length *n+1*.

This approach works good to compare words which shares common prefixes but it does not tell us much when comparing words with no shared prefix.

The score goes from 0 to 100 assigning 100 to keywords which are found with a prefix with length 1, 50 with length 2 and so on until 0.    

### Order of results
The order of the suggestions seems to be not significant to identify the relevance of the keywords within that set.
The order seems consistent across different calls, but it seems rather set by other criteria.

For example, during end of november 2020, the search by prefix `c` return the keyword `cerave moisturizing cream` in the results.
But searching by prefix `ce` the first result in the list is actually `cerave` only, which was not present in the previous suggestions.

* `https://completion.amazon.com/api/2017/suggestions?mid=ATVPDKIKX0DER&alias=aps&prefix=c`
* `https://completion.amazon.com/api/2017/suggestions?mid=ATVPDKIKX0DER&alias=aps&prefix=ce`

This would lead to the assumption that the keyword `cerave moisturizing cream` has a higher search frequency than `cerave` because the latter was neither appearing in the first search, but amazon decided to give the priority to `cerave` when the prefix is `ce`.

### Amazon endpoint used

2 different endpoints have been found for autocomplete
* `https://completion.amazon.com/search/complete`
* `https://completion.amazon.com/api/2017/suggestions`

The first return the JSON output in a simple positional array format, the latter in a more structured way.

The results are similar but not the same, for example comparing the result for the prefix `c`

* `https://completion.amazon.com/search/complete?search-alias=aps&client=amazon-search-ui&mkt=1&q=c`
* `https://completion.amazon.com/api/2017/suggestions?mid=ATVPDKIKX0DER&alias=aps&prefix=c`

the keyword `christmas tree`, during end of November 2020, is not returned as suggestion with the first endpoint, but it is in the second.

This project provides implementation for both, given the fact that amazon.com seems using the second, that is the default one. 
