package cs601.project3;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import cs601.project3.http.HttpRequest;
import cs601.project3.utils.HttpUtils;

public class UnitTest {

	@Test
	public void testParsingRequestHeader() {
		HttpRequest request = new HttpRequest();

		try {
			File htmlRequest = new File("src/test/resources/right/postTest.txt");
			InputStream inputStream = new FileInputStream(htmlRequest);
			assertEquals(HttpUtils.handleRequestHeader(inputStream, request), true);

			htmlRequest = new File("src/test/resources/right/getTest.txt");
			inputStream = new FileInputStream(htmlRequest);
			assertEquals(HttpUtils.handleRequestHeader(inputStream, request), true);

			htmlRequest = new File("src/test/resources/wrong/postTest.txt");
			inputStream = new FileInputStream(htmlRequest);
			assertEquals(HttpUtils.handleRequestHeader(inputStream, request), false);


			htmlRequest = new File("src/test/resources/wrong/getTest.txt");
			inputStream = new FileInputStream(htmlRequest);
			assertEquals(HttpUtils.handleRequestHeader(inputStream, request), false);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRequestHeader() {
		HttpRequest request = new HttpRequest();

		try {
			File htmlRequest = new File("src/test/resources/right/postTest.txt");
			InputStream in = new FileInputStream(htmlRequest);
			HttpUtils.handleRequestHeader(in, request);
			assertEquals(request.getMethod(), "POST");
			assertEquals(request.getPath(), "/slackbot");
			assertEquals(request.getProtocol(), "HTTP/1.1");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRequestHeaderContent() {
		HttpRequest request = new HttpRequest();

		try {
			File htmlRequest = new File("src/test/resources/right/postTest.txt");
			InputStream in = new FileInputStream(htmlRequest);
			HttpUtils.handleRequestHeader(in, request);
			assertEquals(request.getHeaders().size(), 13);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetRequestBody() {
		HttpRequest request = new HttpRequest();

		try {
			Path htmlRequestBody = new File("src/test/resources/right/postBodyTest.txt").toPath();
			byte[] body = Files.readAllBytes(htmlRequestBody);
			String expected = new String(body);

			File htmlRequest = new File("src/test/resources/right/postTest.txt");
			InputStream in = new FileInputStream(htmlRequest);
			HttpUtils.handleRequestHeader(in, request);
			HttpUtils.handleRequestBody(in, request);
			String actual = request.getBody();
			assertEquals(actual, expected);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testParseQuery() {
		String query = "name=Hiep+Bui&age=23";
		Map<String, String> expected = new HashMap<String, String>();
		expected.put("name", "Hiep Bui");
		expected.put("age", "23");
		Map<String, String> actual = HttpUtils.parseQuery(query);
		boolean result = true;
		for(Entry<String, String> entry : expected.entrySet()) {
			if(actual.containsKey(entry.getKey())) {
				if(!entry.getValue().equals(actual.get(entry.getKey()))){
					result = false;
					break;
				}
			} else {
				result = false;
				break;
			}
		}
		assertEquals(result, true);
	}

}
