
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

TODO
