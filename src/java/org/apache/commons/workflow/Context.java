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

package org.apache.commons.workflow;


import java.util.EmptyStackException;
import org.apache.commons.jxpath.JXPathContext;


/**
 * <p>A <strong>Context</strong> represents the dynamic computational state of
 * a workflow process that is currently being executed.  If multiple users
 * are executing the same workflow process at the same time, they must have
 * their own instance of Context.</p>
 *
 * <p>Workflow engine implementations can register zero or more Scope
 * instances, to provide the Steps that are being executed with access to
 * arbitrary collections of JavaBeans.  A Context will always have at least
 * one Scope, called the LOCAL scope, which is the default source or
 * destination for bean references.  Scope instances can be identified
 * by an integer subscript (which must be in the range 0 .. MAX_SCOPES-1),
 * or a registered name.  The registered name of the LOCAL scope is
 * "local".</p>
 *
 * <p>In addition to Scopes, which can be used for storing beans under
 * specific names, an evaluation stack is provided for the convenience of
 * Step implementations that want to pass temporary results back and forth.
 * As with all Stack-based implementations, it is the responsibility of
 * the Step implementations that are executed to maintain the stack's
 * integrity.</p>
 *
 * @version $Revision$ $Date$
 * @author Craig R. McClanahan
 */

public interface Context {


    // ----------------------------------------------------- Manifest Constants


    /**
     * The public identifier of the LOCAL scope, which is always defined by a
     * Context implementation.
     */
    public static final int LOCAL_SCOPE = 0;


    /**
     * The maximum number of Scopes (including LOCAL_SCOPE) that can be
     * registered.
     */
    public static final int MAX_SCOPES = 8;



    // --------------------------------------------------- Scope Access Methods


    // These methods provide Steps with the ability to get and set arbitrary
    // objects in arbitrary scopes.  When a scope is not mentioned explicitly,
    // either LOCAL_SCOPE will be used, or scopes will be searched in
    // increasing order of their identifiers, as specified in the various
    // method descriptions.


    /**
     * Does a bean with the specified key exist in any specified scope?
     * Scopes will be searched in ascending order of their identifiers,
     * starting with LOCAL_SCOPE.
     *
     * @param key Key of the bean to be searched for (cannot be null)
     */
    public boolean contains(String key);


    /**
     * Does a bean with the specified key exist in the specified scope?
     *
     * @param key Key of the bean to be searched for (cannot be null)
     * @param scope Identifier of the scope to be returned.
     */
    public boolean contains(String key, int scope);


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
    public Object get(String key);


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
    public Object get(String key, int scope);


    /**
     * Store the specified bean under the specified key, in local scope,
     * replacing any previous value for that key.  Any previous value will
     * be returned.
     *
     * @param key Key of the bean to be stored (cannot be null)
     * @param value Value of the bean to be stored
     */
    public Object put(String key, Object value);


    /**
     * Store the specified bean under the specified key, in the specified
     * scope, replacing any previous value for that key.  Any previous value
     * will be returned.
     *
     * @param key Key of the bean to be stored (cannot be null)
     * @param value Value of the bean to be stored
     * @param scope Identifier of the scope to use for storage
     */
    public Object put(String key, Object value, int scope);


    /**
     * Remove any existing value for the specified key, in any scope.
     * Scopes will be searched in ascending order of their identifiers,
     * starting with LOCAL_SCOPE.  Any previous value for this key will
     * be returned.
     *
     * @param key Key of the bean to be removed
     */
    public Object remove(String key);


    /**
     * Remove any existing value for the specified key, from the specified
     * scope.  Any previous value for this key will be returned.
     *
     * @param key Key of the bean to be removed
     * @param scope Scope the bean to be removed from
     */
    public Object remove(String key, int scope);



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
    public void addScope(int scope, String name, Scope impl);


    /**
     * Return the Scope implementation registered for the specified
     * identifier, if any; otherwise, return <code>null</code>.
     *
     * @param scope Scope identifier to select
     */
    public Scope getScope(int scope);


