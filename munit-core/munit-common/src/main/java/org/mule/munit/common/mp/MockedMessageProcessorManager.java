/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.munit.common.mp;

import org.mule.modules.interceptor.processors.MessageProcessorCall;
import org.mule.modules.interceptor.processors.MessageProcessorId;
import org.mule.modules.interceptor.processors.MessageProcessorManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * The Class that manages the mocking process. Gets the behaviors, stores the message processor calls and stores
 * the spy process
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.3.2
 */
public class MockedMessageProcessorManager extends MessageProcessorManager
{

    public static String ID = "_muleMockMpManager";

    /**
     * <p>
     * These are the real calls of the message processors.
     * </p>
     */
    protected List<MunitMessageProcessorCall> calls = new LinkedList<MunitMessageProcessorCall>();


    /**
     * <p>
     * The spy assertions that are ran before a message processor call
     * </p>
     */
    protected List<SpyAssertion> beforeCallSpyAssertions = new LinkedList<SpyAssertion>();

    /**
     * <p>
     * The spy assertions that are ran after a message processor call
     * </p>
     */
    protected List<SpyAssertion> afterCallSpyAssertions = new LinkedList<SpyAssertion>();

    /**
     * <p>
     * Reset all the status
     * </p>
     */
    public synchronized void reset()
    {
        behaviors.clear();
        calls.clear();
        beforeCallSpyAssertions.clear();
        afterCallSpyAssertions.clear();
    }

    /**
     * <p>
     * Retrieve all the execute calls for a message processor that satisfies the attribute matchers
     * </p>
     *
     * @param mpId               The Message processor Id
     * @param attributesMatchers The attributes that the message processor must match
     * @return The List of message processor calls
     */
    public synchronized List<MessageProcessorCall> findCallsFor(MessageProcessorId mpId, Map<String, Object> attributesMatchers)
    {
        List<MessageProcessorCall> expected = new ArrayList<MessageProcessorCall>();
        MessageProcessorCall matchingCall = new MessageProcessorCall(mpId);
        matchingCall.setAttributes(attributesMatchers);
        for (MessageProcessorCall call : calls)
        {
            if (matchingCall.matchingWeight(call) >= 0)
            {
                expected.add(call);
            }
        }
        return expected;
    }

    /**
     * <p>
     * Gets the best matching Before Spy assertion.
     * </p>
     *
     * @param messageProcessorCall The comparing call
     * @return The best matching Before spy assertion
     */
    public synchronized SpyAssertion getBetterMatchingBeforeSpyAssertion(MessageProcessorCall messageProcessorCall)
    {
        return getBetterMatchingAction(messageProcessorCall, beforeCallSpyAssertions);
    }


    /**
     * <p>
     * Gets the best matching After Spy assertion.
     * </p>
     *
     * @param messageProcessorCall The comparing call
     * @return The best matching After spy assertion
     */
    public synchronized SpyAssertion getBetterMatchingAfterSpyAssertion(MessageProcessorCall messageProcessorCall)
    {
        return getBetterMatchingAction(messageProcessorCall, afterCallSpyAssertions);
    }

    public synchronized void addCall(MunitMessageProcessorCall call)
    {
        calls.add(call);
    }

    public synchronized void addBeforeCallSpyAssertion(SpyAssertion spyAssertion)
    {
        beforeCallSpyAssertions.add(spyAssertion);
    }

    public synchronized void addAfterCallSpyAssertion(SpyAssertion spyAssertion)
    {
        afterCallSpyAssertions.add(spyAssertion);
    }

    public synchronized List<MunitMessageProcessorCall> getCalls()
    {
        return new LinkedList<MunitMessageProcessorCall>(calls);
    }
}
