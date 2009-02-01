" Vim filetype plugin file
" Language:     Clojure
" Maintainer:   Meikel Brandmeyer <mb@kotka.de>

" Only do this when not done yet for this buffer
if exists("b:did_ftplugin")
	finish
endif

let b:did_ftplugin = 1

let s:cpo_save = &cpo
set cpo&vim

let b:undo_ftplugin = "setlocal fo< com< cms< cpt< isk< def<"

setlocal iskeyword+=?,-,*,!,+,/,=,<,>,.

setlocal define=^\\s*(def\\(-\\|n\\|n-\\|macro\\|struct\\|multi\\)?

" Set 'formatoptions' to break comment lines but not other lines,
" and insert the comment leader when hitting <CR> or using "o".
setlocal formatoptions-=t formatoptions+=croql
setlocal commentstring=;%s

" Set 'comments' to format dashed lists in comments.
setlocal comments=sO:;\ -,mO:;\ \ ,n:;

" Take all directories of the CLOJURE_SOURCE_DIRS environment variable
" and add them to the path option.
if has("win32") || has("win64")
	let s:delim = ";"
else
	let s:delim = ":"
endif
for dir in split($CLOJURE_SOURCE_DIRS, s:delim)
	call vimclojure#AddPathToOption(dir . "/**", 'path')
endfor

" When the matchit plugin is loaded, this makes the % command skip parens and
" braces in comments.
let b:match_words = &matchpairs
let b:match_skip = 's:comment\|string\|character'

" Win32 can filter files in the browse dialog
if has("gui_win32") && !exists("b:browsefilter")
	let b:browsefilter = "Clojure Source Files (*.clj)\t*.clj\n" .
				\ "Jave Source Files (*.java)\t*.java\n" .
				\ "All Files (*.*)\t*.*\n"
endif

for ns in ['clojure.core', 'clojure.set', 'clojure.xml', 'clojure.zip']
	call vimclojure#AddCompletions(ns)
endfor

" Define toplevel folding if desired.
function! ClojureGetFoldingLevel(lineno)
	let closure = { 'lineno' : a:lineno }

	function closure.f() dict
		execute self.lineno

		if vimclojure#SynIdName() =~ 'clojureParen\d' && vimclojure#Yank('l', 'normal "lyl') == '('
			return 1
		endif

		if searchpairpos('(', '', ')', 'bWr', 'vimclojure#SynIdName() !~ "clojureParen\\d"') != [0, 0]
			return 1
		endif

		return 0
	endfunction

	return vimclojure#WithSavedPosition(closure)
endfunction

if exists("g:clj_want_folding") && g:clj_want_folding == 1
	setlocal foldexpr=ClojureGetFoldingLevel(v:lnum)
	setlocal foldmethod=expr
endif

let &cpo = s:cpo_save
