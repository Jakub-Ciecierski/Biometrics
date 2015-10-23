mkdir -p bin

find ./src -name *.java > bin/sources_list.txt
javac -d ./bin -classpath "${CLASSPATH}" @bin/sources_list.txt
