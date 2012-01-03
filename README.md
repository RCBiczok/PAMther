![PAMther logo](https://bitbucket.org/RCBiczok/pamther/raw/07b13f9619a1/logo.png "PAMther Logo")
# PAMther in a nutshell #
PAMther aims to be a pure Java interface to the Unix [PAM](http://linux.die.net/man/5/pam 
"PAM documentation") API (Pluggable Authentication Modules).
PAM itself is a native authentication stack and is commonly used in nearly every UNIX or Linux OS
(e.g. Mac OSX, FreeBSD; Solaris, Ubuntu, Fedora).

# Features #
* Authentication and account validation either through the direct API or the JAAS module.
* Direct interaction with PAM modules via JAAS Callback mechanism.
* Mechanism for changing users authentication token.

# Examples #
Just have a look at the [unit tests]
(https://bitbucket.org/RCBiczok/pamther/src/72201c44165d/src/test/java/org/pamther/jaas/PAMLoginModuleTest.java 
"JAAS login module unit test").

# Download #
See [download section](https://bitbucket.org/RCBiczok/pamther/downloads "Download section").

# License #
This library is provided under the [Apache 2](http://www.apache.org/licenses/LICENSE-2.0.html "License file") license.