echo "Pulling repo... "
git pull origin master
echo "Success!"

echo "Compiling repo... "
javac -cp /usr/local/share/java/zmq.jar:lib/gson-2.5.jar:. tankbattle/client/stub/Client.java
echo "Success!"

echo "Running project..."
java -Djava.library.path=/usr/local/lib -cp /usr/local/share/java/zmq.jar:lib/gson-2.5.jar:. tankbattle.client.stub.Client