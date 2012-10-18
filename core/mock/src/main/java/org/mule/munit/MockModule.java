/**
 * This file was automatically generated by the Mule Development Kit
 */
package org.mule.munit;

import org.mule.MessageExchangePattern;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.NestedProcessor;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Optional;
import org.mule.api.config.MuleProperties;
import org.mule.api.context.MuleContextAware;
import org.mule.api.processor.MessageProcessor;
import org.mule.construct.Flow;
import org.mule.munit.endpoint.MockEndpointManager;
import org.mule.munit.endpoint.OutboundBehavior;
import org.mule.munit.mp.MockMpManager;
import org.mule.munit.mp.MpBehavior;
import org.mule.tck.MuleTestUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Generic module
 *
 * @author MuleSoft, Inc.
 */
@Module(name="mock", schemaVersion="3.3")
public class MockModule implements MuleContextAware
{
    private MuleContext muleContext;

    private static String getStackTrace(Throwable throwable) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        return writer.toString();
    }


    /**
     * <p>Define what the mock must return on a message processor call.</p>
     * <p/>
     * <p>If the message processor doesn't return any value then there is no need to define an expect.</p>
     * <p/>
     * <p>You can define the message processor parameters in the same order they appear in the API documentation. In
     * order to define the behaviour on that particular case.</p>
     * <p/>
     * {@sample.xml ../../../doc/mock-connector.xml.sample mock:expect}
     *
     * @param thatMessageProcessor Message processor name.
     * @param toReturn             Expected return value.
     * @param toReturnResponseFrom The flow name that creates the expected result
     * @param parameters           Message processor parameters.
     */
    @Processor
    public void expect(String thatMessageProcessor,
                       @Optional Map<String, Object> parameters,
                       @Optional final Object toReturn,
                       @Optional String toReturnResponseFrom) {
        try {
            final Object expectedObject = toReturn != null ? toReturn : getResultOf(toReturnResponseFrom);
            MockMpManager manager = (MockMpManager) muleContext.getRegistry().lookupObject(MockMpManager.ID);
            manager.addBehavior(new MpBehavior(getName(thatMessageProcessor), getNamespace(thatMessageProcessor), parameters, expectedObject));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getNamespace(String when) {
        String[] split = when.split(":");
        if (split.length > 1) {
            return split[0];
        }

        return "mule";
    }

    private String getName(String when) {
        String[] split = when.split(":");
        if (split.length > 1) {
            return split[1];
        }

        return split[0];
    }


    /**
     * <p>Expect to throw an exception when message processor is called. </p>
     * <p/>
     * {@sample.xml ../../../doc/mock-connector.xml.sample mock:expectFail}
     *
     * @param throwA Java Exception full qualified name.
     * @param when   Message processor name.
     */
//    @Processor
    public void expectFail(String when, String throwA) {
//        try {
//
//            MockedMethod mockedMethod = getMockedMethod(when);
//            Method method = mockedMethod.getMethod();
//
//            if ( method != null  ){
//                when(method.invoke(mock, mockedMethod.getAnyParameters(new HashMap<Integer, Object>())))
//                        .thenThrow((Throwable) Class.forName(throwA).newInstance());
//            }
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }


    /**
     * Check that the message processor was called with some specified parameters
     * <p/>
     * {@sample.xml ../../../doc/mock-connector.xml.sample mock:verifyCall}
     *
     * @param messageProcessor Message processor Id
     * @param parameters       Message processor parameters.
     * @param times            Number of times the message processor has to be called
     * @param atLeast          Number of time the message processor has to be called at least.
     * @param atMost           Number of times the message processor has to be called at most.
     */
//    @Processor
    public void verifyCall(String messageProcessor, Map<String, Object> parameters, @Optional Integer times,
                           @Optional Integer atLeast, @Optional Integer atMost) {
//        try {
//
//            MockedMethod mockedMethod = getMockedMethod(messageProcessor);
//            Method method = mockedMethod.getMethod();
//
//            if ( method != null  ){
//                if ( times != null  )
//                    verify(mock, times(times));
//                else if ( atLeast != null )
//                    verify(mock, atLeast(atLeast));
//                else if ( atMost != null )
//                    verify(mock, atMost(atMost));
//                else
//                    verify(mock);
//
//
//                Map<Integer, Object> parameterIndex = mockedMethod.getParameters(parameters);
//
//                method.invoke(mock, mockedMethod.getAnyParameters(parameterIndex));
//            }
//
//        } catch (InvocationTargetException e) {
//            fail("Verification Error:" + getStackTrace(e));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }


    /**
     * Throw an Exception when a connector tries to connect.
     * <p/>
     * {@sample.xml ../../../doc/mock-connector.xml.sample mock:failOnConnect}
     */
//    @Processor
    public void failOnConnect() {
//        try {
//            doThrow(new Exception()).when(connectionManagerMock).acquireConnection(any());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    /**
     * Reset mock behaviour
     * <p/>
     * {@sample.xml ../../../doc/mock-connector.xml.sample mock:reset}
     */
//    @Processor
    public void reset() {
//        if (connectionManagerMock != null && connectionKeyClass !=null){
//            setConnectionManagerDefaultBehaviour();
//        }
//
//        Mockito.reset(mock);
    }


    @Override
    public void setMuleContext(MuleContext muleContext) {
        this.muleContext = muleContext;
    }



    /**
     * Reset mock behaviour
     *
     * {@sample.xml ../../../doc/mock-connector.xml.sample mock:outboundEndpoint}
     *
     * @param address the address
     * @param returnPayload the Return Payload
     * @param returnInboundProperties inbound properties
     * @param returnInvocationProperties invocation properties
     * @param returnSessionProperties invocation session properties
     * @param returnOutboundProperties oubound properties
     * @param assertions assertions
     */
    @Processor
    public void outboundEndpoint(String address,
                                 @Optional Object returnPayload,
                                 @Optional Map<String, Object> returnInvocationProperties,
                                 @Optional Map<String, Object> returnInboundProperties,
                                 @Optional Map<String, Object> returnSessionProperties,
                                 @Optional Map<String, Object> returnOutboundProperties,
                                 @Optional List<NestedProcessor> assertions) {

        MockEndpointManager factory = (MockEndpointManager) muleContext.getRegistry().lookupObject(MuleProperties.OBJECT_MULE_ENDPOINT_FACTORY);

        OutboundBehavior behavior = new OutboundBehavior(returnPayload, createMessageProcessorsFrom(assertions));

        behavior.setInboundProperties(returnInboundProperties);
        behavior.setInvocationProperties(returnInvocationProperties);
        behavior.setOutboundProperties(returnOutboundProperties);
        behavior.setSessionProperties(returnSessionProperties);

        factory.addExpect(address, behavior);
    }

    private List<MessageProcessor> createMessageProcessorsFrom(List<NestedProcessor> assertions) {
        if (assertions == null) {
            return null;
        }


        List<MessageProcessor> mps = new ArrayList<MessageProcessor>();
        for (NestedProcessor nestedProcessor : assertions) {
            mps.add(new NestedMessageProcessor(nestedProcessor));
        }

        return mps;
    }


    private Object getResultOf(String mustReturnResponseFrom) {

        try {
            Flow flow = (Flow) muleContext.getRegistry().lookupFlowConstruct(mustReturnResponseFrom);
            if (flow == null) {
                throw new RuntimeException("Flow " + mustReturnResponseFrom + " does not exist");
            }

            MuleEvent process = flow.process(testEvent());
            return process.getMessage().getPayload();
        } catch (Exception e) {
            throw new RuntimeException("Could not call flow " + mustReturnResponseFrom + " to get the expected result");
        }
    }

    private MuleEvent testEvent() throws Exception {
        return MuleTestUtils.getTestEvent(null, MessageExchangePattern.REQUEST_RESPONSE, muleContext);
    }

}
