package test.java;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.github.tomakehurst.wiremock.WireMockServer;

import cucumber.api.junit.Cucumber;
import cucumber.api.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "feature/mock.feature", glue = "", monochrome = true, dryRun = false)
public class MockTest {

	private static WireMockServer wireMockServer = new WireMockServer(options().port(9999));
	private static String content, fileJson = "./resources/__files/request_calculator.json";

	@BeforeClass
	public static void setUp() {

		// read requesting json
		try {
			content = new String(Files.readAllBytes(Paths.get(fileJson)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// start mock at :9999
		wireMockServer.start();
		configureFor("localhost", 9999);
		wireMockServer.stubFor(post(urlPathEqualTo("/calculadora/calcular")).withRequestBody(equalTo(content))
				.willReturn(aResponse().withStatus(200).withBody("61")));
	}

	@AfterClass
	public static void tearDown() {
		wireMockServer.stop();
	}
}
