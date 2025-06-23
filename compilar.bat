@echo off
echo ­ƒº╣ Limpiando...
rmdir /s /q bin
mkdir bin

echo ­ƒôª Compilando...
javac --module-path javafx\javafx-sdk-21.0.7\lib --add-modules javafx.controls,javafx.fxml ^
-cp ".;lib\gs-core-2.0.jar;lib\gs-ui-swing-2.0.jar" -d bin *.java

echo ­ƒÜÇ Ejecutando...
java --module-path javafx\javafx-sdk-21.0.7\lib --add-modules javafx.controls,javafx.fxml ^
-cp "bin;lib\gs-core-2.0.jar;lib\gs-ui-swing-2.0.jar" MainApp

pause
