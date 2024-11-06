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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.nabu.eai.developer.managers.base.BaseGUIManager;
import be.nabu.eai.developer.managers.util.SimpleProperty;
import be.nabu.eai.module.services.vm.VMServiceGUIManager;
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
		return "Services";
	}

	@Override
	public String getArtifactName() {
		return "Blox Authorization Service";
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
