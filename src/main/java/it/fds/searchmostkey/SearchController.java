package it.fds.searchmostkey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("estimate")
public class SearchController {

  Log LOGGER = LogFactory.getLog(getClass());

  @Autowired ScoreKeywordService scoreKeywordService;

  @GetMapping
  public EstimationDTO scoreKeyword(@RequestParam String keyword) {
    try {
      return new EstimationDTO(keyword, scoreKeywordService.scoreKeyword(keyword));
    } catch (Throwable t) {
      LOGGER.error(t);
      throw new InternalError(
          "An error occurred while contacting services. Please wait few seconds and retry again.",
          t);
    }
  }
}
