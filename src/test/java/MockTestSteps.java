package test.java;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;
import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MockTestSteps {

	private final WireMockServer wireMockServer = new WireMockServer(options().port(9999));

	private String content, response, fileJson = "./resources/__files/request_calculator.json";
	private JSONObject jsonRequest;

	
	public void setup() throws InterruptedException {

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
		wireMockServer.stubFor(post(urlPathEqualTo("/api/books")).withRequestBody(equalTo(content))
				.willReturn(aResponse().withStatus(200).withBody("61")));
		Thread.sleep(1000);
	}

	@Given("^a operacao a ser realizada$")
	public void a_operacao_a_ser_realizada(DataTable input) throws Throwable {
		setup();
		// get headers
		List<String> headers = input.getGherkinRows().get(0).getCells();
		jsonRequest = new JSONObject();

		for (int i = 1; i < input.getGherkinRows().size(); i++) {
			// values
			List<String> values = input.getGherkinRows().get(i).getCells();

			// add id_modelo
			jsonRequest.put(headers.get(0), values.get(0));

			// add variaveis
			JSONArray array = new JSONArray();
			array.put(new JSONObject().put(headers.get(1), values.get(1)));
			array.put(new JSONObject().put(headers.get(2), values.get(2)));
			jsonRequest.put("variaveis", array);

			jsonRequest.put("sistema_produto", "Plataforma de Taxas");
		}

	}

	@Then("^retornar erro caso as entradas estejam invalidas$")
	public void retornar_erro_caso_as_entradas_estejam_invalidas() throws Throwable {
		// do request and stop server
		try {
			Request.Post("http://localhost:9999/api/books")
					.bodyString(jsonRequest.toString(), ContentType.APPLICATION_JSON).execute().returnContent()
					.toString();
		} catch (Exception e) {
			assertEquals(HttpResponseException.class, e.getClass());
		}
		wireMockServer.stop();
	}

	@Then("^retornar o resultado$")
	public void retornar_o_resultado() throws Throwable {
		// do request and stop server
		response = Request.Post("http://localhost:9999/api/books")
				.bodyString(jsonRequest.toString(), ContentType.APPLICATION_JSON).execute().returnContent().toString();
		assertEquals(response, "61");
		wireMockServer.stop();
	}

}