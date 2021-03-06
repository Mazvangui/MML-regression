/**
 * generated by Xtext 2.20.0
 */
package org.xtext.example.mydsl.mml;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>RFormula</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.xtext.example.mydsl.mml.RFormula#getPredictive <em>Predictive</em>}</li>
 *   <li>{@link org.xtext.example.mydsl.mml.RFormula#getPredictors <em>Predictors</em>}</li>
 * </ul>
 *
 * @see org.xtext.example.mydsl.mml.MmlPackage#getRFormula()
 * @model
 * @generated
 */
public interface RFormula extends EObject
{
  /**
   * Returns the value of the '<em><b>Predictive</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Predictive</em>' containment reference.
   * @see #setPredictive(FormulaItem)
   * @see org.xtext.example.mydsl.mml.MmlPackage#getRFormula_Predictive()
   * @model containment="true"
   * @generated
   */
  FormulaItem getPredictive();

  /**
   * Sets the value of the '{@link org.xtext.example.mydsl.mml.RFormula#getPredictive <em>Predictive</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Predictive</em>' containment reference.
   * @see #getPredictive()
   * @generated
   */
  void setPredictive(FormulaItem value);

  /**
   * Returns the value of the '<em><b>Predictors</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Predictors</em>' containment reference.
   * @see #setPredictors(XFormula)
   * @see org.xtext.example.mydsl.mml.MmlPackage#getRFormula_Predictors()
   * @model containment="true"
   * @generated
   */
  XFormula getPredictors();

  /**
   * Sets the value of the '{@link org.xtext.example.mydsl.mml.RFormula#getPredictors <em>Predictors</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Predictors</em>' containment reference.
   * @see #getPredictors()
   * @generated
   */
  void setPredictors(XFormula value);

} // RFormula
