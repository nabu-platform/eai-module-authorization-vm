package be.nabu.eai.module.authorization.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.nabu.eai.developer.managers.VMServiceGUIManager;
import be.nabu.eai.developer.managers.base.BaseGUIManager;
import be.nabu.eai.developer.managers.util.SimpleProperty;
import be.nabu.eai.repository.api.ArtifactManager;
import be.nabu.eai.repository.api.Repository;
import be.nabu.libs.property.api.Property;
import be.nabu.libs.property.api.Value;
import be.nabu.libs.services.api.DefinedService;
import be.nabu.libs.services.vm.api.VMService;

public class VMAuthorizationGUIManager extends VMServiceGUIManager {

	public static final String OUTPUT = "isAllowed";
	
	public VMAuthorizationGUIManager() {
		// disable interface management
		Map<String, String> configuration = new HashMap<String, String>();
		configuration.put(VMServiceGUIManager.INTERFACE_EDITABLE, "false");
		setConfiguration(configuration);
	}
	
	@Override
	public ArtifactManager<VMService> getArtifactManager() {
		return new VMAuthorizationManager();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getArtifactClass() {
		return VMAuthorizationService.class;
	}

	@Override
	public String getCategory() {
		return "Authorization";
	}

	@Override
	public String getArtifactName() {
		return "Authorization Service";
	}

	@Override
	protected List<Property<?>> getCreateProperties() {
		List<Property<?>> properties = new ArrayList<Property<?>>();
		properties.add(new SimpleProperty<DefinedService>("Service", DefinedService.class, true));
		return properties;
	}

	@Override
	protected VMService newVMService(Repository repository, String id, Value<?>...values) {
		DefinedService targetService = BaseGUIManager.getValue("Service", DefinedService.class, values);
		if (targetService == null) {
			throw new RuntimeException("No or invalid service id");
		}
		VMAuthorizationService service = new VMAuthorizationService(targetService);
		service.setId(id);
		return service;
	}

}
