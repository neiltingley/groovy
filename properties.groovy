/**
 * Created by ntingley on 07/03/14.
 * needs .ssh/config file with
 *  Host *
 StrictHostKeyChecking no
 Host github.com
 User git
 IdentityFile   <path to private key>


 */

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
    def Map bigMap = [:]

    def PropertyFinder() {

    }

    def String localPath = "c:\\temp\\dtp-resources.git"


    def main() {

        cloneRepo(localPath)
        def repo = new FileRepository(new File("c:\\temp\\dtp-resources.git\\.git"))
        def Git git = new Git(repo);

        try {
            git.checkout()
                    .setName("develop")
                    .setAllPaths(true)
                    .setStartPoint("65edd196c3cc811a70272319600868ccf7c487bc")
                    .call()


        } catch (
        GitAPIException e
        ) {
            println e.message

        }
        slurpProperties(localPath + "\\config\\environments")

    }

    def cloneRepo(String d) {

        def File basedir = new File(d)
        if (!basedir.exists()) {


            Git.cloneRepository()
                    .setURI("git@github.com:digitaltransformation/dtp-resources.git")
                    .setDirectory(basedir)
                    .call()
        }

        def builder = new FileRepositoryBuilder()
        return builder.setGitDir(basedir)
                .readEnvironment()
                .findGitDir()
                .build()

    }

    def slurpProperties(String dir) {


        def List properties = new FileNameFinder().getFileNames(dir, "**/*.properties")

        properties.each { propertyFileName ->

            def File propertyFile = new File(propertyFileName)
            def baseName = propertyFile.name
            Properties p = new Properties()
            propertyFile.withInputStream { p.load(it) }
            bigMap[propertyFile] = p

        }

    }
}

c = new PropertyFinder()
c.main()
