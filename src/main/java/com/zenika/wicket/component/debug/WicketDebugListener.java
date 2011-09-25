package com.zenika.wicket.component.debug;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentOnBeforeRenderListener;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;

import com.zenika.wicket.component.debug.plugins.WicketDebugPlugin;
import com.zenika.wicket.component.debug.plugins.component.ComponentPlugin;
import com.zenika.wicket.component.debug.utils.JsonUtils;
import com.zenika.wicket.component.debug.utils.WicketUtils;

@SuppressWarnings("serial")
public class WicketDebugListener implements IComponentOnBeforeRenderListener, Serializable {

	private static final String DEFAULT_BORDER_COLORS = "#FF3333,#00CC00,#0066FF,#33CCFF,#FF9900,#CC0066,#FFCC00,#6600FF,#1DB798";

	private static final String TRUE = "true";

	private Map<String, String> configuration = new HashMap<String, String>();

	private List<WicketDebugPlugin> plugins = new ArrayList<WicketDebugPlugin>();

	public WicketDebugListener() {
		setDefaultConfiguration();
		configure(configuration);

		if (TRUE.equalsIgnoreCase(configuration.get("component.plugin.enable"))) {
			plugins.add(new ComponentPlugin(configuration));
		}

		addPlugin(plugins, configuration);
	}

	private void setDefaultConfiguration() {
		configuration.put("component.plugin.enable", TRUE);
		configuration.put("component.plugin.include.jquery", TRUE);
		configuration.put("component.plugin.border.colors", DEFAULT_BORDER_COLORS);
		configuration.put("component.plugin.clipboard.copy", TRUE);
	}

	protected void configure(Map<String, String> configuration) {

	}

	protected void addPlugin(List<WicketDebugPlugin> plugins, Map<String, String> configuration) {

	}

	public void onBeforeRender(Component component) {

		if (WicketUtils.isPage(component)) {
			component.add(new WicketDebugConfigurationBehavior());
		}

		for (WicketDebugPlugin plugin : plugins) {
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
		public void renderHead(Component component, IHeaderResponse response) {
			StringBuffer headBuffer = new StringBuffer();
			headBuffer.append("var " + CONFIGURATION_MAP_NAME + " = ");
			headBuffer.append(JsonUtils.mapToJsonString(configuration));
			response.renderJavaScript(headBuffer.toString(), CONFIGURATION_MAP_NAME);
		}

	}

}
