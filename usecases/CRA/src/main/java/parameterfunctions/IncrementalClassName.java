package parameterfunctions;

import java.util.List;

import uk.ac.kcl.inf.mdeoptimiser.libraries.core.optimisation.interpreter.evolvers.parameters.IEvolverParametersFunction;
import uk.ac.kcl.inf.mdeoptimiser.libraries.core.optimisation.interpreter.guidance.Solution;

public class IncrementalClassName implements IEvolverParametersFunction {
	
	private static int count = 0;

	@Override
	public String sample(List<Solution> solutions) {
		return "C" + count++;
	}


}
