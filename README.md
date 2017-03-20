A new design for javadocs. This isn't about asthetics, this is about functionality.

Traditional Javadocs are just too cumbersome to use. PriorityDoc has neat features, including :

-  Shows only the most important methods, and hides the others. Show them with a single key press.
-  Skip to methods based on their initial letter with a single key press.
-  Remove clutter by hiding fully qualified names; hover over a type to see its package name.
-  Expand and contract method and field details - no more jumping around between the summary and the details section.
-  Static fields and methods are separate from their non-static cousins.
-  Careful use of colours, icons, sensible text alignment and hiding unwanted cruft makes scanning the documention easier.

Build
=====

gradle build
gradle install  # Puts the jar in a local cache for other gradle projects to pick up.
gradle javadoc

I use an rsync script, to push the javadocs to my public website :

./rsyncdocs.sh

More Info
=========

For more information, visit the [PriorityDoc website](http://giddyserv/iwiki/view/software/PriorityDoc).

Here's an example of documentation generated with PriorityDoc :

* [JGuifier](http://nickthecoder.co.uk/public/jguifier/docs/javadoc/)

Browse the API to get a feel for how PriorityDoc works.

Use the letters in the header (or just type the letter) and the icons on their left to jump around the page.

Use the numbers near the top right (or just type 1 to 5) to filter the documentation :
"1" will show only the most important information, and "5" will show everything.

The small "Home" icon in the top left will take you "Home" to the main index page.
The package name to its right takes you to the package overview corresponding to the class you are looking at.
Note, when on the "Home" page, there is no package name, the project title is there instead.

Many of the navigation items have keyboard shortcuts. Hover over them to see the shortcut. For example, shift+I
takes you to the main index page.

Note. Javascript is required for the richer experience, but the documentation is sitll readable with
javascript disabled.
