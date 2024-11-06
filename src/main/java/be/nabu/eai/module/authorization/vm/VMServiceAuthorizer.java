/*
* Copyright (C) 2016 Alexander Verbruggen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

package be.nabu.eai.module.authorization.vm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.nabu.libs.services.ServiceRuntime;
import be.nabu.libs.services.api.ServiceAuthorizer;
import be.nabu.libs.services.api.ServiceException;
import be.nabu.libs.types.api.ComplexContent;

public class VMServiceAuthorizer implements ServiceAuthorizer {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private VMAuthorizationService authorizationService;

	public VMServiceAuthorizer(VMAuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}
	
	@Override
	public boolean canRun(ServiceRuntime runtime, ComplexContent input) {
		ServiceRuntime child = new ServiceRuntime(authorizationService, runtime.getExecutionContext());
		try {
			ComplexContent output = child.run(input);
			return output == null || output.get("isAllowed") == null || (Boolean) output.get("isAllowed");
		}
		catch (ServiceException e) {
			logger.error("Could not check security using: " + authorizationService.getId(), e);
			return false;
		}
	}

}
