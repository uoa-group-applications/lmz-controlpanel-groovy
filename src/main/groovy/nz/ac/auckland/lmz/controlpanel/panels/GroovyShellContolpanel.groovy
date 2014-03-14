package nz.ac.auckland.lmz.controlpanel.panels

import groovy.transform.CompileStatic
import nz.ac.auckland.common.stereotypes.UniversityComponent
import nz.ac.auckland.lmz.controlpanel.DefaultControlPanelAssets
import nz.ac.auckland.lmz.controlpanel.bus.GroovyShellExecEventHandler
import nz.ac.auckland.lmz.controlpanel.core.ControlPanel
import nz.ac.auckland.lmz.controlpanel.core.ControlPanelAssets
import nz.ac.auckland.lmz.controlpanel.core.ControlPanelMetadata
import nz.ac.auckland.stencil.LinkBuilder

import javax.annotation.PostConstruct
import javax.inject.Inject


@UniversityComponent
@CompileStatic
class GroovyShellContolpanel implements ControlPanel {

	@Inject LinkBuilder linkBuilder
	@Inject DefaultControlPanelAssets defaultControlPanelAssets

	/**
	 * @return the control panel meta data
	 */
	@Override
	ControlPanelMetadata getMetadata() {

		return new ControlPanelMetadata(
				title: "Groovy Admin Console",
				description: "Admin console for running groovy script",
				uri: "admin-console",
				assets: new ControlPanelAssets(
						javascripts: ['/groovyshell/js/groovyshell.js'],
						stylesheets: ['/groovyshell/css/groovyshell.css'],
						embeds: [defaultControlPanelAssets as ControlPanelAssets])
		);
	}

	/**
	 * @return the template to render
	 */
	@Override
	String getTemplate() {
		return "/groovyshell/jsp/groovyshell.jsp";
	}

	/**
	 * @return current user information and table of available fake users
	 */
	@Override
	Map<String, Object> getViewModel() {
		String hinttext = """
// Groovy Code here

// Implicit variables include:
//    ctx: spring context
"""
		GroovyShellExecEventHandler.defaultBeans.each { String beanName, List<String> data ->
			hinttext += "\n//     ${beanName}: ${data[1]}"
		}

		hinttext += "\n\n// Hint:\n// use listBeans() to see all available beans\n"
		hinttext += "// use listBeanMethods(bean) to see method names\n"

		return [hinttext:hinttext]
	}
}
