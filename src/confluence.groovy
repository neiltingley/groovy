import groovy.xml.MarkupBuilder
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

import javax.management.modelmbean.XMLParseException


@Grab('net.sourceforge.nekohtml:nekohtml:1.9.16')
def parser = new org.cyberneko.html.parsers.SAXParser()
parser.setFeature('http://xml.org/sax/features/namespaces', false)
parser.setFeature('http://cyberneko.org/html/features/balance-tags/document-fragment', true)

def table = new XmlSlurper(parser).parse(new File("C:\\workspace\\groovy\\confluence-table.html"))

//println table.TBODY.TR.size()

table.TBODY.TR.each { it ->

    println it.'*'[0]
    print "\n"
}

result = table.TBODY.TR.find { it ->

    it.'*'[0] == "NFR"
}

/*
println result.getClass()
def origin = result.parent()
def tr = result.parent()


tr.TD[1].A[0].@href="Some link"
tr.TD[1].A[0]="Some link text"
*/

/*tr.replaceNode { node ->
    TR {
        TD("NFR2")
        TD("some_name",HREF:"some_link")

    }
}*/

//origin.replaceNode { node -> tr }

println XmlUtil.serialize(new StreamingMarkupBuilder().bind {
    mkp.yield table
})

result.TD[1].replaceNode { node ->
    A("Some link text", href: "Some text")

}

println XmlUtil.serialize(new StreamingMarkupBuilder().bind {
    mkp.yield table
})