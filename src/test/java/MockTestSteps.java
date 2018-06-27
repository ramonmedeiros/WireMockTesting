package test.java;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MockTestSteps {

	private String response;

	private JSONObject jsonRequest;

	@Given("^a operacao a ser realizada$")
	public void a_operacao_a_ser_realizada(DataTable input) throws Throwable {
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
			Request.Post("http://localhost:9999/calculadora/calcular")
					.bodyString(jsonRequest.toString(), ContentType.APPLICATION_JSON).execute().returnContent()
					.toString();
		} catch (Exception e) {
			assertEquals(HttpResponseException.class, e.getClass());
		}
	}

	@Then("^retornar o resultado$")
	public void retornar_o_resultado() throws Throwable {
		// do request and stop server
		response = Request.Post("http://localhost:9999/calculadora/calcular")
				.bodyString(jsonRequest.toString(), ContentType.APPLICATION_JSON).execute().returnContent().toString();
		assertEquals(response, "61");
	}

}
