
clear
echo "START OF SCRIPT"
echo "---------------------------------------------------------"
echo ""



#compiles source files
echo "//////////////////////////////////////////////////////////////////////"
echo "                         COMPILING SOURCE FILES                          "
echo "///////////////////////////////////////////////////////////////////////"
echo ""
	read -p "press any key to continue" 
	if javac -classpath ./src/ -d ./bin/ ./src/board/*.java; then
		echo "./src/board/*.java compiled"
	else
		exit 1
	fi
	if javac -classpath ./src/ -d ./bin/ ./src/gui/*.java; then
		echo "./src/gui/*.java compiled"
	else
		exit 1
	fi
	if javac -classpath ./src/ -d ./bin/ ./src/scoreboard/*.java; then
		echo "./src/scoreboard/*.java compiled"
	else
		exit 1
	fi
	if javac -classpath ./src/ -d ./bin/ ./src/*.java; then
		echo "./src/*.java compiled"
	else
		exit 1

	fi
	
	if cp ./bin/Gertrude.class ./Gertrude.class; then
		echo "Gertrude was copied";
	else
		exit 1
	fi

	
	if cp ./bin/board/*.* ./board/; then
		echo "board was copied";
	else
		exit 1
	fi




#runs the program
echo ""
echo "//////////////////////////////////////////////////////////////////////"
echo "                          RUNNING THE PROGRAM                      "
echo "///////////////////////////////////////////////////////////////////////"
echo ""
echo ""
read -p "press any key to continue" 
java -classpath ./bin/ Referee



echo ""
echo "END OF SCRIPT"
echo "///////////////////////////////////////////////////////////////////////"
echo "///////////////////////////////////////////////////////////////////////"
exit 0

