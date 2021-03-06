package org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf;

import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.xtext.example.mydsl.mml.AllVariables;
import org.xtext.example.mydsl.mml.CrossValidation;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.DataInput;
import org.xtext.example.mydsl.mml.FormulaItem;
import org.xtext.example.mydsl.mml.FrameworkLang;
import org.xtext.example.mydsl.mml.GTB;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MMLModel;
import org.xtext.example.mydsl.mml.PredictorVariables;
import org.xtext.example.mydsl.mml.RFormula;
import org.xtext.example.mydsl.mml.RandomForest;
import org.xtext.example.mydsl.mml.SGD;
import org.xtext.example.mydsl.mml.SVR;
import org.xtext.example.mydsl.mml.StratificationMethod;
import org.xtext.example.mydsl.mml.TrainingTest;
import org.xtext.example.mydsl.mml.Validation;
import org.xtext.example.mydsl.mml.ValidationMetric;
import org.xtext.example.mydsl.mml.XFormula;
import org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf.Output;

@SuppressWarnings("all")
public class MmlCompilateurScikitLearn {
  private MMLModel mmlModel;
  
  private MLAlgorithm mlAlgorithm;
  
  private final String name = "MmlCompilateurScikitLearn";
  
  private MmlCompilateurScikitLearn() {
  }
  
  public MmlCompilateurScikitLearn(final MMLModel mmlModel, final MLAlgorithm mlAlgorithm) {
    if ((mmlModel == null)) {
      throw new IllegalArgumentException((("you should initialize " + this.name) + "with non null value"));
    }
    this.mmlModel = mmlModel;
    this.mlAlgorithm = mlAlgorithm;
  }
  
