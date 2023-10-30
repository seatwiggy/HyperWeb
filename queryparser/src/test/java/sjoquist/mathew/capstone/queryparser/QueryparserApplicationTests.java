package sjoquist.mathew.capstone.queryparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;

import sjoquist.mathew.capstone.queryparser.controllers.QueryController;

@SpringBootTest
class QueryparserApplicationTests {
	@SuppressWarnings("java:S2699")
	@Test
	void contextLoads() {
	}

	@ParameterizedTest
	@CsvSource({
			"hello world,/webpages/search?text=hello+world,",
			"hello world url:google.com url:duck.com,/webpages/search?text=hello+world&url=google.com+dock.com,",
			"hello world -goodbye -later,/webpages/search?text=hello+world&exclude=goodbye+later,"
	})
	void testQueryParsing(String query, String expected) {
		String actual = QueryController.parse(query);
		assertEquals(expected, actual);
	}
}