    /**
     * Return the Scope implementation registered for the specified
     * name, if any; otherwise, return <code>null</code>.
     *
     * @param name Scope name to select
     */
    public Scope getScope(String name);


    /**
     * Return the Scope identifier registered for the specified
     * name, if any; otherwise, return <code>null</code>.
     *
     * @param name Scope name to select
     */
    public int getScopeId(String name);


    // ----------------------------------------------- Evaluation Stack Methods


    /**
     * Clear the evaluation stack.
     */
    public void clear();


    /**
     * Is the evaluation stack currently empty?
     */
    public boolean isEmpty();


    /**
     * Return the top item from the evaluation stack without removing it.
     *
     * @exception EmptyStackException if the stack is empty
     */
    public Object peek() throws EmptyStackException;


    /**
     * Pop and return the top item from the evaluation stack.
     *
     * @exception EmptyStackException if the stack is empty
     */
    public Object pop() throws EmptyStackException;


    /**
     * Push a new item onto the top of the evaluation stack.
     *
     * @param item New item to be pushed
     */
    public void push(Object item);


    // ----------------------------------------------- BlockState Stack Methods


    /**
     * Clear the BlockState stack.
     */
    public void clearBlockState();


    /**
     * Is the BlockState stack currently empty?
     */
    public boolean isEmptyBlockState();


    /**
     * Return the top item from the BlockState stack without removing it.
     *
     * @exception EmptyStackException if the stack is empty
     */
    public BlockState peekBlockState() throws EmptyStackException;


    /**
     * Pop and return the top item from the BlockState stack.
     *
     * @exception EmptyStackException if the stack is empty
     */
    public BlockState popBlockState() throws EmptyStackException;


    /**
     * Push a new item onto the top of the BlockState stack.
     *
     * @param item New item to be pushed
     */
    public void pushBlockState(BlockState item);


    // ---------------------------------------------- Dynamic Execution Methods


    // These methods are used to request the dynamic execution of the
    // Steps related to a particular Activity.


    /**
     * <p>Save the execution state (i.e. the currently assigned next step)
     * of the Activity we are currently executing, and begin executing the
     * specified Activity.  When that Activity exits (either normally
     * or by throwing an exception), the previous Activity will be resumed
     * where it left off.
     *
     * @param activity The Activity to be called
     */
    public void call(Activity activity);


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
    public void execute() throws StepException;


    /**
     * <p>Return the <code>Activity</code> we will be executing when the
     * <code>execute()</code> method is called, if any.</p>
     */
    public Activity getActivity();


    /**
     * Return the JXPathContext object that represents a unified namespace
     * covering all of our registered <code>Scopes</code>.
     */
    public JXPathContext getJXPathContext();


    /**
     * <p>Return the <code>Step</code> that will be executed the next time
     * that <code>execute()</code> is called, if any.</p>
     */
    public Step getNextStep();


    /**
     * <p>Return the suspend flag.</p>
     */
    public boolean getSuspend();


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
    public void setNextStep(Step nextStep);


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
    public void setActivity(Activity activity);


    /**
     * <p>Set the suspend flag.  This is called by a <code>Step</code> that
     * wants to signal the <code>Context</code> to return control to the
     * caller of the <code>execute()</code> method before executing the
     * next <code>Step</code> in the current activity.</p>
     *
     * @param suspend The new suspend flag
     */
    public void setSuspend(boolean suspend);


    // ------------------------------------------------- Event Listener Methods


    /**
     * Add a listener that is notified each time beans are added,
     * replaced, or removed in this context.
     *
     * @param listener The ContextListener to be added
     */
    public void addContextListener(ContextListener listener);


    /**
     * Remove a listener that is notified each time beans are added,
     * replaced, or removed in this context.
     *
     * @param listener The ContextListener to be removed
     */
    public void removeContextListener(ContextListener listener);


}