  public String compileDataInput() {
    try {
      final DataInput dataInput = this.mmlModel.getInput();
      final String fileLocation = dataInput.getFilelocation();
      int crossValidationNumber = 0;
      String algorithmImport = "";
      String algorithmBody = "";
      final Validation validation = this.mmlModel.getValidation();
      final StratificationMethod stratification = validation.getStratification();
      final List<ValidationMetric> metrics = validation.getMetric();
      double percentageTest = 0.3;
      boolean _matched = false;
      if (stratification instanceof CrossValidation) {
        _matched=true;
        final CrossValidation crossValidation = ((CrossValidation) stratification);
        crossValidationNumber = crossValidation.getNumber();
        String _algorithmImport = algorithmImport;
        algorithmImport = (_algorithmImport + "\nfrom sklearn.model_selection import cross_val_score");
      }
      if (!_matched) {
        if (stratification instanceof TrainingTest) {
          _matched=true;
          final TrainingTest trainingTest = ((TrainingTest) stratification);
          int _number = trainingTest.getNumber();
          final double percentageTraining = (((double) _number) / 100.0);
          percentageTest = (1.0 - percentageTraining);
        }
      }
      final MLAlgorithm mlAlgorithm = this.mlAlgorithm;
      boolean _matched_1 = false;
      if (mlAlgorithm instanceof DT) {
        _matched_1=true;
        String _algorithmImport = algorithmImport;
        algorithmImport = (_algorithmImport + "\nfrom sklearn import tree");
        String _algorithmBody = algorithmBody;
        algorithmBody = (_algorithmBody + "\nclf = tree.DecisionTreeRegressor()");
        String _algorithmBody_1 = algorithmBody;
        algorithmBody = (_algorithmBody_1 + "\nclf.fit(X_train, y_train)");
        if ((crossValidationNumber == 0)) {
          String _algorithmBody_2 = algorithmBody;
          algorithmBody = (_algorithmBody_2 + "\ny_pred =  clf.predict(X_test)");
        }
      }
      if (!_matched_1) {
        if (mlAlgorithm instanceof RandomForest) {
          _matched_1=true;
          String _algorithmImport = algorithmImport;
          algorithmImport = (_algorithmImport + "\nimport numpy as np");
          String _algorithmImport_1 = algorithmImport;
          algorithmImport = (_algorithmImport_1 + "\nfrom sklearn.ensemble import RandomForestRegressor");
          String _algorithmBody = algorithmBody;
          algorithmBody = (_algorithmBody + "\nclf = RandomForestRegressor()");
          String _algorithmBody_1 = algorithmBody;
          algorithmBody = (_algorithmBody_1 + "\nclf.fit(X_train, y_train)");
          if ((crossValidationNumber == 0)) {
            String _algorithmBody_2 = algorithmBody;
            algorithmBody = (_algorithmBody_2 + "\ny_pred = clf.predict(X_test)");
          }
        }
      }
      if (!_matched_1) {
        if (mlAlgorithm instanceof SVR) {
          _matched_1=true;
          String _algorithmImport = algorithmImport;
          algorithmImport = (_algorithmImport + "\nimport numpy as np");
          String _algorithmImport_1 = algorithmImport;
          algorithmImport = (_algorithmImport_1 + "\nfrom sklearn.svm import SVR");
          String _algorithmBody = algorithmBody;
          algorithmBody = (_algorithmBody + "\nclf = SVR()");
          String _algorithmBody_1 = algorithmBody;
          algorithmBody = (_algorithmBody_1 + "\nclf.fit(X_train, y_train)");
          if ((crossValidationNumber == 0)) {
            String _algorithmBody_2 = algorithmBody;
            algorithmBody = (_algorithmBody_2 + "\ny_pred = clf.predict(X_test)");
          }
        }
      }
      if (!_matched_1) {
        if (mlAlgorithm instanceof SGD) {
          _matched_1=true;
          InputOutput.<String>println("SGD");
        }
      }
      if (!_matched_1) {
        if (mlAlgorithm instanceof GTB) {
          _matched_1=true;
          InputOutput.<String>println("GTB");
        }
      }
      if (!_matched_1) {
        InputOutput.<String>print("default");
      }
      final String trainning = "train_test_split";
      String pythonImport = "import pandas as pd\n";
      String _pythonImport = pythonImport;
      pythonImport = (_pythonImport + (algorithmImport + "\n"));
      for (final ValidationMetric validationMetric : metrics) {
        String _pythonImport_1 = pythonImport;
        String _string = validationMetric.getLiteral().toString();
        String _plus = ("from sklearn.metrics import " + _string);
        String _plus_1 = (_plus + "\n");
        pythonImport = (_pythonImport_1 + _plus_1);
      }
      String _pythonImport_2 = pythonImport;
      pythonImport = (_pythonImport_2 + (("from sklearn.model_selection import " + trainning) + "\n"));
      final String csvReading = (("\nmml_data = pd.read_csv(\"" + fileLocation) + "\")");
      String pandasCode = (pythonImport + csvReading);
      final RFormula formula = this.mmlModel.getFormula();
      if ((formula == null)) {
        String column = "\ncolumn = mml_data.columns[-1]";
        String _pandasCode = pandasCode;
        pandasCode = (_pandasCode + (("\n" + column) + " \nX = mml_data.drop(columns=[column]) "));
        String _pandasCode_1 = pandasCode;
        pandasCode = (_pandasCode_1 + "\ny = mml_data[column] ");
      } else {
        int predictiveColumn = 0;
        String predictiveColName = null;
        FormulaItem _predictive = formula.getPredictive();
        boolean _tripleNotEquals = (_predictive != null);
        if (_tripleNotEquals) {
          final FormulaItem predictive = formula.getPredictive();
          int _column = predictive.getColumn();
          boolean _tripleNotEquals_1 = (_column != 0);
          if (_tripleNotEquals_1) {
            predictiveColumn = predictive.getColumn();
            String _pandasCode_2 = pandasCode;
            pandasCode = (_pandasCode_2 + (("y = mml_data.iloc[:, " + Integer.valueOf(predictiveColumn)) + "].values"));
          } else {
            String _colName = predictive.getColName();
            boolean _tripleNotEquals_2 = (_colName != null);
            if (_tripleNotEquals_2) {
              predictiveColName = formula.getPredictive().getColName();
              String _pandasCode_3 = pandasCode;
              pandasCode = (_pandasCode_3 + (("\ny = mml_data[\'" + predictiveColName) + "\'] "));
            }
          }
        } else {
          String column_1 = "\ncolumn = mml_data.columns[-1]";
          String _pandasCode_4 = pandasCode;
          pandasCode = (_pandasCode_4 + ("\n" + column_1));
          String _pandasCode_5 = pandasCode;
          pandasCode = (_pandasCode_5 + "\ny = mml_data[column] ");
        }
        final XFormula xformula = formula.getPredictors();
        boolean _matched_2 = false;
        if (xformula instanceof AllVariables) {
          _matched_2=true;
          if ((predictiveColumn != 0)) {
            String _pandasCode_6 = pandasCode;
            pandasCode = (_pandasCode_6 + (("X = mml_data.iloc[:, 0:" + Integer.valueOf(predictiveColumn)) + "].values"));
          } else {
            if ((predictiveColName != null)) {
              String _pandasCode_7 = pandasCode;
              pandasCode = (_pandasCode_7 + (("\ncolumn = \"" + predictiveColName) + "\""));
              String _pandasCode_8 = pandasCode;
              pandasCode = (_pandasCode_8 + "\nX = mml_data.drop(columns=[column]) ");
            } else {
              String column_2 = "\ncolumn = mml_data.columns[-1]";
              String _pandasCode_9 = pandasCode;
              pandasCode = (_pandasCode_9 + (("\n" + column_2) + " \nX = mml_data.drop(columns=[column]) "));
            }
          }
        }
        if (!_matched_2) {
          if (xformula instanceof PredictorVariables) {
            _matched_2=true;
            XFormula _predictors = formula.getPredictors();
            PredictorVariables predictorsVariables = ((PredictorVariables) _predictors);
            final List<FormulaItem> predictorsList = predictorsVariables.getVars();
            String column_2 = "\ncolumn = mml_data.columns[-1]";
            String _pandasCode_6 = pandasCode;
            pandasCode = (_pandasCode_6 + (("\n" + column_2) + " \nX = mml_data.drop(columns=[column]) "));
          }
        }
        if (!_matched_2) {
          InputOutput.<String>print("default");
        }
      }
      String _pandasCode_6 = pandasCode;
      pandasCode = (_pandasCode_6 + ("\ntest_size = " + Double.valueOf(percentageTest)));
      String _pandasCode_7 = pandasCode;
      pandasCode = (_pandasCode_7 + "\nX_train, X_test, y_train, y_test = train_test_split(X, y, test_size=test_size) ");
      String _pandasCode_8 = pandasCode;
      pandasCode = (_pandasCode_8 + ("\n" + algorithmBody));
      for (final ValidationMetric validationMetric_1 : metrics) {
        if ((crossValidationNumber == 0)) {
          String _pandasCode_9 = pandasCode;
          String _string_1 = validationMetric_1.getLiteral().toString();
          String _plus_2 = ("\naccuracy = " + _string_1);
          String _plus_3 = (_plus_2 + "(y_test, y_pred)");
          pandasCode = (_pandasCode_9 + _plus_3);
          String _pandasCode_10 = pandasCode;
          String _string_2 = validationMetric_1.getLiteral().toString();
          String _plus_4 = ("\nprint(\'" + _string_2);
          String _plus_5 = (_plus_4 + "\', accuracy)");
          pandasCode = (_pandasCode_10 + _plus_5);
        } else {
          String _pandasCode_11 = pandasCode;
          String _string_3 = validationMetric_1.getLiteral().toString();
          String _plus_6 = ((("\nscores = cross_val_score(estimator=clf,  X=X, y=y, cv=" + Integer.valueOf(crossValidationNumber)) + ",scoring=(\'neg_") + _string_3);
          String _plus_7 = (_plus_6 + "\'))");
          pandasCode = (_pandasCode_11 + _plus_7);
          String _pandasCode_12 = pandasCode;
          String _string_4 = validationMetric_1.getLiteral().toString();
          String _plus_8 = ("\nprint(\'" + _string_4);
          String _plus_9 = (_plus_8 + "\', abs(scores.mean()))");
          pandasCode = (_pandasCode_12 + _plus_9);
        }
      }
      byte[] _bytes = pandasCode.getBytes();
      File _file = new File("mml.py");
      Files.write(_bytes, _file);
      return pandasCode;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public Output compile() {
    try {
      final Output result = new Output();
      result.frameworkLang = FrameworkLang.SCIKIT;
      result.mlAlgorithm = this.mlAlgorithm;
      final DataInput dataInput = this.mmlModel.getInput();
      result.fileLocation = dataInput.getFilelocation();
      final String render = this.compileDataInput();
      byte[] _bytes = render.getBytes();
      File _file = new File("mml.py");
      Files.write(_bytes, _file);
      long _currentTimeMillis = System.currentTimeMillis();
      final long startTime = ((long) _currentTimeMillis);
      final Process p = Runtime.getRuntime().exec("python mml.py");
      long _currentTimeMillis_1 = System.currentTimeMillis();
      final long endTime = ((long) _currentTimeMillis_1);
      InputStream _inputStream = p.getInputStream();
      InputStreamReader _inputStreamReader = new InputStreamReader(_inputStream);
      final BufferedReader in = new BufferedReader(_inputStreamReader);
      String line = null;
      while (((line = in.readLine()) != null)) {
        {
          String[] output = line.split(",");
          final String metricName = (output[0]).replace("(", "").replace("\'", "");
          final String value = (output[1]).replace(")", "");
          result.validationMetric_result.put(metricName, Double.valueOf(value));
        }
      }
      result.timestamp = (endTime - startTime);
      return result;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
