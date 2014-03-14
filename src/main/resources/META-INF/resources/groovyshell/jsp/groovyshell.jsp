<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<h2>Groovy Shell</h2>

<div class="container-fluid">
    <div class="row-fluid">
        <div class="span6">
            <div class="well">
                <button class="btn btn-primary" onclick="LMZ.invokeScript()" title="Ctrl-Enter">Execute</button>
            </div>
            <!--Groovy script input-->
            <div>
                <textarea id="textgroovyinput" class="span12" rows="35">${model.hinttext}</textarea>
            </div>
        </div>
        <div class="span6">
            <div class="well">
                <button class="btn" onclick="LMZ.clearOutput()" title="Esc">Clear</button>
            </div>
            <!--Groovy script output-->
            <div>
                <textarea id="textgroovyoutput" class="span12" rows="35"></textarea>
            </div>
        </div>
    </div>
</div>
