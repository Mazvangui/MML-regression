/*
 * generated by Xtext 2.19.0
 */
package org.xtext.example.mydsl.tests

import com.google.common.io.Files
import com.google.inject.Inject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import org.eclipse.xtext.testing.InjectWith
import org.eclipse.xtext.testing.extensions.InjectionExtension
import org.eclipse.xtext.testing.util.ParseHelper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.^extension.ExtendWith
import org.xtext.example.mydsl.mml.CSVParsingConfiguration
import org.xtext.example.mydsl.mml.DataInput
import org.xtext.example.mydsl.mml.FrameworkLang
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm
import org.xtext.example.mydsl.mml.MMLModel

@ExtendWith(InjectionExtension)
@InjectWith(MmlInjectorProvider)
class test {
	@Inject
	ParseHelper<MMLModel> parseHelper

	@Test
	def void loadModel() {
		val MMLModel result = parseHelper.parse('''
			datainput "foo.csv"
				mlframework scikit-learn
				algorithm DT
				TrainingTest { 
					percentageTraining 70
				}
				mean_absolute_error
		''')
		Assertions.assertNotNull(result)
		val errors = result.eResource.errors
		Assertions.assertTrue(errors.isEmpty, '''Unexpected errors: «errors.join(", ")»''')

		Assertions.assertEquals("foo.csv", result.input.filelocation)
	}

	@Test
	def void compileDataInput() {
		val MMLModel result = parseHelper.parse('''
			datainput "boston.csv" separator ;
				mlframework scikit-learn
				algorithm DT
				TrainingTest { 
					percentageTraining 70
				}
				mean_absolute_error
		''')
		val DataInput dataInput = result.input;
		val String fileLocation = dataInput.filelocation;

		val String pythonImport = "import pandas as pd\n";
		val String DEFAULT_COLUMN_SEPARATOR = ","; // by default
		var String csv_separator = DEFAULT_COLUMN_SEPARATOR;
		val CSVParsingConfiguration parsingInstruction = dataInput.parsingInstruction;
		if (parsingInstruction !== null) {
			System.err.println("parsing instruction..." + parsingInstruction);
			csv_separator = parsingInstruction.getSep().toString();
		}
		val String csvReading = "mml_data = pd.read_csv(" + mkValueInSingleQuote(fileLocation) + ", sep=" +
			mkValueInSingleQuote(csv_separator) + ")";
		var String pandasCode = pythonImport + csvReading;
		//val MLChoiceAlgorithm algo =result.algorithms.get(0);
		//algorithmTreatment(algo);
		pandasCode += "\nprint (mml_data)\n";

		Files.write(pandasCode.getBytes(), new File("mml.py"));
	// end of Python generation
	/*
	 * Calling generated Python script (basic solution through systems call)
	 * we assume that "python" is in the path
	 */
	  val Process p = Runtime.getRuntime().exec("python mml.py");
	  val BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
	  var String line;
	  while ((line = in.readLine()) !== null) {
	  	System.out.println(line);
	 }
	}
	
	def String algorithmTreatment(MLChoiceAlgorithm algo){
		val FrameworkLang framework = algo.framework
		println(framework.value);
		""
	}

	def String mkValueInSingleQuote(String str) {
		return "'" + str + "'";
	}
}
