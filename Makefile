compileServer:
        javac -cp .:../junit5.jar *.java

startServer: compileServer
        java WebApp

runAllTests: compileServer
        java -jar ../junit5.jar --class-path . --select-class FrontendTests

clean:
        rm -f *.class