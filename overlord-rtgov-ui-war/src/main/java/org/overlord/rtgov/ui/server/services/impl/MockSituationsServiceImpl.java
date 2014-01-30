/*
 * Copyright 2013 JBoss Inc
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
package org.overlord.rtgov.ui.server.services.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.TreeSet;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import org.overlord.rtgov.ui.client.model.CallTraceBean;
import org.overlord.rtgov.ui.client.model.Constants;
import org.overlord.rtgov.ui.client.model.MessageBean;
import org.overlord.rtgov.ui.client.model.SituationBean;
import org.overlord.rtgov.ui.client.model.SituationResultSetBean;
import org.overlord.rtgov.ui.client.model.SituationSummaryBean;
import org.overlord.rtgov.ui.client.model.SituationsFilterBean;
import org.overlord.rtgov.ui.client.model.TraceNodeBean;
import org.overlord.rtgov.ui.client.model.UiException;
import org.overlord.rtgov.ui.server.services.ISituationsServiceImpl;

/**
 * Concrete implementation of the faults service.
 *
 * @author eric.wittmann@redhat.com
 */
@ApplicationScoped
@Alternative
public class MockSituationsServiceImpl implements ISituationsServiceImpl {

    /**
     * Constructor.
     */
    public MockSituationsServiceImpl() {
    }
    
    /**
     * @see org.overlord.rtgov.ui.server.services.ISituationsServiceImpl#search(SituationsFilterBean, int, String, boolean)
     */
    @Override
    public SituationResultSetBean search(SituationsFilterBean filters, int page, String sortColumn,
            boolean ascending) throws UiException {
        SituationResultSetBean rval = new SituationResultSetBean();
        ArrayList<SituationSummaryBean> situations = new ArrayList<SituationSummaryBean>();
        rval.setSituations(situations);
        rval.setItemsPerPage(20);
        rval.setStartIndex(0);
        rval.setTotalResults(4);

        SituationSummaryBean situation = new SituationSummaryBean();
        situation.setSituationId("1"); //$NON-NLS-1$
        situation.setSeverity("critical"); //$NON-NLS-1$
        situation.setType("Rate Limit Exceeded"); //$NON-NLS-1$
        situation.setSubject("{urn:namespace}ImportantService|VeryImportantOperation"); //$NON-NLS-1$
        situation.setTimestamp(new Date());
        situation.setDescription("Some description of the Situation goes here in this column so that it can be read by the user."); //$NON-NLS-1$
        situation.getProperties().put("Property-1", "Property one Value"); //$NON-NLS-1$ //$NON-NLS-2$
        situation.getProperties().put("Property-2", "Property two Value"); //$NON-NLS-1$ //$NON-NLS-2$
        situation.getProperties().put("Property-3", "Property three Value"); //$NON-NLS-1$ //$NON-NLS-2$
        situations.add(situation);

        situation = new SituationSummaryBean();
        situation.setSituationId("2"); //$NON-NLS-1$
        situation.setSeverity("high"); //$NON-NLS-1$
        situation.setType("SLA Violation"); //$NON-NLS-1$
        situation.setSubject("{urn:namespace}ServiceA|OperationB"); //$NON-NLS-1$
        situation.setTimestamp(new Date());
        situation.setDescription("Some description of the Situation goes here in this column so that it can be read by the user."); //$NON-NLS-1$
        situations.add(situation);

        situation = new SituationSummaryBean();
        situation.setSituationId("3"); //$NON-NLS-1$
        situation.setSeverity("high"); //$NON-NLS-1$
        situation.setType("SLA Violation"); //$NON-NLS-1$
        situation.setSubject("{urn:namespace}ServiceA|OperationB"); //$NON-NLS-1$
        situation.setTimestamp(new Date());
        situation.setDescription("Some description of the Situation goes here in this column so that it can be read by the user."); //$NON-NLS-1$
        situation.getProperties().put("Property-1", "Property one Value"); //$NON-NLS-1$ //$NON-NLS-2$
        situation.getProperties().put("Property-2", "Property two Value"); //$NON-NLS-1$ //$NON-NLS-2$
        situations.add(situation);

        situation = new SituationSummaryBean();
        situation.setSituationId("4"); //$NON-NLS-1$
        situation.setSeverity("low"); //$NON-NLS-1$
        situation.setType("Rate Limit Approaching"); //$NON-NLS-1$
        situation.setSubject("{urn:namespace}SomeService|AnotherOperation"); //$NON-NLS-1$
        situation.setTimestamp(new Date());
        situation.setDescription("Some description of the Situation goes here in this column so that it can be read by the user."); //$NON-NLS-1$
        situations.add(situation);
        
        sort(situations, sortColumn, ascending);

        return rval;
    }

