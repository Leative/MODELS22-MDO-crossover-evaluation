# Towards a Configurable Crossover Operator for Model-Driven Optimization - Evaluation Data
Evaluation artifacts for the paper _Towards a Configurable Crossover Operator for Model-Driven Optimization_ submitted to MODELS22.

## MDEOptimiser
A precompiled version of the optimzation tool MDEOptimiser (as of 20th of May 2022) with integrated crossover operator is contained as a command line version in the folder `MDEOptimiser`. 

## Folder structure
All artifacts of the use case (CRA case) are located in the folder `usecases/CRA`. 
The subfolder `models` contains the EMF meta-model, the instance models and the Henshin model transformations used as mutation and repair operators.
The subfolder `src` contains the java implementation of the meta-model and, in the package `guidancefunctions`, the implementation of the feasibility constraints and objectives. The package `crossoverdefinition` contains a class used to specify the problem part of the meta-model. Both packages need a part of the adapted MDEOptimiser as a dependency which is delivered as a local p2 repository in `usecases/p2repo`.
The configuration files used by MDEOptimiser to specify the different variants of evolutionary algorithms are located in the folder `configs`.
The detailed results of the experiments of the evaluation of the paper can be found in folder `results`.
More information about the results can be found in the `README` contained in that folder.

## Compiling the use case
The use case is provided as a maven project.
To build it, execute the command `mvn clean compile` in the folder `usecases`.
The build process may take some time as dependencies need to be resolved and downloaded.
The successful build was tested with Apache Maven 3.6.3 and Java 11.0.6. 
As the build configuration uses Tycho 2.0.0 newer Java versions (e.g. Java 16) might not allow for a successful build without adaptions.

## Running experiments in batches
To simplify the execution of a batch of experiments a bash script is contained in the main folder. The script was also used to conduct the experiments of the paper. To run 10 evolutionary computations for all configurations of the CRA case, for example, a call to the script would look like:

`./experiment-runner.sh "usecases/CRA/target/classes/" "usecases/CRA/configs/" "usecases/CRA/"`

Note that the MDEOptimiser argument specifying the path to the project folder (e.g. `usecases/CRA`) 
should not contain backwards navigation (e.g., `usescases/../usecases/CRA`) as an Exception is thrown by MDEOptimiser in this case.
