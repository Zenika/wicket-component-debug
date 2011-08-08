package com.zenika.wicket.component.debug.plugins;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;

import com.zenika.wicket.component.debug.utils.WicketUtils;

@SuppressWarnings("serial")
public abstract class AbstractWicketDebugPlugin implements WicketDebugPlugin, Serializable {

	protected HashMap<String, String> configuration;

	public AbstractWicketDebugPlugin() {

	}

	public AbstractWicketDebugPlugin(HashMap<String, String> configuration) {
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

		final List<ResourceReference> jsReferences = new ArrayList<ResourceReference>();
		addJavaScriptReference(jsReferences);

		final List<ResourceReference> cssReferences = new ArrayList<ResourceReference>();
		addCssReference(cssReferences);

		component.add(new AbstractBehavior() {
			@Override
			public void renderHead(IHeaderResponse response) {
				response.renderOnLoadJavascript(contribution.toString());
				for (ResourceReference resourceReference : jsReferences) {
					response.renderJavascriptReference(resourceReference);
				}
				for (ResourceReference resourceReference : cssReferences) {
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
	protected void addJavaScriptReference(List<ResourceReference> jsResourcesReference) {

	}

	/**
	 * Add css files references to the <head/>
	 * 
	 * @param cssResourcesReference
	 */
	protected void addCssReference(List<ResourceReference> cssResourcesReference) {

	}

}
