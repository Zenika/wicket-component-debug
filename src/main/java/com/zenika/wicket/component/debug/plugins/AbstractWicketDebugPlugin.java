package com.zenika.wicket.component.debug.plugins;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.zenika.wicket.component.debug.utils.WicketUtils;

@SuppressWarnings("serial")
public abstract class AbstractWicketDebugPlugin implements WicketDebugPlugin, Serializable {

	protected Map<String, String> configuration;

	public AbstractWicketDebugPlugin() {

	}

	public AbstractWicketDebugPlugin(final Map<String, String> configuration) {
		this.configuration = configuration;
	}

	/**
	 * Run the plugin process
	 */
	@Override
	public final void execute(final Component component) {
		if (WicketUtils.isPage(component)) {
			this.headerContribution(component);
		}
		this.process(component);
	}

	/**
	 * Contribute to <head/> all the references added by the plugin
	 *
	 * @param component
	 */
	private void headerContribution(final Component component) {

		final StringBuffer contribution = new StringBuffer();
		this.contributeToHead(contribution);

		final List<PackageResourceReference> jsReferences = new ArrayList<PackageResourceReference>();
		this.addJavaScriptReference(jsReferences);

		final List<PackageResourceReference> cssReferences = new ArrayList<PackageResourceReference>();
		this.addCssReference(cssReferences);

		component.add(new Behavior() {

			@Override
			public void renderHead(final Component component, final IHeaderResponse response) {
				super.renderHead(component, response);
				response.render(OnDomReadyHeaderItem.forScript(contribution.toString()));
				for (final PackageResourceReference resourceReference : jsReferences) {
					response.render(JavaScriptHeaderItem.forReference(resourceReference));
				}
				for (final PackageResourceReference resourceReference : cssReferences) {
					response.render(CssHeaderItem.forReference(resourceReference));
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
	protected void contributeToHead(final StringBuffer contributionBuffer) {

	}

	/**
	 * Add javascript files references to the <head/>
	 *
	 * @param jsResourcesReference
	 */
	protected void addJavaScriptReference(final List<PackageResourceReference> jsResourcesReference) {

	}

	/**
	 * Add css files references to the <head/>
	 *
	 * @param cssResourcesReference
	 */
	protected void addCssReference(final List<PackageResourceReference> cssResourcesReference) {

	}

}
