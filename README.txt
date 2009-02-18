___    ______           ______________     ________                    
__ |  / /__(_)______ _____  ____/__  /___________(_)___  _____________ 
__ | / /__  /__  __ `__ \  /    __  /_  __ \____  /_  / / /_  ___/  _ \
__ |/ / _  / _  / / / / / /___  _  / / /_/ /___  / / /_/ /_  /   /  __/
_____/  /_/  /_/ /_/ /_/\____/  /_/  \____/___  /  \__,_/ /_/    \___/ 
                                           /___/                       

This archive contains a syntax file, a filetype plugin and an indent plugin
for clojure.

The syntax is maintained by Toralf Wittner <toralf.wittner@gmail.com>. I
included it with his permission. All kudos for the highlighting go to Toralf.

Additionally I created a filetype and indent plugin. The blame for those go to
me. The indent pugin now also works with the vectors ([]) and maps ({}). The
ftplugin now comes with a completion dictionary. Since Clojure is still rather
evolving the completions might get outdated overtime. For this the generation
script by Parth Malwankar is included with his permission.

To setup the plugins copy the contents of this archive to your ~/.vim directory.
The ftdetect/clojure.vim sets up an autocommand to automatically detect .clj
files as clojure files. The rest works automagically when you enabled the
corresponding features (see :help :filetype).

-- Meikel Brandmeyer <mb@kotka.de>
   Frankfurt am Main, August 16th 2008
            _________            ________________
            __  ____/_______________(_)__  /__  /_____ _
            _  / __ _  __ \_  ___/_  /__  /__  /_  __ `/
            / /_/ / / /_/ /  /   _  / _  / _  / / /_/ /
            \____/  \____//_/    /_/  /_/  /_/  \__,_/

Gorilla – a Clojure environment for Vim
=======================================

Gorilla provides a similar, although as sophisticated environment for
Vim as SLIME does for Emacs. It uses a modified Repl, which is provides
a network interface to a running Clojure.

Requirements
============

You need a Ruby enabled Vim. Please note, that the Windows installars
and MacVim already ship with Ruby enabled. Ruby itself might be installed
separately however. For Unix (in particular Linux), your vendor probably
already provides a Vim package with Ruby enabled.

Gorilla depends on syntax highlighting as done by VimClojure to extract
eg. s-expressions. So the latest VimClojure must be installed as well.

Please make sure that the following options are set in your .vimrc:

––8<––––8<––––8<––
syntax on
filetype plugin indent on
––8<––––8<––––8<––

Otherwise the filetype is not activated, and hence Gorilla doesn't work.

Building Gorilla
================

Note: Unless you patched the Clojure side of Gorilla you should never
have to rebuild the jarfile.

To build gorilla, create a local.properties file that contains the path to
your clojure.jar and clojure-contrib.jar. Also, include standalone=true if you
want a standalone gorilla.jar, which runs without further dependencies.
The file should look similar to:

––8<––––8<––––8<––
clojure.jar=/path/to/clojure.jar
clojure-contrib.jar=/path/to/clojure-contrib.jar
––8<––––8<––––8<––

Once you have created this file, simply run the following command:

ant clean jar

To run Gorilla you need the clojure.jar, clojure-contrib.jar and
gorilla.jar in your Classpath:

java -cp /path/to/clojure.jar:/path/to/clojure-contrib.jar:gorilla.jar de.kotka.gorilla

For standalone version use:

ant -Dstandalone=true clean jar

This creates gorilla.jar which can be launched by typing:

java -jar gorilla.jar

Please refer to the online documentation in the doc folder for further
information on how to use Gorilla, its features and its caveats.

Meikel Branmdeyer <mb@kotka.de>
Frankfurt am Main, 2008
