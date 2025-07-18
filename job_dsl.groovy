folder('/Tools') {
    description('Folder for miscellaneous tools')
    displayName('Tools')
}

job('Tools/clone-repository') {
    wrappers {
        preBuildCleanup()
    }
    parameters {
        stringParam('GIT_REPOSITORY_URL', null, 'Git URL of the repository to clone')
    }
    steps {
        shell('git clone "$GIT_REPOSITORY_URL"')
    }
}

job('Tools/SEED') {
    parameters {
        stringParam('GITHUB_NAME', null, "GitHub repository owner/repo_name (e.g.: 'me/myrepo')")
        stringParam('DISPLAY_NAME', null, 'Display name for the job')
    }
    steps {
        dsl({
            text('''
              job("${DISPLAY_NAME}") {
                scm {
                  git("https://github.com/${GITHUB_NAME}", "main")
                }
                triggers {
                  scm("H/1 * * * * ")
                }
                wrappers {
                  preBuildCleanup()
                }
                steps {
                  shell("make fclean")
                  shell("make")
                  shell("make tests_run")
                  shell("make clean")
                }
              }
          ''')
        })
    }
}