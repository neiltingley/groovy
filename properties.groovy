/**
 * Created by ntingley on 07/03/14.
 * needs .ssh/config file with
 *  Host *
        StrictHostKeyChecking no
    Host github.com
        User git
    IdentityFile   <path to private key>


 */

import org.eclipse.jgit.api.CloneCommand
@Grab(group = 'org.eclipse.jgit', module = 'org.eclipse.jgit', version = '3.2.0.201312181205-r')
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider

def builder = new FileRepositoryBuilder()
def repository = builder.setGitDir(new File("c:\\workspace\\dtp-resources\\"))
        .readEnvironment()
        .findGitDir()
        .build()

println repository.getDirectory()


def bigMap = [:]
def localPath = new File("c:\\temp\\dtp-resources.git");
cloneRepo(localPath)


def cloneRepo(String basedir) {

    if (localPath.exists()) {
        localPath.delete()
    }

    Git.cloneRepository()
            .setURI("git@github.com:digitaltransformation/dtp-resources.git")
            .setDirectory(localPath)
            .call()

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

//slurpProperties( "c:\\workspace\\dtp-resources\\config\\environments")




