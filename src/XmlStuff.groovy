/**
 * Created by ntingley on 17/02/14.
 */
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

def string = '''

<movies>
 <movie>
  <title>PHP: Behind the Parser</title>
  <characters>
   <character>
    <name>Ms. Coder</name>
    <actor>Onlivia Actora</actor>
   </character>
   <character>
    <name>Mr. Coder</name>
    <actor>El Act&#211;r</actor>
   </character>
  </characters>
  <plot>
   So, this language. It's like, a programming language. Or is it a
   scripting language? All is revealed in this thrilling horror spoof
   of a documentary.
  </plot>
  <great-lines>
   <line>PHP solves all my web problems</line>
  </great-lines>
  <rating type="thumbs">7</rating>
  <rating type="stars">5</rating>

 </movie>

 <movie>
  <title>Another Movie</title>
  <characters>
     <character>
       <actor>boris</actor>
   </character>
  </characters>
</movie>
</movies>'''

def movies = new XmlSlurper().parseText(string)

def allMovies = movies.movie

assert allMovies.size() == 2

//def result = movies.depthFirst().findAll { it.title.text() == "Another Movie" }
def result = movies.movie.findAll { it.title.text() == "Another Movie" }
result.replaceNode { title ("Replaced Move")}

// find element with value boris (recursively)
def result2 = movies.depthFirst().find {
    it.text() == "boris"
}

println result2.getClass()




println XmlUtil.serialize(new StreamingMarkupBuilder().bind {
    mkp.yield result2
} )

//println (result.getClass())