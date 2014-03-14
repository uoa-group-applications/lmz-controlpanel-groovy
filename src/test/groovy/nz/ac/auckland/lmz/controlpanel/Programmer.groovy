package nz.ac.auckland.lmz.controlpanel


class Programmer {
	int age = 30

	boolean happy = false

	Set languages = ['java', 'groovy', 'python']

	public List getLanguages(){
		return languages
	}

	public void learnLanguage(String language){
		languages << language?.toLowerCase()
	}

	public void work(String language){
		happy = language?.equalsIgnoreCase('groovy')
	}
}
