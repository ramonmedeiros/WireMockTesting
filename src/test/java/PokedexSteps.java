package test.java;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PokedexSteps {

	private String url = "https://pokeapi.co/api/v2/";
	private final WireMockServer wireMockServer = new WireMockServer(options().dynamicPort());
	private String content, responsePokemon, fileJson = "./resources/__files/pokemon.json";

	public PokedexSteps() {

		// read requesting json
		try {
			content = new String(Files.readAllBytes(Paths.get(fileJson)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Given("^i have a running API server$")
	public void i_have_a_running_API_server() throws Throwable {
		String response = Request.Get(url).execute().returnContent().toString();
		JSONObject respJson = new JSONObject(response);
		assertEquals(respJson.get("pokemon"), "https://pokeapi.co/api/v2/pokemon/");

	}

	@When("^i request /pokemon$")
	public void i_request_pokemon() throws Throwable {
		responsePokemon = Request.Get(url + "pokemon/").execute().returnContent().toString();
		assertEquals(new JSONObject(responsePokemon).toString(), new JSONObject(content).toString());
	}

	@Then("^bulbasaur will be the first$")
	public void bulbasaur_will_be_the_first() throws Throwable {
		JSONArray respJson = (JSONArray) new JSONObject(responsePokemon).get("results");
		assertEquals(((JSONObject) respJson.get(0)).get("name"), "bulbasaur");
		
	}

	@Given("^i have a running mock server$")
	public void i_have_a_running_mock_server() throws Throwable {
		// start mock at :9999
		wireMockServer.start();
		configureFor("localhost", wireMockServer.port());
		wireMockServer.stubFor(get(urlPathEqualTo("/api/v2/pokemon/"))
				.willReturn(aResponse().withStatus(200).withBody(content)));
		this.url = "http://localhost:" + wireMockServer.port() + "/api/v2/";
	}
}
