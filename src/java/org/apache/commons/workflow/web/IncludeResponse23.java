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

package org.apache.commons.workflow.web;


import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;


/**
 * <p>Implementation of <code>HttpServletResponseWrapper</code> for use in
 * <code>IncludeStep23</code>.  It buffers the response characters up into
 * a memory-resident buffer that can be converted into a String by calling
 * <code>getContent()</code>.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class IncludeResponse23 extends ServletResponseWrapper {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new response wrapper according to the specified parameters.
     *
     * @param response The servlet response we are wrapping
     */
    public IncludeResponse23(ServletResponse response) {

        super(response);

    }


    // ----------------------------------------------------- Instance Variables


    /**
     * Accumulator for output that is generated via
     * <code>getOutputStream()</code>.
     */
    protected ByteArrayOutputStream baos = null;


    /**
     * Accumulator for output that is generated via
     * <code>getWriter()</code>.
     */
    protected CharArrayWriter caw = null;


    // --------------------------------------------------------- Public Methods


    /**
     * Swallow any attempt to flush the response buffer.
     */
    public void flushBuffer() throws IOException {

        ; // No action is required

    }


    /**
     * Return the character encoding for the included response (if any).
     */
    public String getCharacterEncoding() {

        return (null); // FIXME - getCharacterEncoding()

    }


    /**
     * Return the response data written to this response as a String.
     *
     * @exception IOException if a conversion error occurs
     */
    public String getContent() throws IOException {

        String encoding = getCharacterEncoding();
        if (baos != null) {
            if (encoding == null)
                return (baos.toString());
            else
                return (baos.toString(encoding));
        } else if (caw != null) {
            return (caw.toString());
        } else {
            return ("");
        }

    }


    /**
     * Return a ServletOutputStream that can be used to accumulate the response
     * data for the included resource.
     *
     * @exception IOException if an I/O error occurs
     */
    public ServletOutputStream getOutputStream() throws IOException {

        if (caw != null)
            throw new IllegalStateException("getWriter() already called");
        baos = new ByteArrayOutputStream();
        //        return (new IncludeOutputStream23(this));
        return (null); // FIXME - getOutputStream()

    }


    /**
     * Return a PrintWriter that can be used to accumulate the response data
     * for the included resource.
     *
     * @exception IOException if an I/O error occurs
     */
    public PrintWriter getWriter() throws IOException {

        if (baos != null)
            throw new IllegalStateException
                ("getOutputStream() already called");
        caw = new CharArrayWriter();
        //        return (new IncludeWriter23(this));
        return (null); // FIXME - getWriter()

    }


    /**
     * Reset the response buffer and all headers.
     */
    public void reset() {

        resetBuffer();

    }


    /**
     * Reset the response buffer to contain no data.
     */
    public void resetBuffer() {

        if (baos != null)
            baos.reset();
        else if (caw != null)
            caw.reset();

    }


    /**
     * Set the content type (and possibly the character encoding) of the
     * response data.
     *
     * @param contentType The new content type
     */
    public void setContentType(String contentType) {

        ; // FIXME - setContentType()

    }


    // -------------------------------------------------------- Package Methods


    /**
     * Write a sequence of bytes to our accumulator.
     *
     * @param b The byte array
     */
    void write(byte b[]) {

        baos.write(b, 0, b.length);

    }


    /**
     * Write a sequence of bytes to our accumulator.
     *
     * @param b The byte array
     * @param off Starting offset
     * @param len Number of bytes to be written
     */
    void write(byte b[], int off, int len) {

        baos.write(b, off, len);

    }


    /**
     * Write a sequence of characters to our accumulator.
     *
     * @param c The character array
     */
    void write(char c[]) {

        caw.write(c, 0, c.length);

    }


    /**
     * Write a sequence of characters to our accumulator.
     *
     * @param c The character array
     * @param off Starting offset
     * @param len Number of bytes to be written
     */
    void write(char c[], int off, int len) {

        caw.write(c, off, len);

    }


    /**
     * Write a single byte or character (based on which accumulator has
     * been created) to our accumulator.
     *
     * @param value The byte or character to be written
     */
    void write(int value) {

        if (baos != null)
            baos.write(value);
        else
            caw.write(value);

    }


    /**
     * Write a sequence of characters to our accumulator.
     *
     * @param s The character string
     */
    void write(String s) {

        caw.write(s, 0, s.length());

    }


    /**
     * Write a sequence of characters to our accumulator.
     *
     * @param s The character string
     * @param off Starting offset
     * @param len Number of characters to write
     */
    void write(String s, int off, int len) {

        caw.write(s, off, len);

    }


}
