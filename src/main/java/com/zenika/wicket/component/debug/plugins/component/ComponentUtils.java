package com.zenika.wicket.component.debug.plugins.component;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;

@SuppressWarnings("serial")
public class ComponentUtils implements Serializable {

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
		if (component.getDefaultModel() != null) {
			buffer.append(";model,").append(component.getDefaultModel().getClass().getCanonicalName());
			try {
				if (component.getDefaultModelObject() != null) {
					buffer.append(";object,").append(component.getDefaultModelObject().getClass().getCanonicalName());
				}
			} catch (WicketRuntimeException e) {

			}
		}
		return buffer.toString();
	}

}
