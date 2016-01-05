package be.nabu.eai.module.authorization.vm;

import be.nabu.eai.repository.ServiceInterfaceFromDefinedService;
import be.nabu.libs.property.ValueUtils;
import be.nabu.libs.services.ServiceRuntime;
import be.nabu.libs.services.api.DefinedService;
import be.nabu.libs.services.api.DefinedServiceInterface;
import be.nabu.libs.services.api.ServiceAuthorizer;
import be.nabu.libs.services.api.ServiceAuthorizerProvider;
import be.nabu.libs.services.vm.Pipeline;
import be.nabu.libs.services.vm.PipelineInterfaceProperty;
import be.nabu.libs.services.vm.SimpleVMServiceDefinition;
import be.nabu.libs.types.base.ValueImpl;
import be.nabu.libs.types.structure.Structure;

public class VMAuthorizationService extends SimpleVMServiceDefinition implements ServiceAuthorizerProvider {

	public VMAuthorizationService(Pipeline pipeline) {
		super(pipeline);
	}
	
	public VMAuthorizationService(DefinedService parent) {
		super(new Pipeline(new Structure(), new Structure()));
		// set the service to inherit from
		getPipeline().setProperty(new ValueImpl<DefinedServiceInterface>(PipelineInterfaceProperty.getInstance(), new VMAuthorizationManager.SecurityInterface(new ServiceInterfaceFromDefinedService(parent))));
	}

	@Override
	public ServiceAuthorizer getAuthorizer(ServiceRuntime runtime) {
		DefinedServiceInterface value = ValueUtils.getValue(PipelineInterfaceProperty.getInstance(), getPipeline().getProperties());
		if (value != null && runtime.getService() instanceof DefinedService && value.getId().equals(((DefinedService) runtime.getService()).getId())) {
			return new VMServiceAuthorizer(this);
		}
		return null;
	}
	
}
