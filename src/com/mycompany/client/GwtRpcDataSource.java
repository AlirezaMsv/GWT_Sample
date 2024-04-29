package com.holisoft.holiframework.client.data.hfw;


import com.holisoft.holiframework.shared.data.hfw.SerializableResultSet;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.HandleErrorCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSProtocol;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.ValueCallback;

/**
 * Data source with ability to communicate with server by GWT RPC.<p/>
 * SmartClient natively supports data protocol "clientCustom". This protocol
 * means that communication with server should be implemented in
 * <code>transformRequest (DSRequest request)</code> method. Here is a few
 * things to note on <code>transformRequest</code> implementation:
 * <ul>
 * <li><code>DSResponse</code> object has to be created and
 * <code>processResponse (requestId, response)</code> must be called to finish
 * data request. <code>requestId</code> should be taken from original
 * <code>DSRequest.getRequestId ()</code>.</li>
 * <li>"clientContext" attribute from <code>DSRequest</code> should be copied to
 * <code>DSResponse</code>.</li>
 * <li>In case of failure <code>DSResponse</code> should contain at least "status"
 * attribute with error code (&lt;0).</li>
 * <li>In case of success <code>DSResponse</code> should contain at least "data"
 * attribute with operation type specific data:
 *  <ul>
 *      <li>FETCH - <code>ListGridRecord[]</code> retrieved records.</li>
 *      <li>ADD - <code>ListGridRecord[]</code> with single added record.
 *          Operation is called on every newly added record.</li>
 *      <li>UPDATE - <code>ListGridRecord[]</code> with single updated record.
 *          Operation is called on every updated record.</li>
 *      <li>REMOVE - <code>ListGridRecord[]</code> with single removed record.
 *          Operation is called on every removed record.</li>
 *  </ul>
 * </li>
 * </ul>
 *
 * @author Aleksandras Novikovas
 * @author System Tier
 * @version 1.0
 */
