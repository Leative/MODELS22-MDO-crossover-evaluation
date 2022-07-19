#!/bin/bash

# execution: ./hpc-experiment-runner.sh path_to_classes path_to_config project_path number_of_batches estimated_time
# number_of_batches is just an integer. For each config a job array will be created executing the desired number_of_batches in parallel on separate cpus.
# estimated_time is optional and needs to be specified in following format: DD-HH:MM:SS. Defaults to one day.

# Prepare variables passed as environment to the hpc scheduler
export CLASSPATH_ADDITION
export CONFIG
export PROJECT_DIR

# Be aware of os specific path separators in classpath (: Linux, ; Windows/Mac)
execute_single_config () {
	CONFIGDIR=$(dirname $CONFIG)
	JOB_ID="${CONFIGDIR##*/}_${CONFIG##*/}"
	mkdir slurmlogs/$JOB_ID
	sbatch --time=$EST_TIME --array=0-$BATCH_NUMBER --job=$JOB_ID --output=slurmlogs/$JOB_ID/%A_%a.out --error=slurmlogs/$JOB_ID/%A_%a.error --export=CLASSPATH_ADDITION,CONFIG,PROJECT_DIR,JOB_ID single-config-multi-batches-job.slurm
}

# Three parameters: classpath, configuration list, project dir, model (optional)
execute_all_configs () {
	for CONFIG in ${CONFIGS[@]}
	do
		[ -e "$CONFIG" ] || continue
		echo "Executing $CONFIG"
		execute_single_config "$CLASSPATH_ADDITION" "$CONFIG" "$PROJECT_DIR"
	done
}

# Specify path to fitness function classes and meta model classes.
# As usual for java classpath all classes in subdirectories will be considered.
# 
CLASSPATH_ADDITION="$1"

# Specify a single configuration file or a directory containing configuration files.
# In case of a directory, configurations inside subdirectories will not be considered.
if [ -f "$2" ]; then
	echo "Single configuration file will be executed"
	CONFIGS="$2"
elif [ -d "$2" ]; then
	echo "All configs in the specified directory will be executed:"
	CONFIGS=()
	for path in "$2"/*
	do
		if [ -f "$path" ]; then
			CONFIGS+=("$path")
		fi
	done
	for CONFIG in ${CONFIGS[@]}
	do
		echo $CONFIG
	done
else
	echo "Invalid configuration specification"
	exit 1
fi	

# The path relative to which the .mopt configurations will be interpreted.
# This means, the basepath inside .mopt configurations will consider this
# project directory as root directory.
if [ -d "$3" ]; then
	PROJECT_DIR="$3";
else
	echo "Invalid project directory"
	exit 1
fi

# As fourth param specify number of batches which will be run in parallel
if [ "$4" -gt 0 ]; then
	BATCH_NUMBER=$(("$4"-1))
	echo "$4 batches will be performed"
else
	echo "Invalid batch number"
	exit 1
fi	

# As fifth param an estimated maximum execution time for each batch can be specified.
# Lower values might allow faster scheduling and shorter waiting times.
#!/bin/bash
if [ ! -z "$5" ]; then
	EST_TIME="$5"
	echo "$EST_TIME is set as maximum execution time."
else
	EST_TIME="01-00:00:00"
	echo "No execution time specified. Defaulting to $EST_TIME"
fi	

execute_all_configs