package test.java;

import org.junit.runner.RunWith;
import cucumber.api.junit.Cucumber;
import cucumber.api.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "feature/pokemon.feature", 
glue = "", monochrome = true, dryRun = false)
public class Pokedex {
}


