package nz.ac.auckland.lmz.controlpanel.shell


abstract class LMZGroovyShellScriptBase extends Script{

	/**
	 * Example function, will be available from withing script
	 * @return
	 */
	void listBeans() {
		getBinding().getVariable('ctx').getBeanDefinitionNames().sort().each{
			this.println(it)
		}
	}

	void listBeanMethods(Object bean){
		def allMethods = bean.metaClass.getMetaMethods()*.name + bean.metaClass.getMethods()*.name
		def allGenericMethods =  new Object().metaClass.getMethods()*.name + new Object().metaClass.getMetaMethods()*.name

		def result = allMethods - allGenericMethods // remove all methods that groovy adds to all objects

		// filter crap
		result.unique()
		result = result.findAll {
			return !it.contains('$')
		}
		result = result.sort();


		println "[${result.join('\n')}]"
	}

}
