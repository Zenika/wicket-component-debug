package com.zenika.wicket.component.debug;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentOnBeforeRenderListener;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;

import com.zenika.wicket.component.debug.plugins.WicketDebugPlugin;
import com.zenika.wicket.component.debug.plugins.component.ComponentPlugin;
import com.zenika.wicket.component.debug.utils.JsonUtils;
import com.zenika.wicket.component.debug.utils.WicketUtils;

@SuppressWarnings("serial")
public class WicketDebugListener implements IComponentOnBeforeRenderListener, Serializable {

	private static final String DEFAULT_BORDER_COLORS = "#FF3333,#00CC00,#0066FF,#33CCFF,#FF9900,#CC0066,#FFCC00,#6600FF,#1DB798";

	private static final String TRUE = "true";

	private final Map<String, String> configuration = new HashMap<String, String>();

	private final List<WicketDebugPlugin> plugins = new ArrayList<WicketDebugPlugin>();

	public WicketDebugListener() {
		this.setDefaultConfiguration();
		this.configure(this.configuration);

		if (TRUE.equalsIgnoreCase(this.configuration.get("component.plugin.enable"))) {
			this.plugins.add(new ComponentPlugin(this.configuration));
		}

		this.addPlugin(this.plugins, this.configuration);
	}

	private void setDefaultConfiguration() {
		this.configuration.put("component.plugin.enable", TRUE);
		this.configuration.put("component.plugin.include.jquery", TRUE);
		this.configuration.put("component.plugin.border.colors", DEFAULT_BORDER_COLORS);
		this.configuration.put("component.plugin.clipboard.copy", TRUE);
	}

	protected void configure(final Map<String, String> configuration) {

	}

	protected void addPlugin(final List<WicketDebugPlugin> plugins, final Map<String, String> configuration) {

	}

	@Override
	public void onBeforeRender(final Component component) {

		if (WicketUtils.isPage(component)) {
			component.add(new WicketDebugConfigurationBehavior());
		}

		for (final WicketDebugPlugin plugin : this.plugins) {
			plugin.execute(component);
		}

	}

	/**
	 * Behavior exposing the plugin configuration map <br/>
	 * to the web page for javascript use.
	 */
	private class WicketDebugConfigurationBehavior extends Behavior implements Serializable {

		private static final String CONFIGURATION_MAP_NAME = "dpc";

		@Override
		public void renderHead(final Component component, final IHeaderResponse response) {
			final StringBuffer headBuffer = new StringBuffer();
			headBuffer.append("var " + CONFIGURATION_MAP_NAME + " = ");
			headBuffer.append(JsonUtils.mapToJsonString(WicketDebugListener.this.configuration));
			response.render(OnDomReadyHeaderItem.forScript(headBuffer.toString()));
		}

	}

}
