/*
 * Copyright 1999-2001,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 

package org.apache.commons.workflow.io;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.StepException;
import org.apache.commons.workflow.base.BaseStep;


/**
 * <p>Read the contents of the specified file from the filesystem, and
 * push the contents as a String object onto the evaluation stack.</p>
 *
 * <p>Supported Attributes:</p>
 * <ul>
 * <li><strong>encoding</strong> - Character encoding in which to interpret
 *     the characters in the specified file, or omitted for the platform
 *     default encoding.</li>
 * <li><strong>file</strong> - Relative or absolute operating system pathname
 *     whose contents are to be read.</li>
 * </ul>
 *
 * <strong>DESIGN QUESTION - What about binary content?</strong>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class ReadStep extends BaseStep {


    // ----------------------------------------------------------= Constructors


    /**
     * Construct a default instance of this Step.
     */
    public ReadStep() {

        super();

    }


    /**
     * Construct an instance of this Step with the specified identifier.
     *
     * @param id Step identifier
     */
    public ReadStep(String id) {

        super();
        setId(id);

    }


    /**
     * Construct a fully configured instance of this Step.
     *
     * @param id Step identifier
     * @param encoding Character encoding to use
     * @param file Relative or absolute pathname
     */
    public ReadStep(String id, String encoding, String file) {

        super();
        setId(id);
        setEncoding(encoding);
        setFile(file);

    }


    // ------------------------------------------------------------- Properties


    /**
     * The character encoding used to interpret the contents of this file.
     */
    protected String encoding = null;

    public String getEncoding() {
        return (this.encoding);
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }


    /**
     * The relative or absolute pathname of the operating system file.
     */
    protected String file = null;

    public String getFile() {
        return (this.file);
    }

    public void setFile(String file) {
        this.file = file;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Perform the executable actions related to this Step, in the context of
     * the specified Context.
     *
     * @param context The Context that is tracking our execution state
     *
     * @exception StepException if a processing error has occurred
     */
    public void execute(Context context) throws StepException {

        // Define variables we will need later
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        InputStreamReader isr = null;
        StringBuffer sb = new StringBuffer();
        StepException se = null;

        try {

            // Construct a suitable InputStreamReader
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis, 2048);
            if (encoding == null)
                isr = new InputStreamReader(bis);
            else
                isr = new InputStreamReader(bis, encoding);

            // Copy all characters from this file
            while (true) {
                int ch = isr.read();
                if (ch < 0)
                    break;
                sb.append((char) ch);
            }

            // Close the input file
            isr.close();
            isr = null;
            bis = null;
            fis = null;

        } catch (IOException e) {

            se = new StepException("IOException processing '" + file + "'",
                                   e, this);

        } finally {

            if (isr != null) {
                try {
                    isr.close();
                } catch (Throwable t) {
                    ;
                }
            } else if (bis != null) {
                try {
                    bis.close();
                } catch (Throwable t) {
                    ;
                }
            } else if (fis != null) {
                try {
                    fis.close();
                } catch (Throwable t) {
                    ;
                }
            }

        }

        // Push results or throw exception as appropriate
        if (se != null)
            throw se;
        context.push(sb.toString());

    }


}
