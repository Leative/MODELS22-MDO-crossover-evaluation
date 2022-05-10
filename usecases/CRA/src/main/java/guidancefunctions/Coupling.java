package guidancefunctions;

import java.util.ArrayList;
import java.util.List;

import architectureCRA.Attribute;
import architectureCRA.Class;
import architectureCRA.ClassModel;
import architectureCRA.Feature;
import architectureCRA.Method;
import uk.ac.kcl.inf.mdeoptimiser.libraries.core.optimisation.IGuidanceFunction;
import uk.ac.kcl.inf.mdeoptimiser.libraries.core.optimisation.interpreter.guidance.Solution;

public class Coupling implements IGuidanceFunction {

	@Override
	public double computeFitness(Solution model) {

		var classModel = (ClassModel) model.getModel();

		return calculateCoupling(classModel);
	}

	private int mai(final Class classSource, final Class classTarget) {
		int mai = 0;
		for (final Method method : getMethodsClass(classSource)) {
			for (final Attribute attribute : getAttributesClass(classTarget)) {
				if (method.getDataDependency().contains(attribute)) {
					mai++;
				}
			}
		}
		return mai;
	}

	private int mmi(final Class classSource, final Class classTarget) {
		int mmi = 0;
		for (final Method methodSource : getMethodsClass(classSource)) {
			for (final Method methodTarget : getMethodsClass(classTarget)) {
				if (methodSource.getFunctionalDependency().contains(methodTarget)) {
					mmi++;
				}
			}
		}
		return mmi;
	}

	private List<Attribute> getAttributesClass(final Class clazz) {
		final List<Attribute> attributes = new ArrayList<>();
		for (final Feature feature : clazz.getEncapsulates()) {
			if (feature instanceof Attribute) {
				attributes.add((Attribute) feature);
			}
		}
		return attributes;
	}

	private List<Method> getMethodsClass(final Class clazz) {
		final List<Method> methods = new ArrayList<>();
		for (final Feature feature : clazz.getEncapsulates()) {
			if (feature instanceof Method) {
				methods.add((Method) feature);
			}
		}
		return methods;
	}

	private double calculateCoupling(final Class classSource, final ClassModel model) {
		double couplingRatio = 0;
		for (final Class classTarget : model.getClasses()) {
			if (classSource != classTarget) {
				if (getMethodsClass(classSource).size() == 0) {
					couplingRatio += 0.0;
				} else { // From here, |M(clsi)|>0
					if (getMethodsClass(classTarget).size() <= 1) { // The second part of the addition is 0
						if (getAttributesClass(classTarget).size() == 0) { // Now, also the first part of the addition
																			// is 0
							couplingRatio += 0.0;
						} else { // Now, the first part of the addition is not 0
							couplingRatio += mai(classSource, classTarget)
									/ (double) (getMethodsClass(classSource).size()
											* getAttributesClass(classTarget).size());
						}
					} else { // Now, the second part of the addition is not 0
						if (getAttributesClass(classTarget).size() == 0) {
							couplingRatio += mmi(classSource, classTarget)
									/ (double) (getMethodsClass(classSource).size()
											* (getMethodsClass(classTarget).size() - 1));
						} else { // Now, non of the parts is 0
							couplingRatio += mai(classSource, classTarget)
									/ (double) (getMethodsClass(classSource).size()
											* getAttributesClass(classTarget).size())
									+ mmi(classSource, classTarget) / (double) (getMethodsClass(classSource).size()
											* (getMethodsClass(classTarget).size() - 1));
						}
					}
				}
			}
		}
		return couplingRatio;
	}

	private double calculateCoupling(final ClassModel model) {
		double couplingRatio = 0;
		for (final Class clazz : model.getClasses()) {
			couplingRatio += calculateCoupling(clazz, model);
		}
		return couplingRatio;
	}

	@Override
	public String getName() {
		return "Minimize Coupling";
	}

}
