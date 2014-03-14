package nz.ac.auckland.lmz.controlpanel

import nz.ac.auckland.common.stereotypes.UniversityComponent

import javax.annotation.PostConstruct

@UniversityComponent
class SomePrettyBean {
	public String something = 'I am pretty'

	int counter = 0;

	@PostConstruct
	public init(){
		// update class with methods
		Programmer.metaClass.feed = {String food->
			delegate.happy = (food in ['meat', 'cookie'])
		}

		Programmer.metaClass.getFavouriteLanguage = {
			return 'groovy'
		}
	}

	public String getSomething(){
		return something
	}

	public int increment(){
		return counter++
	}

	public Programmer getNewProgrammer(){
		Programmer pr = new Programmer()

		// update object with methods
		def newMetaClass = new ExpandoMetaClass(pr.class)
		newMetaClass.listenMusic = {
			delegate.happy = true
		}
		newMetaClass.initialize()
		pr.metaClass = newMetaClass

		return pr
	}
}
