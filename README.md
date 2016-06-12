# Towards a Source Code Analyser for Choosing Between Data Structure Implementations
This repository contains the code for the thesis project by Andrew Whalley at the University of Queensland, Australia: August 2015 - June 2016.

## Disclaimer
The code has been developed by Andrew Whalley. This code uses the open source PMD project to analyse Java files. The project is open source but incomplete. People are free to fork this project under the GNU license and use it as they wish. Any issues can be submitted to the issue tracker, but the project may not progress after June 2016.

## jdsa-thesis
This thesis is concerned with the analysis of how List implementations (ArrayLists and LinkedLists) are used in Java code. The aim is to perform a naive complexity analysis on the code in order to determine an approximation of the runtime complexity each implementation would have and hence form a recommendation as to which implementation would be more efficient. 

For more information, please refer to the thesis paper available at: \<Not actually finished yet\>

## Installation instructions
1. Get PMD for Eclipse
2. Fork this repository
3. Add the repository to Eclipse
4. Execute DetectDataStructure as an Eclipse Application
5. In the new window edit the preferences of PMD
6. Add the DetectDataStructure rule to PMD
7. On the source code to be analysed, right-click and select PMD->Check Code
8. Information about the analysis is displayed to standard out in the original Eclipse runtime window
\<Will update to be more specific\>

## Program Rundown
\<How the DetectDataStructure code works and links to other files so that the project can be extended easily\>
