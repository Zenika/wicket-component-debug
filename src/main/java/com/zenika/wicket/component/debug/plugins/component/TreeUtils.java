package com.zenika.wicket.component.debug.plugins.component;

import java.io.Serializable;
import java.util.LinkedHashSet;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;

import com.zenika.wicket.component.debug.utils.WicketUtils;

@SuppressWarnings("serial")
public class TreeUtils implements Serializable {

	/**
	 * Recursive method building the HTML list that will be converted into a
	 * jQueryTree
	 *
	 * @param component
	 *            component to add to the list
	 * @param components
	 *            component list
	 * @param treeBuffer
	 *            buffer contribued to the head via the 'tree' variable
	 */
	protected static void generateTree(final Component component, final LinkedHashSet<Component> components, final StringBuffer treeBuffer) {

		// Add the component informations to its markup in a debugid attribute

		component.add(new Behavior() {
			@Override
			public void onComponentTag(final Component component, final ComponentTag tag) {
				tag.getAttributes().put("debugid", component.getId());
				tag.getAttributes().put("class", "component-debug");
			}
		});

		// component.add(new AttributeAppender("debugid", new Model<String>(""),
		// ""));
		// component.add(new AttributeAppender("debugid", new
		// Model<String>(component.getId()), ""));

		// Add the 'component-debug' class for dom mainupulation
		// component.add(new AttributeAppender("class", new
		// Model<String>("component-debug"), " "));

		// Get the component class
		final String componentClass = TreeUtils.getComponentClass(component);

		// Build the html list
		treeBuffer.append("<li>");
		treeBuffer.append("<label id='").append(component.getId()).append("-wrapper-label'>");
		treeBuffer.append("<input type='checkbox'/>");
		treeBuffer.append("<span class='debug-span'").append("tooltip=\'");
		treeBuffer.append(ComponentUtils.getInfosAsString(component)).append("' >");

		treeBuffer.append(componentClass);

		// Add the wicket:id to the label if the component id a raw one
		if (TreeUtils.isRawComponent(component)) {
			treeBuffer.append(" - [ ").append(component.getId()).append(" ]");
		}

		treeBuffer.append("</span>").append("</label>");

		boolean haschild = false;
		boolean first = true;

		for (final Component child : components) {
			if ((child.getParent() != null) && child.getParent().equals(component)) {
				if (first) {
					treeBuffer.append("<ul>");
					first = false;
				}
				haschild = true;
				TreeUtils.generateTree(child, components, treeBuffer);
			}
		}
		if (haschild) {
			treeBuffer.append("</ul>");
		}
		treeBuffer.append("</li>");
	}

	/**
	 * Get the component classname or the extended one if empty
	 *
	 * @param component
	 * @return Component class simple name
	 */
	private static String getComponentClass(final Component component) {
		final String componentClass = (component.getClass().getSimpleName().equals("")) ? component.getClass()
				.getSuperclass().getSimpleName() : component.getClass().getSimpleName();
				return componentClass;
	}

	private static boolean isRawComponent(final Component component) {
		return WicketUtils.isField(component) || WicketUtils.isForm(component)
				|| WicketUtils.isWebMarkupContainer(component);
	}

}
