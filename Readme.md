

Wicket Component Debug
============================

`Wicket Component Debug` is a tool that simplify wicket applications maintenance by showing the current page hierarchy _directly_ in the browser.


Features
--------

* Components bordering (with color selection)
* Components highlighting
* Components informations (Class, Wicket Id, Model and ModelObject)
* Copy to clipboard information link
* Configurable
* Extensible


Screenshots
--------

### _Panels bordering_

![Bordering](https://github.com/Zenika/wicket-component-debug/raw/gh-pages/screenshot-1.png)  


###  _Form highlighting_

![Highlighting](https://github.com/Zenika/wicket-component-debug/raw/gh-pages/screenshot-2.png)

How to use
--------

To enable `Wicket Component Debug` just add the listener somewhere in your __WebApplication__ init() method.

    public class MyApplication extends WebApplication {

        [...]

        @Override
        protected void init() {
            [...]
            addPostComponentOnBeforeRenderListener(new WicketDebugListener());
            [...]
        }

        [...]

    }

To configure `Wicket Component Debug` you need to override the `configure` method.
For example, if your application already uses jquery, you can (__and you should__) disable it.

	[...]

	addPostComponentOnBeforeRenderListener(new WicketDebugListener() {
		
		@Override
		protected void configure(Map<String, String> configuration) {
			configuration.put("component.plugin.include.jquery", "false");
		}
		
	});
	
	[...]
	
At the moment, configuration map includes only those keys :

* `component.plugin.enable` - Enable or disable the component plugin (default : true)
* `component.plugin.include.jquery` - Include jquery (default : true, jquery 1.6.2)
* `component.plugin.border.colors` - Comma seperated list of hexadecimal colors for component bordering.
* `component.plugin.clipboard.copy` - Display the copy to clipboard link (default : true)

Custom plugin
--------

You can enhance `Wicket Component Debug` by creating your own plugins. 

To do so, just create a class that extends `AbstractWicketDebugPlugin` and overrides the `process` method.

    public class MyPlugin extends AbstractWicketDebugPlugin {
        @Override
        protected void process(Component component) {
            System.out.println("Component : " + component);
        }
    }

You can also override 3 others methods to easily contribute to the <head/> section of the page.

* `contributeToHead` - Contribute with plain Javascript.
* `caddJavaScriptReference` - Contribute with a Javascript ResourceReference file.
* `addCssReference` - Contribute with a CSS ResourceReference file.


Look at the [TreePlugin](https://github.com/Zenika/wicket-component-debug/blob/master/src/main/java/com/zenika/wicket/component/debug/plugins/tree/TreePlugin.java) if you want an example of a simple custom plugin. It basically just output the components hierarchy in the console with some customizable parameters such as _wicket:id_, _class_...

![Highlighting](https://github.com/Zenika/wicket-component-debug/raw/gh-pages/screenshot-3.png)


If your looking for a more complexe one, try the [ComponentPlugin](https://github.com/Zenika/wicket-component-debug/blob/master/src/main/java/com/zenika/wicket/component/debug/plugins/component/ComponentPlugin.java).

