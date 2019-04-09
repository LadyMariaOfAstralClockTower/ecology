package com.ltzy.webservices;

import java.awt.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 采购申请
 * @author user
 *
 */

@WebService
public abstract interface HrmOrgService
{
	@WebMethod(operationName="fetchHrmOrg", action="urn:com.ltzy.webservices.HrmOrgService.fetchHrmOrg")
	public String fetchHrmOrg(@WebParam(name="params") String params);
	
	@WebMethod(operationName="updatePassword", action="urn:com.ltzy.webservices.HrmOrgService.updatePassword")
	public String updatePassword(@WebParam(name="number") String number,@WebParam(name="uiNumber") String uiNumber,@WebParam(name="newPassword") String newPassword);
}
