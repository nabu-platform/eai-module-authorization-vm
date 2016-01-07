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
