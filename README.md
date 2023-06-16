# checkPdfContainsEOF
usage: checkPdfContainsEOF file [retry [delay]]"

If file not starts with %PDF or not has %%EOF at end of file

retrys to check file "retry" times delaying by "delay" secunds

If found %PDF and %%EOF exiting 0 otherwise 1
