package com.zenika.wicket.component.debug.plugins;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.zenika.wicket.component.debug.utils.WicketUtils;

@SuppressWarnings("serial")
public abstract class AbstractWicketDebugPlugin implements WicketDebugPlugin, Serializable {

	protected Map<String, String> configuration;

	public AbstractWicketDebugPlugin() {

	}

	public AbstractWicketDebugPlugin(Map<String, String> configuration) {
		this.configuration = configuration;
	}

	/**
	 * Run the plugin process
	 */
	public final void execute(Component component) {
		if (WicketUtils.isPage(component)) {
			headerContribution(component);
		}
		process(component);
	}

	/**
	 * Contribute to <head/> all the references added by the plugin
	 * 
	 * @param component
	 */
	private void headerContribution(Component component) {

		final StringBuffer contribution = new StringBuffer();
		contributeToHead(contribution);

		final List<PackageResourceReference> jsReferences = new ArrayList<PackageResourceReference>();
		addJavaScriptReference(jsReferences);

		final List<PackageResourceReference> cssReferences = new ArrayList<PackageResourceReference>();
		addCssReference(cssReferences);

		component.add(new Behavior() {

			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				super.renderHead(component, response);
				response.renderOnLoadJavaScript(contribution.toString());
				for (PackageResourceReference resourceReference : jsReferences) {
					response.renderJavaScriptReference(resourceReference);
				}
				for (PackageResourceReference resourceReference : cssReferences) {
					response.renderCSSReference(resourceReference);
				}
			}

		});
	}

	abstract protected void process(Component component);

	/**
	 * Contribute to <head/> with plain Javascript
	 * 
	 * @param contributionBuffer
	 */
	protected void contributeToHead(StringBuffer contributionBuffer) {

	}

	/**
	 * Add javascript files references to the <head/>
	 * 
	 * @param jsResourcesReference
	 */
	protected void addJavaScriptReference(List<PackageResourceReference> jsResourcesReference) {

	}

	/**
	 * Add css files references to the <head/>
	 * 
	 * @param cssResourcesReference
	 */
	protected void addCssReference(List<PackageResourceReference> cssResourcesReference) {

	}

}
