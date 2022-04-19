kill -9 $(ps -ef | grep '[v]pdt-file-service-0.0.1-SNAPSHOT --spring.profiles.active=staging' | awk '{print $2}')
echo "Waiting 2s to kill running process..."

echo "starting service"
nohup java -jar /home/huyvpdt/file-service/vpdt-file-service-0.0.1-SNAPSHOT.jar --spring.profiles.active=staging &
echo "finished"
