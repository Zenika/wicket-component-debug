package com.zenika.wicket.component.debug.plugins.component;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;

public class ComponentUtils {

	/**
	 * Extract component informations and return them as a string
	 * 
	 * @param component
	 * @return formated informations : "wicket:id,...;class,...;etc..."
	 */
	static String getInfosAsString(Component component) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("wicket:id,").append(component.getId());
		String componentClass = (component.getClass().getSimpleName().equals("")) ? component.getClass()
				.getSuperclass().getCanonicalName() : component.getClass().getCanonicalName();
		buffer.append(";class,").append(componentClass);
		if (component.getModel() != null) {
			buffer.append(";model,").append(component.getModel().getClass().getCanonicalName());
			try {
				if (component.getModelObject() != null) {
					buffer.append(";object,").append(component.getModelObject().getClass().getCanonicalName());
				}
			} catch (WicketRuntimeException e) {

			}
		}
		return buffer.toString();
	}

}
