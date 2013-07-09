 * * This package is free software; you can redistribute it and/or
 * * modify it under the terms of the GNU General Public License 
 * * as published by the Free Software Foundation; either version 2 
 * * of the License, or (at your option) any later version, 
 * * provided that any use properly credits the author. 
 * * This program is distributed in the hope that it will be useful,
 * * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * * GNU General Public License for more details at http://www.gnu.org * * */



IonChannelHMMs
==============

This is code to generate Fig. 2 from an upcoming review in the 2nd edition of the Handbook of Ion Channels.

Note the source code is in the src directory and the compiled class files are in the bin files.

You will need a recent version of Java installed (this can be obtained here: http://www.oracle.com/technetwork/java/index.html ) 

To run this code, a file called "PropertiesFile.txt" must be in the classpath.

(There is an example of the properties file checked in at IonChannelHMMs/bin/PropertiesFile.txt)

The PropertiesFile.txt should contain a line:

MARKOV_CHAIN_OUT_DIR=D:\\simDir

which should point to where you would like the data files to be written. 

(Notice the double-back slash for Windows users).

To run the code, open up a console or DOS box.

From the directory IonChannelHMMs/bin/ type:

java gaussianMarkovModels.GenerateChainFromGaussianModel

If all has gone well, you should see 3 files in the directory pointed to by MARKOV_CHAIN_OUT_DIR.

"TwoStateIonChannel_GaussianPosteriorProbs.txt" has the most useful data. 
The columns in that file are:

Iteration - the iteration value corresponding to one interval of time

emission - the data for that iteration

trueState - the true state of the model at the time of the iteration

PosteriorProbStateC - From the Forward/Backward algorithm, p(Closed|data)

PosteriorProbStateO - From the Forward/Backward algorithm, p(Open|data)

To generate Fig. 4 in the paper, set TwoStateIonChannel.OpenState.meanEmission to 0.5 and re-compile.

If you run into trouble, spot bugs, would like to contribute to this code base or have any questions, 
e-mail me at anthony.fodor@gmail.com





