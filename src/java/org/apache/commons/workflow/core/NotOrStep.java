package org.apache.commons.workflow.core;

import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.Descriptor;
import org.apache.commons.workflow.Step;
import org.apache.commons.workflow.StepException;


/**
 * <p>Evaluate properties specified by the associated Descriptors, and
 * transfer control to the specified step if ANY of them are
 * <code>false</code> (if boolean) or null (if Object).
 *
 * <b>This is the exact opposite of OrStep</b>
 *
 * To avoid non-deterministic evaluation stack behavior, all of the
 * specified Descriptors are always evaluated.</p>
 *
 * <p>Supported Attributes:</p>
 * <ul>
 * <li><strong>step</strong> - Identifier of the Step to which control
 *     should be transferred if the condition is met.</li>
 * </ul>
 *
 * @version $Revision$ $Date$
 * @author Preston Sheldon
 */

public class NotOrStep extends GotoStep {


    // ----------------------------------------------------------= Constructors


    /**
     * Construct a default instance of this Step.
     */
    public NotOrStep() {

        super();

    }
    /**
     * Construct an instance of this Step with the specified identifier.
     *
     * @param id Step identifier
     */
    public NotOrStep(String id) {

        this(id, null, null);

    }
    /**
     * Construct a fully configured instance of this Step.
     *
     * @param id Step identifier of this step
     * @param step Step identifier to which control should be redirected
     */
    public NotOrStep(String id, String step) {

        this(id, step, null);

    }
    /**
     * Construct a fully configured instance of this Step.
     *
     * @param id Step identifier of this step
     * @param step Step identifier to which control should be redirected
     * @param descriptor Initial descriptor to be added
     */
    public NotOrStep(String id, String step, Descriptor descriptor) {

        super();
        setId(id);
        setStep(step);
        addDescriptor(descriptor);

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

        // Process all associated descriptors
        boolean condition = false;
        Descriptor descriptors[] = findDescriptors();
        for (int i = 0; i < descriptors.length; i++) {
            Object value = descriptors[i].get(context);
            if (value != null) {
                if (value instanceof Boolean) {
                    if (((Boolean) value).booleanValue())
                        condition = true;
                } else {
                    condition = true;
                }
            }
        }

        // Conditionally forward control to the specified step
        if (!condition) {
            Step next = getOwner().findStep(this.step);
            if (next == null)
                throw new StepException("Cannot find step '" + step + "'",
                                        this);
            context.setNextStep(next);
        }
                
    }
}

