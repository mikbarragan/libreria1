package es.santander.adn360.core.web;

import es.santander.adn360.core.model.exception.FunctionalException;
import es.santander.adn360.core.util.CustomerProductQueryParams;
import es.santander.adn360.core.util.ExceptionEnum;
import es.santander.adn360.core.util.PaginatedResponse;
import es.santander.adn360.core.util.ProductQueryParams;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * The TestController Class
 */
@RestController
@RequestMapping("/test")
@Validated
public class TestController {

    private static final String TEST_REGEX = "^(F|J)[0-9]{9}$";
    private static final String HEADER_SANTANDER_CHANNEL = "X-Santander-Channel";

    @RequestMapping(value = "/commonException", method = RequestMethod.GET)
    public String testingException() throws Exception {
        throw new Exception("Testing common exception");
    }

    @RequestMapping(value = "/statusContract", method = RequestMethod.GET)
    public String checkSatusContract(
            @Validated
            final CustomerProductQueryParams customerProductQueryParams ) throws Exception {
        return customerProductQueryParams.getSituations_indicator();
    }

    @RequestMapping(value = "/missingParametersException", method = RequestMethod.GET)
    public String testingMissingParametersExceptions(@Valid
                                                     @RequestParam("strTest") String strTest) {
        return strTest;
    }

    @RequestMapping(value = "/constraintViolationException", method = RequestMethod.GET)
    public String testingConstraintViolationExceptions(@Valid @Pattern(regexp = TEST_REGEX)
                                                       @RequestParam("strTest") String strTest) {
        return strTest;
    }

    @RequestMapping(value = "/invalidBindException", method = RequestMethod.GET)
    public String invalidBindingException(@Valid @RequestParam("strTest") String strTest) {
        return strTest;
    }

    @RequestMapping(value = "/functionalNoDataFoundException", method = RequestMethod.GET)
    public String testingFunctionalExceptions() throws FunctionalException {
        throw new FunctionalException(ExceptionEnum.NO_DATA_FOUND, "Testing functional exception");
    }

    @RequestMapping(value = "/coordinates", method = RequestMethod.GET)
    public ProductQueryParams testingChannelHeader(@Validated ProductQueryParams coordinates) {
        return coordinates;
    }

    @RequestMapping(value = "/pagination", method = RequestMethod.GET)
    public TestPaginatedResponse testPagination() {
        return new TestPaginatedResponse();
    }

    @RequestMapping(value = "/paginationEvaluateNullHeaders", method = RequestMethod.GET)
    public TestPaginatedResponse testPaginationEvaluateNullHeaders() {
        return new TestPaginatedResponse();
    }

    @RequestMapping(value = "/paginationEvaluateExampleHeader", method = RequestMethod.GET)
    public TestPaginatedResponse testPaginationEvaluateExampleHeader(HttpServletResponse response) {
        response.addHeader("Example-Header", "value");
        return new TestPaginatedResponse();
    }

    @RequestMapping(value = "/paginationEvaluateInvalidLinkHeader", method = RequestMethod.GET)
    public TestPaginatedResponse testPaginationEvaluateInvalidLinkHeader(HttpServletResponse response) {
        response.addHeader("Link", "</test/paginationEvaluateValidLinkHeader?_limit=3&_offset=septimo>;other=\"self\"");
        return new TestPaginatedResponse();
    }

    @RequestMapping(value = "/paginationEvaluateValidLinkHeader", method = RequestMethod.GET)
    public TestPaginatedResponse testPaginationEvaluateValidLinkHeader(HttpServletResponse response) {
        response.addHeader("Link", "</test/paginationEvaluateValidLinkHeader?_limit=3&_offset=septimo>; rel=\"next\"");
        return new TestPaginatedResponse();
    }

    @Data
    public static class TestPaginatedResponse implements PaginatedResponse<String, String> {
        private List<String> list = Arrays.asList("primero", "segundo", "tercero", "cuarto", "quinto", "sexto", "septimo");

        @Override
        public List<String> getPaginatedField() {
            return this.list;
        }

        @Override
        public void setPaginatedField(List<String> list) {
            this.list = list;
        }

        @Override
        public Long getOffsetById(String id) {
            OptionalInt value = IntStream.range(0, list.size())
                    .filter(i -> id.equalsIgnoreCase(list.get(i)))
                    .findFirst();

            if (!value.isPresent())
                throw new FunctionalException(ExceptionEnum.INVALID_INPUT_PARAMETERS,
                        "Pagination: Get index by 'Offset' parameter no data found.");

            return Long.valueOf(value.getAsInt());
        }

        @Override
        public String getIdByOffset(Long offset) {
            try {
                return list.get(offset.intValue());
            } catch (Exception e) {
                throw new FunctionalException(ExceptionEnum.INTERNAL_SERVER_ERROR,
                        "Pagination: Get id by 'Offset' index throws error.");
            }
        }

        @Override
        public boolean isValidOffset(String id) {
            return id != null && !"".equalsIgnoreCase(id) && id.length() < 8;
        }
    }
}
