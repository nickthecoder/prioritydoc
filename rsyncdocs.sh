#!/bin/sh

rsync --verbose --archive --delete --exclude-from=.rync-excludes build/docs /gidea/documents/public/prioritydoc