public abstract class GwtRpcDataSource
    extends DataSource {
	
	public static enum DSEvent {
		Fetch,
		FetchCompleted,
		Add,
		AddCompleted,
		Update,
		UpdateCompleted,
		Delete,
		DeleteCompleted,
	};
	
	protected ValueCallback eventsCallback;

    /**
     * Creates new data source which communicates with server by GWT RPC.
     * It is normal server side SmartClient data source with data protocol
     * set to <code>DSProtocol.CLIENTCUSTOM</code> ("clientCustom" - natively
     * supported by SmartClient but should be added to smartGWT) and with data
     * format <code>DSDataFormat.CUSTOM</code>.
     */
    public GwtRpcDataSource () {
        setDataProtocol (DSProtocol.CLIENTCUSTOM);
        setDataFormat (DSDataFormat.CUSTOM);
        setClientOnly (false);
    }

    /**
     * Executes request to server.
     *
     * @param request <code>DSRequest</code> being processed.
     * @return <code>Object</code> data from original request.
     */
    @Override
    protected Object transformRequest (final DSRequest request) {
        String requestId = request.getRequestId ();
        DSResponse response = new DSResponse ();
        
        response.setAttribute ("clientContext", request.getAttributeAsObject ("clientContext"));
        // Asume success
        response.setStatus (0);
        switch (request.getOperationType ()) {
            case FETCH:
                executeFetch (requestId, request, response);
                onFetchCompleted(requestId, request, response);
                break;
            case ADD:
                if(beforeAdd(requestId, request, response))
                {
                	doAdd(requestId, request, response);
                }
                break;
            case UPDATE:
                if(beforeUpdate(requestId, request, response))
                {
                	doUpdate(requestId, request, response);
                }
                break;
            case REMOVE:
                executeRemove (requestId, request, response);
                onDeleteCompleted(requestId, request, response);
                break;
            case CUSTOM:
            	processResponse(requestId, new DSResponse());
            	break;
            default:
                // Operation not implemented.
                break;
        }
        return request.getData ();
    }
    
    public boolean beforeAdd(final String requestId, final DSRequest request, final DSResponse response)
    {
    	if(preAddCheck == null)
    	{
    		return true;
    	}
    	
    	preAddCheck.check(request, b -> {
    		if(b)
    		{
    			doAdd(requestId, request, response);
    		}
    	});
    	
    	return false;
    }
    
    private void doAdd(final String requestId, final DSRequest request, final DSResponse response)
    {
    	executeAdd (requestId, request, response);
        onAddCompleted(requestId, request, response);
    }
    
    public boolean beforeUpdate(final String requestId, final DSRequest request, final DSResponse response)
    {
    	if(preUpdateCheck == null)
    	{
    		return true;
    	}

    	preUpdateCheck.check(request, b -> {
    		if(b)
    		{
    			doUpdate(requestId, request, response);
    		}
    	});
    	
    	return false;
    }
    
    private void doUpdate(final String requestId, final DSRequest request, final DSResponse response)
    {
    	executeUpdate (requestId, request, response);
        onUpdateCompleted(requestId, request, response);
    }

    protected void onFetchCompleted(final String requestId, final DSRequest request, final DSResponse response){}
    protected void onAddCompleted(final String requestId, final DSRequest request, final DSResponse response){}
    protected void onUpdateCompleted(final String requestId, final DSRequest request, final DSResponse response){}
    protected void onDeleteCompleted(final String requestId, final DSRequest request, final DSResponse response){}

    /**
     * Executed on <code>FETCH</code> operation. <code>processResponse (requestId, response)</code>
     * should be called when operation completes (either successful or failure).
     *
     * @param requestId <code>String</code> extracted from <code>DSRequest.getRequestId ()</code>.
     * @param request <code>DSRequest</code> being processed.
     * @param response <code>DSResponse</code>. <code>setData (list)</code> should be called on
     *      successful execution of this method. <code>setStatus (&lt;0)</code> should be called
     *      on failure.
     */
    protected abstract void executeFetch (final String requestId, final DSRequest request, final DSResponse response);

    /**
     * Executed on <code>ADD</code> operation. <code>processResponse (requestId, response)</code>
     * should be called when operation completes (either successful or failure).
     *
     * @param requestId <code>String</code> extracted from <code>DSRequest.getRequestId ()</code>.
     * @param request <code>DSRequest</code> being processed. <code>request.getData ()</code>
     *      contains record should be added.
     * @param response <code>DSResponse</code>. <code>setData (list)</code> should be called on
     *      successful execution of this method. Array should contain single element representing
     *      added row. <code>setStatus (&lt;0)</code> should be called on failure.
     */
    protected abstract void executeAdd (final String requestId, final DSRequest request, final DSResponse response);

    /**
     * Executed on <code>UPDATE</code> operation. <code>processResponse (requestId, response)</code>
     * should be called when operation completes (either successful or failure).
     *
     * @param requestId <code>String</code> extracted from <code>DSRequest.getRequestId ()</code>.
     * @param request <code>DSRequest</code> being processed. <code>request.getData ()</code>
     *      contains record should be updated.
     * @param response <code>DSResponse</code>. <code>setData (list)</code> should be called on
     *      successful execution of this method. Array should contain single element representing
     *      updated row. <code>setStatus (&lt;0)</code> should be called on failure.
     */
    protected abstract void executeUpdate (final String requestId, final DSRequest request, final DSResponse response);

    /**
     * Executed on <code>REMOVE</code> operation. <code>processResponse (requestId, response)</code>
     * should be called when operation completes (either successful or failure).
     *
     * @param requestId <code>String</code> extracted from <code>DSRequest.getRequestId ()</code>.
     * @param request <code>DSRequest</code> being processed. <code>request.getData ()</code>
     *      contains record should be removed.
     * @param response <code>DSResponse</code>. <code>setData (list)</code> should be called on
     *      successful execution of this method. Array should contain single element representing
     *      removed row. <code>setStatus (&lt;0)</code> should be called on failure.
     */
    protected abstract void executeRemove (final String requestId, final DSRequest request, final DSResponse response);
    
    static{
    	RPCManager.setHandleErrorCallback(new HandleErrorCallback() {
			
			@Override
			public void handleError(DSResponse response, DSRequest request) {
				//SC.say("Hi");
			}
		});
    }
    
    protected void operationFailed(final String requestId, final DSResponse response){
    	try{
    		response.setData(new Record[]{});
			response.setStatus(RPCResponse.STATUS_FAILURE);
			processResponse(requestId, response);
    	}catch(Exception ex){}
    }
    
    private String dataSourceFieldsPrefix = "";
    private boolean clearDataSourceFieldsPrefixAfterCopy;
    public void setDataSourceFieldsPrefix(String prefix, boolean clearAfterCopy){
    	this.dataSourceFieldsPrefix = prefix;
    	this.clearDataSourceFieldsPrefixAfterCopy = clearAfterCopy;
    }
    
    public String getDataSourceFieldsPrefix(){
    	return this.dataSourceFieldsPrefix;
    }
    
    protected void copyValues(final SerializableResultSet from, final Record to) {
    	
		String[] fields = this.getFieldNames();
		/*if(fields == null || fields.length==0){
			fields = from.getColumnsOfCurrentRow();
		}*/
		for(String field : fields){
			try{
				if(!from.hasColumn(dataSourceFieldsPrefix + field)){
					continue;
				}
				FieldType ftype = getField(field).getType();
				if(ftype==FieldType.INTEGER){
					to.setAttribute(dataSourceFieldsPrefix + field, from.getInt(field));
				}else if(ftype==FieldType.FLOAT){
					to.setAttribute(dataSourceFieldsPrefix + field, from.getFloat(field));
				}else if(ftype==FieldType.BOOLEAN){
					String tmp = from.getString(field);
					to.setAttribute(dataSourceFieldsPrefix + field, "true".equalsIgnoreCase(tmp) || "1".equals(tmp));
				}else if(ftype==FieldType.DATETIME || ftype==FieldType.DATE){
					to.setAttribute(dataSourceFieldsPrefix + field, from.getPersianPrintableDatetime(field));
				}else{
					to.setAttribute(dataSourceFieldsPrefix + field, from.getObject(field));
				}
			}catch(Exception ex){
				
			}
		}
		if(this.clearDataSourceFieldsPrefixAfterCopy){
			this.dataSourceFieldsPrefix = "";
		}
	
    }
    
    @FunctionalInterface
    public static interface PreOperation{
    	
    	void check(DSRequest dsRequest, BooleanCallback onFinish);
    }
    
    private PreOperation preAddCheck;
    private PreOperation preUpdateCheck;

	public PreOperation getPreAddCheck() {
		return preAddCheck;
	}

	public PreOperation getPreUpdateCheck() {
		return preUpdateCheck;
	}

	public void setPreAddCheck(PreOperation preAddCheck) {
		this.preAddCheck = preAddCheck;
	}

	public void setPreUpdateCheck(PreOperation preUpdateCheck) {
		this.preUpdateCheck = preUpdateCheck;
	}

}
