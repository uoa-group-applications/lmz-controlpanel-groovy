package nz.ac.auckland.lmz.controlpanel.bus

import groovy.transform.CompileStatic
import nz.ac.auckland.lmz.controlpanel.shell.LMZGroovyShellScriptBase
import nz.ac.auckland.syllabus.events.Event
import nz.ac.auckland.syllabus.events.EventHandler
import nz.ac.auckland.syllabus.payload.EventRequestBase
import nz.ac.auckland.syllabus.payload.EventResponseBase
import org.codehaus.groovy.control.CompilerConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext

import javax.annotation.PostConstruct
import javax.inject.Inject

@Event(name = 'groovyexec', namespace = 'admin')
@CompileStatic
class GroovyShellExecEventHandler implements EventHandler<GroovyShellExecRequest, GroovyShellExecResponse> {
	private static final Logger log = LoggerFactory.getLogger(GroovyShellExecEventHandler)

	@Inject
	ApplicationContext ctx

	// Cache shell configuration
	Binding binding
	CompilerConfiguration configuration


	public static Map<String, List<String>> defaultBeans = [
		ebean: ['com.avaje.ebean.EbeanServer', 'EbeanServer (if present)'],
		userStore: ['nz.ac.auckland.ldap.LdapUserStore', 'Current user info (ex. userStore.user.upi)'],
		jobsService: ['nz.ac.auckland.jobs.periodic.PeriodicJobs', 'Periodic jobs manager (if present)']
	]


	@PostConstruct
	public void init() {
		configuration = new CompilerConfiguration()
		configuration.setScriptBaseClass(LMZGroovyShellScriptBase.class.name)

		binding = new Binding();
		binding.setVariable('ctx', ctx);

		defaultBeans.each { String beanName, List<String> data ->
			try {
				def bean = ctx.getBean(Class.forName(data[0]))
				binding.setVariable(beanName, bean)
			} catch (ClassNotFoundException e) {
				// expected error. some services may not be present
				log.info("Skipping bean ${data[0]} since class not found")
			} catch (BeansException eb) {
				log.warn("Error creating bean ${data[0]}: ${eb.getMessage()}", eb);
			}
		}

	}

	@Override
	GroovyShellExecResponse handleEvent(GroovyShellExecRequest requestType) throws Exception {
		String body = requestType.scriptbody
		log.info("Executing script: \n$body")

		// replace 'out' to capture script output
		StringWriter sw = new StringWriter();
		PrintWriter theOut = new PrintWriter(sw);
		binding.setVariable('out', theOut)

		def shell = new GroovyShell(this.class.classLoader, binding, configuration)

		String response
		try {
			response = shell.evaluate(body)
			String responseString = sw.toString()

			if (responseString)
				response = responseString + "\n\nResult:\n$response"
			else
				response = "Result:\n$response"

		} catch (Throwable e) {
			log.warn("Exception while executing script: ${e.getMessage()}")
			log.debug('Script exception stacktrace:', e)
			e.printStackTrace(theOut);
			response = sw.toString();
		}
		return new GroovyShellExecResponse(message: response?.toString())
	}

	static class GroovyShellExecRequest extends EventRequestBase {
		String scriptbody
	}

	static class GroovyShellExecResponse extends EventResponseBase {
		String message = "Okey"
	}
}


