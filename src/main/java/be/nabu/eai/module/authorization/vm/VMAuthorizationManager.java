package be.nabu.eai.module.authorization.vm;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import be.nabu.eai.repository.api.ResourceEntry;
import be.nabu.eai.repository.managers.ServiceInterfaceManager;
import be.nabu.eai.repository.managers.VMServiceManager;
import be.nabu.libs.property.ValueUtils;
import be.nabu.libs.resources.ResourceReadableContainer;
import be.nabu.libs.resources.api.ReadableResource;
import be.nabu.libs.services.api.DefinedServiceInterface;
import be.nabu.libs.services.api.ServiceInterface;
import be.nabu.libs.services.vm.Pipeline;
import be.nabu.libs.services.vm.PipelineInterfaceProperty;
import be.nabu.libs.services.vm.api.VMService;
import be.nabu.libs.services.vm.step.Sequence;
import be.nabu.libs.types.SimpleTypeWrapperFactory;
import be.nabu.libs.types.api.ComplexType;
import be.nabu.libs.types.base.SimpleElementImpl;
import be.nabu.libs.types.base.ValueImpl;
import be.nabu.libs.types.structure.Structure;
import be.nabu.libs.validator.api.Validation;

public class VMAuthorizationManager extends VMServiceManager {

	public static class SecurityInterface implements DefinedServiceInterface {
		
		private final DefinedServiceInterface iface;
		private Structure output;

		public SecurityInterface(DefinedServiceInterface iface) {
			this.iface = iface;
		}

		@Override
		public ComplexType getInputDefinition() {
			return iface.getInputDefinition();
		}

		@Override
		public ComplexType getOutputDefinition() {
			if (output == null) {
				output = new Structure();
				output.add(new SimpleElementImpl<Boolean>("isAllowed", SimpleTypeWrapperFactory.getInstance().getWrapper().wrap(Boolean.class), output));
			}
			return output;
		}

		@Override
		public ServiceInterface getParent() {
			return null;
		}

		@Override
		public String getId() {
			return iface.getId();
		}
	}

	@Override
	public VMService load(ResourceEntry entry, List<Validation<?>> messages) throws IOException, ParseException {
		Pipeline pipeline = new ServiceInterfaceManager().loadPipeline(entry, messages);

		// next we load the root sequence
		Sequence sequence = parseSequence(new ResourceReadableContainer((ReadableResource) getResource(entry, "service.xml", false)));
		
		final DefinedServiceInterface iface = ValueUtils.getValue(PipelineInterfaceProperty.getInstance(), pipeline.getProperties());
		if (iface == null) {
			throw new ParseException("Could not find the interface for the security service", 0);
		}
		DefinedServiceInterface rewritten = new SecurityInterface(iface);
		pipeline.setProperty(new ValueImpl<DefinedServiceInterface>(PipelineInterfaceProperty.getInstance(), rewritten));
		
		VMAuthorizationService service = new VMAuthorizationService(pipeline);
		service.setRoot(sequence);
		service.setId(entry.getId());
		return service;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getArtifactClass() {
		return VMAuthorizationService.class;
	}
	
}
