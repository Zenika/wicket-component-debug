package com.zenika.wicket.component.debug.plugins.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;

import com.zenika.wicket.component.debug.plugins.AbstractWicketDebugPlugin;
import com.zenika.wicket.component.debug.utils.WicketUtils;

@SuppressWarnings("serial")
public class TreePlugin extends AbstractWicketDebugPlugin {

	private List<Component> components;

	private StringBuffer treeBuffer;

	public TreePlugin(Map<String, String> configuration) {
		this.configuration = configuration;
		this.treeBuffer = new StringBuffer();
	}

	public void process(Component component) {
		if (components == null)
			components = new ArrayList<Component>();
		if (isWantedComponent(component)) {
			components.add(component);
		} else if (WicketUtils.isPage(component)) {
			treeBuffer.append("\n\n");
			treeBuffer.append("-----------------------------------");
			treeBuffer.append("-----------------------------------");
			treeBuffer.append("\n");
			navigateTree(component, 0);
			treeBuffer.append("-----------------------------------");
			treeBuffer.append("-----------------------------------");
			treeBuffer.append("\n");
			System.out.println(treeBuffer.toString());
			treeBuffer = new StringBuffer();
			components.clear();
		}
	}

	public void navigateTree(Component component, int level) {
		printLine(component, level);
		for (Component child : components) {
			if (child.getParent().equals(component)) {
				navigateTree(child, level + 1);
			}
		}
	}

	public void printLine(Component component, int level) {

		StringBuffer line = new StringBuffer();
		for (int i = 0; i < level; i++) {
			line.append("-");
		}

		StringBuffer space = new StringBuffer();
		for (int i = 1; i < level; i++) {
			space.append("  ");
		}

		if (level == 0) {
			treeBuffer.append(component.getClass().getSimpleName());
		} else {
			treeBuffer.append(space).append("|").append("\n");
			treeBuffer.append(space).append("|").append(line).append(" ");

			if (component.getClass().getSimpleName().equals("")) {
				treeBuffer.append(component.getClass().getSuperclass().getSimpleName());
			} else {
				treeBuffer.append(component.getClass().getSimpleName());
			}

			if ("true".equals(configuration.get("tree.plugin.show.id"))) {
				treeBuffer.append(" / id : ").append(component.getId());
			}

			if ("true".equals(configuration.get("tree.plugin.show.model")) && component.getDefaultModel() != null) {
				treeBuffer.append(" / model : ").append(component.getDefaultModel().getClass().getSimpleName());
			}

			try {
				if ("true".equals(configuration.get("tree.plugin.show.model.object"))
						&& component.getDefaultModel() != null && component.getDefaultModelObject() != null) {
					treeBuffer.append(" / object : ").append(
							component.getDefaultModelObject().getClass().getSimpleName());
				}
			} catch (Exception e) {

			}
		}
		treeBuffer.append("\n");
	}

	private boolean isWantedComponent(Component component) {
		return WicketUtils.isPanel(component) || WicketUtils.isForm(component) || WicketUtils.isButton(component)
				|| WicketUtils.isWebMarkupContainer(component) || WicketUtils.isField(component)
				&& component.isVisible();
	}

}
