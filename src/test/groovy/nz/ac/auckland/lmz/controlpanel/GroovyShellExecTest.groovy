package nz.ac.auckland.lmz.controlpanel

import nz.ac.auckland.lmz.controlpanel.bus.GroovyShellExecEventHandler
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import javax.inject.Inject
import java.util.regex.Matcher


@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration('classpath:/simple-spring.xml')
class GroovyShellExecTest {
	@Inject GroovyShellExecEventHandler handler

	String LINE_GET_BEAN = "  def bean = ctx.getBean(${SomePrettyBean.class.name})"

	@Test
	public void testResult(){
		String body = "$LINE_GET_BEAN\n  bean.getSomething()"

		def result = handler.handleEvent(new GroovyShellExecEventHandler.GroovyShellExecRequest(scriptbody: body))
		assert result?.message ==~ /Result:[\n\r]+I am pretty/
	}

	@Test
	public void testPrintlnFromScript(){
		String body = "$LINE_GET_BEAN\n  println bean.getSomething()\n  return 5"

		def result = handler.handleEvent(new GroovyShellExecEventHandler.GroovyShellExecRequest(scriptbody: body))
		assert result?.message ==~ /I am pretty[\n\r]+Result:[\n\r]+5/
	}

	@Test
	public void testListingBeanMethods(){
		String body = "$LINE_GET_BEAN\n  listBeanMethods(bean.getNewProgrammer())"

		def result = handler.handleEvent(new GroovyShellExecEventHandler.GroovyShellExecRequest(scriptbody: body))
		Matcher matcher = result?.message =~ /.*\[([^\\[\\]]+)\].*/      // matches X in   '[X]...'
		assert matcher
		String methodsString = matcher[0][1]//.replaceAll('\\s+','')
		String[] methods = methodsString.split('\\s')
		// ensure methods added to class (feed and getFavouriteLanguage) and methods added to object (work) are also found
		assert methods.toList().containsAll(['feed', 'getFavouriteLanguage', 'listenMusic', 'work', 'learnLanguage', 'getAge', 'getHappy', 'getLanguages', 'isHappy'])
	}
}
