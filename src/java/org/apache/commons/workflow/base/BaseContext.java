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

package org.apache.commons.workflow.base;


import java.util.EmptyStackException;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.workflow.Activity;
import org.apache.commons.workflow.Block;
import org.apache.commons.workflow.BlockState;
import org.apache.commons.workflow.Context;
import org.apache.commons.workflow.ContextEvent;
import org.apache.commons.workflow.ContextListener;
import org.apache.commons.workflow.Owner;
import org.apache.commons.workflow.Step;
import org.apache.commons.workflow.StepException;
import org.apache.commons.workflow.Scope;
import org.apache.commons.workflow.util.ContextSupport;


/**
 * <strong>BaseContext</strong> is a basic <code>Context</code> implementation
 * that can serve as a convenient base class for more sophisticated
 * <code>Context</code> implementations.
 *
 * <p><strong>WARNING</strong> - No synchronization is performed within this
 * class.  If it is used in a multiple thread environment, callers must
 * take suitable precations.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public class BaseContext implements Context {



    // ----------------------------------------------------------- Constructors


    /**
     * Construct a new BaseContext with all default values.
     */
    public BaseContext() {

        super();
        names[LOCAL_SCOPE] = "local";
        scopes[LOCAL_SCOPE] = new BaseScope();

    }


    // ----------------------------------------------------- Instance Variables


    /**
     * The <code>Activity</code> that we are associated with and executing
     * <code>Steps</code> from (if any).
     */
    protected Activity activity = null;


    /**
     * The bean representing the information about this Context made visible
     * through JXPath.
     */
    protected BaseContextBean bean = null;


    /**
     * The suspended "next step" Step for each in-progress Activity that has
     * issued a <code>call()</code> to execute a subordinate Activity.
     */
    protected ArrayStack calls = new ArrayStack();


    /**
     * The set of names associated with the registered <code>Scopes</code>.
     */
    protected String names[] = new String[MAX_SCOPES];


    /**
     * The <code>Step</code> that will be executed first the next time that
     * <code>execute()</code> is called.  This is maintained dynamically as
     * execution proceeds, much like the "next instruction pointer" of a
     * CPU tracks what instruction will be executed next.
     */
    protected Step nextStep = null;


    /**
     * The set of <code>Scopes</code> that have been associated with
     * this Context.  When initially created, every Context has a
     * Scope attached to identifier LOCAL_SCOPE already created.
     */
    protected Scope scopes[] = new Scope[MAX_SCOPES];


    /**
     * The evaluation stack of nameless objects being processed.
     */
    protected ArrayStack stack = new ArrayStack();


    /**
     * The BlockState stack of BlockState objects used to manage iteration.
     */
    protected ArrayStack state = new ArrayStack();


    /**
     * The event listener support object for this <code>Context</code>.
     */
    protected ContextSupport support = new ContextSupport(this);


    /**
     * The suspension flag.  If this is set when a particular <code>Step</code>
     * returns, control will be returned from our <code>execute()</code>
     * method to allow interaction with the rest of the application.  The
     * next time that <code>execute()</code> is called, execution will
     * resume with the next scheduled step.
     */
    protected boolean suspend = false;


    // --------------------------------------------------- Scope Access Methods


    // These methods provide Steps with the ability to get and set arbitrary
    // objects in arbitrary scopes.  When a scope is not mentioned explicitly,
    // either LOCAL_SCOPE will be used, or scopes will be searched in
    // increasing order of their identifiers, as specified in the various
    // method descriptions.


    /**
     * Return true if a bean with the specified key exist in any specified
     * scope.  Scopes will be searched in ascending order of their identifiers,
     * starting with LOCAL_SCOPE.
     *
     * @param key Key of the bean to be searched for (cannot be null)
     */
    public boolean contains(String key) {

        for (int i = 0; i < MAX_SCOPES; i++) {
            if (scopes[i] == null)
                continue;
            if (scopes[i].containsKey(key))
                return (true);
        }
        return (false);

    }


    /**
     * Does a bean with the specified key exist in the specified scope?
     *
     * @param key Key of the bean to be searched for (cannot be null)
     * @param scope Identifier of the scope to be returned.
     */
    public boolean contains(String key, int scope) {

        if (scopes[scope] == null)
            return (false);
        else
            return (scopes[scope].containsKey(key));

    }


    /**
     * Return the bean associated with the specified key, if it exists in
     * any scope.  Scopes will be searched in increasing order of their
     * identifiers, starting with LOCAL_SCOPE.  If the underlying Scope
     * allows null values, a <code>null</code> return will be ambiguous.
     * Therefore, you can use the <code>contains()</code> method to determine
     * whether the key actually exists.
     *
     * @param key Key of the bean to be retrieved (cannot be null)
     */
    public Object get(String key) {

        for (int i = 0; i < MAX_SCOPES; i++) {
            if (scopes[i] == null)
                continue;
            Object bean = scopes[i].get(key);
            if (bean != null)
                return (bean);
        }
        return (null);

    }


    /**
     * Return the bean associated with the specified key, if it exists in
     * the specified scope.  If the underlying Scope allows null values,
     * a <code>null</code> return will be ambiguous.  Therefore, you can
     * use the <code>contains()</code> method to determine whether the
     * key actually exists.
     *
     * @param key Key of the bean to be searched for
     * @param scope Identifier of the scope to be searched
     */
    public Object get(String key, int scope) {

        if (scopes[scope] == null)
            return (null);
        else
            return (scopes[scope].get(key));

    }


    /**
     * Store the specified bean under the specified key, in local scope,
     * replacing any previous value for that key.  Any previous value will
     * be returned.
     *
     * @param key Key of the bean to be stored (cannot be null)
     * @param value Value of the bean to be stored
     */
    public Object put(String key, Object value) {

        return (scopes[LOCAL_SCOPE].put(key, value));

    }


    /**
     * Store the specified bean under the specified key, in the specified
     * scope, replacing any previous value for that key.  Any previous value
     * will be returned.
     *
     * @param key Key of the bean to be stored (cannot be null)
     * @param value Value of the bean to be stored
     * @param scope Identifier of the scope to use for storage
     */
    public Object put(String key, Object value, int scope) {

        if (scopes[scope] == null)
            return (null);
        else
            return (scopes[scope].put(key, value));

    }


    /**
     * Remove any existing value for the specified key, in any scope.
     * Scopes will be searched in ascending order of their identifiers,
     * starting with LOCAL_SCOPE.  Any previous value for this key will
     * be returned.
     *
     * @param key Key of the bean to be removed
     */
    public Object remove(String key) {

        return (scopes[LOCAL_SCOPE].remove(key));

    }


    /**
     * Remove any existing value for the specified key, from the specified
     * scope.  Any previous value for this key will be returned.
     *
     * @param key Key of the bean to be removed
     * @param scope Scope the bean to be removed from
     */
    public Object remove(String key, int scope) {

        if (scopes[scope] == null)
            return (null);
        else
            return (scopes[scope].remove(key));

    }



    // --------------------------------------------- Scope Registration Methods


    // These methods provide capabilities to register and retrieve
    // Scope implementations.  Workflow engine implementations will
    // register desired scopes when they create Context instances.


    /**
     * <p>Register a Scope implementation under the specified identifier.
     * It is not legal to replace the LOCAL_SCOPE implementation that is
     * provided by the Context implementation.</p>
     *
     * <p>In addition to registering the new Scope such that it can be
     * accessed dirctly via calls like <code>Context.get(String,int)</code>,
     * the Scope <code>impl</code> object will also be added to the LOCAL
     * Scope under the same name.  This makes possible a single unified
     * namespace of all accessible objects that can be navigated by
     * expression languages.</p>
     *
     * @param scope Scope identifier to register under
     * @param name Scope name to register under
     * @param impl Scope implementation to be registered (or null to
     *  remove a previous registration)
     * @exception IllegalArgumentException if you attempt to register
     *  or deregister the local scope
     */
    public void addScope(int scope, String name, Scope impl) {

        if (scope == LOCAL_SCOPE)
            throw new IllegalArgumentException("Cannot replace local scope");
        if (impl == null) {
            getScope(LOCAL_SCOPE).remove(name);
            names[scope] = null;
            scopes[scope] = null;
        } else {
            getScope(LOCAL_SCOPE).put(name, impl);
            names[scope] = name;
            scopes[scope] = impl;
        }
        bean = null;

    }


    /**
     * Return the Scope instance for our local Scope as a simple property.
     */
    public Scope getLocal() {

        return (getScope(LOCAL_SCOPE));

    }


    /**
     * Return the Scope implementation registered for the specified
     * identifier, if any; otherwise, return <code>null</code>.
     *
     * @param scope Scope identifier to select
     */
    public Scope getScope(int scope) {

        if ((scope >= 0) && (scope < MAX_SCOPES))
            return (scopes[scope]);
        else
            return (null);

    }


    /**
     * Return the Scope implementation registered for the specified
     * name, if any; otherwise, return <code>null</code>.
     *
     * @param name Scope name to select
     */
    public Scope getScope(String name) {

        return (getScope(getScopeId(name)));

    }


    /**
     * Return the Scope identifier registered for the specified
     * name, if any; otherwise, return <code>-1</code>.
     *
     * @param name Scope name to select
     */
    public int getScopeId(String name) {

        for (int i = 0; i < names.length; i++) {
            if (names[i] == null)
                continue;
            if (names[i].equals(name))
                return (i);
        }
        return (-1);

    }


    // ----------------------------------------------- Evaluation Stack Methods


    /**
     * Clear the evaluation stack.
     */
    public void clear() {

        stack.clear();

    }


    /**
     * Is the evaluation stack currently empty?
     */
    public boolean isEmpty() {

        return (stack.size() == 0);

    }


    /**
     * Return the top item from the evaluation stack without removing it.
     *
     * @exception EmptyStackException if the stack is empty
     */
    public Object peek() throws EmptyStackException {

        return (stack.peek());

    }


    /**
     * Pop and return the top item from the evaluation stack.
     *
     * @exception EmptyStackException if the stack is empty
     */
    public Object pop() throws EmptyStackException {

        return (stack.pop());

    }


    /**
     * Push a new item onto the top of the evaluation stack.
     *
     * @param item New item to be pushed
     */
    public void push(Object item) {

        stack.push(item);

    }


    // ----------------------------------------------- BlockState Stack Methods


    /**
     * Clear the BlockState stack.
     */
    public void clearBlockState() {

        state.clear();

    }


    /**
     * Is the BlockState stack currently empty?
     */
    public boolean isEmptyBlockState() {

        return (state.size() == 0);

    }


    /**
     * Return the top item from the BlockState stack without removing it.
     *
     * @exception EmptyStackException if the stack is empty
     */
    public BlockState peekBlockState() throws EmptyStackException {

        return ((BlockState) state.peek());

    }


    /**
     * Pop and return the top item from the BlockState stack.
     *
     * @exception EmptyStackException if the stack is empty
     */
    public BlockState popBlockState() throws EmptyStackException {

        return ((BlockState) state.pop());

    }


    /**
     * Push a new item onto the top of the BlockState stack.
     *
     * @param item New item to be pushed
     */
    public void pushBlockState(BlockState item) {

        state.push(item);

    }


    // ---------------------------------------------- Dynamic Execution Methods


    // These methods are used to request the dynamic execution of the
    // Steps related to a particular Activity.


    /**
     * <p>Save the execution state (ie the currently assigned next step)
     * of the Activity we are currently executing, and begin executing the
     * specified Activity.  When that Activity exits (either normally
     * or by throwing an exception), the previous Activity will be resumed
     * where it left off.
     *
     * @param activity The Activity to be called
     */
    public void call(Activity activity) {

        // Save the next Step for the current Activity (if we have
        // any remaining steps to worry about -- a call on the last
        // step of an activity is more like a non-local goto)
        if (this.nextStep != null)
            calls.push(this.nextStep);

        // Forward control to the first Step of the new Activity
        this.activity = activity;
        this.nextStep = activity.getFirstStep();

    }


    /**
     * <p>Execute the <code>Step</code> currently identified as the next
     * step, and continue execution until there is no next step, or until
     * the <code>suspend</code> property has been set to true.  Upon
     * completion of an activity, any execution state that was saved to due
     * to utilizing the <code>call()</code> method will be restored, and
     * the saved Activity execution shall be resumed.
     *
     * @exception StepException if an exception is thrown by the
     *  <code>execute()</code> method of a Step we have executed
     * @exception IllegalStateException if there is no defined next step
     *  (either because there is no Activity, or because we have already
     *  completed all the steps of this activity)
     */
    public void execute() throws StepException {

        // Do we actually have a next step to be performed
        if (activity == null)
            throw new IllegalStateException("No Activity has been selected");
        if (nextStep == null)
            nextStep = activity.getFirstStep();
        if (nextStep == null)
            throw new IllegalStateException("Activity has been completed");

        // Reset the suspend flag until set by another step
        suspend = false;

        // Send a beforeActivity() event to interested listeners
        support.fireBeforeActivity(nextStep);

        // Perform execution until suspended or completed
        Step thisStep = null;
        StepException exception = null;
        while (true) {

            // Process a suspension of Activity execution
            if (suspend)
                break;                // Suspend set by a Step

            // Process completion of an Activity
            if (nextStep == null) {

                // If there are no active calls, we are done
                if (calls.empty())
                    break;

                // If there are active calls, resume the most recent one
                try {
                    nextStep = (Step) calls.pop();
                    Owner owner = nextStep.getOwner();
                    while (!(owner instanceof Activity)) {
                        owner = ((Step) owner).getOwner();
                    }
                    this.activity = (Activity) owner;
                } catch (EmptyStackException e) {
                    ; // Can not happen
                }
                continue;

            }

            // Execute the (now) current Step
            thisStep = nextStep;
            nextStep = thisStep.getNextStep(); // Assume sequential execution
            try {
                support.fireBeforeStep(thisStep);
                thisStep.execute(this);
                support.fireAfterStep(thisStep);
            } catch (StepException e) {
                exception = e;
                support.fireAfterStep(thisStep, exception);
                break;
            } catch (Throwable t) {
                exception = new StepException(t, thisStep);
                support.fireAfterStep(thisStep, exception);
                break;
            }

        }

        // Send an afterActivity event to interested listeners
        support.fireAfterActivity(thisStep, exception);

        // Rethrow any StepException that was thrown
        if (exception != null)
            throw exception;

    }


    /**
     * <p>Return the <code>Activity</code> we will be executing when the
     * <code>execute()</code> method is called, if any.</p>
     */
    public Activity getActivity() {

        return (this.activity);

    }


    /**
     * Return the set of pending Step executions that are pending because
     * of calls to subordinate Activities have occurred.  If there are
     * no pending Step executions, a zero-length array is returned.
     */
    public Step[] getCalls() {

        Step steps[] = new Step[calls.size()];
        return ((Step[]) calls.toArray(steps));

    }


    /**
     * Return the JXPathContext object that represents a unified namespace
     * covering all of our registered <code>Scopes</code>.
     */
    public JXPathContext getJXPathContext() {

        if (bean == null)
            bean = new BaseContextBean(this);
        return (JXPathContext.newContext(bean));

    }


    /**
     * <p>Return the <code>Step</code> that will be executed the next time
     * that <code>execute()</code> is called, if any.</p>
     */
    public Step getNextStep() {

        return (this.nextStep);

    }


    /**
     * <p>Return the suspend flag.</p>
     */
    public boolean getSuspend() {

        return (this.suspend);

    }


    /**
     * <p>Set the <code>Activity</code> to be executed, and make the first
     * defined <code>Step</code> within this <code>Activity</code> the next
     * action to be performed by <code>execute()</code>.</p>
     *
     * <p>If <code>null</code> is passed, any currently associated Activity
     * will be released, and the evaluation stack and nested call state
     * stack will be cleared.</p>
     *
     * <p><strong>WARNING</strong> - This will have to become more sophisticated
     * in order to support calling nested Activities.</p>
     *
     * @param activity The new Activity to be executed, or <code>null</code>
     *  to release resources
     */
    public void setActivity(Activity activity) {

        if (activity == null) {
            this.activity = null;
            this.nextStep = null;
            clear();
            clearBlockState();
        } else {
            this.activity = activity;
            this.nextStep = activity.getFirstStep();
        }
        calls.clear();

    }


    /**
     * <p>Set the <code>Step</code> that will be executed the next time
     * that <code>execute()</code> is called.  This is called by a
     * <code>Step</code> that wants to perform branching based on some
     * calculated results.</p>
     *
     * @param nextStep The next Step to be executed
     *
     * @exception IllegalArgumentException if the specified Step is not
     *  part of the current Activity
     */
    public void setNextStep(Step nextStep) {

        // Make sure the specified next Step is within the current Activity
        if (nextStep != null) {
            Owner owner = nextStep.getOwner();
            while (owner instanceof Block)
                owner = ((Block) owner).getOwner();
            if (this.activity != (Activity) owner)
                throw new IllegalArgumentException
                    ("Step is not part of the current Activity");
        }
        this.nextStep = nextStep;

    }


    /**
     * <p>Set the suspend flag.  This is called by a <code>Step</code> that
     * wants to signal the <code>Context</code> to return control to the
     * caller of the <code>execute()</code> method before executing the
     * next <code>Step</code> in the current activity.</p>
     *
     * @param suspend The new suspend flag
     */
    public void setSuspend(boolean suspend) {

        this.suspend = suspend;

    }


    // ------------------------------------------------- Event Listener Methods


    /**
     * Add a listener that is notified each time beans are added,
     * replaced, or removed in this context.
     *
     * @param listener The ContextListener to be added
     */
    public void addContextListener(ContextListener listener) {

        support.addContextListener(listener);

    }


    /**
     * Remove a listener that is notified each time beans are added,
     * replaced, or removed in this context.
     *
     * @param listener The ContextListener to be removed
     */
    public void removeContextListener(ContextListener listener) {

        support.removeContextListener(listener);

    }


    // -------------------------------------------------------- Package Methods


    /**
     * Return the array of registered <code>Scope</code> names for this
     * <code>Context</code>.
     */
    String[] getScopeNames() {

        return (this.names);

    }


}
