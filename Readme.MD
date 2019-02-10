# Description
The application performs matching the content of between the given text files and finds the most similar files.
Comparison is performed in pairs. The criteria for similarity: single text line.

####How it works
1. Take all  files located in '\src\main\testFiles\'.
2. Take the content of each file line by line and add them to the list.
(Separate list for the each file. Separate list item for the each file line.)
3. Compare two lists with file lines and extract only equal lines.
4. Take the number of lines from the file which has more lines.
5. Calculate the percentage of similarity by formula: 
> numberOfEqualLines * 100 / numberOfLinesInFileWithMoreLines

####Example
If two files contain same number of lines with same content inside they will be 100% equal.
If one of files has 10 unique text lines and another one has also 10 same lines except one which has changed word,
those files will be equal on 90%, because the value of one line is 10% and it is different in two files.