    /**
     * Sorts the list of situations.
     * @param situations
     * @param sortColumn
     * @param ascending
     */
    private void sort(ArrayList<SituationSummaryBean> situations, final String sortColumn, final boolean ascending) {
        TreeSet<SituationSummaryBean> sorted = new TreeSet<SituationSummaryBean>(new Comparator<SituationSummaryBean>() {
            @SuppressWarnings("unchecked")
            @Override
            public int compare(SituationSummaryBean sit1, SituationSummaryBean sit2) {
                Comparable<?> c1;
                Comparable<?> c2;
                if (sortColumn.equals(Constants.SORT_COLID_TYPE)) {
                    c1 = sit1.getType();
                    c2 = sit2.getType();
                } else if (sortColumn.equals(Constants.SORT_COLID_SUBJECT)) {
                    c1 = sit1.getSubject();
                    c2 = sit2.getSubject();
                } else {
                    c1 = sit1.getTimestamp();
                    c2 = sit2.getTimestamp();
                }
                int rval = 0;
                if (ascending) {
                    rval = ((Comparable<Object>) c1).compareTo(c2);
                } else {
                    rval = ((Comparable<Object>) c2).compareTo(c1);
                }
                if (rval == 0) {
                    rval = sit1.getSituationId().compareTo(sit2.getSituationId());
                }
                return rval;
            }
        });
        sorted.addAll(situations);
        situations.clear();
        situations.addAll(sorted);
    }

    /**
     * @see org.overlord.rtgov.ui.server.services.ISituationsServiceImpl#getService(java.lang.String)
     */
    @Override
    public SituationBean get(String situationId) throws UiException {
        SituationBean situation = new SituationBean();
        situation.setSituationId("1"); //$NON-NLS-1$
        situation.setSeverity("critical"); //$NON-NLS-1$
        situation.setType("Rate Limit Exceeded"); //$NON-NLS-1$
        situation.setSubject("{urn:namespace}ImportantService|VeryImportantOperation"); //$NON-NLS-1$
        situation.setTimestamp(new Date());
        situation.setDescription("Some description of the Situation goes here in this column so that it can be read by the user."); //$NON-NLS-1$
        situation.getProperties().put("Property-1", "Property one Value"); //$NON-NLS-1$ //$NON-NLS-2$
        situation.getProperties().put("Property-2", "Property two Value"); //$NON-NLS-1$ //$NON-NLS-2$
        situation.getProperties().put("Property-3", "Property three Value"); //$NON-NLS-1$ //$NON-NLS-2$
        situation.getContext().put("Context-1", "This is the value of the context 1 property."); //$NON-NLS-1$ //$NON-NLS-2$
        situation.getContext().put("Context-2", "This is the value of the context 2 property."); //$NON-NLS-1$ //$NON-NLS-2$

        MessageBean message = createMockMessage();
        situation.setMessage(message);
        
        CallTraceBean callTrace = createMockCallTrace();
        situation.setCallTrace(callTrace);

        return situation;
    }

