@call "c:\Program Files (x86)\Microsoft Visual Studio\2019\BuildTools\VC\Auxiliary\Build\vcvars64.bat"
@native-image -jar target\checkPdfContainsEOF-jar-with-dependencies.jar -o pdfContainsEOF
