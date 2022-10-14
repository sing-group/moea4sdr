# Multi-Objective Evolutioary Algorithms for Synset Dimensionality Reduction 
This tool has been developed to discover the usage of Multi-Objective Evolutionary computation to reduce the dimensionality of synset-based datatsets.

# Requirements
Maven is required for building the software.



## Building
The source has been developed using Maven. You can simply build/rebuild the software by entering the following command:

```
mvn clean package
```

## Configuring
The configuration contains 2 parameters (minimun generalization steps and maximum generalization steps). The minimum generalization steps parameter can be setted to the following values:
* -1 Indicates that a synset column can be deleted
* 0 Indicates that a synset column can be kept with no modification
* x>1 Indicates that a column should be generalized following the ontology at least one time

The maximum generalization steps parameter should be setted to a value greater than the minum generalization value. 

This parameters can be configured in two forms:
1. Using standard command line parameters. i.e. ```java -jar target/JMetalBdp4j-1.0-SNAPSHOT.jar <lowest> <highest>```. This form of configuration has priority against the remaining ones.
2. Using the properties file cfg.properties and stablishing MIN_BOUND and MAX_BOUND paramters

## Running
From the main folder of the project execute the following command:

```
java -jar target/JMetalBdp4j-1.0-SNAPSHOT.jar
```

If you want to speficy command-line parameters, please use the following syntax:
```
java -jar target/JMetalBdp4j-1.0-SNAPSHOT.jar <lowest> <highest>
```

## Credits

This file is part of the work developed by Iñaki Vélez in this Ph.D. thesis.