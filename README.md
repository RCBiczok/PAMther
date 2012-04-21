![PAMther logo](https://github.com/RCBiczok/PAMther/raw/master/logo.png "PAMther Logo")
# PAMther in a nutshell #
PAMther aims to be a pure Java interface to the Unix [PAM](http://linux.die.net/man/5/pam 
"PAM documentation") API (Pluggable Authentication Modules).
PAM itself is a native authentication stack and is commonly used in nearly every UNIX or Linux OS
(e.g. Mac OSX, FreeBSD; Solaris, Ubuntu, Fedora).

# Features #
* Authentication and account validation either through the direct API or the JAAS module.
* Direct interaction with PAM modules via JAAS Callback mechanism.
* Mechanism for changing users authentication token.

# Tested platforms #
* Linux (Ubuntu 11.10 x64)
* Solaris (v11 x64)

# Examples #
Just have a look at the [unit tests](https://github.com/RCBiczok/PAMther/blob/master/src/test/java/org/pamther/jaas/PAMLoginModuleTest.java 
"JAAS login module unit test").

# Download #
See [download section](https://github.com/RCBiczok/PAMther/downloads "Download section").

# License #
This library is provided under the [Apache 2](http://www.apache.org/licenses/LICENSE-2.0.html "License file") license.