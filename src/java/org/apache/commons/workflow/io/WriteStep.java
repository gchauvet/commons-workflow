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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.EmptyStackException;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.StepException;
import org.apache.commons.workflow.base.BaseStep;


/**
 * <p>Pop the top value from the evaluation stack, and write its contents
 * as a string to the specified file in the filesystem (replacing any
 * previous contents in that file).</p>
 *
 * <p>Supported Attributes:</p>
 * <ul>
 * <li><strong>encoding</strong> - Character encoding in which to write
 *     the characters to the specified file, or omitted for the platform
 *     default encoding.</li>
 * <li><strong>file</strong> - Relative or absolute operating system pathname
 *     whose contents are to be written.</li>
 * </ul>
 *
 * <strong>DESIGN QUESTION - What about binary content?</strong>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class WriteStep extends BaseStep {


    // ----------------------------------------------------------= Constructors


    /**
     * Construct a default instance of this Step.
     */
    public WriteStep() {

        super();

    }


    /**
     * Construct an instance of this Step with the specified identifier.
     *
     * @param id Step identifier
     */
    public WriteStep(String id) {

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
    public WriteStep(String id, String encoding, String file) {

        super();
        setId(id);
        setEncoding(encoding);
        setFile(file);

    }


    // ------------------------------------------------------------- Properties


    /**
     * The character encoding used to write the contents of this file.
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

        // Pop the top value from the evaluation stack
        Object value = null;
        try {
            value = context.pop();
        } catch (EmptyStackException e) {
            throw new StepException("Evaluation stack is empty", e, this);
        }
        String string = null;
        if (value instanceof String)
            string = (String) value;
        else
            string = value.toString();

        // Define variables we will need later
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        OutputStreamWriter osw = null;
        StepException se = null;

        try {

            // Construct a suitable OutputStreamWriter
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos, 2048);
            if (encoding == null)
                osw = new OutputStreamWriter(bos);
            else
                osw = new OutputStreamWriter(bos, encoding);

            // Copy all characters to this file
            osw.write(string, 0, string.length());

            // Close the output file
            osw.flush();
            osw.close();
            osw = null;
            bos = null;
            fos = null;

        } catch (IOException e) {

            se = new StepException("IOException processing '" + file + "'",
                                   e, this);

        } finally {

            if (osw != null) {
                try {
                    osw.close();
                } catch (Throwable t) {
                    ;
                }
            } else if (bos != null) {
                try {
                    bos.close();
                } catch (Throwable t) {
                    ;
                }
            } else if (fos != null) {
                try {
                    fos.close();
                } catch (Throwable t) {
                    ;
                }
            }

            if (se != null)
                (new File(file)).delete();

        }

    }


}
