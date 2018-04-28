package app.unitsdata.wellknown;

import app.unitsdata.ItemsCRUDController;
import app.unitsdata.model.School;
import app.unitsdata.repository.SchoolRepository;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author igogo
 */
@RestController
@RequestMapping("/public/")
public class UnitsInfoController {

    private final Logger logger = LoggerFactory.getLogger(UnitsInfoController.class);

    @Autowired
    SchoolRepository repository;

    @RequestMapping(value = "/school/{keywords}", method = RequestMethod.GET)
//    public List<School> getEduUnits(@PathVariable("keywords") String keywords,
//            @RequestParam(defaultValue = "none") String stage,
//            @RequestParam(defaultValue = "none") String county) throws IOException {
    public List<School> getEduUnits(@PathVariable("keywords") String keywords) throws IOException {
        logger.info("get edu unit name");

        List<School> schools;
        schools = repository.findByKeywords(keywords);
        return schools;

    }

}
