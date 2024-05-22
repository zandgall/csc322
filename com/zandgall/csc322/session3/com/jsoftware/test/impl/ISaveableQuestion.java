/* CSC322 SESSION 3: ASSIGNMENT - PROF. SUSAN FURTNEY
 > ZANDER GALL - GALLA@CSP.EDU

 ## ISaveableQuestion
 # An interface that streamlines the ability for a question to write itself to a save file

 : MADE IN NEOVIM */

package com.jsoftware.test.impl;

import java.io.FileWriter;
import java.io.IOException;

public interface ISaveableQuestion {

	/**
	 * Writes save data of the implementing question using the writer provided
	 * Throws IOException
	 * @param writer The FileWriter used to write save data
	 */
	public void write(FileWriter writer) throws IOException;

}
