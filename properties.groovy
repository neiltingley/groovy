/**
 * Created by ntingley on 07/03/14.
 * needs .ssh/config file with
 *  Host *
 StrictHostKeyChecking no
 Host github.com
 User git
 IdentityFile   <path to private key>


 */
@Grab(group = 'org.eclipse.jgit', module = 'org.eclipse.jgit', version = '3.2.0.201312181205-r')

import org.eclipse.jgit.api.CheckoutCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.internal.storage.file.FileRepository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder

def bigMap = [:]
def localPath = new File("c:\\temp\\dtp-resources.git");
cloneRepo(localPath)
def repo = new FileRepository(new File("c:\\temp\\dtp-resources.git\\.git"))
git = new Git(repo);

try {
    git.checkout()
            .setName("develop")
            .setAllPaths(true)
            .setStartPoint("65edd196c3cc811a70272319600868ccf7c487bc")
            .call()


} catch (GitAPIException e) {
    println e.message

}

def cloneRepo(File basedir) {

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

        propertyFile = new File(propertyFileName)
        baseName = propertyFile.name
        p = new Properties()
        propertyFile.withInputStream { p.load(it) }
        bigMap[propertyFile] = p

    }

}

slurpProperties( localPath + "\\config\\environments")




