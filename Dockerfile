FROM jenkins/jenkins:lts

# désactiver l'assistant de configuration
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"
# indiquer au plugin JCaC où trouver le fichier yaml
ENV CASC_JENKINS_CONFIG="/var/jenkins_home/config/jcasc.yml" 

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

RUN mkdir -p /usr/share/jenkins/ref/init.groovy.d \
 && printf '%s\n' \
    "// Jenkins with JCasC ready" \
    > /usr/share/jenkins/ref/init.groovy.d/00-startup.groovy

USER 0
RUN chown -R jenkins:jenkins /var/jenkins_home
USER jenkins