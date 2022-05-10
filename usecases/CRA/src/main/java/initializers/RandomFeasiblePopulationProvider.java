package initializers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;
import architectureCRA.ArchitectureCRAFactory;
import architectureCRA.Class;
import architectureCRA.ClassModel;
import architectureCRA.Feature;
import uk.ac.kcl.inf.mdeoptimiser.libraries.core.optimisation.IModelInitialiser;
import uk.ac.kcl.inf.mdeoptimiser.libraries.core.optimisation.interpreter.guidance.Solution;

public class RandomFeasiblePopulationProvider implements IModelInitialiser {
	
	private static final int POPULATION_SIZE = 100;
	
	private static int counter;
	private ClassModel classModel;
	// Seed 42l works well for sound and complete rule sets.
	private final Random rnd = new Random();
	private final ArchitectureCRAFactory fac = ArchitectureCRAFactory.eINSTANCE;
	
	@Override
	public Solution initialise(EObject model) {
		classModel = (ClassModel) model;
		
		addRandomNumberOfClasses();
		distributeFeaturesUniformly();
		
//		if (counter < POPULATION_SIZE) {
//			HenshinResourceSet rs = new HenshinResourceSet("");
//			Resource res = rs.createResource("feasibleInstance" + counter++ + ".xmi");
//			res.getContents().add(model);
//
//			try {
//				res.save(null);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}		
//		}
		return new Solution(classModel);
	}
	
	/**
	 * Adds a random number of classes to the classmodel.
	 */
	private void addRandomNumberOfClasses() {
		int maxNumberOfClasses = 1 + rnd.nextInt(classModel.getFeatures().size());
		for (int i=1; i <= maxNumberOfClasses; i++) {
			Class newClass = fac.createClass();
			newClass.setName("C" + i);
			classModel.getClasses().add(newClass);
		}
	}
	
	private void addDiverseNumberOfClasses() {
		int maxNumberOfClasses = 1 + rnd.nextInt(classModel.getFeatures().size());
	}

	/**
	 * Distributes the features in the classmodel uniformly accross the created classes.
	 */
	private void distributeFeaturesUniformly() {
		int numberOfClasses = classModel.getClasses().size();
		Class[] classArray = new Class[numberOfClasses];
		classModel.getClasses().toArray(classArray);
		
		List<Feature> unassignedFeatures = new ArrayList<>(classModel.getFeatures());
		int remainingFeatures = unassignedFeatures.size();
		
		// add at least one feature to each class
		for (Class emptyClass : classArray) {
			int chosenFeature = rnd.nextInt(remainingFeatures--);
			emptyClass.getEncapsulates().add(unassignedFeatures.remove(chosenFeature));					
		}
		
		// distribute remaining features randomly
		for (Feature feat : unassignedFeatures) {
			feat.getIsEncapsulatedBy().add(classArray[rnd.nextInt(numberOfClasses)]);
		}
	}

}
