package com.zenika.wicket.component.debug.utils;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;

public final class WicketUtils {

	private WicketUtils() {

	}

	public static boolean isPage(Component component) {
		return WebPage.class.isAssignableFrom(component.getClass());
	}

	public static boolean isPanel(Component component) {
		return Panel.class.isAssignableFrom(component.getClass()) && !(component instanceof PagingNavigator);
	}

	public static boolean isForm(Component component) {
		return Form.class.isAssignableFrom(component.getClass());
	}

	public static boolean isField(Component component) {
		return TextField.class.isAssignableFrom(component.getClass())
				|| DropDownChoice.class.isAssignableFrom(component.getClass());
	}

	public static boolean isButton(Component component) {
		return Button.class.isAssignableFrom(component.getClass());
	}

	public static boolean isWebMarkupContainer(Component component) {
		return WebMarkupContainer.class.isAssignableFrom(component.getClass())
				&& (component.getClass().equals(WebMarkupContainer.class));
	}

}
