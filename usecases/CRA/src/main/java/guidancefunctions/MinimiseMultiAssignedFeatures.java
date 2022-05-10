package guidancefunctions;

import architectureCRA.ClassModel;
import uk.ac.kcl.inf.mdeoptimiser.libraries.core.optimisation.IGuidanceFunction;
import uk.ac.kcl.inf.mdeoptimiser.libraries.core.optimisation.interpreter.guidance.Solution;

public class MinimiseMultiAssignedFeatures implements IGuidanceFunction {

	public double computeFitness(Solution model) {
		
		var features = ((ClassModel) model.getModel()).getFeatures();	
		var constraintValue = features.stream().filter(feature -> feature.getIsEncapsulatedBy().size() > 1).count();
		return constraintValue;
	}

	public String getName() {
		return "Minimise Features assigned to multiple Classes";
	}

}
