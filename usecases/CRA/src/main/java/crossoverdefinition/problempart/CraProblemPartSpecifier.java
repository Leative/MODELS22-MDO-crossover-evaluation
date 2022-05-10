package crossoverdefinition.problempart;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import architectureCRA.ArchitectureCRAPackage;
import uk.ac.kcl.inf.mdeoptimiser.libraries.core.optimisation.IProblemPartSpecifier;

public class CraProblemPartSpecifier implements IProblemPartSpecifier {

	@Override
	public Set<EObject> getProblemPart(EPackage metamodel) {
		HashSet<EObject> problemPartTypes = new HashSet<>();
		problemPartTypes.add(ArchitectureCRAPackage.Literals.CLASS_MODEL__FEATURES);
		problemPartTypes.add(ArchitectureCRAPackage.Literals.METHOD__DATA_DEPENDENCY);
		problemPartTypes.add(ArchitectureCRAPackage.Literals.METHOD__FUNCTIONAL_DEPENDENCY);
		return problemPartTypes;
	}

}
