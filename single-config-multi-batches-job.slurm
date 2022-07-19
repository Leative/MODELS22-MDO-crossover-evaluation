#!/bin/bash

#SBATCH --mem-per-cpu=8200
#SBATCH --mail-user=stefan.john@uni-marburg.de
#SBATCH --mail-type=END

module purge

# Be aware of os specific path separators in classpath (: Linux, ; Windows/Mac)
../jdk-11/bin/java -Xmx8192m -cp "$CLASSPATH_ADDITION:./MDEOptimiser/*" uk.ac.kcl.inf.mdeoptimiser.interfaces.cli.Run -m "$CONFIG" -p "$PROJECT_DIR" -b "$((SLURM_ARRAY_TASK_ID))"
