import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

/**
 * Created by ntingley on 25/02/14.
 */
def writer = new StringWriter()  // html is written here by markup builder
def markup = new groovy.xml.MarkupBuilder(writer)  // the builder
def foo = "Some String"

markup.html{
    td (foo)
}

def outWriter = new FileWriter("tempfile")
groovy.xml.XmlUtil.serialize( markup, outWriter)