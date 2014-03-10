/**
 * Created by ntingley on 24/02/14.
 */
/**
 * Created by ntingley on 24/02/14.
 */
@Grab('com.github.kevinsawicki:http-request:5.5')
@Grab('net.sourceforge.nekohtml:nekohtml:1.9.16')

import com.github.kevinsawicki.http.*
import groovy.json.JsonException
import groovy.json.JsonSlurper
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

import java.text.SimpleDateFormat

def env = System.getenv()
//env.each { println it}
def deployEnvironment = env["ENV"]
def jobName = env["SHOP.VERSION.jobName"]
def buildNumber = env["SHOP.VERSION_NUMBER"]
def buildId = env["BUILD_ID"]
def jenkinsUrl = "https://ec2-54-217-245-147.eu-west-1.compute.amazonaws.com/jenkins"
def url = "${jenkinsUrl}/job/${jobName}/${buildNumber}/api/json"

def inDateParser = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
def Date d = inDateParser.parse(buildId)
def outDateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
def String buildDate = outDateParser.format(d)

def req = HttpRequest.get(url).basic("ntingley", "1Password")
req.trustAllHosts()
req.trustAllCerts()

def String body = req.body()
def slurper = new JsonSlurper()
def json

try {
    json = slurper.parseText(body)

} catch (JsonException e) {
    print url

    e.printStackTrace()
}

def jobUrl = json.url
def jobBuildNumber = json.number
def jobBuildTag

if (jobName =~ "master") {
    jobBuildTag = json.actions.parameters[0][0].value
    jobBuildNumber = "${jobBuildNumber} ($jobBuildTag)"
}

def parser = new org.cyberneko.html.parsers.SAXParser()
parser.setFeature('http://xml.org/sax/features/namespaces', false)
parser.setFeature('http://cyberneko.org/html/features/balance-tags/document-fragment', true)
def table = new XmlSlurper(parser).parse(new File('/opt/od/var/lib/jenkins/jobs/ee-dtp-resources-confluence/confluence-table.html'))

table.TBODY.TR.each { it ->

    println it.'*'[0]
    print "\n"
}

result = table.TBODY.TR.find { it ->

    it.'*'[0] == deployEnvironment
}


result.TD[1].replaceNode { node ->
    TD {
        A("${jobName} - ${jobBuildNumber}", href: jobUrl)
    }
}

result.TD[2].replaceNode { node ->
    TD (
        buildDate
    )
}

println XmlUtil.serialize(new StreamingMarkupBuilder().bind {
    mkp.yield table
})