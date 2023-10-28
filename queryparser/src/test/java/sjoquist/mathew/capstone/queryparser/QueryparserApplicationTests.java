package sjoquist.mathew.capstone.queryparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import sjoquist.mathew.capstone.queryparser.controllers.QueryController;

@SpringBootTest
class QueryparserApplicationTests {

	@SuppressWarnings("java:S2699")
	@Test
	void contextLoads() {
	}

	@Test
	void simpleQuery() {
		String query = "hello world";
		String expected = "/webpages/search?text=hello+world";
		String actual = QueryController.parse(query);
		assertEquals(expected, actual);
	}
}
