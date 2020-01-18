package org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf;

import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
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
import org.xtext.example.mydsl.mml.MLChoiceAlgorithm;
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
import org.xtext.example.mydsl.mml.impl.DTImpl;
import org.xtext.example.mydsl.tests.bobo_abdellah_sabrina_charaf.Output;

@SuppressWarnings("all")
public class MmlCompilateurR {
  private MMLModel mmlModel;
  
  private MLAlgorithm MLA;
  
  private final String name = "MmlCompilateurR";
  
  private MmlCompilateurR() {
  }
  
  public MmlCompilateurR(final MMLModel mmlModel, final MLAlgorithm MLA) {
    if ((mmlModel == null)) {
      throw new IllegalArgumentException((("you should initialize " + this.name) + "with non null value"));
    }
    this.mmlModel = mmlModel;
    this.MLA = MLA;
  }
  
  public EList<MLAlgorithm> removeDuplicate(final EList<MLChoiceAlgorithm> input) {
    final EList<MLAlgorithm> result = new UniqueEList<MLAlgorithm>();
    final List<String> list = new ArrayList<String>();
    for (final MLChoiceAlgorithm item : input) {
      {
        final MLAlgorithm MLA = item.getAlgorithm();
        boolean _contains = list.contains(MLA.getClass().getSimpleName());
        boolean _not = (!_contains);
        if (_not) {
          list.add(MLA.getClass().getSimpleName());
          result.add(MLA);
        }
      }
    }
    return result;
  }
  
