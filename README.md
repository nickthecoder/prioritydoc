A new design for javadocs. This isn't about asthetics, this is about functionality.

Traditional Javadocs are just too cumbersome to use. PriorityDoc has neat features, including :

-  Shows only the most important methods, and hides the others. Show them with a single key press.
-  Skip to methods based on their initial letter with a single key press.
-  Remove clutter by hiding fully qualified names; hover over a type to see its package name.
-  Expand and contract method and field details.
-  Static fields and methods are separate from their non-static cousins.
-  Designed to be easy to scan through.

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
