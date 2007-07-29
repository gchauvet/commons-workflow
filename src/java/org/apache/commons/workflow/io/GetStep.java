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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.EmptyStackException;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.StepException;
import org.apache.commons.workflow.base.BaseStep;
import org.apache.commons.workflow.util.WorkflowUtils;


/**
 * <p>Retrieve the contents of a specified URL resource, and push the
 * contents as a String object onto the evaluation stack.</p>
 *
 * <p>Supported Attributes:</p>
 * <ul>
 * <li><strong>url</strong> - URL of the resource to be retrieved, or
 *     omitted to pop a computed String value from the top of the
 *     evaluation stack.</li>
 * </ul>
 *
 * <strong>DESIGN QUESTION - What about binary content?</strong>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class GetStep extends BaseStep {


    // ----------------------------------------------------------= Constructors


    /**
     * Construct a default instance of this Step.
     */
    public GetStep() {

        super();

    }


    /**
     * Construct an instance of this Step with the specified identifier.
     *
     * @param id Step identifier
     */
    public GetStep(String id) {

        super();
        setId(id);

    }


    /**
     * Construct a fully configured instance of this Step.
     *
     * @param id Step identifier
     * @param url Resource url
     */
    public GetStep(String id, String url) {

        super();
        setId(id);
        setUrl(url);

    }


    // ------------------------------------------------------------- Properties


    /**
     * The URL of the resource to be retrieved.
     */
    protected String url = null;

    public String getUrl() {
        return (this.url);
    }

    public void setUrl(String url) {
        this.url = url;
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

        // Get the remote URL we will be contacting
        Object remote = this.url;
        URL remoteURL = null;
        if (remote == null) {
            try {
                remote = (String) context.pop();
            } catch (EmptyStackException e) {
                throw new StepException("Evaluation stack is empty", this);
            }
        }
        if (remote instanceof URL) {
            remoteURL = (URL) remote;
        } else if (remote instanceof String) {
            try {
                remoteURL = new URL((String) remote);
            } catch (MalformedURLException e) {
                throw new StepException("Invalid URL '" + remote + "'",
                                        e, this);
            }
        } else {
            try {
                remoteURL = new URL(remote.toString());
            } catch (MalformedURLException e) {
                throw new StepException("Invalid URL '" + remote + "'",
                                        e, this);
            }
        }

        // Define variables we will need later
        URLConnection conn = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        InputStreamReader isr = null;
        StringBuffer sb = null;
        StepException se = null;

        try {

            // Open a connection to the specified URL
            conn = remoteURL.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.connect();
            int contentLength = conn.getContentLength();
            if (contentLength < 2048)
                contentLength = 2048;
            sb = new StringBuffer(contentLength);

            // Parse the character encoding to be used
            String contentType = conn.getContentType();
            String encoding =
                WorkflowUtils.parseCharacterEncoding(contentType);

            // Construct a suitable InputStreamReader
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 2048);
            if (encoding == null)
                isr = new InputStreamReader(bis);
            else
                isr = new InputStreamReader(bis, encoding);

            // Copy all characters from this resource
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
            is = null;

        } catch (IOException e) {

            se = new StepException("IOException processing '" + remoteURL +
                                   "'", e, this);

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
            } else if (is != null) {
                try {
                    is.close();
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
