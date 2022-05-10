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

public class Cohesion implements IGuidanceFunction {

	public double computeFitness(Solution model) {

		var classModel = (ClassModel) model.getModel();

		// Inverted to make it a minimization objective.
		return calculateCohesion(classModel) * -1d;
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

	private double calculateCohesion(final ClassModel model) {
		double cohesionRatio = 0.0;
		for (final Class clazz : model.getClasses()) {
			if (getMethodsClass(clazz).size() == 0) {
				cohesionRatio += 0.0;
			} else if (getMethodsClass(clazz).size() == 1) { // Here, the second part of the addition is still 0
				if (getAttributesClass(clazz).size() == 0) { // and now, also the first part is 0
					cohesionRatio += 0.0;
				} else { // now, the first part is not 0
					cohesionRatio += mai(clazz, clazz)
							/ (double) (getMethodsClass(clazz).size() * getAttributesClass(clazz).size());
				}
			} else { // Here, we have more than one method in the clazz
				if (getAttributesClass(clazz).size() == 0) { // Now, the first part of the addition will be 0
					cohesionRatio += mmi(clazz, clazz)
							/ (double) (getMethodsClass(clazz).size() * (getMethodsClass(clazz).size() - 1));
				} else { // Here, we have more than 0 attributes and more than 1 method, so we use the
							// whole formula
					cohesionRatio += mai(clazz, clazz)
							/ (double) (getMethodsClass(clazz).size() * getAttributesClass(clazz).size())
							+ mmi(clazz, clazz)
									/ (double) (getMethodsClass(clazz).size() * (getMethodsClass(clazz).size() - 1));
				}
			}
		}
		return cohesionRatio;
	}

	@Override
	public String getName() {
		return "Maximize Cohesion";
	}

}