    /**
     * Creates a mock message.
     */
    private MessageBean createMockMessage() {
        String msgContent = "<collection>\r\n" +  //$NON-NLS-1$
                "<asset>\r\n" +  //$NON-NLS-1$
                "  <author>krisv</author>\r\n" +  //$NON-NLS-1$
                "  <binaryContentAttachmentFileName></binaryContentAttachmentFileName>\r\n" +  //$NON-NLS-1$
                "  <binaryLink>http://localhost:8080/drools-guvnor/rest/packages/srampPackage/assets/Evaluation/binary\r\n" +  //$NON-NLS-1$
                "  </binaryLink>\r\n" +  //$NON-NLS-1$
                "  <description></description>\r\n" +  //$NON-NLS-1$
                "  <metadata>\r\n" +  //$NON-NLS-1$
                "   <checkInComment>&lt;content from webdav&gt;</checkInComment>\r\n" +  //$NON-NLS-1$
                "   <created>2012-10-05T14:34:14.970-04:00</created>\r\n" +  //$NON-NLS-1$
                "   <disabled>false</disabled>\r\n" +  //$NON-NLS-1$
                "   <format>bpmn</format>\r\n" +  //$NON-NLS-1$
                "   <note>&lt;![CDATA[ &lt;content from webdav&gt; ]]&gt;</note>\r\n" +  //$NON-NLS-1$
                "   <state>Draft</state>\r\n" +  //$NON-NLS-1$
                "   <uuid>09512d48-585d-4393-86e2-39418369f066</uuid>\r\n" +  //$NON-NLS-1$
                "   <versionNumber>9</versionNumber>\r\n" +  //$NON-NLS-1$
                "  </metadata>\r\n" +  //$NON-NLS-1$
                "  <published>2012-10-05T15:11:32.923-04:00</published>\r\n" +  //$NON-NLS-1$
                "  <refLink>http://localhost:8080/drools-guvnor/rest/packages/srampPackage/assets/Evaluation\r\n" +  //$NON-NLS-1$
                "  </refLink>\r\n" +  //$NON-NLS-1$
                "  <sourceLink>http://localhost:8080/drools-guvnor/rest/packages/srampPackage/assets/Evaluation/source\r\n" +  //$NON-NLS-1$
                "  </sourceLink>\r\n" +  //$NON-NLS-1$
                "  <title>Evaluation</title>\r\n" +  //$NON-NLS-1$
                " </asset>\r\n" +  //$NON-NLS-1$
                "</asset>"; //$NON-NLS-1$
        MessageBean msg = new MessageBean();
        msg.setContent(msgContent);
        return msg;
    }

    /**
     * Creates a mock call trace!
     */
    protected CallTraceBean createMockCallTrace() {
        CallTraceBean callTrace = new CallTraceBean();

        TraceNodeBean rootNode = createTraceNode("Call", "Success", "urn:switchyard:parent", "submitOrder", 47, 100); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        callTrace.getTasks().add(rootNode);

        TraceNodeBean childNode = createTraceNode("Call", "Success", "urn:switchyard:application", "lookupItem", 10, 55); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        rootNode.getTasks().add(childNode);
        TraceNodeBean leafNode = createTraceNode("Task", "Success", null, null, 3, 30); //$NON-NLS-1$ //$NON-NLS-2$
        leafNode.setDescription("Information: Found the item."); //$NON-NLS-1$
        childNode.getTasks().add(leafNode);
        leafNode = createTraceNode("Task", "Success", null, null, 7, 70); //$NON-NLS-1$ //$NON-NLS-2$
        leafNode.setDescription("Information: Secured the item."); //$NON-NLS-1$
        childNode.getTasks().add(leafNode);

        childNode = createTraceNode("Call", "Success", "urn:switchyard:application", "deliver", 8, 44); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        childNode.setIface("org.overlord.public.interface.Application"); //$NON-NLS-1$
        childNode.setOperation("doIt"); //$NON-NLS-1$
        childNode.getProperties().put("property-1", "Value for property 1"); //$NON-NLS-1$ //$NON-NLS-2$
        childNode.getProperties().put("property-2", "Value for property 2"); //$NON-NLS-1$ //$NON-NLS-2$
        childNode.getProperties().put("property-3", "Value for property 3"); //$NON-NLS-1$ //$NON-NLS-2$
        childNode.getProperties().put("property-4", "Value for property 4"); //$NON-NLS-1$ //$NON-NLS-2$
        rootNode.getTasks().add(childNode);
        leafNode = createTraceNode("Task", "Success", null, null, 4, 100); //$NON-NLS-1$ //$NON-NLS-2$
        leafNode.setDescription("Information: Delivering the order."); //$NON-NLS-1$
        childNode.getTasks().add(leafNode);

        return callTrace;
    }

    /**
     * Creates a single trace node.
     * @param type
     * @param status
     * @param component
     * @param op
     * @param duration
     * @param percentage
     */
    protected TraceNodeBean createTraceNode(String type, String status, String component, String op, long duration, int percentage) {
        TraceNodeBean node = new TraceNodeBean();
        node.setType(type);
        node.setStatus(status);
        node.setComponent(component);
        node.setOperation(op);
        node.setDuration(duration);
        node.setPercentage(percentage);
        return node;
    }
    
    /**
     * @see org.overlord.rtgov.ui.server.services.ISituationsServiceImpl#resubmit(java.lang.String, java.lang.String)
     */
    @Override
    public void resubmit(String situationId, String message) throws UiException {
        // Do nothing!
        System.out.println("Resubmitted message for situation: " + situationId); //$NON-NLS-1$
        System.out.println(message);
    }

}
