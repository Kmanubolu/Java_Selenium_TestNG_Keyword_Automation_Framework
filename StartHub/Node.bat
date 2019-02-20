cd\
cd C:\Selenium\Libs
java -Dwebdriver.chrome.driver=C:\Selenium\Drivers\chromedriver.exe -jar selenium-server-standalone-3.141.59.jar -role node -hub http://localhost:8888/grid/register -browser platform=WINDOWS,browserName=chrome,version=node1,maxInstances=8 -maxSession 4 -port 5588

pause 50