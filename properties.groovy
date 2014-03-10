/**
 * Created by ntingley on 07/03/14.
 * needs .ssh/config file with
 *  Host *
 StrictHostKeyChecking no
 Host github.com
 User git
 IdentityFile   <path to private key>


 */


import com.thoughtworks.xstream.io.xml.PrettyPrintWriter

import java.util.Properties
import groovy.transform.TypeChecked
@Grab(group = 'org.eclipse.jgit', module = 'org.eclipse.jgit', version = '3.2.0.201312181205-r')

import org.eclipse.jgit.api.CheckoutCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.storage.file.FileRepositoryBuilder


class PropertyFinder {
    def Map bigMap1 = [:]
    def Map bigMap2 = [:]
    def String localPath = "c:\\temp\\dtp-resources.git"
    def Git git

    def PropertyFinder() {

    }

    def clone() {

        cloneRepo(localPath)
        def repo = new FileRepository(new File("c:\\temp\\dtp-resources.git\\.git"))
        this.git = new Git(repo);

    }

    def checkout(String hash) {

        def repo = new FileRepository(new File(localPath + "\\.git"))
        def Git git = new Git(repo);
        try {
            git.checkout()
                    .setName("develop")
                    .setAllPaths(true)
                    .setStartPoint(hash)
                    .call()


        } catch (
        GitAPIException e
        ) {
            println e.message

        }


    }

    def cloneRepo(String d) {

        def File basedir = new File(d)
        if (!basedir.exists()) {


            Git.cloneRepository()
                    .setURI("git@github.com:digitaltransformation/dtp-resources.git")
                    .setDirectory(basedir)
                    .call()
        }

//        def builder = new FileRepositoryBuilder()
//        return builder.setGitDir(basedir)
//                .readEnvironment()
//                .findGitDir()
//                .build()

    }

    def slurpProperties(String dir) {

        def m = [:]
        def List properties = new FileNameFinder().getFileNames(dir, "**/*.properties")

        properties.each { propertyFileName ->

            def File propertyFile = new File(propertyFileName)
            def baseName = propertyFile.name
            Properties p = new Properties()
            propertyFile.withInputStream { p.load(it) }
            m[baseName] = p

        }

        return m

    }

    def compareTo(Map m, Map n) {

        def Properties props1
        def Properties props2
        m.each { k ->

            props1 = k.value
            props2 = n[k.key]


            if (!props1.equals(props2)) {

                println "\n"
                println "Property file: " + k.key
                println "================================"


                def newKeys = props1*.key
                def oldKeys = props2*.key

                def removedKeys = oldKeys - newKeys
                def addedKeys = newKeys - oldKeys

                removedKeys.each { println "Added Key -- " + it }
                addedKeys.each { println "Removed Key -- " + it }

                if (props1.size == props2.size) {


                    props1.each { j ->
                        if ( j.value != props2[j.key] ) {
                            println ( "Changed key: " + j.key + "\t" + j.value + '<--->' +  props2[j.key])
                        }

                    }
                }
            }


        }

    }

    def main() {

        git = cloneRepo(localPath)
        checkout("316d3728f2e3bdad4db7e037f94cadaaa0bf7fad")
        bigMap1 = slurpProperties(localPath + "\\config\\environments")
        checkout("d3810b9c30d2fe85496691b7900ffe47efc44600")
        bigMap2 = slurpProperties(localPath + "\\config\\environments")
        compareTo(bigMap1, bigMap2)


    }

}


c = new PropertyFinder()
c.main()