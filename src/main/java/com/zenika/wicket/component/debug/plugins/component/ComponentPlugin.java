package com.zenika.wicket.component.debug.plugins.component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.WicketAjaxReference;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.zenika.wicket.component.debug.plugins.AbstractWicketDebugPlugin;
import com.zenika.wicket.component.debug.utils.WicketUtils;

@SuppressWarnings("serial")
public class ComponentPlugin extends AbstractWicketDebugPlugin {

	private static final JavaScriptResourceReference JQUERY_JS_REFERENCE = new JavaScriptResourceReference(
			ComponentPlugin.class, "jquery-1.6.2.js");

	private static final JavaScriptResourceReference JQUERY_TIP_JS_REFERENCE = new JavaScriptResourceReference(
			ComponentPlugin.class, "jquery.qtip-1.0.0-rc3.js");

	private static final JavaScriptResourceReference WICKET_AJAX_JS_REFERENCE = new JavaScriptResourceReference(
			WicketAjaxReference.class, "wicket-ajax.js");

	private static final JavaScriptResourceReference COMPONENT_PLUGIN_JS_REFERENCE = new JavaScriptResourceReference(
			ComponentPlugin.class, "component-plugin.js");

	private static final JavaScriptResourceReference JQUERY_TREE_JS_REFERENCE = new JavaScriptResourceReference(
			ComponentPlugin.class, "jQuery.Tree.js");

	private static final PackageResourceReference JQUERY_TREE_CSS_REFERENCE = new PackageResourceReference(
			ComponentPlugin.class, "jQuery.Tree.css");

	private static final PackageResourceReference COMPONENT_PLUGIN_CSS_REFERENCE = new PackageResourceReference(
			ComponentPlugin.class, "component-plugin.css");

	private static final PackageResourceReference CLIPPY_FLASH_REFERENCE = new PackageResourceReference(
			ComponentPlugin.class, "clippy.swf");

	private static final String PAGE_SESSION_KEY = "wicket-debug-page";

	private static final String COMPONENTS_SESSION_KEY = "wicket-debug-components";

	private String swfUrl = null;

	public ComponentPlugin(Map<String, String> configuration) {
		super(configuration);
	}

	/**
	 * Add javascript references to the <head/> section
	 */
	@Override
	public void addJavaScriptReference(List<PackageResourceReference> references) {
		references.add(WICKET_AJAX_JS_REFERENCE);
		references.add(JQUERY_JS_REFERENCE);
		references.add(JQUERY_TIP_JS_REFERENCE);
		references.add(COMPONENT_PLUGIN_JS_REFERENCE);
		references.add(JQUERY_TREE_JS_REFERENCE);
	}

	/**
	 * Add css references to the <head/> section
	 */
	@Override
	public void addCssReference(List<PackageResourceReference> references) {
		references.add(JQUERY_TREE_CSS_REFERENCE);
		references.add(COMPONENT_PLUGIN_CSS_REFERENCE);
	}

	protected void process(Component component) {

		// Get current components from session
		LinkedHashSet<Component> components = getComponents();

		StringBuffer treeBuffer = null;

		if (WicketUtils.isPage(component)) {

			// Generate the components html list
			treeBuffer = new StringBuffer();
			treeBuffer.append("var tree = \"<ul id='wicket-component-debug-tree'>");
			TreeUtils.generateTree(component, components, treeBuffer);
			treeBuffer.append("</ul>\"");

			// Insert the components html list to the <head> section
			final String tree = treeBuffer.toString();
			component.add(new Behavior() {
				@Override
				public void renderHead(Component component, IHeaderResponse response) {
					response.renderJavaScript(tree, "treeHead");
				}

			});

			// Add copy to clipboard swf url
			setClipboardSwfUrl(component);

		} else if (isWantedComponent(component)) {

			// If the component belong to a different page than the
			// one in session, change it and clear the components list.
			// This is not related to ajax components
			if (!component.getPage().equals(getPageInSession())) {
				setPageInSession(component.getPage());
				components.clear();
			}

			components.add(component);

			// Ajax components treatment
			if (AjaxRequestTarget.get() != null) {
				treeBuffer = new StringBuffer();
				// Handling back button
				if (components.size() == 1) {
					treeBuffer.append("alert('After hitting BACK you need to refresh the page in ");
					treeBuffer.append("order to see ajax components in wicket-component-debug tree.');");
				} else {
					treeBuffer.append("WicketComponentDebug.buildTree(\"");
					treeBuffer.append("<ul id='wicket-component-debug-tree'>");
					TreeUtils.generateTree(getPageInSession(), components, treeBuffer);
					treeBuffer.append("</ul>");
					treeBuffer.append("\")");
				}
				// Rebuild tree after ajax component rendering
				AjaxRequestTarget.get().appendJavaScript(treeBuffer.toString());
			}

			// Set current components in session
			setComponents(components);
		}

	}

	/**
	 * Test if the component belong to the ones wanted in the tree
	 * 
	 * @param component
	 * @return
	 */
	private boolean isWantedComponent(Component component) {
		return WicketUtils.isPanel(component) || WicketUtils.isForm(component) || WicketUtils.isButton(component)
				|| WicketUtils.isWebMarkupContainer(component) || WicketUtils.isField(component)
				&& component.isVisible();
	}

	/**
	 * Add the clippy.swf url to the configuration
	 * 
	 * @param component
	 */
	private void setClipboardSwfUrl(Component component) {
		if (swfUrl == null) {
			String contextPath = getContextPath();
			swfUrl = component.urlFor(CLIPPY_FLASH_REFERENCE, new PageParameters()).toString();
			configuration.put("swf.url", contextPath + "/" + swfUrl);
		}
	}

	private String getContextPath() {
		return ((WebRequest) RequestCycle.get().getRequest()).getContextPath();
	}

	private HttpSession getHTTPSession() {
		ServletWebRequest servletWebRequest = (ServletWebRequest) RequestCycle.get().getRequest();
		return servletWebRequest.getContainerRequest().getSession();
	}

	@SuppressWarnings("unchecked")
	private LinkedHashSet<Component> getComponents() {
		HttpSession session = getHTTPSession();
		if (session.getAttribute(COMPONENTS_SESSION_KEY) == null) {
			session.setAttribute(COMPONENTS_SESSION_KEY, new LinkedHashSet<Component>());
		}
		return (LinkedHashSet<Component>) session.getAttribute(COMPONENTS_SESSION_KEY);
	}

	private void setComponents(LinkedHashSet<Component> components) {
		getHTTPSession().setAttribute(COMPONENTS_SESSION_KEY, components);
	}

	private Component getPageInSession() {
		return (Component) getHTTPSession().getAttribute(PAGE_SESSION_KEY);
	}

	private void setPageInSession(Component component) {
		getHTTPSession().setAttribute(PAGE_SESSION_KEY, component);
	}

}