  public String render() {
    final DataInput dataInput = this.mmlModel.getInput();
    final EList<MLChoiceAlgorithm> MLCAList = this.mmlModel.getAlgorithms();
    final RFormula formula = this.mmlModel.getFormula();
    final Validation validation = this.mmlModel.getValidation();
    final StratificationMethod stratificationMethod = validation.getStratification();
    final EList<ValidationMetric> VMList = validation.getMetric();
    final String fileLocation = dataInput.getFilelocation();
    double split_ratio = 0.7;
    boolean _matched = false;
    if (stratificationMethod instanceof CrossValidation) {
      _matched=true;
    }
    if (!_matched) {
      if (stratificationMethod instanceof TrainingTest) {
        _matched=true;
        final TrainingTest trainingTest = ((TrainingTest) stratificationMethod);
        split_ratio = trainingTest.getNumber();
      }
    }
    String imports = ("library(dplyr)" + "\n");
    String _imports = imports;
    imports = (_imports + ("library(caTools)" + "\n"));
    String _imports_1 = imports;
    imports = (_imports_1 + ("library(Metrics)" + "\n"));
    String predictiveColName = "colnames(df)[ncol(df)-1]";
    int predictiveColumn = 0;
    String predictors = ".";
    final String DEFAULT_COLUMN_SEPARATOR = ",";
    final String csv_separator = DEFAULT_COLUMN_SEPARATOR;
    String rasCode = ((((("read.csv(\"" + fileLocation) + "\",head = TRUE, sep=\"") + csv_separator) + "\")->df") + "\n");
    String selectX = ("df %>% select(-c())->X" + "\n");
    String selectY = ("df %>% select(c())->Y" + "\n");
    if ((formula != null)) {
      FormulaItem _predictive = formula.getPredictive();
      boolean _tripleNotEquals = (_predictive != null);
      if (_tripleNotEquals) {
        predictiveColName = formula.getPredictive().getColName();
        predictiveColumn = formula.getPredictive().getColumn();
        if ((predictiveColName != null)) {
          selectX = ((("df %>% select(-c(" + predictiveColName) + "))->X") + "\n");
        } else {
          selectY = ((("df %>% select(-c(colnames(df)[" + Integer.valueOf(predictiveColumn)) + "]))->X") + "\n");
        }
      }
      XFormula _predictors = formula.getPredictors();
      boolean _matched_1 = false;
      if (_predictors instanceof AllVariables) {
        _matched_1=true;
        predictors = ".";
      }
      if (!_matched_1) {
        if (_predictors instanceof PredictorVariables) {
          _matched_1=true;
          XFormula _predictors_1 = formula.getPredictors();
          final PredictorVariables predictorVariables = ((PredictorVariables) _predictors_1);
          EList<FormulaItem> _vars = predictorVariables.getVars();
          for (final FormulaItem formulaItem : _vars) {
            String _colName = formulaItem.getColName();
            boolean _tripleNotEquals_1 = (_colName != null);
            if (_tripleNotEquals_1) {
              String _predictors_2 = predictors;
              String _colName_1 = formulaItem.getColName();
              String _plus = (_colName_1 + " + ");
              predictors = (_predictors_2 + _plus);
            } else {
              String _predictors_3 = predictors;
              int _column = formulaItem.getColumn();
              String _plus_1 = ("colnames(df)[" + Integer.valueOf(_column));
              String _plus_2 = (_plus_1 + "] + ");
              predictors = (_predictors_3 + _plus_2);
            }
          }
          int _length = predictors.length();
          int _minus = (_length - 4);
          predictors = predictors.substring(0, _minus);
        }
      }
    }
    String _rasCode = rasCode;
    rasCode = (_rasCode + (selectX + selectY));
    String _rasCode_1 = rasCode;
    rasCode = (_rasCode_1 + ((((("sample.split(df$" + predictiveColName) + ",SplitRatio=") + Double.valueOf(split_ratio)) + ")->split_index") + "\n"));
    String _rasCode_2 = rasCode;
    rasCode = (_rasCode_2 + ("train<-subset(df,split_index==T)" + "\n"));
    String _rasCode_3 = rasCode;
    rasCode = (_rasCode_3 + ("test<-subset(df,split_index==F)" + "\n"));
    final EList<MLAlgorithm> MLAList = this.removeDuplicate(MLCAList);
    for (final MLAlgorithm MLA : MLAList) {
      boolean _matched_2 = false;
      if (MLA instanceof DT) {
        _matched_2=true;
        String _imports_2 = imports;
        imports = (_imports_2 + "library(rpart)\n");
        final DTImpl dtImpl = ((DTImpl) MLA);
        String _rasCode_4 = rasCode;
        rasCode = (_rasCode_4 + (((("fit <- rpart(" + predictiveColName) + "~") + predictors) + 
          ", data = train, method = \'class\', control = rpart.control(cp = 0"));
        int _max_depth = dtImpl.getMax_depth();
        boolean _tripleNotEquals_1 = (_max_depth != 0);
        if (_tripleNotEquals_1) {
          String _rasCode_5 = rasCode;
          int _max_depth_1 = dtImpl.getMax_depth();
          String _plus = (",maxdepth = " + Integer.valueOf(_max_depth_1));
          rasCode = (_rasCode_5 + _plus);
        }
        String _rasCode_6 = rasCode;
        rasCode = (_rasCode_6 + ("))" + "\n"));
        String _rasCode_7 = rasCode;
        rasCode = (_rasCode_7 + ("result1<-predict(fit, test, type = \'class\')" + "\n"));
        String _rasCode_8 = rasCode;
        rasCode = (_rasCode_8 + ("result <- as.numeric(levels(result1))[result1]" + "\n"));
      }
      if (!_matched_2) {
        if (MLA instanceof SVR) {
          _matched_2=true;
          InputOutput.<String>println("SVR");
        }
      }
      if (!_matched_2) {
        if (MLA instanceof GTB) {
          _matched_2=true;
          InputOutput.<String>println("GTB");
        }
      }
      if (!_matched_2) {
        if (MLA instanceof RandomForest) {
          _matched_2=true;
          InputOutput.<String>println("RandomForest");
        }
      }
      if (!_matched_2) {
        if (MLA instanceof SGD) {
          _matched_2=true;
          InputOutput.<String>println("SGD");
        }
      }
      if (!_matched_2) {
        InputOutput.<String>println("default");
      }
    }
    rasCode = (imports + rasCode);
    String _rasCode_4 = rasCode;
    rasCode = (_rasCode_4 + ((("test %>% select(c(" + predictiveColName) + "))->testY") + "\n"));
    String _rasCode_5 = rasCode;
    rasCode = (_rasCode_5 + ("testY2 <- testY[,1:length(testY)]" + "\n"));
    for (final ValidationMetric item : VMList) {
      if (item != null) {
        switch (item) {
          case MSE:
            String _rasCode_6 = rasCode;
            rasCode = (_rasCode_6 + "mse(testY2, result)");
            break;
          case MAE:
            String _rasCode_7 = rasCode;
            rasCode = (_rasCode_7 + "mae(testY2, result)");
            break;
          case MAPE:
            String _rasCode_8 = rasCode;
            rasCode = (_rasCode_8 + "mape(testY2, result)");
            break;
          default:
            break;
        }
      } else {
      }
    }
    return rasCode;
  }
  
  public Output compile() {
    try {
      final Output result = new Output();
      result.frameworkLang = FrameworkLang.R;
      result.mlAlgorithm = this.MLA;
      final String render = this.render();
      final String filePath = "./src/org/xtext/example/mydsl/tests/bobo_abdellah_sabrina_charaf/mml.R";
      byte[] _bytes = render.getBytes();
      File _file = new File(filePath);
      Files.write(_bytes, _file);
      final Process p = Runtime.getRuntime().exec(("Rscript " + filePath));
      InputStream _inputStream = p.getInputStream();
      InputStreamReader _inputStreamReader = new InputStreamReader(_inputStream);
      final BufferedReader in = new BufferedReader(_inputStreamReader);
      String line = null;
      while (((line = in.readLine()) != null)) {
        System.out.println(line);
      }
      return result;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
