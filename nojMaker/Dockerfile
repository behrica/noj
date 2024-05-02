FROM noj_base

SHELL ["/bin/bash", "-c"]
ENV SHELL /bin/bash
#ENV HOME  /root
#ENV BASH_ENV="\$HOME/.sdkman/bin/sdkman-init.sh"

ENV JAVA_VERSION "java"
ENV PYTHON_VERSION "3.8"
ENV R_VERSION "4.2.1"
ENV CLOJURE_VERSION "1.11.3.1463"
RUN sdk install $JAVA_VERSION
RUN pyenv install $PYTHON_VERSION && pyenv global $PYTHON_VERSION


RUN curl -L -O https://github.com/clojure/brew-install/releases/download/$CLOJURE_VERSION/posix-install.sh  && chmod +x posix-install.sh && ./posix-install.sh
RUN curl -O https://cdn.rstudio.com/r/ubuntu-2204/pkgs/r-${R_VERSION}_1_amd64.deb && gdebi -n r-${R_VERSION}_1_amd64.deb
RUN ln -s /opt/R/${R_VERSION}/bin/R /usr/local/bin/R && ln -s /opt/R/${R_VERSION}/bin/Rscript /usr/local/bin/Rscript
RUN echo "source /root/.sdkman/bin/sdkman-init.sh" > ~/.bashrc
